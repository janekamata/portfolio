package cs3500.pa05.controller.handlers.file;


import cs3500.pa05.controller.JournalController;
import cs3500.pa05.controller.MasterController;
import cs3500.pa05.controller.handlers.AbstractEventHandler;
import cs3500.pa05.controller.subcontrollers.SavePasswordController;
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
 * Represents a save event handler.
 */
public class SaveEventHandler extends AbstractEventHandler {

  /**
   * The File chooser.
   */
  private final FileChooser fileChooser = new FileChooser();
  /**
   * The File chooser text field.
   */
  private final TextField fileChooserTextField;

  /**
   * The Path.
   */
  private Path path;

  /**
   * Instantiates a new save event handler.
   *
   * @param stage   the stage
   * @param popup   the popup
   * @param dialog  the dialog pane
   * @param journal the journal
   * @param master  the master
   */
  public SaveEventHandler(Stage stage, Popup popup, DialogPane dialog,
      JournalInteractable journal, MasterController master) {
    super(stage, popup, dialog, journal, master);
    this.fileChooser.setTitle("Select File");
    Path pathDirectory = FileSystems.getDefault().getPath("");
    this.fileChooser.setInitialDirectory(new File(pathDirectory.toUri()));
    this.fileChooser.getExtensionFilters()
        .add(new FileChooser.ExtensionFilter(".bujo Files", "*.bujo"));
    Button fileChooserButton = (Button) this.dialog.getContent()
        .lookup("#fileChooserButton");
    this.fileChooserTextField = (TextField) this.dialog.getContent()
        .lookup("#fileChooserTextField");
    fileChooserButton.setOnAction(this::chooseFile);
  }

  /**
   * Choose file handler for the file chooser.
   *
   * @param event the event
   */
  private void chooseFile(ActionEvent event) {
    File file = this.fileChooser.showSaveDialog(this.popup);
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
      this.popup.hide();
      this.showSavePassword();
    }
  }

  /**
   * Show save password dialog.
   */
  private void showSavePassword() {
    JournalController savePassword = new SavePasswordController(this.journal, this.stage,
        this.master, this.path);
    savePassword.run();
  }


  /**
   * Verify input.
   *
   * @return the boolean representing if the input is valid
   */
  @Override
  protected boolean isValidInput() {
    Path filePath = Path.of(this.fileChooserTextField.getText());
    boolean result = this.isValidPath(filePath);
    this.path = filePath;
    return result;
  }

  /**
   * Determines if the given path is a valid path.
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
    if (Files.exists(path) && (!Files.isWritable(path) || !Files.isRegularFile(
        path))) {
      super.showErrorPopUp("Write path must be a regular, writable file.");
      result = false;
    }
    return result;
  }
}