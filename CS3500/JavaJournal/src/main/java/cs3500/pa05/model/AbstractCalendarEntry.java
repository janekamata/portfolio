package cs3500.pa05.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa05.model.enums.DayType;

/**
 * Represents an abstract calendar entry.
 */
public abstract class AbstractCalendarEntry implements CalendarEntry {

  /**
   * The Entry name.
   */
  @JsonProperty("entryName")
  protected String entryName;
  /**
   * The Description.
   */
  @JsonProperty("description")
  protected String description;
  /**
   * The Day.
   */
  @JsonProperty("dayType")
  protected DayType dayType;

  /**
   * Instantiates a new Abstract calendar entry.
   *
   * @param entryName   the entry name
   * @param description the description
   * @param dayType     the day
   */
  AbstractCalendarEntry(
      @JsonProperty("entryName") String entryName,
      @JsonProperty("description") String description,
      @JsonProperty("dayType") DayType dayType) {
    this.entryName = entryName;
    this.description = description;
    this.dayType = dayType;
  }

  protected AbstractCalendarEntry() {

  }

  /**
   * Set description.
   *
   * @param description the description
   */
  @Override
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Get day type.
   *
   * @return the day type
   */
  @Override
  public DayType getDayType() {
    return this.dayType;
  }

  /**
   * Set day.
   *
   * @param dayType the day
   */
  @Override
  public void setDayType(DayType dayType) {
    this.dayType = dayType;
  }

  /**
   * Indicates where some other entry is "equal to" this calendar entry.
   *
   * @param other the other entry to compare to
   * @return the boolean representing if this entry and the given entry are equal
   */
  protected boolean isSameEntry(AbstractCalendarEntry other) {
    return this.entryName.equals(other.entryName)
        && this.description.equals(other.description)
        && this.dayType == other.dayType;
  }

  /**
   * Gets name.
   *
   * @return the name
   */
  @Override
  public String getEntryName() {
    return this.entryName;
  }

  /**
   * Set entry name.
   *
   * @param name the name
   */
  @Override
  public void setEntryName(String name) {
    this.entryName = name;
  }
}
