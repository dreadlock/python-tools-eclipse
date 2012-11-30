package org.dcarew.pythontools.ui.editors;

import org.eclipse.jface.text.IRegion;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

public class PythonEditor extends TextEditor {
  private PythonEditorOutlinePage outlinePage;
  
  public PythonEditor() {
    setSourceViewerConfiguration(new PythonEditorSourceViewerConfiguration(this, getPreferenceStore()));

    //setKeyBindingScopes(new String[] {"com.googlecode.goclipse.editor"});
  }
  
  @SuppressWarnings("rawtypes")
  @Override
  public Object getAdapter(Class required) {
    if (IContentOutlinePage.class.equals(required)) {
      if (outlinePage == null) {
        outlinePage = new PythonEditorOutlinePage(this);
      }

      return outlinePage;
    }

    return super.getAdapter(required);
  }

  protected void handleReconcilation(IRegion partition) {
    if (outlinePage != null) {
      outlinePage.handleEditorReconcilation();
    }
  }

}
