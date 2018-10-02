<package>

import java.util.function.Supplier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link <Type>Predicate}.
 */
public class <Type>PredicateTest {

  /** The fixed test results: true or false. */
  private final boolean[] values = {true, false};

  /**
   * Simple class to result a fixed test result and description message.
   */
  private class Dummy<Type>Predicate implements <Type>Predicate, Supplier\<String> {

    private final boolean result;

    private Dummy<Type>Predicate(boolean result) {
      this.result = result;
    }

    @Override
    public boolean test(<type> value) {
      return result;
    }

    @Override
    public String get() {
      return "result:" + result;
    }
  }
  <Methods,methods,operators:{Method,method,operator |
<\n>
/**
 * Test {@link <Type>Predicate#<method>(<Type>Predicate)\} and {@link <Type>Predicate#negate()\}.
 */
@Test
public void test<Method>() {
  for (final boolean value1 : values) {
    Dummy<Type>Predicate predicate1 = new Dummy<Type>Predicate(value1);
    for (final boolean value2 : values) {
      Dummy<Type>Predicate predicate2 = new Dummy<Type>Predicate(value2);

      // Test <method>
      <Type><Method>Predicate predicate = (<Type><Method>Predicate) predicate1.<method>(predicate2);
      boolean expected = value1 <operator> value2;
      Assertions.assertEquals(expected, predicate.test(<dummyValue>), "Bad boolean operator");
      String description = predicate.get();
      Assertions.assertTrue(description.contains("<operator>"), "Description missing <operator>");
      Assertions.assertTrue(description.contains(predicate1.get()),
          () -> "Description missing " + predicate1.get());
      Assertions.assertTrue(description.contains(predicate2.get()),
          () -> "Description missing " + predicate1.get());

      // Test negate
      <Type>NegatePredicate negatePredicate = (<Type>NegatePredicate) predicate.negate();
      Assertions.assertEquals(!expected, negatePredicate.test(<dummyValue>));
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
