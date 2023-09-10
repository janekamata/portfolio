package cs3500.pa05.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * Represents a note on the journal.
 */
public class Note {

  /**
   * The Note.
   */
  @JsonProperty("contents")
  private String contents;

  /**
   * Instantiates a new Note.
   */
  public Note() {
    this.contents = "";
  }

  /**
   * Instantiates a new Note.
   *
   * @param contents the note
   */
  public Note(@JsonProperty("contents") String contents) {
    this.contents = contents;
  }

  /**
   * Get contents string.
   *
   * @return the string
   */
  public String getContents() {
    return this.contents;
  }

  /**
   * Sets note.
   *
   * @param contents the note
   */
  public void setContents(String contents) {
    this.contents = contents;
  }

  /**
   * Indicates whether some other object is "equal to" this note.
   *
   * @param obj the object to be compared to
   * @return whether the given object is equal to this note
   */
  @Override
  public boolean equals(Object obj) {
    boolean result = false;
    if (obj != null) {
      if (this.getClass() == obj.getClass()) {
        Note other = (Note) obj;
        result = this.contents.equals(other.contents);
      }
    }
    return result;
  }

  /**
   * Returns a hash code value for the note.
   *
   * @return the hashcode of this note
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.contents);
  }
}
