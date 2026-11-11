<package>

import uk.ac.sussex.gdsc.test.api.ApiTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link <Number><Number>AreClose<Sign>}.
 */
public class <Number><Number>AreClose<Sign>Test {

  /**
   * Test {@link <Number><Number>AreClose<Sign>#test(<number>, <number>)}.
   */
  @Test
  public void testTest() {
    final <number>[] values = ApiTests.get<Sign><Number>Values();
    for (final double relativeError : new double[] {0, 0.5}) {
      for (final <errorType> absoluteError : new <errorType>[] {0, 1}) {
        final <Number><Number>AreClose<Sign> predicate =
            new <Number><Number>AreClose<Sign>(relativeError, absoluteError);
        for (final <number> value1 : values) {
          for (final <number> value2 : values) {
            final boolean expected = <Number>Equality.testAreClose<Sign>(value1, value2,
                relativeError, absoluteError);
            Assertions.assertEquals(expected, predicate.test(value1, value2),
                () -> String.format("Does not compute <Number>Equality.testAreClose<Sign> :"
                    + " v1=%s, v2=%s, rel.error=%s, abs.error=%s",
                    value1, value2, relativeError, absoluteError));
          }
        }
      }
    }
  }

  /**
   * Test {@link <Number><Number>AreClose<Sign>#get() }.
   */
  @Test
  public void testGet() {
    final double relativeError = 1e-3;
    final <errorType> absoluteError = <absError>;
    final <Number><Number>AreClose<Sign> predicate =
        new <Number><Number>AreClose<Sign>(relativeError, absoluteError);
    final String s = predicate.get();
    for (final String item : new String[] {"||", String.valueOf(relativeError),
        String.valueOf(absoluteError), <if(unsigned)>"unsigned |v1-v2|/max(v1,v2) "<else>"|v1-v2|/max(|v1|,|v2|) "<endif>, "|v1-v2| "}) {
      Assertions.assertTrue(s.contains(item), () -> "Missing \<" + item + ">: " + s);
    }
  }
}
