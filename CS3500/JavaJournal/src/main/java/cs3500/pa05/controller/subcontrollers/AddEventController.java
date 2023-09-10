package cs3500.pa05.controller.subcontrollers;

import cs3500.pa05.controller.JournalController;
import cs3500.pa05.controller.MasterController;
import cs3500.pa05.controller.handlers.CancelHandler;
import cs3500.pa05.controller.handlers.event.FinishEventHandler;
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
 * Represents an Add event controller.
 */
public class AddEventController extends AbstractSubController {

  /**
   * The Event creation dialog pane.
   */
  @FXML
  private DialogPane eventCreationDialogPane;

  /**
   * Instantiates a new Add event controller.
   *
   * @param stage   the stage
   * @param journal the journal
   * @param master  the master
   */
  public AddEventController(Stage stage, JournalInteractable journal, MasterController master) {
    super(stage, journal, master);
  }

  /**
   * Runs the controller.
   */
  @Override
  public void run() {
    if (this.journal.hasMaxEvents()) {
      JournalController badInput = new BadInputController(this.stage,
          "You have reached the max amount of events possible");
      badInput.run();
    } else {
      this.loadRealPopUp();
    }


  }

  /**
   * Loads the real pop up.
   */
  private void loadRealPopUp() {
    Scene scene = null;
    try {
      FXMLLoader loader = new FXMLLoader(
          this.getClass().getClassLoader().getResource("EventCreationDialog.fxml"));
      loader.setController(this);
      scene = loader.load();
    } catch (IOException e) {
      System.out.println("loader failed");
    }
    EventHandler<ActionEvent> finish = new FinishEventHandler(this.stage, this.popup,
        this.eventCreationDialogPane,
        this.journal, this.master);
    EventHandler<ActionEvent> cancel = new CancelHandler(this.stage, this.popup,
        this.eventCreationDialogPane,
        this.journal, this.master);
    super.loadPopUp(finish, cancel, Objects.requireNonNull(scene), this.eventCreationDialogPane);
    this.addMenuOptions();
    this.popup.show(this.stage);
  }

  /**
   * Add menu options.
   */
  private void addMenuOptions() {
    ChoiceBox eventCreationDayChoiceBox = (ChoiceBox) this.eventCreationDialogPane.lookup(
        "#eventCreationDayChoiceBox");
    eventCreationDayChoiceBox.getItems()
        .addAll(DayType.MONDAY, DayType.TUESDAY, DayType.WEDNESDAY,
            DayType.THURSDAY, DayType.FRIDAY, DayType.SATURDAY, DayType.SUNDAY);
    eventCreationDayChoiceBox.setValue(this.journal.getStart());
  }

}
