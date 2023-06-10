package cs3500.pa02.generatestudy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the MarkDownFileVisitor class.
 */
class MarkDownFileVisitorTest {

  /**
   * The constant FILE_SEPARATOR.
   */
  private static final String FILE_SEPARATOR = File.separator;
  /**
   * The constant SAMPLE_INPUTS_DIRECTORY. Represents the directory to be used for testing.
   */
  private static final String SAMPLE_INPUTS_DIRECTORY =
      String.join(MarkDownFileVisitorTest.FILE_SEPARATOR, "src", "test", "resources",
          "sampleFiles");

  /**
   * The constant MY_MD_VISITOR.
   */
  private static final MarkDownFileVisitor MY_MD_VISITOR = new MarkDownFileVisitor();
  /**
   * The Original err print stream.
   */
  private final PrintStream originalErr = System.err;
  /**
   * The Err content output stream.
   */
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
  /**
   * The Pizza path.
   */
  private Path pizzaPath;
  /**
   * The Test 1 path.
   */
  private Path test1Path;
  /**
   * The Test 4 path.
   */
  private Path test4Path;
  /**
   * The Temp files 1.
   */
  private File tempFiles1;

  /**
   * Sets up. Initializes the fields before each test.
   */
  @BeforeEach
  void setUp() {
    this.pizzaPath = Path.of("pizza");
    this.test1Path = Path.of(
        MarkDownFileVisitorTest.SAMPLE_INPUTS_DIRECTORY + MarkDownFileVisitorTest.FILE_SEPARATOR
            + "test1.md");
    this.test4Path = Path.of(
        MarkDownFileVisitorTest.SAMPLE_INPUTS_DIRECTORY + MarkDownFileVisitorTest.FILE_SEPARATOR
            + "test4.md");
    System.setErr(new PrintStream(this.errContent, true, StandardCharsets.UTF_8));
    try {
      this.tempFiles1 = File.createTempFile("temp1", ".md");
    } catch (IOException e) {
      fail();
    }
  }

  /**
   * Tear down. Resets the fields before each test.
   */
  @AfterEach
  void tearDown() {
    System.setErr(this.originalErr);
    this.pizzaPath = null;
    this.test1Path = null;
    this.test4Path = null;
    this.tempFiles1 = null;
    assertNull(this.pizzaPath);
    assertNull(this.test1Path);
    assertNull(this.test4Path);
    assertNull(this.tempFiles1);
  }

  /**
   * Test pre visit directory.
   */
  @Test
  void testPreVisitDirectory() {
    assertEquals(FileVisitResult.CONTINUE,
        MarkDownFileVisitorTest.MY_MD_VISITOR.preVisitDirectory(Path.of("pizza"), null));
  }

  /**
   * Test visit file.
   */
  @Test
  void testVisitFile() {
    try {
      assertEquals(FileVisitResult.CONTINUE,
          MarkDownFileVisitorTest.MY_MD_VISITOR.visitFile(this.test1Path,
              Files.readAttributes(this.test1Path,
                  BasicFileAttributes.class)));
      assertEquals(FileVisitResult.CONTINUE,
          MarkDownFileVisitorTest.MY_MD_VISITOR.visitFile(this.test1Path,
              Files.readAttributes(this.test1Path,
                  BasicFileAttributes.class)));
      assertEquals(FileVisitResult.CONTINUE,
          MarkDownFileVisitorTest.MY_MD_VISITOR.visitFile(this.test4Path,
              Files.readAttributes(this.test1Path,
                  BasicFileAttributes.class)));
    } catch (IOException e) {
      fail();
    }
  }

  /**
   * Test visit file failed.
   */
  @Test
  void testVisitFileFailed() {
    assertEquals(FileVisitResult.CONTINUE,
        MarkDownFileVisitorTest.MY_MD_VISITOR.visitFileFailed(this.pizzaPath, null));
    MarkDownFileVisitorTest.MY_MD_VISITOR.visitFileFailed(this.pizzaPath,
        new IOException("hello"));
    assertEquals("Visit file failed for the file: hello\n".strip(),
        this.errContent.toString().strip());
  }

  /**
   * Test post visit directory.
   */
  @Test
  void testPostVisitDirectory() {
    assertEquals(FileVisitResult.CONTINUE,
        MarkDownFileVisitorTest.MY_MD_VISITOR.postVisitDirectory(this.pizzaPath, null));
  }

  /**
   * Test get files.
   */
  @Test
  void testGetFiles() {
    Collection<File> resultFiles = new ArrayList<>();
    resultFiles.add(
        new File(
            MarkDownFileVisitorTest.SAMPLE_INPUTS_DIRECTORY + MarkDownFileVisitorTest.FILE_SEPARATOR
                + "test1.md"));
    resultFiles.add(new File(
        String.join(MarkDownFileVisitorTest.FILE_SEPARATOR,
            MarkDownFileVisitorTest.SAMPLE_INPUTS_DIRECTORY, "sampleFiles2",
            "test3.md")));
    resultFiles.add(
        new File(
            MarkDownFileVisitorTest.SAMPLE_INPUTS_DIRECTORY + MarkDownFileVisitorTest.FILE_SEPARATOR
                + "test2.md"));
    assertThrows(RuntimeException.class,
        () -> MarkDownFileVisitorTest.MY_MD_VISITOR.getFiles(Path.of("pizza")));
    assertEquals(resultFiles,
        MarkDownFileVisitorTest.MY_MD_VISITOR.getFiles(Path.of(
            MarkDownFileVisitorTest.SAMPLE_INPUTS_DIRECTORY)));
  }
}

