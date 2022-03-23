<package>

import uk.ac.sussex.gdsc.test.api.ApiTests;

<if(import1)>import <import1>;<endif>
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link <Number>Is<Absolutely>Close}.
 */
public class <Number>Is<Absolutely>CloseTest {

  /**
   * Test {@link <Number>Is<Absolutely>Close#test(<number>)}.
   */
  @Test
  public void testTest() {
    final <number>[] values = ApiTests.get<Number>Values();
    for (final <number> value1 : values) {
      for (final <errorType> absoluteError : new <errorType>[] {<errorValue>}) {
        final <Number>Is<Absolutely>Close predicate =
            new <Number>Is<Absolutely>Close(value1, absoluteError);
        Assertions.assertEquals(value1, predicate.getReference(),
            "Predicate has incorrect reference value");
        for (final <number> value2 : values) {
          final boolean expected = <Number>Equality.testAreWithin(value1, value2, absoluteError);
          Assertions.assertEquals(expected, predicate.test(value2),
              () -> String.format("Does not compute <Number>Equality.testAreWithin :"
                  + " v1=%s, v2=%s, abs.error=%s", value1, value2, absoluteError));
        }
      }
    }
  }

  /**
   * Test {@link <Number>Is<Absolutely>Close#get() }.
   */
  @Test
  public void testGet() {
    final <number>[] values = ApiTests.get<Number>Values();
    final <errorType> absoluteError = <absError>;
    for (final <number> value1 : values) {
      final <Number>Is<Absolutely>Close predicate =
          new <Number>Is<Absolutely>Close(value1, absoluteError);
      final String s = predicate.get();
      for (final String item : new String[] {String.valueOf(value1), "|v1-v2|",
          String.valueOf(absoluteError)}) {
        Assertions.assertTrue(s.contains(item), () -> "Missing \<" + item + ">: " + s);
      }
    }
  }
}
