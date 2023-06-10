package cs3500.pa02.studysession;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Represents a card in a set with a question, answer, and difficulty.
 */
public class Card {

  /**
   * The Question on this card.
   */
  private final String question;
  /**
   * The Answer on this card.
   */
  private final String answer;
  /**
   * The Difficulty of this card.
   */
  private final AtomicReference<CardDifficulty> difficulty = new AtomicReference<>();

  /**
   * Instantiates a new Card with the default difficulty of hard. Used when reading from a .md
   * file.
   *
   * @param question the question on the card
   * @param answer   the answer to the question on the card
   */
  public Card(String question, String answer) {
    this(question, answer, CardDifficulty.HARD);
  }

  /**
   * Instantiates a new Card using a given difficulty. Used when reading from a .sr file.
   *
   * @param question   the question on the card
   * @param answer     the answer to the question on the card
   * @param difficulty the difficulty of the question
   */
  public Card(String question, String answer, String difficulty) {
    this(question, answer,
        CardDifficulty.EASY.toString().equals(difficulty.strip()) ? CardDifficulty.EASY
            : CardDifficulty.HARD);
  }

  /**
   * Instantiates a new Card using the question, answer, and difficulty.
   *
   * @param question   the question
   * @param answer     the answer
   * @param difficulty the difficulty
   */
  public Card(String question, String answer, CardDifficulty difficulty) {
    this.question = Objects.requireNonNull(question.strip());
    this.answer = Objects.requireNonNull(answer.strip());
    this.difficulty.set(Objects.requireNonNull(difficulty));
  }

  /**
   * Overrides toString in class Object. Converts this card and all of its contents into a single
   * string and returns that string.
   *
   * @return the difficulty, question, ans wer of this card as a single String
   */
  @Override
  public String toString() {
    return String.valueOf(this.difficulty) + ' ' + this.question + " ;;; " + this.answer;
  }

  /**
   * Gets difficulty.
   *
   * @return the difficulty
   */
  public CardDifficulty getDifficulty() {
    return this.difficulty.get();
  }

  /**
   * Sets difficulty.
   *
   * @param difficulty the difficulty
   */
  public void setDifficulty(CardDifficulty difficulty) {
    this.difficulty.set(difficulty);
  }

  /**
   * Gets this card's question.
   *
   * @return the question
   */
  public String getQuestion() {
    return this.question;
  }

  /**
   * Gets this card's answer.
   *
   * @return the answer
   */
  public String getAnswer() {
    return this.answer;
  }
}
