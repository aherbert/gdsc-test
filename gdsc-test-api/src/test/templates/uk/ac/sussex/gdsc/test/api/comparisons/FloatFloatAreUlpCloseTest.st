<package>

import uk.ac.sussex.gdsc.test.api.ApiTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link <Float><Float>AreUlpClose}.
 */
public class <Float><Float>AreUlpCloseTest {

  /**
   * Test {@link <Float><Float>AreUlpClose#test(<float>, <float>)}.
   */
  @Test
  public void testTest() {
    final <float>[] values = ApiTests.get<Float>Values();
    for (final <ulpType> ulpError : new <ulpType>[] {<ulpValue>}) {
      final <Float><Float>AreUlpClose predicate = new <Float><Float>AreUlpClose(ulpError);
      for (final <float> value1 : values) {
        for (final <float> value2 : values) {
          final boolean expected = <Float>Equality.testAreWithinUlp(value1, value2, ulpError);
          Assertions.assertEquals(expected, predicate.test(value1, value2),
              () -> String.format("Does not compute <Float>Equality.testAreUlpClose :"
                  + " v1=%s, v2=%s, ulp.error=%s", value1, value2, ulpError));
        }
      }
    }
  }

  /**
   * Test {@link <Float><Float>AreUlpClose#get() }.
   */
  @Test
  public void testGet() {
    final <ulpType> ulpError = <ulpError>;
    final <Float><Float>AreUlpClose predicate = new <Float><Float>AreUlpClose(ulpError);
    final String s = predicate.get();
    for (final String item : new String[] {"ulp", String.valueOf(ulpError)}) {
      Assertions.assertTrue(s.contains(item), () -> "Missing \<" + item + ">: " + s);
    }
  }
}
