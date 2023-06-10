package cs3500.pa02.generatestudy;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;

/**
 * Represents a file comparator that imposes a total ordering on files based on the name of the
 * source file
 */
public class FilenameComparator implements Comparator<File>, Serializable {


  /**
   * Compares two paths by file name alphabetically.
   *
   * @param f1 the first file to be compared.
   * @param f2 the second file to be compared.
   * @return a negative integer, zero, or a positive integer as the first file's name is less than,
   *     equal to, or greater than the second file's
   */
  @Override
  public int compare(File f1, File f2) {
    return f1.getName().compareTo(f2.getName());
  }
}
