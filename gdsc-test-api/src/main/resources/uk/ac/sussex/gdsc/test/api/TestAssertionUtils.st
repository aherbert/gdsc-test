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

import java.util.Deque;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Adds additional helper assertion utility functions.
 *
 * \<p>Note: The ideas for this class are based on the {@code org.junit.jupiter.api} package from
 * JUnit 5.
 */
final class TestAssertionUtils {
  /**
   * The prefix for the formatted value.
   *
   * \<p>This should be followed by the value then '>'.
   */
  private static final String VALUE_FORMAT = "value: \<";
  /**
   * The prefix for the formatted expected value.
   *
   * \<p>This should be followed by the expected value then '>'.
   */
  private static final String EXPECTED_FORMAT = "expected: \<";
  /**
   * The prefix for the formatted actual value.
   *
   * \<p>It is assumed this will be following the expected value.
   *
   * \<p>This should be followed by the actual value then '>'.
   */
  private static final String ACTUAL_FORMAT = ">, actual: \<";

  /**
   * Do not allow public construction.
   */
  private TestAssertionUtils() {}

  ///////////////////////////////////////////////////////////////////
  // Methods below copied from org.junit.jupiter.api.AssertionUtils
  ///////////////////////////////////////////////////////////////////

  // Taken from AssertionUtils
  private static String nullSafeGet(Supplier\<String> messageSupplier) {
    return (messageSupplier == null ? null : messageSupplier.get());
  }

  /**
   * Builds the fail message prefix.
   *
   * @param message the message
   * @return the prefix
   */
  static String buildPrefix(String message) {
    return (StringUtils.isNotEmpty(message) ? message + " ==> " : "");
  }

  /**
   * Format indexes for the array fail message.
   *
   * @param indexes the indexes
   * @return the formatted indices
   */
  static String formatIndexes(Deque\<Integer> indexes) {
    if (indexes == null || indexes.isEmpty()) {
      return "";
    }
    final String indexesString =
        indexes.stream().map(Object::toString).collect(Collectors.joining("][", "[", "]"));
    return " at index " + indexesString;
  }<types,Types,Classes:{type,Type,Class |
<\n>
  /////////////////////////////////////
  //<type>
  /////////////////////////////////////

  /**
   * \<em>Asserts\</em> the test between {@code expected\} and {@code actual\}.
   *
   * \<p>Fails with the supplied failure {@code message\}.
   *
   * @param expected The expected value.
   * @param actual The value to check against \<code>expected\</code>.
   * @param test The test.
   * @param message The message.
   */
  static void assertTest(<type> expected, <type> actual, <Type><Type>BiPredicate test,
      String message) {
    if (!test.test(expected, actual)) {
      throw new AssertionError(format(expected, actual, test, message));
    \}
  \}

  /**
   * \<em>Asserts\</em> the test between {@code expected\} and {@code actual\}.
   *
   * \<p>If necessary, the failure message will be retrieved lazily from the supplied
   * {@code messageSupplier\}.
   *
   * @param expected The expected value.
   * @param actual The value to check against \<code>expected\</code>.
   * @param test The test.
   * @param messageSupplier The message supplier.
   */
  static void assertTest(<type> expected, <type> actual, <Type><Type>BiPredicate test,
      Supplier\<String> messageSupplier) {
    if (!test.test(expected, actual)) {
      throw new AssertionError(format(expected, actual, test, nullSafeGet(messageSupplier)));
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
      throw new AssertionError(format(value, test, message));
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
      throw new AssertionError(format(value, test, nullSafeGet(messageSupplier)));
    \}
  \}}><types,Types,Classes:{type,Type,Class |
<\n>
  /**
  * Format the message.
   *
   * \<p>Adapted from {@code format(Object, Object, String)\}.
   *
   * @param expected The expected value.
   * @param actual The value to check against \<code>expected\</code>.
   * @param test The test.
   * @param message The message.
   * @return the formatted message
   */
  static String format(<type> expected, <type> actual, <Type><Type>BiPredicate test,
      String message) {
    return buildPrefix(message) + formatValues(expected, actual, test);
  \}

  /**
   * Format the message.
   *
   * @param value The value to test.
   * @param test The test.
   * @param message The message.
   * @return the formatted message
   */
  static String format(<type> value, <Type>Predicate test, String message) {
    return buildPrefix(message) + formatValue(value, test);
  \}}><types,Types,Classes:{type,Type,Class |
  <\n>
  /**
   * Format the values.
   *
   * \<p>Adapted from {@code formatValues(Object, Object)\}.
   *
   * @param expected The expected value.
   * @param actual The value to check against \<code>expected\</code>.
   * @param test The test.
   * @return the formatted values
   */
  static String formatValues(<type> expected, <type> actual, <Type><Type>BiPredicate test) {
    final StringBuilder msg = new StringBuilder(EXPECTED_FORMAT).append(expected)
        .append(ACTUAL_FORMAT).append(actual).append('>');
    appendPredicate(msg, test);
    return msg.toString();
  \}}><types,Types,Classes:{type,Type,Class |
<\n>
  /**
   * Format the value.
   *
   * @param value The value to test.
   * @param test The test.
   * @return the formatted values
   */
  static String formatValue(<type> value, <Type>Predicate test) {
    final StringBuilder msg = new StringBuilder(VALUE_FORMAT).append(value).append('>');
    appendPredicate(msg, test);
    return msg.toString();
  \}}>

  private static void appendPredicate(final StringBuilder msg, final Object predicate) {
    final String description = StringUtils.toString(predicate);
    if (StringUtils.isNotEmpty(description)) {
      msg.append(". ").append(description);
    }
  }
}
