package org.dcarew.pythontools.core.pylint;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.dcarew.pythontools.core.PythonCorePlugin;
import org.dcarew.pythontools.core.utils.IOUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Path;

class ResourcePylintConfig implements IPylintConfig {
  private String name;
  private String resourcePath;

  private String indent;

  //private String pluginId;

  private boolean indentInited;

  public ResourcePylintConfig(IConfigurationElement element) {
    name = element.getAttribute("name");
    resourcePath = element.getAttribute("resource");
    //pluginId = element.getContributor().getName();
  }

  @Override
  public byte[] getContents() throws IOException {
    return IOUtils.getContentsAsBytes(getFile());
  }

  @Override
  public File getFile() {
    try {
      URL bundleURL = FileLocator.find(PythonCorePlugin.getPlugin().getBundle(), new Path(
          resourcePath), null);

      if (bundleURL != null) {
        URL fileURL = FileLocator.toFileURL(bundleURL);

        if (fileURL != null) {
          return new File(fileURL.toURI());
        }
      }

      return null;
    } catch (IOException ioe) {
      return null;
    } catch (URISyntaxException e) {
      return null;
    }
  }

  @Override
  public String getIndent() {
    if (!indentInited) {
      indentInited = true;

      try {
        indent = Utils.parseIdent(getContents());
      } catch (IOException e) {

      }
    }

    return indent;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean isModifyable() {
    return false;
  }

  @Override
  public String toString() {
    return "[" + getName() + "," + isModifyable() + "]";
  }

}
