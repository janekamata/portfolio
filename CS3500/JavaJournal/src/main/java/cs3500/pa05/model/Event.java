package cs3500.pa05.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa05.model.enums.CompletionStatus;
import cs3500.pa05.model.enums.DayType;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Represents an event calendar entry.
 */
public class Event extends AbstractCalendarEntry {

  /**
   * The Start time.
   */
  @JsonProperty("startTime")
  private Time startTime;
  /**
   * The Duration.
   */
  @JsonProperty("duration")
  private int duration;

  /**
   * Instantiates a new Event.
   *
   * @param entryName   the entry name
   * @param description the description
   * @param dayType     the day
   * @param startTime   the start time
   * @param duration    the duration
   */
  public Event(
      @JsonProperty("entryName") String entryName,
      @JsonProperty("description") String description,
      @JsonProperty("dayType") DayType dayType,
      @JsonProperty("startTime") Time startTime,
      @JsonProperty("duration") int duration) {
    super(entryName, description, dayType);
    this.startTime = startTime;
    this.duration = duration;
  }

  /**
   * Sets start time.
   *
   * @param startTime the start time
   */
  public void setStartTime(Time startTime) {
    this.startTime = startTime;
  }

  /**
   * Indicates whether some other object is "equal to" this event.
   *
   * @param obj the obj to compare to
   * @return if this event and the given object are equal
   */
  @Override
  public boolean equals(Object obj) {
    boolean result = false;
    if (obj != null) {
      if (this.getClass() == obj.getClass()) {
        Event other = (Event) obj;
        result = super.isSameEntry(other)
            && this.startTime.equals(other.startTime)
            && this.duration == other.duration;
      }
    }
    return result;
  }

  /**
   * Hash code int.
   *
   * @return the hashcode of this event
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.description, this.dayType, this.startTime, this.duration);
  }

  /**
   * Returns a string representing the contents of this event that the viewer will see on the view.
   *
   * @return the string representing the event in the view
   */
  @Override
  public String textDescription() {
    StringJoiner result = new StringJoiner(System.lineSeparator());
    result.add(
        "Start: " + this.startTime.toString());
    result.add("Duration: " + this.duration + " minutes");
    result.add("");
    result.add(this.description);
    return result.toString();
  }

  /**
   * Events are considered complete
   *
   * @return the completion status
   */
  @JsonIgnore
  @Override
  public CompletionStatus getStatus() {
    return CompletionStatus.NONE;
  }

  /**
   * Gets the duration.
   *
   * @return the duration of this event
   */
  public int getDuration() {
    return this.duration;
  }

  /**
   * Sets duration.
   *
   * @param duration the duration
   */
  public void setDuration(int duration) {
    this.duration = duration;
  }
}
