package org.dcarew.pythontools.ui.editors;

import org.dcarew.pythontools.ui.PythonUIPlugin;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Point;

// see CompilationUnitEditor.BracketInserter
class BracketInserter implements VerifyKeyListener {
  private PythonEditor editor;

  public BracketInserter(PythonEditor editor) {
    this.editor = editor;
  }

  @SuppressWarnings("unused")
  @Override
  public void verifyKey(VerifyEvent event) {
    // TODO: this code needs cleanup before we turn it on
    if (false) {
      return;
    }
      
    // early pruning to slow down normal typing as little as possible
    if (!event.doit || editor.isBlockSelectionModeEnabled() && isMultilineSelection()) {
      return;
    }

//    // TODO: respect this
//    if (editor.getInsertMode() != PythonEditor.SMART_INSERT) {
//      return;
//    }

    switch (event.character) {
      case '(':
      case '[':
      case '\'':
      case '\"':
        break;
      default:
        return;
    }

    final ISourceViewer sourceViewer = editor.getISourceViewer();
    IDocument document = sourceViewer.getDocument();

    final Point selection = sourceViewer.getSelectedRange();
    final int offset = selection.x;
    final int length = selection.y;

    try {
//      IRegion startLine = document.getLineInformationOfOffset(offset);
//      IRegion endLine = document.getLineInformationOfOffset(offset + length);

      final char character = event.character;
      final char closingCharacter = PythonEditor.getPeerCharacter(character);

      // TODO: check for end of doc
      char nextChar = document.getChar(offset);

      if (nextChar == closingCharacter) {
        return;
      }
      
      // TODO: make sure we're not in a comment
//      ITypedRegion partition = TextUtilities.getPartition(document,
//          IJavaPartitions.JAVA_PARTITIONING, offset, true);
//      if (!IDocument.DEFAULT_CONTENT_TYPE.equals(partition.getType())) {
//        return;
//      }

      if (!editor.validateEditorInputState()) {
        return;
      }

      final StringBuffer buffer = new StringBuffer();
      buffer.append(character);
      buffer.append(closingCharacter);

      document.replace(offset, length, buffer.toString());
      sourceViewer.setSelectedRange(offset + 1, 0);
      
//      BracketLevel level = new BracketLevel();
//      fBracketLevelStack.push(level);
//
//      LinkedPositionGroup group = new LinkedPositionGroup();
//      group.addPosition(new LinkedPosition(document, offset + 1, 0, LinkedPositionGroup.NO_STOP));
//
////      LinkedModeModel model = new LinkedModeModel();
////      model.addLinkingListener(this);
////      model.addGroup(group);
////      model.forceInstall();
//
//      // set up position tracking for our magic peers
//      if (fBracketLevelStack.size() == 1) {
//        document.addPositionCategory(CATEGORY);
//        document.addPositionUpdater(fUpdater);
//      }
//      level.fFirstPosition = new Position(offset, 1);
//      level.fSecondPosition = new Position(offset + 1, 1);
//      document.addPosition(CATEGORY, level.fFirstPosition);
//      document.addPosition(CATEGORY, level.fSecondPosition);
//
//      level.fUI = new EditorLinkedModeUI(model, sourceViewer);
//      level.fUI.setSimpleMode(true);
//      level.fUI.setExitPolicy(new ExitPolicy(closingCharacter,
//          PythonEditor.getEscapeCharacter(closingCharacter), fBracketLevelStack));
//      level.fUI.setExitPosition(sourceViewer, offset + 2, 0, Integer.MAX_VALUE);
//      level.fUI.setCyclingMode(LinkedModeUI.CYCLE_NEVER);
//      level.fUI.enter();
//
//      IRegion newSelection = level.fUI.getSelectedRegion();
//      sourceViewer.setSelectedRange(newSelection.getOffset(), newSelection.getLength());

      event.doit = false;
    } catch (BadLocationException e) {
      PythonUIPlugin.logError(e);
    }
  }

  private boolean isMultilineSelection() {
    ISelection selection = editor.getSelectionProvider().getSelection();

    if (selection instanceof ITextSelection) {
      ITextSelection ts = (ITextSelection) selection;
      return ts.getStartLine() != ts.getEndLine();
    }

    return false;
  }

}
