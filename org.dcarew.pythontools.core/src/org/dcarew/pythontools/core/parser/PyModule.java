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
          // TODO:
          
        } else if (obj.has("class")) {
          // TODO:
          
        } else if (obj.has("function")) {
          // TODO:
          parseFunction(lineMapper, module, obj.getJSONObject("function"));
        } else {
          // TODO:
          
        }
      }
      
      return module;
    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return new PyModule();
    }
  }

  public PyModule() {

  }

  private static void parseFunction(LineMapper lineMapper, PyModule module, JSONObject obj) throws JSONException {
    // TODO:
    JSONObject location = obj.getJSONObject("location");
    
    PyFunction function = new PyFunction(obj.getString("name"));
    function.setLocation(lineMapper.getOffset(location.getInt("lineno"), location.getInt("col_offset")));
    
    module.addChild(function);
  }

}
