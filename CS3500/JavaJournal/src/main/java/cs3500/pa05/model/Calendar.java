package cs3500.pa05.model;

import static java.util.stream.Collectors.groupingBy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import cs3500.pa05.model.comparators.CompareByDay;
import cs3500.pa05.model.enums.CalendarFeature;
import cs3500.pa05.model.enums.CompletionStatus;
import cs3500.pa05.model.enums.DayType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.TreeMap;

/**
 * Represents a calendar entry.
 */
@JsonPropertyOrder({"name", "startDay", "maxEvents", "maxTasks", "tasks", "events"})
public class Calendar {

  /**
   * The Tasks.
   */
  @JsonProperty("tasks")
  private List<Task> tasks;
  /**
   * The Events.
   */
  @JsonProperty("events")
  private List<Event> events;
  /**
   * The Start day.
   */
  @JsonProperty("startDay")
  private DayType startDay;
  /**
   * The Compare by day comparator.
   */
  @JsonIgnore
  private CompareByDay compareByDay;
  /**
   * The Entry comparator.
   */
  @JsonIgnore
  private Comparator<? super CalendarEntry> entryComparator;
  /**
   * The Name.
   */
  @JsonProperty("name")
  private String name;
  /**
   * The Max events.
   */
  @JsonProperty("maxEvents")
  private int maxEvents;
  /**
   * The Max tasks.
   */
  @JsonProperty("maxTasks")
  private int maxTasks;

  /**
   * Instantiates a new Calendar.
   */
  public Calendar() {
    this.events = new ArrayList<>();
    this.tasks = new ArrayList<>();
    this.startDay = DayType.TUESDAY;
    this.compareByDay = new CompareByDay(this.startDay);
    List<String> sampleNames = List.of("The Best Week Ever", "Cajun Style", "fonteNO", "fonteYES",
        "JSON-Derulo", "Send OODS", "Wenjamin Werner", "Josie Doesn't Remember the Alamo",
        "Ashley's Spinny Hat");
    this.name = sampleNames.get(new Random(System.currentTimeMillis()).nextInt(sampleNames.size()));
    this.maxEvents = 10;
    this.maxTasks = 10;
  }

  /**
   * Instantiates a new Calendar.
   *
   * @param tasks  the tasks
   * @param events the events
   */
  public Calendar(Collection<? extends Task> tasks, Collection<? extends Event> events) {
    this();
    this.tasks.addAll(tasks);
    this.events.addAll(events);
  }

  /**
   * Returns a map between day type and a list of tasks. The list of tasks is sorted by the entry
   * comparator and formatted based on the feature it is being displayed with.
   *
   * @param feature the feature the tasks are being displayed with
   * @return the task list as a map between day type and list of tasks.
   */
  public Map<DayType, List<Task>> getTaskList(CalendarFeature feature) {
    Map<DayType, List<Task>> sortedMap = new TreeMap<>(this.compareByDay);
    sortedMap.putAll(this.tasks.stream().collect(groupingBy(Task::getDayType)));
    if (!Objects.isNull(this.entryComparator)) {
      if (feature == CalendarFeature.TASK) {
        List<Task> taskList = new ArrayList<>(
            sortedMap.values().stream().flatMap(List::stream).toList());
        taskList.sort(this.entryComparator);
        sortedMap.clear();
        sortedMap.put(DayType.SUNDAY, taskList);
      }
      for (Map.Entry<DayType, List<Task>> entry : sortedMap.entrySet()) {
        entry.getValue().sort(this.entryComparator);
      }
    }
    return sortedMap;
  }

  /**
   * Get name string.
   *
   * @return the string
   */
  public String getName() {
    return this.name;
  }

