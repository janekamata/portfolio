package cs3500.pa05.controller.handlers.file;


import cs3500.pa05.controller.JournalController;
import cs3500.pa05.controller.MasterController;
import cs3500.pa05.controller.handlers.AbstractEventHandler;
import cs3500.pa05.controller.persistence.MyFileReader;
import cs3500.pa05.controller.subcontrollers.PasswordController;
import cs3500.pa05.model.JournalInteractable;
import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;

/**
 * Represents an open event handler.
 */
public class OpenEventHandler extends AbstractEventHandler {

  /**
   * The File chooser.
   */
  private final FileChooser fileChooser = new FileChooser();
  /**
   * The File chooser text field.
   */
  private final TextField fileChooserTextField;

  /**
   * Instantiates a new open event handler.
   *
   * @param stage   the stage
   * @param popup   the popup
   * @param dialog  the dialog pane
   * @param journal the journal
   * @param master  the master
   */
  public OpenEventHandler(Stage stage, Popup popup, DialogPane dialog,
      JournalInteractable journal, MasterController master) {
    super(stage, popup, dialog, journal, master);
    this.fileChooser.setTitle("Select File");
    Path path = FileSystems.getDefault().getPath("");
    this.fileChooser.setInitialDirectory(new File(path.toUri()));
    this.fileChooser.getExtensionFilters()
        .add(new FileChooser.ExtensionFilter(".bujo Files", "*.bujo"));
    Button fileChooserButton = (Button) this.dialog.getContent()
        .lookup("#fileChooserButton");
    this.fileChooserTextField = (TextField) this.dialog.getContent()
        .lookup("#fileChooserTextField");
    fileChooserButton.setOnAction(this::chooseFile);
  }

  /**
   * Choose file handler for the file chooser button.
   *
   * @param event the event
   */
  private void chooseFile(ActionEvent event) {
    File file = this.fileChooser.showOpenDialog(this.popup);
    if (file != null) {
      this.fileChooserTextField.setText(file.getAbsolutePath());
    }
  }

  /**
   * Invoked when a specific event of the type for which this handler is registered happens.
   *
   * @param event the event
   */
  @Override
  public void handle(ActionEvent event) {
    if (this.isValidInput()) {
      this.showPasswordDialog();
    }
  }

  private void showPasswordDialog() {
    Path filePath = Path.of(this.fileChooserTextField.getText());
    JournalInteractable newJournal = MyFileReader.readFromFileToJournal(String.valueOf(filePath));
    newJournal.setPath(String.valueOf(filePath));
    this.popup.hide();
    JournalController passwordController = new PasswordController(newJournal, this.stage,
        this.master);

    passwordController.run();
  }

  /**
   * Verify input.
   *
   * @return the boolean representing if the input is valid
   */
  @Override
  protected boolean isValidInput() {
    Path filePath = Path.of(this.fileChooserTextField.getText());
    return this.isValidPath(filePath);
  }

  /**
   * Determines if the path is a valid path.
   *
   * @param path the path
   * @return the boolean representing if the path is valid
   */
  private boolean isValidPath(Path path) {
    boolean result = true;
    if (!path.toString().endsWith(".bujo")) {
      super.showErrorPopUp("Make sure the path ends with .bujo");
      result = false;
    }
    if (!Files.isReadable(path) || !Files.isRegularFile(
        path)) {
      super.showErrorPopUp("Read path must be a regular, readable file.");
      result = false;
    }
    return result;
  }
}