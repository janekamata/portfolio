package cs3500.pa02.generatestudy;

/**
 * Represents a markdown element "important" show in [[...]] and nested under a heading
 */
public class Important extends AbstractMarkdownElement {

  /**
   * Instantiates a new Important element
   *
   * @param value the contents of the [[]] in the original markdown file
   */
  public Important(String value) {
    super(value);
  }

  /**
   * Overrides toString in class Object. Converts this Important into a single string and returns
   * that string
   *
   * @return the contents of this Important as a single String
   */
  @Override
  public String toString() {
    return "- " + this.value;
  }
}
