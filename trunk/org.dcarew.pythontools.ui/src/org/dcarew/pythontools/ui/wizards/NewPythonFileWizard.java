package org.dcarew.pythontools.ui.wizards;

import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.wizards.newresource.BasicNewFileResourceWizard;

public class NewPythonFileWizard extends BasicNewFileResourceWizard {

  public NewPythonFileWizard() {
    
  }

  @Override
  public void addPages() {
    super.addPages();
    
    WizardNewFileCreationPage page = (WizardNewFileCreationPage)getPages()[0];
    
    page.setTitle("New Python File");
    page.setDescription("Create a new Python file");
    // TODO:
    //page.setImageDescriptor(sdfsdf);
    
    page.setFileExtension("py");
  }
  
  
}
