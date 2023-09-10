package cs3500.pa05.model.enums;

/**
 * Represents a comparator class type enum.
 */
public enum ComparatorClass {
  /**
   * Compare by duration comparator class.
   */
  COMPARE_BY_DURATION("By duration"),

  /**
   * Compare by name comparator class.
   */
  COMPARE_BY_NAME("By name");

  /**
   * The Name of this enum in the view.
   */
  private final String name;

  /**
   * Instantiates a new Comparator class.
   *
   * @param name the name
   */
  ComparatorClass(String name) {
    this.name = name;
  }

  /**
   * Returns a string representing this comparator class in the view.
   *
   * @return the string representing this enum
   */
  @Override
  public String toString() {
    return this.name;
  }
}
