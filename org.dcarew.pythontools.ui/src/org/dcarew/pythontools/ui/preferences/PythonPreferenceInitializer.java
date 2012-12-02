package org.dcarew.pythontools.ui.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class PythonPreferenceInitializer extends AbstractPreferenceInitializer {

  public PythonPreferenceInitializer() {
    
  }

  @Override
  public void initializeDefaultPreferences() {
    IPreferenceStore store = PreferenceConstants.getPreferenceStore();

    PreferenceConstants.initializeDefaultValues(store);
  }

}
