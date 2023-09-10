package cs3500.pa05.controller.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.pa05.model.JournalInteractable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Represents a file writer.
 */
public class MyFileWriter {

  /**
   * Write from journal to file.
   *
   * @param journal  the journal
   * @param filePath the file path
   */
  public static void writeFromJournalToFile(JournalInteractable journal, String filePath) {
    MyFileWriter.validateWritePath(Path.of(filePath));
    try {
      ObjectMapper mapper = new ObjectMapper();
      System.out.println("writing from file writer");
      mapper.writeValue(new File(filePath), journal);
    } catch (IOException e) {
      throw new IllegalStateException("Caught IOException: " + e.getMessage());
    }
  }

  /**
   * Validate write path.
   *
   * @param filePath the file path
   */
  private static void validateWritePath(Path filePath) {
    if (Files.exists(filePath) && (!Files.isWritable(filePath) || !Files.isRegularFile(
        filePath))) {
      throw new IllegalArgumentException("Write path must be a regular, writable file.");
    }
    if (!String.valueOf(filePath).endsWith(".bujo")) {
      throw new IllegalArgumentException(
          "Write path must be to a .bujo file.");
    }
  }
}
