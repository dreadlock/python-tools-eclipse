package org.dcarew.pythontools.ui.resolutions;

import org.dcarew.pythontools.core.builder.MarkerUtils;
import org.dcarew.pythontools.ui.PythonUIPlugin;
import org.eclipse.core.resources.IMarker;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IMarkerResolution2;

class DisableRuleResolution implements IMarkerResolution2 {
  private String code;

  DisableRuleResolution(IMarker marker) {
    code = MarkerUtils.getErrorCode(marker);
  }

  @Override
  public String getLabel() {
    return "Disable the " + code + " rule entirely";
  }

  @Override
  public void run(IMarker marker) {
    // TODO:

  }

  @Override
  public String getDescription() {
    return null;
  }

  @Override
  public Image getImage() {
    // TODO: customize this icon

    return PythonUIPlugin.getImage("icons/python_16.png");
  }

}
