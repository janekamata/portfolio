package cs3500.pa05.model;

import static cs3500.pa05.model.enums.DayType.FRIDAY;
import static cs3500.pa05.model.enums.DayType.SUNDAY;
import static cs3500.pa05.model.enums.DayType.THURSDAY;
import static cs3500.pa05.model.enums.DayType.TUESDAY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Represents tests for abstract calendar entry.
 */
class AbstractCalendarEntryTest {

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
   * Test set description.
   */
  @Test
  void testSetDescription() {
    assertEquals(this.event, this.oldEvent);
    this.event.setDescription("Pizza");
    assertEquals(new Event("Example Event", "Pizza", THURSDAY,
        new Time(10, 10), 60), this.event);
  }

  /**
   * Test set day type.
   */
  @Test
  void testSetDayType() {
    assertEquals(this.event, this.oldEvent);
    this.event.setDayType(FRIDAY);
    assertEquals(new Event("Example Event", "This is an example event I am making.", FRIDAY,
            new Time(10, 10), 60),
        this.event);
  }

  /**
   * Test set entry name.
   */
  @Test
  void testSetEntryName() {
    assertEquals(this.event, this.oldEvent);
    this.event.setEntryName("Bologna");
    assertEquals(new Event("Bologna", "This is an example event I am making.", THURSDAY,
            new Time(10, 10), 60),
        this.event);
  }

  /**
   * Test unequals.
   */
  @Test
  void testUnequals() {
    assertNotEquals(TUESDAY, this.event);
    assertNotEquals(this.event, null);
    assertNotEquals(this.event, new Event("no", "bad", SUNDAY, new Time(1, 1), 9));
    assertNotEquals(this.event, new Event("Example Event", "bad", SUNDAY, new Time(1, 1), 9));
    assertNotEquals(this.event,
        new Event("Example Event", "This is an example event I am making.", SUNDAY,
            new Time(1, 1),
            9));
  }
}
