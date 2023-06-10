package cs3500.pa02.studysession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Objects;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Represents tests for the SessionStats clas.
 */
class SessionStatsTest {

  /**
   * The Session stats.
   */
  private SessionStats sessionStats;

  /**
   * Sets up.
   */
  @BeforeEach
  void setUp() {
    this.sessionStats = new SessionStats(5, 5);
  }

  /**
   * Tear down.
   */
  @AfterEach
  void tearDown() {
    this.sessionStats = null;
    assertNull(this.sessionStats);
  }


  /**
   * Test increment answered.
   */
  @Test
  void testIncrementAnswered() {
    this.sessionStats.incrementAnswered();
    String result = """
        Good Job! You answered 1 question(s).

        0 question(s) went from easy to hard.
        0 question(s) went from hard to easy.

        Current Counts in Question Bank:
        5 Hard Question(s)
        5 Easy Question(s)""";
    assertEquals(result, this.sessionStats.toString());
  }

  /**
   * Test to string.
   */
  @Test
  void testToString() {
    String result = """
        Good Job! You answered 0 question(s).

        0 question(s) went from easy to hard.
        0 question(s) went from hard to easy.

        Current Counts in Question Bank:
        5 Hard Question(s)
        5 Easy Question(s)""";
    assertEquals(result, this.sessionStats.toString());
  }

  /**
   * Test change difficulty.
   */
  @Test
  void testChangeDifficulty() {
    String result = "Card difficulty did not change. Difficulty was originally easy.";
    assertEquals(result,
        this.sessionStats.changeDifficulty(CardDifficulty.EASY, CardDifficulty.EASY));
    String stats = """
        Good Job! You answered 0 question(s).

        0 question(s) went from easy to hard.
        0 question(s) went from hard to easy.

        Current Counts in Question Bank:
        5 Hard Question(s)
        5 Easy Question(s)""";
    assertEquals(stats, this.sessionStats.toString());
    result = "Card difficulty changed to easy.";
    assertEquals(result,
        this.sessionStats.changeDifficulty(CardDifficulty.HARD, CardDifficulty.EASY));
    stats = """
        Good Job! You answered 0 question(s).

        0 question(s) went from easy to hard.
        1 question(s) went from hard to easy.

        Current Counts in Question Bank:
        4 Hard Question(s)
        6 Easy Question(s)""";
    assertEquals(stats, this.sessionStats.toString());
    result = "Card difficulty changed to hard.";
    assertEquals(result,
        this.sessionStats.changeDifficulty(CardDifficulty.EASY, CardDifficulty.HARD));
    stats = """
        Good Job! You answered 0 question(s).

        1 question(s) went from easy to hard.
        1 question(s) went from hard to easy.

        Current Counts in Question Bank:
        5 Hard Question(s)
        5 Easy Question(s)""";
    assertEquals(stats, this.sessionStats.toString());
  }


  /**
   * Test equals.
   */
  @Test
  void testEquals() {
    assertNotEquals(new SessionStats(0, 0), this.sessionStats);
    assertEquals(new SessionStats(5, 5), this.sessionStats);
    assertNotEquals(null, this.sessionStats);
    assertNotEquals(this.sessionStats, null);
    assertNotEquals(CardDifficulty.EASY, this.sessionStats);
    assertNotEquals(this.sessionStats, CardDifficulty.EASY);
  }

  /**
   * Test hash code.
   */
  @Test
  void testHashCode() {
    assertEquals(Objects.hash(0, 0, 0, 5, 5),
        this.sessionStats.hashCode());
    assertEquals(Objects.hash(0, 0, 0, 0, 0),
        new SessionStats(0, 0).hashCode());
  }
}