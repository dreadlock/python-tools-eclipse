package org.dcarew.pythontools.core.debugger;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A VM frame object.
 */
public class PyCallFrame {

  static List<PyCallFrame> createFrom(JSONArray arr) throws JSONException {
    List<PyCallFrame> frames = new ArrayList<PyCallFrame>();

    for (int i = 0; i < arr.length(); i++) {
      frames.add(createFrom(arr.getJSONObject(i)));
    }

    return frames;
  }

  private static PyCallFrame createFrom(JSONObject object) throws JSONException {
    PyCallFrame frame = new PyCallFrame();

    frame.functionName = object.optString("functionName");
    frame.libraryId = object.optInt("libraryId");
    frame.location = PyLocation.createFrom(object.getJSONObject("location"));
    frame.locals = PyVariable.createFrom(object.optJSONArray("locals"));

    return frame;
  }

  private String functionName;

  private int libraryId;

  private PyLocation location;

  private List<PyVariable> locals;

  private PyCallFrame() {

  }

  /**
   * Name of the Dart function called on this call frame.
   */
  public String getFunctionName() {
    return functionName;
  }

  public int getLibraryId() {
    return libraryId;
  }

  public List<PyVariable> getLocals() {
    return locals;
  }

  /**
   * Location in the source code.
   */
  public PyLocation getLocation() {
    return location;
  }

  public boolean isMain() {
    return "main".equals(functionName);
  }

  @Override
  public String toString() {
    return "[" + functionName + "]";
  }

}
