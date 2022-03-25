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
  void testToString() {
    Assertions.assertNull(MessageBuilder.toString(null));

    final String string1 = "asdhjkfkas";
    final String string2 = "ahsfhkasdj";
    Assertions.assertEquals(string1, MessageBuilder.toString(string1));
    Assertions.assertNotEquals(string1, MessageBuilder.toString(string2));

    final Supplier\<String> supp1 = () -> string1;
    final Supplier\<String> supp2 = () -> null;
    Assertions.assertEquals(string1, MessageBuilder.toString(supp1));
    Assertions.assertNotEquals(string1, MessageBuilder.toString(supp2));

    Assertions.assertNull(MessageBuilder.toString(supp2));
  }
}
