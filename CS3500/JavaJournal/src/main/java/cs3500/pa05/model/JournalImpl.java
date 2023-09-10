package cs3500.pa05.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import cs3500.pa05.model.enums.CalendarFeature;
import cs3500.pa05.model.enums.CompletionStatus;
import cs3500.pa05.model.enums.DayType;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a journal implementation.
 */
@JsonPropertyOrder({"note", "calendar"})
public class JournalImpl implements JournalInteractable {

  /**
   * The Calendar.
   */
  @JsonProperty("calendar")
  private Calendar calendar;
  /**
   * The Note.
   */
  @JsonProperty("note")
  private Note note;

  @JsonProperty("password")
  private String password;
  /**
   * The Path.
   */
  @JsonIgnore
  private String path;

  /**
   * Instantiates a new Journal.
   *
   * @param calendar the calendar
   * @param note     the note
   * @param password the password
   */
  public JournalImpl(
      @JsonProperty("calendar") Calendar calendar,
      @JsonProperty("note") Note note, @JsonProperty("password") String password) {
    this.calendar = calendar;
    this.note = note;
    this.password = password;
  }

  @Override
  public Calendar getCalendar() {
    return this.calendar;
  }

  @Override
  public void setCalendar(Calendar calendar) {
    this.calendar = calendar;
  }

  /**
   * Clears the tasks list for starting from template
   */
  @Override
  public void clearTasks() {
    this.calendar.clearTasks();
  }

  /**
   * Clears the events list for starting from a template
   */
  @Override
  public void clearEvents() {
    this.calendar.clearEvents();
  }


  /**
   * Returns this password
   */
  @Override
  public String getPassword() {
    return this.password;
  }

  /**
   * @param password sets the new password
   */
  @Override
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Determines if this journal has met the maximum amount of events.
   *
   * @return if max events is met
   */
  @Override
  public boolean hasMaxEvents() {
    return this.calendar.hasMaxEvents();
  }

  /**
   * Determines if this journal has met the maximum amount of tasks.
   *
   * @return if max tasks is met
   */
  @Override
  public boolean hasMaxTasks() {
    return this.calendar.hasMaxTasks();
  }

  /**
   * Returns the starting day type
   *
   * @return starting day
   */
  @JsonIgnore
  @Override
  public DayType getStart() {
    return this.calendar.getStartDay();
  }

  /**
   * Sets start.
   *
   * @param start the start
   */
  @Override
  public void setStart(DayType start) {
    this.calendar.setStartDay(start);
  }

  /**
   * Counts the number of completed tasks.
   *
   * @return the int representing the number of completed tasks
   */
  @Override
  public int countCompletedTasks() {
    return this.calendar.countCompleted();
  }


  /**
   * Get notes content.
   *
   * @return the string representing the content
   */
  @Override
  @JsonIgnore
  public String getNote() {
    return this.note.getContents();
  }

  /**
   * Sets note content.
   *
   * @param content the content
   */
  @Override
  public void setNote(String content) {
    this.note.setContents(content);
  }

  /**
   * Returns a map from day type to a list of tasks from the calendar based on the given feature.
   *
   * @param feature the feature
   * @return the tasks map
   */
  @Override
  public Map<DayType, List<Task>> getTasks(CalendarFeature feature) {
    return this.calendar.getTaskList(feature);
  }

  /**
   * Get week name string.
   *
   * @return the string
   */
  @Override
  @JsonIgnore
  public String getWeekName() {
    return this.calendar.getName();
  }

  /**
   * Sets week name.
   *
   * @param name the name
   */
  @Override
  public void setWeekName(String name) {
    this.calendar.setName(name);
  }

  /**
   * Returns a map from day type to a list of events from the calendar based on the given feature.
   *
   * @return the list
   */
  @Override
  public Map<DayType, List<Event>> getEvents() {
    return this.calendar.getEventList();
  }

  /**
   * Add task to the calendar.
   *
   * @param entryName        the entry name
   * @param description      the description
   * @param day              the day
   * @param completionStatus the completion status
   */
  @Override
  public void addTask(String entryName, String description, DayType day,
      CompletionStatus completionStatus) {
    this.calendar.addTask(new Task(entryName, description, day, completionStatus));
  }

  /**
   * Add event to the calendar.
   *
   * @param entryName   the entry name
   * @param description the description
   * @param day         the day
   * @param startTime   the start time
   * @param duration    the duration
   */
  @Override
  public void addEvent(String entryName, String description, DayType day, Time startTime,
      int duration) {
    this.calendar.addEvent(new Event(entryName, description, day, startTime, duration));
  }

  /**
   * Sets task limit.
   *
   * @param limit the limit
   */
  @Override
  public void setTaskLimit(int limit) {
    this.calendar.setMaxTasks(limit);
  }

  /**
   * Gets path.
   *
   * @return the path
   */
  @Override
  @JsonIgnore
  public String getPath() {
    return this.path;
  }

  /**
   * Sets path.
   *
   * @param path the path
   */
  @Override
  public void setPath(String path) {
    this.path = path;
  }

  /**
   * Sets event limit.
   *
   * @param limit the limit
   */
  @Override
  public void setEventLimit(int limit) {
    this.calendar.setMaxEvents(limit);
  }

  /**
   * Gets max events limit.
   *
   * @return max event limit
   */
  @Override
  @JsonIgnore
  public int getMaxEvents() {
    return this.calendar.getMaxEvents();
  }

  /**
   * Gets max tasks limit.
   *
   * @return max tasks limit
   */
  @Override
  @JsonIgnore
  public int getMaxTasks() {
    return this.calendar.getMaxTasks();
  }

  /**
   * Indicates whether some other object is "equal to" this journal.
   *
   * @param obj the object to be compared to
   * @return the boolean representing whether the given object equals this journal.
   */
  @Override
  public boolean equals(Object obj) {
    boolean result = false;
    if (obj != null) {
      if (this.getClass() == obj.getClass()) {
        JournalImpl other = (JournalImpl) obj;
        result = this.calendar.equals(other.calendar) && this.note.equals(other.note);
      }
    }
    return result;
  }

  /**
   * Returns a hash code value for this journal.
   *
   * @return the hash code of this journal
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.calendar, this.note);
  }

  /**
   * Sorts the events and the tasks by either name or duration. For duration, tasks will follow any
   * sorting of the events.
   *
   * @param comparator what we are comparing by
   */
  @Override
  public void sortBy(Comparator<? super CalendarEntry> comparator) {
    this.calendar.sortBy(comparator);
  }
}
