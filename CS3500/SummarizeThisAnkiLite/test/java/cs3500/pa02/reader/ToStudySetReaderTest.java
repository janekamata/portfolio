package cs3500.pa02.reader;

import cs3500.pa02.studysession.Card;
import cs3500.pa02.studysession.CardDifficulty;
import cs3500.pa02.studysession.StudySet;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for ToStudySetReader.
 */
class ToStudySetReaderTest {

  /**
   * The constant toStudyGuide.
   */
  private static final MyFileReader<StudySet> FILE_READER = new ToStudySetReader();
  /**
   * The constant FILE_SEPARATOR.
   */
  private static final String FILE_SEPARATOR = File.separator;
  /**
   * The constant SAMPLE_INPUTS_DIRECTORY. Represents the directory to be used for testing.
   */
  private static final String SAMPLE_INPUTS_DIRECTORY =
      String.join(ToStudySetReaderTest.FILE_SEPARATOR, "src", "test", "resources", "sampleFiles");
  /**
   * The Empty study set.
   */
  private StudySet emptyStudySet;
  /**
   * The Nonempty study set md.
   */
  private StudySet nonemptyStudySetMarkdown;
  /**
   * The Nonempty study set sr.
   */
  private StudySet nonemptyStudySetSr;
  /**
   * The Cards md.
   */
  private List<Card> cardsMarkdown;
  /**
   * The Cards sr.
   */
  private List<Card> cardsSr;

  /**
   * Sets up.
   */
  @BeforeEach
  void setUp() {
    this.emptyStudySet = new StudySet();
    this.cardsMarkdown = new ArrayList<>();
    this.cardsMarkdown.add(new Card("Question 1", "Answer 1"));
    this.cardsMarkdown.add(new Card("Question 2", "Answer 2"));
    this.cardsSr = new ArrayList<>();
    this.cardsSr.add(new Card("What is your favorite color?", "Purple", CardDifficulty.HARD));
    this.cardsSr.add(new Card("Who is the president?", "Jo\\nhn", CardDifficulty.EASY));
    this.cardsSr.add(new Card("How are you to;day?", "Meh", CardDifficulty.EASY));
    this.cardsSr.add(new Card("When \"is where?", "Sometime"));
    this.nonemptyStudySetMarkdown = new StudySet();
    this.nonemptyStudySetMarkdown.updateCards(this.cardsMarkdown);
    this.nonemptyStudySetSr = new StudySet();
    this.nonemptyStudySetSr.updateCards(this.cardsSr);
  }

  /**
   * Tear down.
   */
  @AfterEach
  void tearDown() {
    this.emptyStudySet = null;
    this.cardsMarkdown = null;
    this.nonemptyStudySetMarkdown = null;
    this.cardsSr = null;
    this.nonemptyStudySetSr = null;
    Assertions.assertNull(this.emptyStudySet);
    Assertions.assertNull(this.cardsMarkdown);
    Assertions.assertNull(this.nonemptyStudySetMarkdown);
    Assertions.assertNull(this.cardsSr);
    Assertions.assertNull(this.nonemptyStudySetSr);
  }

  /**
   * Test MyFileReader initialization.
   */
  @Test
  void testMyFileReader() {
    Assertions.assertSame(ToStudySetReader.class, ToStudySetReaderTest.FILE_READER.getClass());
  }

  /**
   * Test readFileTo.
   */
  @Test
  void testReadFileTo() {
    ToStudySetReaderTest.FILE_READER.readFileTo(new File(
            ToStudySetReaderTest.SAMPLE_INPUTS_DIRECTORY + ToStudySetReaderTest.FILE_SEPARATOR
                + "test1.md"),
        this.emptyStudySet);
    Assertions.assertEquals(this.nonemptyStudySetMarkdown, this.emptyStudySet);
    this.emptyStudySet = new StudySet();
    ToStudySetReaderTest.FILE_READER.readFileTo(new File(
            ToStudySetReaderTest.SAMPLE_INPUTS_DIRECTORY + ToStudySetReaderTest.FILE_SEPARATOR
                + "test1.sr"),
        this.emptyStudySet);
    Assertions.assertEquals(this.nonemptyStudySetSr, this.emptyStudySet);
    Assertions.assertThrows(RuntimeException.class,
        () -> ToStudySetReaderTest.FILE_READER.readFileTo(new File("pizza.md"),
            this.emptyStudySet));
  }
}