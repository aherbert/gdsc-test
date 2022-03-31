<package>

import uk.ac.sussex.gdsc.test.api.function.<Number>Predicate;

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * Tests {@code <number>} equality using a relative and/or an absolute error tolerance.
 *
 * \<p>The computed relative error is symmetric and the test is equivalent to testing convergence of
 * two values.
 *
 * @see <Number>Predicate#test(<number>)
 * @see <Number>Equality#areClose(<number>, <number>, double, <errorType>)
 */
public class <Number>IsClose
    implements <Number>Predicate, Supplier\<String>, Serializable {

  /** The serial version ID. */
  private static final long serialVersionUID = 1L;

  /** The reference value to test against. */
  private final <number> reference;
  /** The relative error. */
  private final double relativeError;
  /** The absolute error. */
  private final <errorType> absoluteError;

  /**
   * Create a predicate to test <an> {@code <number>} value is close to a {@code reference} value
   * using a relative and/or absolute error. The relative error between values {@code reference}
   * and {@code value} is relative to the largest magnitude of the two values and the test is:
   *
   * \<pre>
   * {@code
   * |reference - value| \<= max(|reference|, |value|) * relativeError
   * \}\</pre>
   *
   * \<p>The relative error is symmetric for {@code reference} and {@code value}. The test is
   * equivalent to testing convergence on the reference value.
   *
   * @param reference The reference value.
   * @param relativeError The maximum relative error between {@code reference} and
   *        {@code value} for which both numbers are still considered equal.
   * @param absoluteError The maximum absolute error between {@code reference} and
   *        {@code value} for which both numbers are still considered equal.
   * @throws IllegalArgumentException If the relative error is not positive finite and below 2
   * @throws IllegalArgumentException If the absolute error is not positive or is
   *         {@code >=} than the maximum difference between <number> primitives
   */
  public <Number>IsClose(<number> reference,
      double relativeError, <errorType> absoluteError) {
    <Number>Equality.validateAreClose(relativeError, absoluteError);
    this.reference = reference;
    this.relativeError = relativeError;
    this.absoluteError = absoluteError;
  }

  /**
   * Get the reference value.
   *
   * @return The reference value
   */
  public <number> getReference() {
    return reference;
  }

  /**
   * {@inheritDoc}
   *
   * \<p>Tests that the {@code value} is close to the reference using a relative and/or an absolute
   * error tolerance.
   *
   * @return true if close
   */
  @Override
  public boolean test(<number> value) {
    // Errors have been checked so use the package level method
    return <Number>Equality.testAreClose(reference, value, relativeError, absoluteError);
  }

  /**
   * Get a description of this predicate.
   *
   * @return The description
   */
  @Override
  public String get() {
    return Descriptions.getReferenceDescription(<Class>.toString(reference))
        + Descriptions.getDescriptionClose(relativeError, absoluteError);
  }
}
