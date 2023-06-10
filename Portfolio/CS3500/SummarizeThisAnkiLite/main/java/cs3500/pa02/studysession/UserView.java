package cs3500.pa02.studysession;

/**
 * Represents the user's view during the study session.
 */
public interface UserView {


  /**
   * Show setup instructions.
   */
  void showSetup();

  /**
   * Show question.
   *
   * @param question the question
   */
  void showQuestion(CharSequence question);

  /**
   * Show answer.
   *
   * @param answer the answer
   */
  void showAnswer(CharSequence answer);

  /**
   * Show options for the user while on a card.
   */
  void showOptions();

  /**
   * Show stats for the session.
   *
   * @param stats the stats
   */
  void showStats(CharSequence stats);

  /**
   * Show message.
   *
   * @param message the message
   */
  void showMessage(CharSequence message);
}
