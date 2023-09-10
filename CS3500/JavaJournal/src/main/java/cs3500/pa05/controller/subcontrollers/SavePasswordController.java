package cs3500.pa05.controller.subcontrollers;

import cs3500.pa05.controller.MasterController;
import cs3500.pa05.controller.handlers.CancelHandler;
import cs3500.pa05.controller.handlers.file.SavePasswordEventHandler;
import cs3500.pa05.model.JournalInteractable;
import java.io.IOException;
import java.nio.file.Path;
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
 * Represents a save password controller.
 */
public class SavePasswordController extends AbstractSubController {

  /**
   * The Path.
   */
  private final Path path;
  /**
   * The password dialog pane.
   */
  @FXML
  private DialogPane passwordDialogPane;

  /**
   * Instantiates a new save password controller.
   *
   * @param journal the journal
   * @param stage   the stage
   * @param master  the master
   * @param path    the path
   */
  public SavePasswordController(JournalInteractable journal, Stage stage, MasterController master,
      Path path) {
    super(stage, journal, master);
    this.path = path;
  }

  /**
   * Runs the controller.
   */
  @Override
  public void run() {
    System.out.println("Password dialog open");
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
    EventHandler<ActionEvent> finish = new SavePasswordEventHandler(this.stage, this.popup,
        this.passwordDialogPane, this.journal, this.master, this.path);
    EventHandler<ActionEvent> cancel = new CancelHandler(this.stage, this.popup,
        this.passwordDialogPane, this.journal, this.master);
    super.loadPopUp(finish, cancel, Objects.requireNonNull(scene), this.passwordDialogPane);
    this.popup.show(this.stage);
  }

  /**
   * Creates password.
   */
  private void createPassword() {
    TextArea passwordPromptTextArea = (TextArea) this.passwordDialogPane.lookup(
        "#passwordPromptTextArea");
    passwordPromptTextArea.setWrapText(true);
    passwordPromptTextArea.setText(
        "Please enter your password below");
  }

}
