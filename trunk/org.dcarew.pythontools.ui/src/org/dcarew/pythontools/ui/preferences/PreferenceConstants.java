package org.dcarew.pythontools.ui.preferences;

import org.dcarew.pythontools.ui.PythonUIPlugin;
import org.eclipse.jface.preference.IPreferenceStore;

public class PreferenceConstants {

  /**
   * A named preference that controls whether bracket matching highlighting is turned on or off.
   * <p>
   * Value is of type <code>Boolean</code>.
   */
  public final static String EDITOR_MATCHING_BRACKETS = "matchingBrackets";

  /**
   * A named preference that holds the color used to highlight matching brackets.
   * <p>
   * Value is of type <code>String</code>. A RGB color value encoded as a string using class
   * <code>PreferenceConverter</code>
   * 
   * @see org.eclipse.jface.resource.StringConverter
   * @see org.eclipse.jface.preference.PreferenceConverter
   */
  public final static String EDITOR_MATCHING_BRACKETS_COLOR = "matchingBracketsColor";

  private PreferenceConstants() {

  }

  public static IPreferenceStore getPreferenceStore() {
    return PythonUIPlugin.getPlugin().getPreferenceStore();
  }

  public static void initializeDefaultValues(IPreferenceStore store) {
    store.setDefault(PreferenceConstants.EDITOR_MATCHING_BRACKETS, true);
    store.setDefault(PreferenceConstants.EDITOR_MATCHING_BRACKETS_COLOR, "192,192,192");

    // See JavaUIPreferenceInitializer.setThemeBasedPreferences().

//    setDefault(
//        store,
//        PreferenceConstants.EDITOR_MATCHING_BRACKETS_COLOR,
//        findRGB(registry, EDITOR_MATCHING_BRACKETS_COLOR, new RGB(192, 192,192)), false);
  }

}
