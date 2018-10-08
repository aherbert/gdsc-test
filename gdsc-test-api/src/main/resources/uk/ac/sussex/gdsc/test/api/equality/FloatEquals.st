<package>

import uk.ac.sussex.gdsc.test.api.function.<Float>Predicate;

import java.util.function.Supplier;

/**
 * Tests {@code <float>} equality to a reference value using the {@code == operator}.
 *
 * @see <Float>Predicate#test(<float>)
 */
public class <Float>Equals implements <Float>Predicate, Supplier\<String> {

  /** The reference value to test against. */
  private final <float> referenceValue;

  /**
   * Create a new instance of this class.
   *
   * \<p>Equality to the reference value will use the {@code == operator}.
   *
   * @param referenceValue The reference value.
   */
  public <Float>Equals(<float> referenceValue) {
    this.referenceValue = referenceValue;
  }

  /**
   * Get the reference value.
   *
   * @return The reference value
   */
  public <float> getReferenceValue() {
    return referenceValue;
  }

  /**
   * {@inheritDoc}
   *
   * \<p>Tests that the value is equal to the reference value using the {@code ==} operator.
   *
   * \<p>Equality imposed by this method is inconsistent with
   * {@link <Float>#equals(Object)} and, {@link <Float>#compare(<float>, <float>)}.
   *
   * \<p>For example {@code -0} and {@code 0} are equal. {@code <Float>.NaN} and
   * {@code <Float>.NaN} are not equal.
   *
   * @return true if equal
   * @see #getReferenceValue()
   */
  @Override
  public boolean test(<float> value) {
    return value == referenceValue;
  }

  /**
   * Get a description of this predicate.
   *
   * @return The description
   */
  @Override
  public String get() {
    return "value == " + <Float>.toString(referenceValue);
  }
}
