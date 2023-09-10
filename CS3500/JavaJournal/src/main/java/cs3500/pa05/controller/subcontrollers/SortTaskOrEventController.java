package cs3500.pa05.controller.subcontrollers;

import cs3500.pa05.controller.MasterController;
import cs3500.pa05.controller.handlers.CancelHandler;
import cs3500.pa05.controller.handlers.SortHandler;
import cs3500.pa05.model.JournalInteractable;
import cs3500.pa05.model.enums.ComparatorClass;
import java.io.IOException;
import java.util.Objects;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;

/**
 * Represents a Sort task or event controller.
 */
public class SortTaskOrEventController extends AbstractSubController {

  /**
   * The Sort task event dialog pane.
   */
  @FXML
  private DialogPane sortTaskEventDialogPane;

  /**
   * Instantiates a new Sort task or event controller.
   *
   * @param stage   the stage
   * @param journal the journal
   * @param master  the master
   */
  public SortTaskOrEventController(Stage stage, JournalInteractable journal,
      MasterController master) {
    super(stage, journal, master);

  }


  /**
   * Runs the controller.
   */
  @Override
  public void run() {
    Scene scene = null;
    try {
      FXMLLoader loader = new FXMLLoader(
          this.getClass().getClassLoader().getResource("SortTaskOrEventDialog.fxml"));
      loader.setController(this);
      scene = loader.load();
    } catch (IOException e) {
      System.out.println("loader failed");
    }

    EventHandler<ActionEvent> finish = new SortHandler(this.stage, this.popup,
        this.sortTaskEventDialogPane,
        this.journal,
        this.master);
    EventHandler<ActionEvent> cancel = new CancelHandler(this.stage, this.popup,
        this.sortTaskEventDialogPane,
        this.journal, this.master);

    this.addMenuOptions();
    super.loadPopUp(finish, cancel, Objects.requireNonNull(scene), this.sortTaskEventDialogPane);
    this.popup.show(this.stage);
  }

  /**
   * Add menu options.
   */
  private void addMenuOptions() {
    ChoiceBox sortTaskEventChoiceBox = (ChoiceBox) this.sortTaskEventDialogPane.lookup(
        "#sortTaskEventChoiceBox");
    sortTaskEventChoiceBox.getItems()
        .addAll(ComparatorClass.COMPARE_BY_NAME, ComparatorClass.COMPARE_BY_DURATION);
  }
}
