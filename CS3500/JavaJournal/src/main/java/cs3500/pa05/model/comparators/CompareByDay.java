package cs3500.pa05.model.comparators;

import cs3500.pa05.model.enums.DayType;
import java.io.Serializable;
import java.util.Comparator;

/**
 * Compares day types based on the order of the days in the calendar.
 */
public class CompareByDay implements Comparator<DayType>, Serializable {

  /**
   * Instantiates a new Compare by day.
   *
   * @param start the start day of the week
   */
  public CompareByDay(DayType start) {
    DayType.changeOrder(start);
  }

  /**
   * Compares day types by their order in the week.
   *
   * @param o1 the first day to be compared.
   * @param o2 the second day to be compared.
   * @return an int indicating which one comes first in the week
   */
  @Override
  public int compare(DayType o1, DayType o2) {
    return Integer.compare(o1.getOrder(), o2.getOrder());
  }
}
