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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * Class used for scrambling bytes.
 *
 * <p>This is done using an algorithm to scramble consecutive 128-bit blocks from a source seed. The
 * scrambled outputs from each block are then concatenated to create the scrambled sequence.
 */
public class ByteScrambler {

  /** The bit scramblers. */
  private final BitScrambler128[] bitScramblers;

  /** The output length (in bytes). */
  private final int outputLength;

  /** The working output bytes. */
  private final byte[] bytes;

  /**
   * Class to scramble up to 16 bytes. The input seed bytes are used to construct a 128-bit counter.
   * This is incremented using a golden ratio and the output is then mixed through the Stafford
   * variant 13 64-bit mixer for upper and lower 64-bits.
   *
   * <p>This is similar to a SplittableRandom random number generator which uses a 64-bit counter
   * and the same mix function. The difference is the mix function does not cascade bits over the
   * entire 128-bits of the Weyl sequence; the upper and lower do not influence each other during
   * mixing. This could be updated with a different mix function, for example the 128-bit mix
   * function from the <a href="https://en.wikipedia.org/wiki/Permuted_congruential_generator">PCG
   * family</a> of RNGs (see XSL-RR-RR).
   */
  static class BitScrambler128 {
    /** The size of bytes output by the scrambler. */
    static final int BYTES = 16;

    /**
     * The golden ratio, phi, scaled to 64-bits and rounded to odd.
     *
     * <pre>
     * phi = (sqrt(5) - 1) / 2) * 2^64
     *     ~ 0.61803 * 2^64
     *     = 11400714819323198485 (unsigned 64-bit integer)
     * </pre>
     */
    private static final long GOLDEN_RATIO = 0x9e3779b97f4a7c15L;

    /**
     * The golden ratio, phi, scaled to 128-bits, truncated to the lower 64-bits and rounded to odd.
     *
     * <pre>
     * phi ~ 0.61803 * 2^128
     *     = 210306068529402873165736369884012333108.06448599
     *     = 0x9e3779b97f4a7c15f39cc0605cedc834 (unsigned 128-bit integer)
     * </pre>
     *
     * <p>This is the next 64-bits of the same number as {@link #GOLDEN_RATIO}. This number has 26
     * binary transitions (from 0 to 1 or vice versa) so satisfies the minimum of 24 transitions
     * required for a SplittableRandom increment.
     */
    private static final long GOLDEN_RATIO_128_LOWER = 0xf39cc0605cedc835L;

    /** The lower 128-bits of the counter. */
    private long lower;
    /** The upper 128-bits of the counter. */
    private long upper;

    /**
     * Create a new instance using the seed to initialise the 128-bit counter.
     *
     * <p>The seed is interpreted as a little endian 128-bit integer.
     *
     * @param seed the seed
     */
    BitScrambler128(byte[] seed) {
      // Extract the state.
      final byte[] bytes = Arrays.copyOf(seed, BYTES);
      final ByteBuffer bb = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
      lower = bb.getLong();
      upper = bb.getLong();
    }

    /**
     * Return the next 128-bit number in the sequence into the provided array.
     *
     * @param bytes the bytes
     * @param index the index
     */
    void next(byte[] bytes, int index) {
      // Add to the counter.
      // This is done as if 64-bit unsigned integers.
      // Thus negative numbers are >= 2^63.
      final long r = lower + GOLDEN_RATIO_128_LOWER;
      // Detect overflow using the method from Long::compareUnsigned(long, long)
      if (r + Long.MIN_VALUE < lower + Long.MIN_VALUE) {
        // Overflow
        upper++;
      }
      lower = r;
      upper += GOLDEN_RATIO;
      // Mix and output
      putLongBigEndian(bytes, index, stafford13(upper));
      putLongBigEndian(bytes, index + 8, stafford13(lower));
    }

    /**
     * Perform variant 13 of David Stafford's 64-bit mix function.
     *
     * <p>This is ranked first of the top 14 Stafford mixers.
     *
     * @param value the input value
     * @return the output value
     * @see <a href="http://zimbry.blogspot.com/2011/09/better-bit-mixing-improving-on.html">Better
     *      Bit Mixing - Improving on MurmurHash3&#39;s 64-bit Finalizer.</a>
     */
    static long stafford13(long value) {
      value = (value ^ (value >>> 30)) * 0xbf58476d1ce4e5b9L;
      value = (value ^ (value >>> 27)) * 0x94d049bb133111ebL;
      return value ^ (value >>> 31);
    }

    /**
     * Put a big-endian long into the working bytes array.
     *
     * @param bytes the bytes
     * @param index the index
     * @param value the value
     */
    private static void putLongBigEndian(byte[] bytes, int index, long value) {
      bytes[index] = (byte) (value >> 56);
      bytes[index + 1] = (byte) (value >> 48);
      bytes[index + 2] = (byte) (value >> 40);
      bytes[index + 3] = (byte) (value >> 32);
      bytes[index + 4] = (byte) (value >> 24);
      bytes[index + 5] = (byte) (value >> 16);
      bytes[index + 6] = (byte) (value >> 8);
      bytes[index + 7] = (byte) (value);
    }
  }

  /**
   * Creates a new byte scrambler.
   *
   * @param seed the seed
   * @return the byte scrambler
   */
  public static ByteScrambler getByteScrambler(byte[] seed) {
    return new ByteScrambler(seed);
  }

  /**
   * Creates a new byte scrambler.
   *
   * @param seed the seed
   */
  private ByteScrambler(byte[] seed) {
    final int blocks =
        (int) (((long) seed.length + BitScrambler128.BYTES - 1) / BitScrambler128.BYTES);

    // Copy the seed
    outputLength = seed.length;
    bytes = Arrays.copyOf(seed, blocks * BitScrambler128.BYTES);

    // Create a scrambler for each block of the original seed
    bitScramblers = new BitScrambler128[blocks];
    for (int i = 0; i < blocks; i++) {
      final int from = i * BitScrambler128.BYTES;
      final int to = from + BitScrambler128.BYTES;
      bitScramblers[i] = new BitScrambler128(Arrays.copyOfRange(bytes, from, to));
    }
  }

  /**
   * Scramble the current bytes and return them.
   *
   * @return the bytes
   */
  public byte[] scramble() {
    for (int i = 0; i < bitScramblers.length; i++) {
      bitScramblers[i].next(bytes, i * BitScrambler128.BYTES);
    }
    return Arrays.copyOf(bytes, outputLength);
  }
}
