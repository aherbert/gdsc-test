/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
 *
 * Contains utilities for use with test frameworks.
 * %%
 * Copyright (C) 2018 - 2022 Alex Herbert
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package uk.ac.sussex.gdsc.test.utils;

import java.util.Arrays;

/**
 * Contains utility functions for Hexidecimal (Hex) numbers.
 */
public final class Hex {

  /** The empty string. */
  private static final char[] EMPTY_CHARS = new char[0];
  /** The empty byte array. */
  private static final byte[] EMPTY_BYTES = new byte[0];
  /** Output Hex characters. */
  private static final char[] HEX_DIGITS =
      {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
  /**
   * The byte used to indicate the character is unmapped. No ASCII character is above 127 so this
   * can use -1. Bad characters can be identified with a sign test.
   */
  private static final byte UNMAPPED = (byte) -1;

  /**
   * This array is a lookup table that translates hex characters to 4-bit byte numbers.
   */
  private static final byte[] DECODE_TABLE;

  static {
    // Lower case is after upper case
    DECODE_TABLE = new byte['f' + 1];
    Arrays.fill(DECODE_TABLE, UNMAPPED);
    int value = 0;
    for (int c = '0'; c <= '9'; c++) {
      DECODE_TABLE[c] = (byte) value++;
    }
    value = 10;
    for (int c = 'A'; c <= 'F'; c++) {
      DECODE_TABLE[c] = (byte) value++;
    }
    value = 10;
    for (int c = 'a'; c <= 'f'; c++) {
      DECODE_TABLE[c] = (byte) value++;
    }
  }

  /** No instances. */
  private Hex() {}

  /**
   * Encode the bytes as hex characters.
   *
   * <p>Output hex characters are [0-9a-f].
   *
   * <p>If the bytes are null then an empty array is returned.
   *
   * @param bytes the bytes
   * @return the hex characters
   */
  public static char[] encode(byte[] bytes) {
    // Safe for null input
    int len;
    if (bytes == null || (len = bytes.length) == 0) {
      return EMPTY_CHARS;
    }
    // Two hex characters per byte
    final char[] chars = new char[len << 1];
    for (int i = len; i-- != 0;) {
      chars[i << 1] = HEX_DIGITS[(bytes[i] & 0xf0) >>> 4];
      chars[(i << 1) + 1] = HEX_DIGITS[bytes[i] & 0xf];
    }
    return chars;
  }

  /**
   * Encode the bytes as a hex string.
   *
   * <p>Output hex characters are [0-9a-f].
   *
   * <p>If the bytes are null then an empty string is returned.
   *
   * @param bytes the bytes
   * @return the hex string
   */
  public static String encodeAsString(byte[] bytes) {
    return new String(encode(bytes));
  }

  /**
   * Decode the hex char sequence into bytes. Hex characters are [0-9A-Fa-f].
   *
   * <p>If the sequence is not a valid hex character then an empty array is returned.
   *
   * <p>If the sequence is an odd length then the final hex character is assumed to be '0'.
   *
   * @param string the string
   * @return the bytes
   */
  public static byte[] decode(CharSequence string) {
    // Safe for null input
    int len;
    if (string == null || (len = string.length()) == 0) {
      return EMPTY_BYTES;
    }

    // Convert: Two hex characters per byte
    final int length = len >> 1;
    // Allow extra odd characters.
    final byte[] decoded = new byte[length + (len & 0x1)];
    // Handle final odd character
    if ((len & 0x1) == 1) {
      final int ch1 = mapToHexNumber(string.charAt(len - 1));
      if (ch1 < 0) {
        return EMPTY_BYTES;
      }
      decoded[length] = (byte) (ch1 << 4);
    }

    // Process pairs
    for (int i = length; i-- != 0;) {
      final int ch1 = mapToHexNumber(string.charAt(i << 1));
      final int ch2 = mapToHexNumber(string.charAt((i << 1) + 1));
      if ((ch1 | ch2) < 0) {
        // Not valid so return empty
        return EMPTY_BYTES;
      }
      decoded[i] = (byte) ((ch1 << 4) | ch2);
    }

    return decoded;
  }

  /**
   * Map the character to a hex number representation (0-f) or {@link #UNMAPPED}.
   *
   * @param ch the character code
   * @return the hex number
   */
  private static byte mapToHexNumber(int ch) {
    return ch < DECODE_TABLE.length ? DECODE_TABLE[ch] : UNMAPPED;
  }
}
