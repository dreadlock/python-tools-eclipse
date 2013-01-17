package org.dcarew.pythontools.core.debugger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A debugger command result.
 */
public class PyResult<T> {

  static <T> PyResult<T> createFrom(JSONObject params) throws JSONException {
    PyResult<T> result = new PyResult<T>();

    if (params.has("error")) {
      result.setError(params.getString("error"));
    }

    return result;
  }

  static <T> PyResult<T> createFrom(T object) {
    PyResult<T> result = new PyResult<T>();

    result.setResult(object);

    return result;
  }

  static JSONObject createJsonErrorResult(String message) throws JSONException {
    JSONObject obj = new JSONObject();

    obj.put("error", message);

    return obj;
  }

  private String error;

  private T result;

  PyResult() {

  }

  public String getError() {
    return error;
  }

  public T getResult() {
    return result;
  }

  public boolean isError() {
    return error != null;
  }

  @Override
  public String toString() {
    if (error != null) {
      return error.toString();
    } else if (result != null) {
      return result.toString();
    } else {
      return super.toString();
    }
  }

  void setError(String error) {
    this.error = error;
  }

  void setResult(T result) {
    this.result = result;
  }

}
