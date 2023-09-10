package cs3500.pa05.controller.subcontrollers;

import cs3500.pa05.controller.MasterController;
import cs3500.pa05.controller.handlers.file.RenameWeekEventHandler;
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
 * Represents a set week name controller.
 */
public class SetWeekNameController extends AbstractSubController {

  /**
   * The Set week name dialog pane.
   */
  @FXML
  private DialogPane setWeekNameDialogPane;


  /**
   * Instantiates a new Set week name controller.
   *
   * @param stage   the stage
   * @param journal the journal
   * @param master  the master controller
   */
  public SetWeekNameController(Stage stage, JournalInteractable journal, MasterController master) {
    super(stage, journal, master);
  }

  /**
   * Runs the controller.
   */
  @Override
  public void run() {
    Scene scene = null;
    try {
      FXMLLoader loader = new FXMLLoader(
          this.getClass().getClassLoader().getResource("SetWeekNameDialog.fxml"));
      loader.setController(this);
      scene = loader.load();
    } catch (IOException e) {
      System.out.println("loader failed");
    }
    EventHandler<ActionEvent> finish = new RenameWeekEventHandler(this.stage, this.popup,
        this.setWeekNameDialogPane,
        this.journal,
        this.master);
    EventHandler<ActionEvent> cancel = event -> this.popup.hide();
    super.loadPopUp(finish, cancel, Objects.requireNonNull(scene), this.setWeekNameDialogPane);
    this.popup.show(this.stage);
  }
}