package org.dcarew.pythontools.ui.editors;

import org.dcarew.pythontools.core.PythonMetaData;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

import java.util.ArrayList;
import java.util.List;

class PythonCodeScanner extends RuleBasedScanner {

  public PythonCodeScanner() {
    Token keywordToken = new Token(new TextAttribute(Display.getDefault().getSystemColor(
        SWT.COLOR_BLACK), null, SWT.BOLD));
//    Token commentToken = new Token(new TextAttribute(Display.getDefault().getSystemColor(
//        SWT.COLOR_DARK_GREEN)));
//    Token stringToken = new Token(new TextAttribute(Display.getDefault().getSystemColor(
//        SWT.COLOR_BLUE)));

    List<IRule> rules = new ArrayList<IRule>();

    PythonWordRule keywordRule = new PythonWordRule(new PythonWordDetector());

    for (String keyword : PythonMetaData.getKeywords()) {
      keywordRule.addWord(keyword, keywordToken);
    }

    rules.add(keywordRule);

//    // comments
//    rules.add(new EndOfLineRule("#", commentToken));
//
//    // strings
//    rules.add(new MultiLineRule("\"\"\"", "\"\"\"", stringToken));
//    rules.add(new MultiLineRule("'''", "'''", stringToken));
//    rules.add(new SingleLineRule("'", "'", stringToken, '\\', true));
//    rules.add(new SingleLineRule("\"", "\"", stringToken, '\\', true));

    setRules(rules.toArray(new IRule[rules.size()]));
  }

}
