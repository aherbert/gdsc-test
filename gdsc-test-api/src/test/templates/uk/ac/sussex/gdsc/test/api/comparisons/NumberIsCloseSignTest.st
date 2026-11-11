<package>

import uk.ac.sussex.gdsc.test.api.ApiTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link <Number>IsClose<Sign>}.
 */
public class <Number>IsClose<Sign>Test {

  /**
   * Test {@link <Number>IsClose<Sign>#test(<number>)}.
   */
  @Test
  public void testTest() {
    final <number>[] values = ApiTests.get<Sign><Number>Values();
    for (final <number> value1 : values) {
      for (final double relativeError : new double[] {0, 0.5}) {
        for (final <errorType> absoluteError : new <errorType>[] {0, 1}) {
          final <Number>IsClose<Sign> predicate =
              new <Number>IsClose<Sign>(value1, relativeError, absoluteError);
          Assertions.assertEquals(value1, predicate.getReference(),
              "Predicate has incorrect reference value");
          for (final <number> value2 : values) {
            final boolean expected = <Number>Equality.testAreClose<Sign>(value1, value2,
                relativeError, absoluteError);
            Assertions.assertEquals(expected, predicate.test(value2),
                () -> String.format("Does not compute <Number>Equality.testAreClose<Sign> :"
                    + " v1=%s, v2=%s, rel.error=%s, abs.error=%s",
                    value1, value2, relativeError, absoluteError));
          }
        }
      }
    }
  }

  /**
   * Test {@link <Number>IsClose<Sign>#get() }.
   */
  @Test
  public void testGet() {
    final double relativeError = 1e-3;
    final <errorType> absoluteError = <absError>;
    final <number>[] values = ApiTests.get<Sign><Number>Values();
    for (final <number> value1 : values) {
      final <Number>IsClose<Sign> predicate =
          new <Number>IsClose<Sign>(value1, relativeError, absoluteError);
      final String s = predicate.get();
      for (final String item : new String[] {<valueString>, "||",
          String.valueOf(relativeError),
          String.valueOf(absoluteError), <if(unsigned)>"unsigned |v1-v2|/max(v1,v2) "<else>"|v1-v2|/max(|v1|,|v2|) "<endif>, "|v1-v2| "}) {
        Assertions.assertTrue(s.contains(item), () -> "Missing \<" + item + ">: " + s);
      }
    }
  }
}
