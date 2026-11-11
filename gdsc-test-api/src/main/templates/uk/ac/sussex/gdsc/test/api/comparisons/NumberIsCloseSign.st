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
<if(unsigned)>
 * \<p>The values are interpreted as unsigned integers.
 *
<endif> * @see <Number>Predicate#test(<number>)
 * @see <Number>Equality#areClose<Sign>(<number>, <number>, double, <errorType>)
 */
public class <Number>IsClose<Sign>
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
   * |reference - value| \<= <if(unsigned)>max(reference, value)<else>max(|reference|, |value|)<endif> * relativeError
   * \}\</pre>
   *
   * \<p>The relative error is symmetric for {@code reference} and {@code value}. The test is
   * equivalent to testing convergence on the reference value.
   *
<if(unsigned)>
   * \<p>The values are interpreted as unsigned integers.
   *
<endif>
   * \<p>The test is performed using<if(unsigned)><\n>   *<endif> {@link <Number>Equality#areClose<Sign>(<number>, <number>, double, <errorType>)}
   * which defines the computation of the difference and the relative limit.
   *
   * @param reference The reference value.
   * @param relativeError The maximum relative error between {@code reference} and
   *        {@code value} for which both numbers are still considered equal.
   * @param absoluteError The maximum absolute error between {@code reference} and
   *        {@code value} for which both numbers are still considered equal.
   * @throws IllegalArgumentException If the relative error is not positive finite and below 2
<if(maxError)>
   * @throws IllegalArgumentException If the absolute error is not positive or is not below the
   *         maximum difference between <number> primitives
<elseif(finiteError)>
   * @throws IllegalArgumentException If the absolute error is not positive finite
<else>
   * @throws IllegalArgumentException If the absolute error is not positive
<endif>
   */
  public <Number>IsClose<Sign>(<number> reference,
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
    return <Number>Equality.testAreClose<Sign>(reference, value, relativeError, absoluteError);
  }

  /**
   * Get a description of this predicate.
   *
   * @return The description
   */
  @Override
  public String get() {
<if(unsigned)>
    return Descriptions.getReferenceDescription(Descriptions.toUnsignedString(reference))
        + Descriptions.getUnsignedDescription(
            Descriptions.getDescriptionCloseUnsigned(relativeError, absoluteError));
<else>
    return Descriptions.getReferenceDescription(<Class>.toString(reference))
        + Descriptions.getDescriptionClose(relativeError, absoluteError);
<endif>
  }
}
