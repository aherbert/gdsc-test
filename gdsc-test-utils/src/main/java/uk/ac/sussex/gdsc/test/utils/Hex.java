/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
 *
 * Contains utilities for use with test frameworks.
 * %%
 * Copyright (C) 2018 - 2020 Alex Herbert
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
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
  /** The byte used to indicate the character is unmapped.
   * No ASCII character is above 127 so this can use -1.
   * Bad characters can be identified with a sign test. */
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
    for (int i = 0; i < len; i++) {
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
    // Process pairs
    for (int i = 0; i < length; i++) {
      final int ch1 = mapToHexNumber(string.charAt(i << 1));
      final int ch2 = mapToHexNumber(string.charAt((i << 1) + 1));
      if ((ch1 | ch2) < 0) {
        // Not valid so return empty
        return EMPTY_BYTES;
      }

      decoded[i] = (byte) ((ch1 << 4) | ch2);
    }

    // Handle final odd character
    if ((len & 0x1) == 1) {
      final int ch1 = mapToHexNumber(string.charAt(len - 1));
      if (ch1 < 0) {
        return EMPTY_BYTES;
      }
      decoded[decoded.length - 1] = (byte) (ch1 << 4);
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
    return (ch > DECODE_TABLE.length) ? UNMAPPED : DECODE_TABLE[ch];
  }
}
