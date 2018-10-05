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
import uk.ac.sussex.gdsc.test.api.function.PrimitiveBiPredicate;
import uk.ac.sussex.gdsc.test.api.function.PrimitivePredicate;
import uk.ac.sussex.gdsc.test.api.function.ShortPredicate;
import uk.ac.sussex.gdsc.test.api.function.ShortShortBiPredicate;

import org.opentest4j.AssertionFailedError;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Supplier;

/**
 * Adds additional helper assert array functions.
 *
 * \<p>Note: The ideas for this class are based on the {@code org.junit.jupiter.api} package from
 * JUnit 5.
 */
final class TestAssertionArrayUtils {

  /**
   * Do not allow public construction.
   */
  private TestAssertionArrayUtils() {}

  //////////////////////////////////////////////////////
  // Support nested arrays of each primitive array type
  //////////////////////////////////////////////////////

  /**
   * \<em>Asserts\</em> the test between {@code expected} and {@code actual}.
   *
   * \<p>This supports nested primitive arrays, e.g. {@code double[][]}.
   *
   * @param expected The expected value.
   * @param actual The value to check against \<code>expected\</code>.
   * @param test The test.
   */
  static void assertArrayTest(Object[] expected, Object[] actual, PrimitiveBiPredicate test) {
    assertArrayTest(expected, actual, test, new ArrayDeque\<>(), (String) null);
  }

  /**
   * \<em>Asserts\</em> the test between {@code expected} and {@code actual}.
   *
   * \<p>Fails with the supplied failure {@code message}.
   *
   * \<p>This supports nested primitive arrays, e.g. {@code double[][]}.
   *
   * @param expected The expected value.
   * @param actual The value to check against \<code>expected\</code>.
   * @param test The test.
   * @param message The message.
   */
  static void assertArrayTest(Object[] expected, Object[] actual, PrimitiveBiPredicate test,
      String message) {
    assertArrayTest(expected, actual, test, new ArrayDeque\<>(), message);
  }

  /**
   * \<em>Asserts\</em> the test between {@code expected} and {@code actual}.
   *
   * \<p>If necessary, the failure message will be retrieved lazily from the supplied
   * {@code messageSupplier\}.
   *
   * \<p>This supports nested primitive arrays, e.g. {@code double[][]}.
   *
   * @param expected The expected value.
   * @param actual The value to check against \<code>expected\</code>.
   * @param test The test.
   * @param messageSupplier the message supplier
   */
  static void assertArrayTest(Object[] expected, Object[] actual, PrimitiveBiPredicate test,
      Supplier\<String> messageSupplier) {
    assertArrayTest(expected, actual, test, new ArrayDeque\<>(), messageSupplier);
  }

  // Adapted from AssertArrayEquals
  private static void assertArrayTest(Object[] expected, Object[] actual, PrimitiveBiPredicate test,
      Deque\<Integer> indexes, Object messageOrSupplier) {
    if (expected == actual) {
      return;
    }
    assertArraysNotNull(expected, actual, indexes, messageOrSupplier);
    assertArraysHaveSameLength(expected.length, actual.length, indexes, messageOrSupplier);

    for (int i = 0; i \< expected.length; i++) {
      final Object expectedElement = expected[i];
      final Object actualElement = actual[i];

      if (expectedElement == actualElement) {
        continue;
      }

      indexes.addLast(i);
      assertArrayElements(expectedElement, actualElement, test, indexes, messageOrSupplier);
      indexes.removeLast();
    }
  }

  /**
   * \<em>Asserts\</em> the test on {@code value\}.
   *
   * \<p>This supports nested primitive arrays, e.g. {@code double[][]}.
   *
   * @param value The value.
   * @param test The test.
   */
  static void assertArrayTest(Object[] value, PrimitivePredicate test) {
    assertArrayTest(value, test, new ArrayDeque\<>(), (String) null);
  }

