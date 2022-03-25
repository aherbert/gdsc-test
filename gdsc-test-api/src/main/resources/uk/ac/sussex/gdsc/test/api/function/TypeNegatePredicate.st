<package>

import uk.ac.sussex.gdsc.test.api.Strings;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Represents a negation of a predicate including the string representation using {@link Supplier}
 * for a {@link String}.
 *
 * @see java.util.function.Predicate
 * @since 1.0
 */
public class <Type>NegatePredicate implements <Type>Predicate, Supplier\<String> {

  /** The predicate. */
  private final <Type>Predicate predicate;

  /**
   * Create a new negate predicate.
   *
   * @param predicate the predicate
   */
  public <Type>NegatePredicate(<Type>Predicate predicate) {
    this.predicate = Objects.requireNonNull(predicate, "Predicate is null");
  }

  @Override
  public boolean test(<type> value) {
    return !predicate.test(value);
  }

  @Override
  public <Type>Predicate negate() {
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
