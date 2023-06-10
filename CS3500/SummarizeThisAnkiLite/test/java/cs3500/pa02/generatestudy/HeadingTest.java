package cs3500.pa02.generatestudy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the Heading class.
 */
class HeadingTest {

  /**
   * The Dummy important nonempty.
   */
  private ArrayList<Important> dummyImportantNonempty;
  /**
   * The Dummy important empty.
   */
  private ArrayList<Important> dummyImportantEmpty;
  /**
   * The Dummy heading non empty.
   */
  private Heading dummyHeadingNonEmpty;
  /**
   * The Dummy heading empty.
   */
  private Heading dummyHeadingEmpty;

  /**
   * Sets up. Initializes the fields before each test.
   */
  @BeforeEach
  void setUp() {
    this.dummyImportantNonempty = new ArrayList<>();
    this.dummyImportantNonempty.add(new Important("Important 1"));
    this.dummyImportantNonempty.add(new Important("Important 2"));
    this.dummyImportantEmpty = new ArrayList<>();
    this.dummyHeadingNonEmpty = new Heading("Nonempty", this.dummyImportantNonempty);
    this.dummyHeadingEmpty = new Heading("Empty", this.dummyImportantEmpty);
  }

  /**
   * Tear down. Resets the fields before each test.
   */
  @AfterEach
  void tearDown() {
    this.dummyImportantNonempty = null;
    this.dummyImportantEmpty = null;
    this.dummyHeadingNonEmpty = null;
    this.dummyHeadingEmpty = null;
    assertNull(this.dummyImportantNonempty);
    assertNull(this.dummyHeadingEmpty);
    assertNull(this.dummyHeadingNonEmpty);
    assertNull(this.dummyHeadingEmpty);
  }

  /**
   * Test toString.
   */
  @Test
  void testToString() {
    assertEquals("Nonempty\n- Important 1\n- Important 2", this.dummyHeadingNonEmpty.toString());
    assertEquals("Empty", this.dummyHeadingEmpty.toString());
  }
}