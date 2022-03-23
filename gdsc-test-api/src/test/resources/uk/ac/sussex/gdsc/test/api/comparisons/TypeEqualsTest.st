<package>

import uk.ac.sussex.gdsc.test.api.ApiTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link <Type>Equals}.
 */
public class <Type>EqualsTest {

  /**
   * Test {@link <Type>Equals#test(<type>)}.
   */
  @Test
  public void testTest() {
    final <type>[] values = ApiTests.get<Type>Values();
    for (final <type> value1 : values) {
      final <Type>Equals predicate = new <Type>Equals(value1);
      Assertions.assertEquals(value1, predicate.getReference(),
          "Predicate has incorrect reference value");
      for (final <type> value2 : values) {
        Assertions.assertEquals(value1 == value2, predicate.test(value2),
            "Predicate does not compute ==");
      }
    }
  }

  /**
   * Test {@link <Type>Equals#get() }.
   */
  @Test
  public void testGet() {
    final <type>[] values = ApiTests.get<Type>Values();
    for (final <type> value : values) {
      final <Type>Equals predicate = new <Type>Equals(value);
      final String s = predicate.get();
      Assertions.assertTrue(s.contains("=="), "Description missing ==");
      final String stringValue = String.valueOf(value);
      Assertions.assertTrue(s.contains(stringValue),
          () -> "Description missing " + stringValue);
    }
  }
}
