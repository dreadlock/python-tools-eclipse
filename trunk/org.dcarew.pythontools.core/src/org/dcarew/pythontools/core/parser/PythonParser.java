package org.dcarew.pythontools.core.parser;

import org.dcarew.pythontools.core.PythonCorePlugin;
import org.dcarew.pythontools.core.utils.ProcessRunner;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;

import java.io.File;
import java.io.IOException;
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
          return PyModule.parseJson(runner.getStdout());
        }
      } else {
        ProcessRunner runner = new ProcessRunner(new File("."), pythonPath, getPyParsePath(),
            file.getLocation().toOSString());

        if (runner.execute() == 0) {
          return PyModule.parseJson(runner.getStdout());
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
}
