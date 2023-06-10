package cs3500.pa02.reader;

import cs3500.pa02.studysession.Card;
import cs3500.pa02.studysession.StudySet;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a file reader that reads a file into a study set.
 */
public class ToStudySetReader implements MyFileReader<StudySet> {

  /**
   * The constant CARD_PATTERN_MD.
   */
  private static final Pattern CARD_PATTERN_MD = Pattern.compile(
      "^- \\[\\[(\\s*)(.*?)\\s*:::\\s*(.*?)]]$");
  /**
   * The constant CARD_PATTERN_SR.
   */
  private static final Pattern CARD_PATTERN_SR = Pattern.compile(
      "^([*-] )?(.*?)(?:;;;)([\\s\\S]+)$");

  /**
   * The constant FILE_EXTENSION_SR.
   */
  private static final String FILE_EXTENSION_SR = ".sr";

  /**
   * Determines card pattern to use based on file path.
   *
   * @param file the file path
   * @return the pattern to use for a card
   */
  private static Pattern determineCardPattern(File file) {
    return file.toString().endsWith(ToStudySetReader.FILE_EXTENSION_SR)
        ? ToStudySetReader.CARD_PATTERN_SR
        : ToStudySetReader.CARD_PATTERN_MD;
  }

  /**
   * Extracts all the cards from the given line using a pattern based on the file type. Returns a
   * list of cards with their respective question and answer.
   *
   * @param line        the line that could contain cards
   * @param cardPattern the card pattern used to find cards
   * @return list representing the cards in the given line
   */
  private static List<Card> extractCard(CharSequence line, Pattern cardPattern) {
    Matcher matcher = cardPattern.matcher(line);
    List<Card> cardList = new ArrayList<>();
    while (matcher.find()) {
      cardList.add(new Card(matcher.group(2), matcher.group(3), matcher.group(1)));
    }
    return cardList;
  }

  /**
   * Reads the given file into a given study set. Depending on the file type, the method utilizes
   * different patterns to identify cards. An IO exception is thrown by the FileReader if an error
   * occurs when reading.
   *
   * @param file           the file to be read from
   * @param typeToReadInto the study set to be read into
   */
  @Override
  public void readFileTo(File file, StudySet typeToReadInto) {
    Pattern cardPattern = ToStudySetReader.determineCardPattern(file);
    try (BufferedReader bufferedReader = new BufferedReader(
        new FileReader(file, StandardCharsets.UTF_8))) {
      String line = bufferedReader.readLine();
      StringBuilder tempLine = new StringBuilder();
      while (line != null) {
        if ((line.startsWith("- ") || line.isEmpty() || file.toString().endsWith(".sr"))) {
          if (!tempLine.isEmpty()) {
            typeToReadInto.updateCards(
                ToStudySetReader.extractCard(tempLine.toString(), cardPattern));
          }
          tempLine.setLength(0);
          tempLine.append(line.trim());
        } else if (!tempLine.isEmpty()) {
          tempLine.append(' ').append(line.trim());
        }
        line = bufferedReader.readLine();
      }
      typeToReadInto.updateCards(ToStudySetReader.extractCard(tempLine.toString(), cardPattern));
    } catch (IOException e) {
      throw new RuntimeException("Caught IOException: " + e.getMessage());
    }
  }
}
