package org.dcarew.pythontools.core.debugger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A debugger location object.
 */
public class PyLocation {

  static PyLocation createFrom(JSONObject object) throws JSONException {
    PyLocation location = new PyLocation();

    location.file = object.optString("file");
    location.lineNumber = object.optInt("lineNumber", -1);
    // This field is not currently used by the VM.
    location.columnNumber = object.optInt("columnNumber", -1);

    return location;
  }

  private int columnNumber;

  private int lineNumber;

  private String file;

  public PyLocation(String file, int lineNumber) {
    this.file = file;
    this.lineNumber = lineNumber;
  }

  PyLocation() {

  }

  public int getColumnNumber() {
    return columnNumber;
  }

  public String getFile() {
    return file;
  }

  public int getLineNumber() {
    return lineNumber;
  }

  public JSONObject toJSONObject() throws JSONException {
    JSONObject object = new JSONObject();

    object.put("url", file);
    object.put("lineNumber", lineNumber);

    if (columnNumber != -1) {
      object.put("columnNumber", columnNumber);
    }

    return object;
  }

  @Override
  public String toString() {
    return "[" + file + "," + lineNumber + (columnNumber == -1 ? "" : "," + columnNumber) + "]";
  }

  void updateInfo(String file, int lineNumber) {
    this.file = file;
    this.lineNumber = lineNumber;
  }

}
