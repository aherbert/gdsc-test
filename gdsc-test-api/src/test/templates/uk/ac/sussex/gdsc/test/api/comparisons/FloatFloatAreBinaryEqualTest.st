<package>

import uk.ac.sussex.gdsc.test.api.ApiTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link <Float><Float>AreBinaryEqual}.
 */
public class <Float><Float>AreBinaryEqualTest {

  /**
   * Test {@link <Float><Float>AreBinaryEqual#test(<float>, <float>)}.
   */
  @Test
  public void testTest() {
    final <Float><Float>AreBinaryEqual predicate = <Float><Float>AreBinaryEqual.INSTANCE;
    final <float>[] values = ApiTests.get<Float>Values();
    for (final <float> value1 : values) {
      for (final <float> value2 : values) {
        Assertions.assertEquals(<Float>.compare(value1, value2) == 0,
            predicate.test(value1, value2),
            "Predicate does not compute <Float>.compare");
      }
    }
  }

  /**
   * Test {@link <Float><Float>AreBinaryEqual#get() }.
   */
  @Test
  public void testGet() {
    final <Float><Float>AreBinaryEqual predicate = new <Float><Float>AreBinaryEqual();
    Assertions.assertTrue(predicate.get().contains("is"), "Description missing 'is'");
  }
}
