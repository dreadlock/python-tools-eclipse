package org.dcarew.pythontools.core.pylint;

import java.io.File;
import java.io.IOException;

public interface IPylintConfig {

  public String getName();

  public File getFile();

  public byte[] getContents() throws IOException;

  public boolean isModifyable();

}