  /**
   * \<em>Asserts\</em> the test on {@code value\}.
   *
   * \<p>Fails with the supplied failure {@code message}.
   *
   * \<p>This supports nested primitive arrays, e.g. {@code double[][]}.
   *
   * @param value The value.
   * @param test The test.
   * @param message The message.
   */
  static void assertArrayTest(Object[] value, PrimitivePredicate test,
      String message) {
    assertArrayTest(value, test, new ArrayDeque\<>(), message);
  }

  /**
   * \<em>Asserts\</em> the test on {@code value\}.
   *
   * \<p>If necessary, the failure message will be retrieved lazily from the supplied
   * {@code messageSupplier\}.
   *
   * \<p>This supports nested primitive arrays, e.g. {@code double[][]}.
   *
   * @param value The value.
   * @param test The test.
   * @param messageSupplier the message supplier
   */
  static void assertArrayTest(Object[] value, PrimitivePredicate test,
      Supplier\<String> messageSupplier) {
    assertArrayTest(value, test, new ArrayDeque\<>(), messageSupplier);
  }

  // Adapted from AssertArrayEquals
  private static void assertArrayTest(Object[] value, PrimitivePredicate test,
      Deque\<Integer> indexes, Object messageOrSupplier) {
    assertArrayNotNull(value, indexes, messageOrSupplier);

    for (int i = 0; i \< value.length; i++) {
      final Object valueElement = value[i];

      indexes.addLast(i);
      assertArrayElement(valueElement, test, indexes, messageOrSupplier);
      indexes.removeLast();
    }
  }

  <types,Types:{type,Type |
<\n>
/////////////////////////////////////
// <type>
/////////////////////////////////////

/**
 * \<em>Asserts\</em> the test between {@code expected\} and {@code actual\}.
 *
 * \<p>Fails with the supplied failure message.
 *
 * @param expected The expected value.
 * @param actual The value to check against \<code>expected\</code>.
 * @param test The test.
 * @param indexes the indexes
 * @param messageOrSupplier the message or supplier
 */
static void assertArrayTest(<type>[] expected, <type>[] actual,
    <Type><Type>BiPredicate test, Deque\<Integer> indexes, Object messageOrSupplier) {
  if (expected == actual) {
    return;
  \}
  assertArraysNotNull(expected, actual, indexes, messageOrSupplier);
  assertArraysHaveSameLength(expected.length, actual.length, indexes, messageOrSupplier);

  for (int i = 0; i \< expected.length; i++) {
    if (!test.test(expected[i], actual[i])) {
      throw new AssertionFailedError(failArraysMessage(expected[i], actual[i], test,
          nullSafeIndexes(indexes, i), messageOrSupplier));
    \}
  \}
\}

/**
 * \<em>Asserts\</em> the test on {@code value\}.
 *
 * \<p>Fails with the supplied failure message.
 *
 * @param value The value.
 * @param test The test.
 * @param indexes the indexes
 * @param messageOrSupplier the message or supplier
 */
static void assertArrayTest(<type>[] value, <Type>Predicate test,
    Deque\<Integer> indexes, Object messageOrSupplier) {
  assertArrayNotNull(value, indexes, messageOrSupplier);

  for (int i = 0; i \< value.length; i++) {
    if (!test.test(value[i])) {
      throw new AssertionFailedError(failArrayMessage(value[i], test,
          nullSafeIndexes(indexes, i), messageOrSupplier));
    \}
  \}
\}}>

