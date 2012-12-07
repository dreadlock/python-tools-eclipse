package org.dcarew.pythontools.core.parser;

import org.dcarew.pythontools.core.utils.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class PyNode {
  private String name;
  private int offset = -1;
  private PyNode parent;
  private List<PyNode> children = new ArrayList<PyNode>();

  public PyNode() {

  }

  public PyNode(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public List<PyNode> getChildren() {
    return children;
  }

  public void addChild(PyNode child) {
    child.parent = this;

    children.add(child);
  }

  @Override
  public boolean equals(Object obj) {
    if (getClass().isInstance(obj)) {
      return ObjectUtils.safeEquals(getName(), ((PyNode) obj).getName());
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return name == null ? 0 : getName().hashCode();
  }

  @Override
  public String toString() {
    return getName();
  }

  public PyNode getParent() {
    return parent;
  }

  public int getOffset() {
    return offset;
  }

  protected void setLocation(int offset) {
    this.offset = offset;
  }
  
}
