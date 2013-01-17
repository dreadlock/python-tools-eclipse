package org.dcarew.pythontools.core.debugger;

import java.util.List;

/**
 * A listener for VM debugging events.
 */
public interface PyListener {

  public static enum PausedReason {
    breakpoint, exception, interrupted, unknown;

    public static PausedReason parse(String str) {
      try {
        return valueOf(str);
      } catch (Throwable t) {
        return unknown;
      }
    }
  }

  /**
   * Handle the debugger paused event.
   * 
   * @param reason possible values are "breakpoint" and "exception"
   * @param isolate
   * @param frames
   * @param exception can be null
   */
  public void debuggerPaused(PausedReason reason, List<PyCallFrame> frames, PyValue exception);

  /**
   * Handle the debugger resumed event.
   */
  public void debuggerResumed();

}
