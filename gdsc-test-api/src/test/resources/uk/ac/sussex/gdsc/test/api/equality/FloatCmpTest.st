<package>

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link <Float>Cmp}.
 */
public class <Float>CmpTest {

  /**
   * Test {@link <Float>Cmp#test(<float>, <float>)}.
   */
  @Test
  public void testTest() {
    final <float>[] values = { <values> };
    for (<float> value1 : values) {
      final <Float>Cmp predicate = new <Float>Cmp(value1);
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
   * Test {@link <Float>Cmp#get() }.
   */
  @Test
  public void testGet() {
    final <float>[] values = { <values> };
    for (<float> value : values) {
      final <Float>Cmp predicate = new <Float>Cmp(value);
      Assertions.assertTrue(predicate.get().contains("cmp"), "Description missing cmp");
      final String stringValue = String.valueOf(value);
      Assertions.assertTrue(predicate.get().contains(stringValue),
          () -> "Description missing " + stringValue);
    }
  }
}
