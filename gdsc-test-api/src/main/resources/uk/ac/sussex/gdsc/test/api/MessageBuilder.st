<package>

import uk.ac.sussex.gdsc.test.api.function.BooleanBooleanBiPredicate;
import uk.ac.sussex.gdsc.test.api.function.BooleanPredicate;
import uk.ac.sussex.gdsc.test.api.function.ByteByteBiPredicate;
import uk.ac.sussex.gdsc.test.api.function.BytePredicate;
import uk.ac.sussex.gdsc.test.api.function.CharCharBiPredicate;
import uk.ac.sussex.gdsc.test.api.function.CharPredicate;
import uk.ac.sussex.gdsc.test.api.function.DoubleDoubleBiPredicate;
import uk.ac.sussex.gdsc.test.api.function.DoublePredicate;
import uk.ac.sussex.gdsc.test.api.function.FloatFloatBiPredicate;
import uk.ac.sussex.gdsc.test.api.function.FloatPredicate;
import uk.ac.sussex.gdsc.test.api.function.IntIntBiPredicate;
import uk.ac.sussex.gdsc.test.api.function.IntPredicate;
import uk.ac.sussex.gdsc.test.api.function.LongLongBiPredicate;
import uk.ac.sussex.gdsc.test.api.function.LongPredicate;
import uk.ac.sussex.gdsc.test.api.function.ShortPredicate;
import uk.ac.sussex.gdsc.test.api.function.ShortShortBiPredicate;
import java.util.function.Supplier;

/**
 * Build messages for assertions.
 */
final class MessageBuilder {
  /**
   * The prefix for the formatted value.
   *
   * \<p>This should be followed by the value then '>'.
   */
  private static final String VALUE_FORMAT = "value: \<";
  /**
   * The prefix for the formatted expected value.
   *
   * \<p>This should be followed by the expected value then '>'.
   */
  private static final String EXPECTED_FORMAT = "expected: \<";
  /**
   * The prefix for the formatted actual value.
   *
   * \<p>It is assumed this will be following the expected value.
   *
   * \<p>This should be followed by the actual value then '>'.
   */
  private static final String ACTUAL_FORMAT = ">, actual: \<";
  /**
   * The suffix for the formatted actual value.
   */
  private static final char END_FORMAT = '>';
  /** The prefix for array indices. */
  private static final String AT_INDEX = " at index ";
  /** The space ' ' character. */
  private static final char SPACE_CHAR = ' ';

  /** The message.*/
  private final StringBuilder sb = new StringBuilder(256);

  /**
   * Create an instance.
   */
  private MessageBuilder() {}

  /**
   * Create an instance with the given prefix.
   *
   * @param prefix the prefix
   */
  private MessageBuilder(String prefix) {
    if (isNotEmpty(prefix)) {
      sb.append(prefix).append(" ==> ");
    }
  }

  /**
   * Create an instance.
   *
   * @return the message builder
   */
  static MessageBuilder create() {
    return new MessageBuilder();
  }

  /**
   * Create an instance with the prefix from the message or Supplier. If the message
   * implements {@link java.util.function.Supplier Supplier\} obtain the object
   * and convert to a {@link Object#toString String\}; if a String then use the
   * message directory. Null or empty String messages are ignored.
   *
   * @param messageOrSupplier the prefix
   * @return the message builder
   */
  static MessageBuilder of(Object messageOrSupplier) {
    return new MessageBuilder(toString(messageOrSupplier));
  }

  @Override
  public String toString() {
    return sb.toString();
  }

  /**
   * Appends that the named object was null to the message.
   * \<pre>
   * name + " was &lt;null&gt;"
   * \</pre>
   *
   * @param name the name of the object
   * @return the message builder
   */
  MessageBuilder appendNull(String name) {
    sb.append(name).append(" was \<null>");
    return this;
  }

  /**
   * Append the formatted expected and actual values to the message.
   *
   * @param expected the expected value
   * @param actual the actual value
   * @return the message builder
   */
  MessageBuilder appendFormatted(String expected, String actual) {
    sb.append(EXPECTED_FORMAT).append(expected)
      .append(ACTUAL_FORMAT).append(actual).append(END_FORMAT);
    return this;
  }

  /**
   * Append the formatted value to the message.
   *
   * @param value the value
   * @return the message builder
   */
  MessageBuilder appendFormatted(String value) {
    sb.append(VALUE_FORMAT).append(value).append(END_FORMAT);
    return this;
  }

