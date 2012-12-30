package org.dcarew.pythontools.ui.editors;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.MonoReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;
import org.eclipse.ui.texteditor.HippieProposalProcessor;

public class PythonEditorSourceViewerConfiguration extends TextSourceViewerConfiguration {
  private PythonEditor editor;
  private MonoReconciler reconciler;
  private PythonCodeScanner scanner;
  private PythonCommentScanner commentScanner;
  private PythonStringScanner stringScanner;

  public PythonEditorSourceViewerConfiguration(PythonEditor editor, IPreferenceStore preferenceStore) {
    super(preferenceStore);

    this.editor = editor;
  }

  @Override
  public IAutoEditStrategy[] getAutoEditStrategies(ISourceViewer sourceViewer, String contentType) {
    if (IDocument.DEFAULT_CONTENT_TYPE.equals(contentType)) {
      return new IAutoEditStrategy[] {new PythonAutoIndentStrategy(editor)};
    } else {
      return super.getAutoEditStrategies(sourceViewer, contentType);
    }
  }

  @Override
  public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
    return new String[] {
        IDocument.DEFAULT_CONTENT_TYPE, PythonPartitionScanner.PYTHON_COMMENT,
        PythonPartitionScanner.PYTHON_STRING};
  }

  @Override
  public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
    ContentAssistant assistant = new ContentAssistant();

    // TODO: have the PythonContentAssistProcessor use info from the hippie
    // completer. Only use suggestions from .py files.
    assistant.setContentAssistProcessor(new HippieProposalProcessor(),
        IDocument.DEFAULT_CONTENT_TYPE);

    return assistant;
  }

  @Override
  public IHyperlinkDetector[] getHyperlinkDetectors(ISourceViewer sourceViewer) {
    if (sourceViewer == null) {
      return null;
    }

    return new IHyperlinkDetector[] {new PythonHyperlinkDetector(editor)};
  }

  // TODO: set the indent from the current style
//  @Override
//  public String[] getIndentPrefixes(ISourceViewer sourceViewer, String contentType) {
//    return new String[] {"  ", ""};
//  }

  @Override
  public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
    PresentationReconciler reconciler = new PresentationReconciler();

    // TODO: keywords don't always get correctly damaged (i.e., type Nonee)
    DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getCodeScanner());
    reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
    reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

    dr = new DefaultDamagerRepairer(getCommentScanner());
    reconciler.setDamager(dr, PythonPartitionScanner.PYTHON_COMMENT);
    reconciler.setRepairer(dr, PythonPartitionScanner.PYTHON_COMMENT);

    dr = new DefaultDamagerRepairer(getStringScanner());
    reconciler.setDamager(dr, PythonPartitionScanner.PYTHON_STRING);
    reconciler.setRepairer(dr, PythonPartitionScanner.PYTHON_STRING);

    return reconciler;
  }

  @Override
  public IReconciler getReconciler(ISourceViewer sourceViewer) {
    if (reconciler == null && sourceViewer != null) {
      reconciler = new MonoReconciler(new PythonEditorReconcilingStrategy(editor), false);
      reconciler.setDelay(500);
    }

    return reconciler;
  }

  protected PythonCodeScanner getCodeScanner() {
    if (scanner == null) {
      scanner = new PythonCodeScanner();
      scanner.setDefaultReturnToken(new Token(new TextAttribute(null)));
    }

    return scanner;
  }

  protected PythonCommentScanner getCommentScanner() {
    if (commentScanner == null) {
      commentScanner = new PythonCommentScanner();
    }

    return commentScanner;
  }

  protected PythonStringScanner getStringScanner() {
    if (stringScanner == null) {
      stringScanner = new PythonStringScanner();
    }

    return stringScanner;
  }

}
