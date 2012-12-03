package org.dcarew.pythontools.core.building;

import org.dcarew.pythontools.core.PythonCorePlugin;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.jobs.Job;

import java.util.ArrayList;
import java.util.List;

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

                if (PythonCorePlugin.isPythonFile(file)) {
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

              if (delta.getKind() == IResourceDelta.ADDED) {
                if (PythonCorePlugin.isPythonFile(file)) {
                  files.add(file);
                }
              } else if (delta.getKind() == IResourceDelta.CHANGED) {
                if (PythonCorePlugin.isPythonFile(file)) {
                  if (delta.getFlags() != IResourceDelta.MARKERS) {
                    files.add(file);
                  }
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
    if (files.size() == 0) {
      return;
    }

    // Analyze in a job.
    Job job = new PylintJob(files.get(0).getProject(), files);
    job.schedule();
  }

}
