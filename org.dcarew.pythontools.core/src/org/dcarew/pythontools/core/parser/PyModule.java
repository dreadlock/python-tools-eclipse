package org.dcarew.pythontools.core.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PyModule extends PyNode {

  static PyModule parseJson(LineMapper lineMapper, String str) {
    try {
      JSONObject json = new JSONObject(str);

      JSONArray arr = json.getJSONArray("children");
      PyModule module = new PyModule();

      for (int i = 0; i < arr.length(); i++) {
        JSONObject obj = arr.getJSONObject(i);
        String type = obj.optString("type");

        if ("import".equals(type)) {
          parseImport(lineMapper, module, obj);
        } else if ("class".equals(type)) {
          parseClass(lineMapper, module, obj);
        } else if ("function".equals(type)) {
          parseFunction(lineMapper, module, obj);
        } else if ("assign".equals(type)) {
          parseAssign(lineMapper, module, obj);
        } else if ("if".equals(type)) {
          parseIf(lineMapper, module, obj);
        } else {
          // TODO:
          System.out.println("unexpected: " + obj);
        }
      }

      return module;
    } catch (JSONException e) {
      // TODO:
      e.printStackTrace();

      return new PyModule();
    }
  }

  private static void parseAssign(LineMapper mapper, PyNode parent, JSONObject obj)
      throws JSONException {
    JSONObject location = obj.getJSONObject("start");

    PyAssign pyassign = new PyAssign(obj.getString("name"));
    pyassign.setLocation(mapper.refineOffset(
        mapper.getOffset(location.getInt("line"), location.getInt("col")), pyassign.getName()));

    parent.addChild(pyassign);
  }

  private static void parseClass(LineMapper mapper, PyNode parent, JSONObject obj)
      throws JSONException {
    JSONObject location = obj.getJSONObject("start");

    PyClass pyclass = new PyClass(obj.getString("name"));
    pyclass.setLocation(mapper.refineOffset(
        mapper.getOffset(location.getInt("line"), location.getInt("col")), pyclass.getName()));

    parent.addChild(pyclass);

    // parse children
    JSONArray arr = obj.getJSONArray("children");

    for (int i = 0; i < arr.length(); i++) {
      JSONObject child = arr.getJSONObject(i);

      if (child.has("type") && child.optString("type").equals("function")) {
        parseFunction(mapper, pyclass, child);
      } else {
        // TODO:

      }
    }
  }

  private static void parseFunction(LineMapper mapper, PyNode parent, JSONObject obj)
      throws JSONException {
    JSONObject location = obj.getJSONObject("start");

    PyFunction pyfunction = new PyFunction(obj.getString("name"));
    pyfunction.setLocation(mapper.refineOffset(
        mapper.getOffset(location.getInt("line"), location.getInt("col")), pyfunction.getName()));

    parent.addChild(pyfunction);
  }

  private static void parseIf(LineMapper mapper, PyNode parent, JSONObject obj)
      throws JSONException {
    JSONObject location = obj.getJSONObject("start");

    PyIf pyif = new PyIf(obj.getString("name"));
    pyif.setLocation(mapper.refineOffset(
        mapper.getOffset(location.getInt("line"), location.getInt("col")), pyif.getName()));

    parent.addChild(pyif);
  }

  private static void parseImport(LineMapper mapper, PyNode parent, JSONObject obj)
      throws JSONException {
    JSONObject location = obj.getJSONObject("start");

    JSONArray arr = obj.getJSONArray("children");
    // TODO:
    PyImport pyimport = new PyImport(arr.getJSONObject(0).getString("name"));
    pyimport.setLocation(mapper.refineOffset(
        mapper.getOffset(location.getInt("line"), location.getInt("col")), pyimport.getName()));

    parent.addChild(pyimport);
  }

  public PyModule() {

  }

}
