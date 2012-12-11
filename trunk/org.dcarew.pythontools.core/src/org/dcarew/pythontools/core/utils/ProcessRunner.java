package org.dcarew.pythontools.core.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;

import org.dcarew.pythontools.core.PythonCorePlugin;

public class ProcessRunner {
  private ProcessBuilder builder;
  private StringBuilder stdout = new StringBuilder();

  public ProcessRunner(File cwd, String... args) {
    builder = new ProcessBuilder(args);
    builder.directory(cwd);
    builder.redirectErrorStream(true);
  }

  public int execute() throws IOException {
    return execute(null);
  }

  public int execute(String stdinInput) throws IOException {
    stdout.setLength(0);

    final Process process = builder.start();

    // Read from stdout.
    final Thread stdoutThread = new Thread(new Runnable() {
      @Override
      public void run() {
        pipeOutput(process.getInputStream(), stdout);
      }
    });

    stdoutThread.start();

    if (stdinInput != null) {
      byte[] data = stdinInput.getBytes(Charset.forName("UTF-8"));
      process.getOutputStream().write(data);
      process.getOutputStream().close();
    }

    try {
      return process.waitFor();
    } catch (InterruptedException e) {
      return 0;
    }
  }

  public List<String> getCommands() {
    return builder.command();
  }

  public String getStdout() {
    return stdout.toString();
  }

  protected void pipeOutput(InputStream in, StringBuilder builder) {
    try {
      Reader reader = new InputStreamReader(in, "UTF-8");
      char[] buffer = new char[512];

      int count = reader.read(buffer);

      while (count != -1) {
        builder.append(buffer, 0, count);

        count = reader.read(buffer);
      }
    } catch (UnsupportedEncodingException e) {
      PythonCorePlugin.logError(e);
    } catch (IOException e) {
      // This exception is expected.

    }
  }
}
