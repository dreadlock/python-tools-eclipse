package org.dcarew.pythontools.ui.properties;

import org.dcarew.pythontools.core.PythonCorePlugin;
import org.dcarew.pythontools.core.pylint.IPylintConfig;
import org.dcarew.pythontools.core.pylint.PylintConfigManager;
import org.dcarew.pythontools.ui.PythonUIPlugin;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.dialogs.PropertyPage;

public class PythonProjectPropertyPage extends PropertyPage implements SelectionListener {
  private Button analyzeProjectButton;
  private Button overrideButton;
  private Combo combo;

  public PythonProjectPropertyPage() {
    noDefaultAndApplyButton();
  }

  @Override
  public boolean performOk() {
    IProject project = getProject();
    IPylintConfig config = PylintConfigManager.getConfig(combo.getText());

    try {
      PythonCorePlugin.getPlugin().applyProjectSettings(project,
          analyzeProjectButton.getSelection(), overrideButton.getSelection(), config);
    } catch (CoreException e) {

    }

    return true;
  }

  @Override
  public void widgetDefaultSelected(SelectionEvent e) {

  }

  @Override
  public void widgetSelected(SelectionEvent e) {
    updateControlStates();
  }

  @Override
  protected Control createContents(Composite parent) {
    Composite composite = new Composite(parent, SWT.NONE);
    GridLayoutFactory.fillDefaults().applyTo(composite);

    Group group = new Group(composite, SWT.NONE);
    group.setText("Enable Pylint");
    GridLayoutFactory.swtDefaults().extendedMargins(0, 0, 0, 3).applyTo(group);
    GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.FILL).applyTo(group);

    analyzeProjectButton = new Button(group, SWT.CHECK);
    analyzeProjectButton.setText("Analyze this project with Pylint");
    analyzeProjectButton.addSelectionListener(this);

    group = new Group(composite, SWT.NONE);
    group.setText("Pylint settings");
    GridLayoutFactory.swtDefaults().numColumns(2).applyTo(group);
    GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.FILL).applyTo(group);

    overrideButton = new Button(group, SWT.CHECK);
    overrideButton.setText("Enable project specific settings");
    overrideButton.addSelectionListener(this);

    Link link = new Link(group, SWT.NONE);
    link.setText("<a>Configure Workspace Settings...</a>");
    GridDataFactory.fillDefaults().grab(true, false).align(SWT.END, SWT.BEGINNING).applyTo(link);
    link.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        PreferenceDialog dialog = PreferencesUtil.createPreferenceDialogOn(getShell(),
            PythonUIPlugin.PREF_PAGE_ID, null, null);

        dialog.open();
      }
    });

    Label separator = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
    GridDataFactory.fillDefaults().grab(true, false).span(2, 1).align(SWT.FILL, SWT.BEGINNING).applyTo(
        separator);

    Composite subComposite = new Composite(group, SWT.NONE);
    GridLayoutFactory.fillDefaults().numColumns(2).applyTo(subComposite);
    GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.FILL).applyTo(subComposite);

    Label label = new Label(subComposite, SWT.NONE);
    label.setText("Configuration:");
    GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).applyTo(label);

    combo = new Combo(subComposite, SWT.DROP_DOWN);
    GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).applyTo(combo);

    for (IPylintConfig config : PylintConfigManager.getAllConfigs()) {
      combo.add(config.getName());
    }

    IProject project = getProject();

    analyzeProjectButton.setSelection(PythonCorePlugin.getPlugin().isAnalysisEnabled(project));
    overrideButton.setSelection(PythonCorePlugin.getPlugin().isProjectOverride(project));

    String configName = PythonCorePlugin.getPlugin().getProjectPreferences(project).get(
        PythonCorePlugin.PYLINT_CONFIG_PREF, null);
    IPylintConfig config = PylintConfigManager.getConfig(configName);

    if (config == null) {
      config = PythonCorePlugin.getPlugin().getPylintConfig();
    }

    if (config != null) {
      int index = combo.indexOf(config.getName());

      if (index != -1) {
        combo.select(index);
      }
    }

    updateControlStates();

    return composite;
  }

  protected IProject getProject() {
    return (IProject) getElement().getAdapter(IProject.class);
  }

  private void updateControlStates() {
    overrideButton.setEnabled(analyzeProjectButton.getSelection());
    combo.setEnabled(analyzeProjectButton.getSelection() && overrideButton.getSelection());
  }

}
