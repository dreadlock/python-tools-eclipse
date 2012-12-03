/*
 * Copyright (c) 2012, the Dart project authors.
 * 
 * Licensed under the Eclipse Public License v1.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
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
          new String[] {PythonPartitionScanner.PYTHON_COMMENT});
      partitioner.connect(document);
      document.setDocumentPartitioner(partitioner);
    }

    return info;
  }

}
