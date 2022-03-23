<package>

import uk.ac.sussex.gdsc.test.api.ApiTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link <Float><Float>BinaryEqual}.
 */
public class <Float><Float>BinaryEqualTest {

  /**
   * Test {@link <Float><Float>BinaryEqual#test(<float>, <float>)}.
   */
  @Test
  public void testTest() {
    final <Float><Float>BinaryEqual predicate = <Float><Float>BinaryEqual.INSTANCE;
    final <float>[] values = ApiTests.get<Float>Values();
    for (<float> value1 : values) {
      for (<float> value2 : values) {
        Assertions.assertEquals(<Float>.compare(value1, value2) == 0,
            predicate.test(value1, value2),
            "Predicate does not compute <Float>.compare");
      }
    }
  }

  /**
   * Test {@link <Float><Float>BinaryEqual#get() }.
   */
  @Test
  public void testGet() {
    final <Float><Float>BinaryEqual predicate = new <Float><Float>BinaryEqual();
    Assertions.assertTrue(predicate.get().contains("is"), "Description missing 'is'");
  }
}
