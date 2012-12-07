package org.dcarew.pythontools.ui.editors;

import org.dcarew.pythontools.core.parser.PyNode;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class PyNodeContentProvider implements ITreeContentProvider {

  public PyNodeContentProvider() {
    
  }
  
  @Override
  public void dispose() {

  }

  @Override
  public Object[] getChildren(Object element) {
    PyNode node = (PyNode) element;

    return node.getChildren().toArray();
  }

  @Override
  public Object[] getElements(Object element) {
    return getChildren(element);
  }

  @Override
  public Object getParent(Object element) {
    PyNode node = (PyNode) element;

    return node.getParent();
  }

  @Override
  public boolean hasChildren(Object element) {
    return getChildren(element).length > 0;
  }

  @Override
  public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

  }

}
