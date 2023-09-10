package cs3500.pa05.controller.handlers.file;


import cs3500.pa05.controller.MasterController;
import cs3500.pa05.controller.handlers.AbstractEventHandler;
import cs3500.pa05.controller.persistence.MyFileReader;
import cs3500.pa05.model.JournalInteractable;
import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;

/**
 * Represents an open template handler.
 */
public class OpenTemplateHandler extends AbstractEventHandler {

  /**
   * The File chooser.
   */
  private final FileChooser fileChooser = new FileChooser();
  /**
   * The File chooser text field.
   */
  private final TextField fileChooserTextField;

  /**
   * Path of the new journal
   */
  private String finalPath;


  /**
   * Instantiates a new open template handler.
   *
   * @param stage   the stage
   * @param popup   the popup
   * @param dialog  the dialog pane
   * @param journal the journal
   * @param master  the master
   */
  public OpenTemplateHandler(Stage stage, Popup popup, DialogPane dialog,
      JournalInteractable journal, MasterController master) {
    super(stage, popup, dialog, journal, master);
    this.fileChooser.setTitle("Select File");
    Path path = FileSystems.getDefault().getPath(".");
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
    TextArea pathArea = (TextArea) this.dialog.lookup("#filePathTextArea");
    TextField newNameTextField = (TextField) this.dialog.lookup("#newJournalNameTextField");
    Path path = FileSystems.getDefault().getPath("");
    this.finalPath = path.toAbsolutePath() + File.separator + newNameTextField.getText() + ".bujo";
    pathArea.setText("The new week will be saved at : " + this.finalPath);
  }

  /**
   * Invoked when a specific event of the type for which this handler is registered happens.
   *
   * @param event the event
   */
  @Override
  public void handle(ActionEvent event) {
    if (this.isValidInput()) {
      this.finishHandle();
    }
  }

  /**
   * Opens a new journal from the path as a template, clearing out the events and tasks Keeps the
   * other preferences Adds new week name
   *
   * @param templatePath path to the template
   */
  private void openNewJournal(Path templatePath) {
    JournalInteractable newJournal = MyFileReader.readFromFileToJournal(
        String.valueOf(templatePath));
    newJournal.clearEvents();
    newJournal.clearTasks();
    TextField newNameTextField = (TextField) this.dialog.lookup("#newJournalNameTextField");
    newJournal.setWeekName(newNameTextField.getText());
    newJournal.setPath(this.finalPath);
    this.master.updateJournal(newJournal);
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
    if (!Files.isReadable(path) || !Files.isRegularFile(
        path)) {
      super.showErrorPopUp("Read path must be a regular, readable file.");
      result = false;
    }
    this.openNewJournal(path);
    return result;
  }
}