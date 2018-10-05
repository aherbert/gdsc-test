<package>

import uk.ac.sussex.gdsc.test.api.StringUtils;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

/**
 * Represents a logical {@code <Op>} of two predicates including their string representations using
 * {@link Supplier} for a {@link String}.
 *
 * @see BiPredicate
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
   * @param predicate1 The first predicate
   * @param predicate2 The second predicate
   */
  public <Type1><Type2><Op>BiPredicate(<Type1><Type2>BiPredicate predicate1,
      <Type1><Type2>BiPredicate predicate2) {
    this.predicate1 = Objects.requireNonNull(predicate1, "Predicate 1 is null");
    this.predicate2 = Objects.requireNonNull(predicate2, "Predicate 2 is null");
  }

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
    return StringUtils.<op>ToString(predicate1, predicate2);
  }
}