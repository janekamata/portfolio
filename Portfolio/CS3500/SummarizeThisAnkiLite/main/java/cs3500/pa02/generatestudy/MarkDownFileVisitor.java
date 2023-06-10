package cs3500.pa02.generatestudy;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a MarkDownFileVisitor used to walk the file system that implements FileVisitor.
 * Accumulates all the .md files visited.
 */
public class MarkDownFileVisitor implements FileVisitor<Path> {

  /**
   * Represents a list of .md files visited by the visitor.
   */
  private final List<File> mdFiles;

  /**
   * Instantiates a new Markdown file visitor.
   */
  public MarkDownFileVisitor() {
    this.mdFiles = new ArrayList<>();
  }

  /**
   * Invoked for a directory before entries in the directory are visited.
   *
   * @param dir   a reference to the directory
   * @param attrs the directory's basic attributes
   * @return CONTINUE to continue walking the file system
   */
  @Override
  public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
    return FileVisitResult.CONTINUE;
  }

  /**
   * Adds the path of a file to the list if it is a .md file that is a regular, readable file.
   *
   * @param file  a reference to the file
   * @param attrs the file's basic attributes
   * @return CONTINUE to continue walking the file system
   */
  @Override
  public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
    if (file.toString().endsWith(".md")) {
      if (attrs.isRegularFile() && Files.isReadable(file) && !this.mdFiles.contains(
          file.toFile())) {
        this.mdFiles.add(file.toFile());
      }
    }
    return FileVisitResult.CONTINUE;
  }

  /**
   * Invoked for a file that couldn't be visited.
   *
   * @param file a reference to the file
   * @param exc  the I/O exception that prevented the file from being visited
   * @return CONTINUE to continue walking the file system
   */
  @Override
  public FileVisitResult visitFileFailed(Path file, IOException exc) {
    if (exc != null) {
      System.err.println(
          "Visit file failed for the file: " + exc.getMessage());
    }
    return FileVisitResult.CONTINUE;
  }

  /**
   * Invoked for a directory after entries in the directory have been visited.
   *
   * @param dir a reference to the directory
   * @param exc {@code null} if the iteration of the directory completes without an error; otherwise
   *            the I/O exception that caused the iteration of the directory to complete
   *            prematurely
   * @return CONTINUE to continue walking the file system
   */
  @Override
  public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
    return FileVisitResult.CONTINUE;
  }

  /**
   * Gets all the .md files found while walking the file system. Checks if the given readPath is a
   * directory.
   *
   * @param readPath the read path
   * @return the list of .md files found while walking the file system
   */
  public List<File> getFiles(Path readPath) {
    try {
      // Checks that the read path is a directory
      if (!Files.isDirectory(readPath)) {
        throw new IllegalArgumentException("First argument must be the path to a directory.");
      }
      Files.walkFileTree(readPath, this);
    } catch (IOException e) {
      throw new RuntimeException("Caught IOException: " + e.getMessage());
    }
    return Collections.unmodifiableList(this.mdFiles);
  }
}
