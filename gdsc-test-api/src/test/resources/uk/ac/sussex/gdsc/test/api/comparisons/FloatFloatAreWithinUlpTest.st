<package>

import uk.ac.sussex.gdsc.test.api.ApiTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link <Float><Float>AreWithinUlp}.
 */
public class <Float><Float>AreWithinUlpTest {

  /**
   * Test {@link <Float><Float>AreWithinUlp#test(<float>, <float>)}.
   */
  @Test
  public void testTest() {
    final <float>[] values = ApiTests.get<Float>Values();
    for (final <ulpType> ulpError : new <ulpType>[] {<ulpValue>}) {
      final <Float><Float>AreWithinUlp predicate = new <Float><Float>AreWithinUlp(ulpError);
      for (final <float> value1 : values) {
        for (final <float> value2 : values) {
          final boolean expected = <Float>Equality.testAreWithinUlp(value1, value2, ulpError);
          Assertions.assertEquals(expected, predicate.test(value1, value2),
              () -> String.format("Does not compute <Float>Equality.testAreWithinUlp :"
                  + " v1=%s, v2=%s, ulp.error=%s", value1, value2, ulpError));
        }
      }
    }
  }

  /**
   * Test {@link <Float><Float>AreWithinUlp#get() }.
   */
  @Test
  public void testGet() {
    final <ulpType> ulpError = <ulpError>;
    final <Float><Float>AreWithinUlp predicate = new <Float><Float>AreWithinUlp(ulpError);
    final String s = predicate.get();
    for (final String item : new String[] {"ulp", String.valueOf(ulpError)}) {
      Assertions.assertTrue(s.contains(item), () -> "Missing \<" + item + ">: " + s);
    }
  }
}
