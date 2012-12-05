package org.dcarew.pythontools.core;

import org.dcarew.pythontools.core.builder.PythonResourceChangeBuilder;
import org.dcarew.pythontools.core.utils.PythonLocator;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.framework.BundleContext;
import org.osgi.service.prefs.BackingStoreException;

public class PythonCorePlugin extends Plugin {
  // The plug-in ID
  public static final String PLUGIN_ID = "org.dcarew.pythontools.core"; //$NON-NLS-1$

  public static final String LAUNCH_CONFIG_ID = "org.dcarew.pythontools.launchConfig";

  public static final String MARKER_ID = "org.dcarew.pythontools.core.pythonMarker";

  // The shared instance
  private static PythonCorePlugin plugin;

  private IEclipsePreferences prefs;

  @Override
  public void start(BundleContext context) throws Exception {
    super.start(context);

    plugin = this;

    // If we haven't run before, set up the defaults for Python.
    if (!getPreferences().getBoolean("inited", false)) {
      initPython();
    }
    
    // This is really not awesomely stable.
    ResourcesPlugin.getWorkspace().addResourceChangeListener(PythonResourceChangeBuilder.getBuilder(),
        IResourceChangeEvent.POST_CHANGE); // PRE_BUILD
  }

  private void initPython() {
    try {
      PythonLocator locator = new PythonLocator();
      
      String pythonPath = locator.getDefaultPythonPath();
      String pylintPath = locator.getDefaultPylintPath();
      
      getPreferences().putBoolean("inited", true);
      getPreferences().put("python", pythonPath == null ? "" : pythonPath);
      getPreferences().put("pylint", pylintPath == null ? "" : pylintPath);
      
      getPreferences().flush();
    } catch (BackingStoreException e) {

    }
  }

  @Override
  public void stop(BundleContext context) throws Exception {
    ResourcesPlugin.getWorkspace().removeResourceChangeListener(PythonResourceChangeBuilder.getBuilder());

    plugin = null;

    super.stop(context);
  }

  /**
   * Returns the shared instance
   * 
   * @return the shared instance
   */
  public static PythonCorePlugin getPlugin() {
    return plugin;
  }

  public static void logError(Throwable exception) {
    if (getPlugin() != null) {
      getPlugin().getLog().log(
          new Status(IStatus.ERROR, PLUGIN_ID, exception.getMessage(), exception));
    }
  }

  public static boolean isPythonFile(IFile file) {
    return "py".equals(file.getFileExtension());
  }
  
  public IEclipsePreferences getPreferences() {
    if (prefs == null) {
      prefs = InstanceScope.INSTANCE.getNode(PLUGIN_ID);
    }

    return prefs;
  }
  
  public String getPythonPath() {
    String path = getPreferences().get("python", null);
    
    if (path != null && path.length() == 0) {
      return null;
    } else {
      return path;
    }
  }
  
  public String getPylintPath() {
    String path = getPreferences().get("pylint", null);
    
    if (path != null && path.length() == 0) {
      return null;
    } else {
      return path;
    }
  }
  
  public void setPythonPath(String path) {
    try {
      getPreferences().put("python", path == null ? "" : path);
      getPreferences().flush();
    } catch (BackingStoreException e) {

    }
  }

  public void setPylintPath(String path) {
    try {
      getPreferences().put("pylint", path == null ? "" : path);
      getPreferences().flush();
    } catch (BackingStoreException e) {

    }
  }

}
