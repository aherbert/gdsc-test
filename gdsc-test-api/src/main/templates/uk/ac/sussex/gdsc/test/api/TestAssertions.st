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

import java.util.function.Supplier;

/**
 * Adds helper assert functions for tests.
 *
 * @see AssertionError
 */
public final class TestAssertions {

  /** No instances. */
  private TestAssertions() {}

  /**
   * \<em>Asserts\</em> the test between {@code expected} and {@code actual}.
   *
   * \<p>This is a helper function for nested primitive arrays, e.g. {@code double[][][]}. The array
   * is descended recursively and if an entry is a primitive array then the test is cast to the
   * appropriate primitive test.
   *
   * @param expected the expected value
   * @param actual the actual value to test against {@code expected\}
   * @param test the test predicate
   * @throws AssertionError if the test failed or the primitive test was not the correct type
   */
  public static void assertArrayTest(Object[] expected, Object[] actual,
      PrimitiveBiPredicate test) {
    TestArrayAssertions.assertArrayTest(expected, actual, test);
  }

  /**
   * \<em>Asserts\</em> the test between {@code expected} and {@code actual}.
   *
   * \<p>This is a helper function for nested primitive arrays, e.g. {@code double[][][]}. The array
   * is descended recursively and if an entry is a primitive array then the test is cast to the
   * appropriate primitive test.
   *
   * \<p>Fails with the supplied failure {@code message}.
   *
   * @param expected the expected value
   * @param actual the actual value to test against {@code expected\}
   * @param test the test predicate
   * @param message the message
   * @throws AssertionError if the test failed or the primitive test was not the correct type
   */
  public static void assertArrayTest(Object[] expected, Object[] actual, PrimitiveBiPredicate test,
      String message) {
    TestArrayAssertions.assertArrayTest(expected, actual, test, message);
  }

  /**
   * \<em>Asserts\</em> the test between {@code expected} and {@code actual}.
   *
   * \<p>This is a helper function for nested primitive arrays, e.g. {@code double[][][]}. The array
   * is descended recursively and if an entry is a primitive array then the test is cast to the
   * appropriate primitive test.
   *
   * \<p>If necessary, the failure message will be retrieved lazily from the supplied
   * {@code messageSupplier}.
   *
   * @param expected the expected value
   * @param actual the actual value to test against {@code expected\}
   * @param test the test predicate
   * @param messageSupplier the message supplier
   * @throws AssertionError if the test failed or the primitive test was not the correct type
   */
  public static void assertArrayTest(Object[] expected, Object[] actual, PrimitiveBiPredicate test,
      Supplier\<String> messageSupplier) {
    TestArrayAssertions.assertArrayTest(expected, actual, test, messageSupplier);
  }

  /**
   * \<em>Asserts\</em> the test on {@code value}.
   *
   * \<p>This is a helper function for nested primitive arrays, e.g. {@code double[][][]}. The array
   * is descended recursively and if an entry is a primitive array then the test is cast to the
   * appropriate primitive test.
   *
   * @param value The value.
   * @param test the test predicate
   * @throws AssertionError if the test failed or the primitive test was not the correct type
   */
  public static void assertArrayTest(Object[] value, PrimitivePredicate test) {
    TestArrayAssertions.assertArrayTest(value, test);
  }

  /**
   * \<em>Asserts\</em> the test on {@code value}.
   *
   * \<p>This is a helper function for nested primitive arrays, e.g. {@code double[][][]}. The array
   * is descended recursively and if an entry is a primitive array then the test is cast to the
   * appropriate primitive test.
   *
   * \<p>Fails with the supplied failure {@code message}.
   *
   * @param value The value.
   * @param test the test predicate
   * @param message the message
   * @throws AssertionError if the test failed or the primitive test was not the correct type
   */
  public static void assertArrayTest(Object[] value, PrimitivePredicate test, String message) {
    TestArrayAssertions.assertArrayTest(value, test, message);
  }

