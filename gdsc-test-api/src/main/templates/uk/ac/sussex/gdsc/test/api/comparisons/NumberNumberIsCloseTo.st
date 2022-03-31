<package>

import uk.ac.sussex.gdsc.test.api.function.<Number><Number>BiPredicate;

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * Tests an {@code actual} {@code <number>} value is close to an {@code expected} value using a
 * relative and/or an absolute error tolerance.
 *
 * \<p>The computed relative error is asymmetric and the test is equivalent to testing
 * {@code actual} falls within a relative and/or absolute range of {@code expected}.
 *
 * @see <Number><Number>BiPredicate#test(<number>, <number>)
 * @see <Number>Equality#isCloseTo(<number>, <number>, double, <errorType>)
 */
public class <Number><Number>IsCloseTo
    implements <Number><Number>BiPredicate, Supplier\<String>, Serializable {

  /** The serial version ID. */
  private static final long serialVersionUID = 1L;

  /** The relative error. */
  private final double relativeError;
  /** The absolute error. */
  private final <errorType> absoluteError;

  /**
   * Create a predicate to test <an> {@code <number> value} is close to an {@code expected} value
   * using a relative and/or absolute error. The relative error between values {@code expected} and
   * {@code actual} is relative to the magnitude of {@code expected} and the test is:
   *
   * \<pre>
   * {@code
   * |expected - actual| \<= |expected| * relativeError
   * \}\</pre>
   *
   * \<p>The relative error is asymmetric for {@code expected} and {@code actual}. The test is
   * equivalent to testing {@code actual} falls within a relative and/or absolute range of
   * {@code expected}.
   *
   * @param relativeError The maximum error, relative to {@code expected}, between
   *        {@code expected} and {@code actual} for which both numbers are still
   *        considered equal.
   * @param absoluteError The maximum absolute error between {@code expected} and
   *        {@code actual} for which both numbers are still considered equal.
   * @throws IllegalArgumentException If the relative error is not positive finite
   * @throws IllegalArgumentException If the absolute error is not positive or is
   *         {@code >=} than the maximum difference between <number> primitives
   */
  public <Number><Number>IsCloseTo(double relativeError, <errorType> absoluteError) {
    <Number>Equality.validateIsCloseTo(relativeError, absoluteError);
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
  public boolean test(<number> expected, <number> actual) {
    // Errors have been checked so use the package level method
    return <Number>Equality.testIsCloseTo(expected, actual, relativeError, absoluteError);
  }

  /**
   * Get a description of this predicate.
   *
   * @return The description
   */
  @Override
  public String get() {
    return Descriptions.getDescriptionIsCloseTo(relativeError, absoluteError);
  }
}
