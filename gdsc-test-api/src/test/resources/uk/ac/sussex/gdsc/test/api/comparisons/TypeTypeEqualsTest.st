<package>

import uk.ac.sussex.gdsc.test.api.ApiTestUtils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link <Type><Type>Equals}.
 */
public class <Type><Type>EqualsTest {

  /**
   * Test {@link <Type><Type>Equals#test(<type>, <type>)}.
   */
  @Test
  public void testTest() {
    final <Type><Type>Equals predicate = <Type><Type>Equals.getInstance();
    final <type>[] values = ApiTestUtils.get<Type>Values();
    for (<type> value1 : values) {
      for (<type> value2 : values) {
        Assertions.assertEquals(value1 == value2, predicate.test(value1, value2),
            "Predicate does not compute ==");
      }
    }
  }

  /**
   * Test {@link <Type><Type>Equals#get() }.
   */
  @Test
  public void testGet() {
    final <Type><Type>Equals predicate = new <Type><Type>Equals();
    Assertions.assertTrue(predicate.get().contains("=="), "Description missing ==");
  }
}
