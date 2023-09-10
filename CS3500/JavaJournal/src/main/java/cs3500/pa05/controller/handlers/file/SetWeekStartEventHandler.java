package cs3500.pa05.controller.handlers.file;

import cs3500.pa05.controller.MasterController;
import cs3500.pa05.controller.handlers.AbstractEventHandler;
import cs3500.pa05.model.JournalInteractable;
import cs3500.pa05.model.enums.DayType;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DialogPane;
import javafx.stage.Popup;
import javafx.stage.Stage;

/**
 * Represents a set week start event handler.
 */
public class SetWeekStartEventHandler extends AbstractEventHandler {

  /**
   * Instantiates a new Set week start event handler.
   *
   * @param stage   the stage
   * @param popup   the popup
   * @param dialog  the dialog
   * @param journal the journal
   * @param master  the master
   */
  public SetWeekStartEventHandler(Stage stage, Popup popup, DialogPane dialog,
      JournalInteractable journal, MasterController master) {
    super(stage, popup, dialog, journal, master);
  }

  /**
   * Invoked when a specific event of the type for which this handler is registered happens.
   *
   * @param event the event which occurred
   */
  @Override
  public void handle(ActionEvent event) {
    FXMLLoader loader = new FXMLLoader(
        this.getClass().getClassLoader().getResource("SetWeekStartDialog.fxml"));
    try {
      loader.load();
    } catch (IOException e) {
      System.out.println("loader failed");
    }
    ChoiceBox setStartChoiceBox = (ChoiceBox) this.dialog.getContent()
        .lookup("#setStartChoiceBox");
    this.journal.setStart((DayType) setStartChoiceBox.getValue());
    this.master.updateView();
    this.popup.hide();
  }

  /**
   * Verify input.
   *
   * @return the boolean representing if the input is valid
   */
  @Override
  protected boolean isValidInput() {
    return false;
  }
}
