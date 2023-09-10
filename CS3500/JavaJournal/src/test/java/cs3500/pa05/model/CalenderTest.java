package cs3500.pa05.model;

import static cs3500.pa05.model.enums.CompletionStatus.COMPLETE;
import static cs3500.pa05.model.enums.DayType.FRIDAY;
import static cs3500.pa05.model.enums.DayType.MONDAY;
import static cs3500.pa05.model.enums.DayType.THURSDAY;
import static cs3500.pa05.model.enums.DayType.TUESDAY;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa05.model.comparators.CompareByName;
import cs3500.pa05.model.enums.CalendarFeature;
import cs3500.pa05.model.enums.CompletionStatus;
import cs3500.pa05.model.enums.DayType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Represents tests for calendar.
 */
class CalenderTest {

  /**
   * The Calendar.
   */
  private Calendar calendar;
  /**
   * The Set-up calendar.
   */
  private Calendar setUpCalendar;
  /**
   * The Tasks.
   */
  private List<Task> tasks;
  /**
   * The Events.
   */
  private List<Event> events;

  /**
   * Sets up.
   */
  @BeforeEach
  void setUp() {
    this.calendar = new Calendar();
    this.calendar.setMaxEvents(5);
    this.calendar.setMaxTasks(5);
    this.calendar.setName("Josie Doesn't Remember the Alamo");

    Event event = new Event("Example Event", "This is an example event I am making.", THURSDAY,
        new Time(10, 10), 60);
    Event event2 = new Event("Example Event", "This is an example event I am making.", FRIDAY,
        new Time(5, 55), 60);
    List<Event> eventArrayList = new ArrayList<>();
    eventArrayList.add(event);
    eventArrayList.add(event2);
    this.events = eventArrayList;

    Task task = new Task("Example Task", "This is an example task I am making.", MONDAY,
        CompletionStatus.INCOMPLETE);
    Task task2 = new Task("Example Task", "This is an example task I am making.", TUESDAY,
        COMPLETE);
    List<Task> taskArrayList = new ArrayList<>();
    taskArrayList.add(task);
    taskArrayList.add(task2);
    this.tasks = taskArrayList;

    this.setUpCalendar = new Calendar(taskArrayList, eventArrayList);
    this.setUpCalendar.setMaxEvents(5);
    this.setUpCalendar.setMaxTasks(5);
    this.setUpCalendar.setName("Josie Doesn't Remember the Alamo");
  }

  /**
   * Tear down.
   */
  @AfterEach
  void tearDown() {
    this.calendar.clearEvents();
    this.calendar.clearTasks();
    this.calendar = null;
  }


  /**
   * Test add event add task.
   */
  @Test
  void testAddEventAddTask() {
    this.calendar.addEvent(
        new Event("Example Event", "This is an example event I am making.", THURSDAY,
            new Time(10, 10), 60));
    this.calendar.addEvent(
        new Event("Example Event", "This is an example event I am making.", FRIDAY, new Time(5, 55),
            60));
    this.calendar.addTask(new Task("Example Task", "This is an example task I am making.", MONDAY,
        CompletionStatus.INCOMPLETE));
    this.calendar.addTask(
        new Task("Example Task", "This is an example task I am making.", TUESDAY, COMPLETE));
    assertEquals(this.calendar, this.setUpCalendar);
  }


  /**
   * Test equals.
   */
  @Test
  void testEquals() {
    assertNotEquals(this.calendar, MONDAY);
    assertNotEquals(this.setUpCalendar, new Calendar());

    assertNotEquals(this.setUpCalendar, new Calendar(this.tasks, new ArrayList<>()));
    assertNotEquals(this.calendar, new Calendar(this.tasks, new ArrayList<>()));

    assertEquals(this.setUpCalendar, this.setUpCalendar);
    assertNotEquals(null, this.calendar);
  }

  /**
   * Test unequal.
   */
  @Test
  void testUnequal() {
    Calendar cal = new Calendar(this.tasks, this.events);
    cal.setMaxEvents(5);
    cal.setMaxTasks(5);
    cal.setName("Josie Doesn't Remember the Alamo");
    assertEquals(this.setUpCalendar, cal);
    assertNotEquals(null, this.calendar);
  }

  /**
   * Test unequal 2.
   */
  @Test
  void testUnequal2() {
    Calendar cal = new Calendar(this.tasks, this.events);
    cal.setMaxEvents(4);
    cal.setMaxTasks(5);
    assertNotEquals(this.setUpCalendar, cal);
    Calendar cal2 = new Calendar(this.tasks, this.events);
    cal2.setMaxEvents(5);
    cal2.setMaxTasks(4);
    assertNotEquals(this.setUpCalendar, cal2);
  }

  /**
   * Test hash code.
   */
  @Test
  void testHashCode() {
    assertDoesNotThrow(() -> this.calendar.hashCode());
    assertDoesNotThrow(() -> this.setUpCalendar.hashCode());
  }

  /**
   * Test set up.
   */
  @Test
  void testSetUp() {
    Calendar cal = new Calendar();
    cal.setMaxTasks(5);
    cal.setMaxEvents(5);
    cal.setName("Josie Doesn't Remember the Alamo");

    assertEquals(this.calendar, cal);
    this.calendar.setName("Ash");
    assertNotEquals(this.calendar, cal);
    cal.setName("Ash");
    assertEquals(this.calendar, cal);
    this.calendar.setStartDay(MONDAY);
    assertNotEquals(this.calendar, cal);
    cal.setStartDay(MONDAY);
    assertEquals(this.calendar, cal);
  }

  /**
   * Test get event list.
   */
  @Test
  void testGetEventList() {
    this.setUpCalendar.sortBy(new CompareByName());
    Map<DayType, List<Event>> expected = new Calendar(this.tasks, this.events).getEventList();
    Map<DayType, List<Event>> result = this.setUpCalendar.getEventList();
    assertEquals(expected, result);
  }

  /**
   * Test get task list false.
   */
  @Test
  void testGetTaskListFalse() {
    this.setUpCalendar.sortBy(new CompareByName());
    Map<DayType, List<Task>> expected = new Calendar(this.tasks, this.events).getTaskList(
        CalendarFeature.EVENT);
    Map<DayType, List<Task>> result = this.setUpCalendar.getTaskList(CalendarFeature.TASK);
    assertNotEquals(expected, result);
  }

  /**
   * Test get task list true.
   */
  @Test
  void testGetTaskListTrue() {
    this.setUpCalendar.sortBy(new CompareByName());
    assertDoesNotThrow(
        () -> new Calendar(this.tasks, this.events).getTaskList(CalendarFeature.TASK));
    assertDoesNotThrow(() -> this.setUpCalendar.getTaskList(CalendarFeature.EVENT));
  }

  /**
   * Test get name.
   */
  @Test
  void testGetName() {
    assertEquals("Josie Doesn't Remember the Alamo", this.setUpCalendar.getName());
  }

  /**
   * Test has max events and tasks.
   */
  @Test
  void testHasMaxEventsAndTasks() {
    assertFalse(this.calendar.hasMaxEvents());
    this.calendar.setMaxEvents(0);
    assertTrue(this.calendar.hasMaxEvents());

    assertFalse(this.calendar.hasMaxTasks());
    this.calendar.setMaxTasks(0);
    assertTrue(this.calendar.hasMaxTasks());

    assertEquals(0, this.calendar.getMaxTasks());
    assertEquals(0, this.calendar.getMaxEvents());

    assertNotEquals(null, this.calendar);
  }
}

