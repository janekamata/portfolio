package cs3500.pa05.view;

import cs3500.pa05.controller.JournalController;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

/**
 * Represents a journal view.
 */
public class JournalViewImpl implements JournalView {

  /**
   * The Loader.
   */
  private final FXMLLoader loader;

  /**
   * Instantiates a new gui view.
   *
   * @param controller the controller
   */
  public JournalViewImpl(JournalController controller) {
    // look up and store the layout
    this.loader = new FXMLLoader();
    this.loader.setLocation(this.getClass().getClassLoader().getResource("JavaJournal.fxml"));
    this.loader.setController(controller);
  }

  /**
   * Loads a scene from a GUI layout.
   *
   * @return the layout
   */
  @Override
  public Scene load() {
    // load the layout
    try {
      return this.loader.load();
    } catch (IOException exc) {
      throw new IllegalStateException("Unable to load layout: " + exc.getMessage());
    }
  }
}
