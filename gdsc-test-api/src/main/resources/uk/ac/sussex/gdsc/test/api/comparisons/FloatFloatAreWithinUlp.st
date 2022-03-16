<package>

import uk.ac.sussex.gdsc.test.api.function.<Float><Float>BiPredicate;

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * Tests {@code <float>} equality using a ULP (unit in the last place) error tolerance.
 *
 * \<p>The test is equivalent to testing there are {@code (N - 1)} or fewer
 * representable numbers between the two values, where {@code N} is the ULP error tolerance.
 *
 * @see <Float><Float>BiPredicate#test(<float>, <float>)
 * @see <Float>Equality#areWithinUlp(<float>, <float>, <ulpType>)
 * @see \<a href="https://en.wikipedia.org/wiki/Unit_in_the_last_place">Unit in the last place\</a>
 */
public class <Float><Float>AreWithinUlp
    implements <Float><Float>BiPredicate, Supplier\<String>, Serializable {

  /** The serial version ID. */
  private static final long serialVersionUID = 1L;

  /** The ulp error. */
  private final <ulpType> ulpError;

  /**
   * Create a predicate to test two {@code <float>} values are equal within a ULP error.
   *
   * \<p>The number of changes in the unit of least precision to adjust one value
   * to the other value are counted and the test is {@code count \<= ulpError}. The sign of
   * zero is ignored, hence the test considers {@code -<zero>} and {@code <zero>} to be equal.
   *
   * \<p>The test is equivalent to testing there are {@code (ulpError - 1)} or fewer
   * representable numbers between the two values.
   *
   * \<p>NaN values are ignored and the test returns false.
   *
   * @param ulpError The maximum ULP error between \<code>value1\</code>
   *                 and \<code>value2\</code> for which both numbers are still
   *                 considered equal.
   * @throws IllegalArgumentException If the ULP error is not positive
   */
  public <Float><Float>AreWithinUlp(<ulpType> ulpError) {
    <Float>Equality.validateUlpError(ulpError);
    this.ulpError = ulpError;
  }

  /**
   * {@inheritDoc}
   *
   * \<p>Tests that two <float>s are equal within a ULP error tolerance.
   *
   * @return true if equal within error
   */
  @Override
  public boolean test(<float> value1, <float> value2) {
    // Errors have been checked so use the package level method
    return <Float>Equality.testAreWithinUlp(value1, value2, ulpError);
  }

  /**
   * Get a description of this predicate.
   *
   * @return The description
   */
  @Override
  public String get() {
    return Descriptions.getDescriptionWithinUlp(ulpError);
  }
}
