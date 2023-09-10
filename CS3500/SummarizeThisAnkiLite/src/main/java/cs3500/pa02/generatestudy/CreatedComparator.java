package cs3500.pa02.generatestudy;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.util.Comparator;

/**
 * Represents a file comparator that imposes a total order on files based on the create-date time
 * stamp of the source file.
 */
public class CreatedComparator implements Comparator<File>, Serializable {

  /**
   * Compares two paths by create-date time stamp.
   *
   * @param f1 the first file to be compared.
   * @param f2 the second file to be compared.
   * @return a negative integer, zero, or a positive integer as the first file's creationTime is
   *     less than, equal to, or greater than the second file's
   */
  @Override
  public int compare(File f1, File f2) {
    int result;
    try {
      FileTime p1Time = (FileTime) Files.getAttribute(f1.toPath(), "creationTime");
      FileTime p2Time = (FileTime) Files.getAttribute(f2.toPath(), "creationTime");
      result = p1Time.compareTo(p2Time);
    } catch (IOException e) {
      throw new RuntimeException("Caught IOException: " + e.getMessage());
    }
    return result;
  }
}
