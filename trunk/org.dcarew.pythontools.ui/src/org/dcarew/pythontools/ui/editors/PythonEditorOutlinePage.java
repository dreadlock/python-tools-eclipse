package org.dcarew.pythontools.ui.editors;

import org.dcarew.pythontools.core.parser.PyNode;
import org.dcarew.pythontools.ui.PythonUIPlugin;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

/**
 * The outline page for the Python editor.
 */
public class PythonEditorOutlinePage extends ContentOutlinePage {
	private PythonEditor editor;

	/**
	 * Create a new GoEditorOutlinePage.
	 * 
	 * @param documentProvider
	 * @param editor
	 */
	public PythonEditorOutlinePage(PythonEditor editor) {
		this.editor = editor;
	}

	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		
		getTreeViewer().setContentProvider(new PyNodeContentProvider());
		// DecoratingStyledCellLabelProvider?
		getTreeViewer().setLabelProvider(new DelegatingStyledCellLabelProvider(new PyNodeLabelProvider()));
    getTreeViewer().setInput(editor.getModel());

    // TODO: do we want to do this, or let the selection synchronization handle it?
    getTreeViewer().expandToLevel(2);

    getTreeViewer().addSelectionChangedListener(new ISelectionChangedListener() {
      @Override
      public void selectionChanged(SelectionChangedEvent event) {
        handleTreeViewerSelectionChanged(event.getSelection());
      }
    });
  }

  protected void handleEditorReconcilation() {
    if (!getControl().isDisposed()) {
      refreshAsync();
    }
  }

  protected void handleTreeViewerSelectionChanged(ISelection selection) {
    if (selection instanceof IStructuredSelection) {
      Object sel = ((IStructuredSelection) selection).getFirstElement();

      if (sel instanceof PyNode) {
        PyNode node = (PyNode) sel;

        editor.selectAndReveal(node);
      }
    }
  }

  private void refresh() {
    try {
      if (!getTreeViewer().getControl().isDisposed()) {
        getTreeViewer().refresh(editor.getModel());
      }
    } catch (Throwable exception) {
      PythonUIPlugin.logError(exception);

      getTreeViewer().setInput(null);
    }
  }

  private void refreshAsync() {
    Display.getDefault().asyncExec(new Runnable() {
      @Override
      public void run() {
        refresh();
      }
    });
  }
  
}
