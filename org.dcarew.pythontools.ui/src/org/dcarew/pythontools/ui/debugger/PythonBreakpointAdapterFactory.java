package org.dcarew.pythontools.ui.debugger;

import org.dcarew.pythontools.core.PythonCorePlugin;
import org.dcarew.pythontools.ui.editors.PythonEditor;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.debug.ui.actions.IToggleBreakpointsTarget;

public class PythonBreakpointAdapterFactory implements IAdapterFactory {

  @SuppressWarnings("rawtypes")
  @Override
  public Object getAdapter(Object adaptableObject, Class adapterType) {
    if (adapterType == IToggleBreakpointsTarget.class && adaptableObject instanceof PythonEditor) {
      PythonEditor editor = (PythonEditor) adaptableObject;
      IResource resource = (IResource) editor.getEditorInput().getAdapter(IResource.class);

      if (resource instanceof IFile && PythonCorePlugin.isPythonFile((IFile) resource)) {
        return new PythonToggleBreakpointDelegate();
      }
    }

    return null;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public Class[] getAdapterList() {
    return new Class[] {IToggleBreakpointsTarget.class};
  }

}
