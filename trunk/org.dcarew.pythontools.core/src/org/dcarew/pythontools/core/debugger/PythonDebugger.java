package org.dcarew.pythontools.core.debugger;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.dcarew.pythontools.core.PythonCorePlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;

public class PythonDebugger {
  public static final int DEFAULT_PORT = 9200;

  public static String getPyDebugPath() {
    try {
      URL bundleURL = FileLocator.find(PythonCorePlugin.getPlugin().getBundle(), new Path(
          "scripts/pydebug.py"), null);

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
