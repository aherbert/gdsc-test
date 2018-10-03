<package>

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
    <Float><Float>Cmp predicate = <Float><Float>Cmp.getInstance();
    <float>[] values = { <values> };
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
    <Float><Float>Cmp predicate = new <Float><Float>Cmp();
    Assertions.assertTrue(predicate.get().contains("cmp"), "Description missing ==");
  }
}
