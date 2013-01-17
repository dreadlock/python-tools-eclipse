package org.dcarew.pythontools.ui.editors;

import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.texteditor.BasicTextEditorActionContributor;
import org.eclipse.ui.texteditor.ITextEditor;

public class PythonEditorActionContributor extends BasicTextEditorActionContributor {
  private ToggleMarkOccurrencesAction toggleMarkOccurrencesAction;

  public PythonEditorActionContributor() {
    toggleMarkOccurrencesAction = new ToggleMarkOccurrencesAction();
  }

  @Override
  public void init(IActionBars bars, IWorkbenchPage page) {
    super.init(bars, page);

    bars.setGlobalActionHandler(IPythonEditorActionDefinitionIds.TOGGLE_MARK_OCCURRENCES,
        toggleMarkOccurrencesAction);
  }

  @Override
  public void setActiveEditor(IEditorPart part) {
    super.setActiveEditor(part);

    ITextEditor textEditor = null;

    if (part instanceof ITextEditor) {
      textEditor = (ITextEditor) part;
    }

    toggleMarkOccurrencesAction.setEditor(textEditor);
  }

}
