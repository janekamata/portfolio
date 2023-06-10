package cs3500.pa02.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;
import java.util.function.Function;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for StudySession.
 */
class StudySessionTest {

  /**
   * The constant FILE_SEPARATOR.
   */
  private static final String FILE_SEPARATOR = File.separator;
  /**
   * The constant SAMPLE_INPUTS_DIRECTORY. Represents the directory to be used for testing.
   */
  private static final String SAMPLE_INPUTS_DIRECTORY =
      String.join(StudySessionTest.FILE_SEPARATOR, "src", "test", "resources");

  /**
   * The constant LINE_SEPARATOR.
   */
  private static final String LINE_SEPARATOR = "\r\n";
  /**
   * The User input.
   */
  private StringJoiner userInput;
  /**
   * The In content.
   */
  private ByteArrayInputStream inContent;
  /**
   * The Out content.
   */
  private ByteArrayOutputStream outContent;

  /**
   * Sets up.
   */
  @BeforeEach
  void setUp() {
    this.userInput = new StringJoiner(StudySessionTest.LINE_SEPARATOR);
    this.outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(this.outContent, true, StandardCharsets.UTF_8));
  }

  /**
   * Tear down.
   */
  @AfterEach
  void tearDown() {
    this.userInput = null;
    this.outContent.reset();
    assertNull(this.userInput);
    System.setOut(System.out);
    System.setIn(System.in);
  }

  /**
   * Test run for a full session changing difficulties.
   */
  @Test
  void testRunFull() {
    this.userInput.add("pizza hut");
    this.userInput.add("serious.sr");
    this.userInput.add(
        StudySessionTest.SAMPLE_INPUTS_DIRECTORY + StudySessionTest.FILE_SEPARATOR
            + "result.sr");
    this.userInput.add("-1");
    this.userInput.add("0");
    this.userInput.add("2");
    this.userInput.add("1");
    this.userInput.add("1");
    this.userInput.add("2");
    this.userInput.add("2");
    this.userInput.add("3");
    this.userInput.add("3");
    this.inContent = new ByteArrayInputStream(
        this.userInput.toString().getBytes(StandardCharsets.UTF_8));
    System.setIn(new BufferedInputStream(this.inContent));
    new StudySession(1).run();
    Function<String, String> removeQuestionAnswer = (String text) -> {
      int questionIndex = text.indexOf("Question:");
      int questionEnd = text.indexOf("Select", questionIndex);
      int answerIndex = text.indexOf("Answer:");
      int answerEnd = text.indexOf("---", answerIndex);
      int nextQuestionIndex = text.lastIndexOf("Question:");
      int nextQuestionEnd = text.indexOf("Select", nextQuestionIndex);
      int nextAnswerIndex = text.lastIndexOf("Answer:");
      int nextAnswerEnd = text.indexOf("---", nextAnswerIndex);
      return text.substring(0, questionIndex) + text.substring(questionEnd, answerIndex)
          + text.substring(answerEnd, nextQuestionIndex) + text.substring(nextQuestionEnd,
          nextAnswerIndex) + text.substring(nextAnswerEnd);
    };
    String processedOutText = removeQuestionAnswer.apply(this.outContent.toString());
    String expectedOutput = """

        -----------------------------------------
        Hello, welcome to your study session! Provide a SR question bank file path:\s"""
        + "Given path must be to a readable .sr file. Enter a new path: \n"
        + "Given path must be to a readable .sr file. Enter a new path: \n"
        + "How many questions would you like to practice today? "
        + "Given number of questions must be greater than 0. Enter a new number: \n"
        + "Given number of questions must be greater than 0. Enter a new number: \n"
        + "Starting session...\n"
        + '\n'
        + "-----------------------------------------\n"
        + "Question: When \"is where?\n"
        + '\n'
        + "Select an option (1 - Mark easy  |  2 - Mark hard"
        + "  |  3 - See answer  |  4 - Save and exit): "
        + "Card difficulty changed to easy.\n"
        + "Select an option (1 - Mark easy  |  2 - Mark hard"
        + "  |  3 - See answer  |  4 - Save and exit): "
        + "Card difficulty did not change. Difficulty was originally easy.\n"
        + "Select an option (1 - Mark easy  |  2 - Mark hard"
        + "  |  3 - See answer  |  4 - Save and exit): "
        + "Card difficulty changed to hard.\n"
        + "Select an option (1 - Mark easy  |  2 - Mark hard"
        + "  |  3 - See answer  |  4 - Save and exit): "
        + "Card difficulty did not change." + " Difficulty was originally hard.\n"
        + "Select an option (1 - Mark easy  |  2 - Mark hard"
        + "  |  3 - See answer  |  4 - Save and exit): "
        + '\n'
        + "Answer: Sometime\n"
        + "-----------------------------------------\n"
        + "Question: What is your favorite color?\n"
        + '\n'
        + "Select an option (1 - Mark easy  |  2 - Mark hard"
        + "  |  3 - See answer  |  4 - Save and exit): "
        + """

        Answer: Purple
        -----------------------------------------
        Good Job! You answered 2 question(s).

        1 question(s) went from easy to hard.
        1 question(s) went from hard to easy.

        Current Counts in Question Bank:
        2 Hard Question(s)
        2 Easy Question(s)
        -----------------------------------------
        """;
    String processedExpectedText = removeQuestionAnswer.apply(expectedOutput);
    assertEquals(processedExpectedText, processedOutText);
  }

  /**
   * Test run for exit and bad options input.
   */
  @Test
  void testRunExitAndBadInput() {
    this.userInput.add(StudySessionTest.SAMPLE_INPUTS_DIRECTORY + StudySessionTest.FILE_SEPARATOR
        + "result.sr");
    this.userInput.add("2");
    this.userInput.add("-1");
    this.userInput.add("0");
    this.userInput.add("10");
    this.userInput.add("4");
    this.inContent = new ByteArrayInputStream(
        this.userInput.toString().getBytes(StandardCharsets.UTF_8));
    System.setIn(new BufferedInputStream(this.inContent));
    new StudySession().run();
    Function<String, String> removeQuestionAnswer = (String text) -> {
      int questionIndex = text.indexOf("Question:");
      int questionEnd = text.indexOf("Select", questionIndex);
      return text.substring(0, questionIndex) + text.substring(questionEnd);
    };
    String processedOutText = removeQuestionAnswer.apply(this.outContent.toString());
    String expectedOutput = """

        -----------------------------------------
        Hello, welcome to your study session! Provide a SR question bank file path:\s"""
        + "How many questions would you like to practice today? "
        + "Starting session...\n"
        + '\n'
        + "-----------------------------------------\n"
        + "Question: When \"is where?\n"
        + '\n'
        + "Select an option (1 - Mark easy  |  2 - Mark hard"
        + "  |  3 - See answer  |  4 - Save and exit): "
        + "Given option number must be between 1-4. Enter a new number.\n"
        + "Select an option (1 - Mark easy  |  2 - Mark hard"
        + "  |  3 - See answer  |  4 - Save and exit): "
        + "Given option number must be between 1-4. Enter a new number.\n"
        + "Select an option (1 - Mark easy  |  2 - Mark hard"
        + "  |  3 - See answer  |  4 - Save and exit): "
        + "Given option number must be between 1-4. Enter a new number.\n"
        + "Select an option (1 - Mark easy  |  2 - Mark hard"
        + "  |  3 - See answer  |  4 - Save and exit): "
        + "Exiting session..."
        + """


        -----------------------------------------
        Good Job! You answered 1 question(s).

        0 question(s) went from easy to hard.
        0 question(s) went from hard to easy.

        Current Counts in Question Bank:
        2 Hard Question(s)
        2 Easy Question(s)
        -----------------------------------------
        """;
    String processedExpectedText = removeQuestionAnswer.apply(expectedOutput);
    assertEquals(processedExpectedText, processedOutText);
  }
}