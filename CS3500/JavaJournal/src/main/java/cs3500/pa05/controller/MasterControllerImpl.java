package cs3500.pa05.controller;

import cs3500.pa05.controller.persistence.MyFileWriter;
import cs3500.pa05.controller.subcontrollers.AddEventController;
import cs3500.pa05.controller.subcontrollers.AddTaskController;
import cs3500.pa05.controller.subcontrollers.CalendarFeatureController;
import cs3500.pa05.controller.subcontrollers.OpenJournalController;
import cs3500.pa05.controller.subcontrollers.SaveJournalController;
import cs3500.pa05.controller.subcontrollers.SetWeekNameController;
import cs3500.pa05.controller.subcontrollers.SetWeekStartController;
import cs3500.pa05.controller.subcontrollers.SortTaskOrEventController;
import cs3500.pa05.controller.subcontrollers.StartFromTemplateController;
import cs3500.pa05.model.Calendar;
import cs3500.pa05.model.Event;
import cs3500.pa05.model.JournalImpl;
import cs3500.pa05.model.JournalInteractable;
import cs3500.pa05.model.Note;
import cs3500.pa05.model.Task;
import cs3500.pa05.model.enums.CalendarFeature;
import cs3500.pa05.model.enums.DayType;
import cs3500.pa05.view.CalendarCardView;
import cs3500.pa05.view.TaskQueueTaskView;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Represents an implementation of the journal controller.
 */
public class MasterControllerImpl implements MasterController {

  /**
   * The Add task controller.
   */
  private final JournalController addTaskController;
  /**
   * The Add event controller.
   */
  private final JournalController addEventController;
  /**
   * The Set task or event limit controller.
   */
  private final TaskOrEventController setTaskOrEventLimitController;
  /**
   * The Sort task or event controller.
   */
  private final JournalController sortTaskOrEventController;
  /**
   * The Set week name controller.
   */
  private final SetWeekNameController setWeekNameController;
  /**
   * The Set week start controller.
   */
  private final SetWeekStartController setWeekStartController;
  /**
   * The Save journal controller.
   */
  private final JournalController saveJournalController;
  /**
   * The Open journal controller.
   */
  private final OpenJournalController openJournalController;

  /**
   * the Template journal controller
   */
  private final JournalController startFromTemplateController;

  /**
   * The Journal.
   */
  private final JournalInteractable journal;
  /**
   * The Stage.
   */
  private final Stage stage;
  /**
   * The Day boxes.
   */
  private final List<VBox> dayBoxes = new ArrayList<>();
  /**
   * Set start menu item
   */
  @FXML
  private MenuItem setStartMenuItem;
  /**
   * Week stats box
   */
  @FXML
  private HBox weekStatsHbox;
  /**
   * The Set week name menu item.
   */
  @FXML
  private MenuItem setWeekNameMenuItem;
  /**
   * The Add task menu item.
   */
  @FXML
  private MenuItem addTaskMenuItem;
  /**
   * The Add event menu item.
   */
  @FXML
  private MenuItem addEventMenuItem;
  /**
   * The Change event limit menu item.
   */
  @FXML
  private MenuItem changeEventLimitMenuItem;
  /**
   * The Change task limit menu item.
   */
  @FXML
  private MenuItem changeTaskLimitMenuItem;
  /**
   * The Sort event and task menu item.
   */
  @FXML
  private MenuItem sortItemsMenuItem;
  /**
   * The Task queue pane vbox.
   */
  @FXML
  private VBox taskQueuePaneVbox;
  /**
   * The Notes text area.
   */
  @FXML
  private TextArea notesTextArea;
  /**
   * The Day labels.
   */
  @FXML
  private List<Label> dayBoxLabels;
  /**
   * The Task queue search.
   */
  @FXML
  private TextField taskQueueSearch;
  /**
   * The Save menu item.
   */
  @FXML
  private MenuItem saveMenuItem;
  /**
   * The Open menu item.
   */
  @FXML
  private MenuItem openMenuItem;
  /**
   * The New week menu item.
   */
  @FXML
  private MenuItem newWeekMenuItem;
  /**
   * The Calendar grid pane.
   */
  @FXML
  private GridPane calendarGridPane;
  /**
   * The template menu item
   */
  @FXML
  private MenuItem startFromTemplateMenuItem;
  @FXML
  private Scene scene;


  /**
   * Instantiates a new Journal controller.
   *
   * @param journal the journal
   * @param stage   the stage
   */
  public MasterControllerImpl(JournalInteractable journal, Stage stage) {
    this.journal = journal;
    this.stage = stage;
    this.addTaskController = new AddTaskController(stage, journal, this);
    this.addEventController = new AddEventController(stage, journal, this);
    this.setTaskOrEventLimitController = new CalendarFeatureController(stage, journal, this);
    this.sortTaskOrEventController = new SortTaskOrEventController(stage, journal, this);
    this.setWeekNameController = new SetWeekNameController(stage, journal, this);
    this.setWeekStartController = new SetWeekStartController(stage, journal, this);
    this.saveJournalController = new SaveJournalController(stage, journal, this);
    this.openJournalController = new OpenJournalController(stage, journal, this);
    this.startFromTemplateController = new StartFromTemplateController(stage, journal, this);
  }

