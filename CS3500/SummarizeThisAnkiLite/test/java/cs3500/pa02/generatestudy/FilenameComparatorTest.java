package cs3500.pa02.generatestudy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the FilenameComparator class.
 */
class FilenameComparatorTest {

  /**
   * The Filename comparator.
   */
  private final Comparator<File> filenameComparator = new FilenameComparator();
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
      this.appleFile = File.createTempFile("apple", ".md");
      this.boyFile = File.createTempFile("boy", ".md");
      this.catFile = File.createTempFile("cat", ".md");
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
    assertEquals(-1, this.filenameComparator.compare(this.appleFile, this.boyFile));
    assertEquals(1, this.filenameComparator.compare(this.boyFile, this.appleFile));
    List<File> result = new ArrayList<>();
    result.add(this.catFile);
    result.add(this.appleFile);
    result.add(this.boyFile);
    result.sort(this.filenameComparator);
    assertEquals(List.of(this.appleFile, this.boyFile, this.catFile), result);
  }
}