  /**
   * \<em>Asserts\</em> the test on {@code value}.
   *
   * \<p>This is a helper function for nested primitive arrays, e.g. {@code double[][][]}. The array
   * is descended recursively and if an entry is a primitive array then the test is cast to the
   * appropriate primitive test.
   *
   * \<p>If necessary, the failure message will be retrieved lazily from the supplied
   * {@code messageSupplier}.
   *
   * @param value The value.
   * @param test the test predicate
   * @param messageSupplier the message supplier
   * @throws AssertionError if the test failed or the primitive test was not the correct type
   */
  public static void assertArrayTest(Object[] value, PrimitivePredicate test,
      Supplier\<String> messageSupplier) {
    TestArrayAssertions.assertArrayTest(value, test, messageSupplier);
  }<types,Types:{type,Type |
<\n>
  /////////////////////////////////////
  // <type>
  /////////////////////////////////////

  /**
   * \<em>Asserts\</em> the test between {@code expected\} and {@code actual\}.
   *
   * @param expected the expected value
   * @param actual the actual value to test against {@code expected\}
   * @param test the test predicate
   * @throws AssertionError if the test failed
   */
  public static void assertArrayTest(<type>[] expected, <type>[] actual,
      <Type><Type>BiPredicate test) {
    TestArrayAssertions.assertArrayTest(expected, actual, test, (Object) null, (Indices) null);
  \}

  /**
   * \<em>Asserts\</em> the test between {@code expected\} and {@code actual\}.
   *
   * \<p>Fails with the supplied failure {@code message\}.
   *
   * @param expected the expected value
   * @param actual the actual value to test against {@code expected\}
   * @param test the test predicate
   * @param message the message
   * @throws AssertionError if the test failed
   */
  public static void assertArrayTest(<type>[] expected, <type>[] actual,
      <Type><Type>BiPredicate test, String message) {
    TestArrayAssertions.assertArrayTest(expected, actual, test, message, (Indices) null);
  \}

  /**
   * \<em>Asserts\</em> the test between {@code expected\} and {@code actual\}.
   *
   * \<p>If necessary, the failure message will be retrieved lazily from the supplied
   * {@code messageSupplier\}.
   *
   * @param expected the expected value
   * @param actual the actual value to test against {@code expected\}
   * @param test the test predicate
   * @param messageSupplier the message supplier
   * @throws AssertionError if the test failed
   */
  public static void assertArrayTest(<type>[] expected, <type>[] actual,
      <Type><Type>BiPredicate test, Supplier\<String> messageSupplier) {
    TestArrayAssertions.assertArrayTest(expected, actual, test, messageSupplier, (Indices) null);
  \}

  /**
   * \<em>Asserts\</em> the test on {@code value\}.
   *
   * @param value the value to test
   * @param test the test predicate
   * @throws AssertionError if the test failed
   */
  public static void assertArrayTest(<type>[] value, <Type>Predicate test) {
    TestArrayAssertions.assertArrayTest(value, test, (Object) null, (Indices) null);
  \}

  /**
   * \<em>Asserts\</em> the test on {@code value\}.
   *
   * \<p>Fails with the supplied failure {@code message\}.
   *
   * @param value the value to test
   * @param test the test predicate
   * @param message the message
   * @throws AssertionError if the test failed
   */
  public static void assertArrayTest(<type>[] value, <Type>Predicate test, String message) {
    TestArrayAssertions.assertArrayTest(value, test, message, (Indices) null);
  \}

  /**
   * \<em>Asserts\</em> the test on {@code value\}.
   *
   * \<p>If necessary, the failure message will be retrieved lazily from the supplied
   * {@code messageSupplier\}.
   *
   * @param value the value to test
   * @param test the test predicate
   * @param messageSupplier the message supplier
   * @throws AssertionError if the test failed
   */
  public static void assertArrayTest(<type>[] value, <Type>Predicate test,
      Supplier\<String> messageSupplier) {
    TestArrayAssertions.assertArrayTest(value, test, messageSupplier, (Indices) null);
  \}}><types,Types:{type,Type |
<\n>
  /////////////////////////////////////
  // <type>
  /////////////////////////////////////

  /**
   * \<em>Asserts\</em> the test between {@code expected\} and {@code actual\}.
   *
   * @param expected the expected value
   * @param actual the actual value to test against {@code expected\}
   * @param test the test predicate
   * @throws AssertionError if the test failed
   */
  public static void assertTest(<type> expected, <type> actual, <Type><Type>BiPredicate test) {
    TestPrimitiveAssertions.assertTest(expected, actual, test, (String) null);
  \}

  /**
   * \<em>Asserts\</em> the test between {@code expected\} and {@code actual\}.
   *
   * \<p>Fails with the supplied failure {@code message\}.
   *
   * @param expected the expected value
   * @param actual the actual value to test against {@code expected\}
   * @param test the test predicate
   * @param message the message
   * @throws AssertionError if the test failed
   */
  public static void assertTest(<type> expected, <type> actual, <Type><Type>BiPredicate test,
      String message) {
    TestPrimitiveAssertions.assertTest(expected, actual, test, message);
  \}

  /**
   * \<em>Asserts\</em> the test between {@code expected\} and {@code actual\}.
   *
   * \<p>If necessary, the failure message will be retrieved lazily from the supplied
   * {@code messageSupplier\}.
   *
   * @param expected the expected value
   * @param actual the actual value to test against {@code expected\}
   * @param test the test predicate
   * @param messageSupplier the message supplier
   * @throws AssertionError if the test failed
   */
  public static void assertTest(<type> expected, <type> actual, <Type><Type>BiPredicate test,
      Supplier\<String> messageSupplier) {
    TestPrimitiveAssertions.assertTest(expected, actual, test, messageSupplier);
  \}

  /**
   * \<em>Asserts\</em> the test on {@code value\}.
   *
   * @param value the value to test
   * @param test the test predicate
   * @throws AssertionError if the test failed
   */
  public static void assertTest(<type> value, <Type>Predicate test) {
    TestPrimitiveAssertions.assertTest(value, test, (String) null);
  \}

  /**
   * \<em>Asserts\</em> the test on {@code value\}.
   *
   * \<p>Fails with the supplied failure {@code message\}.
   *
   * @param value the value to test
   * @param test the test predicate
   * @param message the message
   * @throws AssertionError if the test failed
   */
  public static void assertTest(<type> value, <Type>Predicate test, String message) {
    TestPrimitiveAssertions.assertTest(value, test, message);
  \}

  /**
   * \<em>Asserts\</em> the test on {@code value\}.
   *
   * \<p>If necessary, the failure message will be retrieved lazily from the supplied
   * {@code messageSupplier\}.
   *
   * @param value the value to test
   * @param test the test predicate
   * @param messageSupplier the message supplier
   * @throws AssertionError if the test failed
   */
  public static void assertTest(<type> value, <Type>Predicate test,
      Supplier\<String> messageSupplier) {
    TestPrimitiveAssertions.assertTest(value, test, messageSupplier);
  \}}>
}