  /**
   * \<em>Asserts\</em> the test between two elements from Object[] arrays.
   *
   * \<p>If the elements are matching array types then the appropriate assertArrayTest method is
   * invoked, otherwise fails.
   *
   * @param expected The expected value.
   * @param actual The value to check against \<code>expected\</code>.
   * @param test The test.
   */
  private static void assertArrayElements(Object expected, Object actual, PrimitiveBiPredicate test,
      Deque\<Integer> indexes, Object messageOrSupplier) {
    if (expected instanceof Object[]) {
      assertObjectArray(expected, actual);
      assertArrayTest((Object[]) expected, (Object[]) actual, test, indexes, messageOrSupplier);
    <types,Types:{type,Type |
\} else if (expected instanceof <type>[]) {
  assert<Type>Array(expected, actual);
  assert<Type><Type>BiPredicate(test);
  assertArrayTest((<type>[]) expected, (<type>[]) actual, (<Type><Type>BiPredicate) test,
      indexes, messageOrSupplier);
}>    } else {
      failNotMatchingArrayTypes(expected, actual);
    }
  }

  /**
   * Asserts the actual object is a Object array.
   *
   * \<p>If false then fail using the array type of the expected object in the message.
   *
   * @param expected The expected object (should be an Object array).
   * @param actual The actual object.
   */
  private static void assertObjectArray(Object expected, Object actual) {
    if (!(actual instanceof Object[])) {
      failNotMatchingArrayTypes(expected, actual);
    }
  }

  /**
   * Fail due to the array types not matching.
   *
   * @param expected The expected value.
   * @param actual The value to check against \<code>expected\</code>.
   */
  private static void failNotMatchingArrayTypes(Object expected, Object actual)
      throws AssertionFailedError {
    throw new AssertionFailedError("Expected matching array types but was: \<"
        + getClassName(expected) + "> and \<" + getClassName(actual) + ">");
  }<types,Types:{type,Type |
<\n>
  /**
   * Asserts the actual object is a primitive <type> array.
   *
   * \<p>If false then fail using the array type of the expected object in the message.
   *
   * @param expected The expected object (should be a primitive <type> array).
   * @param actual The actual object.
   */
  private static void assert<Type>Array(Object expected, Object actual) {
    if (!(actual instanceof <type>[])) {
      failNotMatchingArrayTypes(expected, actual);
    \}
  \}

  /**
   * Asserts the test predicate is a <Type><Type>BiPredicate.
   *
   * @param test The test.
   */
  private static void assert<Type><Type>BiPredicate(PrimitiveBiPredicate test) {
    if (!(test instanceof <Type><Type>BiPredicate)) {
      throw new AssertionFailedError(
          "Expected <Type><Type>BiPredicate but was: \<" + getClassName(test) + ">");
    \}
  \}}>

  /**
   * \<em>Asserts\</em> the test on an element from an Object[] array.
   *
   * \<p>If the element is a array then the appropriate assertArrayTest method is invoked,
   * otherwise fails.
   *
   * @param value The value.
   * @param test The test.
   */
  private static void assertArrayElement(Object value, PrimitivePredicate test,
      Deque\<Integer> indexes, Object messageOrSupplier) {
    if (value instanceof Object[]) {
      assertArrayTest((Object[]) value, test, indexes, messageOrSupplier);
    <types,Types:{type,Type |
\} else if (value instanceof <type>[]) {
  assert<Type>Predicate(test);
  assertArrayTest((<type>[]) value, (<Type>Predicate) test, indexes, messageOrSupplier);
}>    } else {
      throw new AssertionFailedError("Expected primitive array but was: \<"
          + getClassName(value) + ">");
    }
  }<types,Types:{type,Type |
<\n>
  /**
   * Asserts the test predicate is a <Type>Predicate.
   *
   * @param test The test.
   */
  private static void assert<Type>Predicate(PrimitivePredicate test) {
    if (!(test instanceof <Type>Predicate)) {
      throw new AssertionFailedError(
          "Expected <Type>Predicate but was: \<" + getClassName(test) + ">");
    \}
  \}}>

  /**
   * Gets the class name of the object.
   *
   * @param obj The object.
   * @return the class name
   */
  static String getClassName(Object obj) {
    // Taken from AssertionUtils
    return (obj == null ? "null"
        : obj instanceof Class ? getCanonicalName((Class\<?>) obj) : obj.getClass().getName());
  }

