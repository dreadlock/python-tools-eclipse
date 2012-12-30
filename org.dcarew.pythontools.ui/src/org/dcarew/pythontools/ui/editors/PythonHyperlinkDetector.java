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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.AbstractHyperlinkDetector;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.text.rules.IWordDetector;

// TODO: use a word detector
// TODO: only in the default partition type

class PythonHyperlinkDetector extends AbstractHyperlinkDetector {
  private static class MethodHyperlink implements IHyperlink {
    private IRegion region;
    private String text;

    MethodHyperlink(IRegion region, String text) {
      this.region = region;
      this.text = text;
    }

    @Override
    public IRegion getHyperlinkRegion() {
      return region;
    }

    @Override
    public String getHyperlinkText() {
      return text;
    }

    @Override
    public String getTypeLabel() {
      return "Python";
    }

    @Override
    public void open() {
      // TODO:

    }
  }

  private PythonEditor editor;

  public PythonHyperlinkDetector(PythonEditor editor) {
    this.editor = editor;
  }

  @Override
  public IHyperlink[] detectHyperlinks(ITextViewer textViewer, IRegion region,
      boolean canShowMultipleHyperlinks) {
    // TODO:
    if (true) {
      return null;
    }

    if (region == null || textViewer == null || editor == null) {
      return null;
    }

    IDocument document = textViewer.getDocument();

    // Only try and hyper-link in the default partition content type.
    try {
      ITypedRegion type = document.getPartition(region.getOffset());

      if (type.getType() != IDocument.DEFAULT_CONTENT_TYPE) {
        return null;
      }
    } catch (BadLocationException ex) {
      return null;
    }

    try {
      IRegion strRegion = getCurrentWord(document, region.getOffset(), new PythonWordDetector());

      if (strRegion == null) {
        return null;
      }

      return new IHyperlink[] {new MethodHyperlink(strRegion, document.get(strRegion.getOffset(),
          strRegion.getLength()))};

//      // TODO: is there a link to this symbol in the file?
//      IFile file = getFileFor(textViewer,
//          document.get(strRegion.getOffset(), strRegion.getLength()));
//
//      if (file != null) {
//        // TODO:
//        //return new IHyperlink[] {new FileHyperlink(strRegion, file)};
//      }
    } catch (BadLocationException e) {

    }

    return null;
  }

  private IRegion getCurrentWord(IDocument document, int offset, IWordDetector wordDetector)
      throws BadLocationException {
    IRegion border = document.getLineInformationOfOffset(offset);

    int start = offset;
    int end = offset;

    char c = document.getChar(start);

    while (wordDetector.isWordStart(c)) {
      start--;

      if (start < border.getOffset()) {
        return null;
      }

      c = document.getChar(start);
    }

    c = document.getChar(end);

    while (wordDetector.isWordPart(c)) {
      end++;

      if (end > (border.getOffset() + border.getLength())) {
        return null;
      }

      c = document.getChar(end);
    }

    return new Region(start + 1, end - start - 1);
  }

  private IFile getFileFor(ITextViewer textViewer, String path) {
    IFile baseFile = (IFile) editor.getEditorInput().getAdapter(IFile.class);

    if (baseFile != null) {
      IResource resource = baseFile.getParent().findMember(new Path(path));

      if (resource instanceof IFile && resource.exists()) {
        return (IFile) resource;
      }
    }

    return null;
  }

}
