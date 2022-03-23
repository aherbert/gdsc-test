<package>

import uk.ac.sussex.gdsc.test.api.function.<Type>Predicate;

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * Tests {@code <type>} equality to a reference value using the {@code == operator}.
 *
 * @see <Type>Predicate#test(<type>)
 */
public class <Type>IsEqual
    implements <Type>Predicate, Supplier\<String>, Serializable {

  /** The serial version ID. */
  private static final long serialVersionUID = 1L;

  /** The reference value to test against. */
  private final <type> reference;

  /**
   * Create a predicate to test {@code <type>} equality.
   *
   * \<p>Equality to the reference value will use the {@code == operator}.
   *
   * @param reference The reference value.
   */
  public <Type>IsEqual(<type> reference) {
    this.reference = reference;
  }

  /**
   * Get the reference value.
   *
   * @return The reference value
   */
  public <type> getReference() {
    return reference;
  }

  /**
   * {@inheritDoc}
   *
   * \<p>Tests that the value is equal to the reference value using the {@code ==} operator.
<if(javadocFloat)>
   *
   * \<p>Equality imposed by this method is inconsistent with
   * {@link <Type>#equals(Object)} and, {@link <Type>#compare(<type>, <type>)}.
   *
   * \<p>For example {@code -<zero>} and {@code <zero>} are equal. {@code <Type>.NaN} and
   * {@code <Type>.NaN} are not equal.
<endif>
   *
   * @return true if equal
   * @see #getReference()
   */
  @Override
  public boolean test(<type> value) {
    return value == reference;
  }

  /**
   * Get a description of this predicate.
   *
   * @return The description
   */
  @Override
  public String get() {
    return "value == " + <Class>.toString(reference);
  }
}
