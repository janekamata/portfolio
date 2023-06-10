package cs3500.pa02.controller;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for GenerateStudy.
 */
class GenerateStudyTest {

  /**
   * The constant FILE_SEPARATOR.
   */
  private static final String FILE_SEPARATOR = File.separator;
  /**
   * The constant SAMPLE_INPUTS_DIRECTORY. Represents the directory to be used for testing.
   */
  private static final String SAMPLE_RESULTS_DIRECTORY =
      String.join(GenerateStudyTest.FILE_SEPARATOR, "src", "test", "resources", "sampleResults");
  /**
   * The Temp file 1.
   */
  private File tempFile1;

  /**
   * Sets up.
   */
  @BeforeEach
  void setUp() {
    try {
      this.tempFile1 = File.createTempFile("temp1", ".md");
    } catch (IOException e) {
      fail();
    }
  }

  /**
   * Tear down.
   */
  @AfterEach
  void tearDown() {
    this.tempFile1 = null;
    assertNull(this.tempFile1);
  }

  /**
   * Test for run.
   */
  @Test
  void testRun() {

    assertThrows(RuntimeException.class,
        () -> new GenerateStudy(new String[]{"", "filename", "1"}).run());
    //          new GenerateStudy(new String[]{GenerateStudyTest.SAMPLE_RESULTS_DIRECTORY,
    //          "filename",
    //              this.tempFile1.getAbsolutePath()}).run();
    //          new GenerateStudy(new String[]{GenerateStudyTest.SAMPLE_RESULTS_DIRECTORY,
    //          "filename",
    //              GenerateStudyTest.SAMPLE_RESULTS_DIRECTORY + GenerateStudyTest.FILE_SEPARATOR
    //                  + "resultFilename.md"}).run();
    //          long mismatch = Files.mismatch(
    //              Path.of(GenerateStudyTest.SAMPLE_RESULTS_DIRECTORY
    //                  + GenerateStudyTest.FILE_SEPARATOR
    //                  + "resultFilename.md"),
    //              Path.of(this.tempFile1.toURI()));
    //          assertEquals(-1, mismatch);
    //          new GenerateStudy(new String[]{GenerateStudyTest.SAMPLE_RESULTS_DIRECTORY,
    //          "created",
    //              this.tempFile1.getAbsolutePath()}).run();
    //          new GenerateStudy(new String[]{GenerateStudyTest.SAMPLE_RESULTS_DIRECTORY,
    //          "created",
    //              GenerateStudyTest.SAMPLE_RESULTS_DIRECTORY + GenerateStudyTest.FILE_SEPARATOR
    //                  + "resultCreated.md"}).run();
    //          mismatch = Files.mismatch(
    //              Path.of(GenerateStudyTest.SAMPLE_RESULTS_DIRECTORY
    //                  + GenerateStudyTest.FILE_SEPARATOR
    //                  + "resultCreated.md"),
    //              Path.of(this.tempFile1.toURI()));
    //          assertEquals(-1, mismatch);
    //          new GenerateStudy(new String[]{GenerateStudyTest.SAMPLE_RESULTS_DIRECTORY,
    //          "modified",
    //              this.tempFile1.getAbsolutePath()}).run();
    //          new GenerateStudy(new String[]{GenerateStudyTest.SAMPLE_RESULTS_DIRECTORY,
    //          "modified",
    //              GenerateStudyTest.SAMPLE_RESULTS_DIRECTORY + GenerateStudyTest.FILE_SEPARATOR
    //                  + "resultModified.md"}).run();
    //          mismatch = Files.mismatch(
    //              Path.of(GenerateStudyTest.SAMPLE_RESULTS_DIRECTORY
    //                  + GenerateStudyTest.FILE_SEPARATOR
    //                  + "resultModified.md"),
    //              Path.of(this.tempFile1.toURI()));
    //          assertEquals(-1, mismatch);

  }
}