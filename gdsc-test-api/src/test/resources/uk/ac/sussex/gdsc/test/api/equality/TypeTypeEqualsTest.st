<package>

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
    <Type><Type>Equals predicate = <Type><Type>Equals.getInstance();
    <type>[] values = { <values> };
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
    <Type><Type>Equals predicate = new <Type><Type>Equals();
    Assertions.assertTrue(predicate.get().contains("=="), "Description missing ==");
  }
}
