package cs3500.pa05.controller.subcontrollers;

import cs3500.pa05.controller.JournalController;
import cs3500.pa05.controller.MasterController;
import cs3500.pa05.controller.handlers.CancelHandler;
import cs3500.pa05.controller.handlers.task.FinishTaskHandler;
import cs3500.pa05.model.JournalInteractable;
import cs3500.pa05.model.enums.CompletionStatus;
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
 * Represents an Add task controller.
 */
public class AddTaskController extends AbstractSubController {

  /**
   * The Task creation dialog pane.
   */
  @FXML
  private DialogPane taskCreationDialogPane;


  /**
   * Instantiates a new Add task controller.
   *
   * @param stage   the stage
   * @param journal the journal
   * @param master  the master controller
   */
  public AddTaskController(Stage stage, JournalInteractable journal, MasterController master) {
    super(stage, journal, master);
  }

  /**
   * Runs the controller.
   */
  @Override
  public void run() {
    if (this.journal.hasMaxTasks()) {
      JournalController badInput = new BadInputController(this.stage,
          "You have reached the max task limit for the week");
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
          this.getClass().getClassLoader().getResource("TaskCreationDialog.fxml"));
      loader.setController(this);
      scene = loader.load();
    } catch (IOException e) {
      System.out.println("loader failed");
    }
    EventHandler<ActionEvent> finish = new FinishTaskHandler(this.stage, this.popup,
        this.taskCreationDialogPane,
        this.journal, this.master);
    EventHandler<ActionEvent> cancel = new CancelHandler(this.stage, this.popup,
        this.taskCreationDialogPane,
        this.journal, this.master);
    super.loadPopUp(finish, cancel, Objects.requireNonNull(scene), this.taskCreationDialogPane);
    this.addMenuOptions();
    this.popup.show(this.stage);
  }

  /**
   * Add menu options.
   */
  private void addMenuOptions() {
    ChoiceBox taskCreationDayChoiceBox = (ChoiceBox) this.taskCreationDialogPane.lookup(
        "#taskCreationDayChoiceBox");
    taskCreationDayChoiceBox.getItems().addAll(DayType.MONDAY, DayType.TUESDAY, DayType.WEDNESDAY,
        DayType.THURSDAY, DayType.FRIDAY, DayType.SATURDAY, DayType.SUNDAY);
    taskCreationDayChoiceBox.setValue(this.journal.getStart());
    ChoiceBox taskCreationStatusChoiceBox = (ChoiceBox) this.taskCreationDialogPane.lookup(
        "#taskCreationStatusChoiceBox");
    taskCreationStatusChoiceBox.getItems()
        .addAll(CompletionStatus.INCOMPLETE, CompletionStatus.COMPLETE);
    taskCreationStatusChoiceBox.setValue(CompletionStatus.INCOMPLETE);
  }
}
