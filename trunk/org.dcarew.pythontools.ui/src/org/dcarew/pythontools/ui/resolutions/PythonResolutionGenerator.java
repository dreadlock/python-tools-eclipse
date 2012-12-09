package org.dcarew.pythontools.ui.resolutions;

import org.dcarew.pythontools.core.builder.MarkerUtils;
import org.eclipse.core.resources.IMarker;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator;

import java.util.ArrayList;
import java.util.List;

public class PythonResolutionGenerator implements IMarkerResolutionGenerator {

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

}
