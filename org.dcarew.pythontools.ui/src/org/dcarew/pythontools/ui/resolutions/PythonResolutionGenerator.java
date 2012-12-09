package org.dcarew.pythontools.ui.resolutions;

import java.util.ArrayList;
import java.util.List;

import org.dcarew.pythontools.core.PythonCorePlugin;
import org.dcarew.pythontools.core.builder.MarkerUtils;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator2;

public class PythonResolutionGenerator implements IMarkerResolutionGenerator2 {

  public PythonResolutionGenerator() {

  }

  @Override
  public IMarkerResolution[] getResolutions(IMarker marker) {
    List<IMarkerResolution> resolutions = new ArrayList<IMarkerResolution>();

    String code = MarkerUtils.getErrorCode(marker);

    if (code != null) {
      resolutions.add(new IgnoreMarkerResolution(marker));

      // TODO: implement
      //resolutions.add(new DisableRuleResolution(marker));
    }

    return resolutions.toArray(new IMarkerResolution[resolutions.size()]);
  }

  @Override
  public boolean hasResolutions(IMarker marker) {
    try {
      String type = marker.getType();

      if (!type.equals(PythonCorePlugin.MARKER_ID)) {
        return false;
      }
    } catch (CoreException e) {
      return false;
    }

    return MarkerUtils.getErrorCode(marker) != null;
  }

}
