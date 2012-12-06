package org.dcarew.pythontools.core.debugger;

import org.dcarew.pythontools.core.PythonCorePlugin;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.LineBreakpoint;

public class PythonBreakpoint extends LineBreakpoint {
  private final static String MARKER_ID = "org.dcarew.pythontools.core.breakpointMarker";

  public PythonBreakpoint() {

  }

  public PythonBreakpoint(IResource resource, int lineNumber) throws CoreException {
    IMarker marker = resource.createMarker(MARKER_ID);

    marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
    marker.setAttribute(IBreakpoint.ID, getModelIdentifier());
    marker.setAttribute(IMarker.MESSAGE, "Python breakpoint, line " + lineNumber);

    setMarker(marker);
    setEnabled(true);
  }

  @Override
  public String getModelIdentifier() {
    return PythonCorePlugin.PLUGIN_ID;
  }

  public boolean isBreakpointEnabled() {
    try {
      return isEnabled();
    } catch (CoreException e) {
      return false;
    }
  }

  public IFile getFile() {
    return (IFile)getMarker().getResource();
  }

  public int getLine() {
    try {
      return getLineNumber();
    } catch (CoreException e) {
      return -1;
    }
  }

}
