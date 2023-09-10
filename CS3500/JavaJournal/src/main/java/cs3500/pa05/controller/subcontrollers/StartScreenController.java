package cs3500.pa05.controller.subcontrollers;

import cs3500.pa05.controller.JournalController;
import cs3500.pa05.controller.MasterController;
import cs3500.pa05.controller.MasterControllerImpl;
import cs3500.pa05.model.JournalInteractable;
import cs3500.pa05.view.JournalViewImpl;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Represents a start screen controller.
 */
public class StartScreenController implements JournalController {

  /**
   * The Journal.
   */
  private final JournalInteractable journal;
  /**
   * The Stage.
   */
  private final Stage stage;
  /**
   * The Bad input dialog pane.
   */
  @FXML
  private StackPane startScreenStackPane;


  /**
   * Instantiates a new start screen controller.
   *
   * @param journal the journal
   * @param stage   the stage
   */
  public StartScreenController(JournalInteractable journal, Stage stage) {
    this.stage = stage;
    this.journal = journal;
  }

  /**
   * Runs the controller.
   */
  @Override
  public void run() {
    System.out.println("Start screen dialog open");
    Scene scene = null;
    try {
      FXMLLoader loader = new FXMLLoader(
          this.getClass().getClassLoader().getResource("StartScreenDialog.fxml"));
      loader.setController(this);
      scene = loader.load();
    } catch (IOException e) {
      System.out.println("loader failed");
    }
    this.stage.setScene(scene);
    Button finishButton = (Button) this.startScreenStackPane.lookup("#startScreenButton");
    finishButton.setOnAction(event -> this.showJournal());
  }

  /**
   * Show journal.
   */
  private void showJournal() {
    MasterController master = new MasterControllerImpl(this.journal, this.stage);
    JournalViewImpl view = new JournalViewImpl(master);
    Scene scene = view.load();
    scene.getStylesheets().add("JavaJournalStyle.css");
    this.stage.setScene(scene);
    master.run();
  }
}
