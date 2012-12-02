package org.dcarew.pythontools.ui.launching;

import org.dcarew.pythontools.core.launching.PythonLaunchConfigWrapper;
import org.dcarew.pythontools.ui.PythonUIPlugin;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.PixelConverter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.FilteredResourcesSelectionDialog;

public class PythonLaunchConfigurationTab extends AbstractLaunchConfigurationTab {
  private Text scriptPath;
  private Text scriptArguments;
  private Text cwdPath;

  private Text interpreterArguments;

  private ModifyListener textModifyListener = new ModifyListener() {
    @Override
    public void modifyText(ModifyEvent e) {
      notifyPanelChanged();
    }
  };

  public PythonLaunchConfigurationTab() {

  }

  @Override
  public void createControl(Composite parent) {
    Composite composite = new Composite(parent, SWT.NONE);
    GridLayoutFactory.swtDefaults().applyTo(composite);

    // Script group
    Group scriptGroup = new Group(composite, SWT.NONE);
    scriptGroup.setText("Application settings");
    GridDataFactory.fillDefaults().grab(true, false).applyTo(scriptGroup);
    GridLayoutFactory.swtDefaults().numColumns(3).applyTo(scriptGroup);
    ((GridLayout) scriptGroup.getLayout()).marginBottom = 5;

    Label label = new Label(scriptGroup, SWT.NONE);
    label.setText("Python script:");

    scriptPath = new Text(scriptGroup, SWT.SINGLE | SWT.BORDER);
    scriptPath.addModifyListener(textModifyListener);
    GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.CENTER).hint(100, -1).applyTo(
        scriptPath);

    final Button scriptPathButton = new Button(scriptGroup, SWT.PUSH);
    scriptPathButton.setText("Browse...");
    PixelConverter converter = new PixelConverter(scriptPathButton);
    int widthHint = converter.convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
    GridDataFactory.swtDefaults().hint(widthHint, -1).applyTo(scriptPathButton);
    scriptPathButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        FilteredResourcesSelectionDialog dialog = new FilteredResourcesSelectionDialog(getShell(),
            false, ResourcesPlugin.getWorkspace().getRoot(), IResource.FILE);
        dialog.setTitle("Select Python Script");
        dialog.setInitialPattern(".py");
        
        if (Window.OK == dialog.open()) {
          IResource resource = (IResource) dialog.getResult()[0];

          scriptPath.setText(resource.getFullPath().toString());
        }
      }
    });

    label = new Label(scriptGroup, SWT.NONE);
    label.setText("Arguments:");

    scriptArguments = new Text(scriptGroup, SWT.SINGLE | SWT.BORDER);
    scriptArguments.addModifyListener(textModifyListener);
    GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.CENTER).hint(100, -1).applyTo(
        scriptArguments);

    // spacer
    new Label(scriptGroup, SWT.NONE);

    label = new Label(scriptGroup, SWT.NONE);
    label.setText("Working directory:");

    cwdPath = new Text(scriptGroup, SWT.SINGLE | SWT.BORDER);
    cwdPath.addModifyListener(textModifyListener);
    GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.CENTER).hint(100, -1).applyTo(
        cwdPath);

    final Button cwdPathButton = new Button(scriptGroup, SWT.PUSH);
    cwdPathButton.setText("Browse...");
    GridDataFactory.swtDefaults().hint(widthHint, -1).applyTo(cwdPathButton);
    cwdPathButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        DirectoryDialog dialog = new DirectoryDialog(getShell(), SWT.OPEN);
        dialog.setText("Select Current Working Directory");
        String path = dialog.open();

        if (path != null) {
          cwdPath.setText(path);
        }
      }
    });

    // Interpreter group
    Group interpreterGroup = new Group(composite, SWT.NONE);
    interpreterGroup.setText("Python interpreter settings");
    GridDataFactory.fillDefaults().grab(true, false).applyTo(interpreterGroup);
    GridLayoutFactory.swtDefaults().numColumns(3).applyTo(interpreterGroup);
    ((GridLayout) interpreterGroup.getLayout()).marginBottom = 5;

    label = new Label(interpreterGroup, SWT.NONE);
    label.setText("Interpreter arguments:");

    interpreterArguments = new Text(interpreterGroup, SWT.SINGLE | SWT.BORDER);
    interpreterArguments.addModifyListener(textModifyListener);
    GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.CENTER).hint(100, -1).applyTo(
        interpreterArguments);

    // spacer
    label = new Label(interpreterGroup, SWT.NONE);
    GridDataFactory.swtDefaults().hint(widthHint, -1).applyTo(label);

    setControl(composite);
  }

  @Override
  public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {

  }

  @Override
  public void initializeFrom(ILaunchConfiguration configuration) {
    PythonLaunchConfigWrapper wrapper = new PythonLaunchConfigWrapper(configuration);

    scriptPath.setText(wrapper.getResourcePath());
    scriptArguments.setText(wrapper.getScriptArguments());
    cwdPath.setText(wrapper.getWorkingDirectory());
    interpreterArguments.setText(wrapper.getInterpreterArguments());
  }

  @Override
  public String getMessage() {
    return "Configure a Python script to run.";
  }

  @Override
  public void performApply(ILaunchConfigurationWorkingCopy configuration) {
    PythonLaunchConfigWrapper wrapper = new PythonLaunchConfigWrapper(configuration);

    wrapper.setResourcePath(scriptPath.getText());
    wrapper.setScriptArguments(scriptArguments.getText());
    wrapper.setWorkingDirectory(cwdPath.getText());
    wrapper.setInterpreterArguments(interpreterArguments.getText());
  }

  @Override
  public String getName() {
    return "Main";
  }

  @Override
  public Image getImage() {
    return PythonUIPlugin.getImage("icons/python_16.png");
  }

  @Override
  public boolean isValid(ILaunchConfiguration launchConfig) {
    return getErrorMessage() == null;
  }

  @Override
  public String getErrorMessage() {
    // Check that the script name is not empty.
    if (scriptPath.getText().length() == 0) {
      return "Please select a Python script.";
    }

    return null;
  }

  private void notifyPanelChanged() {
    setDirty(true);

    updateLaunchConfigurationDialog();
  }

}
