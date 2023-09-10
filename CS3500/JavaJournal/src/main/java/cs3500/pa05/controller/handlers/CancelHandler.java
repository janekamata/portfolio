package cs3500.pa05.controller.handlers;


import cs3500.pa05.controller.MasterController;
import cs3500.pa05.model.JournalInteractable;
import javafx.event.ActionEvent;
import javafx.scene.control.DialogPane;
import javafx.stage.Popup;
import javafx.stage.Stage;

/**
 * Represents a cancel handler.
 */
public class CancelHandler extends AbstractEventHandler {

  /**
   * Instantiates a new Cancel event handler.
   *
   * @param stage   the stage
   * @param popup   the popup
   * @param dialog  the dialog
   * @param journal the journal
   * @param master  the master
   */
  public CancelHandler(Stage stage, Popup popup, DialogPane dialog,
      JournalInteractable journal, MasterController master) {
    super(stage, popup, dialog, journal, master);
  }

  /**
   * No Input To Verify
   *
   * @return the boolean
   */
  @Override
  protected boolean isValidInput() {
    return true;
  }

  /**
   * Invoked when a specific event of the type for which this handler is registered happens.
   *
   * @param event the event
   */
  @Override
  public void handle(ActionEvent event) {
    if (this.isValidInput()) {
      this.popup.hide();
    }
  }


}
