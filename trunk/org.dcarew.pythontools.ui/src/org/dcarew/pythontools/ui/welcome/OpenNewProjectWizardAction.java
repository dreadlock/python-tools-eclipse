package org.dcarew.pythontools.ui.welcome;

import org.dcarew.pythontools.ui.wizards.NewPythonProjectWizard;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.intro.IIntroPart;

/**
 * This action is invoked from the welcome page.
 */
public class OpenNewProjectWizardAction extends Action {

  public OpenNewProjectWizardAction() {

  }

  @Override
  public void run() {
    closeIntroPage();

    NewPythonProjectWizard wizard = new NewPythonProjectWizard();
    wizard.init(PlatformUI.getWorkbench(), new StructuredSelection());
    WizardDialog dialog = new WizardDialog(
        PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), wizard);
    dialog.open();
  }

  private void closeIntroPage() {
    IIntroPart introPart = PlatformUI.getWorkbench().getIntroManager().getIntro();

    if (introPart != null) {
      PlatformUI.getWorkbench().getIntroManager().closeIntro(introPart);
    }
  }

}
