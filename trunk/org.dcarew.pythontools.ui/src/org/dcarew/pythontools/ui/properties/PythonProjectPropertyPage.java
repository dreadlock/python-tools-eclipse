package org.dcarew.pythontools.ui.properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.dialogs.PropertyPage;

public class PythonProjectPropertyPage extends PropertyPage {

  public PythonProjectPropertyPage() {

  }

  @Override
  protected Control createContents(Composite parent) {
    // TODO: implement
    Label label = new Label(parent, SWT.NONE);

    label.setText("");

    return label;
  }

}
