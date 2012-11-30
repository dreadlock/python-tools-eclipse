package org.dcarew.pythontools.ui.editors;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.MonoReconciler;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;

public class PythonEditorSourceViewerConfiguration extends TextSourceViewerConfiguration {
  private PythonEditor editor;
  private MonoReconciler reconciler;
  
  public PythonEditorSourceViewerConfiguration(PythonEditor editor, IPreferenceStore preferenceStore) {
    super(preferenceStore);

    this.editor = editor;
  }

  @Override
  public IReconciler getReconciler(ISourceViewer sourceViewer) {
    if (reconciler == null && sourceViewer != null) {
      reconciler = new MonoReconciler(new PythonEditorReconcilingStrategy(editor), false);
      reconciler.setDelay(500);
    }

    return reconciler;
  }

}
