<package>

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

/**
 * Tests the logical combination of {@link <Type1>Predicate\} and {@link <Type2>Predicate\}.
 */
public class <Type1>Predicate<Type2>PredicateTest {

  /** The fixed test results: true or false. */
  private final boolean[] values = {true, false};

  /**
   * Simple class to result a fixed test result and description message for predicate 1.
   */
  private class Dummy<Type1>Predicate1 implements <Type1>Predicate, Supplier\<String> {

    private final boolean result;

    private Dummy<Type1>Predicate1(boolean result) {
      this.result = result;
    }

    @Override
    public boolean test(<type1> value) {
      return result;
    }

    @Override
    public String get() {
      return "result1:" + result;
    }
  }

  /**
   * Simple class to result a fixed test result and description message for predicate 2.
   */
  private class Dummy<Type2>Predicate2 implements <Type2>Predicate, Supplier\<String> {

    private final boolean result;

    private Dummy<Type2>Predicate2(boolean result) {
      this.result = result;
    }

    @Override
    public boolean test(<type2> value) {
      return result;
    }

    @Override
    public String get() {
      return "result2:" + result;
    }
  }<Ops,ops,operators:{Op,op,operator |
<\n>
  /**
   * Test {@link <Type1>Predicate<Op><Type2>Predicate\}.
   */
  @Test
  public void test<Type1>Predicate<Op><Type2>Predicate() {
    for (final boolean value1 : values) {
      Dummy<Type1>Predicate1 predicate1 = new Dummy<Type1>Predicate1(value1);
      for (final boolean value2 : values) {
        Dummy<Type2>Predicate2 predicate2 = new Dummy<Type2>Predicate2(value2);

        // Test <op>
        <Type1>Predicate<Op><Type2>Predicate predicate =
            new <Type1>Predicate<Op><Type2>Predicate(predicate1, predicate2);
        boolean expected = value1 <operator> value2;
        Assertions.assertEquals(expected, predicate.test(<dummyValue>, <dummyValue>),
            "Bad boolean operator");

        String description = predicate.get();
        Assertions.assertTrue(description.contains("<operator>"), "Description missing <operator>");
        Assertions.assertTrue(description.contains(predicate1.get()),
            () -> "Description missing " + predicate1.get());
        Assertions.assertTrue(description.contains(predicate2.get()),
            () -> "Description missing " + predicate2.get());
      \}
    \}
  \}}>
}
