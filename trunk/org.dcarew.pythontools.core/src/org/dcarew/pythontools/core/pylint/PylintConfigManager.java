package org.dcarew.pythontools.core.pylint;

import org.dcarew.pythontools.core.PythonCorePlugin;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PylintConfigManager {
  private static final String EXTENSION_POINT_ID = PythonCorePlugin.PLUGIN_ID
      + ".pylintConfigurations";

  private static List<IPylintConfig> resourceConfigs = new ArrayList<IPylintConfig>();

  private static IPylintConfig selectedConfig;

  static {
    initialize();
  }

  private static void initialize() {
    IExtensionRegistry registery = Platform.getExtensionRegistry();
    IExtensionPoint extensionPoint = registery.getExtensionPoint(EXTENSION_POINT_ID);
    IConfigurationElement[] elements = extensionPoint.getConfigurationElements();

    for (IConfigurationElement element : elements) {
      resourceConfigs.add(new ResourcePylintConfig(element));
    }

    Collections.sort(resourceConfigs, new Comparator<IPylintConfig>() {
      @Override
      public int compare(IPylintConfig config1, IPylintConfig config2) {
        return config1.getName().compareToIgnoreCase(config2.getName());
      }
    });

    // TODO: init selectedConfig

  }

  private PylintConfigManager() {

  }

  public static List<IPylintConfig> getAllConfigs() {
    return Collections.unmodifiableList(resourceConfigs);
  }

  public static IPylintConfig getSelectedConfig() {
    return selectedConfig;
  }

  public static void setSelectedConfig(IPylintConfig config) {
    // TODO: write this

    // TODO: send an event

    selectedConfig = config;
  }

}
