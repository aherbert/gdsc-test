<package>

import uk.ac.sussex.gdsc.test.api.ApiTestUtils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link <Float><Float>IsCloseTo}.
 */
public class <Float><Float>IsCloseToTest {

  /**
   * Test {@link <Float><Float>IsCloseTo#test(<float>, <float>)}.
   */
  @Test
  public void testTest() {
    final <float>[] values = ApiTestUtils.get<Float>Values();
    for (double relativeError : new double[] {0, 0.5}) {
      for (<float> absoluteError : new <float>[] {0, 1}) {
        final <Float><Float>IsCloseTo predicate =
            new <Float><Float>IsCloseTo(relativeError, absoluteError);
        for (<float> value1 : values) {
          for (<float> value2 : values) {
            final boolean expected = EqualityUtils.<float>sTestIsCloseTo(value1, value2,
                relativeError, absoluteError);
            Assertions.assertEquals(expected, predicate.test(value1, value2),
                () -> String.format("Does not compute EqualityUtils.<float>sTestIsCloseTo :"
                    + " v1=%s, v2=%s, rel.error=%s, abs.error=%s",
                    value1, value2, relativeError, absoluteError));
          }
        }
      }
    }
  }

  /**
   * Test {@link <Float><Float>IsCloseTo#get() }.
   */
  @Test
  public void testGet() {
    final double relativeError = <relError>;
    final <float> absoluteError = <absError>;
    final <Float><Float>IsCloseTo predicate =
        new <Float><Float>IsCloseTo(relativeError, absoluteError);
    for (String item : new String[] {"||", String.valueOf(relativeError),
        String.valueOf(absoluteError), "|v1-v2|/|v1|", "|v1-v2| "}) {
      Assertions.assertTrue(predicate.get().contains(item),
          () -> "Missing \<" + item + ">: " + predicate.get());
    }
  }
}
