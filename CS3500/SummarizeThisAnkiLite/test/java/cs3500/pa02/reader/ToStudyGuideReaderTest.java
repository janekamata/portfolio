package cs3500.pa02.reader;

import cs3500.pa02.generatestudy.Heading;
import cs3500.pa02.generatestudy.Important;
import cs3500.pa02.generatestudy.StudyGuide;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the MyFileReader class.
 */
class ToStudyGuideReaderTest {

  /**
   * The constant FILE_SEPARATOR.
   */
  private static final String FILE_SEPARATOR = File.separator;
  /**
   * The constant SAMPLE_INPUTS_DIRECTORY. Represents the directory to be used for testing.
   */
  private static final String SAMPLE_INPUTS_DIRECTORY =
      String.join(ToStudyGuideReaderTest.FILE_SEPARATOR, "src", "test", "resources", "sampleFiles");
  /**
   * The constant FILE_READER.
   */
  private static final MyFileReader<StudyGuide> FILE_READER = new ToStudyGuideReader();
  /**
   * The Empty study guide.
   */
  private StudyGuide emptyStudyGuide;
  /**
   * The Test 1 study guide.
   */
  private StudyGuide test1StudyGuide;
  /**
   * The Test 1 headings.
   */
  private List<Heading> test1Headings;
  /**
   * The Test 1 important.
   */
  private List<Important> test1Important;

  /**
   * Sets up. Initializes the fields before each test.
   */
  @BeforeEach
  void setUp() {
    this.emptyStudyGuide = new StudyGuide();
    this.test1Important = new ArrayList<>();
    this.test1Important.add(new Important("world"));
    this.test1Headings = new ArrayList<>();
    this.test1Headings.add(new Heading("## Heading", this.test1Important));
    this.test1Headings.add(new Heading("### Hello", new ArrayList<>()));
    this.test1StudyGuide = new StudyGuide(this.test1Headings);
  }

  /**
   * Tear down. Resets the fields before each test.
   */
  @AfterEach
  void tearDown() {
    this.emptyStudyGuide = null;
    this.test1Important = null;
    this.test1Headings = null;
    this.test1StudyGuide = null;
    Assertions.assertNull(this.emptyStudyGuide);
    Assertions.assertNull(this.test1Important);
    Assertions.assertNull(this.test1Headings);
    Assertions.assertNull(this.test1StudyGuide);
  }

  /**
   * Test MyFileReader initialization.
   */
  @Test
  void testMyFileReader() {
    Assertions.assertSame(ToStudyGuideReader.class, ToStudyGuideReaderTest.FILE_READER.getClass());
  }

  /**
   * Test for readFileToStudyGuide
   */
  @Test
  void testReadFileToStudyGuide() {
    ToStudyGuideReaderTest.FILE_READER.readFileTo(
        new File(
            ToStudyGuideReaderTest.SAMPLE_INPUTS_DIRECTORY + ToStudyGuideReaderTest.FILE_SEPARATOR
                + "test1.md"), this.emptyStudyGuide);
    Assertions.assertEquals(this.test1StudyGuide, this.emptyStudyGuide);
    Assertions.assertThrows(RuntimeException.class,
        () -> ToStudyGuideReaderTest.FILE_READER.readFileTo(new File("pizza.md"),
            this.emptyStudyGuide));
  }
}