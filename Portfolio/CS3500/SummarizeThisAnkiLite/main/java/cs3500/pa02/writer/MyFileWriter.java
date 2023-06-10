package cs3500.pa02.writer;

import java.nio.file.Path;

/**
 * Represents a file writer to type T.
 *
 * @param <T> the type parameter
 */
@FunctionalInterface
public interface MyFileWriter<T> {

  /**
   * Writes from given Type object to the write path.
   *
   * @param writePath the write path to write to
   * @param type      the type to write from
   */
  void writeToFile(Path writePath, T type);
}
