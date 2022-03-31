<package>

import uk.ac.sussex.gdsc.test.api.ApiTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link <Type><Type>AreEqual}.
 */
public class <Type><Type>AreEqualTest {

  /**
   * Test {@link <Type><Type>AreEqual#test(<type>, <type>)}.
   */
  @Test
  public void testTest() {
    final <Type><Type>AreEqual predicate = <Type><Type>AreEqual.INSTANCE;
    final <type>[] values = ApiTests.get<Type>Values();
    for (final <type> value1 : values) {
      for (final <type> value2 : values) {
        Assertions.assertEquals(value1 == value2, predicate.test(value1, value2),
            "Predicate does not compute ==");
      }
    }
  }

  /**
   * Test {@link <Type><Type>AreEqual#get() }.
   */
  @Test
  public void testGet() {
    final <Type><Type>AreEqual predicate = new <Type><Type>AreEqual();
    Assertions.assertTrue(predicate.get().contains("=="), "Description missing ==");
  }
}
