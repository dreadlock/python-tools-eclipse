package org.dcarew.pythontools.ui.editors;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;

public class PythonPartitionScanner extends RuleBasedPartitionScanner {
  public final static String PYTHON_COMMENT = "__python_comment";

  public PythonPartitionScanner() {
    IToken commentToken = new Token(PYTHON_COMMENT);

    IPredicateRule[] rules = new IPredicateRule[1];

    rules[0] = new EndOfLineRule("#", commentToken);

    setPredicateRules(rules);
  }

}
