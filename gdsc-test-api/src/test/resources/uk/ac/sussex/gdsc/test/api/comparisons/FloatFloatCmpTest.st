<package>

import uk.ac.sussex.gdsc.test.api.ApiTestUtils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link <Float><Float>Cmp}.
 */
public class <Float><Float>CmpTest {

  /**
   * Test {@link <Float><Float>Cmp#test(<float>, <float>)}.
   */
  @Test
  public void testTest() {
    final <Float><Float>Cmp predicate = <Float><Float>Cmp.getInstance();
    final <float>[] values = ApiTestUtils.get<Float>Values();
    for (<float> value1 : values) {
      for (<float> value2 : values) {
        Assertions.assertEquals(<Float>.compare(value1, value2) == 0,
            predicate.test(value1, value2),
            "Predicate does not compute <Float>.compare");
      }
    }
  }

  /**
   * Test {@link <Float><Float>Cmp#get() }.
   */
  @Test
  public void testGet() {
    final <Float><Float>Cmp predicate = new <Float><Float>Cmp();
    Assertions.assertTrue(predicate.get().contains("cmp"), "Description missing ==");
  }
}
