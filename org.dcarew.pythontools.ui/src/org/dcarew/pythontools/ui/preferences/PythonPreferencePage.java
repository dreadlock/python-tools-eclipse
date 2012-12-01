package org.dcarew.pythontools.ui.preferences;

import org.dcarew.pythontools.core.PythonCorePlugin;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.osgi.framework.Version;

public class PythonPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

  public PythonPreferencePage() {
    setDescription("Python Tools for Eclipse v" + getVersionText());
  }

  @Override
  public void init(IWorkbench workbench) {

  }

  @Override
  protected Control createContents(Composite parent) {
    // TODO:
    Label label = new Label(parent, SWT.NONE);
    //label.setText("dsfsdf");
    return label;
  }

  private static String getVersionText() {
    Version version = PythonCorePlugin.getPlugin().getBundle().getVersion();

    return version.getMajor() + "." + version.getMinor() + "." + version.getMicro();
  }

}
