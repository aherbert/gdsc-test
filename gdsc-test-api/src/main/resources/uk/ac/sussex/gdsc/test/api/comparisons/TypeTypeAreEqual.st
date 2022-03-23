<package>

import uk.ac.sussex.gdsc.test.api.function.<Type><Type>BiPredicate;

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * Tests {@code <type>} equality using the {@code == operator}.
 *
 * @see <Type><Type>BiPredicate#test(<type>, <type>)
 */
public class <Type><Type>AreEqual
    implements <Type><Type>BiPredicate, Supplier\<String>, Serializable {

  /** The serial version ID. */
  private static final long serialVersionUID = 1L;

  /** A default instance. */
  public static final <Type><Type>AreEqual INSTANCE = new <Type><Type>AreEqual();

  /**
   * {@inheritDoc}
   *
   * \<p>Tests that two <type>s are equal using the {@code ==} operator.
<if(javadocFloat)>
   *
   * \<p>Equality imposed by this method is inconsistent with
   * {@link <Type>#equals(Object)} and, {@link <Type>#compare(<type>, <type>)}.
   *
   * \<p>For example {@code -0} and {@code 0} are equal. {@code <Float>.NaN} and
   * {@code <Float>.NaN} are not equal.
<endif>
   *
   * @return true if equal
   */
  @Override
  public boolean test(<type> value1, <type> value2) {
    return value1 == value2;
  }

  /**
   * Get a description of this predicate.
   *
   * @return The description
   */
  @Override
  public String get() {
    return "value1 == value2";
  }
}
