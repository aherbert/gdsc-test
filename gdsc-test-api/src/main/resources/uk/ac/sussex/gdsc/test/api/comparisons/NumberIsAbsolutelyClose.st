<package>

import uk.ac.sussex.gdsc.test.api.function.<Number>Predicate;

import java.io.Serializable;
<if(import1)>import <import1>;<\n><endif>import java.util.function.Supplier;

/**
 * Tests {@code <number>} equality using an absolute error tolerance.
 *
 * @see <Number>Predicate#test(<number>)
 * @see <Number>Equality#areWithin(<number>, <number>, <errorType>)
 */
public class <Number>Is<Absolutely>Close
    implements <Number>Predicate, Supplier\<String>, Serializable {

  /** The serial version ID. */
  private static final long serialVersionUID = 1L;

  /** The reference value to test against. */
  private final <number> reference;
  /** The absolute error. */
  private final <errorType> absoluteError;

  /**
   * Create a predicate to test <an> {@code <number>} value is close to a {@code reference} value using
   * an absolute error.
   *
   * @param reference The reference value.
   * @param absoluteError The maximum absolute error between {@code reference}
   *                      and {@code value} for which both numbers are still
   *                      considered equal.
   * @throws IllegalArgumentException If the absolute error is not positive or below the maximum
   *         measurable difference between <number> values
   */
  public <Number>Is<Absolutely>Close(<number> reference, <errorType> absoluteError) {
    <Number>Equality.validateAbsoluteError(absoluteError);
    this.reference = reference;
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
   * \<p>Tests that the {@code value} is close to the reference using an absolute error tolerance.
   *
   * @return true if close
   */
  @Override
  public boolean test(<number> value) {
    // Errors have been checked so use the package level method
    return <Number>Equality.testAreWithin(reference, value, absoluteError);
  }

  /**
   * Get a description of this predicate.
   *
   * @return The description
   */
  @Override
  public String get() {
    return Descriptions.getReferenceDescription(<Class>.toString(reference)) +
           Descriptions.getDescriptionWithin(absoluteError);
  }
}
