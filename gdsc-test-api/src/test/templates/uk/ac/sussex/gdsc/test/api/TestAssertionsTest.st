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

import java.util.ArrayList;
import java.util.function.Supplier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link TestAssertions}.
 */
public class TestAssertionsTest {

  private String testMessage = "ashjdkfksahk";
  private Supplier\<String> testMessageSupplier = () -> "125636438274";

  /**
   * Simple class that fails to implement the desired primitive specialisation of the predicate.
   */
  private static class BadPredicate implements PrimitivePredicate, PrimitiveBiPredicate {
    // Do nothing
  }

  /**
   * Test {@link TestAssertions#assertArrayTest(Object[], PrimitivePredicate)}
   * for an Object[] array, i.e. not a nested primitive array.
   */
  @Test
  public void testAssertTestWithNullMessageSupplier() {
    // Fail the test
    IntPredicate predicate = (v) -> false;
    Supplier\<String> nullMessageSupplier = null;

    int value1 = 12345;
    String message = Assertions.assertThrows(AssertionError.class, () -> {
      TestAssertions.assertTest(value1, predicate, nullMessageSupplier);
    }, "Does not throw with failing predicate").getMessage();
    String valueString1 = Integer.toString(value1);
    assertMessageContains(message, valueString1, "the value1");
  }

  /**
   * Assert the message contains the sub-string.
   *
   * \<p>If missing fails with a description of the sub-string.
   *
   * @param message the message
   * @param subString the sub-string
   * @param description the description
   */
  private static void assertMessageContains(String message, String subString, String description) {
    Assertions.assertTrue(message.contains(subString), () -> String.format(
        "Message '%s' is missing '%s' (%s)", message, subString, description));
  }

  /**
   * Test {@link TestAssertions#assertArrayTest(Object[], PrimitivePredicate)}
   * for an Object[] array, i.e. not a nested primitive array.
   */
  @Test
  public void testAssertObjectArrayTestWithObjectArrays() {
    // The predicate does not matter
    PrimitivePredicate predicate = new BadPredicate();

    Object[] value1 = new Object[1][1];
    String message = Assertions.assertThrows(AssertionError.class, () -> {
      TestAssertions.assertArrayTest(value1, predicate);
    }, "Does not throw with nested Object[] array").getMessage();
    assertMessageContains(message, "Non-array type", "the failure reason");
  }

