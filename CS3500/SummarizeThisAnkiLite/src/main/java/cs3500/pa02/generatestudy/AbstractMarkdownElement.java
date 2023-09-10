package cs3500.pa02.generatestudy;

/**
 * Represents an abstract mark down element.
 */
public abstract class AbstractMarkdownElement implements MarkdownElement {

  /**
   * Represents the value/name/title of an element
   */
  protected final String value;

  /**
   * Instantiates a new Abstract mark down element.
   *
   * @param value the value/name/title of an element
   */
  AbstractMarkdownElement(String value) {
    this.value = value;
  }

  /**
   * Overrides toString in class Object. Converts this markdown element into a single string and
   * returns that string
   *
   * @return the contents of this markdown element as a single String
   */
  @Override
  public abstract String toString();
}
