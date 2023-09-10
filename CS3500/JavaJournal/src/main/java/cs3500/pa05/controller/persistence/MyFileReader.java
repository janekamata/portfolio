package cs3500.pa05.controller.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.pa05.model.JournalImpl;
import cs3500.pa05.model.JournalInteractable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Represents a file reader to a journa.
 */
public class MyFileReader {

  /**
   * Read from file to journal.
   *
   * @param filePath the file path
   * @return the journal
   */
  public static JournalInteractable readFromFileToJournal(String filePath) {
    MyFileReader.validateReadPath(Path.of(filePath));
    try {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.readValue(new File(filePath), JournalImpl.class);
    } catch (IOException e) {
      throw new IllegalStateException("Caught IOException: " + e.getMessage());
    }
  }

  /**
   * Validate read path.
   *
   * @param filePath the file path
   */
  private static void validateReadPath(Path filePath) {
    if (!filePath.toString().endsWith(".bujo") || !Files.isReadable(filePath)) {
      System.out.println(filePath.endsWith(".bujo"));
      throw new IllegalArgumentException("Given filepath is not a readable .bujo file.");
    }
  }
}
