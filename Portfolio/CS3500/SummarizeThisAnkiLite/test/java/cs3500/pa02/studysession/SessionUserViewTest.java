package cs3500.pa02.studysession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa02.mockmodels.MockAppendable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the SessionUserView class.
 */
class SessionUserViewTest {

  /**
   * The constant OUT.
   */
  private static final StringBuilder OUT = new StringBuilder();
  /**
   * The constant MOCK_OUT.
   */
  private static final MockAppendable MOCK_OUT = new MockAppendable();
  /**
   * The constant SESSION_USER_VIEW.
   */
  private static final UserView SESSION_USER_VIEW = new SessionUserView(SessionUserViewTest.OUT);
  /**
   * The constant MOCK_SESSION_USER_VIEW.
   */
  private static final UserView MOCK_SESSION_USER_VIEW = new SessionUserView(
      SessionUserViewTest.MOCK_OUT);

  /**
   * Sets up.
   */
  @BeforeEach
  void setUp() {
    assertTrue(SessionUserViewTest.OUT.isEmpty());
  }

  /**
   * Tear down.
   */
  @AfterEach
  void tearDown() {
    SessionUserViewTest.OUT.setLength(0);
    assertTrue(SessionUserViewTest.OUT.isEmpty());
  }

  /**
   * Test show setup.
   */
  @Test
  void testShowSetup() {
    SessionUserViewTest.SESSION_USER_VIEW.showSetup();
    StringBuilder result = new StringBuilder();
    result.append("""
          
        -----------------------------------------
        Hello, welcome to your study session! Provide a SR question bank file path:\s""");
    assertEquals(SessionUserViewTest.OUT.toString(), result.toString());
    SessionUserViewTest.SESSION_USER_VIEW.showSetup();
    result.append("How many questions would you like to practice today? ");
    assertEquals(SessionUserViewTest.OUT.toString(), result.toString());
    SessionUserViewTest.SESSION_USER_VIEW.showSetup();
    assertEquals(SessionUserViewTest.OUT.toString(), result.toString());
    assertThrows(RuntimeException.class,
        SessionUserViewTest.MOCK_SESSION_USER_VIEW::showSetup);
  }

  /**
   * Test show question.
   */
  @Test
  void testShowQuestion() {
    SessionUserViewTest.SESSION_USER_VIEW.showQuestion("This is a question?");
    String result = """

        -----------------------------------------
        Question: This is a question?

        """;
    assertEquals(SessionUserViewTest.OUT.toString(), result);
    assertThrows(RuntimeException.class,
        () -> SessionUserViewTest.MOCK_SESSION_USER_VIEW.showQuestion("Test"));
  }

  /**
   * Test show answer.
   */
  @Test
  void testShowAnswer() {
    SessionUserViewTest.SESSION_USER_VIEW.showAnswer("This is an answer.");
    String result = "\nAnswer: This is an answer.";
    assertEquals(SessionUserViewTest.OUT.toString(), result);
    assertThrows(RuntimeException.class,
        () -> SessionUserViewTest.MOCK_SESSION_USER_VIEW.showAnswer("Test"));
  }

  /**
   * Test show options.
   */
  @Test
  void testShowOptions() {
    SessionUserViewTest.SESSION_USER_VIEW.showOptions();
    String result = "Select an option"
        + " (1 - Mark easy  |  2 - Mark hard  |  3 - See answer  |  4 - Save and exit): ";
    assertEquals(SessionUserViewTest.OUT.toString(), result);
    assertThrows(RuntimeException.class,
        SessionUserViewTest.MOCK_SESSION_USER_VIEW::showOptions);
  }

  /**
   * Test show stats.
   */
  @Test
  void testShowStats() {
    String stats = """
        Good Job! You answered 0 question(s).

        1 question(s) went from easy to hard.
        1 question(s) went from hard to easy.

        Current Counts in Question Bank:
        5 Hard Question(s)
        5 Easy Question(s)""";
    SessionUserViewTest.SESSION_USER_VIEW.showStats(stats);
    String result = "\n-----------------------------------------\n" + stats
        + "\n-----------------------------------------\n";
    assertEquals(SessionUserViewTest.OUT.toString(), result);
    assertThrows(RuntimeException.class,
        () -> SessionUserViewTest.MOCK_SESSION_USER_VIEW.showStats(stats));
  }

  /**
   * Test show message.
   */
  @Test
  void testShowMessage() {
    SessionUserViewTest.SESSION_USER_VIEW.showMessage("This is a message.");
    String result = "This is a message.\n";
    assertEquals(SessionUserViewTest.OUT.toString(), result);
    assertThrows(RuntimeException.class,
        () -> SessionUserViewTest.MOCK_SESSION_USER_VIEW.showMessage("Test"));
  }
}

