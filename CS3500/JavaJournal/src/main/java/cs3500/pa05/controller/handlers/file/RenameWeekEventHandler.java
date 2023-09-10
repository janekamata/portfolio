package cs3500.pa05.controller.handlers.file;

import cs3500.pa05.controller.MasterController;
import cs3500.pa05.controller.handlers.AbstractEventHandler;
import cs3500.pa05.model.JournalInteractable;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.stage.Popup;
import javafx.stage.Stage;

/**
 * Represents a rename week event handler.
 */
public class RenameWeekEventHandler extends AbstractEventHandler {

  /**
   * Instantiates a new Rename week event handler.
   *
   * @param stage   the stage
   * @param popup   the popup
   * @param dialog  the dialog
   * @param journal the journal
   * @param master  the master
   */
  public RenameWeekEventHandler(Stage stage, Popup popup, DialogPane dialog,
      JournalInteractable journal, MasterController master) {
    super(stage, popup, dialog, journal, master);
  }

  /**
   * Verify input.
   *
   * @return the boolean representing if the input is valid
   */
  @Override
  protected boolean isValidInput() {
    return true;
  }

  /**
   * Invoked when a specific event of the type for which this handler is registered happens.
   *
   * @param event the event which occurred
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
    TextField setWeekNameTextField = (TextField) this.dialog.getContent()
        .lookup("#setWeekNameTextField");
    this.journal.setWeekName(setWeekNameTextField.getText());
    this.master.updateView();
    this.popup.hide();
  }
}
