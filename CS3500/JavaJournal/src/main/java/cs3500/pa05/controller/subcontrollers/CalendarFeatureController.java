package cs3500.pa05.controller.subcontrollers;

import cs3500.pa05.controller.MasterController;
import cs3500.pa05.controller.TaskOrEventController;
import cs3500.pa05.controller.handlers.CancelHandler;
import cs3500.pa05.controller.handlers.event.ChangeEventLimitEventHandler;
import cs3500.pa05.controller.handlers.task.ChangeTaskLimitEventHandler;
import cs3500.pa05.model.JournalInteractable;
import cs3500.pa05.model.enums.CalendarFeature;
import java.io.IOException;
import java.util.Objects;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;

/**
 * Represents a calendar feature controller.
 */
public class CalendarFeatureController extends AbstractSubController implements
    TaskOrEventController {

  /**
   * The Set limit dialog pane.
   */
  @FXML
  private DialogPane setLimitDialogPane;

  /**
   * The Which calendar feature.
   */
  private CalendarFeature whichCalendarFeature = CalendarFeature.TASK;

  /**
   * Instantiates a new Calendar feature controller.
   *
   * @param stage   the stage
   * @param journal the journal
   * @param master  the master
   */
  public CalendarFeatureController(Stage stage, JournalInteractable journal,
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
          this.getClass().getClassLoader().getResource("SetLimitDialog.fxml"));
      loader.setController(this);
      scene = loader.load();
    } catch (IOException e) {
      System.out.println("loader failed");
    }
    EventHandler<ActionEvent> finish = null;
    EventHandler<ActionEvent> cancel = null;
    switch (this.whichCalendarFeature) {
      case TASK -> {
        finish = new ChangeTaskLimitEventHandler(this.stage, this.popup,
            this.setLimitDialogPane,
            this.journal, this.master);
        cancel = new CancelHandler(this.stage, this.popup,
            this.setLimitDialogPane,
            this.journal, this.master);
      }
      case EVENT -> {
        finish = new ChangeEventLimitEventHandler(this.stage, this.popup,
            this.setLimitDialogPane,
            this.journal, this.master);
        cancel = new CancelHandler(this.stage, this.popup,
            this.setLimitDialogPane,
            this.journal, this.master);
      }
      default -> System.out.println("Bad Calendar Feature");
    }
    super.loadPopUp(finish, cancel, Objects.requireNonNull(scene), this.setLimitDialogPane);
    this.popup.show(this.stage);
  }

  /**
   * Sets task or event.
   *
   * @param calendarFeatureSetting the calendar feature setting
   */
  @Override
  public void setTaskOrEvent(CalendarFeature calendarFeatureSetting) {
    this.whichCalendarFeature = calendarFeatureSetting;
  }
}
