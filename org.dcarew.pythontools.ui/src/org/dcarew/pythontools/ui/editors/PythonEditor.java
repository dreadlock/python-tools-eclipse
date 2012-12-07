package org.dcarew.pythontools.ui.editors;

import org.dcarew.pythontools.core.parser.PyModule;
import org.dcarew.pythontools.core.parser.PyNode;
import org.dcarew.pythontools.core.parser.PythonParser;
import org.dcarew.pythontools.ui.PythonUIPlugin;
import org.dcarew.pythontools.ui.preferences.PreferenceConstants;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewerExtension;
import org.eclipse.jface.text.source.DefaultCharacterPairMatcher;
import org.eclipse.jface.text.source.ICharacterPairMatcher;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

public class PythonEditor extends TextEditor {
  private PythonEditorOutlinePage outlinePage;
  private EditorImageUpdater imageUpdater;
  private PyModule model;

  private BracketInserter bracketInserter = new BracketInserter(this);

  public PythonEditor() {
    setPreferenceStore(PythonUIPlugin.getPlugin().getCombinedPreferenceStore());

    setSourceViewerConfiguration(new PythonEditorSourceViewerConfiguration(this,
        getPreferenceStore()));

    setRulerContextMenuId("#PythonEditorRulerContext");

    setDocumentProvider(new PythonDocumentProvider());

    // TODO: key binding scopes
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
  public void createPartControl(Composite parent) {
    super.createPartControl(parent);

    ISourceViewer sourceViewer = getSourceViewer();

    if (sourceViewer instanceof ITextViewerExtension) {
      ((ITextViewerExtension) sourceViewer).prependVerifyKeyListener(bracketInserter);
    }

    getDocument().addDocumentListener(new IDocumentListener() {
      @Override
      public void documentAboutToBeChanged(DocumentEvent event) {

      }

      @Override
      public void documentChanged(DocumentEvent event) {
        handleDocumentModified();
      }
    });
  }

  @Override
  public void dispose() {
    if (imageUpdater != null) {
      imageUpdater.dispose();
    }

    ISourceViewer sourceViewer = getSourceViewer();

    if (sourceViewer instanceof ITextViewerExtension) {
      ((ITextViewerExtension) sourceViewer).removeVerifyKeyListener(bracketInserter);
    }

    super.dispose();
  }

  @Override
  protected void configureSourceViewerDecorationSupport(SourceViewerDecorationSupport support) {
    ICharacterPairMatcher matcher = new DefaultCharacterPairMatcher(
        new char[] {'(', ')', '[', ']'}, IDocumentExtension3.DEFAULT_PARTITIONING);
    support.setCharacterPairMatcher(matcher);
    support.setMatchingCharacterPainterPreferenceKeys(PreferenceConstants.EDITOR_MATCHING_BRACKETS,
        PreferenceConstants.EDITOR_MATCHING_BRACKETS_COLOR);

    super.configureSourceViewerDecorationSupport(support);
  }

  @Override
  protected void setTitleImage(Image image) {
    super.setTitleImage(image);
  }

  protected String getText() {
    return getSourceViewer().getDocument().get();
  }

  protected void handleReconcilation(IRegion partition) {
    if (outlinePage != null) {
      outlinePage.handleEditorReconcilation();
    }
  }

  public IDocument getDocument() {
    return getSourceViewer().getDocument();
  }

  protected PyModule getModel() {
    if (model == null) {
      model = new PythonParser(getText()).parse();
    }

    return model;
  }

  protected void handleDocumentModified() {
    model = null;
  }

  ISourceViewer getISourceViewer() {
    return getSourceViewer();
  }

  static char getEscapeCharacter(char character) {
    switch (character) {
      case '"':
      case '\'':
        return '\\';
      default:
        return 0;
    }
  }

  static char getPeerCharacter(char character) {
    switch (character) {
      case '(':
        return ')';

      case ')':
        return '(';

      case '<':
        return '>';

      case '>':
        return '<';

      case '[':
        return ']';

      case ']':
        return '[';

      case '"':
        return character;

      case '\'':
        return character;

      default:
        throw new IllegalArgumentException();
    }
  }

  public void selectAndReveal(PyNode node) {
    if (node.getOffset() != -1) {
      int length = node.getName() == null ? 0 : node.getName().length();

      selectAndReveal(node.getOffset(), length);
    }
  }

}
