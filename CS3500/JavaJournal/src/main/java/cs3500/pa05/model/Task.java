package cs3500.pa05.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa05.model.enums.CompletionStatus;
import cs3500.pa05.model.enums.DayType;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Represents a task calendar entry.
 */
public class Task extends AbstractCalendarEntry {

  /**
   * The Completion status.
   */
  @JsonProperty("completionStatus")
  private CompletionStatus completionStatus;

  /**
   * Instantiates a new Task.
   *
   * @param entryName        the entry name
   * @param description      the description
   * @param dayType          the day
   * @param completionStatus the completion status
   */
  public Task(
      @JsonProperty("entryName") String entryName,
      @JsonProperty("description") String description,
      @JsonProperty("dayType") DayType dayType,
      @JsonProperty("completionStatus") CompletionStatus completionStatus) {
    super(entryName, description, dayType);
    this.completionStatus = completionStatus;
  }

  /**
   * Sets completion status.
   *
   * @param completionStatus the completion status
   */
  public void setCompletionStatus(CompletionStatus completionStatus) {
    this.completionStatus = completionStatus;
  }

  /**
   * Indicates whether some other object is "equal to" this task.
   *
   * @param obj the obj to compare to
   * @return a boolean representing if this task is equal to a given task
   */
  @Override
  public boolean equals(Object obj) {
    boolean result = false;
    if (obj != null) {
      if (this.getClass() == obj.getClass()) {
        Task other = (Task) obj;
        result = super.isSameEntry(other)
            && this.completionStatus == other.completionStatus;
      }
    }
    return result;
  }

  /**
   * Returns a hash code value for this task.
   *
   * @return an int representing this task's hashcode
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.entryName, this.description, this.dayType, this.completionStatus);
  }

  /**
   * Returns a string representing the contents of this task that the viewer will see on the view.
   *
   * @return the string representing the task in the view
   */
  @Override
  public String textDescription() {
    StringJoiner result = new StringJoiner(System.lineSeparator());
    result.add(this.description);
    return result.toString();
  }

  /**
   * Gets status.
   *
   * @return the status
   */
  @JsonIgnore
  @Override
  public CompletionStatus getStatus() {
    return this.completionStatus;
  }
}
