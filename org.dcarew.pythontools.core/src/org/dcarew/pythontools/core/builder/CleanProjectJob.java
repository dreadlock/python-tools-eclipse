package org.dcarew.pythontools.core.builder;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class CleanProjectJob extends WorkspaceJob {
  private IProject project;

  public CleanProjectJob(IProject project) {
    super("Clean " + project.getName());

    this.project = project;

    setRule(project);
  }

  @Override
  public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
    project.build(IncrementalProjectBuilder.CLEAN_BUILD, monitor);

    return Status.OK_STATUS;
  }
}
