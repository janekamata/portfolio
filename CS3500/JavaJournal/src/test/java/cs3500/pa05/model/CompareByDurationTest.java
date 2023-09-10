package cs3500.pa05.model;

import static cs3500.pa05.model.enums.CompletionStatus.INCOMPLETE;
import static cs3500.pa05.model.enums.DayType.MONDAY;
import static cs3500.pa05.model.enums.DayType.THURSDAY;
import static org.junit.jupiter.api.Assertions.assertEquals;

import cs3500.pa05.model.comparators.CompareByDuration;
import java.util.Comparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Represents tests for compare by duration.
 */
class CompareByDurationTest {

  /**
   * The Duration compare.
   */
  private final Comparator<CalendarEntry> durationCompare = new CompareByDuration();
  /**
   * The Event 1.
   */
  private Event event1;
  /**
   * The Event 2.
   */
  private Event event2;
  /**
   * The Task 1.
   */
  private Task task1;
  /**
   * The Task 2.
   */
  private Task task2;

  /**
   * Sets up.
   */
  @BeforeEach
  void setUp() {
    this.event1 = new Event("Example Event", "This is an example event I am making.", THURSDAY,
        new Time(10, 10), 20);
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
    assertEquals(40, this.durationCompare.compare(this.event1, this.event2));
    assertEquals(-40, this.durationCompare.compare(this.event2, this.event1));
    assertEquals(0, this.durationCompare.compare(this.event2, this.event2));
  }

  /**
   * Test task.
   */
  @Test
  void testTask() {
    assertEquals(-1, this.durationCompare.compare(this.task1, this.task2));
    assertEquals(-1, this.durationCompare.compare(this.task2, this.task1));
    assertEquals(-1, this.durationCompare.compare(this.task2, this.task2));
  }
}