package cs3500.pa02.writer;

import cs3500.pa02.generatestudy.StudyGuide;
import cs3500.pa02.studysession.StudySet;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Represents a file writer that writes the study guide or study set to the path
 *
 * @param <T> the type parameter
 */
public class MyFileWriterImpl<T> implements MyFileWriter<T> {

  /**
   * Writes the type's contents as a String to the file writePath. If the file does not exist, then
   * a new file is created.
   *
   * @param writePath the writePath the type should be written to
   * @param type      the type of the object that needs to be written to the file
   */
  @Override
  public void writeToFile(Path writePath, T type) {
    this.validateWritePath(writePath, type);
    try (BufferedWriter bufferedWriter = Files.newBufferedWriter(writePath,
        StandardCharsets.UTF_8)) {
      bufferedWriter.write(type.toString());
    } catch (IOException e) {
      System.err.println("Caught IOException: " + e.getMessage());
    }
  }

  /**
   * Validates the write path. Checks if the writePath is writable and regular. Also checks if it
   * plans to be the correct file type.
   *
   * @param writePath the write path the type should be written to
   * @param type      the type of the object that needs to be written to the file
   */
  private void validateWritePath(Path writePath, T type) {
    // checks that the write path is writable
    if (Files.exists(writePath) && (!Files.isWritable(writePath) || !Files.isRegularFile(
        writePath))) {
      throw new IllegalArgumentException("Write path must be a regular, writable file.");
    }
    String expectedExtension = this.getExpectedExtension(type);
    if (!writePath.toString().endsWith(expectedExtension)) {
      throw new IllegalArgumentException(
          "Write path must be to a " + expectedExtension + " file.");
    }
  }


  /**
   * Gets expected extension based on the type of the object being written from.
   *
   * @param type the type of the object that needs to be written to the file
   * @return the expected extension for the type
   */
  private String getExpectedExtension(T type) {
    String expectedExtension;
    if (type instanceof StudyGuide) {
      expectedExtension = ".md";
    } else if (type instanceof StudySet) {
      expectedExtension = ".sr";
    } else {
      throw new IllegalArgumentException("Write path must be to a supported file type.");
    }
    return expectedExtension;
  }

}
