package cs3500.pa05.controller.subcontrollers;

import cs3500.pa05.controller.JournalController;
import java.io.IOException;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.stage.Popup;
import javafx.stage.Stage;

/**
 * Represents a Bad input controller.
 */
public class BadInputController implements JournalController {

  /**
   * The Error statement.
   */
  private final String errorStatement;
  /**
   * The Stage.
   */
  private final Stage stage;
  /**
   * The Popup.
   */
  private final Popup popup;
  /**
   * The Bad input dialog pane.
   */
  @FXML
  private DialogPane badInputDialogPane;

  /**
   * Instantiates a new Bad input controller.
   *
   * @param stage          the stage
   * @param errorStatement the error statement
   */
  public BadInputController(Stage stage, String errorStatement) {
    this.stage = stage;
    this.popup = new Popup();
    this.errorStatement = errorStatement;
  }

  /**
   * Runs the controller.
   */
  @Override
  public void run() {
    Scene scene = null;
    try {
      FXMLLoader loader = new FXMLLoader(
          this.getClass().getClassLoader().getResource("BadInputDialog.fxml"));
      loader.setController(this);
      scene = loader.load();
    } catch (IOException e) {
      System.out.println("loader failed");
    }
    Button finishButton = (Button) this.badInputDialogPane.lookupButton(ButtonType.CLOSE);
    finishButton.setOnAction(event -> this.popup.hide());
    TextArea errorText = (TextArea) this.badInputDialogPane.getContent()
        .lookup("#badInputTextArea");
    errorText.setWrapText(true);
    errorText.setText(this.errorStatement);
    this.popup.getContent().add(Objects.requireNonNull(scene).getRoot());
    this.popup.show(this.stage);
  }


}
