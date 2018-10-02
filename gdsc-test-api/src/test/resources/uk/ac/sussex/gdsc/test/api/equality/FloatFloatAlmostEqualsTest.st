<package>

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
    for (double relativeError : new double[] { -1, 0, 0.5}) {
      for (<float> absoluteError : new <float>[] { -1, 0, 1}) {
        if (relativeError \< 0 && absoluteError \< 0) {
          // Not a valid combination
          continue;
        }
        <Float><Float>AlmostEquals predicate =
            new <Float><Float>AlmostEquals(relativeError, absoluteError);
        <float>[] values = { <values> };
        for (<float> value1 : values) {
          for (<float> value2 : values) {
            boolean expected =
                EqualityUtils.<float>sTestAlmostEqual(value1, value2, relativeError, absoluteError);
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
    double relativeError = <relError>;
    <float> absoluteError = <absError>;
    <Float><Float>AlmostEquals predicate = new <Float><Float>AlmostEquals(relativeError, absoluteError);
    for (String item : new String[] {"||", String.valueOf(relativeError),
        String.valueOf(absoluteError)}) {
      Assertions.assertTrue(predicate.get().contains(item),
          () -> "Missing \<" + item + ">: " + predicate.get());
    }
  }
}
