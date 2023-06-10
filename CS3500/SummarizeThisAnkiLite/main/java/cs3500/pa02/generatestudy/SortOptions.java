package cs3500.pa02.generatestudy;

import java.io.File;
import java.util.Comparator;

/**
 * Represents sort options for the study guide.
 */
public enum SortOptions {
  /**
   * Filename sort option flag.
   */
  FILENAME,
  /**
   * Created sort option flag.
   */
  CREATED,
  /**
   * Modified sort option flag.
   */
  MODIFIED;

  /**
   * Returns the appropriate file comparator based on the sort option flag. Checks that the given
   * flag is either filename, created, or modified.
   *
   * @return the file comparator that compares two files based on a set criteria
   */
  public Comparator<File> getFileComparator() {
    Comparator<File> result = new FilenameComparator();
    if (this == SortOptions.FILENAME) {
      result = new FilenameComparator();
    }
    if (this == SortOptions.CREATED) {
      result = new CreatedComparator();
    }
    if (this == SortOptions.MODIFIED) {
      result = new ModifiedComparator();
    }
    return result;
  }
}

