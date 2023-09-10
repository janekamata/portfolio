package cs3500.pa05.model;

import static cs3500.pa05.model.enums.CompletionStatus.COMPLETE;
import static cs3500.pa05.model.enums.CompletionStatus.INCOMPLETE;
import static cs3500.pa05.model.enums.DayType.MONDAY;
import static cs3500.pa05.model.enums.DayType.SUNDAY;
import static cs3500.pa05.model.enums.DayType.WEDNESDAY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Objects;
import org.junit.jupiter.api.Test;

/**
 * Represents test for task.
 */
class TaskTest {

  /**
   * Test set complete status.
   */
  @Test
  void testSetCompleteStatus() {
    Task task = new Task("Example Task", "This is an example task I am making.", MONDAY,
        INCOMPLETE);
    Task taskExpected = new Task("Example Task", "This is an example task I am making.", MONDAY,
        COMPLETE);
    assertNotEquals(task, taskExpected);
    task.setCompletionStatus(COMPLETE);
    assertEquals(task, taskExpected);
  }

  /**
   * Test hash code.
   */
  @Test
  void testHashCode() {
    CalendarEntry taskHashCode = new Task("Get grocery", "Pick up carrots, potatoes, ice cream.",
        WEDNESDAY,
        COMPLETE);
    assertEquals(
        Objects.hash("Get grocery", "Pick up carrots, potatoes, ice cream.", WEDNESDAY, COMPLETE),
        taskHashCode.hashCode());
  }

  /**
   * Test equals.
   */
  @Test
  void testEquals() {
    Task task = new Task("Get grocery", "Pick up carrots, potatoes, ice cream.", WEDNESDAY,
        COMPLETE);
    assertNotEquals(task, SUNDAY);
  }

  /**
   * Test unequals.
   */
  @Test
  void testUnequals() {
    Task task = new Task("Get grocery", "Pick up carrots, potatoes, ice cream.", WEDNESDAY,
        COMPLETE);
    assertNotEquals(task, null);
    assertNotEquals(task, new Task("bad", "bad", MONDAY, INCOMPLETE));
    assertNotEquals(task, new Task("Get grocery", "bad", MONDAY, INCOMPLETE));
    assertNotEquals(task,
        new Task("Get grocery", "Pick up carrots, potatoes, ice cream.", MONDAY, INCOMPLETE));
    assertNotEquals(task,
        new Task("Get grocery", "Pick up carrots, potatoes, ice cream.", WEDNESDAY, INCOMPLETE));
    assertEquals(
        new Task("Get grocery", "Pick up carrots, potatoes, ice cream.", WEDNESDAY, COMPLETE),
        task);
  }

  /**
   * Test text description.
   */
  @Test
  void testTextDescription() {
    CalendarEntry taskHashCode = new Task("Get grocery", "Pick up carrots, potatoes, ice cream.",
        WEDNESDAY,
        COMPLETE);
    assertEquals("Pick up carrots, potatoes, ice cream.", taskHashCode.textDescription());
  }

  /**
   * Test get status.
   */
  @Test
  void testGetStatus() {
    Task task = new Task("", "", MONDAY, COMPLETE);
    task.setCompletionStatus(INCOMPLETE);
    assertEquals(INCOMPLETE, task.getStatus());
  }
}
