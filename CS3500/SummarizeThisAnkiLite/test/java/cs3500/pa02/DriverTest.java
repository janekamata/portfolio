package cs3500.pa02;

import static cs3500.pa02.Driver.main;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for Driver.
 */
class DriverTest {

  /**
   * The constant FILE_SEPARATOR.
   */
  private static final String FILE_SEPARATOR = File.separator;
  /**
   * The constant SAMPLE_INPUTS_DIRECTORY. Represents the directory to be used for testing.
   */
  private static final String SAMPLE_INPUTS_DIRECTORY =
      String.join(DriverTest.FILE_SEPARATOR, "src", "test", "resources");

  /**
   * The constant LINE_SEPARATOR.
   */
  private static final String LINE_SEPARATOR = "\r\n";
  /**
   * The User input.
   */
  private StringJoiner userInput;
  /**
   * The Out content.
   */
  private ByteArrayOutputStream outContent;

  /**
   * Sets up.
   */
  @BeforeEach
  void setUp() {
    this.userInput = new StringJoiner(DriverTest.LINE_SEPARATOR);
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
   * Test Driver initialization.
   */
  @Test
  void testDriver() {
    Driver driver = new Driver();
    assertSame(Driver.class, driver.getClass());
  }

  /**
   * Test main exceptions.
   */
  @Test
  void testMainExceptions() {
    assertThrows(IllegalArgumentException.class, () -> main(new String[]{"", " 1", "1", "1"}));
    assertThrows(IllegalArgumentException.class, () -> main(new String[]{"", "filename"}));
  }

  /**
   * Test main study session.
   */
  @Test
  void testMainStudySession() {
    this.userInput.add(
        DriverTest.SAMPLE_INPUTS_DIRECTORY + DriverTest.FILE_SEPARATOR
            + "result.sr");
    this.userInput.add("serious.sr");
    ByteArrayInputStream inContent = new ByteArrayInputStream(
        this.userInput.toString().getBytes(StandardCharsets.UTF_8));
    System.setIn(new BufferedInputStream(inContent));
    assertThrows(RuntimeException.class, () -> main(new String[]{}));
  }

  /**
   * Test main generate study.
   */
  @Test
  void testMainGenerateStudy() {
    assertThrows(RuntimeException.class,
        () -> main(new String[]{"sampleFiles", "pizza", "resuld.md"}));
  }
}