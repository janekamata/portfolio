package cs3500.pa05.model.enums;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Represents tests for the comparator class enum.
 */
class ComparatorClassTest {

  /**
   * Test to string.
   */
  @Test
  void testToString() {
    assertEquals("By duration", ComparatorClass.COMPARE_BY_DURATION.toString());
    assertEquals("By name", ComparatorClass.COMPARE_BY_NAME.toString());
  }

}