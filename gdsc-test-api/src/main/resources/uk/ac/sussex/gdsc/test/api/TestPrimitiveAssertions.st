<package>

import uk.ac.sussex.gdsc.test.api.function.BooleanBooleanBiPredicate;
import uk.ac.sussex.gdsc.test.api.function.BooleanPredicate;
import uk.ac.sussex.gdsc.test.api.function.ByteByteBiPredicate;
import uk.ac.sussex.gdsc.test.api.function.BytePredicate;
import uk.ac.sussex.gdsc.test.api.function.CharCharBiPredicate;
import uk.ac.sussex.gdsc.test.api.function.CharPredicate;
import uk.ac.sussex.gdsc.test.api.function.DoubleDoubleBiPredicate;
import uk.ac.sussex.gdsc.test.api.function.DoublePredicate;
import uk.ac.sussex.gdsc.test.api.function.FloatFloatBiPredicate;
import uk.ac.sussex.gdsc.test.api.function.FloatPredicate;
import uk.ac.sussex.gdsc.test.api.function.IntIntBiPredicate;
import uk.ac.sussex.gdsc.test.api.function.IntPredicate;
import uk.ac.sussex.gdsc.test.api.function.LongLongBiPredicate;
import uk.ac.sussex.gdsc.test.api.function.LongPredicate;
import uk.ac.sussex.gdsc.test.api.function.ShortPredicate;
import uk.ac.sussex.gdsc.test.api.function.ShortShortBiPredicate;

import java.util.function.Supplier;

/**
 * Adds additional helper assertion utility functions.
 *
 * \<p>Note: The ideas for this class are based on the {@code org.junit.jupiter.api} package from
 * JUnit 5.
 */
final class TestPrimitiveAssertions {

  /** No instances. */
  private TestPrimitiveAssertions() {}<types,Types,Classes:{type,Type,Class |
<\n>
  /////////////////////////////////////
  // <type>
  /////////////////////////////////////

  /**
   * \<em>Asserts\</em> the test between {@code expected\} and {@code actual\}.
   *
   * \<p>Fails with the supplied failure {@code message\}.
   *
   * @param expected The expected value.
   * @param actual The value to check against {@code expected\}.
   * @param test The test.
   * @param message The message.
   */
  static void assertTest(<type> expected, <type> actual, <Type><Type>BiPredicate test,
      String message) {
    if (!test.test(expected, actual)) {
      throw new AssertionError(
          MessageBuilder.of(message).append(expected, actual, test).toString());
    \}
  \}

  /**
   * \<em>Asserts\</em> the test between {@code expected\} and {@code actual\}.
   *
   * \<p>If necessary, the failure message will be retrieved lazily from the supplied
   * {@code messageSupplier\}.
   *
   * @param expected The expected value.
   * @param actual The value to check against {@code expected\}.
   * @param test The test.
   * @param messageSupplier The message supplier.
   */
  static void assertTest(<type> expected, <type> actual, <Type><Type>BiPredicate test,
      Supplier\<String> messageSupplier) {
    if (!test.test(expected, actual)) {
      throw new AssertionError(
          MessageBuilder.of(messageSupplier).append(expected, actual, test).toString());
    \}
  \}

  /**
   * \<em>Asserts\</em> the test on {@code value\}.
   *
   * \<p>Fails with the supplied failure {@code message\}.
   *
   * @param value The value to test.
   * @param test The test.
   * @param message The message.
   */
  static void assertTest(<type> value, <Type>Predicate test, String message) {
    if (!test.test(value)) {
      throw new AssertionError(
          MessageBuilder.of(message).append(value, test).toString());
    \}
  \}

  /**
   * \<em>Asserts\</em> the test on {@code value\}.
   *
   * \<p>If necessary, the failure message will be retrieved lazily from the supplied
   * {@code messageSupplier\}.
   *
   * @param value The value to test.
   * @param test The test.
   * @param messageSupplier The message supplier.
   */
  static void assertTest(<type> value, <Type>Predicate test, Supplier\<String> messageSupplier) {
    if (!test.test(value)) {
      throw new AssertionError(
          MessageBuilder.of(messageSupplier).append(value, test).toString());
    \}
  \}}>
}
