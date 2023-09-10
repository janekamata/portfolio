package cs3500.pa05.model.enums;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 * Represents a day type enum.
 */
public enum DayType {
  /**
   * Monday.
   */
  MONDAY("Monday", 0),
  /**
   * Tuesday.
   */
  TUESDAY("Tuesday", 1),
  /**
   * Wednesday.
   */
  WEDNESDAY("Wednesday", 2),
  /**
   * Thursday.
   */
  THURSDAY("Thursday", 3),
  /**
   * Friday.
   */
  FRIDAY(
      "Friday", 4),
  /**
   * Saturday.
   */
  SATURDAY("Saturday", 5),
  /**
   * Sunday.
   */
  SUNDAY("Sunday", 6);

  private static final DayType[] DEQUE_TYPE = new DayType[0];
  /**
   * The Day.
   */
  private final String day;
  /**
   * The Order.
   */
  private int order;

  /**
   * Instantiates a new Day type.
   *
   * @param day   the day
   * @param order the order
   */
  DayType(String day, int order) {
    this.day = day;
    this.order = order;
  }

  /**
   * Changes the order of the days based on the given start day for the week.
   *
   * @param start the start day for the week
   */
  public static void changeOrder(DayType start) {
    Deque<DayType> deque = new ArrayDeque<>(Arrays.stream(DayType.values()).toList());
    while (deque.getFirst() != start) {
      DayType head = deque.poll();
      deque.offerLast(head);
    }
    DayType[] finalDeque = deque.toArray(DayType.DEQUE_TYPE);
    for (int n = 0; n < finalDeque.length; n += 1) {
      finalDeque[n].order = n;
    }
  }


  /**
   * Gets order.
   *
   * @return the order
   */
  public int getOrder() {
    return this.order;
  }

  /**
   * Returns a string representing this day as the user sees it in the view.
   *
   * @return the string representing this enum in the view
   */
  @Override
  public String toString() {
    return this.day;
  }


}
