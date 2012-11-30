package org.dcarew.pythontools.ui.editors;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;

/**
 * This IReconcilingStrategy notifies the editor that a reconcile is occurring.
 * 
 * @see GoEditorSourceViewerConfiguration
 */
public class PythonEditorReconcilingStrategy implements IReconcilingStrategy {
	private PythonEditor editor;

	/**
	 * Create a new GoEditorReconcilingStrategy.
	 * 
	 * @param editor
	 */
	public PythonEditorReconcilingStrategy(PythonEditor editor) {
		this.editor = editor;
	}

	@Override
	public void setDocument(IDocument document) {

	}

	@Override
	public void reconcile(DirtyRegion dirtyRegion, IRegion subRegion) {
		// This really won't get called, as we indicate that we don't support incremental
		// reconciliation.
		editor.handleReconcilation(null);
	}

	@Override
	public void reconcile(IRegion partition) {
		editor.handleReconcilation(partition);
	}

}
