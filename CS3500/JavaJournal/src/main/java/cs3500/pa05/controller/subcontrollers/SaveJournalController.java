package cs3500.pa05.controller.subcontrollers;

import cs3500.pa05.controller.MasterController;
import cs3500.pa05.controller.handlers.CancelHandler;
import cs3500.pa05.controller.handlers.file.SaveEventHandler;
import cs3500.pa05.model.JournalInteractable;
import java.io.IOException;
import java.util.Objects;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;

/**
 * Represents a save journal controller.
 */
public class SaveJournalController extends AbstractSubController {

  /**
   * The file chooser dialog pane.
   */
  @FXML
  private DialogPane fileChooserDialogPane;


  /**
   * Instantiates a new save journal controller.
   *
   * @param stage   the stage
   * @param journal the journal
   * @param master  the master controller
   */
  public SaveJournalController(Stage stage, JournalInteractable journal, MasterController master) {
    super(stage, journal, master);
  }

  /**
   * Runs the controller.
   */
  @Override
  public void run() {
    this.loadRealPopUp();
  }

  /**
   * Load real pop up.
   */
  private void loadRealPopUp() {

    Scene scene = null;
    try {
      FXMLLoader loader = new FXMLLoader(
          this.getClass().getClassLoader().getResource("FileChooserDialog.fxml"));
      loader.setController(this);
      scene = loader.load();
    } catch (IOException e) {
      System.out.println("loader failed");
    }
    EventHandler<ActionEvent> finish = new SaveEventHandler(this.stage, this.popup,
        this.fileChooserDialogPane,
        this.journal, this.master);
    EventHandler<ActionEvent> cancel = new CancelHandler(this.stage, this.popup,
        this.fileChooserDialogPane,
        this.journal, this.master);
    super.loadPopUp(finish, cancel, Objects.requireNonNull(scene), this.fileChooserDialogPane);
    this.popup.show(this.stage);
  }

}
