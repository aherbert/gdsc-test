<package>

import uk.ac.sussex.gdsc.test.api.function.<Float>Predicate;

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * Tests {@code <float>} equality to a reference value using a ULP (unit in the last place)
 * error tolerance.
 *
 * \<p>The test is equivalent to testing there are {@code (N - 1)} or fewer
 * representable numbers between the two values, where {@code N} is the ULP error tolerance.
 *
 * @see <Float>Predicate#test(<float>)
 * @see <Float>Equality#areWithinUlp(<float>, <float>, <ulpType>)
 * @see \<a href="https://en.wikipedia.org/wiki/Unit_in_the_last_place">Unit in the last place\</a>
 */
public class <Float>IsUlpClose
    implements <Float>Predicate, Supplier\<String>, Serializable {

  /** The serial version ID. */
  private static final long serialVersionUID = 1L;

  /** The reference value to test against. */
  private final <float> reference;

  /** The ulp error. */
  private final <ulpType> ulpError;

  /**
   * Create a predicate to test a {@code <float>} value is close to a {@code reference} value using
   * a ULP error.
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
   * @param reference The reference value.
   * @param ulpError The maximum ULP error between {@code reference}
   *                 and {@code value} for which both numbers are still
   *                 considered equal.
   * @throws IllegalArgumentException If the ULP error is not positive
   */
  public <Float>IsUlpClose(<float> reference, <ulpType> ulpError) {
    <Float>Equality.validateUlpError(ulpError);
    this.reference = reference;
    this.ulpError = ulpError;
  }

  /**
   * Get the reference value.
   *
   * @return The reference value
   */
  public <float> getReference() {
    return reference;
  }

  /**
   * {@inheritDoc}
   *
   * \<p>Tests that the <float> is close to the reference using a ULP error tolerance.
   *
   * @return true if close
   */
  @Override
  public boolean test(<float> value) {
    // Errors have been checked so use the package level method
    return <Float>Equality.testAreWithinUlp(reference, value, ulpError);
  }

  /**
   * Get a description of this predicate.
   *
   * @return The description
   */
  @Override
  public String get() {
    return Descriptions.getReferenceDescription(<Float>.toString(reference)) +
           Descriptions.getDescriptionWithinUlp(ulpError);
  }
}
