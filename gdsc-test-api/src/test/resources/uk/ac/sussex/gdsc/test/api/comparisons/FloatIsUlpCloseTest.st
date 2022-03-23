<package>

import uk.ac.sussex.gdsc.test.api.ApiTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link <Float>IsUlpClose}.
 */
public class <Float>IsUlpCloseTest {

  /**
   * Test {@link <Float>IsUlpClose#test(<float>)}.
   */
  @Test
  public void testTest() {
    final <float>[] values = ApiTests.get<Float>Values();
    for (final <float> value1 : values) {
      for (final <ulpType> ulpError : new <ulpType>[] {<ulpValue>}) {
        final <Float>IsUlpClose predicate = new <Float>IsUlpClose(value1, ulpError);
        Assertions.assertEquals(value1, predicate.getReference(),
            "Predicate has incorrect reference value");
        for (<float> value2 : values) {
          final boolean expected = <Float>Equality.testAreWithinUlp(value1, value2, ulpError);
          Assertions.assertEquals(expected, predicate.test(value2),
              () -> String.format("Does not compute <Float>Equality.testAreUlpClose :"
                  + " v1=%s, v2=%s, ulp.error=%s", value1, value2, ulpError));
        }
      }
    }
  }

  /**
   * Test {@link <Float>IsUlpClose#get()}.
   */
  @Test
  public void testGet() {
    final <float>[] values = ApiTests.get<Float>Values();
    for (final <float> value1 : values) {
      final <ulpType> ulpError = <ulpError>;
      final <Float>IsUlpClose predicate = new <Float>IsUlpClose(value1, ulpError);
      final String s = predicate.get();
      for (String item : new String[] {String.valueOf(value1), "ulp", String.valueOf(ulpError)}) {
        Assertions.assertTrue(s.contains(item), () -> "Missing \<" + item + ">: " + s);
      }
    }
  }
}
