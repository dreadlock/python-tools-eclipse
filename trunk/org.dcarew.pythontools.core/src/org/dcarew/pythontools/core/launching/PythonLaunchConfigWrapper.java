package org.dcarew.pythontools.core.launching;

import org.dcarew.pythontools.core.PythonCorePlugin;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;

import java.util.ArrayList;
import java.util.List;

public class PythonLaunchConfigWrapper {
  private static final String WORKING_DIRECTORY = "workingDirectory";
  private static final String RESOURCE_PATH = "resourcePath";
  private static final String SCRIPT_ARGUMENTS = "scriptArguments";
  private static final String INTERPRETER_ARGUMENTS = "interpreterArguments";

  private ILaunchConfiguration launchConfig;

  public PythonLaunchConfigWrapper(ILaunchConfiguration launchConfig) {
    this.launchConfig = launchConfig;
  }

  public String getResourcePath() {
    return getStringValue(RESOURCE_PATH);
  }

  public IResource getResource() {
    String path = getResourcePath();

    if (path == null || path.length() == 0) {
      return null;
    } else {
      return ResourcesPlugin.getWorkspace().getRoot().findMember(getResourcePath());
    }
  }

  public void setResourcePath(String value) {
    getWorkingCopy().setAttribute(RESOURCE_PATH, value);

    updateMappedResources(value);
  }

  public String getWorkingDirectory() {
    return getStringValue(WORKING_DIRECTORY);
  }

  public void setWorkingDirectory(String value) {
    getWorkingCopy().setAttribute(WORKING_DIRECTORY, value);
  }

  protected ILaunchConfigurationWorkingCopy getWorkingCopy() {
    return (ILaunchConfigurationWorkingCopy) launchConfig;
  }

  private void updateMappedResources(String resourcePath) {
    IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(resourcePath);

    if (resource != null && !(resource instanceof IWorkspaceRoot)) {
      getWorkingCopy().setMappedResources(new IResource[] {resource});
    } else {
      getWorkingCopy().setMappedResources(null);
    }
  }

  public String getScriptArguments() {
    return getStringValue(SCRIPT_ARGUMENTS);
  }
  
  public String[] getScriptArgumentsAsArray() {
    return parseArgumentString(getScriptArguments());
  }
  
  public String getInterpreterArguments() {
    return getStringValue(INTERPRETER_ARGUMENTS);
  }

  public String[] getInterpreterArgumentsAsArray() {
    return parseArgumentString(getInterpreterArguments());
  }
  
  protected String getStringValue(String key) {
    try {
      return launchConfig.getAttribute(key, "");
    } catch (CoreException e) {
      PythonCorePlugin.logError(e);

      return "";
    }
  }

  public void setInterpreterArguments(String value) {
    getWorkingCopy().setAttribute(INTERPRETER_ARGUMENTS, value);
  }

  public void setScriptArguments(String value) {
    getWorkingCopy().setAttribute(SCRIPT_ARGUMENTS, value);
  }

  private static String[] parseArgumentString(String command) {
    List<String> args = new ArrayList<String>();

    StringBuilder builder = new StringBuilder();
    boolean inQuote = false;
    boolean prevWasSlash = false;

    for (final char c : command.toCharArray()) {
      if (!prevWasSlash && (c == '\'' || c == '"')) {
        inQuote = !inQuote;
        prevWasSlash = false;

        // Don't include the quote char.
        continue;
      }

      if (c == ' ' && !inQuote) {
        args.add(builder.toString());
        builder.setLength(0);
      } else {
        builder.append(c);
      }

      prevWasSlash = c == '\\';
    }

    if (builder.length() > 0) {
      args.add(builder.toString());
    }

    return args.toArray(new String[args.size()]);
  }

}
