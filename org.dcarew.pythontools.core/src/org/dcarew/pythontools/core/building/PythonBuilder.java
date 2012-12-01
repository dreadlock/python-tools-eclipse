package org.dcarew.pythontools.core.building;

import org.dcarew.pythontools.core.PythonCorePlugin;
import org.dcarew.pythontools.core.utils.ProcessRunner;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PythonBuilder implements IResourceChangeListener {
  private static PythonBuilder builder = new PythonBuilder();

  public static PythonBuilder getBuilder() {
    return builder;
  }

  private PythonBuilder() {

  }

  @Override
  public void resourceChanged(IResourceChangeEvent event) {
    final List<IFile> files = new ArrayList<IFile>();

    try {
      if (event.getDelta() == null) {
        if (event.getResource() != null) {
          event.getResource().accept(new IResourceVisitor() {
            @Override
            public boolean visit(IResource resource) throws CoreException {
              if (resource instanceof IFile) {
                IFile file = (IFile) resource;

                if (file.getName().endsWith(".py")) {
                  files.add(file);
                }
              }

              return true;
            }
          });
        }
      } else {
        event.getDelta().accept(new IResourceDeltaVisitor() {
          @Override
          public boolean visit(IResourceDelta delta) throws CoreException {
            if (delta.getResource() instanceof IFile) {
              IFile file = (IFile) delta.getResource();

              if (delta.getKind() == IResourceDelta.ADDED
                  || delta.getKind() == IResourceDelta.CHANGED) {
                if (file.getName().endsWith(".py")) {
                  files.add(file);
                }
              }
            }

            return true;
          }
        });
      }
    } catch (CoreException e) {
      PythonCorePlugin.logError(e);
    }

    process(files);
  }

  private void process(List<IFile> files) {
    for (IFile file : files) {
      MarkerUtils.clearMarkers(file);

      ProcessRunner runner = new ProcessRunner(getCwd(file), "/Users/devoncarew/pythonpath/pylint",
          "-f", "parseable", "-r", "n", "-i", "y", "--indent-string=\"  \"", file.getName());

      try {
        int exit = runner.execute();

        System.out.println("pylint exit with code " + exit);

        if (exit > 0 && exit < 32) {
          processOutput(file, runner.getStdout());
        }
      } catch (IOException e) {
        PythonCorePlugin.logError(e);
      }
    }
  }

  /*
   * build.py:757: [C] Line too long (85/80) build.py:1: [C] Too many lines in module (1063)
   * build.py:225: [W] TODO(devoncarew): remove this hardcoded e:\ path build.py:690: [W]
   * TODO(devoncarew): add DumpRenderTree.app as well? build.py:816: [W] TODO(pquitslund): migrate
   * to a bucket copy (rather than serial uploads) build.py:1: [C] Missing docstring build.py:34:
   * [C] Invalid name "utils" for type constant (should match (([A-Z_][A-Z0-9_]*)|(__.*__))$)
   * build.py:55: [R, AntWrapper.RunAnt] Too many arguments (13/5) build.py:55: [R,
   * AntWrapper.RunAnt] Too many local variables (21/15) build.py:55: [C, AntWrapper.RunAnt] Invalid
   * name "RunAnt" for type method (should match [a-z_][a-z0-9_]{2,30}$) build.py:55: [R,
   * AntWrapper.RunAnt] Too many branches (19/12) build.py:55: [R, AntWrapper.RunAnt] Too many
   * statements (58/50) build.py:36: [R, AntWrapper] Too few public methods (1/2)
   */

  private void processOutput(IFile file, String str) throws IOException {
    Pattern pattern = Pattern.compile("(\\S+):(\\d+): \\[(.*)\\] (.*)");

    BufferedReader r = new BufferedReader(new StringReader(str));

    String line = r.readLine();

    while (line != null) {
      if (line.contains(": [")) {
        Matcher matcher = pattern.matcher(line);

        if (matcher.matches()) {
          createMarker(file, matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4));
        }
      }

      line = r.readLine();
    }
  }

  // (C) convention, for programming standard violation
  // (R) refactor, for bad code smell
  // (W) warning, for python specific problems
  // (E) error, for probable bugs in the code
  // (F) fatal, if an error occurred which prevented pylint from doing further processing.

  private void createMarker(IFile file, String fileName, String lineStr, String errorType,
      String message) {
    int line = 1;

    try {
      line = Integer.parseInt(lineStr);
    } catch (NumberFormatException fne) {

    }

    String typeCode = errorType;

    if (typeCode.indexOf(',') != -1) {
      typeCode = typeCode.substring(0, typeCode.indexOf(','));
    }

    // TODO: use typecode to set the severity, and filter some stuff
    int severity = IMarker.SEVERITY_WARNING;
    
    if (typeCode.startsWith("E") || typeCode.startsWith("F")) {
      severity = IMarker.SEVERITY_ERROR;
    }
    
    MarkerUtils.createMarker(severity, file, line, typeCode + ": " + message);
  }

  private File getCwd(IFile file) {
    return file.getParent().getLocation().toFile();
  }

}
