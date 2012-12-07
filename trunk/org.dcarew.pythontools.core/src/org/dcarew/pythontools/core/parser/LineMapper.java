package org.dcarew.pythontools.core.parser;

import java.util.ArrayList;
import java.util.List;

class LineMapper {
  List<Integer> lines = new ArrayList<Integer>();
  
  public LineMapper() {
    
  }
  
  public LineMapper(String content) {
    int offset = 0;
    
    lines.add(offset);
    
    for (char c : content.toCharArray()) {
      offset++;
      
      if (c == '\n') {
        lines.add(offset);
      }
    }
  }
  
  public int getOffset(int line, int lineOffset) {
    line -= 1;
    
    if (line < 0 || line >= lines.size()) {
      return -1;
    }
    
    return lines.get(line) + lineOffset;
  }

}
