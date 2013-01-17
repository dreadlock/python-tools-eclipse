package org.dcarew.pythontools.core.debugger;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class represents a debugger variable.
 */
public class PyVariable implements Comparable<PyVariable> {

//  static PyVariable createArrayEntry(VmConnection connection, VmValue listValue, int index) {
//    PyVariable var = new PyVariable(listValue.getIsolate());
//
//    var.name = "[" + Integer.toString(index) + "]";
//    // var.value is lazyily populated
//    var.lazyValue = new LazyValue(connection, listValue, index);
//
//    return var;
//  }

  static List<PyVariable> createFrom(JSONArray arr) throws JSONException {
    if (arr == null) {
      return null;
    }

    List<PyVariable> variables = new ArrayList<PyVariable>();

    for (int i = 0; i < arr.length(); i++) {
      variables.add(createFrom(arr.getJSONObject(i)));
    }

    return variables;
  }

  static PyVariable createFrom(JSONObject obj) {
    // {"name":"server","value":{"objectId":4,"kind":"object","text":"Instance of '_HttpServer@14117cc4'"}}
    PyVariable var = new PyVariable();

    var.name = obj.optString("name");
    var.value = PyValue.createFrom(obj.optJSONObject("value"));

    return var;
  }

  static PyVariable createFromException(PyValue exception) {
    PyVariable variable = new PyVariable();

    variable.name = "exception";
    variable.value = exception;
    variable.isException = true;

    return variable;
  }

  private PyValue value;

  private String name;

  private boolean isException;

  private PyVariable() {

  }

  @Override
  public int compareTo(PyVariable other) {
    return getName().compareTo(other.getName());
  }

  public boolean getIsException() {
    return isException;
  }

  public String getName() {
    return name;
  }

  public PyValue getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "[" + getName() + "]";
  }

}
