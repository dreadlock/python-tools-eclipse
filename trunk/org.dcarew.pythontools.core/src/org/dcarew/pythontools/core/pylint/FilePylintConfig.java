package org.dcarew.pythontools.core.pylint;

import org.dcarew.pythontools.core.utils.IOUtils;
import org.eclipse.core.resources.IFile;

import java.io.File;
import java.io.IOException;

public class FilePylintConfig implements IPylintConfig {
  private IFile file;

  public FilePylintConfig(IFile file) {
    this.file = file;
  }

  @Override
  public String getName() {
    return file.getName();
  }

  @Override
  public File getFile() {
    return file.getLocation().toFile();
  }

  @Override
  public byte[] getContents() throws IOException {
    return IOUtils.getContentsAsBytes(file);
  }

  @Override
  public boolean isModifyable() {
    return true;
  }

  @Override
  public String toString() {
    return "[" + getName() + "," + isModifyable() + "]";
  }

}
