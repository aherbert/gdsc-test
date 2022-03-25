<package>

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Represents a logical {@code <Op>} of two single-value predicates, each predicate testing a
 * different value of the {@link <Type1><Type2>BiPredicate#test(<type1>, <type2>)} method.
 *
 * \<p>Supports a description {@link Supplier} for a {@link String}.
 *
 * @see java.util.function.BiPredicate
 * @since 1.0
 */
public class <Type1>Predicate<Op><Type2>Predicate
    implements <Type1><Type2>BiPredicate, Supplier\<String> {

  /** The first predicate. */
  private final <Type1>Predicate predicate1;
  /** The second predicate. */
  private final <Type2>Predicate predicate2;

  /**
   * Create a new combined {@code <Op>} predicate of two single-value predicates.
   *
   * @param predicate1 the first predicate
   * @param predicate2 the second predicate
   */
  public <Type1>Predicate<Op><Type2>Predicate(<Type1>Predicate predicate1,
      <Type2>Predicate predicate2) {
    this.predicate1 = Objects.requireNonNull(predicate1, "Predicate 1 is null");
    this.predicate2 = Objects.requireNonNull(predicate2, "Predicate 2 is null");
  }

  /**
   * {@inheritDoc}
   *
   * \<p>Test the logical {@code <Op>} combination of the two single-value predicates.
   * The first predicate is used to test {@code value1\} and the second predicate is used
   * to test {@code value2\}.
   */
  @Override
  public boolean test(<type1> value1, <type2> value2) {
    return predicate1.test(value1) <OP> predicate2.test(value2);
  }

  /**
   * {@inheritDoc}
   *
   * \<p>Gets the logical {@code <Op>} combination of the string representation of the two
   * single-value predicates.
   */
  @Override
  public String get() {
    final String p1 = "p(v1)=" + Strings.wrap(Strings.toString(predicate1));
    final String p2 = "p(v2)=" + Strings.wrap(Strings.toString(predicate2));
    return Strings.<op>ToString(p1, p2);
  }
}
