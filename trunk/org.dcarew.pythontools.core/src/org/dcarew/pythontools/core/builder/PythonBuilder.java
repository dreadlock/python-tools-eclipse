package org.dcarew.pythontools.core.builder;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import java.util.Map;

public class PythonBuilder extends IncrementalProjectBuilder {
  public static final String BUILDER_ID = "org.dcarew.pythontools.core.pythonBuilder";

  public PythonBuilder() {

  }

  @Override
  protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor)
      throws CoreException {
    // TODO:

    return null;
  }

}
