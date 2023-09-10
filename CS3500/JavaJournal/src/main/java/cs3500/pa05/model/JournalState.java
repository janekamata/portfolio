package cs3500.pa05.model;

import cs3500.pa05.model.enums.CalendarFeature;
import cs3500.pa05.model.enums.DayType;
import java.util.List;
import java.util.Map;

/**
 * Represents the state of a journal.
 */
public interface JournalState {

  /**
   * Determines if this journal has met the maximum amount of events.
   *
   * @return if max events is met
   */
  boolean hasMaxEvents();

  /**
   * Determines if this journal has met the maximum amount of tasks.
   *
   * @return if max tasks is met
   */
  boolean hasMaxTasks();

  /**
   * Returns the starting day type
   *
   * @return starting day
   */
  DayType getStart();

  /**
   * Counts the number of completed tasks.
   *
   * @return the int representing the number of completed tasks
   */
  int countCompletedTasks();

  /**
   * Returns a map from day type to a list of tasks from the calendar based on the given feature.
   *
   * @param feature the feature
   * @return the tasks map
   */
  Map<DayType, List<Task>> getTasks(CalendarFeature feature);

  /**
   * Gets week name.
   *
   * @return the week name
   */
  String getWeekName();

  /**
   * Get notes content.
   *
   * @return the string representing the content
   */
  String getNote();

  /**
   * Returns a map from day type to a list of events from the calendar based on the given feature.
   *
   * @return the list
   */
  Map<DayType, List<Event>> getEvents();

  /**
   * Gets path.
   *
   * @return the path
   */
  String getPath();

  /**
   * Gets max events limit.
   *
   * @return max event limit
   */
  int getMaxEvents();

  /**
   * Gets max tasks limit.
   *
   * @return max tasks limit
   */
  int getMaxTasks();

  /**
   * Indicates whether some other object is "equal to" this journal.
   *
   * @param obj the object to be compared to
   * @return the boolean representing whether the given object equals this journal.
   */
  @Override
  boolean equals(Object obj);

  /**
   * Returns a hash code value for this journal.
   *
   * @return the hash code of this journal
   */
  @Override
  int hashCode();

  /**
   * Gets calendar.
   *
   * @return the calendar
   */
  Calendar getCalendar();
}
