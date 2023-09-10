package cs3500.pa05.view;

/**
 * The interface Journal view item.
 *
 * @param <T> the type parameter for the creation input
 * @param <R> the type parameter for the creation output
 */
@FunctionalInterface
public interface JournalViewItem<T, R> {

  /**
   * Create type R item based on T.
   *
   * @param item the item
   * @return the type R view item
   */
  R create(T item);
}
