package cs3500.pa05.model;

import static cs3500.pa05.model.enums.DayType.SATURDAY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

/**
 * Represents tests for time.
 */
class TimeTest {

  /**
   * Test to sting 1010.
   */
  @Test
  void testToSting1010() {
    Record time = new Time(10, 10);
    assertEquals("10:10", time.toString());
  }

  /**
   * Test to sting 99.
   */
  @Test
  void testToSting99() {
    Time time = new Time(9, 9);
    assertEquals("09:09", time.toString());
  }

  /**
   * Test equals.
   */
  @Test
  void testEquals() {
    assertNotEquals(new Time(1, 1), null);
    assertEquals(new Time(1, 1), new Time(1, 1));
  }

  /**
   * Test equals values.
   */
  @Test
  void testEqualsValues() {
    assertNotEquals(new Time(1, 1), new Time(0, 0));
  }

  /**
   * Test equals values 2.
   */
  @Test
  void testEqualsValues2() {
    assertNotEquals(new Time(1, 1), new Time(1, 0));
  }

  /**
   * Test equals type.
   */
  @Test
  void testEqualsType() {
    assertNotEquals(new Time(1, 1), SATURDAY);
  }

  /**
   * Test hour.
   */
  @Test
  void testHour() {
    assertEquals(1, new Time(1, 1).hour());
  }

  /**
   * Test minutes.
   */
  @Test
  void testMinutes() {
    assertEquals(1, new Time(1, 1).minutes());
  }
}
