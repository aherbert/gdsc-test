<package>

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

/**
 * Tests {@link <Type1><Type2>BiPredicate}.
 */
public class <Type1><Type2>BiPredicateTest {

  /** The fixed test results: true or false. */
  private final boolean[] values = {true, false};

  /**
   * Simple class to result a fixed test result and description message.
   */
  private class Dummy<Type1><Type2>BiPredicate
      implements <Type1><Type2>BiPredicate, Supplier\<String> {

    private final boolean result;

    private Dummy<Type1><Type2>BiPredicate(boolean result) {
      this.result = result;
    }

    @Override
    public boolean test(<type1> value1, <type2> value2) {
      return result;
    }

    @Override
    public String get() {
      return "result:" + result;
    }
  }<Methods,methods,operators:{Method,method,operator |
<\n>
  /**
   * Test {@link <Type1><Type2>BiPredicate#<method>(<Type1><Type2>BiPredicate)\} and
   * {@link <Type1><Type2>BiPredicate#negate()\}.
   */
  @Test
  public void test<Method>() {
    for (final boolean value1 : values) {
      Dummy<Type1><Type2>BiPredicate predicate1 = new Dummy<Type1><Type2>BiPredicate(value1);
      for (final boolean value2 : values) {
        Dummy<Type1><Type2>BiPredicate predicate2 = new Dummy<Type1><Type2>BiPredicate(value2);

        // Test <method>
        <Type1><Type2><Method>BiPredicate predicate =
            (<Type1><Type2><Method>BiPredicate) predicate1.<method>(predicate2);
        boolean expected = value1 <operator> value2;
        Assertions.assertEquals(expected, predicate.test(<dummyValue>, <dummyValue>),
            "Bad boolean operator");
        String description = predicate.get();
        Assertions.assertTrue(description.contains("<operator>"), "Description missing <operator>");
        Assertions.assertTrue(description.contains(predicate1.get()),
            () -> "Description missing " + predicate1.get());
        Assertions.assertTrue(description.contains(predicate2.get()),
            () -> "Description missing " + predicate2.get());

        // Test negate
        <Type1><Type2>NegateBiPredicate negatePredicate =
            (<Type1><Type2>NegateBiPredicate) predicate.negate();
        Assertions.assertEquals(!expected, negatePredicate.test(<dummyValue>, <dummyValue>));
        String negateDescription = negatePredicate.get();
        Assertions.assertTrue(negateDescription.contains(description),
            () -> "Description missing " + description);
        Assertions.assertTrue(negateDescription.contains("!"), "Description missing !");
        Assertions.assertSame(predicate, negatePredicate.negate(),
            "Double negate is not the same predicate");
      \}
    \}
  \}}>
}
