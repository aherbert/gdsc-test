<package>

import uk.ac.sussex.gdsc.test.api.function.<Type><Type>BiPredicate;

import java.util.function.Supplier;

/**
 * Tests {@code <type>} equality using the {@code == operator}.
 *
 * @see <Type><Type>BiPredicate#test(<type>, <type>)
 */
public class <Type><Type>Equals implements <Type><Type>BiPredicate, Supplier\<String> {

  /**
   * {@inheritDoc}
   *
   * \<p>Tests that two <type>s are equal using the {@code ==} operator.
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
