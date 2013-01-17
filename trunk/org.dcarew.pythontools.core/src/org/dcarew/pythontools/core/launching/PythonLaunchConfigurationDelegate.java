package org.dcarew.pythontools.core.launching;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dcarew.pythontools.core.PythonCorePlugin;
import org.dcarew.pythontools.core.debugger.PyDebugTarget;
import org.dcarew.pythontools.core.debugger.PythonDebugger;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;

public class PythonLaunchConfigurationDelegate extends LaunchConfigurationDelegate {

  public PythonLaunchConfigurationDelegate() {

  }

  @Override
  public boolean buildForLaunch(ILaunchConfiguration configuration, String mode,
      IProgressMonitor monitor) {
    return false;
  }

  @Override
  public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch,
      IProgressMonitor monitor) throws CoreException {
    boolean enableDebugging = ILaunchManager.DEBUG_MODE.equals(mode);

    String pythonPath = PythonCorePlugin.getPlugin().getPythonPath();

    if (pythonPath == null) {
      throw newDebugException("No path for the python interpreter specified in the Preferences dialog.");
    }

    PythonLaunchConfigWrapper wrapper = new PythonLaunchConfigWrapper(configuration);
    IResource resource = wrapper.getResource();

    if (resource == null) {
      throw newDebugException("No file specified to launch");
    }

    File cwd = getCurrentWorkingDirectory(wrapper);
    String scriptPath = translateToFilePath(cwd, resource);

    List<String> commandsList = new ArrayList<String>();

    // "python [pydebug.py] script"
    commandsList.add(pythonPath);
    commandsList.addAll(Arrays.asList(wrapper.getInterpreterArgumentsAsArray()));
    if (enableDebugging) {
      commandsList.add(PythonDebugger.getPyDebugPath());
    }
    commandsList.add(scriptPath);
    commandsList.addAll(Arrays.asList(wrapper.getScriptArgumentsAsArray()));

    String[] commands = commandsList.toArray(new String[commandsList.size()]);
    ProcessBuilder processBuilder = new ProcessBuilder(commands);

    if (cwd != null) {
      processBuilder.directory(cwd);
    }

    Process runtimeProcess = null;

    try {
      runtimeProcess = processBuilder.start();
    } catch (IOException ioe) {
      throw newDebugException(ioe);
    }

    IProcess eclipseProcess = null;

    Map<String, String> processAttributes = new HashMap<String, String>();

    processAttributes.put(IProcess.ATTR_PROCESS_TYPE, "python");

    if (runtimeProcess != null) {
      monitor.beginTask("Python", IProgressMonitor.UNKNOWN);

      eclipseProcess = DebugPlugin.newProcess(launch, runtimeProcess, configuration.getName(),
          processAttributes);
    }

    if (runtimeProcess == null || eclipseProcess == null) {
      if (runtimeProcess != null) {
        runtimeProcess.destroy();
      }

      throw newDebugException("Error starting the Python interpreter");
    }

    //eclipseProcess.setAttribute(IProcess.ATTR_CMDLINE, generateCommandLine(commands));

    if (enableDebugging) {
      PyDebugTarget target = new PyDebugTarget(eclipseProcess);

      try {
        target.connect(launch, PythonDebugger.DEFAULT_PORT);
      } catch (DebugException ex) {
        // We don't throw an exception if the process died before we could connect.
        if (!isProcessDead(runtimeProcess)) {
          throw ex;
        }
      }
    }

    monitor.done();
  }

  @Override
  public boolean preLaunchCheck(ILaunchConfiguration configuration, String mode,
      IProgressMonitor monitor) throws CoreException {
    return saveBeforeLaunch(configuration, mode, monitor);
  }

  @Override
  protected IProject[] getBuildOrder(ILaunchConfiguration configuration, String mode)
      throws CoreException {
    PythonLaunchConfigWrapper launchConfig = new PythonLaunchConfigWrapper(configuration);

    IResource resource = launchConfig.getResource();

    if (resource != null) {
      return new IProject[] {resource.getProject()};
    }

    return null;
  }

  @Override
  protected IProject[] getProjectsForProblemSearch(ILaunchConfiguration configuration, String mode)
      throws CoreException {
    return getBuildOrder(configuration, mode);
  }

  private File getCurrentWorkingDirectory(PythonLaunchConfigWrapper wrapper) {
    if (wrapper.getWorkingDirectory().length() > 0) {
      String cwd = wrapper.getWorkingDirectory();

      return new File(cwd);
    } else {
      IResource resource = wrapper.getResource();

      return resource.getParent().getLocation().toFile();
    }
  }

  private boolean isProcessDead(Process process) {
    try {
      process.exitValue();

      return true;
    } catch (IllegalThreadStateException ex) {
      return false;
    }
  }

  private DebugException newDebugException(String message) {
    return new DebugException(new Status(IStatus.ERROR, PythonCorePlugin.PLUGIN_ID, message));
  }

  private DebugException newDebugException(Throwable t) {
    return new DebugException(
        new Status(IStatus.ERROR, PythonCorePlugin.PLUGIN_ID, t.toString(), t));
  }

  private String translateToFilePath(File cwd, IResource resource) {
    String path = resource.getLocation().toFile().getAbsolutePath();

    if (cwd != null) {
      String cwdPath = cwd.getAbsolutePath();

      if (!cwdPath.endsWith(File.separator)) {
        cwdPath = cwdPath + File.separator;
      }

      if (path.startsWith(cwdPath)) {
        path = path.substring(cwdPath.length());
      }
    }

    return path;
  }

}
