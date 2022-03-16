<package>

import uk.ac.sussex.gdsc.test.api.Strings;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Represents a logical {@code <Op>} of two predicates, each predicate testing the input value.
 *
 * \<p>Supports a description {@link Supplier} for a {@link String}.
 *
 * @see Predicate
 * @since 1.0
 */
public class <Type><Op>Predicate implements <Type>Predicate, Supplier\<String> {

  /** The first predicate. */
  private final <Type>Predicate predicate1;
  /** The second predicate. */
  private final <Type>Predicate predicate2;

  /**
   * Create a new combined {@code <Op>} predicate.
   *
   * @param predicate1 The first predicate
   * @param predicate2 The second predicate
   */
  public <Type><Op>Predicate(<Type>Predicate predicate1, <Type>Predicate predicate2) {
    this.predicate1 = Objects.requireNonNull(predicate1, "Predicate 1 is null");
    this.predicate2 = Objects.requireNonNull(predicate2, "Predicate 2 is null");
  }

  /**
   * {@inheritDoc}
   *
   * \<p>Test the logical {@code <Op>} combination of the two predicates.
   */
  @Override
  public boolean test(<type> value) {
    return predicate1.test(value) <OP> predicate2.test(value);
  }

  /**
   * {@inheritDoc}
   *
   * \<p>Gets the logical {@code <Op>} combination of the string representation of two predicates.
   */
  @Override
  public String get() {
    return Strings.<op>ToString(predicate1, predicate2);
  }
}
