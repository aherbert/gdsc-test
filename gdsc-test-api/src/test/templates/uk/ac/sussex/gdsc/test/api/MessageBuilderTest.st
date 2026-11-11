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

  @Test
  void testIsUnsigned() {
    Assertions.assertFalse(MessageBuilder.isUnsigned(null));
    Assertions.assertFalse(MessageBuilder.isUnsigned(""));
    Assertions.assertFalse(MessageBuilder.isUnsigned("|v1-v2| \<= 1"));
    Assertions.assertTrue(MessageBuilder.isUnsigned("unsigned |v1-v2| \<= 1"));
  }<unsignedTypes,UnsignedTypes,minusOne,zero,unsignedMinusOne:{type,Type,minusOne,zero,unsignedMinusOne |
<\n>
  @Test
  void testAppendUnsigned<Type>() {
    final <type> negative = <minusOne>;
    final <type> zero = <zero>;
    // Uses the unsigned predicates to check the description identifies unsigned values
    Assertions.assertEquals(
        "expected: \<-1 (unsigned <unsignedMinusOne>)>, actual: \<0>. unsigned |v1-v2| \<= 1",
        MessageBuilder.create()
            .append(negative, zero, Predicates.<type>sAreAbsolutelyCloseUnsigned(1)).toString());
    Assertions.assertEquals(
        "expected: \<0>, actual: \<-1 (unsigned <unsignedMinusOne>)>. unsigned |v1-v2| \<= 1",
        MessageBuilder.create()
            .append(zero, negative, Predicates.<type>sAreAbsolutelyCloseUnsigned(1)).toString());
    Assertions.assertEquals(
        "value: \<-1 (unsigned <unsignedMinusOne>)>. v1=0. unsigned |v1-v2| \<= 1",
        MessageBuilder.create()
            .append(negative, Predicates.<type>IsAbsolutelyCloseUnsigned(zero, 1)).toString());
    Assertions.assertEquals("value: \<0>. v1=<unsignedMinusOne>. unsigned |v1-v2| \<= 1",
        MessageBuilder.create()
            .append(zero, Predicates.<type>IsAbsolutelyCloseUnsigned(negative, 1)).toString());
    // Combined predicates
    Assertions.assertTrue(MessageBuilder.create()
        .append(negative, zero, Predicates.<type>sAreAbsolutelyCloseUnsigned(1).negate())
        .toString().contains("(unsigned <unsignedMinusOne>)"));
    Assertions.assertTrue(MessageBuilder.create()
        .append(negative, Predicates.<type>IsAbsolutelyCloseUnsigned(zero, 1).negate())
        .toString().contains("(unsigned <unsignedMinusOne>)"));
    // Signed predicates
    Assertions.assertEquals("expected: \<-1>, actual: \<0>. |v1-v2| \<= 1",
        MessageBuilder.create()
            .append(negative, zero, Predicates.<type>sAreAbsolutelyClose(1)).toString());
    Assertions.assertEquals("value: \<-1>. v1=0. |v1-v2| \<= 1",
        MessageBuilder.create()
            .append(negative, Predicates.<type>IsAbsolutelyClose(zero, 1)).toString());
  \}}>
}
