package org.dcarew.pythontools.core.builder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

public class PythonNature implements IProjectNature {
  public static final String NATURE_ID = "org.dcarew.pythontools.core.pythonNature";

  public static void addBuilderToProject(IProject project) throws CoreException {
    if (!hasPythonBuilder(project)) {
      PythonNature nature = new PythonNature();
      nature.setProject(project);
      nature.configure();
    }
  }

  public static void addNatureToProject(IProject project) throws CoreException {
    IProjectDescription description = project.getDescription();

    Set<String> natures = new HashSet<String>(Arrays.asList(description.getNatureIds()));

    natures.add(NATURE_ID);

    description.setNatureIds(natures.toArray(new String[natures.size()]));

    project.setDescription(description, null);
  }

  public static boolean hasPythonBuilder(IProject project) {
    try {
      IProjectDescription desc = project.getDescription();
      ICommand[] commands = desc.getBuildSpec();

      for (int i = 0; i < commands.length; ++i) {
        if (commands[i].getBuilderName().equals(PythonBuilder.BUILDER_ID)) {
          return true;
        }
      }
    } catch (CoreException ce) {

    }

    return false;
  }

  public static boolean isPythonProject(IProject project) {
    try {
      IProjectDescription description = project.getDescription();

      return Arrays.asList(description.getNatureIds()).contains(NATURE_ID);
    } catch (CoreException ce) {
      return false;
    }
  }

  public static void removeBuilderFromProject(IProject project) throws CoreException {
    if (!hasPythonBuilder(project)) {
      PythonNature nature = new PythonNature();
      nature.setProject(project);
      nature.deconfigure();
    }
  }

  private IProject project;

  public PythonNature() {

  }

  @Override
  public void configure() throws CoreException {
    IProjectDescription desc = project.getDescription();
    ICommand[] commands = desc.getBuildSpec();

    if (hasPythonBuilder(project)) {
      return;
    }

    ICommand[] newCommands = new ICommand[commands.length + 1];
    System.arraycopy(commands, 0, newCommands, 0, commands.length);
    ICommand command = desc.newCommand();
    command.setBuilderName(PythonBuilder.BUILDER_ID);
    newCommands[newCommands.length - 1] = command;
    desc.setBuildSpec(newCommands);
    project.setDescription(desc, null);
  }

  @Override
  public void deconfigure() throws CoreException {
    IProjectDescription description = getProject().getDescription();
    ICommand[] commands = description.getBuildSpec();
    for (int i = 0; i < commands.length; ++i) {
      if (commands[i].getBuilderName().equals(PythonBuilder.BUILDER_ID)) {
        ICommand[] newCommands = new ICommand[commands.length - 1];
        System.arraycopy(commands, 0, newCommands, 0, i);
        System.arraycopy(commands, i + 1, newCommands, i, commands.length - i - 1);
        description.setBuildSpec(newCommands);
        project.setDescription(description, null);
        return;
      }
    }
  }

  @Override
  public IProject getProject() {
    return project;
  }

  @Override
  public void setProject(IProject project) {
    this.project = project;
  }

}
