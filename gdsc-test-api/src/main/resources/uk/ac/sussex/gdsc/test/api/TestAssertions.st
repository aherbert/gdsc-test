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

  /**
   * Do not allow public construction.
   */
  private TestAssertions() {}

  /**
   * \<em>Asserts\</em> the test between {@code expected} and {@code actual}.
   *
   * \<p>This is a helper function for nested primitive arrays, e.g. {@code double[][][]}. The array
   * is descended recursively and if an entry is a primitive array then the test is cast to the
   * appropriate primitive test.
   *
   * @param expected The expected value.
   * @param actual The value to test against \<code>expected\</code>.
   * @param test The test.
   * @throws AssertionError If the test failed or the primitive test was not the correct type.
   */
  public static void assertArrayTest(Object[] expected, Object[] actual,
      PrimitiveBiPredicate test) {
    TestAssertionArrayUtils.assertArrayTest(expected, actual, test);
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
   * @param expected The expected value.
   * @param actual The value to test against \<code>expected\</code>.
   * @param test The test.
   * @param message The message.
   * @throws AssertionError If the test failed or the primitive test was not the correct type.
   */
  public static void assertArrayTest(Object[] expected, Object[] actual, PrimitiveBiPredicate test,
      String message) {
    TestAssertionArrayUtils.assertArrayTest(expected, actual, test, message);
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
   * @param expected The expected value.
   * @param actual The value to test against \<code>expected\</code>.
   * @param test The test.
   * @param messageSupplier The message supplier.
   * @throws AssertionError If the test failed or the primitive test was not the correct type.
   */
  public static void assertArrayTest(Object[] expected, Object[] actual, PrimitiveBiPredicate test,
      Supplier\<String> messageSupplier) {
    TestAssertionArrayUtils.assertArrayTest(expected, actual, test, messageSupplier);
  }

  /**
   * \<em>Asserts\</em> the test on {@code value}.
   *
   * \<p>This is a helper function for nested primitive arrays, e.g. {@code double[][][]}. The array
   * is descended recursively and if an entry is a primitive array then the test is cast to the
   * appropriate primitive test.
   *
   * @param value The value.
   * @param test The test.
   * @throws AssertionError If the test failed or the primitive test was not the correct type.
   */
  public static void assertArrayTest(Object[] value, PrimitivePredicate test) {
    TestAssertionArrayUtils.assertArrayTest(value, test);
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
   * @param test The test.
   * @param message The message.
   * @throws AssertionError If the test failed or the primitive test was not the correct type.
   */
  public static void assertArrayTest(Object[] value, PrimitivePredicate test, String message) {
    TestAssertionArrayUtils.assertArrayTest(value, test, message);
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
   * @param test The test.
   * @param messageSupplier The message supplier.
   * @throws AssertionError If the test failed or the primitive test was not the correct type.
   */
  public static void assertArrayTest(Object[] value, PrimitivePredicate test,
      Supplier\<String> messageSupplier) {
    TestAssertionArrayUtils.assertArrayTest(value, test, messageSupplier);
  }<types,Types:{type,Type |
<\n>
  /////////////////////////////////////
  // <type>
  /////////////////////////////////////

  /**
   * \<em>Asserts\</em> the test between {@code expected\} and {@code actual\}.
   *
   * @param expected The expected value.
   * @param actual The value to test against \<code>expected\</code>.
   * @param test The test.
   * @throws AssertionError If the test failed or the primitive test was not the correct type.
   */
  public static void assertArrayTest(<type>[] expected, <type>[] actual,
      <Type><Type>BiPredicate test) {
    TestAssertionArrayUtils.assertArrayTest(expected, actual, test, null, (String) null);
  \}

  /**
   * \<em>Asserts\</em> the test between {@code expected\} and {@code actual\}.
   *
   * \<p>Fails with the supplied failure {@code message\}.
   *
   * @param expected The expected value.
   * @param actual The value to test against \<code>expected\</code>.
   * @param test The test.
   * @param message The message.
   * @throws AssertionError If the test failed or the primitive test was not the correct type.
   */
  public static void assertArrayTest(<type>[] expected, <type>[] actual,
      <Type><Type>BiPredicate test, String message) {
    TestAssertionArrayUtils.assertArrayTest(expected, actual, test, null, message);
  \}

  /**
   * \<em>Asserts\</em> the test between {@code expected\} and {@code actual\}.
   *
   * \<p>If necessary, the failure message will be retrieved lazily from the supplied
   * {@code messageSupplier\}.
   *
   * @param expected The expected value.
   * @param actual The value to test against \<code>expected\</code>.
   * @param test The test.
   * @param messageSupplier The message supplier.
   * @throws AssertionError If the test failed or the primitive test was not the correct type.
   */
  public static void assertArrayTest(<type>[] expected, <type>[] actual,
      <Type><Type>BiPredicate test, Supplier\<String> messageSupplier) {
    TestAssertionArrayUtils.assertArrayTest(expected, actual, test, null, messageSupplier);
  \}

  /**
   * \<em>Asserts\</em> the test on {@code value\}.
   *
   * @param value The value to test.
   * @param test The test.
   * @throws AssertionError If the test failed or the primitive test was not the correct type.
   */
  public static void assertArrayTest(<type>[] value, <Type>Predicate test) {
    TestAssertionArrayUtils.assertArrayTest(value, test, null, (String) null);
  \}

  /**
   * \<em>Asserts\</em> the test on {@code value\}.
   *
   * \<p>Fails with the supplied failure {@code message\}.
   *
   * @param value The value to test.
   * @param test The test.
   * @param message The message.
   * @throws AssertionError If the test failed or the primitive test was not the correct type.
   */
  public static void assertArrayTest(<type>[] value, <Type>Predicate test, String message) {
    TestAssertionArrayUtils.assertArrayTest(value, test, null, message);
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
   * @throws AssertionError If the test failed or the primitive test was not the correct type.
   */
  public static void assertArrayTest(<type>[] value, <Type>Predicate test,
      Supplier\<String> messageSupplier) {
    TestAssertionArrayUtils.assertArrayTest(value, test, null, messageSupplier);
  \}}><types,Types:{type,Type |
<\n>
  /////////////////////////////////////
  // <type>
  /////////////////////////////////////

  /**
   * \<em>Asserts\</em> the test between {@code expected\} and {@code actual\}.
   *
   * @param expected The expected value.
   * @param actual The value to test against \<code>expected\</code>.
   * @param test The test.
   * @throws AssertionError If the test failed or the primitive test was not the correct type.
   */
  public static void assertTest(<type> expected, <type> actual, <Type><Type>BiPredicate test) {
    TestAssertionUtils.assertTest(expected, actual, test, (String) null);
  \}

  /**
   * \<em>Asserts\</em> the test between {@code expected\} and {@code actual\}.
   *
   * \<p>Fails with the supplied failure {@code message\}.
   *
   * @param expected The expected value.
   * @param actual The value to test against \<code>expected\</code>.
   * @param test The test.
   * @param message The message.
   * @throws AssertionError If the test failed or the primitive test was not the correct type.
   */
  public static void assertTest(<type> expected, <type> actual, <Type><Type>BiPredicate test,
      String message) {
    TestAssertionUtils.assertTest(expected, actual, test, message);
  \}

  /**
   * \<em>Asserts\</em> the test between {@code expected\} and {@code actual\}.
   *
   * \<p>If necessary, the failure message will be retrieved lazily from the supplied
   * {@code messageSupplier\}.
   *
   * @param expected The expected value.
   * @param actual The value to test against \<code>expected\</code>.
   * @param test The test.
   * @param messageSupplier The message supplier.
   * @throws AssertionError If the test failed or the primitive test was not the correct type.
   */
  public static void assertTest(<type> expected, <type> actual, <Type><Type>BiPredicate test,
      Supplier\<String> messageSupplier) {
    TestAssertionUtils.assertTest(expected, actual, test, messageSupplier);
  \}

  /**
   * \<em>Asserts\</em> the test on {@code value\}.
   *
   * @param value The value to test.
   * @param test The test.
   * @throws AssertionError If the test failed or the primitive test was not the correct type.
   */
  public static void assertTest(<type> value, <Type>Predicate test) {
    TestAssertionUtils.assertTest(value, test, (String) null);
  \}

  /**
   * \<em>Asserts\</em> the test on {@code value\}.
   *
   * \<p>Fails with the supplied failure {@code message\}.
   *
   * @param value The value to test.
   * @param test The test.
   * @param message The message.
   * @throws AssertionError If the test failed or the primitive test was not the correct type.
   */
  public static void assertTest(<type> value, <Type>Predicate test, String message) {
    TestAssertionUtils.assertTest(value, test, message);
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
   * @throws AssertionError If the test failed or the primitive test was not the correct type.
   */
  public static void assertTest(<type> value, <Type>Predicate test,
      Supplier\<String> messageSupplier) {
    TestAssertionUtils.assertTest(value, test, messageSupplier);
  \}}>
}
