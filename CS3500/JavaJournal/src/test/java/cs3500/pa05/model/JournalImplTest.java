package cs3500.pa05.model;

import static cs3500.pa05.model.enums.CompletionStatus.COMPLETE;
import static cs3500.pa05.model.enums.CompletionStatus.INCOMPLETE;
import static cs3500.pa05.model.enums.DayType.FRIDAY;
import static cs3500.pa05.model.enums.DayType.MONDAY;
import static cs3500.pa05.model.enums.DayType.THURSDAY;
import static cs3500.pa05.model.enums.DayType.TUESDAY;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import cs3500.pa05.model.comparators.CompareByName;
import cs3500.pa05.model.enums.CalendarFeature;
import cs3500.pa05.model.enums.DayType;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Represents tests for journal.
 */
class JournalImplTest {

  /**
   * The Journal.
   */
  private JournalImpl journal;
  /**
   * The Old journal.
   */
  private JournalImpl oldJournal;

  /**
   * The Tasks.
   */
  private List<Task> tasks;

  /**
   * Sets up.
   */
  @BeforeEach
  void setUp() {
    Event event = new Event("Example Event", "This is an example event I am making.", THURSDAY,
        new Time(10, 10), 60);
    Event event2 = new Event("Example Event", "This is an example event I am making.", FRIDAY,
        new Time(5, 55), 60);
    List<Event> events = new ArrayList<>();
    events.add(event);
    events.add(event2);
    List<Event> eventsOld = new ArrayList<>();
    eventsOld.add(event);
    eventsOld.add(event2);

    Task task = new Task("Example Task", "This is an example task I am making.", MONDAY,
        INCOMPLETE);
    Task task2 = new Task("Example Task", "This is an example task I am making.", TUESDAY,
        COMPLETE);
    List<Task> taskArrayList = new ArrayList<>();
    taskArrayList.add(task);
    taskArrayList.add(task2);
    List<Task> tasksOld = new ArrayList<>();
    tasksOld.add(task);
    tasksOld.add(task2);
    this.tasks = taskArrayList;

    Calendar calendar = new Calendar(taskArrayList, events);
    calendar.setMaxEvents(5);
    calendar.setMaxTasks(5);
    calendar.setName("ash-wee");

    Calendar calendarOld = new Calendar(tasksOld, eventsOld);
    calendarOld.setMaxEvents(5);
    calendarOld.setMaxTasks(5);
    calendarOld.setName("ash-wee");

    this.journal = new JournalImpl(calendar, new Note("note"), " ");
    this.oldJournal = new JournalImpl(calendarOld, new Note("note"), " ");
  }

  /**
   * Tear down.
   */
  @AfterEach
  void tearDown() {
    this.journal = null;
    this.oldJournal = null;
  }

  /**
   * Test set note.
   */
  @Test
  void testSetNote() {
    assertEquals(this.journal, this.oldJournal);
    this.journal.setNote("new note");
    assertNotEquals(this.journal, this.oldJournal);
    this.oldJournal.setNote("new note");
    assertEquals(this.journal, this.oldJournal);
    assertEquals("new note", this.journal.getNote());
  }

  /**
   * Test count completed tasks.
   */
  @Test
  void testCountCompletedTasks() {
    assertEquals(1, this.journal.countCompletedTasks());
  }

  /**
   * Test add task.
   */
  @Test
  void testAddTask() {
    assertEquals(this.journal, this.oldJournal);
    this.journal.addTask("hi", "hi", MONDAY, INCOMPLETE);
    assertNotEquals(this.journal, this.oldJournal);
    this.oldJournal.addTask("hi", "hi", MONDAY, INCOMPLETE);
    assertEquals(this.journal, this.oldJournal);
  }

  /**
   * Test add event.
   */
  @Test
  void testAddEvent() {
    assertEquals(this.journal, this.oldJournal);
    this.journal.addEvent("hi", "hi", MONDAY, new Time(5, 5), 60);
    assertNotEquals(this.journal, this.oldJournal);
    this.oldJournal.addEvent("hi", "hi", MONDAY, new Time(5, 5), 60);
    assertEquals(this.journal, this.oldJournal);
  }

