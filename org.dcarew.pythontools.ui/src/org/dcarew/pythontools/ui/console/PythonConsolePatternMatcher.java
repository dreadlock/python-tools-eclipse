package org.dcarew.pythontools.ui.console;

import org.dcarew.pythontools.ui.PythonUIPlugin;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileInfo;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.IHyperlink;
import org.eclipse.ui.console.IPatternMatchListenerDelegate;
import org.eclipse.ui.console.PatternMatchEvent;
import org.eclipse.ui.console.TextConsole;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Traceback (most recent call last): File "scripts/pyparse.py", line 51, in <module>
 * sys.exit(main(sys.argv[0])) File "scripts/pyparse.py", line 47, in main myFoofoo.visit(astNode)
 * File "/System/Library/Frameworks/Python.framework/Versions/2.7/lib/python2.7/ast.py", line 241,
 * in visit return visitor(node) File "scripts/pyparse.py", line 17, in visit_Module
 * ast.NodeVisitor.generic_visit(self, node) File
 * "/System/Library/Frameworks/Python.framework/Versions/2.7/lib/python2.7/ast.py", line 249, in
 * generic_visit self.visit(item) File
 * "/System/Library/Frameworks/Python.framework/Versions/2.7/lib/python2.7/ast.py", line 241, in
 * visit return visitor(node) File "scripts/pyparse.py", line 32, in visit_ClassDef
 * ast.NodeVisitor.generic_visit(self, node) File
 * "/System/Library/Frameworks/Python.framework/Versions/2.7/lib/python2.7/ast.py", line 249, in
 * generic_visit self.visit(item) File
 * "/System/Library/Frameworks/Python.framework/Versions/2.7/lib/python2.7/ast.py", line 241, in
 * visit return visitor(node) File "scripts/pyparse.py", line 29, in visit_Attribute print
 * '{attribute=%s' % node.name AttributeError: 'Attribute' object has no attribute 'name'
 */

public class PythonConsolePatternMatcher implements IPatternMatchListenerDelegate {
  private TextConsole console;

  @Override
  public void connect(TextConsole console) {
    this.console = console;
  }

  @Override
  public void disconnect() {
    this.console = null;
  }

  // "foo2.py", line 33
  private static Pattern pattern = Pattern.compile("(.*\")(.*\\.py)(\", line )(\\d+)(,.*)");

  @Override
  public void matchFound(PatternMatchEvent event) {
    try {
      String match = console.getDocument().get(event.getOffset(), event.getLength());

      Matcher matcher = pattern.matcher(match);

      if (matcher.matches()) {
        String path = matcher.group(2);
        String lineStr = matcher.group(4);

        int line = 1;

        try {
          line = Integer.parseInt(lineStr);
        } catch (NumberFormatException nfe) {

        }

        int matchOffset = event.getOffset() + matcher.group(1).length();
        int matchLength = path.length();

        IFile file = locateFile(path);

        if (file != null) {
          console.addHyperlink(new PythonConsoleHyperlink(file, line), matchOffset, matchLength);
        } else {
          IFileStore fileStore = locateFileStore(path);

          if (fileStore != null) {
            console.addHyperlink(new PythonConsoleHyperlink(fileStore, line), matchOffset,
                matchLength);
          }
        }
      }
    } catch (BadLocationException e) {

    }
  }

  private IFileStore locateFileStore(String path) {
    IFileStore store = EFS.getLocalFileSystem().getStore(Path.fromOSString(path));
    IFileInfo info = store.fetchInfo();

    if (info.exists()) {
      return store;
    } else {
      return null;
    }
  }

  private IFile locateFile(String path) {
    IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

    for (IProject project : root.getProjects()) {
      IFile file = project.getFile(Path.fromOSString(path));

      if (file != null && file.exists()) {
        return file;
      }
    }

    return null;
  }

  class PythonConsoleHyperlink implements IHyperlink {
    private IFile file;
    private IFileStore fileStore;
    private int line;

    public PythonConsoleHyperlink(IFile file, int line) {
      this.file = file;
      this.line = line;
    }

    public PythonConsoleHyperlink(IFileStore fileStore, int line) {
      this.fileStore = fileStore;
      this.line = line;
    }

    @Override
    public void linkActivated() {
      IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

      if (file != null) {
        try {
          ITextEditor editor = (ITextEditor) IDE.openEditor(page, file);

          gotoLine(page, editor, line);
        } catch (PartInitException ex) {
          Display.getDefault().beep();
        }
      }

      if (fileStore != null) {
        try {
          ITextEditor editor = (ITextEditor) IDE.openEditor(page, new FileStoreEditorInput(
              fileStore), PythonUIPlugin.EDITOR_ID);

          gotoLine(page, editor, line);
        } catch (PartInitException ex) {
          Display.getDefault().beep();
        }
      }
    }

    @Override
    public void linkEntered() {

    }

    @Override
    public void linkExited() {

    }
  }

  private static void gotoLine(IWorkbenchPage page, ITextEditor editor, int line) {
    try {
      IDocumentProvider provider = editor.getDocumentProvider();
      IDocument document = provider.getDocument(editor.getEditorInput());
      int offset = document.getLineOffset(line - 1);
      editor.selectAndReveal(offset, 0);
      if (page.getActiveEditor() != editor) {
        page.activate(editor);
      }
    } catch (BadLocationException x) {

    }
  }

}
