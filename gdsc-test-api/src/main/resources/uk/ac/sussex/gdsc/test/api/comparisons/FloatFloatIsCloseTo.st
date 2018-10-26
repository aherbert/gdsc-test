<package>

import uk.ac.sussex.gdsc.test.api.function.<Float><Float>BiPredicate;

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * Tests an {@code actual} {@code <float>} value is close to an {@code expected} value using a
 * relative and/or an absolute error tolerance.
 *
 * \<p>The computed relative error is asymmetric and the test is equivalent to testing testing
 * {@code actual} falls within a relative and/or absolute range of {@code expected}.
 *
 * @see <Float><Float>BiPredicate#test(<float>, <float>)
 * @see EqualityUtils#areClose(<float>, <float>, double, <float>)
 */
public class <Float><Float>IsCloseTo
    implements <Float><Float>BiPredicate, Supplier\<String>, Serializable {

  /** The serial version ID. */
  private static final long serialVersionUID = 1L;

  /** The relative error. */
  private final double relativeError;
  /** The absolute error. */
  private final <float> absoluteError;

  /**
   * Create a predicate to test a {@code <float>} value is close to an expected value using a
   * relative and/or absolute error. The relative error between values {@code expected} and
   * {@code actual} is relative to the magnitude of {@code expected}:
   *
   * \<pre>
   * relativeError = |expected-actual| / |expected|
   * \</pre>
   *
   * \<p>The relative error is asymmetric for {@code expected} and {@code actual}. The test is
   * equivalent to testing testing {@code actual} falls within a relative and/or absolute range of
   * {@code expected}.
   *
   * @param relativeError The maximum error, relative to \<code>expected\</code>, between
   *        \<code>expected\</code> and \<code>actual\</code> for which both numbers are still
   *        considered equal.
   * @param absoluteError The maximum absolute error between \<code>expected\</code> and
   *        \<code>actual\</code> for which both numbers are still considered equal.
   * @throws IllegalArgumentException If the relative error is not positive finite
   * @throws IllegalArgumentException If the absolute error is not positive finite
   */
  public <Float><Float>IsCloseTo(double relativeError, <float> absoluteError) {
    EqualityUtils.<float>sValidateIsCloseTo(relativeError, absoluteError);
    this.relativeError = relativeError;
    this.absoluteError = absoluteError;
  }

  /**
   * {@inheritDoc}
   *
   * \<p>Tests that {@code actual} is close to {@code expected} using a relative and/or an absolute
   * error tolerance. The relative error is relative to the magnitude of {@code expected}.
   *
   * @return true if actual is close to expected
   */
  @Override
  public boolean test(<float> expected, <float> actual) {
    // Errors have been checked so use the package level method
    return EqualityUtils.<float>sTestIsCloseTo(expected, actual, relativeError, absoluteError);
  }

  /**
   * Get a description of this predicate.
   *
   * @return The description
   */
  @Override
  public String get() {
    return EqualityUtils.<float>sGetDescriptionIsCloseTo(relativeError, absoluteError);
  }
}
