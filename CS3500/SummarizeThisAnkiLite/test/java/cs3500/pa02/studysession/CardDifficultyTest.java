package cs3500.pa02.studysession;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the CardDifficulty enum.
 */
class CardDifficultyTest {

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
    assertEquals("-", CardDifficulty.EASY.toString());
    assertEquals("*", CardDifficulty.HARD.toString());
  }
}