package cs3500.pa02.controller;

import cs3500.pa02.reader.ToStudySetReader;
import cs3500.pa02.studysession.Card;
import cs3500.pa02.studysession.CardDifficulty;
import cs3500.pa02.studysession.SessionOptions;
import cs3500.pa02.studysession.SessionStats;
import cs3500.pa02.studysession.SessionUserView;
import cs3500.pa02.studysession.StudySet;
import cs3500.pa02.studysession.UserView;
import cs3500.pa02.writer.MyFileWriterImpl;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * Represents a controller of the data flow between the study set, user input, and output stream.
 */
public class StudySession implements MyController {

  /**
   * The constant SR_FILE_EXTENSION.
   */
  private static final String SR_FILE_EXTENSION = ".sr";
  /**
   * The Scanner for input.
   */
  private final BufferedReader scanner;
  /**
   * The question bank.
   */
  private final StudySet questionBank;
  /**
   * The stats for the session.
   */
  private final SessionStats stats;
  /**
   * The view of the user.
   */
  private final UserView userView;
  /**
   * The File path to read from and write to.
   */
  private final File filePath;

  /**
   * Instantiates a new Study session with a given input, output, and error output.
   *
   * @param input  the in Readable representing input
   * @param output the out Appendable representing output
   * @param seed   the seed used to shuffle the cards
   */
  private StudySession(InputStream input, Appendable output, long seed) {
    this.scanner = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
    this.userView = new SessionUserView(output);
    this.filePath = this.runSetupPath();
    this.questionBank = this.generateStudySet(seed);
    this.stats = this.runSetupSet();
  }

  /**
   * Instantiates a new Study session with standard input and output streams.
   *
   * @param seed the seed used to shuffle the cards
   */
  public StudySession(long seed) {
    this(System.in,
        new PrintStream(System.out, true, StandardCharsets.UTF_8), seed);
  }

  /**
   * Instantiates a new Study session using the current time as the seed.
   */
  public StudySession() {
    this(System.currentTimeMillis());
  }

  /**
   * Determines if the given user input for the question option is a valid option they can select.
   *
   * @param optionInt the int input from the user
   * @return the boolean representing if the given input is a valid option
   */
  private static boolean isValidOption(int optionInt) {
    return optionInt >= 0 && optionInt < SessionOptions.values().length;
  }

  /**
   * Runs the setup  of the .sr path based on user input. Asks the user for a path until a usable
   * path is provided.
   *
   * @return the file path for the .sr file to read and write from
   */
  private File runSetupPath() {
    this.updateViewSetup();
    File resultFilePath;
    try {
      resultFilePath = new File(this.scanner.readLine());
      while (!resultFilePath.toString().endsWith(StudySession.SR_FILE_EXTENSION)
          || !resultFilePath.canRead()
          || !resultFilePath.isFile()) {
        this.updateViewMessage("Given path must be to a readable .sr file. Enter a new path: ");
        resultFilePath = new File(this.scanner.readLine());
      }
    } catch (IOException e) {
      throw new RuntimeException("Caught IOException: " + e.getMessage());
    }
    return resultFilePath;
  }

  /**
   * Runs the setup of the session stats using the user input for number of questions. Asks the user
   * for a number until a usable number is provided and then orders the cards to determine the
   * initial session stats.
   *
   * @return the session stats the user starts with
   */
  private SessionStats runSetupSet() {
    int numQuestions;
    try {
      this.updateViewSetup();
      numQuestions = Integer.parseInt(this.scanner.readLine());
      while (numQuestions <= 0) {
        this.updateViewMessage(
            "Given number of questions must be greater than 0. Enter a new number: ");
        numQuestions = Integer.parseInt(this.scanner.readLine());
      }
      this.updateViewMessage("Starting session...");
    } catch (IOException e) {
      throw new RuntimeException("Caught IOException: " + e.getMessage());
    }
    return this.questionBank.setUpStudySet(numQuestions);
  }

