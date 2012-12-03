package org.dcarew.pythontools.ui.navigator;

import org.dcarew.pythontools.core.PythonCorePlugin;
import org.dcarew.pythontools.ui.PythonUIPlugin;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;

/**
 * A lightweight decorator for Python problem decorations.
 */
public class PythonLabelDecorator implements ILightweightLabelDecorator {

  public PythonLabelDecorator() {

  }

  @Override
  public void addListener(ILabelProviderListener listener) {

  }

  @Override
  public void removeListener(ILabelProviderListener listener) {

  }

  @Override
  public boolean isLabelProperty(Object element, String property) {
    return false;
  }

  @Override
  public void decorate(Object element, IDecoration decoration) {
    if (element instanceof IFile) {
      IFile file = (IFile) element;

      if (PythonCorePlugin.isPythonFile(file)) { // && isPythonProject(file.getProject())) {
        ImageDescriptor overlayImageDescriptor = getDecorationForResource(file);

        if (overlayImageDescriptor != null) {
          decoration.addOverlay(overlayImageDescriptor, IDecoration.BOTTOM_LEFT);
        }
      }
    } else if (element instanceof IContainer) {
      IContainer folder = (IContainer) element;

      ImageDescriptor overlayImageDescriptor = getDecorationForResource(folder);

      if (overlayImageDescriptor != null) {
        decoration.addOverlay(overlayImageDescriptor, IDecoration.BOTTOM_LEFT);
      }
    }
  }

  @Override
  public void dispose() {

  }

//	private boolean isPythonProject(IProject project) {
//		try {
//			return project.hasNature(PythonNature.NATURE_ID);
//		} catch (CoreException e) {
//			return false;
//		}
//	}

  private ImageDescriptor getDecorationForResource(IResource resource) {
    try {
      int severity = resource.findMaxProblemSeverity(IMarker.PROBLEM, true,
          IResource.DEPTH_INFINITE);

      if (severity == IMarker.SEVERITY_ERROR) {
        return PythonUIPlugin.getImageDescriptor("icons/error_co.gif");
      } else if (severity == IMarker.SEVERITY_ERROR) {
        return PythonUIPlugin.getImageDescriptor("icons/warning_co.gif");
      } else {
        return null;
      }
    } catch (CoreException ce) {
      // ignore

      return null;
    }
  }

}