  /**
   * Update view.
   */
  @Override
  public void updateView() {
    this.updateCalendar();
    this.updateTaskQueue();
    this.notesTextArea.setText(this.journal.getNote());
  }

  /**
   * Update journal.
   *
   * @param other the other
   */
  @Override
  public void updateJournal(JournalInteractable other) {
    this.journal.setNote(other.getNote());
    this.journal.setPassword(other.getPassword());
    this.journal.setPath(other.getPath());
    this.journal.setCalendar(other.getCalendar());
  }

  /**
   * Runs the program.
   */
  @Override
  @FXML
  public void run() {
    this.initDayBoxes();
    this.initDayLabels();
    this.addTaskMenuItem.setOnAction(event -> this.runAndUpdateView(this.addTaskController));
    this.addEventMenuItem.setOnAction(event -> this.runAndUpdateCalender(this.addEventController));
    this.changeEventLimitMenuItem.setOnAction(event -> this.setEventLimit());
    this.changeTaskLimitMenuItem.setOnAction(event -> this.setTaskLimit());
    this.sortItemsMenuItem.setOnAction(event -> this.sortTasksAndEvents());
    this.setWeekNameMenuItem.setOnAction(
        event -> this.runAndUpdateCalender(this.setWeekNameController));
    this.setStartMenuItem.setOnAction(
        event -> this.runAndUpdateCalender(this.setWeekStartController));
    this.taskQueueSearch.setOnKeyTyped(event -> this.updateTaskQueue());
    this.saveMenuItem.setOnAction(event -> this.saveJournal());
    this.notesTextArea.setOnKeyTyped(event -> this.journal.setNote(this.notesTextArea.getText()));
    this.openMenuItem.setOnAction(event -> this.runAndUpdateView(this.openJournalController));
    this.newWeekMenuItem.setOnAction(event -> this.newJournal());
    this.startFromTemplateMenuItem.setOnAction(event -> this.startFromTemplateController.run());
    this.updateView();
    System.out.println("Controller is running");
  }

  /**
   * New journal.
   */
  private void newJournal() {
    this.saveJournal();
    Calendar calendar = new Calendar();
    Note note = new Note();
    String password = " ";
    this.updateJournal(new JournalImpl(calendar, note, password));
    this.updateView();
  }

  /**
   * Runs the controller and updates the view
   *
   * @param controller the controller ot be run
   */
  private void runAndUpdateView(JournalController controller) {
    controller.run();
    this.updateView();
  }

  /**
   * Runs and updates the calendar
   *
   * @param controller the controller to be run
   */
  private void runAndUpdateCalender(JournalController controller) {
    controller.run();
    this.updateCalendar();
  }


  /**
   * Opens save journal dialog
   */
  private void saveJournal() {
    if (this.journal.getPath() == null) {
      this.saveJournalController.run();
    } else {
      MyFileWriter.writeFromJournalToFile(this.journal, this.journal.getPath());
    }
  }

  /**
   * Sort events and tasks
   */
  private void sortTasksAndEvents() {
    this.sortTaskOrEventController.run();
  }

  /**
   * Sets task limit.
   */
  private void setTaskLimit() {
    this.setTaskOrEventLimitController.setTaskOrEvent(CalendarFeature.TASK);
    this.setTaskOrEventLimitController.run();
  }

  /**
   * Sets event limit.
   */
  private void setEventLimit() {
    this.setTaskOrEventLimitController.setTaskOrEvent(CalendarFeature.EVENT);
    this.setTaskOrEventLimitController.run();
  }

  /**
   * Update task queue.
   */
  private void updateTaskQueue() {
    this.taskQueuePaneVbox.getChildren().clear();
    List<Task> tasks = this.journal.getTasks(CalendarFeature.TASK).values().stream()
        .flatMap(Collection::stream)
        .toList();
    tasks = tasks.stream()
        .filter(task -> this.isMatchSearch(task.getEntryName())).toList();
    this.taskQueuePaneVbox.getChildren()
        .addAll(tasks.stream().map(task -> new TaskQueueTaskView(this).create(task)).toList());
    this.updateTaskQueueStats();
  }

  /**
   * Determines if the given string matches the search text using partial matching.
   *
   * @param taskName the task name
   * @return the boolean representing if they match
   */
  private boolean isMatchSearch(String taskName) {
    String searchText = this.taskQueueSearch.getText().toLowerCase().strip();
    String strip = taskName.toLowerCase().strip();
    return strip.startsWith(searchText) || strip.contains(searchText);
  }

