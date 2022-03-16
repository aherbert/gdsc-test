<package>

import uk.ac.sussex.gdsc.test.api.ApiTests;

<if(import1)>import <import1>;<endif>
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link <Number><Number>Are<Within>}.
 */
public class <Number><Number>Are<Within>Test {

  /**
   * Test {@link <Number><Number>Are<Within>#test(<number>, <number>)}.
   */
  @Test
  public void testTest() {
    final <number>[] values = ApiTests.get<Number>Values();
    for (<errorType> absoluteError : new <errorType>[] {<errorValue>}) {
      final <Number><Number>Are<Within> predicate = new <Number><Number>Are<Within>(absoluteError);
      for (<number> value1 : values) {
        for (<number> value2 : values) {
          final boolean expected = <Number>Equality.testAreWithin(value1, value2, absoluteError);
          Assertions.assertEquals(expected, predicate.test(value1, value2),
              () -> String.format("Does not compute <Number>Equality.testAreWithin :"
                  + " v1=%s, v2=%s, abs.error=%s", value1, value2, absoluteError));
        }
      }
    }
  }

  /**
   * Test {@link <Number><Number>Are<Within>#get() }.
   */
  @Test
  public void testGet() {
    final <errorType> absoluteError = <absError>;
    final <Number><Number>Are<Within> predicate = new <Number><Number>Are<Within>(absoluteError);
    for (String item : new String[] {"|v1-v2|", String.valueOf(absoluteError)}) {
      Assertions.assertTrue(predicate.get().contains(item),
          () -> "Missing \<" + item + ">: " + predicate.get());
    }
  }
}
