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
 * Adds helper assert functions for tests on arrays.
 */
final class TestArrayAssertions {

  /** No instances. */
  private TestArrayAssertions() {}

  //////////////////////////////////////////////////////
  // Support nested arrays of each primitive array type
  //////////////////////////////////////////////////////

  /**
   * \<em>Asserts\</em> the test between {@code expected} and {@code actual}.
   *
   * \<p>This supports nested primitive arrays, e.g. {@code double[][]}.
   *
   * @param expected the expected value
   * @param actual the actual value to check against {@code expected\}
   * @param test the test predicate
   */
  static void assertArrayTest(Object[] expected, Object[] actual, PrimitiveBiPredicate test) {
    assertArrayTest(expected, actual, test, (Object) null, new Indices());
  }

  /**
   * \<em>Asserts\</em> the test between {@code expected} and {@code actual}.
   *
   * \<p>Fails with the supplied failure {@code message}.
   *
   * \<p>This supports nested primitive arrays, e.g. {@code double[][]}.
   *
   * @param expected the expected value
   * @param actual the actual value to check against {@code expected\}
   * @param test the test predicate
   * @param message The message.
   */
  static void assertArrayTest(Object[] expected, Object[] actual, PrimitiveBiPredicate test,
      String message) {
    assertArrayTest(expected, actual, test, message, new Indices());
  }

  /**
   * \<em>Asserts\</em> the test between {@code expected} and {@code actual}.
   *
   * \<p>If necessary, the failure message will be retrieved lazily from the supplied
   * {@code messageSupplier\}.
   *
   * \<p>This supports nested primitive arrays, e.g. {@code double[][]}.
   *
   * @param expected the expected value
   * @param actual the actual value to check against {@code expected\}
   * @param test the test predicate
   * @param messageSupplier the message supplier
   */
  static void assertArrayTest(Object[] expected, Object[] actual, PrimitiveBiPredicate test,
      Supplier\<String> messageSupplier) {
    assertArrayTest(expected, actual, test, messageSupplier, new Indices());
  }

  /**
   * \<em>Asserts\</em> the test between {@code expected} and {@code actual}.
   *
   * \<p>If necessary, the failure message will be retrieved lazily from the supplied
   * {@code messageOrSupplier\}.
   *
   * \<p>This supports nested primitive arrays, e.g. {@code double[][]}.
   *
   * \<p>The precursor indices are used to construct the index path to the current pair of arrays.
   * This is used during recursive iteration of nested arrays.
   *
   * @param expected the expected value
   * @param actual the actual value to check against {@code expected\}
   * @param test the test predicate
   * @param messageSupplier the message supplier
   * @param indices the precursor indices (never null)
   */
  private static void assertArrayTest(Object[] expected, Object[] actual, PrimitiveBiPredicate test,
      Object messageOrSupplier, Indices indices) {
    if (expected == actual) {
      return;
    }
    assertNotNull(expected, actual, messageOrSupplier, indices);
    assertEqualLengths(expected.length, actual.length, messageOrSupplier, indices);

    for (int i = 0; i \< expected.length; i++) {
      final Object e = expected[i];
      final Object a = actual[i];

      if (e == a) {
        // Fast skip of the same object
        continue;
      }

      indices.push(i);
      assertArrayElements(e, a, test, messageOrSupplier, indices);
      indices.pop();
    }
  }

  /**
   * \<em>Asserts\</em> the test on {@code value\}.
   *
   * \<p>This supports nested primitive arrays, e.g. {@code double[][]}.
   *
   * @param value the value
   * @param test the test predicate
   */
  static void assertArrayTest(Object[] value, PrimitivePredicate test) {
    assertArrayTest(value, test, (Object) null, new Indices());
  }

  /**
   * \<em>Asserts\</em> the test on {@code value\}.
   *
   * \<p>Fails with the supplied failure {@code message}.
   *
   * \<p>This supports nested primitive arrays, e.g. {@code double[][]}.
   *
   * @param value the value
   * @param test the test predicate
   * @param message The message.
   */
  static void assertArrayTest(Object[] value, PrimitivePredicate test,
      String message) {
    assertArrayTest(value, test, message, new Indices());
  }

  /**
   * \<em>Asserts\</em> the test on {@code value\}.
   *
   * \<p>If necessary, the failure message will be retrieved lazily from the supplied
   * {@code messageSupplier\}.
   *
   * \<p>This supports nested primitive arrays, e.g. {@code double[][]}.
   *
   * @param value the value
   * @param test the test predicate
   * @param messageSupplier the message supplier
   */
  static void assertArrayTest(Object[] value, PrimitivePredicate test,
      Supplier\<String> messageSupplier) {
    assertArrayTest(value, test, messageSupplier, new Indices());
  }

