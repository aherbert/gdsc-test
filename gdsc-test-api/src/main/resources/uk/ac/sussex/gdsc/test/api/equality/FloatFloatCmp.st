<package>

import uk.ac.sussex.gdsc.test.api.function.<Float><Float>BiPredicate;

import java.util.function.Supplier;

/**
 * Tests {@code <float>} equality using binary comparison of two values
 * converted using {@link <Float>#<float>To<Int>Bits(<float>)}.
 *
 * @see <Float><Float>BiPredicate#test(<float>, <float>)
 */
public class <Float><Float>Cmp implements <Float><Float>BiPredicate, Supplier\<String> {

  /**
   * {@inheritDoc}
   *
   * \<p>Tests that two <float>s are equal using binary comparison of two values
   * converted using {@link <Float>#<float>To<Int>Bits(<float>)}.
   *
   * \<p>Equality imposed by this method is consistent with
   * {@link <Float>#equals(Object)} and, {@link <Float>#compare(<float>, <float>)}.
   *
   * \<p>For example {@code -0} and {@code 0} are not equal. {@code <Float>.NaN} and
   * {@code <Float>.NaN} are equal.
   *
   * @return true if equal
   */
  @Override
  public boolean test(<float> value1, <float> value2) {
    return <Float>.<float>To<Int>Bits(value1) == <Float>.<float>To<Int>Bits(value2);
  }

  /**
   * Get a description of this predicate.
   *
   * @return The description
   */
  @Override
  public String get() {
    return "value1 cmp value2";
  }
}
