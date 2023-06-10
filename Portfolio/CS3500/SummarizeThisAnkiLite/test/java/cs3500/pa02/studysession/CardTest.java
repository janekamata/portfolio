package cs3500.pa02.studysession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the Card class.
 */
class CardTest {

  /**
   * The Dummy card easy.
   */
  private Card dummyCardEasy;
  /**
   * The Dummy card hard.
   */
  private Card dummyCardHard;

  /**
   * Sets up before each test.
   */
  @BeforeEach
  void setUp() {
    this.dummyCardEasy = new Card("Easy Question", "Easy Answer", CardDifficulty.EASY);
    this.dummyCardHard = new Card("Hard Question", "Hard Answer");
    assertEquals("Easy Question", this.dummyCardEasy.getQuestion());
    assertEquals("Hard Question", this.dummyCardHard.getQuestion());
    assertEquals("Easy Answer", this.dummyCardEasy.getAnswer());
    assertEquals("Hard Answer", this.dummyCardHard.getAnswer());
    assertEquals(CardDifficulty.EASY, this.dummyCardEasy.getDifficulty());
    assertEquals(CardDifficulty.HARD, this.dummyCardHard.getDifficulty());
  }

  /**
   * Tear down after each test.
   */
  @AfterEach
  void tearDown() {
    this.dummyCardEasy = null;
    this.dummyCardHard = null;
    assertNull(this.dummyCardEasy);
    assertNull(this.dummyCardHard);
  }

  /**
   * Test toString.
   */
  @Test
  void testToString() {
    assertEquals("- Easy Question ;;; Easy Answer", this.dummyCardEasy.toString());
    assertEquals("* Hard Question ;;; Hard Answer", this.dummyCardHard.toString());
  }

  /**
   * Test getQuestion.
   */
  @Test
  void testGetQuestion() {
    assertEquals("Easy Question", this.dummyCardEasy.getQuestion());
    assertEquals("Hard Question", this.dummyCardHard.getQuestion());
  }

  /**
   * Test getAnswer.
   */
  @Test
  void testGetAnswer() {
    assertEquals("Easy Answer", this.dummyCardEasy.getAnswer());
    assertEquals("Hard Answer", this.dummyCardHard.getAnswer());
  }

  /**
   * Test get difficulty.
   */
  @Test
  void testGetDifficulty() {
    assertEquals(CardDifficulty.HARD, this.dummyCardHard.getDifficulty());
    assertEquals(CardDifficulty.EASY, this.dummyCardEasy.getDifficulty());
  }

  /**
   * Test set difficulty.
   */
  @Test
  void testSetDifficulty() {
    this.dummyCardHard.setDifficulty(CardDifficulty.EASY);
    assertEquals(CardDifficulty.EASY, this.dummyCardHard.getDifficulty());
    this.dummyCardEasy.setDifficulty(CardDifficulty.HARD);
    assertEquals(CardDifficulty.HARD, this.dummyCardEasy.getDifficulty());
  }
}