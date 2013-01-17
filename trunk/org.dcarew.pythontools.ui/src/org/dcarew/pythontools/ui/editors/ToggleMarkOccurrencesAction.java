package org.dcarew.pythontools.ui.editors;

import org.dcarew.pythontools.ui.PythonUIPlugin;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.TextEditorAction;

/**
 * A toolbar action which toggles the PythonEditor#EDITOR_MARK_OCCURRENCES mark occurrences
 * preference}.
 */
public class ToggleMarkOccurrencesAction extends TextEditorAction implements
    IPropertyChangeListener {

  private IPreferenceStore fStore;

  public ToggleMarkOccurrencesAction() {
    super(PythonEditor.editorResourceBundle, "ToggleMarkOccurrencesAction.", null,
        IAction.AS_CHECK_BOX);

    setActionDefinitionId(IPythonEditorActionDefinitionIds.TOGGLE_MARK_OCCURRENCES);
    setImageDescriptor(PythonUIPlugin.getImageDescriptor("icons/obj16/mark_occurrences.gif"));

    update();
  }

  @Override
  public void propertyChange(PropertyChangeEvent event) {
    if (event.getProperty().equals(PythonEditor.EDITOR_MARK_OCCURRENCES)) {
      setChecked(Boolean.valueOf(event.getNewValue().toString()).booleanValue());
    }
  }

  @Override
  public void run() {
    fStore.setValue(PythonEditor.EDITOR_MARK_OCCURRENCES, isChecked());
  }

  @Override
  public void setEditor(ITextEditor editor) {
    super.setEditor(editor);

    if (editor != null) {
      if (fStore == null) {
        fStore = PythonUIPlugin.getPlugin().getPreferenceStore();
        fStore.addPropertyChangeListener(this);
      }
    } else if (fStore != null) {
      fStore.removePropertyChangeListener(this);
      fStore = null;
    }

    update();
  }

  @Override
  public void update() {
    ITextEditor editor = getTextEditor();

    boolean checked = false;

    if (editor instanceof PythonEditor) {
      checked = ((PythonEditor) editor).isMarkingOccurrences();
    }

    setChecked(checked);
    setEnabled(editor != null);
  }

}
