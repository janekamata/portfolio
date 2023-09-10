package cs3500.pa05.model;

import cs3500.pa05.model.enums.CompletionStatus;
import cs3500.pa05.model.enums.DayType;
import java.util.Comparator;

/**
 * Represents an interactable journal.
 */
public interface JournalInteractable extends JournalState {

  /**
   * Sets start.
   *
   * @param start the start
   */
  void setStart(DayType start);

  /**
   * Sets week name.
   *
   * @param name the name
   */
  void setWeekName(String name);

  /**
   * Sets note content.
   *
   * @param content the content
   */
  void setNote(String content);

  /**
   * Returns the password
   *
   * @return the password
   */
  String getPassword();

  /**
   * sets the password
   *
   * @param password the password
   */
  void setPassword(String password);

  /**
   * Add task.
   *
   * @param entryName        the entry name
   * @param description      the description
   * @param day              the day
   * @param completionStatus the completion status
   */
  void addTask(String entryName, String description, DayType day,
      CompletionStatus completionStatus);

  /**
   * Add event to the calendar.
   *
   * @param entryName   the entry name
   * @param description the description
   * @param day         the day
   * @param startTime   the start time
   * @param duration    the duration
   */
  void addEvent(String entryName, String description, DayType day, Time startTime,
      int duration);

  /**
   * Sets task limit.
   *
   * @param limit the limit
   */
  void setTaskLimit(int limit);

  /**
   * Sets path.
   *
   * @param path the path
   */
  void setPath(String path);

  /**
   * Sets event limit.
   *
   * @param limit the limit
   */
  void setEventLimit(int limit);

  /**
   * Sorts the events and the tasks by either name or duration. For duration, tasks will follow any
   * sorting of the events.
   *
   * @param comparator what we are comparing by
   */
  void sortBy(Comparator<? super CalendarEntry> comparator);

  /**
   * Clears the tasks list for starting from template
   */
  void clearTasks();

  /**
   * Clears the events list for starting from a template
   */
  void clearEvents();

  /**
   * Sets calendar.
   *
   * @param calendar the calendar
   */
  void setCalendar(Calendar calendar);

}
