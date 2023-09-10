package cs3500.pa05.model;

import static cs3500.pa05.model.enums.CompletionStatus.NONE;
import static cs3500.pa05.model.enums.DayType.SUNDAY;
import static cs3500.pa05.model.enums.DayType.THURSDAY;
import static cs3500.pa05.model.enums.DayType.TUESDAY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Represents testes for event.
 */
class EventTest {

  /**
   * The constant LINE_SEPARATOR.
   */
  private static final String LINE_SEPARATOR = System.lineSeparator();

  /**
   * The Event.
   */
  private Event event;
  /**
   * The Old event.
   */
  private Event oldEvent;

  /**
   * Sets up.
   */
  @BeforeEach
  void setUp() {
    this.event = new Event("Example Event", "This is an example event I am making.", THURSDAY,
        new Time(10, 10), 60);
    this.oldEvent = new Event("Example Event", "This is an example event I am making.", THURSDAY,
        new Time(10, 10), 60);
  }

  /**
   * Tear down.
   */
  @AfterEach
  void tearDown() {
    this.event = null;
    this.oldEvent = null;
  }

  /**
   * Test set start time.
   */
  @Test
  void testSetStartTime() {
    assertEquals(this.event, this.oldEvent);
    this.event.setStartTime(new Time(11, 11));
    assertNotEquals(this.event, this.oldEvent);
  }

  /**
   * Test set duration.
   */
  @Test
  void testSetDuration() {
    assertEquals(this.event, this.oldEvent);
    this.event.setDuration(50);
    assertNotEquals(this.event, this.oldEvent);
  }

  /**
   * Test unequals.
   */
  @Test
  void testUnequals() {
    assertNotEquals(this.event, TUESDAY);
    assertNotEquals(this.event, THURSDAY);
    assertNotEquals(this.event, null);
    assertNotEquals(this.event, new Event("no", "bad", SUNDAY, new Time(1, 1), 9));
    assertNotEquals(this.event, new Event("Example Event", "bad", SUNDAY, new Time(1, 1), 9));
    assertNotEquals(this.event,
        new Event("Example Event", "This is an example event I am making.", SUNDAY,
            new Time(1, 1),
            9));
  }

  /**
   * Test event description.
   */
  @Test
  void testEventDescription() {
    assertEquals("Start: 10:10"
        + EventTest.LINE_SEPARATOR
        + "Duration: 60 minutes"
        + EventTest.LINE_SEPARATOR
        + EventTest.LINE_SEPARATOR
        + "This is an example event I am making.", this.event.textDescription());
  }

  /**
   * Test get duration.
   */
  @Test
  void testGetDuration() {
    assertEquals(60, this.event.getDuration());
  }

  /**
   * Test get status.
   */
  @Test
  void testGetStatus() {
    assertEquals(NONE, this.event.getStatus());
  }
}
