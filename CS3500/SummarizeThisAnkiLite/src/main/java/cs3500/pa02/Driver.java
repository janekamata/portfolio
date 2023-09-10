package cs3500.pa02;


import cs3500.pa02.controller.GenerateStudy;
import cs3500.pa02.controller.StudySession;

/**
 * This is the main driver of this project.
 */
public class Driver {

  /**
   * The entry point of application. Decides which controller the application should use based on
   * the number of arguments the user starts the program with.
   *
   * @param args the input arguments
   */
  public static void main(String[] args) {
    // Checks that there are three or zero arguments
    if (args.length == 3) {
      new GenerateStudy(args).run();
    } else if (args.length == 0) {
      new StudySession().run();
    } else {
      throw new IllegalArgumentException(
          "There are " + args.length + " arguments, but there should only be either 0 or 3.");
    }
  }
}