package cs3500.pa05.model.enums;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Represents tests for the completion status enum.
 */
class CompletionStatusTest {

  /**
   * Test to string.
   */
  @Test
  void testToString() {
    assertEquals("Complete", CompletionStatus.COMPLETE.toString());
    assertEquals("Incomplete", CompletionStatus.INCOMPLETE.toString());
  }
}