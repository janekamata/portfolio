package cs3500.pa02.studysession;

/**
 * The enum Card difficulty.
 */
public enum CardDifficulty {
  /**
   * Easy card difficulty.
   */
  EASY,
  /**
   * Hard card difficulty.
   */
  HARD;

  /**
   * Overrides toString in class Object. Converts this difficulty into a string formatted for a .sr
   * file.
   *
   * @return the difficulty as a single string
   */
  @Override
  public String toString() {
    return (this == CardDifficulty.EASY) ? "-" : "*";
  }
}
