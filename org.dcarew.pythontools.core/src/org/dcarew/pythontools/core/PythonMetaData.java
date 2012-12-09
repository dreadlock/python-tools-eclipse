package org.dcarew.pythontools.core;

import java.util.Arrays;
import java.util.List;

public class PythonMetaData {
  private static List<String> keywords;

  static {
    init();
  }

  private PythonMetaData() {

  }

  private static void init() {
    keywords = Arrays.asList(new String[] {
        "and", "as", "assert", "break", "class", "continue", "def", "del", "elif", "else",
        "except", "exec", "finally", "for", "from", "global", "if", "import", "in", "is", "lambda",
        "not", "or", "pass", "print", "raise", "return", "try", "while", "with", "yield", "None",
        "True", "False"});
  }

  public static List<String> getKeywords() {
    return keywords;
  }

}
