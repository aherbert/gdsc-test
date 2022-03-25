<package>

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Represents a logical {@code <Op>} of two predicates, each predicate testing the two input values.
 *
 * \<p>Supports a description {@link Supplier} for a {@link String}.
 *
 * @see java.util.function.BiPredicate
 * @since 1.0
 */
public class <Type1><Type2><Op>BiPredicate
    implements <Type1><Type2>BiPredicate, Supplier\<String> {

  /** The first predicate. */
  private final <Type1><Type2>BiPredicate predicate1;
  /** The second predicate. */
  private final <Type1><Type2>BiPredicate predicate2;

  /**
   * Create a new combined {@code <Op>} predicate.
   *
   * @param predicate1 the first predicate
   * @param predicate2 the second predicate
   */
  public <Type1><Type2><Op>BiPredicate(<Type1><Type2>BiPredicate predicate1,
      <Type1><Type2>BiPredicate predicate2) {
    this.predicate1 = Objects.requireNonNull(predicate1, "Predicate 1 is null");
    this.predicate2 = Objects.requireNonNull(predicate2, "Predicate 2 is null");
  }

  /**
   * {@inheritDoc}
   *
   * \<p>Test the logical {@code <Op>} combination of the two predicates.
   */
  @Override
  public boolean test(<type1> value1, <type2> value2) {
    return predicate1.test(value1, value2) <OP> predicate2.test(value1, value2);
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
