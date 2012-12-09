package org.dcarew.pythontools.ui.resolutions;

import org.dcarew.pythontools.core.builder.MarkerUtils;
import org.dcarew.pythontools.ui.PythonUIPlugin;
import org.eclipse.core.resources.IMarker;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IMarkerResolution2;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.ide.IDE;

// TODO: subclass WorkbenchMarkerResolution?

class IgnoreMarkerResolution implements IMarkerResolution2 {
  private String code;
  private int line;

  IgnoreMarkerResolution(IMarker marker) {
    code = MarkerUtils.getErrorCode(marker);
    line = marker.getAttribute(IMarker.LINE_NUMBER, -1);
  }

  @Override
  public String getDescription() {
    return null;
  }

  @Override
  public Image getImage() {
    return PythonUIPlugin.getImage("icons/obj16/correction_change.gif");
  }

  @Override
  public String getLabel() {
    return "Ignore this instance of " + code;
  }

  @Override
  public void run(IMarker marker) {
    IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

    try {
      IEditorPart editor = IDE.openEditor(page, marker);

      if (editor instanceof TextEditor && line >= 0) {
        apply((TextEditor) editor, code, line);
      }
    } catch (PartInitException ex) {
      // TODO:

      ex.printStackTrace();
    }
  }

  private void apply(TextEditor editor, String code, int line) {
    try {
      // Add " # pylint: disable=R0201" to the end of the line.
      IDocument document = editor.getDocumentProvider().getDocument(editor.getEditorInput());

      IRegion region = document.getLineInformation(line - 1);
      String lineContent = document.get(region.getOffset(), region.getLength());
      String afterHash = null;

      if (lineContent.indexOf('#') != -1) {
        afterHash = lineContent.substring(lineContent.indexOf('#'));
      }

      boolean hasPylintDisable = false;

      if (afterHash != null && afterHash.indexOf("pylint: disable=") != -1) {
        hasPylintDisable = true;
      }

      // The line already contains the proper disable code.
      if (hasPylintDisable && afterHash.indexOf(code) != -1) {
        return;
      }

      String append = null;

      if (hasPylintDisable) {
        append = "," + code;
      } else {
        if (lineContent.endsWith(" ")) {
          append = "# pylint: disable=" + code;
        } else {
          append = " # pylint: disable=" + code;
        }
      }

      editor.selectAndReveal(region.getOffset(), 0);

      document.replace(region.getOffset() + region.getLength(), 0, append);

      editor.selectAndReveal(region.getOffset() + region.getLength(), append.length());
    } catch (BadLocationException e) {
      Display.getDefault().beep();
    }

  }

}
