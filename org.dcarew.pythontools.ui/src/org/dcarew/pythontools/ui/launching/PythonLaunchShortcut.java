package org.dcarew.pythontools.ui.launching;

import org.dcarew.pythontools.core.PythonCorePlugin;
import org.dcarew.pythontools.core.launching.PythonLaunchConfigWrapper;
import org.dcarew.pythontools.ui.PythonUIPlugin;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.ILaunchShortcut2;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorPart;

import java.util.ArrayList;
import java.util.List;

public class PythonLaunchShortcut implements ILaunchShortcut2 {

  public PythonLaunchShortcut() {

  }

  @Override
  public void launch(ISelection selection, String mode) {
    IResource resource = getLaunchableResource(selection);

    if (resource != null) {
      launch(resource, mode);
    }
  }

  @Override
  public void launch(IEditorPart editor, String mode) {
    IFile file = (IFile) editor.getEditorInput().getAdapter(IFile.class);

    if (file != null) {
      launch(new StructuredSelection(file), mode);
    }
  }

  @Override
  public ILaunchConfiguration[] getLaunchConfigurations(ISelection selection) {
    IResource resource = getLaunchableResource(selection);

    if (resource == null) {
      return null;
    }

    if (resource instanceof IFile && PythonCorePlugin.isPythonFile((IFile) resource)) {
      List<ILaunchConfiguration> results = new ArrayList<ILaunchConfiguration>();

      try {
        ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
        ILaunchConfigurationType type = manager.getLaunchConfigurationType(PythonCorePlugin.LAUNCH_CONFIG_ID);
        ILaunchConfiguration[] configs = manager.getLaunchConfigurations(type);

        for (int i = 0; i < configs.length; i++) {
          ILaunchConfiguration config = configs[i];

          if (testSimilar(resource, config)) {
            results.add(config);
          }
        }
      } catch (CoreException e) {
        PythonUIPlugin.logError(e);
      }

      return results.toArray(new ILaunchConfiguration[results.size()]);
    } else {
      return null;
    }
  }

  @Override
  public ILaunchConfiguration[] getLaunchConfigurations(IEditorPart editor) {
    IFile file = (IFile) editor.getEditorInput().getAdapter(IFile.class);

    if (file != null) {
      return getLaunchConfigurations(new StructuredSelection(file));
    } else {
      return null;
    }
  }

  @Override
  public IResource getLaunchableResource(ISelection selection) {
    if (selection instanceof IStructuredSelection) {
      Object object = ((IStructuredSelection) selection).getFirstElement();

      if (object instanceof IResource) {
        return (IResource) object;
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

  @Override
  public IResource getLaunchableResource(IEditorPart editor) {
    IFile file = (IFile) editor.getEditorInput().getAdapter(IFile.class);

    if (file != null) {
      return getLaunchableResource(new StructuredSelection(file));
    } else {
      return null;
    }
  }

  protected void launch(IResource resource, String mode) {
    if (resource == null) {
      return;
    }

    // Launch an existing configuration if one exists
    ILaunchConfiguration config = findConfig(resource);

    if (config == null) {
      // Create and launch a new configuration
      ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
      ILaunchConfigurationType type = manager.getLaunchConfigurationType(PythonCorePlugin.LAUNCH_CONFIG_ID);
      ILaunchConfigurationWorkingCopy launchConfig = null;

      try {
        launchConfig = type.newInstance(null,
            manager.generateLaunchConfigurationName(resource.getName()));
      } catch (CoreException ce) {
        PythonUIPlugin.logError(ce);
        return;
      }

      PythonLaunchConfigWrapper launchWrapper = new PythonLaunchConfigWrapper(launchConfig);

      launchWrapper.setResourcePath(resource.getFullPath().toString());

      try {
        config = launchConfig.doSave();
      } catch (CoreException e) {
        PythonUIPlugin.logError(e);
        return;
      }
    }

    DebugUITools.launch(config, mode);
  }

  protected ILaunchConfiguration findConfig(IResource resource) {
    ILaunchConfiguration[] configs = getLaunchConfigurations(new StructuredSelection(resource));

    if (configs == null) {
      return null;
    } else if (configs.length == 0) {
      return null;
    } else {
      return configs[0];
    }
  }

  private boolean testSimilar(IResource resource, ILaunchConfiguration config) {
    PythonLaunchConfigWrapper wrapper = new PythonLaunchConfigWrapper(config);

    return resource.equals(wrapper.getResource());
  }

}
