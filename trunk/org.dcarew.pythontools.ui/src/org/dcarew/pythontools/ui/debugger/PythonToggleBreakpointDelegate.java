package org.dcarew.pythontools.ui.debugger;

import org.dcarew.pythontools.core.PythonCorePlugin;
import org.dcarew.pythontools.core.debugger.PythonBreakpoint;
import org.dcarew.pythontools.ui.editors.PythonEditor;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.ILineBreakpoint;
import org.eclipse.debug.ui.actions.IToggleBreakpointsTarget;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;

public class PythonToggleBreakpointDelegate implements IToggleBreakpointsTarget {

	@Override
	public void toggleLineBreakpoints(IWorkbenchPart part, ISelection selection)
			throws CoreException {
	  PythonEditor editor = (PythonEditor) part;

		if (editor != null) {
			IResource resource = (IResource) editor.getEditorInput().getAdapter(IResource.class);
			ITextSelection textSelection = (ITextSelection) selection;
			int lineNumber = textSelection.getStartLine();

			IBreakpoint[] breakpoints = DebugPlugin.getDefault().getBreakpointManager()
					.getBreakpoints(PythonCorePlugin.PLUGIN_ID);

			for (int i = 0; i < breakpoints.length; i++) {
				IBreakpoint breakpoint = breakpoints[i];
				if (resource.equals(breakpoint.getMarker().getResource())) {
					if (((ILineBreakpoint) breakpoint).getLineNumber() == lineNumber + 1) {
						breakpoint.delete();
						return;
					}
				}
			}

			PythonBreakpoint breakpoint = new PythonBreakpoint(resource, lineNumber + 1);
			DebugPlugin.getDefault().getBreakpointManager().addBreakpoint(breakpoint);
		}
	}

	@Override
	public boolean canToggleLineBreakpoints(IWorkbenchPart part, ISelection selection) {
		return part instanceof PythonEditor;
	}

	@Override
	public void toggleMethodBreakpoints(IWorkbenchPart part, ISelection selection)
			throws CoreException {

	}

	@Override
	public boolean canToggleMethodBreakpoints(IWorkbenchPart part, ISelection selection) {
		return false;
	}

	@Override
	public void toggleWatchpoints(IWorkbenchPart part, ISelection selection) throws CoreException {

	}

	@Override
	public boolean canToggleWatchpoints(IWorkbenchPart part, ISelection selection) {
		return false;
	}

}
