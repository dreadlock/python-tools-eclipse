package org.dcarew.pythontools.ui.editors;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DefaultIndentLineAutoEditStrategy;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.TextUtilities;

class PythonAutoIndentStrategy extends DefaultIndentLineAutoEditStrategy {

  public PythonAutoIndentStrategy() {

  }

  @Override
  public final void customizeDocumentCommand(IDocument d, DocumentCommand c) {
    if (c.length == 0 && c.text != null
        && TextUtilities.endsWith(d.getLegalLineDelimiters(), c.text) != -1) {
      doAutoIndentAfterNewLine(d, c);
    }
  }

  protected void doAutoIndentAfterNewLine(IDocument document, DocumentCommand command) {
    if (command.offset == -1 || document.getLength() == 0) {
      return;
    }

    try {
      // find start of line
      int location = (command.offset == document.getLength() ? command.offset - 1 : command.offset);
      IRegion lineInfo = document.getLineInformationOfOffset(location);
      int start = lineInfo.getOffset();

      boolean endsInColon = (document.getChar(command.offset - 1) == ':');

      // find white spaces
      int end = findEndOfWhiteSpace(document, start, command.offset);

      StringBuffer buf = new StringBuffer(command.text);

      if (end > start) {
        // append to input
        buf.append(document.get(start, end - start));
      }

      if (endsInColon) {
        // TODO: we need to be able to configure this
        buf.append("  ");
      }

      command.text = buf.toString();
    } catch (BadLocationException excp) {
      // stop work
    }
  }

}
