package org.dcarew.pythontools.ui.editors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IRegion;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

public class PythonEditor extends TextEditor {
  private PythonEditorOutlinePage outlinePage;
  private EditorImageUpdater imageUpdater;
  
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

  @Override
  protected final void doSetInput(IEditorInput input) throws CoreException {
    super.doSetInput(input);

    if (input instanceof IFileEditorInput) {
      imageUpdater = new EditorImageUpdater(this);
    }
  }

  @Override
  public void dispose() {
    if (imageUpdater != null) {
      imageUpdater.dispose();
    }

    super.dispose();
  }

  @Override
  protected void setTitleImage(Image image) {
    super.setTitleImage(image);
  }
  
  protected void handleReconcilation(IRegion partition) {
    if (outlinePage != null) {
      outlinePage.handleEditorReconcilation();
    }
  }

}
