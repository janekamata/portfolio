package cs3500.pa02.studysession;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Represents the user's view using appendable during the study session.
 */
public class SessionUserView implements UserView {

  /**
   * The Set-up iterator.
   */
  private final Iterator<String> setUpIterator = List.of(
      """

          -----------------------------------------
          Hello, welcome to your study session! Provide a SR question bank file path:\s""",
      "How many questions would you like to practice today? ").iterator();
  /**
   * The Appendable.
   */
  private final Appendable appendable;

  /**
   * Instantiates a new Session view.
   *
   * @param appendable the appendable
   */
  public SessionUserView(Appendable appendable) {
    this.appendable = appendable;
  }

  /**
   * Prints setup instructions by iterating through a set list.
   */
  @Override
  public void showSetup() {
    if (this.setUpIterator.hasNext()) {
      try {
        this.appendable.append(this.setUpIterator.next());
      } catch (IOException e) {
        throw new RuntimeException("Caught IOException: " + e.getMessage());
      }
    }
  }

  /**
   * Prints a given question formatted.
   *
   * @param question the question to be output
   */
  @Override
  public void showQuestion(CharSequence question) {
    try {
      this.appendable.append("\n-----------------------------------------").append("\nQuestion: ")
          .append(question).append("\n\n");
    } catch (IOException e) {
      throw new RuntimeException("Caught IOException: " + e.getMessage());
    }
  }

  /**
   * Prints a given answer formatted.
   *
   * @param answer the answer to be output
   */
  @Override
  public void showAnswer(CharSequence answer) {
    try {
      this.appendable.append("\nAnswer: ").append(answer);
    } catch (IOException e) {
      throw new RuntimeException("Caught IOException: " + e.getMessage());
    }
  }

  /**
   * Prints the user's options while on a card.
   */
  @Override
  public void showOptions() {
    try {
      this.appendable.append(
          "Select an option").append(
          " (1 - Mark easy  |  2 - Mark hard  |  3 - See answer  |  4 - Save and exit): ");
    } catch (IOException e) {
      throw new RuntimeException("Caught IOException: " + e.getMessage());
    }
  }

  /**
   * Prints the stats of a session.
   *
   * @param stats the stats to be output
   */
  @Override
  public void showStats(CharSequence stats) {
    try {
      this.appendable.append("\n-----------------------------------------").append('\n')
          .append(stats).append("\n-----------------------------------------\n");
    } catch (IOException e) {
      throw new RuntimeException("Caught IOException: " + e.getMessage());
    }
  }

  /**
   * Print message to the out appendable.
   *
   * @param message the message
   */
  @Override
  public void showMessage(CharSequence message) {
    try {
      this.appendable.append(message).append('\n');
    } catch (IOException e) {
      throw new RuntimeException("Caught IOException: " + e.getMessage());
    }
  }
}
