package org.dcarew.pythontools.core.debugger;

import org.json.JSONObject;

/**
 * This class represents a VM value.
 */
public class PyValue {

  static PyValue createFrom(JSONObject obj) {
    if (obj == null) {
      return null;
    }

    // {"objectId":4,"kind":"object","text":"Instance of '_HttpServer@14117cc4'"}

    PyValue value = new PyValue();

    value.objectId = obj.optInt("objectId");
    value.kind = obj.optString("kind");
    value.text = obj.optString("text");
    value.length = obj.optInt("length", 0);

    return value;
  }

  private String text;

  private String kind;

  private int objectId;

  private int length;

  private PyObject vmObject;

  private PyValue() {

  }

  /**
   * @return one of "string", "integer", "object", "boolean".
   */
  public String getKind() {
    return kind;
  }

  /**
   * If this value is a list type, this method returns the list length;
   * 
   * @return
   */
  public int getLength() {
    return length;
  }

  public int getObjectId() {
    return objectId;
  }

  public String getText() {
    return text;
  }

  public PyObject getVmObject() {
    return vmObject;
  }

  public boolean isList() {
    return "list".equals(getKind());
  }

  public boolean isNull() {
    if (isObject() && objectId == 0 && text == null) {
      return true;
    }

    return isObject() && (text == null || "null".equals(text));
  }

  public boolean isObject() {
    return "object".equals(getKind());
  }

  public boolean isString() {
    return "string".equals(getKind());
  }

  @Override
  public String toString() {
    return getKind() + "," + getText();
  }

  void setVmObject(PyObject object) {
    this.vmObject = object;
  }

}
