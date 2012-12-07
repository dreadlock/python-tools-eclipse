package org.dcarew.pythontools.ui.editors;

import org.dcarew.pythontools.core.parser.PyNode;
import org.dcarew.pythontools.ui.PythonUIPlugin;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;

public class PyNodeLabelProvider extends LabelProvider implements IStyledLabelProvider {
  
  public PyNodeLabelProvider() {
    
  }

  @Override
  public Image getImage(Object element) {
    return PythonUIPlugin.getImage("icons/python_16.png");
  }

  @Override
  public String getText(Object element) {
    PyNode node = (PyNode) element;

    return node.getName();
  }
  
  @Override
  public StyledString getStyledText(Object element) {
    PyNode node = (PyNode) element;

    StyledString string = new StyledString(node.getName());

    String auxText = getAuxText(node);

    if (auxText != null) {
      string.append(" - " + auxText, StyledString.QUALIFIER_STYLER);
    }

    return string;
  }
  
  private String getAuxText(PyNode node) {
    // TODO:
    
    return null;
  }
  
}
