<package>

import uk.ac.sussex.gdsc.test.api.ApiTestUtils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link <Float><Float>AlmostEquals}.
 */
public class <Float><Float>AlmostEqualsTest {

  /**
   * Test {@link <Float><Float>AlmostEquals#test(<float>, <float>)}.
   */
  @Test
  public void testTest() {
    final <float>[] values = ApiTestUtils.get<Float>Values();
    for (double relativeError : new double[] { -1, 0, 0.5}) {
      for (<float> absoluteError : new <float>[] { -1, 0, 1}) {
        if (relativeError \< 0 && absoluteError \< 0) {
          // Not a valid combination
          continue;
        }
        final <Float><Float>AlmostEquals predicate =
            new <Float><Float>AlmostEquals(relativeError, absoluteError);
        for (<float> value1 : values) {
          for (<float> value2 : values) {
            boolean expected = EqualityUtils.<float>sTestAlmostEqual(value1, value2,
                relativeError, absoluteError);
            Assertions.assertEquals(expected, predicate.test(value1, value2),
                () -> String.format("Does not compute EqualityUtils.testAlmostEqual :"
                    + " v1=%s, v2=%s, rel.error=%s, abs.error=%s",
                    value1, value2, relativeError, absoluteError));
          }
        }
      }
    }
  }

  /**
   * Test {@link <Float><Float>AlmostEquals#get() }.
   */
  @Test
  public void testGet() {
    final double relativeError = <relError>;
    final <float> absoluteError = <absError>;
    final <Float><Float>AlmostEquals predicate =
        new <Float><Float>AlmostEquals(relativeError, absoluteError);
    for (String item : new String[] {"||", String.valueOf(relativeError),
        String.valueOf(absoluteError)}) {
      Assertions.assertTrue(predicate.get().contains(item),
          () -> "Missing \<" + item + ">: " + predicate.get());
    }
  }
}
