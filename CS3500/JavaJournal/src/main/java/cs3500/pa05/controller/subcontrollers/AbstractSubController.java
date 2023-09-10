package cs3500.pa05.controller.subcontrollers;

import cs3500.pa05.controller.JournalController;
import cs3500.pa05.controller.MasterController;
import cs3500.pa05.model.JournalInteractable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.Popup;
import javafx.stage.Stage;

/**
 * Represents an abstract sub controller.
 */
public abstract class AbstractSubController implements JournalController {

  /**
   * The Journal.
   */
  protected final JournalInteractable journal;
  /**
   * The Master.
   */
  protected final MasterController master;
  /**
   * The Stage.
   */
  protected final Stage stage;
  /**
   * The Popup.
   */
  protected final Popup popup;

  /**
   * Instantiates a new Abstract sub controller.
   *
   * @param stage   the stage
   * @param journal the journal
   * @param master  the master
   */
  protected AbstractSubController(Stage stage, JournalInteractable journal,
      MasterController master) {
    this.stage = stage;
    this.journal = journal;
    this.popup = new Popup();
    this.master = master;
  }

  /**
   * Load pop up.
   *
   * @param finish     the finish
   * @param cancel     the cancel
   * @param scene      the scene
   * @param dialogPane the dialog pane
   */
  public void loadPopUp(EventHandler<ActionEvent> finish, EventHandler<ActionEvent> cancel,
      Scene scene,
      DialogPane dialogPane) {
    Button finishButton = (Button) dialogPane.lookupButton(ButtonType.FINISH);
    finishButton.setOnAction(finish);
    Button cancelButton = (Button) dialogPane.lookupButton(ButtonType.CANCEL);
    cancelButton.setOnAction(cancel);
    this.popup.getContent().add(scene.getRoot());
  }


}
