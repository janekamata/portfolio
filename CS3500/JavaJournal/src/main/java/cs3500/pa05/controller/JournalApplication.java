package cs3500.pa05.controller;

import cs3500.pa05.controller.subcontrollers.StartScreenController;
import cs3500.pa05.model.Calendar;
import cs3500.pa05.model.JournalImpl;
import cs3500.pa05.model.JournalInteractable;
import cs3500.pa05.model.Note;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Represents a Journal application.
 */
public class JournalApplication extends Application {

  /**
   * Main entry point for the JavaFx application.
   *
   * @param primaryStage the stage
   */
  @Override
  public void start(Stage primaryStage) {
    System.out.println("start");
    Calendar calendar = new Calendar();
    Note note = new Note();
    JournalInteractable journal = new JournalImpl(calendar, note, " ");
    JournalController controller = new StartScreenController(journal, primaryStage);

    try {
      // load and place the view's scene onto the stage and set the title
      primaryStage.setTitle("Java Journal");
      primaryStage.setAlwaysOnTop(true);
      primaryStage.show();

      // run the controller
      controller.run();
      // render the stage
    } catch (IllegalStateException e) {
      System.out.println("Unable to load GUI");
    }
  }
}