  /**
   * Set name.
   *
   * @param name the name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns a map between day type and a list of events sorted by the entry comparator.
   *
   * @return the task event as a map between day type and list of events.
   */
  public Map<DayType, List<Event>> getEventList() {
    Map<DayType, List<Event>> sortedMap = new TreeMap<>(this.compareByDay);
    sortedMap.putAll(this.events.stream().collect(groupingBy(Event::getDayType)));
    if (!Objects.isNull(this.entryComparator)) {
      for (Map.Entry<DayType, List<Event>> entry : sortedMap.entrySet()) {
        entry.getValue().sort(this.entryComparator);
      }
    }
    return sortedMap;
  }

  /**
   * Counts completed tasks.
   *
   * @return the int representing the amount of completed tasks
   */
  public int countCompleted() {
    return this.tasks.stream()
        .mapToInt(task -> task.getStatus() == CompletionStatus.COMPLETE ? 1 : 0).sum();
  }

  /**
   * Add task to this calendar's list of tasks.
   *
   * @param task the task
   */
  public void addTask(Task task) {
    this.tasks.add(task);
  }

  /**
   * Add event to this calendar's list of events.
   *
   * @param event the event
   */
  public void addEvent(Event event) {
    this.events.add(event);
  }

  /**
   * Returns the start day of this calendar.
   *
   * @return starting day
   */
  public DayType getStartDay() {
    return this.startDay;
  }

  /**
   * Set start day.
   *
   * @param startDay the start day
   */
  public void setStartDay(DayType startDay) {
    this.startDay = startDay;
    this.compareByDay = new CompareByDay(startDay);
  }

  /**
   * Clears tasks for creation from template
   */
  public void clearTasks() {
    this.tasks = new ArrayList<>();
  }

  /**
   * Clears events for creation from template
   */
  public void clearEvents() {
    this.events = new ArrayList<>();
  }

  /**
   * Determines if this calendar has met the maximum events.
   *
   * @return if the max event limit has been met
   */
  public boolean hasMaxEvents() {
    return this.events.size() >= this.maxEvents;
  }

  /**
   * Gets max events limit.
   *
   * @return max event limit
   */
  public int getMaxEvents() {
    return this.maxEvents;
  }

  /**
   * Sets max events limit.
   *
   * @param maxEvents the max events
   */
  public void setMaxEvents(int maxEvents) {
    this.maxEvents = maxEvents;
  }

  /**
   * Determines if this calendar has met the maximum tasks.
   *
   * @return if the max task limit has been met
   */
  public boolean hasMaxTasks() {
    return this.tasks.size() >= this.maxTasks;
  }

  /**
   * Gets max tasks limit.
   *
   * @return max limit of tasks
   */
  public int getMaxTasks() {
    return this.maxTasks;
  }

  /**
   * Sets max tasks limit.
   *
   * @param maxTasks the max tasks
   */
  public void setMaxTasks(int maxTasks) {
    this.maxTasks = maxTasks;
  }

  /**
   * Indicates whether some other object is "equal to" this calendar.
   *
   * @param obj the object to be compared to
   * @return whether the given object equals this calendar.
   */
  @Override
  public boolean equals(Object obj) {
    boolean result = false;
    if (obj != null) {
      if (this.getClass() == obj.getClass()) {
        Calendar other = (Calendar) obj;
        result = this.events.equals(other.events)
            && this.tasks.equals(other.tasks)
            && this.maxEvents == other.maxEvents
            && this.maxTasks == other.maxTasks
            && this.name.equals(other.name)
            && this.startDay == other.startDay;
      }
    }
    return result;
  }


  /**
   * Returns a hash code value for this calendar.
   *
   * @return the hash code of this calendar
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.tasks, this.events, this.maxEvents,
        this.maxTasks);
  }

  /**
   * Sorts the events and the tasks by either name or duration. For duration, tasks will follow any
   * sorting of the events.
   *
   * @param comparator what we are comparing by
   */
  public void sortBy(Comparator<? super CalendarEntry> comparator) {
    this.entryComparator = comparator;
  }
}
