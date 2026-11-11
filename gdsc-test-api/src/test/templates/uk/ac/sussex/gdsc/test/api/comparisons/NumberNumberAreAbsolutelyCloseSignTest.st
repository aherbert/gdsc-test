<package>

import uk.ac.sussex.gdsc.test.api.ApiTests;

<if(import1)>import <import1>;<endif>
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link <Number><Number>Are<Absolutely>Close<Sign>}.
 */
public class <Number><Number>Are<Absolutely>Close<Sign>Test {

  /**
   * Test {@link <Number><Number>Are<Absolutely>Close<Sign>#test(<number>, <number>)}.
   */
  @Test
  public void testTest() {
    final <number>[] values = ApiTests.get<Sign><Number>Values();
    for (final <errorType> absoluteError : new <errorType>[] {<errorValue>}) {
      final <Number><Number>Are<Absolutely>Close<Sign> predicate =
          new <Number><Number>Are<Absolutely>Close<Sign>(absoluteError);
      for (final <number> value1 : values) {
        for (final <number> value2 : values) {
          final boolean expected =<if(unsigned)><\n>              <else> <endif><Number>Equality.testAreWithin<Sign>(value1, value2, absoluteError);
          Assertions.assertEquals(expected, predicate.test(value1, value2),
              () -> String.format("Does not compute <Number>Equality.testAreWithin<Sign> :"
                  + " v1=%s, v2=%s, abs.error=%s", value1, value2, absoluteError));
        }
      }
    }
  }

  /**
   * Test {@link <Number><Number>Are<Absolutely>Close<Sign>#get() }.
   */
  @Test
  public void testGet() {
    final <errorType> absoluteError = <absError>;
    final <Number><Number>Are<Absolutely>Close<Sign> predicate =
        new <Number><Number>Are<Absolutely>Close<Sign>(absoluteError);
    final String s = predicate.get();
    for (final String item : new String[] {<if(unsigned)>"unsigned |v1-v2| "<else>"|v1-v2| "<endif>, String.valueOf(absoluteError)}) {
      Assertions.assertTrue(s.contains(item), () -> "Missing \<" + item + ">: " + s);
    }
  }
}
