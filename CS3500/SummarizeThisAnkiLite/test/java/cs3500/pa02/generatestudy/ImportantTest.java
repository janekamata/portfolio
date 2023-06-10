package cs3500.pa02.generatestudy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the Important class.
 */
class ImportantTest {

  /**
   * The Dummy important non empty.
   */
  private Important dummyImportantNonEmpty;
  /**
   * The Dummy important empty.
   */
  private Important dummyImportantEmpty;

  /**
   * Sets up. Initializes the fields before each test.
   */
  @BeforeEach
  void setUp() {
    this.dummyImportantNonEmpty = new Important("This is important.");
    this.dummyImportantEmpty = new Important("");
  }

  /**
   * Tear down. Resets the fields before each test.
   */
  @AfterEach
  void tearDown() {
    this.dummyImportantNonEmpty = null;
    this.dummyImportantEmpty = null;
    assertNull(this.dummyImportantNonEmpty);
    assertNull(this.dummyImportantEmpty);
  }

  /**
   * Test toString.
   */
  @Test
  void testToString() {
    assertEquals("- This is important.", this.dummyImportantNonEmpty.toString());
    assertEquals("- ", this.dummyImportantEmpty.toString());
  }
}