package org.dcarew.pythontools.core;

import org.dcarew.pythontools.core.builder.PythonNature;
import org.dcarew.pythontools.core.builder.PythonResourceChangeBuilder;
import org.dcarew.pythontools.core.pylint.IPylintConfig;
import org.dcarew.pythontools.core.pylint.PylintConfigManager;
import org.dcarew.pythontools.core.utils.PythonLocator;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.framework.BundleContext;
import org.osgi.service.prefs.BackingStoreException;

public class PythonCorePlugin extends Plugin {
  // The plug-in ID
  public static final String PLUGIN_ID = "org.dcarew.pythontools.core"; //$NON-NLS-1$

  public static final String LAUNCH_CONFIG_ID = "org.dcarew.pythontools.launchConfig";

  public static final String MARKER_ID = "org.dcarew.pythontools.core.pythonMarker";

  public static final String PYLINT_CONFIG_PREF = "pylintConfigPref";

  // The shared instance
  private static PythonCorePlugin plugin;

  /**
   * Returns the shared instance
   * 
   * @return the shared instance
   */
  public static PythonCorePlugin getPlugin() {
    return plugin;
  }

  public static boolean isPythonFile(IFile file) {
    return "py".equals(file.getFileExtension());
  }

  public static void logError(Throwable exception) {
    if (getPlugin() != null) {
      getPlugin().getLog().log(
          new Status(IStatus.ERROR, PLUGIN_ID, exception.getMessage(), exception));
    }
  }

  private IPylintConfig pylintConfig;

  private IEclipsePreferences prefs;

  public void applyProjectSettings(IProject project, boolean analyzeProject,
      boolean overrideWorkspaceSettings, IPylintConfig config) throws CoreException {
    IEclipsePreferences projPrefs = getProjectPreferences(project);

    boolean changed = setPref(projPrefs, "override", overrideWorkspaceSettings);

    changed |= setPref(projPrefs, PYLINT_CONFIG_PREF, config == null ? "" : config.getName());

    if (PythonNature.isPythonProject(project)) {
      changed |= setPref(projPrefs, "analysisDisabled", analyzeProject);
    } else {
      if (analyzeProject && !PythonNature.hasPythonBuilder(project)) {
        PythonNature.addBuilderToProject(project);

        // adding the builder will rebuild the project
        changed = false;
      } else if (!analyzeProject && PythonNature.hasPythonBuilder(project)) {
        PythonNature.removeBuilderFromProject(project);

        changed = true;
      }
    }

    try {
      projPrefs.flush();
    } catch (BackingStoreException e) {

    }

    // TODO: if changed, re-build the project

  }

  public IEclipsePreferences getPreferences() {
    if (prefs == null) {
      prefs = InstanceScope.INSTANCE.getNode(PLUGIN_ID);
    }

    return prefs;
  }

  public IEclipsePreferences getProjectPreferences(IProject project) {
    IScopeContext projectScope = new ProjectScope(project);
    return projectScope.getNode(PLUGIN_ID);
  }

  public IPylintConfig getPylintConfig() {
    if (pylintConfig == null) {
      String name = getPreferences().get(PYLINT_CONFIG_PREF, null);

      pylintConfig = PylintConfigManager.getConfig(name);
    }

    return pylintConfig;
  }

  public IPylintConfig getPylintConfig(IProject project) {
    if (isProjectOverride(project)) {
      return PylintConfigManager.getConfig(getProjectPreferences(project).get(PYLINT_CONFIG_PREF,
          ""));
    }

    return getPylintConfig();
  }

  public String getPylintPath() {
    String path = getPreferences().get("pylint", null);

    if (path != null && path.length() == 0) {
      return null;
    } else {
      return path;
    }
  }

  public String getPythonPath() {
    String path = getPreferences().get("python", null);

    if (path != null && path.length() == 0) {
      return null;
    } else {
      return path;
    }
  }

  public boolean isAnalysisEnabled(IProject project) {
    if (PythonNature.isPythonProject(project)) {
      IScopeContext projectScope = new ProjectScope(project);
      IEclipsePreferences projectNode = projectScope.getNode(PLUGIN_ID);

      return !projectNode.getBoolean("analysisDisabled", false);
    } else {
      return PythonNature.hasPythonBuilder(project);
    }
  }

  public boolean isProjectOverride(IProject project) {
    return getProjectPreferences(project).getBoolean("override", false);
  }

  public void setPylintConfig(IPylintConfig pylintConfig) {
    this.pylintConfig = pylintConfig;

    String name = (pylintConfig == null ? "" : pylintConfig.getName());

    getPreferences().put(PYLINT_CONFIG_PREF, name);

    try {
      getPreferences().flush();
    } catch (BackingStoreException e) {

    }

    // TODO: sent out an event if there was a change

  }

  public void setPylintPath(String path) {
    try {
      getPreferences().put("pylint", path == null ? "" : path);
      getPreferences().flush();
    } catch (BackingStoreException e) {

    }
  }

  public void setPythonPath(String path) {
    try {
      getPreferences().put("python", path == null ? "" : path);
      getPreferences().flush();
    } catch (BackingStoreException e) {

    }
  }

  @Override
  public void start(BundleContext context) throws Exception {
    super.start(context);

    plugin = this;

    // If we haven't run before, set up the defaults for Python.
    if (!getPreferences().getBoolean("inited", false)) {
      initPython();
    }

    // This is really not awesomely stable.
    ResourcesPlugin.getWorkspace().addResourceChangeListener(
        PythonResourceChangeBuilder.getBuilder(), IResourceChangeEvent.POST_CHANGE); // PRE_BUILD
  }

  @Override
  public void stop(BundleContext context) throws Exception {
    ResourcesPlugin.getWorkspace().removeResourceChangeListener(
        PythonResourceChangeBuilder.getBuilder());

    plugin = null;

    super.stop(context);
  }

  private void initPython() {
    try {
      PythonLocator locator = new PythonLocator();

      String pythonPath = locator.getDefaultPythonPath();
      String pylintPath = locator.getDefaultPylintPath();
      IPylintConfig defaultConfig = PylintConfigManager.getDefaultConfig();

      getPreferences().putBoolean("inited", true);
      getPreferences().put("python", pythonPath == null ? "" : pythonPath);
      getPreferences().put("pylint", pylintPath == null ? "" : pylintPath);
      getPreferences().put(PYLINT_CONFIG_PREF, defaultConfig == null ? "" : defaultConfig.getName());

      getPreferences().flush();
    } catch (BackingStoreException e) {

    }
  }

  private boolean setPref(IEclipsePreferences prefs, String key, final boolean value) {
    if (value != prefs.getBoolean(key, false)) {
      prefs.putBoolean(key, value);
      return true;
    }

    return false;
  }

  private boolean setPref(IEclipsePreferences prefs, String key, final String value) {
    if (value != prefs.get(key, null)) {
      prefs.put(key, value);
      return true;
    }

    return false;
  }

}