  /**
   * \<em>Asserts\</em> the test on {@code value\}.
   *
   * \<p>If necessary, the failure message will be retrieved lazily from the supplied
   * {@code messageOrSupplier\}.
   *
   * \<p>This supports nested primitive arrays, e.g. {@code double[][]}.
   *
   * \<p>The precursor indices are used to construct the index path to the current pair of arrays.
   * This is used during recursive iteration of nested arrays.
   *
   * @param value the value
   * @param test the test predicate
   * @param messageSupplier the message supplier
   * @param indices the precursor indices (never null)
   */
  private static void assertArrayTest(Object[] value, PrimitivePredicate test,
      Object messageOrSupplier, Indices indices) {
    assertNotNull(value, messageOrSupplier, indices);

    for (int i = 0; i \< value.length; i++) {
      final Object v = value[i];

      indices.push(i);
      assertArrayElement(v, test, messageOrSupplier, indices);
      indices.pop();
    }
  }<types,Types:{type,Type |
<\n>
  /////////////////////////////////////
  // <type>
  /////////////////////////////////////

  /**
   * \<em>Asserts\</em> the test between {@code expected\} and {@code actual\}.
   *
   * \<p>Fails with the supplied failure message.
   *
   * @param expected the expected value
   * @param actual the actual value to check against {@code expected\}
   * @param test the test predicate
   * @param messageOrSupplier the message or supplier
   * @param indices the indices
   */
  static void assertArrayTest(<type>[] expected, <type>[] actual,
      <Type><Type>BiPredicate test, Object messageOrSupplier, Indices indices) {
    if (expected == actual) {
      return;
    \}
    assertNotNull(expected, actual, messageOrSupplier, indices);
    assertEqualLengths(expected.length, actual.length, messageOrSupplier, indices);

    for (int i = 0; i \< expected.length; i++) {
      if (!test.test(expected[i], actual[i])) {
        throw new AssertionError(
            MessageBuilder.of(messageOrSupplier).append("Array contents fail")
                .append(indices, i).append(", ").append(expected[i], actual[i], test).toString());
      \}
    \}
  \}

  /**
   * \<em>Asserts\</em> the test on {@code value\}.
   *
   * \<p>Fails with the supplied failure message.
   *
   * @param value the value
   * @param test the test predicate
   * @param messageOrSupplier the message or supplier
   * @param indices the indices
   */
  static void assertArrayTest(<type>[] value, <Type>Predicate test,
      Object messageOrSupplier, Indices indices) {
    assertNotNull(value, messageOrSupplier, indices);

    for (int i = 0; i \< value.length; i++) {
      if (!test.test(value[i])) {
        throw new AssertionError(
            MessageBuilder.of(messageOrSupplier).append("Array content fails")
                .append(indices, i).append(", ").append(value[i], test).toString());
      \}
    \}
  \}}>

  /**
   * \<em>Asserts\</em> the test between two elements from Object[] arrays.
   *
   * \<p>If the elements are matching array types then the appropriate assertArrayTest method is
   * invoked, otherwise fails.
   *
   * @param expected the expected value
   * @param actual the actual value to check against {@code expected\}
   * @param test the test predicate
   * @param messageOrSupplier the message or supplier
   * @param indices the indices
   */
  private static void assertArrayElements(Object expected, Object actual, PrimitiveBiPredicate test,
      Object messageOrSupplier, Indices indices) {
    if (expected instanceof Object[]) {
      assertObjectArray(expected, actual, messageOrSupplier, indices);
      assertArrayTest((Object[]) expected, (Object[]) actual, test, messageOrSupplier, indices);
    <types,Types:{type,Type |
\} else if (expected instanceof <type>[]) {
  assert<Type>Array(expected, actual, messageOrSupplier, indices);
  assert<Type><Type>BiPredicate(test);
  assertArrayTest((<type>[]) expected, (<type>[]) actual, (<Type><Type>BiPredicate) test,
      messageOrSupplier, indices);
}>    } else {
      throw new AssertionError(
          MessageBuilder.of(messageOrSupplier).append("Non-array type for expected")
              .append(indices).append(", ")
              .appendFormatted(Classes.getClassName(expected)).toString());
    }
  }

  /**
   * Builds the message used when two input arrays are not the same type.
   *
   * @param expected the expected object
   * @param actual the actual object
   * @param messageOrSupplier the message or supplier
   * @param indices the indices
   */
  private static String buildNonMatchingArrayTypesMessage(Object expected, Object actual,
      Object messageOrSupplier, Indices indices) {
    return MessageBuilder.of(messageOrSupplier).append("Invalid type")
        .append(indices).append(", ")
        .appendFormatted(Classes.getArrayClassName(expected),
                         Classes.getArrayClassName(actual)).toString();
  }

