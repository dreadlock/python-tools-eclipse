package org.dcarew.pythontools.core.parser;

import java.util.ArrayList;
import java.util.List;

class LineMapper {
  private String content;
  private List<Integer> lines = new ArrayList<Integer>();

  public LineMapper() {

  }

  public LineMapper(String content) {
    this.content = content;

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

  public int refineOffset(int offset, String match) {
    if (content != null) {
      // Look for the match string.
      int index = content.indexOf(match, offset);

      if (index != -1) {
        // Verify that we found it before the end of the current line.
        int nextNewLine = content.indexOf('\n', offset);

        if (nextNewLine > index) {
          return index;
        }
      }
    }

    return offset;
  }

}
