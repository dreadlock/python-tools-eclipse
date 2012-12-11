package org.dcarew.pythontools.core.utils;

public class PythonLocator {

  public PythonLocator() {

  }

  public String getDefaultPylintPath() {
    return null;
  }

  public String getDefaultPythonPath() {
    // TODO: on windows, look for python.exe on the PATH

    return "python";
  }

}
