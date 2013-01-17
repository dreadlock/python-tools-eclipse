package org.dcarew.pythontools.core.debugger;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A representation of a debugger object.
 */
public class PyObject {

  static PyObject createFrom(JSONObject obj) throws JSONException {
    PyObject vmObject = new PyObject();

    // { classId : Integer , fields : FieldList }
    vmObject.classId = obj.optInt("classId");
    vmObject.fields = PyVariable.createFrom(obj.optJSONArray("fields"));

    return vmObject;
  }

  private int objectId;

  private int classId;

  private List<PyVariable> fields;

  private PyObject() {

  }

  public int getClassId() {
    return classId;
  }

  public List<PyVariable> getFields() {
    return fields;
  }

  public int getObjectId() {
    return objectId;
  }

  public void setObjectId(int objectId) {
    this.objectId = objectId;
  }

}
