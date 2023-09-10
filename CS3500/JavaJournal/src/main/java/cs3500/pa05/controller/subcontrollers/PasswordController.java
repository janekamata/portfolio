package cs3500.pa05.controller.subcontrollers;

import cs3500.pa05.controller.MasterController;
import cs3500.pa05.controller.handlers.CancelHandler;
import cs3500.pa05.controller.handlers.file.PasswordEventHandler;
import cs3500.pa05.model.JournalInteractable;
import java.io.IOException;
import java.util.Objects;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * Represents a password controller.
 */
public class PasswordController extends AbstractSubController {

  /**
   * The password dialog pane.
   */
  @FXML
  private DialogPane passwordDialogPane;

  /**
   * Instantiates a new password controller.
   *
   * @param journal the journal
   * @param stage   the stage
   * @param master  the master
   */
  public PasswordController(JournalInteractable journal, Stage stage, MasterController master) {
    super(stage, journal, master);
  }

  /**
   * Runs the program.
   */
  @Override
  public void run() {
    Scene scene = null;
    try {
      FXMLLoader loader = new FXMLLoader(
          this.getClass().getClassLoader().getResource("PasswordDialog.fxml"));
      loader.setController(this);
      scene = loader.load();
    } catch (IOException e) {
      System.out.println("loader failed");
    }
    this.createPassword();
    EventHandler<ActionEvent> finish = new PasswordEventHandler(this.stage, this.popup,
        this.passwordDialogPane, this.journal, this.master);
    EventHandler<ActionEvent> cancel = new CancelHandler(this.stage, this.popup,
        this.passwordDialogPane, this.journal, this.master);
    super.loadPopUp(finish, cancel, Objects.requireNonNull(scene), this.passwordDialogPane);
    this.popup.show(this.stage);
  }

  /**
   * Creates the password.
   */
  private void createPassword() {
    TextArea passwordPromptTextArea = (TextArea) this.passwordDialogPane.lookup(
        "#passwordPromptTextArea");
    passwordPromptTextArea.setWrapText(true);
    passwordPromptTextArea.setText(
        "Dr. JSON Derulo is society's smart test, with a mind like a steel trap. "
            + " Hopefully, you picked a password you can actually remember :))."
            + System.lineSeparator()
            + System.lineSeparator()
            + " The default password for .bujo files is: hello");
  }

}
