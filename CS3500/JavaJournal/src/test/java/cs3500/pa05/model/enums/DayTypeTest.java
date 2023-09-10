package cs3500.pa05.model.enums;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Represents tests for the day type enum.
 */
class DayTypeTest {

  /**
   * Test to string.
   */
  @Test
  void testToString() {
    assertEquals("Monday", DayType.MONDAY.toString());
    assertEquals("Tuesday", DayType.TUESDAY.toString());
    assertEquals("Wednesday", DayType.WEDNESDAY.toString());
    assertEquals("Thursday", DayType.THURSDAY.toString());
    assertEquals("Friday", DayType.FRIDAY.toString());
    assertEquals("Saturday", DayType.SATURDAY.toString());
    assertEquals("Sunday", DayType.SUNDAY.toString());
  }
}