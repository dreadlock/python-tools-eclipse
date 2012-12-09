package org.dcarew.pythontools.ui;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.texteditor.ChainedPreferenceStore;
import org.osgi.framework.BundleContext;

import java.util.HashMap;
import java.util.Map;

/**
 * The activator class controls the plug-in life cycle
 */
public class PythonUIPlugin extends AbstractUIPlugin {

  // The plug-in ID
  public static final String PLUGIN_ID = "org.dcarew.pythontools.ui";

  public static final String EDITOR_ID = "org.dcarew.pythontools.ui.editors.python";

  public static final String PREF_PAGE_ID = "org.dcarew.pythontools.ui.preferences.mainPage";

  // The shared instance
  private static PythonUIPlugin plugin;

  private static Map<ImageDescriptor, Image> imageCache = new HashMap<ImageDescriptor, Image>();

  private IPreferenceStore combinedPreferenceStore;

  /**
   * The constructor
   */
  public PythonUIPlugin() {

  }

  @Override
  public void start(BundleContext context) throws Exception {
    super.start(context);

    plugin = this;
  }

  @Override
  public void stop(BundleContext context) throws Exception {
    plugin = null;

    super.stop(context);
  }

  /**
   * Returns a combined preference store, this store is read-only.
   * 
   * @return the combined preference store
   * @since 3.1
   */
  public IPreferenceStore getCombinedPreferenceStore() {
    if (combinedPreferenceStore == null) {
      IPreferenceStore generalTextStore = EditorsUI.getPreferenceStore();

      combinedPreferenceStore = new ChainedPreferenceStore(new IPreferenceStore[] {
          getPreferenceStore(), generalTextStore});
    }

    return combinedPreferenceStore;
  }

  /**
   * Returns the shared instance
   * 
   * @return the shared instance
   */
  public static PythonUIPlugin getPlugin() {
    return plugin;
  }

  public static void logError(Throwable exception) {
    if (getPlugin() != null) {
      getPlugin().getLog().log(
          new Status(IStatus.ERROR, PLUGIN_ID, exception.getMessage(), exception));
    }
  }

  /**
   * Returns an image descriptor for the image file at the given plug-in relative path
   * 
   * @param path the path
   * @return the image descriptor
   */
  public static ImageDescriptor getImageDescriptor(String path) {
    return imageDescriptorFromPlugin(PLUGIN_ID, path);
  }

  /**
   * Get an image given a path relative to this plugin.
   * 
   * @param path
   * @return an image
   */
  public static Image getImage(String path) {
    try {
      Image image = getPlugin().getImageRegistry().get(path);

      if (image != null) {
        return image;
      }

      ImageDescriptor descriptor = getImageDescriptor(path);

      if (descriptor != null) {
        getPlugin().getImageRegistry().put(path, descriptor);

        return getPlugin().getImageRegistry().get(path);
      }
    } catch (Exception ex) {
      PythonUIPlugin.logError(ex);
    }

    return null;
  }

  /**
   * Create or return the cached image for the given image descriptor.
   * 
   * @param imageDescriptor
   * @return the image for the given image descriptor
   */
  public static Image getImage(ImageDescriptor imageDescriptor) {
    Image image = imageCache.get(imageDescriptor);

    if (image == null) {
      image = imageDescriptor.createImage();

      imageCache.put(imageDescriptor, image);
    }

    return image;
  }

}
