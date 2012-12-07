package org.dcarew.pythontools.core.parser;

import org.dcarew.pythontools.core.PythonCorePlugin;
import org.dcarew.pythontools.core.utils.ProcessRunner;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;

public class PythonParser {
  private String contents;
  private IFile file;

  public PythonParser(String contents) {
    this.contents = contents;
  }

  public PythonParser(IFile file) {
    this.file = file;
  }

  public PyModule parse() {
    String pythonPath = PythonCorePlugin.getPlugin().getPythonPath();

    if (pythonPath == null) {
      return new PyModule();
    }

    try {
      if (contents != null) {
        ProcessRunner runner = new ProcessRunner(new File("."), pythonPath, getPyParsePath());

        if (runner.execute(contents) == 0) {
          LineMapper mapper = new LineMapper(contents);

          return PyModule.parseJson(mapper, runner.getStdout());
        }
      } else {
        // TODO: test this path
        ProcessRunner runner = new ProcessRunner(new File("."), pythonPath, getPyParsePath(),
            file.getLocation().toOSString());

        if (runner.execute() == 0) {
          LineMapper mapper = new LineMapper(getContents(file));

          return PyModule.parseJson(mapper, runner.getStdout());
        }
      }

    } catch (IOException ioe) {
      PythonCorePlugin.logError(ioe);
    }

    return new PyModule();
  }

  private String getPyParsePath() {
    try {
      URL bundleURL = FileLocator.find(PythonCorePlugin.getPlugin().getBundle(), new Path(
          "scripts/pyparse.py"), null);

      if (bundleURL != null) {
        URL fileURL = FileLocator.toFileURL(bundleURL);

        if (fileURL != null) {
          File file = new File(fileURL.toURI());

          return file.getAbsolutePath();
        }
      }
    } catch (IOException ioe) {

    } catch (URISyntaxException e) {

    }

    return null;
  }

  private String getContents(IFile file) throws IOException {
    try {
      InputStreamReader reader = new InputStreamReader(file.getContents(), file.getCharset());

      StringBuilder builder = new StringBuilder();
      char[] buffer = new char[4096];
      int count = reader.read(buffer);

      while (count != -1) {
        builder.append(buffer, 0, count);
        count = reader.read(buffer);
      }

      return builder.toString();
    } catch (CoreException ce) {
      throw new IOException(ce);
    }
  }

}