  /**
   * Append the value to the message.
   *
   * @param value the value
   * @return the message builder
   */
  MessageBuilder append(String value) {
    sb.append(value);
    return this;
  }

  /**
   * Appends the indices to the message. The indices are prefixed with " at index ".
   * \<pre>
   * " at index " + [2]
   * " at index " + [0][1][2]
   * \</pre>
   *
   * \<p>The indices are ignored if null or empty.
   *
   * @param indices the indices (can be null or empty)
   * @return the message builder
   */
  MessageBuilder append(Indices indices) {
    if (indices != null && !indices.isEmpty()) {
      sb.append(AT_INDEX);
      indices.append(sb);
    }
    return this;
  }

  /**
   * Appends the indices to the message. The indices are prefixed with " at index ".
   * \<pre>
   * " at index " + [2]
   * " at index " + [0][1][2]
   * \</pre>
   *
   * \<p>The precursor indices can be supplied if the current index is within a nested
   * array. These are ignored if null or empty.
   *
   * @param indices the precursor indices (can be null or empty)
   * @param index the current index
   * @return the message builder
   */
  MessageBuilder append(Indices indices, int index) {
    sb.append(AT_INDEX);
    if (indices != null) {
      indices.append(sb);
    }
    sb.append('[').append(index).append(']');
    return this;
  }<types,Types,Classes:{type,Type,Class |
<\n>
  /**
   * Append the expected and actual values to the message. Append a description of the
   * predicate if it implements {@link java.util.function.Supplier Supplier\} to return
   * an object that can be converted to a {@link Object#toString String\}.
   *
   * @param expected the expected value
   * @param actual the actual value
   * @param test the test predicate
   * @return the message builder
   */
  MessageBuilder append(<type> expected, <type> actual, <Type><Type>BiPredicate test) {
    sb.append(EXPECTED_FORMAT).append(expected)
      .append(ACTUAL_FORMAT).append(actual).append(END_FORMAT);
    return appendPredicate(test);
  \}

  /**
   * Append the value to the message. Append a description of the
   * predicate if it implements {@link java.util.function.Supplier Supplier\} to return
   * an object that can be converted to a {@link Object#toString String\}.
   *
   * @param value the value to test
   * @param test the test predicate
   * @return the message builder
   */
  MessageBuilder append(<type> value, <Type>Predicate test) {
    sb.append(VALUE_FORMAT).append(value).append(END_FORMAT);
    return appendPredicate(test);
  \}}>

  /**
   * Append the predicate description. This is obtained from calling {@code Supplier.get\}
   * on the predicate if it implements {@link java.util.function.Supplier\} and then
   * {@link Object#toString()\} on the returned object. If the predicate is not a {@code Supplier\}
   * then nothing is appended.
   *
   * @param predicate the predicate
   * @return the message builder
   */
  private MessageBuilder appendPredicate(Object predicate) {
    final String description = toString(predicate);
    if (isNotEmpty(description)) {
      sb.append(". ").append(description);
    }
    return this;
  }

  /**
   * Gets the string representation. The object may be a {@link String} or {@link Supplier}.
   *
   * \<ul>
   *  \<li>If the object is a {@link String} then the string is returned.
   *  \<li>If the object is a {@link Supplier} then the supplied object is obtained and,
   *      if not null, {@link Object#toString()} is returned.
   *  \<li>Otherwise returns {@code null}.
   * \</ul>
   *
   * @param object the object
   * @return the string representation
   */
  static String toString(Object object) {
    if (object instanceof String) {
      return (String) object;
    }
    if (object instanceof Supplier) {
      final Object supplied = ((Supplier\<?>) object).get();
      if (supplied != null) {
        return supplied.toString();
      }
    }
    return null;
  }

  /**
   * Checks if the string is not null or empty whitespace.
   *
   * @param string the string
   * @return true, if is not empty
   */
  static boolean isNotEmpty(String string) {
    return string != null && hasNonWhiteSpace(string);
  }

  /**
   * Checks for non white space.
   *
   * @param string the string
   * @return true, if successful
   */
  private static boolean hasNonWhiteSpace(String string) {
    for (int i = 0; i \< string.length(); i++) {
      if (string.charAt(i) > SPACE_CHAR) {
        return true;
      }
    }
    return false;
  }
}
