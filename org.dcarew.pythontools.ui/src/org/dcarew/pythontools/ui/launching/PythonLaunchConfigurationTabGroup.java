package org.dcarew.pythontools.ui.launching;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

public class PythonLaunchConfigurationTabGroup extends AbstractLaunchConfigurationTabGroup {

  public PythonLaunchConfigurationTabGroup() {

  }

  @Override
  public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
    setTabs(new ILaunchConfigurationTab[] {new PythonLaunchConfigurationTab(), new CommonTab()});
  }

}
