<package>

import uk.ac.sussex.gdsc.test.api.function.<Number><Number>BiPredicate;

import java.io.Serializable;
<if(import1)>import <import1>;<\n><endif>import java.util.function.Supplier;

/**
 * Tests {@code <number>} equality using an absolute error tolerance.
 *
 * @see <Number><Number>BiPredicate#test(<number>, <number>)
 * @see <Number>Equality#areWithin(<number>, <number>, <errorType>)
 */
public class <Number><Number>Are<Absolutely>Close
    implements <Number><Number>BiPredicate, Supplier\<String>, Serializable {

  /** The serial version ID. */
  private static final long serialVersionUID = 1L;

  /** The absolute error. */
  private final <errorType> absoluteError;

  /**
   * Create a predicate to test two {@code <number>} values are close using an absolute error.
   *
   * @param absoluteError The maximum absolute error between {@code value1}
   *                      and {@code value2} for which both numbers are still
   *                      considered equal.
   * @throws IllegalArgumentException If the absolute error is not positive or below the maximum
   *         measurable difference between <number> values
   */
  public <Number><Number>Are<Absolutely>Close(<errorType> absoluteError) {
    <Number>Equality.validateAbsoluteError(absoluteError);
    this.absoluteError = absoluteError;
  }

  /**
   * {@inheritDoc}
   *
   * \<p>Tests that two <number>s are equal within an absolute error tolerance.
   *
   * @return true if equal within error
   */
  @Override
  public boolean test(<number> value1, <number> value2) {
    // Errors have been checked so use the package level method
    return <Number>Equality.testAreWithin(value1, value2, absoluteError);
  }

  /**
   * Get a description of this predicate.
   *
   * @return The description
   */
  @Override
  public String get() {
    return Descriptions.getDescriptionWithin(absoluteError);
  }
}
