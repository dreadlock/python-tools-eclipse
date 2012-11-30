package org.dcarew.pythontools.ui.editors;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

/**
 * The outline page for the Go editor.
 */
public class PythonEditorOutlinePage extends ContentOutlinePage {
	//private PythonEditor editor;

	/**
	 * Create a new GoEditorOutlinePage.
	 * 
	 * @param documentProvider
	 * @param editor
	 */
	public PythonEditorOutlinePage(PythonEditor editor) {
		//this.editor = editor;
	}

	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		
//		getTreeViewer().setContentProvider(new OutlinePageContentProvider());
//		getTreeViewer().setLabelProvider(new DelegatingStyledCellLabelProvider(new NodeLabelProvider()));
		getTreeViewer().addSelectionChangedListener(this);
		getTreeViewer().addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				handleSelectionChanged();
			}
		});
		
		refresh();
	}

	protected void handleEditorReconcilation() {
		if (!getControl().isDisposed()) {
			refreshAsync();
		}
	}
	
	protected void handleSelectionChanged() {
//		Object sel = ((IStructuredSelection)getTreeViewer().getSelection()).getFirstElement();
//		
//		if (sel instanceof Node) {
//			IDocument document = documentProvider.getDocument(editor.getEditorInput());
//			
//			Node node = (Node)sel;
//			
//			int line = node.getLine() - 1;
//			
//			if (line != -1) {
//				try {
//					editor.selectAndReveal(document.getLineOffset(line), document.getLineLength(line));
//				} catch (BadLocationException ble) {
//					ble.printStackTrace();
//				}
//			}
//		}
	}

	private void refreshAsync() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
            public void run() {
				refresh();
			}
		});
	}
	
	private void refresh() {
//		try {
//			if (!getTreeViewer().getControl().isDisposed()) {
//				IDocument document = documentProvider.getDocument(editor.getEditorInput());
//				
//				if (document != null) {
//					CodeContext codeContext = null;
//					
//					IEditorInput input = editor.getEditorInput();
//					
//					if (input instanceof IFileEditorInput) {
//						IFile file = ((IFileEditorInput)input).getFile();
//						
//						codeContext = CodeContext.getCodeContext(
//								file.getProject(),
//								file.getLocation().toOSString(),
//								document.get(), false);
//						
//					} else if (input instanceof FileStoreEditorInput) {
//						URI uri = ((FileStoreEditorInput)input).getURI();
//						
//						codeContext = CodeContext.getCodeContext(
//								new File(uri).getPath(), document.get(), false);
//					}
//					
//					if (codeContext != null) {
//						if (getTreeViewer().getInput() == null) {
//							getTreeViewer().setInput(codeContext);
//						} else {
//							OutlinePageContentProvider contentProvider =
//								(OutlinePageContentProvider)getTreeViewer().getContentProvider();
//							
//							contentProvider.setCodeContext(codeContext);
//						}
//					}
//				}
//			}
//		}
//		catch (Throwable exception) {
//			Activator.logError(exception);
//			
//			getTreeViewer().setInput(null);
//		}
	}
	
}
