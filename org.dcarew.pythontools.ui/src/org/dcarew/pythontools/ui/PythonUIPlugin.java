package org.dcarew.pythontools.ui;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class PythonUIPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.dcarew.pythontools.ui"; //$NON-NLS-1$

	// The shared instance
	private static PythonUIPlugin plugin;
	
	/**
	 * The constructor
	 */
	public PythonUIPlugin() {
	  
	}

	@Override
  public void start(BundleContext context) throws Exception {
		super.start(context);
		
		plugin = this;
	}

	@Override
  public void stop(BundleContext context) throws Exception {
		plugin = null;
		
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static PythonUIPlugin getPlugin() {
		return plugin;
	}
	
	public static void logError(Throwable exception) {
    if (getPlugin() != null) {
      getPlugin().getLog().log(
          new Status(IStatus.ERROR, PLUGIN_ID, exception.getMessage(), exception));
    }
  }
	
}
