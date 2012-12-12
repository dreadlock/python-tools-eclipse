package org.dcarew.pythontools.core.pylint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.dcarew.pythontools.core.PythonCorePlugin;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

public class PylintConfigManager {
  private static final String DEFAULT_STYLE = "PEP 8 Style";

  private static final String EXTENSION_POINT_ID = PythonCorePlugin.PLUGIN_ID
      + ".pylintConfigurations";

  private static List<IPylintConfig> resourceConfigs = new ArrayList<IPylintConfig>();

  static {
    initialize();
  }

  public static List<IPylintConfig> getAllConfigs() {
    return Collections.unmodifiableList(resourceConfigs);
  }

  public static IPylintConfig getConfig(String name) {
    for (IPylintConfig config : getAllConfigs()) {
      if (config.getName().equals(name)) {
        return config;
      }
    }

    return null;
  }

  public static IPylintConfig getDefaultConfig() {
    for (IPylintConfig config : resourceConfigs) {
      if (config.getName().equals(DEFAULT_STYLE)) {
        return config;
      }
    }

    return null;
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
  }

  private PylintConfigManager() {

  }

}
