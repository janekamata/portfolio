package cs3500.pa02.controller;

import cs3500.pa02.generatestudy.MarkDownFileVisitor;
import cs3500.pa02.generatestudy.SortOptions;
import cs3500.pa02.generatestudy.StudyGuide;
import cs3500.pa02.reader.MyFileReader;
import cs3500.pa02.reader.ToStudyGuideReader;
import cs3500.pa02.reader.ToStudySetReader;
import cs3500.pa02.studysession.StudySet;
import cs3500.pa02.writer.MyFileWriterImpl;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a controller of the data flow between the inputs, study guide, and study set.
 */
public class GenerateStudy implements MyController {

  /**
   * The .md files found while walking the file system.
   */
  private final List<File> files;
  /**
   * The write path for the study guide.
   */
  private final Path writePath;

  /**
   * Instantiates a new controller.
   *
   * @param commandLineArguments the args where the first is the read path, the second is the flag,
   *                             and the third is the write path
   */
  public GenerateStudy(String[] commandLineArguments) {
    this.writePath = Paths.get(commandLineArguments[2]);
    this.files = GenerateStudy.getSortedFiles(commandLineArguments[0], commandLineArguments[1]);
  }

  /**
   * Gets sorted files.
   *
   * @param readPath       the read path
   * @param sortOptionFlag the sort option flag
   * @return the sorted files
   */
  private static List<File> getSortedFiles(String readPath, String sortOptionFlag) {
    Path path = Paths.get(readPath);
    List<File> files = new ArrayList<>(new MarkDownFileVisitor().getFiles(path));
    files.sort(SortOptions.valueOf(sortOptionFlag.toUpperCase()).getFileComparator());
    return files;
  }

  /**
   * Process files.
   *
   * @param <T>    the type parameter for the reader
   * @param reader the reader for the specific type
   * @param type   the type of the object to be read into
   */
  private <T> void processFiles(MyFileReader<T> reader, T type) {
    for (File markdownFile : this.files) {
      reader.readFileTo(markdownFile, type);
    }
  }

  /**
   * Generates a new study guide.
   *
   * @return the study guide generated from the list of .md files
   */
  private StudyGuide generateStudyGuide() {
    StudyGuide studyGuide = new StudyGuide();
    MyFileReader<StudyGuide> reader = new ToStudyGuideReader();
    this.processFiles(reader, studyGuide);
    return studyGuide;
  }

  /**
   * Generates a new study set.
   *
   * @return the study set generated from the list of .md files
   */
  private StudySet generateStudySet() {
    StudySet studySet = new StudySet();
    MyFileReader<StudySet> reader = new ToStudySetReader();
    this.processFiles(reader, studySet);
    return studySet;
  }

  /**
   * Runs the functionality of the application and starts the data flow to read files, sort them,
   * extract contents, and write the contents.
   */
  @Override
  public void run() {
    StudyGuide studyGuide = this.generateStudyGuide();
    StudySet studySet = this.generateStudySet();
    this.writeStudyGuide(studyGuide);
    this.writeStudySet(studySet);
  }

  /**
   * Write study guide to write path.
   *
   * @param studyGuide the study guide to be written
   */
  private void writeStudyGuide(StudyGuide studyGuide) {
    new MyFileWriterImpl<StudyGuide>().writeToFile(this.writePath, studyGuide);
  }

  /**
   * Writes study set to write path. Changes the write path ending to .sr.
   *
   * @param studySet the study set to be written
   */
  private void writeStudySet(StudySet studySet) {
    String srWritePath = this.writePath.toAbsolutePath().toString().split("\\.")[0] + ".sr";
    new MyFileWriterImpl<StudySet>().writeToFile(Paths.get(srWritePath), studySet);
  }
}

