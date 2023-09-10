package cs3500.pa05.model;

import cs3500.pa05.model.enums.CompletionStatus;
import cs3500.pa05.model.enums.DayType;

/**
 * Represents a calendar entry.
 */
public interface CalendarEntry {

  /**
   * Gets day type.
   *
   * @return the day type
   */
  DayType getDayType();

  /**
   * Sets day type.
   *
   * @param dayType the day type
   */
  void setDayType(DayType dayType);

  /**
   * Returns a string representing the contents of this calendar entry that the viewer will see on
   * the view.
   *
   * @return string the string representing the entry in the view
   */
  String textDescription();

  /**
   * Gets entry name.
   *
   * @return the entry name
   */
  String getEntryName();

  /**
   * Sets entry name.
   *
   * @param name the name
   */
  void setEntryName(String name);

  /**
   * Gets the completion status
   *
   * @return the completion status
   */
  CompletionStatus getStatus();

  /**
   * Sets description.
   *
   * @param description the description
   */
  void setDescription(String description);

  /**
   * Returns a hash code value for the record
   *
   * @return the hashcode of this event
   */
  @Override
  int hashCode();

  /**
   * Indicates whether some other object is "equal to" this calendar entry.
   *
   * @param obj the obj to compare to
   * @return if this entry and the given object are equal
   */
  @Override
  boolean equals(Object obj);

}
