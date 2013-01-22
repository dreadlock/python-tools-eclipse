package org.dcarew.pythontools.core.debugger;

import java.io.IOException;

import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IMemoryBlock;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IThread;

public class PyDebugTarget extends PyDebugElement implements IDebugTarget {
  private IProcess process;
  private PyConnection connection;

  public PyDebugTarget(IProcess process) {
    super(null);
  }

  @Override
  public void breakpointAdded(IBreakpoint breakpoint) {
    // TODO:

  }

  @Override
  public void breakpointChanged(IBreakpoint breakpoint, IMarkerDelta delta) {
    // TODO:

  }

  @Override
  public void breakpointRemoved(IBreakpoint breakpoint, IMarkerDelta delta) {
    // TODO:

  }

  @Override
  public boolean canDisconnect() {
    // TODO:
    return false;
  }

  @Override
  public boolean canResume() {
    // TODO:
    return false;
  }

  @Override
  public boolean canSuspend() {
    // TODO:
    return false;
  }

  @Override
  public boolean canTerminate() {
    // TODO:
    return false;
  }

  public void connect(ILaunch launch, int port) throws DebugException {
    final long timeout = 10000;

    PyConnection connection = new PyConnection();

    long startTime = System.currentTimeMillis();

    try {
      sleep(50);

      while (true) {
        try {
          connection.connect(port);

          break;
        } catch (IOException ioe) {
          if (process.isTerminated()) {
            throw ioe;
          }

          if (System.currentTimeMillis() > startTime + timeout) {
            throw ioe;
          } else {
            sleep(25);
          }
        }
      }
    } catch (IOException ioe) {
      throw new DebugException(new Status(IStatus.ERROR, getModelIdentifier(),
          "Unable to connect to the debugger: " + ioe.getMessage()));
    }

    launch.addDebugTarget(this);

    try {
      connection.sendRunCommand();
    } catch (IOException e) {
      throw new DebugException(new Status(IStatus.ERROR, getModelIdentifier(),
          "Unable to connect to the debugger: " + e.getMessage()));
    }
  }

  @Override
  public void disconnect() throws DebugException {
    // TODO:

  }

  @Override
  public PyConnection getConnection() {
    return connection;
  }

  @Override
  public IDebugTarget getDebugTarget() {
    return this;
  }

  @Override
  public IMemoryBlock getMemoryBlock(long startAddress, long length) throws DebugException {
    // TODO:
    return null;
  }

  @Override
  public String getName() throws DebugException {
    // TODO:
    return null;
  }

  @Override
  public IProcess getProcess() {
    return process;
  }

  @Override
  public IThread[] getThreads() throws DebugException {
    // TODO:
    return null;
  }

  @Override
  public boolean hasThreads() throws DebugException {
    // TODO:
    return false;
  }

  @Override
  public boolean isDisconnected() {
    // TODO:
    return false;
  }

  @Override
  public boolean isSuspended() {
    // TODO:
    return false;
  }

  @Override
  public boolean isTerminated() {
    // TODO:
    return false;
  }

  @Override
  public void resume() throws DebugException {
    // TODO:

  }

  @Override
  public boolean supportsBreakpoint(IBreakpoint breakpoint) {
    // TODO:
    return false;
  }

  @Override
  public boolean supportsStorageRetrieval() {
    // TODO:
    return false;
  }

  @Override
  public void suspend() throws DebugException {
    // TODO:

  }

  @Override
  public void terminate() throws DebugException {
    // TODO:

  }

  private void sleep(int millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {

    }
  }

}
