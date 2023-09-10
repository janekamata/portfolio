package cs3500.pa05.model.enums;

/**
 * Represents a completion status type enum.
 */
public enum CompletionStatus {
  /**
   * Complete completion status.
   */
  COMPLETE("Complete"),
  /**
   * Incomplete completion status.
   */
  INCOMPLETE("Incomplete"),

  /**
   * None completion status.
   */
  NONE("None");

  /**
   * The status as it is shown in the view.
   */
  private final String status;

  /**
   * Instantiates a new Completion status.
   *
   * @param status the status
   */
  CompletionStatus(String status) {
    this.status = status;
  }

  /**
   * Returns a string representing this status enum in the view.
   *
   * @return the string representing this enum in the view
   */
  @Override
  public String toString() {
    return this.status;
  }
}
