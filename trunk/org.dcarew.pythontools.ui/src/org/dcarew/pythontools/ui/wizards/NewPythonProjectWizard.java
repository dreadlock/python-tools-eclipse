package org.dcarew.pythontools.ui.wizards;

import org.dcarew.pythontools.core.builder.PythonNature;
import org.dcarew.pythontools.ui.PythonUIPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

public class NewPythonProjectWizard extends BasicNewProjectResourceWizard {

  public NewPythonProjectWizard() {

  }

  @Override
  public void addPages() {
    super.addPages();

    IWizardPage page = getPages()[0];

    page.setTitle("New Python Project");
    page.setDescription("Create a new Python project");
    // TODO:
    //page.setImageDescriptor(sdfsdf);
  }

  @Override
  public boolean performFinish() {
    if (super.performFinish()) {
      try {
        PythonNature.addToProject(getNewProject());
      } catch (CoreException e) {
        PythonUIPlugin.logError(e);
      }

      return true;
    } else {
      return false;
    }
  }

}
