package cs3500.pa05.controller.handlers.task;

import cs3500.pa05.controller.MasterController;
import cs3500.pa05.controller.handlers.AbstractEventHandler;
import cs3500.pa05.model.JournalInteractable;
import cs3500.pa05.model.enums.CompletionStatus;
import cs3500.pa05.model.enums.DayType;
import java.io.IOException;
import java.util.Objects;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Popup;
import javafx.stage.Stage;

/**
 * Represents a finish task handler.
 */
public class FinishTaskHandler extends AbstractEventHandler {

  /**
   * Instantiates a new Finish task handler.
   *
   * @param stage   the stage
   * @param popup   the popup
   * @param dialog  the dialog
   * @param journal the journal
   * @param master  the master
   */
  public FinishTaskHandler(Stage stage, Popup popup, DialogPane dialog, JournalInteractable journal,
      MasterController master) {
    super(stage, popup, dialog, journal, master);
  }

  /**
   * Invoked when a specific event of the type for which this handler is registered happens.
   *
   * @param event the event
   */
  @Override
  public void handle(ActionEvent event) {
    FXMLLoader loader = new FXMLLoader(
        this.getClass().getClassLoader().getResource("TaskCreationDialog.fxml"));
    try {
      loader.load();
    } catch (IOException e) {
      System.out.println("loader failed");
    }
    if (this.isValidInput()) {
      super.finishHandle();
    }
  }


  /**
   * Verify input.
   *
   * @return the boolean representing if the input is valid
   */
  @Override
  protected boolean isValidInput() {
    TextField taskCreationNameTextField = (TextField) this.dialog.getContent()
        .lookup("#taskCreationNameTextField");
    TextArea taskCreationDescriptionTextArea = (TextArea) this.dialog.getContent()
        .lookup("#taskCreationDescriptionTextArea");
    ChoiceBox taskCreationDayChoiceBox = (ChoiceBox) this.dialog.getContent()
        .lookup("#taskCreationDayChoiceBox");
    ChoiceBox taskCreationStatusChoiceBox = (ChoiceBox) this.dialog.getContent()
        .lookup("#taskCreationStatusChoiceBox");
    boolean result = true;
    if (Objects.equals(taskCreationNameTextField.getText(), "")
        || taskCreationDayChoiceBox.getValue() == null
        || taskCreationStatusChoiceBox.getValue() == null) {
      super.showErrorPopUp(
          "Please fill out the following required fields: name(String), "
              + "day of week, task completion status");
      result = false;
    }
    if (result) {
      this.journal.addTask(taskCreationNameTextField.getText(),
          taskCreationDescriptionTextArea.getText(),
          (DayType) taskCreationDayChoiceBox.getValue(),
          (CompletionStatus) taskCreationStatusChoiceBox.getValue());
    }
    return true;
  }
}