package org.dcarew.pythontools.ui.launching;

import org.dcarew.pythontools.core.launching.PythonLaunchConfigWrapper;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class PythonLaunchConfigurationTab extends AbstractLaunchConfigurationTab {

  public PythonLaunchConfigurationTab() {

  }

  @Override
  public void createControl(Composite parent) {
    Composite composite = new Composite(parent, SWT.NONE);
    GridLayoutFactory.fillDefaults().applyTo(composite);
    setControl(composite);
    
    // TODO: file path
    
    // TODO: python intepreter args
    
    // TODO: script args
    
  }

  @Override
  public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
    PythonLaunchConfigWrapper wrapper = new PythonLaunchConfigWrapper(configuration);
    
    
  }

  @Override
  public void initializeFrom(ILaunchConfiguration configuration) {
    PythonLaunchConfigWrapper wrapper = new PythonLaunchConfigWrapper(configuration);

    
  }

  @Override
  public String getMessage() {
    return "Configure a Python script to run.";
  }

  @Override
  public void performApply(ILaunchConfigurationWorkingCopy configuration) {
    // TODO Auto-generated method stub

  }

  @Override
  public String getName() {
    return "Main";
  }

}
