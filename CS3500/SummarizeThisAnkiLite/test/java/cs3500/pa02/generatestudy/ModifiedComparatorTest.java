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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the CreatedComparator class.
 */
class ModifiedComparatorTest {

  /**
   * The Modified comparator.
   */
  private final ModifiedComparator modifiedComparator = new ModifiedComparator();
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
      Files.setAttribute(this.boyFile.toPath(), "lastModifiedTime",
          FileTime.from(15, TimeUnit.DAYS));
      this.catFile = File.createTempFile("cat", ".md");
      Files.setAttribute(this.catFile.toPath(), "lastModifiedTime",
          FileTime.from(20, TimeUnit.DAYS));
      this.appleFile = File.createTempFile("apple", ".md");
      Files.setAttribute(this.appleFile.toPath(), "lastModifiedTime",
          FileTime.from(25, TimeUnit.DAYS));
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
        () -> this.modifiedComparator.compare(Path.of("pizza").toFile(), this.appleFile));
    assertEquals(1, this.modifiedComparator.compare(this.appleFile, this.boyFile));
    assertEquals(-1, this.modifiedComparator.compare(this.boyFile, this.appleFile));
    List<File> result = new ArrayList<>();
    result.add(this.appleFile);
    result.add(this.boyFile);
    result.add(this.catFile);
    result.sort(this.modifiedComparator);
    assertEquals(List.of(this.boyFile, this.catFile, this.appleFile), result);
  }
}