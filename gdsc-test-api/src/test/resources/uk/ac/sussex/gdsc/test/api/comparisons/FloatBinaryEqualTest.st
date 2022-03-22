<package>

import uk.ac.sussex.gdsc.test.api.ApiTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link <Float>BinaryEqual}.
 */
public class <Float>BinaryEqualTest {

  /**
   * Test {@link <Float>BinaryEqual#test(<float>)}.
   */
  @Test
  public void testTest() {
    final <float>[] values = ApiTests.get<Float>Values();
    for (<float> value1 : values) {
      final <Float>BinaryEqual predicate = new <Float>BinaryEqual(value1);
      Assertions.assertEquals(value1, predicate.getReferenceValue(),
          "Predicate has incorrect reference value");
      for (<float> value2 : values) {
        Assertions.assertEquals(<Float>.compare(value1, value2) == 0,
            predicate.test(value2),
            "Predicate does not compute <Float>.compare");
      }
    }
  }

  /**
   * Test {@link <Float>BinaryEqual#get() }.
   */
  @Test
  public void testGet() {
    final <float>[] values = ApiTests.get<Float>Values();
    for (<float> value : values) {
      final <Float>BinaryEqual predicate = new <Float>BinaryEqual(value);
      Assertions.assertTrue(predicate.get().contains("cmp"), "Description missing cmp");
      final String stringValue = String.valueOf(value);
      Assertions.assertTrue(predicate.get().contains(stringValue),
          () -> "Description missing " + stringValue);
    }
  }
}
