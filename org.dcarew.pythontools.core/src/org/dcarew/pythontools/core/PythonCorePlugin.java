package org.dcarew.pythontools.core;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

public class PythonCorePlugin extends Plugin {
  // The plug-in ID
  public static final String PLUGIN_ID = "org.dcarew.pythontools.core"; //$NON-NLS-1$

  // The shared instance
  private static PythonCorePlugin plugin;


  public void start(BundleContext context) throws Exception {
    super.start(context);
    
    plugin = this;
  }

  public void stop(BundleContext context) throws Exception {
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

}
