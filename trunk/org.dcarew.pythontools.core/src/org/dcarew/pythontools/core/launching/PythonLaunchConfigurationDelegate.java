package org.dcarew.pythontools.core.launching;

import org.dcarew.pythontools.core.PythonCorePlugin;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PythonLaunchConfigurationDelegate extends LaunchConfigurationDelegate {

  public PythonLaunchConfigurationDelegate() {

  }

  @Override
  public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch,
      IProgressMonitor monitor) throws CoreException {
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

    commandsList.add(pythonPath);
    commandsList.addAll(Arrays.asList(wrapper.getInterpreterArgumentsAsArray()));
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

      throw newDebugException("Error starting Dart VM process");
    }

    //eclipseProcess.setAttribute(IProcess.ATTR_CMDLINE, generateCommandLine(commands));

//    if (enableDebugging && !DartCore.isWindows()) {
//      ServerDebugTarget debugTarget = new ServerDebugTarget(launch, eclipseProcess, connectionPort);
//
//      try {
//        debugTarget.connect();
//
//        launch.addDebugTarget(debugTarget);
//      } catch (DebugException ex) {
//        // We don't throw an exception if the process died before we could connect.
//        if (!isProcessDead(runtimeProcess)) {
//          throw ex;
//        }
//      }
//    }

    monitor.done();
  }

  private DebugException newDebugException(String message) {
    return new DebugException(new Status(IStatus.ERROR, PythonCorePlugin.PLUGIN_ID, message));
  }

  private DebugException newDebugException(Throwable t) {
    return new DebugException(
        new Status(IStatus.ERROR, PythonCorePlugin.PLUGIN_ID, t.toString(), t));
  }

  private File getCurrentWorkingDirectory(PythonLaunchConfigWrapper wrapper) {
    if (wrapper.getWorkingDirectory().length() > 0) {
      String cwd = wrapper.getWorkingDirectory();

      return new File(cwd);
    } else {
      IResource resource = wrapper.getResource();

      return resource.getProject().getLocation().toFile();
    }
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
