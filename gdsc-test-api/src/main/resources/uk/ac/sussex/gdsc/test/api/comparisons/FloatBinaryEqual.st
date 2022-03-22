<package>

import uk.ac.sussex.gdsc.test.api.function.<Float>Predicate;

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * Tests {@code <float>} equality to a reference value using binary comparison of the values
 * converted using {@link <Float>#<float>To<Int>Bits(<float>)}.
 *
 * @see <Float>Predicate#test(<float>)
 */
public class <Float>BinaryEqual
    implements <Float>Predicate, Supplier\<String>, Serializable {

  /** The serial version ID. */
  private static final long serialVersionUID = 1L;

  /** The reference value to test against. */
  private final <float> referenceValue;

  /** The binary form of the reference value to test against. */
  private final <int> binaryReferenceValue;

  /**
   * Create a predicate to test {@code <float>} equality.
   *
   * \<p>Equality to the reference value will use binary comparison of the values
   * converted using {@link <Float>#<float>To<Int>Bits(<float>)}.
   *
   * @param referenceValue The reference value.
   */
  public <Float>BinaryEqual(<float> referenceValue) {
    this.referenceValue = referenceValue;
    binaryReferenceValue = <Float>.<float>To<Int>Bits(referenceValue);
  }

  /**
   * Get the reference value.
   *
   * \<p>This can be converted to binary form using {@link <Float>#<float>To<Int>Bits(<float>)}.
   *
   * @return The reference value
   */
  public <float> getReferenceValue() {
    return referenceValue;
  }

  /**
   * {@inheritDoc}
   *
   * \<p>Tests that the value is equal to the reference value using binary comparison of the values
   * converted using {@link <Float>#<float>To<Int>Bits(<float>)}.
   *
   * \<p>Equality imposed by this method is consistent with
   * {@link <Float>#equals(Object)} and, {@link <Float>#compare(<float>, <float>)}.
   *
   * \<p>For example {@code -0} and {@code 0} are not equal. {@code <Float>.NaN} and
   * {@code <Float>.NaN} are equal.
   *
   * @return true if equal
   * @see #getReferenceValue()
   */
  @Override
  public boolean test(<float> value) {
    return <Float>.<float>To<Int>Bits(value) == binaryReferenceValue;
  }

  /**
   * Get a description of this predicate.
   *
   * @return The description
   */
  @Override
  public String get() {
    return "value cmp " + <Float>.toString(referenceValue);
  }
}
