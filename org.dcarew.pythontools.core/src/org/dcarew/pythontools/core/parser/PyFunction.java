package org.dcarew.pythontools.core.parser;

public class PyFunction extends PyNode {

  public PyFunction() {

  }

  public PyFunction(String name) {
    super(name);
  }

  public boolean isMethod() {
    return getParent() instanceof PyClass;
  }

  public String getReturnType() {
    // TODO:

    return null;
  }

}
