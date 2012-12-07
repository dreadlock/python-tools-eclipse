package org.dcarew.pythontools.ui.editors;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;

import java.util.ArrayList;
import java.util.List;

public class PythonPartitionScanner extends RuleBasedPartitionScanner {
  //public final static String PYTHON_CODE = "__python_code";
  public final static String PYTHON_COMMENT = "__python_comment";
  public final static String PYTHON_STRING = "__python_string";

  public PythonPartitionScanner() {
    //setDefaultReturnToken(new Token(PYTHON_CODE));

    IToken commentToken = new Token(PYTHON_COMMENT);
    IToken stringToken = new Token(PYTHON_STRING);

    List<IPredicateRule> rules = new ArrayList<IPredicateRule>();

    rules.add(new EndOfLineRule("#", commentToken));
    rules.add(new MultiLineRule("\"\"\"", "\"\"\"", stringToken));
    rules.add(new MultiLineRule("'''", "'''", stringToken));
    rules.add(new SingleLineRule("'", "'", stringToken, '\\', true));
    rules.add(new SingleLineRule("\"", "\"", stringToken, '\\', true));

    setPredicateRules(rules.toArray(new IPredicateRule[rules.size()]));
  }

}
