package org.dcarew.pythontools.ui.editors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;

public class PythonDocumentProvider extends TextFileDocumentProvider {

  public PythonDocumentProvider() {

  }

  @Override
  protected FileInfo createFileInfo(Object element) throws CoreException {
    FileInfo info = super.createFileInfo(element);

    if (info == null) {
      info = createEmptyFileInfo();
    }

    IDocument document = info.fTextFileBuffer.getDocument();

    if (document != null) {
      IDocumentPartitioner partitioner = new FastPartitioner(new PythonPartitionScanner(),
          new String[] {
              IDocument.DEFAULT_CONTENT_TYPE, PythonPartitionScanner.PYTHON_COMMENT,
              PythonPartitionScanner.PYTHON_STRING});
      partitioner.connect(document);
      document.setDocumentPartitioner(partitioner);
    }

    return info;
  }

}
