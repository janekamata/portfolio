package cs3500.pa02.studysession;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for SessionOptions enum.
 */
class SessionOptionsTest {

  /**
   * Sets up.
   */
  @BeforeEach
  void setUp() {
  }

  /**
   * Tear down.
   */
  @AfterEach
  void tearDown() {
  }

  /**
   * Test toString.
   */
  @Test
  void testToString() {
    assertEquals("Mark easy", SessionOptions.MARK_EASY.toString());
    assertEquals("Mark hard", SessionOptions.MARK_HARD.toString());
    assertEquals("See answer", SessionOptions.SEE_ANSWER.toString());
    assertEquals("Save and exit session", SessionOptions.SAVE_AND_EXIT_SESSION.toString());

  }
}