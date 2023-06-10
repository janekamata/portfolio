package cs3500.pa02.reader;

import java.io.File;

/**
 * Represents a file reader to type T.
 *
 * @param <T> the type parameter
 */
@FunctionalInterface
public interface MyFileReader<T> {

  /**
   * Reads a file into the given Type object.
   *
   * @param file           the file path to read from
   * @param typeToReadInto the type to read into
   */
  void readFileTo(File file, T typeToReadInto);
}
