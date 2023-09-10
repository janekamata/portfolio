package cs3500.pa05.controller.handlers.file;


import cs3500.pa05.controller.MasterController;
import cs3500.pa05.controller.handlers.AbstractEventHandler;
import cs3500.pa05.controller.persistence.MyFileWriter;
import cs3500.pa05.model.JournalInteractable;
import java.nio.file.Path;
import java.util.Objects;
import javafx.event.ActionEvent;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.stage.Popup;
import javafx.stage.Stage;

/**
 * Represents a save password event handler.
 */
public class SavePasswordEventHandler extends AbstractEventHandler {

  /**
   * The Path.
   */
  private final Path path;

  /**
   * Instantiates a new save password handler.
   *
   * @param stage   the stage
   * @param popup   the popup
   * @param dialog  the dialog pane
   * @param journal the journal
   * @param master  the master
   * @param path    the path
   */
  public SavePasswordEventHandler(Stage stage, Popup popup, DialogPane dialog,
      JournalInteractable journal, MasterController master, Path path) {
    super(stage, popup, dialog, journal, master);
    this.path = path;
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
      super.finishHandle();
    }
  }

  /**
   * Show journal.
   */
  private void showJournal() {
    this.journal.setPath(String.valueOf(this.path));
    MyFileWriter.writeFromJournalToFile(this.journal, String.valueOf(this.path));
    System.out.println("writing");
  }


  /**
   * Verify input.
   *
   * @return the boolean representing if the input is valid
   */
  @Override
  protected boolean isValidInput() {
    boolean result = false;
    TextField setLimitTextField = (TextField) this.dialog.getContent()
        .lookup("#passwordAnswerTextField");
    String password = setLimitTextField.getText();
    if (Objects.equals(password, "")) {
      super.showErrorPopUp("Please enter a password");
    } else {
      this.journal.setPassword(password);
      result = true;
    }
    return result;
  }
}