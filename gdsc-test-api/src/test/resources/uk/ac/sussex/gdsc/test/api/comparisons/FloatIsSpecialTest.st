<package>

import uk.ac.sussex.gdsc.test.api.ApiTestUtils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link <Float>Is<Special>}.
 */
public class <Float>Is<Special>Test {

  /**
   * Test {@link <Float>Is<Special>#test(<float>)}.
   */
  @Test
  public void testTest() {
    final <Float>Is<Special> predicate = <Float>Is<Special>.INSTANCE;
    final <float>[] values = ApiTestUtils.get<Float>Values();
    for (<float> value : values) {
      Assertions.assertEquals(<Float>.is<Special>(value), predicate.test(value),
          "Predicate does not compute <Float>.is<Special>");
    }
  }

  /**
   * Test {@link <Float>Is<Special>#get() }.
   */
  @Test
  public void testGet() {
    final <Float>Is<Special> predicate = new <Float>Is<Special>();
    Assertions.assertTrue(predicate.get().contains("<Special>"), "Description missing <Special>");
  }
}
