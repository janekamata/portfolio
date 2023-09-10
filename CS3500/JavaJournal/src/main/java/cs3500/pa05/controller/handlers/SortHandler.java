package cs3500.pa05.controller.handlers;


import cs3500.pa05.controller.MasterController;
import cs3500.pa05.model.JournalInteractable;
import cs3500.pa05.model.comparators.CompareByDuration;
import cs3500.pa05.model.comparators.CompareByName;
import cs3500.pa05.model.enums.ComparatorClass;
import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DialogPane;
import javafx.stage.Popup;
import javafx.stage.Stage;

/**
 * Represents a sort handler.
 */
public class SortHandler extends AbstractEventHandler {

  /**
   * Instantiates a new Sort event handler.
   *
   * @param stage   the stage
   * @param popup   the popup
   * @param dialog  the dialog
   * @param journal the journal
   * @param master  the master
   */
  public SortHandler(Stage stage, Popup popup, DialogPane dialog, JournalInteractable journal,
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
    ChoiceBox sortTaskEventChoiceBox = (ChoiceBox) this.dialog.lookup(
        "#sortTaskEventChoiceBox");
    boolean result = true;
    if (sortTaskEventChoiceBox.getValue() == null) {
      super.showErrorPopUp("Please select one sorting flag");
      result = false;
    } else {
      System.out.println(sortTaskEventChoiceBox.getValue());
      switch ((ComparatorClass) sortTaskEventChoiceBox.getValue()) {
        case COMPARE_BY_DURATION -> this.journal.sortBy(new CompareByDuration());
        case COMPARE_BY_NAME -> this.journal.sortBy(new CompareByName());
        default -> System.out.println("Bad compare type");
      }
    }
    return result;
  }
}