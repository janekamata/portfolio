package cs3500.pa02.reader;

import cs3500.pa02.generatestudy.Heading;
import cs3500.pa02.generatestudy.Important;
import cs3500.pa02.generatestudy.StudyGuide;
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
 * Represents a file reader that reads a file and adds the contents to the study guide
 */
public class ToStudyGuideReader implements MyFileReader<StudyGuide> {

  /**
   * The constant HEADING_PATTERN.
   */
  private static final Pattern HEADING_PATTERN = Pattern.compile("^#{1,4}\\s+(.*)");
  /**
   * The constant IMPORTANT_PATTERN.
   */
  private static final Pattern IMPORTANT_PATTERN = Pattern.compile("\\[\\[(?![^\\]]*:::)(.*?)]]");

  /**
   * Determines if the given line is a heading. Used as a helper for readFileToStudyGuide.
   *
   * @param line the line that should be checked if it's a heading
   * @return if the line is a heading
   */
  private static boolean isHeading(CharSequence line) {
    Matcher matcher = ToStudyGuideReader.HEADING_PATTERN.matcher(line);
    return matcher.matches();
  }

  /**
   * Extracts all the important information from a given String where important information is
   * contained between "[[" and "]]". There can be multiple pieces of important information in a
   * single line. Acts as a helper for updateSummaries.
   *
   * @param line String that contains important information that needs to be extracted
   * @return list of all information contained between all sets of [[]] in the given line
   */
  private static List<Important> extractImportant(CharSequence line) {
    Matcher matcher = ToStudyGuideReader.IMPORTANT_PATTERN.matcher(line);
    List<Important> importantList = new ArrayList<>();
    while (matcher.find()) {
      importantList.add(new Important(matcher.group(1)));
    }
    return importantList;
  }

  /**
   * Processes a heading by updating the study guide with its heading and associated important
   * information.
   *
   * @param studyGuide the study guide to be updated
   * @param heading    the heading text
   * @param bullets    the bullets associated with the given heading
   */
  private static void processHeading(StudyGuide studyGuide, String heading, CharSequence bullets) {
    if (heading != null) {
      List<Important> importantList = ToStudyGuideReader.extractImportant(bullets);
      studyGuide.updateSummaries(new Heading(heading, importantList));
    }
  }

  /**
   * Reads the contents of the given file into the given study guide if the given file. Creates
   * Heading and Important nested under headings based on the contents of the file.
   *
   * @param file           the file to be read into the study guide
   * @param typeToReadInto the study guide to be written to
   */
  @Override
  public void readFileTo(File file, StudyGuide typeToReadInto) {
    try (BufferedReader bufferedReader = new BufferedReader(
        new FileReader(file, StandardCharsets.UTF_8))) {
      String line = bufferedReader.readLine();
      String tempHeading = null;
      StringBuilder tempBullets = new StringBuilder();
      while (line != null) {
        if (ToStudyGuideReader.isHeading(line)) {
          ToStudyGuideReader.processHeading(typeToReadInto, tempHeading, tempBullets.toString());
          tempHeading = line;
          tempBullets.setLength(0);
        } else if (line.startsWith("- ") || line.isEmpty()) {
          tempBullets.append(line.trim());
        } else if (!tempBullets.isEmpty()) {
          tempBullets.append(' ').append(line.trim());
        }
        line = bufferedReader.readLine();
      }
      ToStudyGuideReader.processHeading(typeToReadInto, tempHeading, tempBullets.toString());
    } catch (IOException e) {
      throw new RuntimeException("Caught IOException: " + e.getMessage());
    }
  }
}