  /**
   * Update task queue stats.
   */
  @Override
  public void updateTaskQueueStats() {
    ProgressBar taskProgressBar = (ProgressBar) this.stage.getScene().lookup(
        "#taskProgressBar");
    Label taskProgressBarLabel = (Label) this.stage.getScene().lookup("#taskProgressBarLabel");
    int countTasks = this.journal.getTasks(CalendarFeature.TASK).values().stream()
        .mapToInt(Collection::size)
        .sum();
    int completedTaskPercentage;
    if (this.journal.countCompletedTasks() == 0) {
      completedTaskPercentage = 0;
    } else {
      completedTaskPercentage = this.journal.countCompletedTasks() * 100 / countTasks;
    }
    taskProgressBarLabel.setText(completedTaskPercentage + "%");
    taskProgressBar.setProgress(completedTaskPercentage * 1.0 / 100);
  }

  /**
   * Update calendar.
   */
  @Override
  public void updateCalendar() {
    this.dayBoxes.forEach(day -> day.getChildren().clear());
    Map<DayType, List<Event>> events = this.journal.getEvents();
    Map<DayType, List<Task>> tasks = this.journal.getTasks(CalendarFeature.EVENT);
    events.forEach((dayType, eventList) ->
        eventList.forEach(event ->
            this.dayBoxes.get(dayType.getOrder()).getChildren()
                .add(new CalendarCardView().create(event))
        )
    );
    tasks.forEach((dayType, taskList) ->
        taskList.forEach(task ->
            this.dayBoxes.get(dayType.getOrder()).getChildren()
                .add(new CalendarCardView().create(task))
        )
    );
    Label weekStatsEventsNumber = (Label) this.weekStatsHbox.lookup("#weekStatsEventsNumber");
    weekStatsEventsNumber.setText(
        String.valueOf(events.values().stream().mapToInt(List::size).sum()));
    Label weekStatsTasksNumber = (Label) this.weekStatsHbox.lookup("#weekStatsTasksNumber");
    weekStatsTasksNumber.setText(
        String.valueOf(tasks.values().stream().mapToInt(List::size).sum()));
    Label maxEventsNumber = (Label) this.weekStatsHbox.lookup("#maxEventsNumber");
    maxEventsNumber.setText(String.valueOf(this.journal.getMaxEvents()));
    Label maxTasksNumber = (Label) this.weekStatsHbox.lookup("#maxTasksNumber");
    maxTasksNumber.setText(String.valueOf(this.journal.getMaxTasks()));
    Label weekLabel = (Label) this.scene.lookup("#weekLabel");
    weekLabel.setText(this.journal.getWeekName());
    this.updateCalendarLabels();
  }

  /**
   * Update calendar labels.
   */
  private void updateCalendarLabels() {
    for (DayType day : DayType.values()) {
      this.dayBoxLabels.get(day.getOrder()).setText(day.toString());
    }
  }

  /**
   * Init day boxes.
   */
  private void initDayBoxes() {
    VBox day1Vbox = (VBox) this.calendarGridPane.lookup("#day1Vbox");
    VBox day2Vbox = (VBox) this.calendarGridPane.lookup("#day2Vbox");
    VBox day3Vbox = (VBox) this.calendarGridPane.lookup("#day3Vbox");
    VBox day4Vbox = (VBox) this.calendarGridPane.lookup("#day4Vbox");
    VBox day5Vbox = (VBox) this.calendarGridPane.lookup("#day5Vbox");
    VBox day6Vbox = (VBox) this.calendarGridPane.lookup("#day6Vbox");
    VBox day7Vbox = (VBox) this.calendarGridPane.lookup("#day7Vbox");
    this.dayBoxes.addAll(
        List.of(day1Vbox, day2Vbox, day3Vbox, day4Vbox, day5Vbox, day6Vbox, day7Vbox));
  }

  /**
   * Init day labels.
   */
  private void initDayLabels() {
    Label day1Label = (Label) this.calendarGridPane.lookup("#day1Label");
    Label day2Label = (Label) this.calendarGridPane.lookup("#day2Label");
    Label day3Label = (Label) this.calendarGridPane.lookup("#day3Label");
    Label day4Label = (Label) this.calendarGridPane.lookup("#day4Label");
    Label day5Label = (Label) this.calendarGridPane.lookup("#day5Label");
    Label day6Label = (Label) this.calendarGridPane.lookup("#day6Label");
    Label day7Label = (Label) this.calendarGridPane.lookup("#day7Label");
    this.dayBoxLabels = List.of(day1Label, day2Label, day3Label, day4Label, day5Label, day6Label,
        day7Label);
  }
}

