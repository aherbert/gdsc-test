<package>

import uk.ac.sussex.gdsc.test.api.function.<Number><Number>BiPredicate;

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * Tests {@code <number>} equality using a relative and/or an absolute error tolerance.
 *
 * \<p>The computed relative error is symmetric and the test is equivalent to testing convergence of
 * two values.
 *
 * @see <Number><Number>BiPredicate#test(<number>, <number>)
 * @see EqualityUtils#areClose(<number>, <number>, double, <number>)
 */
public class <Number><Number>AreClose
    implements <Number><Number>BiPredicate, Supplier\<String>, Serializable {

  /** The serial version ID. */
  private static final long serialVersionUID = 1L;

  /** The relative error. */
  private final double relativeError;
  /** The absolute error. */
  private final <errorType> absoluteError;

  /**
   * Create a predicate to test two {@code <number>} values are close using a relative and/or
   * absolute error. The relative error between values {@code value1} and {@code value2} is
   * relative to the largest magnitude of the two values and the test is:
   *
   * \<pre>
   * |value1-value2| \<= max(|value1|, |value2|) * relativeError
   * \</pre>
   *
   * \<p>The relative error is symmetric for {@code value1} and {@code value2}. The test is
   * equivalent to testing convergence of two values.
   *
   * @param relativeError The maximum relative error between \<code>value1\</code> and
   *        \<code>value2\</code> for which both numbers are still considered equal.
   * @param absoluteError The maximum absolute error between \<code>value1\</code> and
   *        \<code>value2\</code> for which both numbers are still considered equal.
   * @throws IllegalArgumentException If the relative error is not positive finite and below 2
   * @throws IllegalArgumentException If the absolute error is not positive or is
   *         <code>>=</code> than the maximum difference between <number> primitives
   */
  public <Number><Number>AreClose(double relativeError, <errorType> absoluteError) {
    <Number>EqualityUtils.validateAreClose(relativeError, absoluteError);
    this.relativeError = relativeError;
    this.absoluteError = absoluteError;
  }

  /**
   * {@inheritDoc}
   *
   * \<p>Tests that two <number>s are close using a relative and/or an absolute
   * error tolerance.
   *
   * @return true if close
   */
  @Override
  public boolean test(<number> value1, <number> value2) {
    // Errors have been checked so use the package level method
    return <Number>EqualityUtils.testAreClose(value1, value2, relativeError, absoluteError);
  }

  /**
   * Get a description of this predicate.
   *
   * @return The description
   */
  @Override
  public String get() {
    return DescriptionHelper.getDescriptionClose(relativeError, absoluteError);
  }
}