  /**
   * Test {@link TestAssertions#assertArrayTest(Object[], Object[], PrimitiveBiPredicate)}
   * for Object[] arrays that have mismatched types.
   */
  @Test
  public void testAssertObjectArrayTestWithMismatchedObjectArrays() {
    // Create a list of all nested array types
    ArrayList\<Object[]> list = new ArrayList\<>();
    list.add(new boolean[1][1]);
    list.add(new byte[1][1]);
    list.add(new char[1][1]);
    list.add(new double[1][1]);
    list.add(new float[1][1]);
    list.add(new int[1][1]);
    list.add(new long[1][1]);
    list.add(new short[1][1]);
    list.add(new Object[1][1]);
    list.add(new Object[1]);

    // The predicate does not matter
    PrimitiveBiPredicate predicate = new BadPredicate();

    // Test mismatched types
    int index2 = list.size() - 1;
    for (int index1 = 0; index1 \< list.size(); index1++) {
      Object[] value1 = list.get(index1);
      Object[] value2 = list.get(index2);
      index2 = index1;
      String message = Assertions.assertThrows(AssertionError.class, () -> {
        TestAssertions.assertArrayTest(value1, value2, predicate);
      }, "Does not throw with nested arrays with mismatched types").getMessage();
      if (value1[0] == null) {
        // First element is not an array
        assertMessageContains(message, "Non-array type", "the failure reason");
      } else {
        // Second element mismatch
        assertMessageContains(message, "Invalid type", "the failure reason");
      }
    }
  }<Types,types,dummyValues1,dummyValues2:{Type,type,dummyValue1,dummyValue2 |
<\n>
  /**
   * Simple class to result a fixed test result and description message.
   */
  private static class Dummy<Type>Predicate
      implements <Type>Predicate, Supplier\<String> {

    /** The limit above which the predicate will fail. */
    private final int limit;
    /** Count of number of times the predicate test method has been used. */
    private int count;

    private Dummy<Type>Predicate(int limit) {
      this.limit = limit;
    \}

    @Override
    public boolean test(<type> value1) {
      return count++ \< limit;
    \}

    @Override
    public String get() {
      return "<Type> limit:" + limit;
    \}
  \}

  /**
   * Simple class to result a fixed test result and description message.
   */
  private static class Dummy<Type><Type>BiPredicate
      implements <Type><Type>BiPredicate, Supplier\<String> {

    /** The count limit above which the predicate will fail. */
    private final int limit;
    /** Count of number of times the predicate test method has been used. */
    private int count;

    private Dummy<Type><Type>BiPredicate(int limit) {
      this.limit = limit;
    \}

    @Override
    public boolean test(<type> value1, <type> value2) {
      return count++ \< limit;
    \}

    @Override
    public String get() {
      return "<Type><Type>Bi limit:" + limit;
    \}
  \}

  /**
   * Test {@link TestAssertions\} for a single primitive {@code <type>\} type.
   */
  @Test
  public void testAssertTest<Type>Predicate() {
    String message;
    <type> value = <dummyValue1>;

    // No message
    Assertions.assertDoesNotThrow(() -> {
      TestAssertions.assertTest(value, new Dummy<Type>Predicate(1));
    \}, "Throws with passed test");
    message = Assertions.assertThrows(AssertionError.class, () -> {
      TestAssertions.assertTest(value, new Dummy<Type>Predicate(0));
    \}, "Do not throw with failed test").getMessage();
    String valueString = String.valueOf(<dummyValue1>);
    assertMessageContains(message, valueString, "the value");
    assertMessageContains(message, "<Type> limit", "the predicate description");

    // String message
    Assertions.assertDoesNotThrow(() -> {
      TestAssertions.assertTest(value, new Dummy<Type>Predicate(1), testMessage);
    \}, "Throws with passed test with message");
    message = Assertions.assertThrows(AssertionError.class, () -> {
      TestAssertions.assertTest(value, new Dummy<Type>Predicate(0), testMessage);
    \}, "Do not throw with failed test with message").getMessage();
    assertMessageContains(message, valueString, "the value");
    assertMessageContains(message, "<Type> limit", "the predicate description");
    assertMessageContains(message, testMessage, "the message");

    // Message supplier
    Assertions.assertDoesNotThrow(() -> {
      TestAssertions.assertTest(value, new Dummy<Type>Predicate(1), testMessageSupplier);
    \}, "Throws with passed test with message supplier");
    message = Assertions.assertThrows(AssertionError.class, () -> {
      TestAssertions.assertTest(value, new Dummy<Type>Predicate(0), testMessageSupplier);
    \}, "Do not throw with failed test with message supplier").getMessage();
    assertMessageContains(message, valueString, "the value");
    assertMessageContains(message, "<Type> limit", "the predicate description");
    assertMessageContains(message, testMessageSupplier.get(), "the supplied message");

    // Test with a custom predicate which does not implement Supplier\<String> (i.e. no description)
    <Type>Predicate customPredicate = (v) -> false;
    message = Assertions.assertThrows(AssertionError.class, () -> {
      TestAssertions.assertTest(value, customPredicate);
    \}, "Does not throw with custom predicate").getMessage();
    assertMessageContains(message, valueString, "the value");
  \}

  /**
   * Test {@link TestAssertions\} for dual primitive {@code <type>\} types.
   */
  @Test
  public void testAssertTest<Type><Type>BiPredicate() {
    String message;
    <type> value1 = <dummyValue1>;
    <type> value2 = <dummyValue2>;

    // No message
    Assertions.assertDoesNotThrow(() -> {
      TestAssertions.assertTest(value1, value2, new Dummy<Type><Type>BiPredicate(1));
    \}, "Throws with passed test");
    message = Assertions.assertThrows(AssertionError.class, () -> {
      TestAssertions.assertTest(value1, value2, new Dummy<Type><Type>BiPredicate(0));
    \}, "Do not throw with failed test").getMessage();
    String valueString1 = String.valueOf(<dummyValue1>);
    String valueString2 = String.valueOf(<dummyValue2>);
    assertMessageContains(message, valueString1, "the expected value");
    assertMessageContains(message, valueString2, "the actual value");
    assertMessageContains(message, "<Type><Type>Bi limit", "the predicate description");

    // String message
    Assertions.assertDoesNotThrow(() -> {
      TestAssertions.assertTest(value1, value2, new Dummy<Type><Type>BiPredicate(1),
          testMessage);
    \}, "Throws with passed test with message");
    message = Assertions.assertThrows(AssertionError.class, () -> {
      TestAssertions.assertTest(value1, value2, new Dummy<Type><Type>BiPredicate(0),
          testMessage);
    \}, "Do not throw with failed test with message").getMessage();
    assertMessageContains(message, valueString1, "the expected value");
    assertMessageContains(message, valueString2, "the actual value");
    assertMessageContains(message, "<Type><Type>Bi limit", "the predicate description");
    assertMessageContains(message, testMessage, "the message");

    // Message supplier
    Assertions.assertDoesNotThrow(() -> {
      TestAssertions.assertTest(value1, value2, new Dummy<Type><Type>BiPredicate(1),
          testMessageSupplier);
    \}, "Throws with passed test with message supplier");
    message = Assertions.assertThrows(AssertionError.class, () -> {
      TestAssertions.assertTest(value1, value2, new Dummy<Type><Type>BiPredicate(0),
          testMessageSupplier);
    \}, "Do not throw with failed test with message supplier").getMessage();
    assertMessageContains(message, valueString1, "the expected value");
    assertMessageContains(message, valueString2, "the actual value");
    assertMessageContains(message, "<Type><Type>Bi limit", "the predicate description");
    assertMessageContains(message, testMessageSupplier.get(), "the supplied message");

    // Test with a custom predicate which does not implement Supplier\<String> (i.e. no description)
    <Type><Type>BiPredicate customPredicate = (v1, v2) -> false;
    message = Assertions.assertThrows(AssertionError.class, () -> {
      TestAssertions.assertTest(value1, value2, customPredicate);
    \}, "Does not throw with custom predicate").getMessage();
    assertMessageContains(message, valueString1, "the expected value");
    assertMessageContains(message, valueString2, "the actual value");
  \}

  /**
   * Test {@link TestAssertions\} for a single array of primitive {@code <type>\} type.
   */
  @Test
  public void testAssertArrayTest<Type>Predicate() {
    String message;
    <type>[] value = new <type>[]{ <dummyValue1> \};

    // No message
    Assertions.assertDoesNotThrow(() -> {
      TestAssertions.assertArrayTest(value, new Dummy<Type>Predicate(1));
    \}, "Throws with passed test");
    message = Assertions.assertThrows(AssertionError.class, () -> {
      TestAssertions.assertArrayTest(value, new Dummy<Type>Predicate(0));
    \}, "Does not throw with failed test").getMessage();
    String valueString = String.valueOf(<dummyValue1>);
    assertMessageContains(message, valueString, "the value");
    assertMessageContains(message, "<Type> limit", "the predicate description");

    // String message
    Assertions.assertDoesNotThrow(() -> {
      TestAssertions.assertArrayTest(value, new Dummy<Type>Predicate(1), testMessage);
    \}, "Throws with passed test with message");
    message = Assertions.assertThrows(AssertionError.class, () -> {
      TestAssertions.assertArrayTest(value, new Dummy<Type>Predicate(0), testMessage);
    \}, "Does not throw with failed test with message").getMessage();
    assertMessageContains(message, valueString, "the value");
    assertMessageContains(message, "<Type> limit", "the predicate description");
    assertMessageContains(message, testMessage, "the message");

    // Message supplier
    Assertions.assertDoesNotThrow(() -> {
      TestAssertions.assertArrayTest(value, new Dummy<Type>Predicate(1), testMessageSupplier);
    \}, "Throws with passed test with message supplier");
    message = Assertions.assertThrows(AssertionError.class, () -> {
      TestAssertions.assertArrayTest(value, new Dummy<Type>Predicate(0), testMessageSupplier);
    \}, "Does not throw with failed test with message supplier").getMessage();
    assertMessageContains(message, valueString, "the value");
    assertMessageContains(message, "<Type> limit", "the predicate description");
    assertMessageContains(message, testMessageSupplier.get(), "the supplied message");

    // Null array
    <type>[] nullValue = null;
    message = Assertions.assertThrows(AssertionError.class, () -> {
      TestAssertions.assertArrayTest(nullValue, new Dummy<Type>Predicate(1),
          testMessageSupplier);
    \}, "Does not throw with null array").getMessage();
    assertMessageContains(message, "value", "the failure was due to the value being null");
    assertMessageContains(message, "null", "the failure was due to the value being null");
    assertMessageContains(message, testMessageSupplier.get(), "the supplied message");

    // Test with a custom predicate which does not implement Supplier\<String> (i.e. no description)
    <Type>Predicate customPredicate = (v) -> false;
    message = Assertions.assertThrows(AssertionError.class, () -> {
      TestAssertions.assertArrayTest(value, customPredicate);
    \}, "Does not throw with custom predicate").getMessage();
    assertMessageContains(message, valueString, "the value");

    // Test the message contains the array index
    int size = 2;
    <type>[] multiValue = new <type>[size];
    for (int i = 0; i \< size; i++) {
      // This test enforces an array processing order on the implementation
      int index = i;
      message = Assertions.assertThrows(AssertionError.class, () -> {
        TestAssertions.assertArrayTest(multiValue, new Dummy<Type>Predicate(index));
      \}, () -> "Failed to throw at specified index: " + index).getMessage();
      assertMessageContains(message, String.format("at index [%d]", i), "the array index");
    \}
  \}

  /**
   * Test {@link TestAssertions\} for a two arrays of primitive {@code <type>\} type.
   */
  @Test
  public void testAssertArrayTest<Type><Type>BiPredicate() {
    String message;
    // Arrays must be different
    <type>[] value1 = new <type>[]{ <dummyValue1> \};
    <type>[] value2 = new <type>[]{ <dummyValue2> \};

    // Same array
    Assertions.assertDoesNotThrow(() -> {
      TestAssertions.assertArrayTest(value1, value1, new Dummy<Type><Type>BiPredicate(1));
    \}, "Throws with same array for expected and actual");

    // No message
    Assertions.assertDoesNotThrow(() -> {
      TestAssertions.assertArrayTest(value1, value2, new Dummy<Type><Type>BiPredicate(1));
    \}, "Throws with passed test");
    message = Assertions.assertThrows(AssertionError.class, () -> {
      TestAssertions.assertArrayTest(value1, value2, new Dummy<Type><Type>BiPredicate(0));
    \}, "Does not throw with failed test").getMessage();
    String valueString1 = String.valueOf(<dummyValue1>);
    String valueString2 = String.valueOf(<dummyValue2>);
    assertMessageContains(message, valueString1, "the expected value");
    assertMessageContains(message, valueString2, "the actual value");
    assertMessageContains(message, "<Type><Type>Bi limit", "the predicate description");

    // String message
    Assertions.assertDoesNotThrow(() -> {
      TestAssertions.assertArrayTest(value1, value2,
          new Dummy<Type><Type>BiPredicate(1), testMessage);
    \}, "Throws with passed test with message");
    message = Assertions.assertThrows(AssertionError.class, () -> {
      TestAssertions.assertArrayTest(value1, value2,
          new Dummy<Type><Type>BiPredicate(0), testMessage);
    \}, "Does not throw with failed test with message").getMessage();
    assertMessageContains(message, valueString1, "the expected value");
    assertMessageContains(message, valueString2, "the actual value");
    assertMessageContains(message, "<Type><Type>Bi limit", "the predicate description");
    assertMessageContains(message, testMessage, "the message");

    // Message supplier
    Assertions.assertDoesNotThrow(() -> {
      TestAssertions.assertArrayTest(value1, value2,
          new Dummy<Type><Type>BiPredicate(1), testMessageSupplier);
    \}, "Throws with passed test with message supplier");
    message = Assertions.assertThrows(AssertionError.class, () -> {
      TestAssertions.assertArrayTest(value1, value2,
          new Dummy<Type><Type>BiPredicate(0), testMessageSupplier);
    \}, "Does not throw with failed test with message supplier").getMessage();
    assertMessageContains(message, valueString1, "the expected value");
    assertMessageContains(message, valueString2, "the actual value");
    assertMessageContains(message, "<Type><Type>Bi limit", "the predicate description");
    assertMessageContains(message, testMessageSupplier.get(), "the supplied message");

    // Null array
    <type>[] nullValue = null;
    message = Assertions.assertThrows(AssertionError.class, () -> {
      TestAssertions.assertArrayTest(value1, nullValue,
          new Dummy<Type><Type>BiPredicate(1), testMessageSupplier);
    \}, "Does not throw with null actual array").getMessage();
    assertMessageContains(message, "actual array", "the failure was due to the actual being null");
    assertMessageContains(message, "null", "the failure was due to the actual being null");
    assertMessageContains(message, testMessageSupplier.get(), "the supplied message");

    message = Assertions.assertThrows(AssertionError.class, () -> {
      TestAssertions.assertArrayTest(nullValue, value2,
          new Dummy<Type><Type>BiPredicate(1), testMessageSupplier);
    \}, "Does not throw with null expected array").getMessage();
    assertMessageContains(message, "expected array",
        "the failure was due to the expected being null");
    assertMessageContains(message, "null", "the failure was due to the expected being null");
    assertMessageContains(message, testMessageSupplier.get(), "the supplied message");

    // Length mismatch
    <type>[] longerValue = new <type>[]{ <dummyValue1>, <dummyValue1> \};
    message = Assertions.assertThrows(AssertionError.class, () -> {
      TestAssertions.assertArrayTest(value1, longerValue,
          new Dummy<Type><Type>BiPredicate(1), testMessageSupplier);
    \}, "Does not throw with longer actual array").getMessage();
    assertMessageContains(message, "lengths differ", "the failure was to to different lengths");
    assertMessageContains(message, testMessageSupplier.get(), "the supplied message");

    message = Assertions.assertThrows(AssertionError.class, () -> {
      TestAssertions.assertArrayTest(longerValue, value1,
          new Dummy<Type><Type>BiPredicate(1), testMessageSupplier);
    \}, "Does not throw with longer expected array").getMessage();
    assertMessageContains(message, "lengths differ", "the failure was to to different lengths");
    assertMessageContains(message, testMessageSupplier.get(), "the supplied message");

    // Test with a custom predicate which does not implement Supplier\<String> (i.e. no description)
    <Type><Type>BiPredicate customPredicate = (v1, v2) -> false;
    message = Assertions.assertThrows(AssertionError.class, () -> {
      TestAssertions.assertArrayTest(value1, value2, customPredicate);
    \}, "Does not throw with custom predicate").getMessage();
    assertMessageContains(message, valueString1, "the expected value");
    assertMessageContains(message, valueString2, "the actual value");

    // Test the message contains the array index.
    // Arrays must be different array references.
    int size = 2;
    <type>[] multiValue1 = new <type>[size];
    <type>[] multiValue2 = new <type>[size];
    for (int i = 0; i \< size; i++) {
      // This test enforces an array processing order on the implementation
      int index = i;
      message = Assertions.assertThrows(AssertionError.class, () -> {
        TestAssertions.assertArrayTest(multiValue1, multiValue2,
            new Dummy<Type><Type>BiPredicate(index));
      \}, () -> "Failed to throw at specified index: " + index).getMessage();
      assertMessageContains(message, String.format("at index [%d]", i), "the array index");
    \}
  \}

  /**
   * Test {@link TestAssertions\} for a single array of primitive {@code <type>\} type.
   */
  @Test
  public void testAssertObjectArrayTest<Type>Predicate() {
    String message;
    <type>[] value = new <type>[]{ <dummyValue1> \};
    Object[] nestedValue = new <type>[][][]{{ value \}\};

    // Test with null predicate
    PrimitivePredicate nullPredicate = null;
    message = Assertions.assertThrows(AssertionError.class, () -> {
      TestAssertions.assertArrayTest(nestedValue, nullPredicate, testMessageSupplier);
    \}, "Does not throw with null predicate").getMessage();
    assertMessageContains(message, "null", "the failure was due to the predicate being null");

    // Test with incorrect predicate
    message = Assertions.assertThrows(AssertionError.class, () -> {
      TestAssertions.assertArrayTest(nestedValue, new BadPredicate(), testMessageSupplier);
    \}, "Does not throw with bad predicate").getMessage();
    assertMessageContains(message, "BadPredicate", "the predicate class name");

    // No message
    Assertions.assertDoesNotThrow(() -> {
      TestAssertions.assertArrayTest(nestedValue, new Dummy<Type>Predicate(1));
    \}, "Throws with passed test");
    message = Assertions.assertThrows(AssertionError.class, () -> {
      TestAssertions.assertArrayTest(nestedValue, new Dummy<Type>Predicate(0));
    \}, "Does not throw with failed test").getMessage();
    String valueString = String.valueOf(<dummyValue1>);
    assertMessageContains(message, valueString, "the value");
    assertMessageContains(message, "<Type> limit", "the predicate description");

    // String message
    Assertions.assertDoesNotThrow(() -> {
      TestAssertions.assertArrayTest(nestedValue, new Dummy<Type>Predicate(1), testMessage);
    \}, "Throws with passed test with message");
    message = Assertions.assertThrows(AssertionError.class, () -> {
      TestAssertions.assertArrayTest(nestedValue, new Dummy<Type>Predicate(0), testMessage);
    \}, "Does not throw with failed test with message").getMessage();
    assertMessageContains(message, valueString, "the value");
    assertMessageContains(message, "<Type> limit", "the predicate description");
    assertMessageContains(message, testMessage, "the message");

    // Message supplier
    Assertions.assertDoesNotThrow(() -> {
      TestAssertions.assertArrayTest(nestedValue, new Dummy<Type>Predicate(1),
          testMessageSupplier);
    \}, "Throws with passed test with message supplier");
    message = Assertions.assertThrows(AssertionError.class, () -> {
      TestAssertions.assertArrayTest(nestedValue, new Dummy<Type>Predicate(0),
          testMessageSupplier);
    \}, "Does not throw with failed test with message supplier").getMessage();
    assertMessageContains(message, valueString, "the value");
    assertMessageContains(message, "<Type> limit", "the predicate description");
    assertMessageContains(message, testMessageSupplier.get(), "the supplied message");

    // Null array
    Object[] nullValue = null;
    message = Assertions.assertThrows(AssertionError.class, () -> {
      TestAssertions.assertArrayTest(nullValue, new Dummy<Type>Predicate(1),
          testMessageSupplier);
    \}, "Does not throw with null array").getMessage();
    assertMessageContains(message, "value", "the failure was due to the value being null");
    assertMessageContains(message, "null", "the failure was due to the value being null");
    assertMessageContains(message, testMessageSupplier.get(), "the supplied message");

    // Test the message contains the array index
    int size = 2;
    <type>[][] multiValue = new <type>[size][size];
    for (int i = 0; i \< size; i++) {
      for (int j = 0; j \< size; j++) {
        // This test enforces an array processing order on the implementation
        int index = i * size + j;
        message = Assertions.assertThrows(AssertionError.class, () -> {
          TestAssertions.assertArrayTest(multiValue, new Dummy<Type>Predicate(index));
        \}, () -> "Failed to throw at specified index: " + index).getMessage();
        assertMessageContains(message, String.format("at index [%d][%d]", i, j), "the array index");
      \}
    \}
  \}

  /**
   * Test {@link TestAssertions\} for a two nested arrays of primitive {@code <type>\} type.
   */
  @Test
  public void testAssertObjectArrayTest<Type><Type>BiPredicate() {
    String message;
    // Arrays must be different
    <type>[] value1 = new <type>[]{ <dummyValue1> \};
    <type>[] value2 = new <type>[]{ <dummyValue2> \};
    Object[] nestedValue1 = new <type>[][][]{{ value1 \}\};
    Object[] nestedValue2 = new <type>[][][]{{ value2 \}\};

    // Test with null predicate
    PrimitiveBiPredicate nullPredicate = null;
    message = Assertions.assertThrows(AssertionError.class, () -> {
      TestAssertions.assertArrayTest(nestedValue1, nestedValue2, nullPredicate,
          testMessageSupplier);
    \}, "Does not throw with null predicate").getMessage();
    assertMessageContains(message, "null", "the failure was due to the predicate being null");

    // Test with incorrect predicate
    message = Assertions.assertThrows(AssertionError.class, () -> {
      TestAssertions.assertArrayTest(nestedValue1, nestedValue2, new BadPredicate(),
          testMessageSupplier);
    \}, "Does not throw with bad predicate").getMessage();
    assertMessageContains(message, "BadPredicate", "the predicate class name");

    // Test with same reference
    Assertions.assertDoesNotThrow(() -> {
      TestAssertions.assertArrayTest(nestedValue1, nestedValue1,
          new Dummy<Type><Type>BiPredicate(1));
    \}, "Throws with same reference");

    // Test with same reference to nested array
    Object[] nestedValue1b = new <type>[][][]{{ value1 \}\};
    Assertions.assertDoesNotThrow(() -> {
      TestAssertions.assertArrayTest(nestedValue1, nestedValue1b,
          new Dummy<Type><Type>BiPredicate(1));
    \}, "Throws with same nested array");

    // No message
    Assertions.assertDoesNotThrow(() -> {
      TestAssertions.assertArrayTest(nestedValue1, nestedValue2,
          new Dummy<Type><Type>BiPredicate(1));
    \}, "Throws with passed test");
    message = Assertions.assertThrows(AssertionError.class, () -> {
      TestAssertions.assertArrayTest(nestedValue1, nestedValue2,
          new Dummy<Type><Type>BiPredicate(0));
    \}, "Does not throw with failed test").getMessage();
    String valueString1 = String.valueOf(<dummyValue1>);
    String valueString2 = String.valueOf(<dummyValue2>);
    assertMessageContains(message, valueString1, "the expected value");
    assertMessageContains(message, valueString2, "the actual value");
    assertMessageContains(message, "<Type><Type>Bi limit", "the predicate description");

    // String message
    Assertions.assertDoesNotThrow(() -> {
      TestAssertions.assertArrayTest(nestedValue1, nestedValue2,
          new Dummy<Type><Type>BiPredicate(1), testMessage);
    \}, "Throws with passed test with message");
    message = Assertions.assertThrows(AssertionError.class, () -> {
      TestAssertions.assertArrayTest(nestedValue1, nestedValue2,
          new Dummy<Type><Type>BiPredicate(0), testMessage);
    \}, "Does not throw with failed test with message").getMessage();
    assertMessageContains(message, valueString1, "the expected value");
    assertMessageContains(message, valueString2, "the actual value");
    assertMessageContains(message, "<Type><Type>Bi limit", "the predicate description");
    assertMessageContains(message, testMessage, "the message");

    // Message supplier
    Assertions.assertDoesNotThrow(() -> {
      TestAssertions.assertArrayTest(nestedValue1, nestedValue2,
          new Dummy<Type><Type>BiPredicate(1), testMessageSupplier);
    \}, "Throws with passed test with message supplier");
    message = Assertions.assertThrows(AssertionError.class, () -> {
      TestAssertions.assertArrayTest(nestedValue1, nestedValue2,
          new Dummy<Type><Type>BiPredicate(0), testMessageSupplier);
    \}, "Does not throw with failed test with message supplier").getMessage();
    assertMessageContains(message, valueString1, "the expected value");
    assertMessageContains(message, valueString2, "the actual value");
    assertMessageContains(message, "<Type><Type>Bi limit", "the predicate description");
    assertMessageContains(message, testMessageSupplier.get(), "the supplied message");

    // Null array
    <type>[][][] nullValue = null;
    message = Assertions.assertThrows(AssertionError.class, () -> {
      TestAssertions.assertArrayTest(nestedValue1, nullValue,
          new Dummy<Type><Type>BiPredicate(1), testMessageSupplier);
    \}, "Does not throw with null actual array").getMessage();
    assertMessageContains(message, "actual array", "the failure was due to the actual being null");
    assertMessageContains(message, "null", "the failure was due to the actual being null");
    assertMessageContains(message, testMessageSupplier.get(), "the supplied message");

    message = Assertions.assertThrows(AssertionError.class, () -> {
      TestAssertions.assertArrayTest(nullValue, nestedValue2,
          new Dummy<Type><Type>BiPredicate(1), testMessageSupplier);
    \}, "Does not throw with null expected array").getMessage();
    assertMessageContains(message, "expected array",
        "the failure was due to the expected being null");
    assertMessageContains(message, "null", "the failure was due to the expected being null");
    assertMessageContains(message, testMessageSupplier.get(), "the supplied message");

    // Length mismatch
    <type>[][][] longerValue = new <type>[][][]{{{ <dummyValue1>, <dummyValue1> \}\}\};
    message = Assertions.assertThrows(AssertionError.class, () -> {
      TestAssertions.assertArrayTest(nestedValue1, longerValue,
          new Dummy<Type><Type>BiPredicate(1), testMessageSupplier);
    \}, "Does not throw with longer actual array").getMessage();
    assertMessageContains(message, "lengths differ", "the failure was to to different lengths");
    assertMessageContains(message, testMessageSupplier.get(), "the supplied message");

    message = Assertions.assertThrows(AssertionError.class, () -> {
      TestAssertions.assertArrayTest(longerValue, nestedValue1,
          new Dummy<Type><Type>BiPredicate(1), testMessageSupplier);
    \}, "Does not throw with longer expected array").getMessage();
    assertMessageContains(message, "lengths differ", "the failure was to to different lengths");
    assertMessageContains(message, testMessageSupplier.get(), "the supplied message");

    // Test the message contains the array index.
    // Arrays must be different array references.
    int size = 2;
    <type>[][] multiValue1 = new <type>[size][size];
    <type>[][] multiValue2 = new <type>[size][size];
    for (int i = 0; i \< size; i++) {
      for (int j = 0; j \< size; j++) {
        // This test enforces an array processing order on the implementation
        int index = i * size + j;
        message = Assertions.assertThrows(AssertionError.class, () -> {
          TestAssertions.assertArrayTest(multiValue1, multiValue2,
              new Dummy<Type><Type>BiPredicate(index));
        \}, () -> "Failed to throw at specified index: " + index).getMessage();
        assertMessageContains(message, String.format("at index [%d][%d]", i, j), "the array index");
      \}
    \}
  \}}>
}
