package org.dcarew.pythontools.ui.editors;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

class PythonCommentScanner extends RuleBasedScanner {

  public PythonCommentScanner() {
    Token commentToken = new Token(new TextAttribute(Display.getDefault().getSystemColor(
        SWT.COLOR_DARK_GREEN)));

    setDefaultReturnToken(commentToken);
  }

}
