/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
 *
 * Contains utilities for use with test frameworks.
 * %%
 * Copyright (C) 2018 - 2025 Alex Herbert
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
   * Class to scramble up to 16 bytes. This is based on SpookyHash V2.
   *
   * <p>The input entropy is from additive constants used to increment a Weyl sequence from a start
   * point based on the input seed. The increments are those used in the sequences for JDK 17s
   * RandomGenerator classes. The golden ratio is also used in SplittableRandom and
   * ThreadLocalRandom. Note: These could be any constants that satisfy the properties of the
   * increment in SplittableRandom (or the sc_const value from SpookyHash): odd and a random mix of
   * 0 and 1 bits. The number of bit transitions can be counted using:
   *
   * <pre>
   * {@code
   * Long.bitCount(z ^ (z >> 1))
   * }
   * </pre>
   *
   * @see <a href="http://www.burtleburtle.net/bob/hash/spooky.html">SpookyHash</a>
   */
  static class BitScrambler128 {
    /** The size of bytes output by the scrambler. */
    static final int BYTES = 16;

    // Golden ratio:
    // new BigDecimal(5).sqrt(new MathContext(100)).subtract(BigDecimal.ONE)
    // .divide(BigDecimal.valueOf(2), new MathContext(100)).multiply(
    // new BigDecimal(0x1.0p128)).toBigInteger().toString(16)
    // 9e3779b97f4a7c15f39cc0605cedc834

    // Silver ratio:
    // new BigDecimal(2).sqrt(new MathContext(100)).subtract(BigDecimal.ONE)
    // .multiply(new BigDecimal(0x1.0p128)).toBigInteger().toString(16)
    // 6a09e667f3bcc908b2fb1366ea957d3e

    /** The first 64-bits of the fractional part of the golden ratio. */
    private static final long GOLDEN_RATIO = 0x9e3779b97f4a7c15L;

    /** The second 64-bits of the fractional part of the golden ratio, rounded to odd. */
    private static final long GOLDEN_RATIO_128_LOWER = 0xf39cc0605cedc835L;

    /** The first 64-bits of the fractional part of the silver ratio, rounded to odd. */
    private static final long SILVER_RATIO = 0x6a09e667f3bcc909L;

    /** The second 64-bits of the fractional part of the silver ratio, rounded to odd. */
    private static final long SILVER_RATIO_128_LOWER = 0xb2fb1366ea957d3fL;

    /**
     * SpookyHashV2 constant 'sc_const': a constant which: is not zero; is odd; is a
     * not-very-regular mix of 1's and 0's; does not need any other special mathematical properties.
     */
    private static final long SC_CONST = 0xdeadbeefdeadbeefL;

    /** Spooky hash state. */
    private long a;
    /** Spooky hash state. */
    private long b;
    /** Spooky hash state. */
    private long c = SC_CONST;
    /** Spooky hash state. */
    private long d = SC_CONST;
    /** Entropy sequence. */
    private long s0;
    /** Entropy sequence. */
    private long s1;
    /** Entropy sequence. */
    private long s2;
    /** Entropy sequence. */
    private long s3;

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
      a = bb.getLong();
      b = bb.getLong();
      // Initialise entropy sequence. This is arbitrary.
      s0 = ~a;
      s1 = ~b;
      s2 = a ^ b;
      s3 = a + b;
    }

    /**
     * Return the next 128-bit number in the sequence into the provided array.
     *
     * @param bytes the bytes
     * @param index the index
     */
    void next(byte[] bytes, int index) {
      // Input 32 bytes of entropy; half before ShortMix, half after
      long h0 = a;
      long h1 = b;
      long h2 = c + (s2 += GOLDEN_RATIO);
      long h3 = d + (s3 += GOLDEN_RATIO_128_LOWER);

      // @formatter:off
      // ShortMix
      h2 = Long.rotateLeft(h2, 50);  h2 += h3;  h0 ^= h2;
      h3 = Long.rotateLeft(h3, 52);  h3 += h0;  h1 ^= h3;
      h0 = Long.rotateLeft(h0, 30);  h0 += h1;  h2 ^= h0;
      h1 = Long.rotateLeft(h1, 41);  h1 += h2;  h3 ^= h1;
      h2 = Long.rotateLeft(h2, 54);  h2 += h3;  h0 ^= h2;
      h3 = Long.rotateLeft(h3, 48);  h3 += h0;  h1 ^= h3;
      h0 = Long.rotateLeft(h0, 38);  h0 += h1;  h2 ^= h0;
      h1 = Long.rotateLeft(h1, 37);  h1 += h2;  h3 ^= h1;
      h2 = Long.rotateLeft(h2, 62);  h2 += h3;  h0 ^= h2;
      h3 = Long.rotateLeft(h3, 34);  h3 += h0;  h1 ^= h3;
      h0 = Long.rotateLeft(h0, 5);   h0 += h1;  h2 ^= h0;
      h1 = Long.rotateLeft(h1, 36);  h1 += h2;  h3 ^= h1;
      // @formatter:on

      h0 += s0 += SILVER_RATIO;
      h1 += s1 += SILVER_RATIO_128_LOWER;

      // Save state
      a = h0;
      b = h1;
      c = h2;
      d = h3;

      // Output 128-bit hash

      // @formatter:off
      // ShortEnd
      h3 ^= h2;  h2 = Long.rotateLeft(h2, 15);  h3 += h2;
      h0 ^= h3;  h3 = Long.rotateLeft(h3, 52);  h0 += h3;
      h1 ^= h0;  h0 = Long.rotateLeft(h0, 26);  h1 += h0;
      h2 ^= h1;  h1 = Long.rotateLeft(h1, 51);  h2 += h1;
      h3 ^= h2;  h2 = Long.rotateLeft(h2, 28);  h3 += h2;
      h0 ^= h3;  h3 = Long.rotateLeft(h3, 9);   h0 += h3;
      h1 ^= h0;  h0 = Long.rotateLeft(h0, 47);  h1 += h0;
      h2 ^= h1;  h1 = Long.rotateLeft(h1, 54);  h2 += h1;
      h3 ^= h2;  h2 = Long.rotateLeft(h2, 32);  h3 += h2;
      h0 ^= h3;  h3 = Long.rotateLeft(h3, 25);  h0 += h3;
      h1 ^= h0;  h0 = Long.rotateLeft(h0, 63);  h1 += h0;
      // @formatter:on

      putLongBigEndian(bytes, index, h1);
      putLongBigEndian(bytes, index + 8, h2);
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
