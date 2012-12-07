package org.dcarew.pythontools.ui.editors;

import org.dcarew.pythontools.core.parser.PyClass;
import org.dcarew.pythontools.core.parser.PyFunction;
import org.dcarew.pythontools.core.parser.PyImport;
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
    if (element instanceof PyFunction) {
      PyFunction pyfunction = (PyFunction) element;

      if (pyfunction.isMethod()) {
        return PythonUIPlugin.getImage("icons/ast/methpro_obj.gif");
      } else {
        return PythonUIPlugin.getImage("icons/ast/methpub_obj.gif");
      }
    }

    if (element instanceof PyClass) {
      return PythonUIPlugin.getImage("icons/ast/class_obj.gif");
    }

    if (element instanceof PyImport) {
      return PythonUIPlugin.getImage("icons/ast/imp_obj.gif");
    }

    return PythonUIPlugin.getImage("python_16.png");
  }

  @Override
  public String getText(Object element) {
    PyNode node = (PyNode) element;

    if (element instanceof PyFunction) {
      return node.getName() + "()";
    } else {
      return node.getName();
    }
  }

  @Override
  public StyledString getStyledText(Object element) {
    PyNode node = (PyNode) element;

    StyledString string = new StyledString(getText(element));

    String auxText = getAuxText(node);

    if (auxText != null) {
      string.append(" - " + auxText, StyledString.QUALIFIER_STYLER);
    }

    return string;
  }

  private String getAuxText(PyNode node) {
    if (node instanceof PyFunction) {
      PyFunction pyfunction = (PyFunction) node;

      return pyfunction.getReturnType();
    }

    return null;
  }

}
