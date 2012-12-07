package org.dcarew.pythontools.ui.editors;

import org.dcarew.pythontools.core.PythonMetaData;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class PythonContentAssistProcessor implements IContentAssistProcessor {

  public PythonContentAssistProcessor() {

  }

  @Override
  public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
    String prefix = getValidPrefix(viewer, offset);

    if (prefix == null) {
      return null;
    }

    List<ICompletionProposal> completions = new ArrayList<ICompletionProposal>();

    // TODO: we should suggest more completions
    for (String keyword : PythonMetaData.getKeywords()) {
      if (keyword.startsWith(prefix)) {
        completions.add(new CompletionProposal(keyword, offset - prefix.length(), prefix.length(),
            keyword.length()));
      }
    }

    Collections.sort(completions, new Comparator<ICompletionProposal>() {
      @Override
      public int compare(ICompletionProposal proposal1, ICompletionProposal proposal2) {
        return proposal1.getDisplayString().compareToIgnoreCase(proposal2.getDisplayString());
      }
    });

    return completions.toArray(new ICompletionProposal[completions.size()]);
  }

  @Override
  public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
    return null;
  }

  @Override
  public char[] getCompletionProposalAutoActivationCharacters() {
    return new char[] {};
  }

  @Override
  public char[] getContextInformationAutoActivationCharacters() {
    return null;
  }

  @Override
  public IContextInformationValidator getContextInformationValidator() {
    return null;
  }

  @Override
  public String getErrorMessage() {
    return null;
  }

  private String getValidPrefix(ITextViewer viewer, int offset) {
    try {
      IDocument doc = viewer.getDocument();

      IRegion lineInfo = doc.getLineInformationOfOffset(offset);

      String line = doc.get(lineInfo.getOffset(), offset - lineInfo.getOffset());

      StringBuilder prefix = new StringBuilder();

      for (int i = line.length() - 1; i >= 0; i--) {
        char c = line.charAt(i);

        if (PythonWordDetector.wordPart(c)) {
          prefix.insert(0, c);
        } else {
          return prefix.toString();
        }
      }

      return prefix.toString();
    } catch (BadLocationException ex) {
      return null;
    }
  }

}
