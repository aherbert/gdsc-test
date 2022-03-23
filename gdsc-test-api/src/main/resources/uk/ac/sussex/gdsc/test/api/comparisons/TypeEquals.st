<package>

import uk.ac.sussex.gdsc.test.api.function.<Type>Predicate;

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * Tests {@code <type>} equality to a reference value using the {@code == operator}.
 *
 * @see <Type>Predicate#test(<type>)
 */
public class <Type>Equals
    implements <Type>Predicate, Supplier\<String>, Serializable {

  /** The serial version ID. */
  private static final long serialVersionUID = 1L;

  /** The reference value to test against. */
  private final <type> referenceValue;

  /**
   * Create a predicate to test {@code <type>} equality.
   *
   * \<p>Equality to the reference value will use the {@code == operator}.
   *
   * @param referenceValue The reference value.
   */
  public <Type>Equals(<type> referenceValue) {
    this.referenceValue = referenceValue;
  }

  /**
   * Get the reference value.
   *
   * @return The reference value
   */
  public <type> getReferenceValue() {
    return referenceValue;
  }

  /**
   * {@inheritDoc}
   *
   * \<p>Tests that the value is equal to the reference value using the {@code ==} operator.
<if(javadocFloat)>
   *
   * \<p>Equality imposed by this method is inconsistent with
   * {@link <Class>#equals(Object)} and, {@link <Class>#compare(<type>, <type>)}.
   *
   * \<p>For example {@code -0} and {@code 0} are equal. {@code <Class>.NaN} and
   * {@code <Class>.NaN} are not equal.
<endif>
   *
   * @return true if equal
   * @see #getReferenceValue()
   */
  @Override
  public boolean test(<type> value) {
    return value == referenceValue;
  }

  /**
   * Get a description of this predicate.
   *
   * @return The description
   */
  @Override
  public String get() {
    return "value == " + <Class>.toString(referenceValue);
  }
}
