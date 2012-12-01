package org.dcarew.pythontools.core;

import org.dcarew.pythontools.core.building.PythonBuilder;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

public class PythonCorePlugin extends Plugin {
  // The plug-in ID
  public static final String PLUGIN_ID = "org.dcarew.pythontools.core"; //$NON-NLS-1$

  public static final String LAUNCH_CONFIG_ID = "org.dcarew.pythontools.launchConfig";
  
  public static final String MARKER_ID = "org.dcarew.pythontools.core.pythonMarker";
  
  // The shared instance
  private static PythonCorePlugin plugin;

  public void start(BundleContext context) throws Exception {
    super.start(context);

    plugin = this;

    ResourcesPlugin.getWorkspace().addResourceChangeListener(PythonBuilder.getBuilder(),
        IResourceChangeEvent.PRE_BUILD);
  }

  public void stop(BundleContext context) throws Exception {
    ResourcesPlugin.getWorkspace().removeResourceChangeListener(PythonBuilder.getBuilder());
    
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

}
