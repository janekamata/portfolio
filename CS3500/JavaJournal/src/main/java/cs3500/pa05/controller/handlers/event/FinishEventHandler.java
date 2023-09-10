package cs3500.pa05.controller.handlers.event;

import cs3500.pa05.controller.MasterController;
import cs3500.pa05.controller.handlers.AbstractEventHandler;
import cs3500.pa05.model.JournalInteractable;
import cs3500.pa05.model.Time;
import cs3500.pa05.model.enums.DayType;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Popup;
import javafx.stage.Stage;

/**
 * Represents a finish event handler.
 */
public class FinishEventHandler extends AbstractEventHandler {

  /**
   * The constant MAX_MINUTES.
   */
  private static final int MAX_MINUTES = 59;
  /**
   * The constant MAX_HOURS.
   */
  private static final int MAX_HOURS = 23;

  /**
   * Instantiates a new Finish event handler.
   *
   * @param stage   the stage
   * @param popup   the popup
   * @param dialog  the dialog
   * @param journal the journal
   * @param master  the master
   */
  public FinishEventHandler(Stage stage, Popup popup, DialogPane dialog,
      JournalInteractable journal,
      MasterController master) {
    super(stage, popup, dialog, journal, master);
  }

  /**
   * Determines if the given time is a valid time.
   *
   * @param eventTime the event time
   * @return the boolean representing if it's valid
   */
  private static boolean isValidTime(Time eventTime) {
    return eventTime.hour() <= 0 || eventTime.hour() > FinishEventHandler.MAX_HOURS
        || eventTime.minutes() < 0
        || eventTime.minutes() > FinishEventHandler.MAX_MINUTES;
  }

  /**
   * Invoked when a specific event of the type for which this handler is registered happens.
   *
   * @param event the event
   */
  @Override
  public void handle(ActionEvent event) {
    FXMLLoader loader = new FXMLLoader(
        this.getClass().getClassLoader().getResource("TaskCreationDialog.fxml"));
    try {
      loader.load();
    } catch (IOException e) {
      System.out.println("loader failed");
    }
    if (this.isValidInput()) {
      super.finishHandle();
    }
  }

  /**
   * Verify input.
   *
   * @return the boolean representing if the input is valid
   */
  @Override
  protected boolean isValidInput() {
    TextField eventCreationNameTextField = this.getTextField("#eventCreationNameTextField");
    TextArea eventCreationDescriptionTextArea = this.getTextArea(
        "#eventCreationDescriptionTextArea");
    ChoiceBox eventCreationDayChoiceBox = this.getChoiceBox("#eventCreationDayChoiceBox");
    TextField eventCreationTimeHourTextField = this.getTextField("#eventCreationTimeHourTextField");
    TextField eventCreationTimeMinuteTextField = this.getTextField(
        "#eventCreationTimeMinuteTextField");
    TextField eventCreationDurationTextField = this.getTextField("#eventCreationDurationTextField");
    boolean result = true;
    Time eventTime = this.parseEventTime(eventCreationTimeHourTextField.getText(),
        eventCreationTimeMinuteTextField.getText());
    int eventDuration = this.parseEventDuration(eventCreationDurationTextField.getText());
    if (eventCreationNameTextField.getText().isEmpty()) {
      this.showError("Please make sure the name is not null");
      result = false;
    }
    if (eventDuration <= 0) {
      this.showError("Please make sure the duration is at least 1.");
      result = false;
    }
    if (FinishEventHandler.isValidTime(eventTime)) {
      this.showError("Please input a valid time. Times are in 24hr format.");
      result = false;
    }
    if (result) {
      this.addEventToJournal(
          eventCreationNameTextField.getText(),
          eventCreationDescriptionTextArea.getText(),
          (DayType) eventCreationDayChoiceBox.getValue(),
          eventTime,
          eventDuration
      );
    }
    return result;
  }

  /**
   * Gets text field.
   *
   * @param id the id
   * @return the text field
   */
  private TextField getTextField(String id) {
    return (TextField) this.dialog.getContent().lookup(id);
  }

  /**
   * Gets text area.
   *
   * @param id the id
   * @return the text area
   */
  private TextArea getTextArea(String id) {
    return (TextArea) this.dialog.getContent().lookup(id);
  }

  /**
   * Gets choice box.
   *
   * @param id the id
   * @return the choice box
   */
  private ChoiceBox getChoiceBox(String id) {
    return (ChoiceBox) this.dialog.getContent().lookup(id);
  }

  /**
   * Parse event time.
   *
   * @param hour   the hour
   * @param minute the minute
   * @return the time
   */
  private Time parseEventTime(String hour, String minute) {
    int eventHour = this.parseInteger(hour, 0);
    int eventMinute = this.parseInteger(minute, 0);
    return new Time(eventHour, eventMinute);
  }

  /**
   * Parse event duration int.
   *
   * @param duration the duration
   * @return the int
   */
  private int parseEventDuration(String duration) {
    return this.parseInteger(duration, 0);
  }

  /**
   * Parse integer int.
   *
   * @param value        the value
   * @param defaultValue the default value
   * @return the int
   */
  private int parseInteger(String value, int defaultValue) {
    int result = defaultValue;
    try {
      result = Integer.parseInt(value);
    } catch (NumberFormatException e) {
      this.showError(
          "Please fill out the following required fields: name(String), "
              +
              "day of week, start time(int), and duration(int)");
    }
    return result;
  }

  /**
   * Show error.
   *
   * @param message the message
   */
  private void showError(String message) {
    super.showErrorPopUp(message);
  }

  /**
   * Add event to journal.
   *
   * @param name        the name
   * @param description the description
   * @param day         the day
   * @param time        the time
   * @param duration    the duration
   */
  private void addEventToJournal(String name, String description, DayType day, Time time,
      int duration) {
    this.journal.addEvent(name, description, day, time, duration);
  }

}