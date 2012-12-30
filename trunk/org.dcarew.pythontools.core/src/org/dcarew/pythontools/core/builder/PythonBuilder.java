package org.dcarew.pythontools.core.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dcarew.pythontools.core.PythonCorePlugin;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public class PythonBuilder extends IncrementalProjectBuilder {
  public static final String BUILDER_ID = "org.dcarew.pythontools.core.pythonBuilder";

  public PythonBuilder() {

  }

  @Override
  protected final IProject[] build(final int kind, Map<String, String> args,
      IProgressMonitor monitor) throws CoreException {
    if (kind == INCREMENTAL_BUILD || kind == AUTO_BUILD) {
      return doIncrementalBuild(getProject(), getDelta(getProject()), monitor);
    } else if (kind == CLEAN_BUILD) {
      doClean(getProject(), monitor);
      return null;
    } else if (kind == FULL_BUILD) {
      return doFullBuild(getProject(), monitor);
    } else {
      throw new IllegalArgumentException("build kind not expected: " + kind);
    }
  }

  @Override
  protected final void clean(IProgressMonitor monitor) throws CoreException {
    doClean(getProject(), monitor);
  }

  protected void doClean(IProject project, IProgressMonitor monitor) {
    MarkerUtils.clearMarkers(project);

    monitor.done();
  }

  protected IProject[] doFullBuild(IProject project, IProgressMonitor monitor) throws CoreException {
    if (!PythonCorePlugin.getPlugin().isAnalysisEnabled(project)) {
      monitor.done();
      return null;
    }

    final List<IFile> files = new ArrayList<IFile>();

    project.accept(new IResourceVisitor() {
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

    process(files, monitor);

    return null;
  }

  protected IProject[] doIncrementalBuild(IProject project, IResourceDelta delta,
      IProgressMonitor monitor) throws CoreException {
    if (!PythonCorePlugin.getPlugin().isAnalysisEnabled(project)) {
      monitor.done();
      return null;
    }

    final List<IFile> files = new ArrayList<IFile>();

    delta.accept(new IResourceDeltaVisitor() {
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

    process(files, monitor);

    return null;
  }

  private void process(List<IFile> files, IProgressMonitor monitor) {
    monitor.beginTask("pylint", files.size());

    try {
      for (IFile file : files) {
        monitor.worked(1);

        PylintJob.process(file);
      }
    } finally {
      monitor.done();
    }
  }

}
