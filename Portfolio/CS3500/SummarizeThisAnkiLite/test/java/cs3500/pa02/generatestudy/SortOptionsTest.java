package cs3500.pa02.generatestudy;

import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the GetFileComparator class.
 */
class SortOptionsTest {

  /**
   * Sets up. Initializes the fields (none) before each test.
   */
  @BeforeEach
  void setUp() {
  }

  /**
   * Tear down. Resets the fields (none) before each test.
   */
  @AfterEach
  void tearDown() {
  }

  /**
   * Test getFileComparator.
   */
  @Test
  void testGetFileComparator() {
    assertSame(FilenameComparator.class, SortOptions.FILENAME.getFileComparator().getClass());
    assertSame(ModifiedComparator.class, SortOptions.MODIFIED.getFileComparator().getClass());
    assertSame(CreatedComparator.class, SortOptions.CREATED.getFileComparator().getClass());
  }
}