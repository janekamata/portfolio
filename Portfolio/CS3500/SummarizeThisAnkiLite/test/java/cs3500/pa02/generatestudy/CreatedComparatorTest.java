package cs3500.pa02.generatestudy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the CreatedComparator class.
 */
class CreatedComparatorTest {

  /**
   * The Created comparator.
   */
  private final CreatedComparator createdComparator = new CreatedComparator();
  /**
   * The Apple file.
   */
  private File appleFile;
  /**
   * The Boy file.
   */
  private File boyFile;
  /**
   * The Cat file.
   */
  private File catFile;

  /**
   * Sets up. Initializes the fields before each test.
   */
  @BeforeEach
  void setUp() {
    try {
      this.boyFile = File.createTempFile("boy", ".md");
      Files.setAttribute(this.boyFile.toPath(), "creationTime",
          FileTime.from(Instant.parse("2020-05-14T12:00:00Z")));
      this.appleFile = File.createTempFile("apple", ".md");
      Files.setAttribute(this.appleFile.toPath(), "creationTime",
          FileTime.from(Instant.parse("2021-05-14T12:00:00Z")));
      this.catFile = File.createTempFile("cat", ".md");
      Files.setAttribute(this.catFile.toPath(), "creationTime",
          FileTime.from(Instant.parse("2022-05-14T12:00:00Z")));
    } catch (IOException e) {
      fail();
    }
  }

  /**
   * Tear down. Resets the fields before each test.
   */
  @AfterEach
  void tearDown() {
    this.appleFile = null;
    this.boyFile = null;
    this.catFile = null;
    assertNull(this.appleFile);
    assertNull(this.boyFile);
    assertNull(this.catFile);
  }

  /**
   * Test for compare.
   */
  @Test
  void testCompare() {
    assertThrows(RuntimeException.class,
        () -> this.createdComparator.compare(Path.of("pizza").toFile(), this.appleFile));
    assertEquals(1, this.createdComparator.compare(this.appleFile, this.boyFile));
    assertEquals(-1, this.createdComparator.compare(this.boyFile, this.appleFile));
    List<File> result = new ArrayList<>();
    result.add(this.appleFile);
    result.add(this.boyFile);
    result.add(this.catFile);
    result.sort(this.createdComparator);
    assertEquals(List.of(this.boyFile, this.appleFile, this.catFile), result);
  }
}