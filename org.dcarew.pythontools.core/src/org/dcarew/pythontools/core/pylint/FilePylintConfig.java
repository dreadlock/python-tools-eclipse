package org.dcarew.pythontools.core.pylint;

import java.io.File;
import java.io.IOException;

import org.dcarew.pythontools.core.utils.IOUtils;
import org.eclipse.core.resources.IFile;

public class FilePylintConfig implements IPylintConfig {
  private IFile file;

  private String indent;

  private boolean indentInited;

  public FilePylintConfig(IFile file) {
    this.file = file;
  }

  @Override
  public byte[] getContents() throws IOException {
    return IOUtils.getContentsAsBytes(file);
  }

  @Override
  public File getFile() {
    return file.getLocation().toFile();
  }

  @Override
  public String getIndent() {
    if (!indentInited) {
      indentInited = true;

      try {
        indent = Utils.parseIdent(getContents());
      } catch (IOException e) {

      }
    }

    return indent;
  }

  @Override
  public String getName() {
    return file.getName();
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
