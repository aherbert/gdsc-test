<package>

import uk.ac.sussex.gdsc.test.api.ApiTestUtils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link <Number><Number>Close}.
 */
public class <Number><Number>CloseTest {

  /**
   * Test {@link <Number><Number>Close#test(<number>, <number>)}.
   */
  @Test
  public void testTest() {
    final <number>[] values = ApiTestUtils.get<Number>Values();
    for (double relativeError : new double[] {0, 0.5}) {
      for (<errorType> absoluteError : new <errorType>[] {0, 1}) {
        final <Number><Number>Close predicate =
            new <Number><Number>Close(relativeError, absoluteError);
        for (<number> value1 : values) {
          for (<number> value2 : values) {
            final boolean expected = EqualityUtils.<number>sTestClose(value1, value2,
                relativeError, absoluteError);
            Assertions.assertEquals(expected, predicate.test(value1, value2),
                () -> String.format("Does not compute EqualityUtils.<number>sTestClose :"
                    + " v1=%s, v2=%s, rel.error=%s, abs.error=%s",
                    value1, value2, relativeError, absoluteError));
          }
        }
      }
    }
  }

  /**
   * Test {@link <Number><Number>Close#get() }.
   */
  @Test
  public void testGet() {
    final double relativeError = 1e-3;
    final <errorType> absoluteError = <absError>;
    final <Number><Number>Close predicate =
        new <Number><Number>Close(relativeError, absoluteError);
    for (String item : new String[] {"||", String.valueOf(relativeError),
        String.valueOf(absoluteError), "|v1-v2|/max", "|v1-v2| "}) {
      Assertions.assertTrue(predicate.get().contains(item),
          () -> "Missing \<" + item + ">: " + predicate.get());
    }
  }
}
