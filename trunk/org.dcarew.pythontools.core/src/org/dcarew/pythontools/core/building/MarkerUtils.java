package org.dcarew.pythontools.core.building;

import org.dcarew.pythontools.core.PythonCorePlugin;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;

public class MarkerUtils {

  private MarkerUtils() {
    
  }

  public static void createMarker(int severity, IFile file, int line, String message) {
    try {
      IMarker marker = file.createMarker(PythonCorePlugin.MARKER_ID);
      marker.setAttribute(IMarker.SEVERITY, severity);
      marker.setAttribute(IMarker.LINE_NUMBER, line);
      marker.setAttribute(IMarker.MESSAGE, message);
    } catch (CoreException e) {
      
    }
  }

  public static void clearMarkers(IFile file) {
    try {
      file.deleteMarkers(PythonCorePlugin.MARKER_ID, true, 0);
    } catch (CoreException e) {
      
    }
  }
  
}
