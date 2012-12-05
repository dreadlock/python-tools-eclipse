package org.dcarew.pythontools.core.parser;

import org.json.JSONException;
import org.json.JSONObject;

public class PyModule {

  static PyModule parseJson(String str) {
    //System.out.println("[" + str + "]");
    
    try {
      JSONObject json = new JSONObject(str);
      
      System.out.println(json);
    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    // TODO:
    return new PyModule();
  }

}
