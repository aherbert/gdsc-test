<package>

import uk.ac.sussex.gdsc.test.api.function.<Float><Float>BiPredicate;

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * Tests {@code <float>} equality using a relative and/or an absolute error tolerance.
 *
 * @see <Float><Float>BiPredicate#test(<float>, <float>)
 * @see EqualityUtils#<float>sAreAlmostEqual(<float>, <float>, double, <float>)
 */
public class <Float><Float>AlmostEquals
    implements <Float><Float>BiPredicate, Supplier\<String>, Serializable {

  /** The serial version ID. */
  private static final long serialVersionUID = 1L;

  /** The relative error. */
  private final double relativeError;
  /** The absolute error. */
  private final <float> absoluteError;

  /**
   * Create a new instance of this class.
   *
   * \<p>Setting the relative error to negative tests equality using only absolute
   * error.
   *
   * \<p>Setting the absolute error to negative tests equality using only relative
   * error.
   *
   * \<p>If both errors are disabled an exception is thrown since no equality test is
   * performed.
   *
   * \<p>Note: the relative error is a double since the relative error computation is
   * performed in double precision.
   *
   * @param relativeError The maximum relative error between \<code>value1\</code>
   *                      and \<code>value2\</code> for which both numbers are still
   *                      considered equal. Ignored if set to negative.
   * @param absoluteError The maximum absolute error between \<code>value1\</code>
   *                      and \<code>value2\</code> for which both numbers are still
   *                      considered equal. Ignored if set to negative.
   * @throws IllegalArgumentException If the relative error is not below 2
   * @throws IllegalArgumentException If the absolute error is not finite
   * @throws IllegalArgumentException If both tolerances are ignored (i.e. no
   *                                  equality test is performed)
   */
  public <Float><Float>AlmostEquals(double relativeError, <float> absoluteError) {
    EqualityUtils.<float>sValidateAlmostEqual(relativeError, absoluteError);
    this.relativeError = relativeError;
    this.absoluteError = absoluteError;
  }

  /**
   * {@inheritDoc}
   *
   * \<p>Tests that two <float>s are almost equal using a relative and/or an absolute
   * error tolerance.
   *
   * @return true if almost equal
   */
  @Override
  public boolean test(<float> value1, <float> value2) {
    // Errors have been checked so use the package level method
    return EqualityUtils.<float>sTestAlmostEqual(value1, value2, relativeError, absoluteError);
  }

  /**
   * Get a description of this predicate.
   *
   * @return The description
   */
  @Override
  public String get() {
    return EqualityUtils.<float>sGetDescriptionAlmostEqual(relativeError, absoluteError);
  }
}
