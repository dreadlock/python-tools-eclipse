package org.dcarew.pythontools.core.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PyModule extends PyNode {

  /*
   * {"module":[ {"import":"foo"}, {"import":"foo"}, {"import":"foo"}, {"import":"foo"},
   * {"import":"foo"},
   * {"function":{"location":{"col_offset":0,"lineno":25},"name":"_BuildOptions"}},
   * {"function":{"location":{"col_offset":0,"lineno":101},"name":"main"}},
   * {"function":{"location":{"col_offset":0,"lineno":175},"name":"_ReadBucket"}},
   * {"function":{"location":{"col_offset":0,"lineno":203},"name":"_RemoveElements"}},
   * {"function":{"location":{"col_offset":0,"lineno":232},"name":"_PromoteBuild"}},
   * {"function":{"location":{"col_offset":0,"lineno":256},"name":"_PrintSeparator"}},
   * {"function":{"location":{"col_offset":0,"lineno":261},"name":"_PrintFailure"}},
   * {"function":{"location":{"col_offset":0,"lineno":267},"name":"_Gsutil"}},
   * {"function":{"location":{"col_offset":0,"lineno":272},"name":"_ExecuteCommand"}} ]}
   */

  static PyModule parseJson(LineMapper lineMapper, String str) {
    try {
      JSONObject json = new JSONObject(str);

      JSONArray arr = json.getJSONArray("module");
      PyModule module = new PyModule();

      for (int i = 0; i < arr.length(); i++) {
        JSONObject obj = arr.getJSONObject(i);

        if (obj.has("import")) {
          parseImport(lineMapper, module, obj.getJSONObject("import"));
        } else if (obj.has("class")) {
          parseClass(lineMapper, module, obj.getJSONObject("class"));
        } else if (obj.has("function")) {
          parseFunction(lineMapper, module, obj.getJSONObject("function"));
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

  public PyModule() {

  }

  private static void parseImport(LineMapper mapper, PyNode parent, JSONObject obj)
      throws JSONException {
    JSONObject location = obj.getJSONObject("location");

    PyImport pyimport = new PyImport(obj.getString("name"));
    pyimport.setLocation(mapper.refineOffset(
        mapper.getOffset(location.getInt("lineno"), location.getInt("col_offset")),
        pyimport.getName()));

    parent.addChild(pyimport);
  }

  private static void parseFunction(LineMapper mapper, PyNode parent, JSONObject obj)
      throws JSONException {
    JSONObject location = obj.getJSONObject("location");

    PyFunction pyfunction = new PyFunction(obj.getString("name"));
    pyfunction.setLocation(mapper.refineOffset(
        mapper.getOffset(location.getInt("lineno"), location.getInt("col_offset")),
        pyfunction.getName()));

    parent.addChild(pyfunction);
  }

  private static void parseClass(LineMapper mapper, PyNode parent, JSONObject obj)
      throws JSONException {
    JSONObject location = obj.getJSONObject("location");

    PyClass pyclass = new PyClass(obj.getString("name"));
    pyclass.setLocation(mapper.refineOffset(
        mapper.getOffset(location.getInt("lineno"), location.getInt("col_offset")),
        pyclass.getName()));

    parent.addChild(pyclass);

    // parse children
    JSONArray arr = obj.getJSONArray("children");

    for (int i = 0; i < arr.length(); i++) {
      JSONObject child = arr.getJSONObject(i);

      if (child.has("function")) {
        parseFunction(mapper, pyclass, child.getJSONObject("function"));
      } else {
        // TODO:

      }
    }
  }

}
