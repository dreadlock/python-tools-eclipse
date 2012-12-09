package org.dcarew.pythontools.core.utils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IOUtils {

  private IOUtils() {

  }

  public static byte[] readStream(InputStream in) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] buffer = new byte[4096];
    int count = in.read(buffer);
    while (count != -1) {
      out.write(buffer, 0, count);
      count = in.read(buffer);
    }

    return out.toByteArray();
  }

  public static byte[] getContentsAsBytes(File file) throws IOException {
    return IOUtils.readStream(new FileInputStream(file));
  }

  public static byte[] getContentsAsBytes(IFile file) throws IOException {
    try {
      return IOUtils.readStream(file.getContents());
    } catch (CoreException ce) {
      throw new IOException(ce);
    }
  }

  public static String getContentsAsString(IFile file) throws IOException {
    try {
      InputStreamReader reader = new InputStreamReader(file.getContents(), file.getCharset());

      StringBuilder builder = new StringBuilder();
      char[] buffer = new char[4096];
      int count = reader.read(buffer);

      while (count != -1) {
        builder.append(buffer, 0, count);
        count = reader.read(buffer);
      }

      return builder.toString();
    } catch (CoreException ce) {
      throw new IOException(ce);
    }
  }

}
