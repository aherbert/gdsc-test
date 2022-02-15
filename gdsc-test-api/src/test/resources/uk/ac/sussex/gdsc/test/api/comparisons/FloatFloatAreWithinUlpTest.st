<package>

import uk.ac.sussex.gdsc.test.api.ApiTestUtils;

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
    final <float>[] values = ApiTestUtils.get<Float>Values();
    for (<errorType> ulpError : new <errorType>[] {<errorValue>}) {
      final <Float><Float>AreWithinUlp predicate = new <Float><Float>AreWithinUlp(ulpError);
      for (<float> value1 : values) {
        for (<float> value2 : values) {
          final boolean expected = <Float>EqualityUtils.testAreWithinUlp(value1, value2, ulpError);
          Assertions.assertEquals(expected, predicate.test(value1, value2),
              () -> String.format("Does not compute <Float>EqualityUtils.testAreWithinUlp :"
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
    final <errorType> ulpError = <ulpError>;
    final <Float><Float>AreWithinUlp predicate = new <Float><Float>AreWithinUlp(ulpError);
    for (String item : new String[] {"ulp", String.valueOf(ulpError)}) {
      Assertions.assertTrue(predicate.get().contains(item),
          () -> "Missing \<" + item + ">: " + predicate.get());
    }
  }
}
