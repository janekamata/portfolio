package cs3500.pa05.model.comparators;

import cs3500.pa05.model.CalendarEntry;
import java.io.Serializable;
import java.util.Comparator;

/**
 * Compares two calendar entries based on their entry name
 */
public class CompareByName implements Comparator<CalendarEntry>, Serializable {

  /**
   * Compares two calendar entries by their entry name
   *
   * @param o1 the first entry to be compared.
   * @param o2 the second entry to be compared.
   * @return an int indicating which one has a name first
   */
  @Override
  public int compare(CalendarEntry o1, CalendarEntry o2) {
    return o1.getEntryName().compareTo(o2.getEntryName());
  }
}
