package cs3500.pa05.controller.handlers.task;


import cs3500.pa05.controller.MasterController;
import cs3500.pa05.controller.handlers.AbstractEventHandler;
import cs3500.pa05.model.JournalInteractable;
import cs3500.pa05.model.enums.CalendarFeature;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.stage.Popup;
import javafx.stage.Stage;

/**
 * Represents a change task limit event handler.
 */
public class ChangeTaskLimitEventHandler extends AbstractEventHandler {


  /**
   * Instantiates a new Change task limit event handler.
   *
   * @param stage   the stage
   * @param popup   the popup
   * @param dialog  the set limit dialog pane
   * @param journal the journal
   * @param master  the master
   */
  public ChangeTaskLimitEventHandler(Stage stage, Popup popup, DialogPane dialog,
      JournalInteractable journal, MasterController master) {
    super(stage, popup, dialog, journal, master);
  }

  /**
   * Invoked when a specific event of the type for which this handler is registered happens.
   *
   * @param event the event
   */
  @Override
  public void handle(ActionEvent event) {
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
    boolean result = true;
    try {
      TextField setLimitTextField = (TextField) this.dialog.getContent()
          .lookup("#setLimitTextField");
      int newLimit = Integer.parseInt(setLimitTextField.getText());
      if (this.journal.getTasks(CalendarFeature.TASK).values().stream().mapToInt(List::size).sum()
          > newLimit) {
        super.showErrorPopUp("You already have more than " + newLimit + " tasks in your journal");
        result = false;
      } else {
        this.journal.setTaskLimit(newLimit);
      }
    } catch (NumberFormatException e) {
      super.showErrorPopUp("Please make sure to input an integer");
    }
    return result;
  }
}