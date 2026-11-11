<package>

import uk.ac.sussex.gdsc.test.api.ApiTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link <Number>IsCloseTo<Sign>}.
 */
public class <Number>IsCloseTo<Sign>Test {

  /**
   * Test {@link <Number>IsCloseTo<Sign>#test(<number>)}.
   */
  @Test
  public void testTest() {
    final <number>[] values = ApiTests.get<Sign><Number>Values();
    for (final <number> value1 : values) {
      for (final double relativeError : new double[] {0, 0.5}) {
        for (final <errorType> absoluteError : new <errorType>[] {0, 1}) {
          final <Number>IsCloseTo<Sign> predicate =
              new <Number>IsCloseTo<Sign>(value1, relativeError, absoluteError);
          Assertions.assertEquals(value1, predicate.getReference(),
              "Predicate has incorrect reference value");
          for (final <number> value2 : values) {
            final boolean expected = <Number>Equality.testIsCloseTo<Sign>(value1, value2,
                relativeError, absoluteError);
            Assertions.assertEquals(expected, predicate.test(value2),
                () -> String.format("Does not compute <Number>Equality.testIsCloseTo<Sign> :"
                    + " v1=%s, v2=%s, rel.error=%s, abs.error=%s",
                    value1, value2, relativeError, absoluteError));
          }
        }
      }
    }
  }

  /**
   * Test {@link <Number>IsCloseTo<Sign>#get() }.
   */
  @Test
  public void testGet() {
    final double relativeError = 1e-3;
    final <errorType> absoluteError = <absError>;
    final <number>[] values = ApiTests.get<Sign><Number>Values();
    for (final <number> value1 : values) {
      final <Number>IsCloseTo<Sign> predicate =
          new <Number>IsCloseTo<Sign>(value1, relativeError, absoluteError);
      final String s = predicate.get();
      for (final String item : new String[] {<valueString>, "||",
          String.valueOf(relativeError),
          String.valueOf(absoluteError), <if(unsigned)>"unsigned |v1-v2|/v1 "<else>"|v1-v2|/|v1| "<endif>, "|v1-v2| "}) {
        Assertions.assertTrue(s.contains(item), () -> "Missing \<" + item + ">: " + s);
      }
    }
  }
}