  /**
   * Test setTaskLimit.
   */
  @Test
  void testSetTaskLimit() {
    assertEquals(this.journal, this.oldJournal);
    this.journal.setTaskLimit(100);
    assertNotEquals(this.journal, this.oldJournal);
    this.oldJournal.setTaskLimit(100);
    assertEquals(this.journal, this.oldJournal);
  }

  /**
   * Test setEventLimit.
   */
  @Test
  void testSetEventLimit() {
    assertEquals(this.journal, this.oldJournal);
    this.journal.setEventLimit(100);
    assertNotEquals(this.journal, this.oldJournal);
    this.oldJournal.setEventLimit(100);
    assertEquals(this.journal, this.oldJournal);
  }

  /**
   * Test sortBy.
   */
  @Test
  void testSortBy() {
    assertNotEquals(null, this.journal);
    assertNotEquals(this.journal, MONDAY);
    assertEquals(this.journal, this.oldJournal);
    this.journal.sortBy(new CompareByName());
    assertEquals(this.journal, this.oldJournal);
    this.oldJournal.sortBy(new CompareByName());
    assertEquals(this.journal, this.oldJournal);
  }

  /**
   * Test getters.
   */
  @Test
  void testGetters() {
    Task[] taskArrayMonday = new Task[2];
    taskArrayMonday[0] = this.tasks.get(0);
    Task[] taskArrayTuesday = new Task[2];
    taskArrayTuesday[0] = this.tasks.get(1);
    Map<DayType, Task[]> tasksMap = new EnumMap<>(DayType.class);
    tasksMap.put(TUESDAY, taskArrayTuesday);
    tasksMap.put(MONDAY, taskArrayMonday);
    assertDoesNotThrow(() -> this.journal.getTasks(CalendarFeature.EVENT));
    assertDoesNotThrow(() -> this.journal.getEvents());
  }

  /**
   * Test hash code.
   */
  @Test
  void testHashCode() {
    assertDoesNotThrow(() -> this.journal.hashCode());
  }

  /**
   * Test get start.
   */
  @Test
  void testGetStart() {
    this.journal.setStart(MONDAY);
    assertEquals(MONDAY, this.journal.getStart());
  }

  /**
   * Test equals.
   */
  @Test
  void testEquals() {
    assertNotEquals(null, this.journal);
  }

  /**
   * Test get and set password.
   */
  @Test
  void testGetAndSetPassword() {
    this.journal.setPassword("hi");
    assertEquals("hi", this.journal.getPassword());
  }

  /**
   * Test has max.
   */
  @Test
  void testHasMax() {
    assertFalse(this.journal.hasMaxEvents());
    assertFalse(this.journal.hasMaxTasks());
    this.journal.setEventLimit(5);
    this.journal.setTaskLimit(5);
    assertEquals(5, this.journal.getMaxEvents());
    assertEquals(5, this.journal.getMaxTasks());
  }

  /**
   * Test get and set week name.
   */
  @Test
  void testGetAndSetWeekName() {
    this.journal.setWeekName("hi");
    assertEquals("hi", this.journal.getWeekName());
  }

  /**
   * Test get and set path.
   */
  @Test
  void testGetAndSetPath() {
    this.journal.setPath("hi");
    assertEquals("hi", this.journal.getPath());
    assertNotEquals(null, this.journal);
  }

  /**
   * Test get and set cal.
   */
  @Test
  void testGetAndSetCal() {
    this.journal.setCalendar(this.journal.getCalendar());
    this.journal.clearEvents();
    this.journal.clearTasks();
    assertEquals(new EnumMap<>(DayType.class), this.journal.getEvents());
    //assertEquals(new ArrayList<>(), this.journal.getTasks(Ca));
  }
}
