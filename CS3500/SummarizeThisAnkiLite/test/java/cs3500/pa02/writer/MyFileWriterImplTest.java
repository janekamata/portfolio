package cs3500.pa02.writer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import cs3500.pa02.generatestudy.Important;
import cs3500.pa02.generatestudy.StudyGuide;
import cs3500.pa02.studysession.Card;
import cs3500.pa02.studysession.CardDifficulty;
import cs3500.pa02.studysession.StudySet;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the class MyFileWriter.
 */
class MyFileWriterImplTest {

  private static final MyFileWriter<StudyGuide> GUIDE_WRITER = new MyFileWriterImpl<>();
  private static final MyFileWriter<StudySet> SET_WRITER = new MyFileWriterImpl<>();
  private static final String FILE_SEPARATOR = File.separator;
  private StudySet nonemptyStudySetSr;
  private List<Card> cardsSr;
  private File tempFileMd;
  private File tempFileSr;
  private StudyGuide emptyStudyGuide;
  private StudyGuide test1StudyGuide;

  /**
   * Sets up. Initializes the fields before each test.
   */
  @BeforeEach
  void setUp() {
    try {
      this.tempFileMd = File.createTempFile("temp1", ".md");
      this.tempFileSr = File.createTempFile("temp1", ".sr");
      this.emptyStudyGuide = new StudyGuide();
      this.test1StudyGuide = new StudyGuide();
      this.cardsSr = new ArrayList<>();
      this.cardsSr.add(new Card("What is your favorite color?", "Purple", CardDifficulty.HARD));
      this.cardsSr.add(new Card("Who is the president?", "Jo\\nhn", CardDifficulty.EASY));
      this.cardsSr.add(new Card("How are you to;day?", "Meh", CardDifficulty.EASY));
      this.cardsSr.add(new Card("When \"is where?", "Sometime"));
      this.nonemptyStudySetSr = new StudySet();
      this.nonemptyStudySetSr.updateCards(this.cardsSr);
    } catch (IOException e) {
      fail();
    }
  }

  /**
   * Tear down. Resets the fields before each test.
   */
  @AfterEach
  void tearDown() {
    this.emptyStudyGuide = null;
    this.test1StudyGuide = null;
    this.tempFileMd = null;
    this.tempFileSr = null;
    this.nonemptyStudySetSr = null;
    this.cardsSr = null;
    assertNull(this.emptyStudyGuide);
    assertNull(this.test1StudyGuide);
    assertNull(this.tempFileMd);
    assertNull(this.tempFileSr);
    assertNull(this.nonemptyStudySetSr);
    assertNull(this.cardsSr);
  }

  /**
   * Test write study guide to file.
   */
  @Test
  void testWriteToFileStudyGuide() {
    Path samplePath = Path.of(
        String.join(MyFileWriterImplTest.FILE_SEPARATOR, "src", "test", "resources", "result.md"));
    try {
      assertThrows(IllegalArgumentException.class,
          () -> MyFileWriterImplTest.GUIDE_WRITER.writeToFile(Path.of("pizza.txt"),
              new StudyGuide()));
      assertThrows(RuntimeException.class,
          () -> MyFileWriterImplTest.GUIDE_WRITER.writeToFile(Path.of("pizza"),
              new StudyGuide()));
      assertThrows(RuntimeException.class,
          () -> MyFileWriterImplTest.GUIDE_WRITER.writeToFile(Path.of("?"),
              new StudyGuide()));
      MyFileWriterImplTest.GUIDE_WRITER.writeToFile(samplePath,
          this.test1StudyGuide);
      MyFileWriterImplTest.GUIDE_WRITER.writeToFile(Path.of(this.tempFileMd.toURI()),
          this.test1StudyGuide);
      long mismatch = Files.mismatch(samplePath, Path.of(
          this.tempFileMd.toURI()));
      assertEquals(-1, mismatch);
      if (!this.tempFileMd.setWritable(false)) {
        fail();
      }
      assertTrue(this.tempFileMd.setWritable(false));
      assertThrows(IllegalArgumentException.class,
          () -> MyFileWriterImplTest.GUIDE_WRITER.writeToFile(
              Path.of(this.tempFileMd.toURI()),
              this.test1StudyGuide));
      assertThrows(IllegalArgumentException.class,
          () -> MyFileWriterImplTest.GUIDE_WRITER.writeToFile(
              Path.of(this.tempFileSr.toURI()),
              this.test1StudyGuide));
    } catch (IOException e) {
      fail();
    }
  }

  /**
   * Test write study set to file.
   */
  @Test
  void testWriteToFileStudySet() {
    Path samplePath = Path.of(
        String.join(MyFileWriterImplTest.FILE_SEPARATOR, "src", "test", "resources", "result.sr"));
    try {
      assertThrows(IllegalArgumentException.class,
          () -> MyFileWriterImplTest.SET_WRITER.writeToFile(Path.of("pizza.txt"),
              new StudySet()));
      assertThrows(RuntimeException.class,
          () -> MyFileWriterImplTest.SET_WRITER.writeToFile(Path.of("pizza"),
              new StudySet()));
      assertThrows(RuntimeException.class,
          () -> MyFileWriterImplTest.SET_WRITER.writeToFile(Path.of("?"),
              new StudySet()));
      MyFileWriterImplTest.SET_WRITER.writeToFile(samplePath, this.nonemptyStudySetSr);
      MyFileWriterImplTest.SET_WRITER.writeToFile(Path.of(this.tempFileSr.toURI()),
          this.nonemptyStudySetSr);
      long mismatch = Files.mismatch(samplePath, Path.of(this.tempFileSr.toURI()));
      assertEquals(-1, mismatch);
      if (!this.tempFileSr.setWritable(false)) {
        fail();
      }
      assertThrows(IllegalArgumentException.class,
          () -> MyFileWriterImplTest.SET_WRITER.writeToFile(
              Path.of(this.tempFileSr.toURI()),
              this.nonemptyStudySetSr));
      assertThrows(IllegalArgumentException.class,
          () -> MyFileWriterImplTest.SET_WRITER.writeToFile(
              Path.of(this.tempFileMd.toURI()),
              this.nonemptyStudySetSr));
      assertThrows(IllegalArgumentException.class,
          () -> new MyFileWriterImpl<Important>().writeToFile(samplePath, new Important("")));
    } catch (IOException e) {
      fail();
    }
  }
}