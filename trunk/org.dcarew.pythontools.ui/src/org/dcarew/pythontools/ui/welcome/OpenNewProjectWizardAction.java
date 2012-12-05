package org.dcarew.pythontools.ui.welcome;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
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

    // TODO:

    MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
        "TODO:", "create a new Python project");
  }

  private void closeIntroPage() {
    IIntroPart introPart = PlatformUI.getWorkbench().getIntroManager().getIntro();

    if (introPart != null) {
      PlatformUI.getWorkbench().getIntroManager().closeIntro(introPart);
    }
  }

}
