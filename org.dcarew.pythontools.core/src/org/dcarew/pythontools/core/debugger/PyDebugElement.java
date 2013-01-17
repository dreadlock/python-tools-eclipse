package org.dcarew.pythontools.core.debugger;

import org.dcarew.pythontools.core.PythonCorePlugin;
import org.eclipse.debug.core.model.DebugElement;
import org.eclipse.debug.core.model.IDebugTarget;

public abstract class PyDebugElement extends DebugElement {

  public PyDebugElement(IDebugTarget target) {
    super(target);
  }

  public PyConnection getConnection() {
    return ((PyDebugTarget) getDebugTarget()).getConnection();
  }

  @Override
  public String getModelIdentifier() {
    return PythonCorePlugin.PLUGIN_ID;
  }

  public PyDebugTarget getPyTarget() {
    return (PyDebugTarget) getDebugTarget();
  }

}
