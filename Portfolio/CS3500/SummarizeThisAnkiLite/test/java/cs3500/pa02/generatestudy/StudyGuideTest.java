package cs3500.pa02.generatestudy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.Objects;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the StudyGuide class.
 */
class StudyGuideTest {

  private ArrayList<Important> dummyImportantNonempty;
  private ArrayList<Important> dummyImportantEmpty;
  private Heading dummyHeadingNonEmpty;
  private Heading dummyHeadingEmpty;
  private ArrayList<Heading> dummyHeadingListNonEmpty;
  private ArrayList<Heading> dummyHeadingListEmpty;
  private StudyGuide dummyStudyGuideNonEmpty;
  private StudyGuide dummyStudyGuideEmpty;

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
    this.dummyHeadingListNonEmpty = new ArrayList<>();
    this.dummyHeadingListNonEmpty.add(this.dummyHeadingNonEmpty);
    this.dummyHeadingListNonEmpty.add(this.dummyHeadingEmpty);
    this.dummyHeadingListEmpty = new ArrayList<>();
    this.dummyStudyGuideNonEmpty = new StudyGuide(this.dummyHeadingListNonEmpty);
    this.dummyStudyGuideEmpty = new StudyGuide();
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
    this.dummyHeadingListNonEmpty = null;
    this.dummyHeadingListEmpty = null;
    this.dummyStudyGuideNonEmpty = null;
    this.dummyStudyGuideEmpty = null;
    assertNull(this.dummyImportantNonempty);
    assertNull(this.dummyHeadingEmpty);
    assertNull(this.dummyHeadingNonEmpty);
    assertNull(this.dummyHeadingEmpty);
    assertNull(this.dummyHeadingListNonEmpty);
    assertNull(this.dummyHeadingListEmpty);
    assertNull(this.dummyStudyGuideNonEmpty);
    assertNull(this.dummyStudyGuideEmpty);
  }

  /**
   * Test toString.
   */
  @Test
  void testToString() {
    assertEquals("", this.dummyStudyGuideEmpty.toString());
    assertEquals("Nonempty\n- Important 1\n- Important 2\n\nEmpty",
        this.dummyStudyGuideNonEmpty.toString());
  }

  /**
   * Test updateSummaries.
   */
  @Test
  void testUpdateSummaries() {
    assertEquals(this.dummyStudyGuideEmpty, new StudyGuide(this.dummyHeadingListEmpty));
    this.dummyStudyGuideEmpty.updateSummaries(new Heading("Nonempty", this.dummyImportantNonempty));
    this.dummyStudyGuideEmpty.updateSummaries(new Heading("Empty", this.dummyImportantEmpty));
    assertEquals(this.dummyStudyGuideEmpty, this.dummyStudyGuideNonEmpty);
  }

  @Test
  void testEquals() {
    assertNotEquals(this.dummyStudyGuideEmpty, this.dummyStudyGuideNonEmpty);
    assertEquals(new StudyGuide(), this.dummyStudyGuideEmpty);
    assertNotEquals(null, this.dummyStudyGuideEmpty);
    assertNotEquals(this.dummyStudyGuideEmpty, this.dummyImportantEmpty);
    assertNotEquals(this.dummyStudyGuideEmpty, null);

    assertNotEquals(null, this.dummyStudyGuideEmpty);
    assertNotEquals(this.dummyStudyGuideEmpty, null);
  }

  @Test
  void testHashCode() {
    assertEquals(Objects.hash(this.dummyHeadingListNonEmpty),
        this.dummyStudyGuideNonEmpty.hashCode());
  }
}