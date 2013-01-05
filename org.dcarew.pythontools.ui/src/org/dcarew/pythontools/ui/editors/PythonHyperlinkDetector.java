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

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.AbstractHyperlinkDetector;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.text.rules.IWordDetector;

class PythonHyperlinkDetector extends AbstractHyperlinkDetector {
  private class MethodHyperlink implements IHyperlink {
    private IRegion region;
    private int index;
    private String text;

    MethodHyperlink(IRegion region, int index, String text) {
      this.region = region;
      this.index = index;
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
      editor.selectAndReveal(index, text.length());
    }
  }

  private PythonEditor editor;

  public PythonHyperlinkDetector(PythonEditor editor) {
    this.editor = editor;
  }

  @Override
  public IHyperlink[] detectHyperlinks(ITextViewer textViewer, IRegion region,
      boolean canShowMultipleHyperlinks) {
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

      String word = document.get(strRegion.getOffset(), strRegion.getLength() + 1);

      if (word.endsWith("(")) {
        word = word.substring(0, word.length() - 1);

        int index = document.search(0, "def " + word + "(", true, true, false);

        if (index != -1) {
          index += 4;

          return new IHyperlink[] {new MethodHyperlink(strRegion, index, word)};
        }
      }
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

}
