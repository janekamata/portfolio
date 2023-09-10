package cs3500.pa05.model;

import static cs3500.pa05.model.enums.CompletionStatus.INCOMPLETE;
import static cs3500.pa05.model.enums.DayType.MONDAY;
import static cs3500.pa05.model.enums.DayType.THURSDAY;
import static org.junit.jupiter.api.Assertions.assertEquals;

import cs3500.pa05.model.comparators.CompareByName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Represents tests for compare by name.
 */
class CompareByNameTest {

  /**
   * The Event 1.
   */
  Event event1;
  /**
   * The Event 2.
   */
  Event event2;
  /**
   * The Task 1.
   */
  Task task1;
  /**
   * The Task 2.
   */
  Task task2;
  /**
   * The Name compare.
   */
  CompareByName nameCompare = new CompareByName();

  /**
   * Sets up.
   */
  @BeforeEach
  void setUp() {
    this.event1 = new Event("Example Event", "This is an example event I am making.", THURSDAY,
        new Time(10, 10), 60);
    this.event2 = new Event("Another Event", "This is an example event I am making.", THURSDAY,
        new Time(10, 10), 60);

    this.task1 = new Task("Example Task", "This is an example task I am making.", MONDAY,
        INCOMPLETE);
    this.task2 = new Task("Another Task", "This is an example task I am making.", MONDAY,
        INCOMPLETE);
  }

  /**
   * Test event.
   */
  @Test
  void testEvent() {
    assertEquals(4, this.nameCompare.compare(this.event1, this.event2));
    assertEquals(-4, this.nameCompare.compare(this.event2, this.event1));
    assertEquals(0, this.nameCompare.compare(this.event2, this.event2));
  }

  /**
   * Test task.
   */
  @Test
  void testTask() {
    assertEquals(4, this.nameCompare.compare(this.task1, this.task2));
    assertEquals(-4, this.nameCompare.compare(this.task2, this.task1));
    assertEquals(0, this.nameCompare.compare(this.task2, this.task2));
  }

}