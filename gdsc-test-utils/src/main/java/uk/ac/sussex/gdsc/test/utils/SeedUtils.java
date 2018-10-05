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

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class used for working with random seeds.
 */
public final class SeedUtils {

  /** The seed algorithm to use for SecureRandom. */
  private static final String SEED_ALGORITHM = "NativePRNGNonBlocking";

  /** The seed size in bytes. */
  private static final int SEED_SIZE = 16;

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
   * Generate a 16 byte seed.
   *
   * <p>The default is to use {@link SecureRandom} with the algorithm "NativePRNGNonBlocking".
   *
   * @return the seed
   */
  public static byte[] generateSeed() {
    return generateSeed(SEED_ALGORITHM);
  }

  /**
   * Generate a 16 byte seed.
   *
   * <p>The default is to use {@link SecureRandom} with the specified algorithm.
   *
   * <p>If the algorithm is not supported then uses {@link #generateSimpleSeed()}.
   *
   * @param algorithm the algorithm
   * @return the seed
   */
  public static byte[] generateSeed(String algorithm) {
    try {
      return SecureRandom.getInstance(algorithm).generateSeed(SEED_SIZE);
    } catch (final NoSuchAlgorithmException ex) {
      Logger.getLogger(SeedUtils.class.getName()).log(Level.WARNING,
          () -> "Defaulting to a simple seed due to unsupported algorithm: " + ex.getMessage());
      return generateSimpleSeed();
    }
  }

  /**
   * Generate a simple 16 byte seed using the current time and the system identity hashcode for the
   * runtime.
   *
   * @return the seed
   */
  public static byte[] generateSimpleSeed() {
    return generateSimpleSeed(System.currentTimeMillis());
  }

  /**
   * Generate a simple 16 byte seed using the given time and the system identity hashcode for the
   * runtime.
   *
   * @param time the time
   * @return the seed
   */
  static byte[] generateSimpleSeed(long time) {
    // Get a fallback seed.
    // Seed generation copied from Commons RNG
    // org.apache.commons.rng.simple.internal.SeedFactory
    final int hash = System.identityHashCode(Runtime.getRuntime());
    final long hashLong = createLong(hash, ~hash);
    // Pack as bytes
    return makeByteArray(time, hashLong);
  }

  /**
   * Make a long from two integer values.
   *
   * @param v1 Number 1 (high order bits).
   * @param v2 Number 2 (low order bits).
   * @return a {@code long} value.
   */
  private static long createLong(int v1, int v2) {
    return (((long) v1) << 32) | (v2 & 0xffffffffL);
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
    // Size is rounded up to allow shorter byte arrays
    final int size = (bytes.length + Long.BYTES - 1) / Long.BYTES;
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
    final int size = (bytes.length + Integer.BYTES - 1) / Integer.BYTES;
    final int[] values = new int[size];

    int count = 0;
    int shift = Integer.SIZE;
    for (final byte bi : bytes) {
      // Update the shift to set the position in the long to write the bits
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
      for (long value : makeLongArray(bytes)) {
        result ^= value;
      }
    }
    return result;
  }
}
