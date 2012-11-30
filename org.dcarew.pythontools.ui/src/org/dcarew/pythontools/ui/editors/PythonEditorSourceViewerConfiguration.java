package org.dcarew.pythontools.ui.editors;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.MonoReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;

public class PythonEditorSourceViewerConfiguration extends TextSourceViewerConfiguration {
  private PythonEditor editor;
  private MonoReconciler reconciler;
  private PythonScanner scanner;
  
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

  @Override
  public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
    return new String[] {IDocument.DEFAULT_CONTENT_TYPE, PythonPartitionScanner.PYTHON_COMMENT};
  }

  @Override
  public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
    PresentationReconciler reconciler = new PresentationReconciler();

    DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getScanner());
    reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
    reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

    DefaultDamagerRepairer ndr = new DefaultDamagerRepairer(getScanner());
    reconciler.setDamager(ndr, PythonPartitionScanner.PYTHON_COMMENT);
    reconciler.setRepairer(ndr, PythonPartitionScanner.PYTHON_COMMENT);

    return reconciler;
  }

  protected PythonScanner getScanner() {
    if (scanner == null) {
      scanner = new PythonScanner();
      scanner.setDefaultReturnToken(new Token(new TextAttribute(null)));
    }
    
    return scanner;
  }

}
