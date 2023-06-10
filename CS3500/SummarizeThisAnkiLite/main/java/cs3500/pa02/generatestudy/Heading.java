package cs3500.pa02.generatestudy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Represents a Markdown heading element of any level denoted by #s
 */
public class Heading extends AbstractMarkdownElement {

  /**
   * Represents the "important" nested under this heading
   */
  private final Collection<Important> contents = new ArrayList<>();

  /**
   * Instantiates a new Heading element
   *
   * @param value the name/title of this heading
   * @param list  the list of "important" nested under this heading
   */
  public Heading(String value, Collection<? extends Important> list) {
    super(value);
    this.contents.addAll(list);
  }

  /**
   * Overrides toString in class Object. Converts this Heading and all of its contents into a single
   * string and returns that string
   *
   * @return the name and list of important of this heading as a single String
   */
  @Override
  public String toString() {
    String result;
    if (this.contents.isEmpty()) {
      result = this.value;
    } else {
      result = this.value + '\n' + this.contents.stream().map(Important::toString)
          .collect(Collectors.joining("\n"));
    }
    return result;
  }
}