  /**
   * Asserts the actual object is a Object array.
   *
   * \<p>If false then fail using the array type of the expected object in the message.
   *
   * @param expected the expected object (should be an Object array)
   * @param actual the actual object
   * @param messageOrSupplier the message or supplier
   * @param indices the indices
   */
  private static void assertObjectArray(Object expected, Object actual,
      Object messageOrSupplier, Indices indices) {
    if (!(actual instanceof Object[])) {
      throw new AssertionError(buildNonMatchingArrayTypesMessage(expected, actual,
          messageOrSupplier, indices));
    }
  }<types,Types:{type,Type |
<\n>
  /**
   * Asserts the actual object is a primitive <type> array.
   *
   * \<p>If false then fail using the array type of the expected object in the message.
   *
   * @param expected the expected object (should be a primitive <type> array)
   * @param actual the actual object
   * @param messageOrSupplier the message or supplier
   * @param indices the indices
   */
  private static void assert<Type>Array(Object expected, Object actual,
      Object messageOrSupplier, Indices indices) {
    if (!(actual instanceof <type>[])) {
      throw new AssertionError(buildNonMatchingArrayTypesMessage(expected, actual,
          messageOrSupplier, indices));
    \}
  \}}>

  /**
   * \<em>Asserts\</em> the test on an element from an Object[] array.
   *
   * \<p>If the element is a array then the appropriate assertArrayTest method is invoked,
   * otherwise fails.
   *
   * @param value the value
   * @param test the test predicate
   */
  private static void assertArrayElement(Object value, PrimitivePredicate test,
      Object messageOrSupplier, Indices indices) {
    if (value instanceof Object[]) {
      assertArrayTest((Object[]) value, test, messageOrSupplier, indices);
    <types,Types:{type,Type |
\} else if (value instanceof <type>[]) {
  assert<Type>Predicate(test);
  assertArrayTest((<type>[]) value, (<Type>Predicate) test, messageOrSupplier, indices);
}>    } else {
      throw new AssertionError(
          MessageBuilder.of(messageOrSupplier).append("Non-array type")
              .append(indices).append(", ")
              .appendFormatted(Classes.getClassName(value)).toString());
    }
  }

  /**
   * Builds the message used when the predicate is not the expected type.
   *
   * @param expected the expected type
   * @param actual the actual object
   */
  private static String buildInvalidPredicateMessage(Object expected, Object actual) {
    return MessageBuilder.create().append("Invalid predicate, ")
        .appendFormatted(Classes.getArrayClassName(expected),
                         Classes.getArrayClassName(actual)).toString();
  }<types,Types:{type,Type |
<\n>
  /**
   * Asserts the test predicate is a <Type><Type>BiPredicate.
   *
   * @param test the test predicate
   */
  private static void assert<Type><Type>BiPredicate(PrimitiveBiPredicate test) {
    if (!(test instanceof <Type><Type>BiPredicate)) {
      throw new AssertionError(
          buildInvalidPredicateMessage(<Type><Type>BiPredicate.class, test).toString());
    \}
  \}

  /**
   * \<em>Asserts\</em> the test predicate is a <Type>Predicate.
   *
   * @param test the test predicate
   */
  private static void assert<Type>Predicate(PrimitivePredicate test) {
    if (!(test instanceof <Type>Predicate)) {
      throw new AssertionError(
          buildInvalidPredicateMessage(<Type>Predicate.class, test).toString());
    \}
  \}}>

  /**
   * \<em>Asserts\</em> the two array objects are not null.
   *
   * @param expected the expected value
   * @param actual the actual value to check against {@code expected\}
   * @param messageSupplier the message supplier
   * @param indices the precursor indices (never null)
   */
  private static void assertNotNull(Object expected, Object actual, Object messageOrSupplier,
      Indices indices) {
    if (expected == null) {
      throw new AssertionError(MessageBuilder.of(messageOrSupplier)
          .appendNull("expected array").append(indices).toString());
    }
    if (actual == null) {
      throw new AssertionError(MessageBuilder.of(messageOrSupplier)
          .appendNull("actual array").append(indices).toString());
    }
  }

  /**
   * \<em>Asserts\</em> the value array is not null.
   *
   * @param value the value
   * @param messageSupplier the message supplier
   * @param indices the precursor indices (never null)
   */
  private static void assertNotNull(Object value, Object messageOrSupplier,
      Indices indices) {
    if (value == null) {
      throw new AssertionError(MessageBuilder.of(messageOrSupplier)
          .appendNull("value array").append(indices).toString());
    }
  }

  /**
   * \<em>Asserts\</em> the two array lengths are equal.
   *
   * @param expected the expected value
   * @param actual the actual value to check against {@code expected\}
   * @param messageSupplier the message supplier
   * @param indices the precursor indices (never null)
   */
  private static void assertEqualLengths(int expected, int actual, Object messageOrSupplier,
      Indices indices) {
    if (expected != actual) {
      throw new AssertionError(MessageBuilder.of(messageOrSupplier)
          .append("Array lengths differ").append(indices).append(", ")
          .append(expected, actual, (IntIntBiPredicate) null).toString());
    }
  }
}
