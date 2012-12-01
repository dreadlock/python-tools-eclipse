/*
 * Copyright (c) 2012, the Dart project authors.
 * 
 * Licensed under the Eclipse Public License v1.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.dcarew.pythontools.ui.editors;

import org.dcarew.pythontools.core.PythonMetaData;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

import java.util.ArrayList;
import java.util.List;

/**
 * The tokenizer (ITokenScanner) for python content.
 */
class PythonScanner extends RuleBasedScanner {

  public PythonScanner() {
    Token keywordToken = new Token(new TextAttribute(Display.getDefault().getSystemColor(
        SWT.COLOR_BLACK), null, SWT.BOLD));
    Token commentToken = new Token(new TextAttribute(Display.getDefault().getSystemColor(
        SWT.COLOR_GRAY)));
    Token stringToken = new Token(new TextAttribute(Display.getDefault().getSystemColor(
        SWT.COLOR_BLUE)));

    List<IRule> rules = new ArrayList<IRule>();

    PythonWordRule keywordRule = new PythonWordRule(new PythonWordDetector());
    
    for (String keyword : PythonMetaData.getKeywords()) {
      keywordRule.addWord(keyword, keywordToken);
    }
    
    rules.add(keywordRule);

    // comments
    rules.add(new EndOfLineRule("#", commentToken));

    // strings
    // TODO: continuations
    // TODO: raw string
    rules.add(new SingleLineRule("'", "'", stringToken, '\\', true));
    rules.add(new SingleLineRule("\"", "\"", stringToken, '\\', true));
    rules.add(new MultiLineRule("'''", "'''", stringToken));
    rules.add(new MultiLineRule("\"\"\"", "\"\"\"", stringToken));

    setRules(rules.toArray(new IRule[rules.size()]));
  }

}
