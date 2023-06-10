package cs3500.pa02.studysession;

/**
 * Represents the choices available to users while answering questions.
 */
public enum SessionOptions {
  /**
   * Mark easy session option.
   */
  MARK_EASY,
  /**
   * Mark hard session option.
   */
  MARK_HARD,
  /**
   * See answer session option.
   */
  SEE_ANSWER,
  /**
   * Save and exit session session option.
   */
  SAVE_AND_EXIT_SESSION;

  /**
   * To return a string representing the session option.
   *
   * @return the string representing the option
   */
  @Override
  public String toString() {
    return switch (this) {
      case MARK_EASY -> "Mark easy";
      case MARK_HARD -> "Mark hard";
      case SEE_ANSWER -> "See answer";
      default -> "Save and exit session";
    };
  }
}
