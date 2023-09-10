package cs3500.pa05.controller.handlers.file;


import cs3500.pa05.controller.MasterController;
import cs3500.pa05.controller.handlers.AbstractEventHandler;
import cs3500.pa05.model.JournalInteractable;
import javafx.event.ActionEvent;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.stage.Popup;
import javafx.stage.Stage;

/**
 * Represents a password event handler.
 */
public class PasswordEventHandler extends AbstractEventHandler {


  /**
   * Initiates the password event handler
   *
   * @param stage   the stage
   * @param popup   the popup
   * @param dialog  the dialog
   * @param journal the journal
   * @param master  the master
   */
  public PasswordEventHandler(Stage stage, Popup popup, DialogPane dialog,
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
      this.showJournal();
    }
  }

  /**
   * Show journal.
   */
  private void showJournal() {
    this.master.updateJournal(this.journal);
    super.finishHandle();
  }


  /**
   * Verify input.
   *
   * @return the boolean representing if the input is valid
   */
  @Override
  protected boolean isValidInput() {
    boolean result = false;
    try {
      TextField setLimitTextField = (TextField) this.dialog.getContent()
          .lookup("#passwordAnswerTextField");
      setLimitTextField.setOnAction(event -> System.out.println("here"));
      String passwordAnswer = setLimitTextField.getText();
      if (this.journal.getPassword().equals(passwordAnswer)) {
        result = true;
      } else {
        super.showErrorPopUp("Wrong password");
      }
    } catch (RuntimeException e) {
      super.showErrorPopUp("Make sure to put in a password");
    }
    return result;
  }
}