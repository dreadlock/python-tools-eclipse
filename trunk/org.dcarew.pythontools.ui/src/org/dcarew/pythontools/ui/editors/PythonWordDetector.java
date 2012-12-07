
package org.dcarew.pythontools.ui.editors;

import org.eclipse.jface.text.rules.IWordDetector;

class PythonWordDetector implements IWordDetector {

  public PythonWordDetector() {

  }

  @Override
  public boolean isWordPart(char c) {
    return wordPart(c);
  }

  @Override
  public boolean isWordStart(char c) {
    return wordStart(c);
  }

  public final static boolean wordPart(char c) {
    return c == '_' || Character.isJavaIdentifierPart(c);
  }

  public final static boolean wordStart(char c) {
    return c == '_' || Character.isJavaIdentifierStart(c);
  }
  
}
