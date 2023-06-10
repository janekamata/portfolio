package cs3500.pa02.studysession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the StudySet class.
 */
class StudySetTest {

  /**
   * The Study set.
   */
  private StudySet studySet;
  /**
   * The Card list 1.
   */
  private List<Card> cardList1;
  /**
   * The Card list 2.
   */
  private List<Card> cardList2;
  /**
   * The hard list.
   */
  private List<Card> allHardList;
  /**
   * The easy list.
   */
  private List<Card> allEasyList;
  /**
   * The Card easy 1.
   */
  private Card cardEasy1;
  /**
   * The Card easy 2.
   */
  private Card cardEasy2;
  /**
   * The Card easy 3.
   */
  private Card cardEasy3;
  /**
   * The Card hard 4.
   */
  private Card cardHard4;
  /**
   * The Card hard 5.
   */
  private Card cardHard5;
  /**
   * The Card hard 6.
   */
  private Card cardHard6;
  /**
   * The Card hard 7.
   */
  private Card cardHard7;
  /**
   * The Stats.
   */
  private SessionStats stats;

  /**
   * Sets up.
   */
  @BeforeEach
  void setUp() {
    this.studySet = new StudySet(1);
    this.cardEasy1 = new Card("1", "11", CardDifficulty.EASY);
    this.cardEasy2 = new Card("2", "22", CardDifficulty.EASY);
    this.cardEasy3 = new Card("3", "33", CardDifficulty.EASY);
    this.cardHard4 = new Card("4", "44", CardDifficulty.HARD);
    this.cardHard5 = new Card("5", "55", CardDifficulty.HARD);
    this.cardHard6 = new Card("6", "66", CardDifficulty.HARD);
    this.cardHard7 = new Card("7", "77", CardDifficulty.HARD);
    this.cardList1 = new ArrayList<>();
    this.cardList1.add(this.cardEasy1);
    this.cardList1.add(this.cardHard4);
    this.cardList1.add(this.cardEasy3);
    this.cardList1.add(this.cardHard5);
    this.cardList2 = new ArrayList<>();
    this.cardList2.add(this.cardHard6);
    this.cardList2.add(this.cardHard7);
    this.cardList2.add(this.cardEasy2);
    this.allEasyList = new ArrayList<>();
    this.allEasyList.add(this.cardEasy1);
    this.allEasyList.add(this.cardEasy3);
    this.allEasyList.add(this.cardEasy2);
    this.allHardList = new ArrayList<>();
    this.allHardList.add(this.cardHard4);
    this.allHardList.add(this.cardHard5);
    this.allHardList.add(this.cardHard6);
    this.allHardList.add(this.cardHard7);
    Random tempRandom = new Random(1);
    Collections.shuffle(this.allEasyList, tempRandom);
    Collections.shuffle(this.allHardList, tempRandom);
    this.studySet.updateCards(this.cardList1);
    this.studySet.updateCards(this.cardList2);
    this.stats = new SessionStats(4, 3);
  }

  /**
   * Tear down.
   */
  @AfterEach
  void tearDown() {
    this.studySet = null;
    this.cardEasy1 = null;
    this.cardEasy2 = null;
    this.cardEasy3 = null;
    this.cardHard4 = null;
    this.cardHard5 = null;
    this.cardHard6 = null;
    this.cardHard7 = null;
    this.cardList1 = null;
    this.cardList2 = null;
    this.allHardList = null;
    this.allEasyList = null;
    this.stats = null;
    assertNull(this.studySet);
    assertNull(this.cardEasy1);
    assertNull(this.cardEasy2);
    assertNull(this.cardEasy3);
    assertNull(this.cardHard4);
    assertNull(this.cardHard5);
    assertNull(this.cardHard6);
    assertNull(this.cardHard7);
    assertNull(this.cardList1);
    assertNull(this.cardList2);
    assertNull(this.allHardList);
    assertNull(this.allEasyList);
    assertNull(this.stats);
  }

  /**
   * Test set up study set.
   */
  @Test
  void testSetUpStudySet() {
    assertEquals(this.stats, this.studySet.setUpStudySet(5));
    StudySet tempStudySet = new StudySet();
    assertNull(tempStudySet.setUpStudySet(2));
  }

  /**
   * Test to string.
   */
  @Test
  void testToString() {
    String result = """
        - 1 ;;; 11
        * 4 ;;; 44
        - 3 ;;; 33
        * 5 ;;; 55
        * 6 ;;; 66
        * 7 ;;; 77
        - 2 ;;; 22""";
    assertEquals(result, this.studySet.toString());
  }

  /**
   * Test update cards.
   */
  @Test
  void testUpdateCards() {
    StudySet tempStudySet = new StudySet();
    tempStudySet.updateCards(this.cardList1);
    tempStudySet.updateCards(this.cardList2);
    assertEquals(tempStudySet, this.studySet);
  }

  /**
   * Test equals.
   */
  @Test
  void testEquals() {
    assertNotEquals(new StudySet(), this.cardEasy1);
    assertEquals(new StudySet(), new StudySet());
    assertNotEquals(null, this.studySet);
    assertNotEquals(this.studySet, null);
  }

  /**
   * Test hash code.
   */
  @Test
  void testHashCode() {
    List<Card> tempList = new ArrayList<>();
    tempList.addAll(this.cardList1);
    tempList.addAll(this.cardList2);
    assertEquals(Objects.hash(tempList), this.studySet.hashCode());
  }

  /**
   * Test next card num below.
   */
  @Test
  void testNextCardNumBelow() {
    List<Card> tempList = new ArrayList<>();
    tempList.addAll(this.allHardList);
    tempList.addAll(this.allEasyList);
    tempList = tempList.stream().limit(5).toList();
    assertNull(new StudySet().nextCard());
    this.studySet.setUpStudySet(5);
    assertEquals(tempList.get(0), this.studySet.nextCard());
    assertEquals(tempList.get(1), this.studySet.nextCard());
    assertEquals(tempList.get(2), this.studySet.nextCard());
    assertEquals(tempList.get(3), this.studySet.nextCard());
    assertEquals(tempList.get(4), this.studySet.nextCard());
    assertNull(this.studySet.nextCard());
  }

  /**
   * Test next card num above.
   */
  @Test
  void testNextCardNumAbove() {
    List<Card> tempList = new ArrayList<>();
    tempList.addAll(this.allHardList);
    tempList.addAll(this.allEasyList);
    tempList = tempList.stream().limit(10).toList();
    assertNull(new StudySet().nextCard());
    this.studySet.setUpStudySet(10);
    assertEquals(tempList.get(0), this.studySet.nextCard());
    assertEquals(tempList.get(1), this.studySet.nextCard());
    assertEquals(tempList.get(2), this.studySet.nextCard());
    assertEquals(tempList.get(3), this.studySet.nextCard());
    assertEquals(tempList.get(4), this.studySet.nextCard());
    assertEquals(tempList.get(5), this.studySet.nextCard());
    assertEquals(tempList.get(6), this.studySet.nextCard());
    assertNull(this.studySet.nextCard());
  }
}