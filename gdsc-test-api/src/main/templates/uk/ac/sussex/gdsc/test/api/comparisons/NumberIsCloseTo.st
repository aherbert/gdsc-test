<package>

import uk.ac.sussex.gdsc.test.api.function.<Number>Predicate;

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * Tests an {@code actual} {@code <number>} value is close to an {@code reference} value using a
 * relative and/or an absolute error tolerance.
 *
 * \<p>The computed relative error is asymmetric and the test is equivalent to testing
 * {@code actual} falls within a relative and/or absolute range of {@code reference}.
 *
 * @see <Number>Predicate#test(<number>)
 * @see <Number>Equality#isCloseTo(<number>, <number>, double, <errorType>)
 */
public class <Number>IsCloseTo
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
   * Create a predicate to test <an> {@code <number>} is close to a {@code reference} value using a
   * relative and/or absolute error. The relative error between values {@code reference} and
   * {@code value} is relative to the magnitude of {@code reference} and the test is:
   *
   * \<pre>
   * {@code
   * |reference - value| \<= |reference| * relativeError
   * \}\</pre>
   *
   * \<p>The relative error is asymmetric for {@code reference} and {@code value}. The test is
   * equivalent to testing {@code value} falls within a relative and/or absolute range of
   * {@code reference}.
   *
   * @param reference The reference value.
   * @param relativeError The maximum error, relative to {@code reference}, between
   *        {@code reference} and {@code value} for which both numbers are still
   *        considered equal.
   * @param absoluteError The maximum absolute error between {@code reference} and
   *        {@code value} for which both numbers are still considered equal.
   * @throws IllegalArgumentException If the relative error is not positive finite
   * @throws IllegalArgumentException If the absolute error is not positive or is
   *         {@code >=} than the maximum difference between <number> primitives
   */
  public <Number>IsCloseTo(<number> reference,
      double relativeError, <errorType> absoluteError) {
    <Number>Equality.validateIsCloseTo(relativeError, absoluteError);
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
   * error tolerance. The relative error is relative to the magnitude of {@code reference}.
   *
   * @return true if close
   */
  @Override
  public boolean test(<number> value) {
    // Errors have been checked so use the package level method
    return <Number>Equality.testIsCloseTo(reference, value, relativeError, absoluteError);
  }

  /**
   * Get a description of this predicate.
   *
   * @return The description
   */
  @Override
  public String get() {
    return Descriptions.getReferenceDescription(<Class>.toString(reference))
        + Descriptions.getDescriptionIsCloseTo(relativeError, absoluteError);
  }
}
