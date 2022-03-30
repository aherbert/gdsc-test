<package>

import java.util.function.Supplier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link MessageBuilder}.
 */
@SuppressWarnings("javadoc")
public class MessageBuilderTest {
  @Test
  void testIsNotEmpty() {
    Assertions.assertTrue(MessageBuilder.isNotEmpty("Not empty"));
    Assertions.assertFalse(MessageBuilder.isNotEmpty(""));
    Assertions.assertFalse(MessageBuilder.isNotEmpty(" "));
    Assertions.assertFalse(MessageBuilder.isNotEmpty("\t"));
    Assertions.assertFalse(MessageBuilder.isNotEmpty(null));
  }

  @Test
  void testObjectToString() {
    Assertions.assertNull(MessageBuilder.objectToString(null));

    final String string1 = "asdhjkfkas";
    final String string2 = "ahsfhkasdj";
    Assertions.assertEquals(string1, MessageBuilder.objectToString(string1));
    Assertions.assertNotEquals(string1, MessageBuilder.objectToString(string2));

    final Supplier\<String> supp1 = () -> string1;
    final Supplier\<String> supp2 = () -> null;
    Assertions.assertEquals(string1, MessageBuilder.objectToString(supp1));
    Assertions.assertNotEquals(string1, MessageBuilder.objectToString(supp2));

    Assertions.assertNull(MessageBuilder.objectToString(supp2));
  }
}
