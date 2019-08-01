/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
 *
 * Contains utilities for use with test frameworks.
 * %%
 * Copyright (C) 2018 Alex Herbert
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
public final class HexUtils {

  /** The empty string. */
  private static final char[] EMPTY_CHARS = new char[0];
  /** The empty byte array. */
  private static final byte[] EMPTY_BYTES = new byte[0];
  /** Output Hex characters. */
  private static final char[] HEX_DIGITS =
      {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
  /** The byte used to indicate the character is unmapped. */
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

  /**
   * Do not allow public construction.
   */
  private HexUtils() {}

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
  public static char[] encodeHex(byte[] bytes) {
    // Safe for null input
    if (bytes == null || bytes.length == 0) {
      return EMPTY_CHARS;
    }
    final int l = bytes.length;
    // Two hex characters per byte
    final char[] chars = new char[l * 2];
    for (int i = 0; i < l; i++) {
      chars[2 * i] = HEX_DIGITS[(0xF0 & bytes[i]) >>> 4];
      chars[2 * i + 1] = HEX_DIGITS[0x0F & bytes[i]];
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
  public static String encodeHexString(byte[] bytes) {
    return new String(encodeHex(bytes));
  }

  /**
   * Decode the hex char sequence into bytes. Hex characters are [0-9A-Fa-f].
   *
   * <p>If the sequence is not a valid hex character then an empty array is returned.
   *
   * <p>If the sequence is an odd length then the final hex character is assumed to be '0'.
   *
   * @param string the string
   * @return the byte[]
   */
  public static byte[] decodeHex(CharSequence string) {
    // Safe for null input
    if (string == null || string.length() == 0) {
      return EMPTY_BYTES;
    }

    // Check for hex byte representation for each character
    final byte[] hexNumber = new byte[string.length()];
    for (int i = 0; i < hexNumber.length; i++) {
      final byte ch = mapToHexNumber(string.charAt(i));
      if (ch == UNMAPPED) {
        // Not valid so return empty
        return EMPTY_BYTES;
      }
      hexNumber[i] = ch;
    }

    // Convert: Two hex characters per byte
    final int length = hexNumber.length / 2;
    // Allow extra odd characters.
    final boolean odd = hexNumber.length % 2 == 1;
    final byte[] decoded = new byte[length + ((odd) ? 1 : 0)];
    // Process pairs
    for (int i = 0; i < length; i++) {
      byte hexPair = hexNumber[2 * i];
      hexPair <<= 4; // Shift to upper 4 bits
      hexPair |= hexNumber[2 * i + 1]; // Combined lower bits
      decoded[i] = hexPair;
    }

    // Handle final odd character
    if (odd) {
      decoded[decoded.length - 1] = (byte) (hexNumber[hexNumber.length - 1] << 4);
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
