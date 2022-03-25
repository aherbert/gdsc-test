<package>

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Represents a negation of a predicate including the string representation using {@link Supplier}
 * for a {@link String}.
 *
 * @see java.util.function.BiPredicate
 * @since 1.0
 */
public class <Type1><Type2>NegateBiPredicate
    implements <Type1><Type2>BiPredicate, Supplier\<String> {

  /** The predicate. */
  private final <Type1><Type2>BiPredicate predicate;

  /**
   * Create a new negate predicate.
   *
   * @param predicate the predicate
   */
  public <Type1><Type2>NegateBiPredicate(<Type1><Type2>BiPredicate predicate) {
    this.predicate = Objects.requireNonNull(predicate, "Predicate is null");
  }

  @Override
  public boolean test(<type1> value1, <type2> value2) {
    return !predicate.test(value1, value2);
  }

  @Override
  public <Type1><Type2>BiPredicate negate() {
    // Back to the original
    return predicate;
  }

  /**
   * {@inheritDoc}
   *
   * \<p>Gets the negation of the string representation of the predicate.
   */
  @Override
  public String get() {
    return Strings.negateToString(predicate);
  }
}
