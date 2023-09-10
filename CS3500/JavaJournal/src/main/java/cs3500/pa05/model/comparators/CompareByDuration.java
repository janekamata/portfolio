package cs3500.pa05.model.comparators;

import cs3500.pa05.model.CalendarEntry;
import cs3500.pa05.model.Event;
import java.io.Serializable;
import java.util.Comparator;

/**
 * Compares the calendar entries by their duration.
 */
public class CompareByDuration implements Comparator<CalendarEntry>, Serializable {

  /**
   * Compares calendar entries by their duration.
   *
   * @param o1 the first entry to be compared.
   * @param o2 the second entry to be compared.
   * @return an int indicating which one has shorter duration
   */
  @Override
  public int compare(CalendarEntry o1, CalendarEntry o2) {
    int result;
    if (o1.getClass() == Event.class) {
      Event e1 = (Event) o1;
      Event e2 = (Event) o2;
      result = Math.subtractExact(e2.getDuration(), e1.getDuration());
    } else {
      result = -1;
    }
    return result;
  }
}