  /**
   * Generates the study set from the filePath.
   *
   * @param seed the seed
   * @return the study set generated from the file path
   */
  private StudySet generateStudySet(long seed) {
    StudySet studySet = new StudySet(seed);
    new ToStudySetReader().readFileTo(this.filePath, studySet);
    return studySet;
  }

  /**
   * Updates the user view with the message.
   */
  private void updateViewMessage(String message) {
    this.userView.showMessage(message);
  }

  /**
   * Updates the user view with the question options.
   */
  private void updateViewOptions() {
    this.userView.showOptions();
  }

  /**
   * Updates the user view with the setup instructions.
   */
  private void updateViewSetup() {
    this.userView.showSetup();
  }

  /**
   * Updates the user view with the session stats.
   */
  private void updateViewStats() {
    this.userView.showStats(this.stats.toString());
  }

  /**
   * Determines if the question flow should be exited based on user input.
   *
   * @param card the card the user is currently on
   * @return the boolean representing if the flow should be exited
   */
  private boolean shouldExit(Card card) {
    boolean shouldContinue = true;
    boolean shouldExit = false;
    while (shouldContinue) {
      this.updateViewOptions();
      int optionInt;
      try {
        optionInt = Integer.parseInt(this.scanner.readLine()) - 1;
      } catch (IOException e) {
        throw new RuntimeException("Caught IOException: " + e.getMessage());
      }
      if (optionInt == SessionOptions.SAVE_AND_EXIT_SESSION.ordinal()) {
        this.stats.incrementAnswered();
        this.updateViewMessage("Exiting session...");
        shouldContinue = false;
        shouldExit = true;
      } else if (StudySession.isValidOption(optionInt)) {
        shouldContinue = !this.hasAnsweredQuestion(card, optionInt);
      } else {
        this.updateViewMessage(
            "Given option number must be between 1-" + SessionOptions.values().length
                + ". Enter a new number.");
      }
    }
    return shouldExit;
  }

  /**
   * Determines if the user has seen the answer for a question based on the option input.
   *
   * @param card      the card the user is currently on
   * @param optionInt the option int input from the user
   * @return the boolean representing if the question's answer has been seen
   */
  private boolean hasAnsweredQuestion(Card card, int optionInt) {
    SessionOptions selectedOption = SessionOptions.values()[optionInt];
    boolean hasAnswered = false;
    if (selectedOption == SessionOptions.MARK_EASY) {
      this.userView.showMessage(
          this.stats.changeDifficulty(card.getDifficulty(), CardDifficulty.EASY));
      card.setDifficulty(CardDifficulty.EASY);
    } else if (selectedOption == SessionOptions.MARK_HARD) {
      this.userView.showMessage(
          this.stats.changeDifficulty(card.getDifficulty(), CardDifficulty.HARD));
      card.setDifficulty(CardDifficulty.HARD);
    } else {
      this.stats.incrementAnswered();
      this.userView.showAnswer(card.getAnswer());
      hasAnswered = true;
    }
    return hasAnswered;
  }

  /**
   * Runs the functionality of the application and starts the data flow. Iterates through cards
   * until the user wants to exit or the user input number of questions has been met. Updates the
   * view to show stats and then writes the updates .sr upon the exit of the question flow.
   */
  @Override
  public void run() {
    this.iterateCards();
    this.updateViewStats();
    this.handleFileWriting();
  }

  /**
   * Handles file writing from a question bank to file path.
   */
  private void handleFileWriting() {
    new MyFileWriterImpl<>().writeToFile(this.filePath.toPath(), this.questionBank);
  }


  /**
   * Iterates through cards. Controls the question flow to show users a question, give them options,
   * take action based on those options, and determine if the program's question flow should be
   * exited.
   */
  private void iterateCards() {
    Card currentCard = this.questionBank.nextCard();
    boolean shouldExit = false;
    while (!shouldExit && (currentCard != null)) {
      this.userView.showQuestion(currentCard.getQuestion());
      shouldExit = this.shouldExit(currentCard);
      if (!shouldExit) {
        currentCard = this.questionBank.nextCard();
      }
    }
  }

}
