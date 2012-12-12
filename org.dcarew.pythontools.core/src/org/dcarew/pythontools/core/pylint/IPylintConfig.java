package org.dcarew.pythontools.core.pylint;

import java.io.File;
import java.io.IOException;

public interface IPylintConfig {

  public byte[] getContents() throws IOException;

  public File getFile();

  public String getIndent();

  public String getName();

  public boolean isModifyable();

}
