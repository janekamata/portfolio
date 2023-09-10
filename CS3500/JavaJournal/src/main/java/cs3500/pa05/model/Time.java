package cs3500.pa05.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a time.
 */
public record Time(
    @JsonProperty("hour") int hour,
    @JsonProperty("minutes") int minutes
) {

  /**
   * Returns a single string representing this time.
   *
   * @return the string representing this time
   */
  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    if (this.hour < 10) {
      result.append(0);
    }
    result.append(this.hour);
    result.append(':');
    if (this.minutes < 10) {
      result.append(0);
    }
    result.append(this.minutes);
    return result.toString();
  }

  /**
   * Indicates whether some other object is "equal to" this time.
   *
   * @param obj the obj to compare this time to
   * @return if this time is equal to the given object
   */
  @Override
  public boolean equals(Object obj) {
    boolean result = false;
    if (obj != null) {
      if (this.getClass() == obj.getClass()) {
        Time other = (Time) obj;
        result = this.hour == other.hour && this.minutes == other.minutes;
      }
    }
    return result;
  }

  /**
   * Returns a hash code value for this time.
   *
   * @return the hash code of the time
   */
  @Override
  public int hashCode() {
    return this.minutes * this.hour;
  }
}
