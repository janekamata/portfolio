package cs3500.pa05.controller.handlers;

import cs3500.pa05.controller.JournalController;
import cs3500.pa05.controller.MasterController;
import cs3500.pa05.controller.subcontrollers.BadInputController;
import cs3500.pa05.model.JournalInteractable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.DialogPane;
import javafx.stage.Popup;
import javafx.stage.Stage;

/**
 * Represents an abstract event handler.
 */
public abstract class AbstractEventHandler implements EventHandler<ActionEvent> {

  /**
   * The Popup.
   */
  protected final Popup popup;
  /**
   * The Stage.
   */
  protected final Stage stage;
  /**
   * The Dialog.
   */
  protected final DialogPane dialog;
  /**
   * The Master.
   */
  protected final MasterController master;
  /**
   * The Journal.
   */
  protected final JournalInteractable journal;

  /**
   * Instantiates a new event handler.
   *
   * @param stage   the stage
   * @param popup   the popup
   * @param dialog  the dialog
   * @param journal the journal
   * @param master  the master
   */
  protected AbstractEventHandler(Stage stage, Popup popup, DialogPane dialog,
      JournalInteractable journal, MasterController master) {
    this.popup = popup;
    this.stage = stage;
    this.journal = journal;
    this.dialog = dialog;
    this.master = master;
  }

  /**
   * Invoked at the end of a handle to update the view and hide.
   */
  protected void finishHandle() {
    this.master.updateView();
    this.popup.hide();
  }

  /**
   * Verify input.
   *
   * @return the boolean representing if the input is valid
   */
  protected abstract boolean isValidInput();

  /**
   * Show error pop up.
   *
   * @param errorStatement the error statement
   */
  protected void showErrorPopUp(String errorStatement) {
    JournalController badInput = new BadInputController(this.stage, errorStatement);
    badInput.run();
  }

}
