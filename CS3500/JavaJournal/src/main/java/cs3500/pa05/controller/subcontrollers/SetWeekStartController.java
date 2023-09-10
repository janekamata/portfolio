package cs3500.pa05.controller.subcontrollers;

import cs3500.pa05.controller.MasterController;
import cs3500.pa05.controller.handlers.file.SetWeekStartEventHandler;
import cs3500.pa05.model.JournalInteractable;
import cs3500.pa05.model.enums.DayType;
import java.io.IOException;
import java.util.Objects;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;

/**
 * Represents a Set week start controller.
 */
public class SetWeekStartController extends AbstractSubController {

  /**
   * The Set start dialog pane.
   */
  @FXML
  private DialogPane setStartDialogPane;


  /**
   * Instantiates a new Set week start controller.
   *
   * @param stage   the stage
   * @param journal the journal
   * @param master  the master
   */
  public SetWeekStartController(Stage stage, JournalInteractable journal, MasterController master) {
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
          this.getClass().getClassLoader().getResource("SetWeekStartDialog.fxml"));
      loader.setController(this);
      scene = loader.load();
    } catch (IOException e) {
      System.out.println("loader failed");
    }
    EventHandler<ActionEvent> finish = new SetWeekStartEventHandler(this.stage, this.popup,
        this.setStartDialogPane,
        this.journal,
        this.master);
    EventHandler<ActionEvent> cancel = event -> this.popup.hide();
    super.loadPopUp(finish, cancel, Objects.requireNonNull(scene), this.setStartDialogPane);
    this.addMenuOptions();
    this.popup.show(this.stage);
  }

  /**
   * Add menu options.
   */
  private void addMenuOptions() {
    ChoiceBox setStartChoiceBox = (ChoiceBox) this.setStartDialogPane.lookup(
        "#setStartChoiceBox");
    setStartChoiceBox.getItems().addAll(DayType.MONDAY, DayType.TUESDAY, DayType.WEDNESDAY,
        DayType.THURSDAY, DayType.FRIDAY, DayType.SATURDAY, DayType.SUNDAY);
  }
}