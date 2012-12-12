package org.dcarew.pythontools.core.pylint;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

class Utils {

  private static final String INDENT_PREFIX = "indent-string=";

  public static String parseIdent(byte[] data) {
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(
          data), "UTF-8"));

      String line = reader.readLine();

      while (line != null) {
        if (line.startsWith(INDENT_PREFIX)) {
          line = line.trim();
          line = line.substring(INDENT_PREFIX.length());
          line = stripQuotes(line);

          if (line.length() > 0) {
            return line;
          } else {
            return null;
          }
        }

        line = reader.readLine();
      }
    } catch (IOException ioe) {

    }

    return null;
  }

  private static String stripQuotes(String str) {
    if (str.startsWith("\"") || str.startsWith("'")) {
      str = str.substring(1);
    }

    if (str.endsWith("\"") || str.endsWith("'")) {
      str = str.substring(0, str.length() - 1);
    }

    return str;
  }

  private Utils() {

  }

}
