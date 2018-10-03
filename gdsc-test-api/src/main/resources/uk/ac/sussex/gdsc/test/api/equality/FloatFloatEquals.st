<package>

import uk.ac.sussex.gdsc.test.api.function.<Float><Float>BiPredicate;

import java.util.function.Supplier;

/**
 * Tests {@code <float>} equality using the {@code ==} operator.
 *
 * @see <Float><Float>BiPredicate#test(<float>, <float>)
 */
public class <Float><Float>Equals implements <Float><Float>BiPredicate, Supplier\<String> {

  /** A default instance. */
  private static final <Float><Float>Equals INSTANCE = new <Float><Float>Equals();

  /**
   * {@inheritDoc}
   *
   * \<p>Tests that two <float>s are equal using the {@code ==} operator.
   *
   * \<p>Equality imposed by this method is inconsistent with
   * {@link <Float>#equals(Object)} and, {@link <Float>#compare(<float>, <float>)}.
   *
   * \<p>For example {@code -0} and {@code 0} are equal. {@code <Float>.NaN} and
   * {@code <Float>.NaN} are not equal.
   *
   * @return true if equal
   */
  @Override
  public boolean test(<float> value1, <float> value2) {
    return value1 == value2;
  }

  /**
   * Get a description of this predicate.
   *
   * @return The description
   */
  @Override
  public String get() {
    return "value1 == value2";
  }

  /**
   * Gets an instance.
   *
   * @return an instance
   */
  public static <Float><Float>Equals getInstance() {
    return INSTANCE;
  }
}
