package org.dcarew.pythontools.core.launching;

import org.dcarew.pythontools.core.PythonCorePlugin;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;

public class PythonLaunchConfigWrapper {
  private static final String WORKING_DIRECTORY = "workingDirectory";
  private static final String RESOURCE_PATH = "resourcePath";
  
  private ILaunchConfiguration launchConfig;

  public PythonLaunchConfigWrapper(ILaunchConfiguration launchConfig) {
    this.launchConfig = launchConfig;
  }
  
  public String getResourcePath() {
    try {
      return launchConfig.getAttribute(RESOURCE_PATH, "");
    } catch (CoreException e) {
      PythonCorePlugin.logError(e);

      return "";
    }
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
    try {
      return launchConfig.getAttribute(WORKING_DIRECTORY, "");
    } catch (CoreException e) {
      PythonCorePlugin.logError(e);

      return "";
    }
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

}
