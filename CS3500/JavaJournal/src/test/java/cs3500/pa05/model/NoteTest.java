package cs3500.pa05.model;

import static cs3500.pa05.model.enums.CompletionStatus.COMPLETE;
import static cs3500.pa05.model.enums.DayType.WEDNESDAY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Objects;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Represents tests for note.
 */
class NoteTest {

  /**
   * The Note.
   */
  private Note note;

  /**
   * Sets up.
   */
  @BeforeEach
  void setUp() {
    this.note = new Note("hello");
  }

  /**
   * Tear down.
   */
  @AfterEach
  void tearDown() {
    this.note = null;
  }

  /**
   * Test hash code.
   */
  @Test
  void testHashCode() {
    assertEquals(Objects.hash("hello"), this.note.hashCode());
  }

  /**
   * Test equals.
   */
  @Test
  void testEquals() {
    assertEquals(new Note("hello"), this.note);
  }

  /**
   * Test unequals.
   */
  @Test
  void testUnequals() {
    Task task = new Task("Get grocery", "Pick up carrots, potatoes, ice cream.", WEDNESDAY,
        COMPLETE);
    assertNotEquals(this.note, null);
    assertNotEquals(this.note, new Note("blah"));
    assertNotEquals(this.note,
        new Task("Get grocery", "Pick up carrots, potatoes, ice cream.", WEDNESDAY, COMPLETE));
  }

  /**
   * Test set contents.
   */
  @Test
  void testSetContents() {
    Note emptyNote = new Note();
    assertEquals(new Note(""), emptyNote);
    emptyNote.setContents("hello");
    assertEquals(emptyNote, this.note);
  }

  /**
   * Test get contents.
   */
  @Test
  void testGetContents() {
    assertEquals("hello", this.note.getContents());
  }
}
