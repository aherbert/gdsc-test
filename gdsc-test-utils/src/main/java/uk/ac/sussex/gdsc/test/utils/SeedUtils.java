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

import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class used for working with random seeds.
 */
public final class SeedUtils {
  /**
   * Do not allow public construction.
   */
  private SeedUtils() {}

  /**
   * Check if the bytes contain only zeros (no information).
   *
   * <p>This also returns true if the bytes are null or zero length.
   *
   * @param bytes the bytes
   * @return true if the array contains only zeros
   */
  public static boolean zeroBytes(byte[] bytes) {
    if (bytes != null) {
      for (final byte b : bytes) {
        if (b != 0) {
          // The bytes are not all zero
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Check if the bytes are null or empty (length zero).
   *
   * <p>Note: An array that is zero filled will return false. This can be detected with a call to
   * {@link #zeroBytes(byte[])}.
   *
   * @param bytes the bytes
   * @return true if null or empty
   * @see SeedUtils#zeroBytes(byte[])
   */
  public static boolean nullOrEmpty(byte[] bytes) {
    return (bytes == null || bytes.length == 0);
  }

  /**
   * Generate a secure byte seed. Uses {@link SecureRandom#nextBytes(byte[])}.
   *
   * @param size the size
   * @return the seed
   */
  public static byte[] generateSeed(int size) {
    return generateSeed(size, true);
  }

  /**
   * Generate a byte seed. Uses {@link SecureRandom#nextBytes(byte[])} for a secure seed; otherwise
   * {@link ThreadLocalRandom#nextBytes(byte[])}.
   *
   * @param size the size
   * @param secure Set to true to create a secure seed
   * @return the seed
   */
  static byte[] generateSeed(int size, boolean secure) {
    final byte[] seed = new byte[size];
    if (secure) {
      new SecureRandom().nextBytes(seed);
    } else {
      ThreadLocalRandom.current().nextBytes(seed);
    }
    return seed;
  }

  /**
   * Make a {@code byte} array from a series of {@code long} values.
   *
   * <p>Output bytes are most-significant byte first.
   *
   * @param values the values
   * @return the bytes
   */
  public static byte[] makeByteArray(long... values) {
    final int size = values.length * Long.BYTES;
    final byte[] bytes = new byte[size];

    int count = 0;
    for (final long value : values) {
      bytes[count++] = (byte) (value >>> 56);
      bytes[count++] = (byte) (value >>> 48);
      bytes[count++] = (byte) (value >>> 40);
      bytes[count++] = (byte) (value >>> 32);
      bytes[count++] = (byte) (value >>> 24);
      bytes[count++] = (byte) (value >>> 16);
      bytes[count++] = (byte) (value >>> 8);
      bytes[count++] = (byte) (value);
    }

    return bytes;
  }

  /**
   * Make a {@code byte} array from a series of {@code int} values.
   *
   * <p>Output bytes are most-significant byte first.
   *
   * @param values the values
   * @return the bytes
   */
  public static byte[] makeByteArray(int... values) {
    final int size = values.length * Integer.BYTES;
    final byte[] bytes = new byte[size];

    int count = 0;
    for (final long value : values) {
      bytes[count++] = (byte) (value >>> 24);
      bytes[count++] = (byte) (value >>> 16);
      bytes[count++] = (byte) (value >>> 8);
      bytes[count++] = (byte) (value);
    }

    return bytes;
  }

  /**
   * Make a {@code long} array from a series of {@code byte} values.
   *
   * <p>Input bytes are most-significant byte first.
   *
   * <p>If the byte array is not long enough then zeros are assumed.
   *
   * @param bytes the bytes
   * @return the values
   */
  public static long[] makeLongArray(byte... bytes) {
    // Size is rounded up to allow shorter byte arrays.
    // Note: >>> 3 is equivalent to divide by 8 but overflow safe.
    final int size = (bytes.length + Long.BYTES - 1) >>> 3;
    final long[] values = new long[size];

    int count = 0;
    int shift = Long.SIZE;
    for (final byte bi : bytes) {
      // Update the shift to set the position in the long to write the bits
      shift -= Byte.SIZE;
      // Convert the byte to a long then shift
      values[count] |= ((bi & 0xFFL) << shift);
      if (shift == 0) {
        shift = Long.SIZE;
        count++;
      }
    }

    return values;
  }

  /**
   * Make a {@code int} array from a series of {@code byte} values.
   *
   * <p>Input bytes are most-significant byte first.
   *
   * <p>If the byte array is not long enough then zeros are assumed.
   *
   * @param bytes the bytes
   * @return the values
   */
  public static int[] makeIntArray(byte... bytes) {
    // Size is rounded up to allow shorter byte arrays
    // Note: >>> 2 is equivalent to divide by 4 but overflow safe.
    final int size = (bytes.length + Integer.BYTES - 1) >>> 2;
    final int[] values = new int[size];

    int count = 0;
    int shift = Integer.SIZE;
    for (final byte bi : bytes) {
      // Update the shift to set the position in the int to write the bits
      shift -= Byte.SIZE;
      // Convert the byte to an int then shift
      values[count] |= ((bi & 0xFF) << shift);
      if (shift == 0) {
        shift = Integer.SIZE;
        count++;
      }
    }

    return values;
  }

  /**
   * Make a {@code long} from a series of {@code byte} values.
   *
   * <p>Returns {@code 0} if the input is length zero.
   *
   * <p>Creates long values from the byte values then combines them using the xor operator.
   *
   * @param bytes the bytes (must not be null)
   * @return the value
   */
  public static long makeLong(byte... bytes) {
    long result = 0;
    if (bytes.length > 0) {
      for (final long value : makeLongArray(bytes)) {
        result ^= value;
      }
    }
    return result;
  }
}
