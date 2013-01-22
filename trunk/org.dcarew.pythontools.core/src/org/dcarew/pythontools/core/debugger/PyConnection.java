package org.dcarew.pythontools.core.debugger;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dcarew.pythontools.core.PythonCorePlugin;
import org.dcarew.pythontools.core.debugger.PyListener.PausedReason;
import org.json.JSONException;
import org.json.JSONObject;

public class PyConnection {

  static interface Callback {
    public void handleResult(JSONObject result) throws JSONException;
  }

  private static final String EVENT_PAUSED = "paused";

  private static Charset UTF8 = Charset.forName("UTF-8");

  private Socket socket;

  private OutputStream out;
  private int nextCommandId = 1;

  private Map<Integer, Callback> callbackMap = new HashMap<Integer, Callback>();

  private List<PyListener> listeners = new ArrayList<PyListener>();

  public PyConnection() {

  }

  public void addListener(PyListener listener) {
    listeners.add(listener);
  }

  public void connect(int port) throws IOException {
    socket = new Socket((String) null, port);

    out = socket.getOutputStream();
    final InputStream in = socket.getInputStream();

    // Start a reader thread.
    new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          processVmEvents(in);
        } catch (EOFException e) {

        } catch (SocketException se) {
          // ignore java.net.SocketException: Connection reset
          final String reset = "Connection reset";

          if (!(se.getMessage() != null && se.getMessage().contains(reset))) {
            PythonCorePlugin.logError(se);
          }
        } catch (IOException e) {
          PythonCorePlugin.logError(e);
        } finally {
          socket = null;
        }
      }
    }).start();
  }

  public void removeListener(PyListener listener) {
    listeners.remove(listener);
  }

  public void sendRunCommand() throws IOException {
    sendSimpleCommand("run");
  }

  public void sendVersionCommand(final PyCallback<String> callback) throws IOException {
    // sys.version
    sendSimpleCommand("version", new Callback() {
      @Override
      public void handleResult(JSONObject result) throws JSONException {
        callback.handleResult(createVersionResult(result));
      }
    });
  }

  protected void processJson(String str) {
    try {
      JSONObject result = new JSONObject(str.trim());

      if (result.has("id")) {
        processResponse(result);
      } else {
        processNotification(result);
      }
    } catch (Throwable exception) {
      PythonCorePlugin.logError(exception);
    }
  }

  protected void processVmEvents(InputStream in) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(in, UTF8));

    String line = reader.readLine();

    while (line != null) {
      if (PythonCorePlugin.LOGGING) {
        System.out.println("<== " + line);
      }

      processJson(line);

      line = reader.readLine();
    }
  }

  protected void sendSimpleCommand(String command) throws IOException {
    sendSimpleCommand(command, null);
  }

  protected void sendSimpleCommand(String command, Callback callback) throws IOException {
    try {
      JSONObject request = new JSONObject();

      request.put("command", command);

      sendRequest(request, callback);
    } catch (JSONException exception) {
      throw new IOException(exception);
    }
  }

  void sendRequest(JSONObject request, Callback callback) throws IOException {
    int id = 0;

    synchronized (this) {
      id = nextCommandId++;

      try {
        request.put("id", id);
      } catch (JSONException ex) {
        throw new IOException(ex);
      }

      if (callback != null) {
        callbackMap.put(id, callback);
      }
    }

    try {
      send(request.toString() + "\n");
    } catch (IOException ex) {
      if (callback != null) {
        synchronized (this) {
          callbackMap.remove(id);
        }
      }

      throw ex;
    }
  }

  private PyResult<String> createVersionResult(JSONObject obj) throws JSONException {
    PyResult<String> result = PyResult.createFrom(obj);

    if (obj.has("result")) {
      result.setResult(obj.getString("result"));
    } else {
      result.setError("no version returned");
    }

    return result;
  }

  private void notifyDebuggerResumed() {
    for (PyListener listener : listeners) {
      listener.debuggerResumed();
    }
  }

  private void processNotification(JSONObject result) throws JSONException {
    if (result.has("event")) {
      String eventName = result.getString("event");
      JSONObject params = result.optJSONObject("params");

      if (eventName.equals(EVENT_PAUSED)) {
        // { "event": "paused", "params": { "callFrames" : [  { "functionName": "main" , "location": { "url": "file:///Users/devoncarew/tools/eclipse_37/eclipse/samples/time/time_server.dart", "lineNumber": 15 }}]}}

        String reason = params.optString("reason", null);

        PyValue exception = PyValue.createFrom(params.optJSONObject("exception"));
        List<PyCallFrame> frames = PyCallFrame.createFrom(params.getJSONArray("callFrames"));

        for (PyListener listener : listeners) {
          listener.debuggerPaused(PausedReason.parse(reason), frames, exception);
        }
      } else {
        PythonCorePlugin.logInfo("no handler for notification: " + eventName);
      }
    } else {
      PythonCorePlugin.logInfo("event not understood: " + result);
    }
  }

  private void processResponse(JSONObject result) throws JSONException {
    // Process a command response.
    int id = result.getInt("id");

    Callback callback;

    synchronized (this) {
      callback = callbackMap.remove(id);
    }

    if (callback != null) {
      callback.handleResult(result);
    } else if (result.has("error")) {
      // If we get an error back, and nobody was listening for the result, then log it.
      PyResult<?> vmResult = PyResult.createFrom(result);

      PythonCorePlugin.logInfo("Error from command id " + id + ": " + vmResult.getError());
    }
  }

  private void send(String str) throws IOException {
    if (PythonCorePlugin.LOGGING) {
      System.out.println("==> " + str.trim());
    }

    byte[] bytes = str.getBytes(UTF8);

    out.write(bytes);
    out.flush();
  }

}
