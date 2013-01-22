package org.dcarew.pythontools.core.debugger;

/**
 * An interface to return a debugger command result.
 */
public interface PyCallback<T> {

  public void handleResult(PyResult<T> result);

}