  // Taken from AssertionUtils
  private static String getCanonicalName(Class\<?> clazz) {
    try {
      final String canonicalName = clazz.getCanonicalName();
      return (canonicalName == null ? clazz.getName() : canonicalName);
    } catch (final Throwable thrown) {
      return clazz.getName();
    }
  }

  // Taken from AssertArrayEquals
  private static void assertArraysNotNull(Object expected, Object actual, Deque\<Integer> indexes,
      Object messageOrSupplier) {
    if (expected == null) {
      throw new AssertionFailedError(
          TestAssertionUtils.buildPrefix(StringUtils.toString(messageOrSupplier))
              + "expected array was \<null>" + TestAssertionUtils.formatIndexes(indexes));
    }
    if (actual == null) {
      throw new AssertionFailedError(
          TestAssertionUtils.buildPrefix(StringUtils.toString(messageOrSupplier))
              + "actual array was \<null>" + TestAssertionUtils.formatIndexes(indexes));
    }
  }

  // Taken from AssertArrayEquals
  private static void assertArrayNotNull(Object value, Deque\<Integer> indexes,
      Object messageOrSupplier) {
    if (value == null) {
      throw new AssertionFailedError(
          TestAssertionUtils.buildPrefix(StringUtils.toString(messageOrSupplier))
              + "value array was \<null>" + TestAssertionUtils.formatIndexes(indexes));
    }
  }

  // Taken from AssertArrayEquals
  private static void assertArraysHaveSameLength(int expected, int actual, Deque\<Integer> indexes,
      Object messageOrSupplier) {
    if (expected != actual) {
      final String prefix = TestAssertionUtils.buildPrefix(
          StringUtils.toString(messageOrSupplier));
      final String message = "array lengths differ" + TestAssertionUtils.formatIndexes(indexes)
          + ", expected: \<" + expected + "> but was: \<" + actual + ">";
      throw new AssertionFailedError(prefix + message);
    }
  }

  // Taken from AssertArrayEquals
  private static Deque\<Integer> nullSafeIndexes(Deque\<Integer> indexes, int newIndex) {
    final Deque\<Integer> result = (indexes == null ? new ArrayDeque\<>() : indexes);
    result.addLast(newIndex);
    return result;
  }

  /**
   * Build a message when the contents of two arrays differ at the provided indexes.
   *
   * @param indexes The indexes.
   * @param formattedValues The formatted values.
   * @return The message
   */
  private static String buildContentsDifferMessage(Deque\<Integer> indexes, String formattedValues) {
    return "array contents differ" + TestAssertionUtils.formatIndexes(indexes) + ", "
        + formattedValues;
  }

  /**
   * Build a message when the content of one array fails at the provided indexes.
   *
   * @param indexes The indexes.
   * @param formattedValue The formatted value.
   * @return The message
   */
  private static String buildContentsFailMessage(Deque\<Integer> indexes, String formattedValue) {
    return "array content" + TestAssertionUtils.formatIndexes(indexes) + ", " + formattedValue;
  }<types,Types:{type,Type |
<\n>
  private static String failArraysMessage(<type> expected, <type> actual,
      <Type><Type>BiPredicate test, Deque\<Integer> indexes, Object messageOrSupplier) {
    final String prefix = TestAssertionUtils.buildPrefix(StringUtils.toString(messageOrSupplier));
    final String message = buildContentsDifferMessage(indexes,
        TestAssertionUtils.formatValues(expected, actual, test));
    return prefix + message;
  \}}><types,Types:{type,Type |
  <\n>
  private static String failArrayMessage(<type> value, <Type>Predicate test,
      Deque\<Integer> indexes, Object messageOrSupplier) {
    final String prefix = TestAssertionUtils.buildPrefix(StringUtils.toString(messageOrSupplier));
    final String message = buildContentsFailMessage(indexes,
        TestAssertionUtils.formatValue(value, test));
    return prefix + message;
  \}}>
}
