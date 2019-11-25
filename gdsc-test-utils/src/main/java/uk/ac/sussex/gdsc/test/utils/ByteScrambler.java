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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Class used for scrambling bytes.
 *
 * <p>This is done using a {@link MessageDigest} algorithm to scramble consecutive n-bit blocks from
 * a source seed.
 *
 * <p>Each block from the source seed is used to initialise a {@link MessageDigest}. Each call to
 * scramble the seed will update the digest with bytes from a sequence. The current digest state
 * from each block are then concatenated to create the scrambled sequence.
 */
public class ByteScrambler {

  /** The default digest algorithm. */
  private static final String DEFAULT_ALGORITHM = "SHA-256";

  /** The message digest. */
  private final MessageDigest[] messageDigest;

  /** The block size. */
  private final int blockSize;

  /** The seed bytes. */
  private final byte[] seed;

  /** The output bytes. */
  private final byte[] bytes;

  /**
   * Class to scramble up to 16 bytes. The input seed bytes are used to construct a 128-bit counter.
   * This is incremented using a golden ratio and the output is then mixed through the Stafford
   * variant 13 64-bit mixer for upper and lower 64-bits. This is effectively a 128-bit
   * SplittableRandom random number generator.
   */
  static class BitScrambler128 {
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
     * phi = (sqrt(5) - 1) / 2) * 2^128
     *     ~ 0.61803 * 2^128
     *     = 210306068529402873165736369884012333108.06448599 (unsigned 128-bit integer)
     *     = 0x9e3779b97f4a7c15f39cc0605cedc834
     * </pre>
     *
     * <p>This is the next 64-bits of the same number as {@link #GOLDEN_RATIO}. This number has 26
     * binary transitions (from 0 to 1 or vice versa) so satisfies the minimum of 24 transitions
     * required for a SplittableRandom increment.
     */
    private static final long GOLDEN_RATIO_128_LOWER = 0xf39cc0605cedc835L;

    /** The upper 128-bits of the counter. */
    private long upper;
    /** The lower 128-bits of the counter. */
    private long lower;
    /** Working area for the bytes. */
    private byte[] bytes;

    /**
     * Create a new instance using the seed to initialise the 128-bit counter.
     *
     * <p>The seed is interpreted as a little endian 128-bit integer.
     *
     * @param seed the seed
     */
    BitScrambler128(byte[] seed) {
      // Extract the state.
      bytes = Arrays.copyOf(seed, 16);
      final ByteBuffer bb = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
      lower = bb.getLong();
      upper = bb.getLong();
    }

    /**
     * Return the next 128-bit number in the sequence.
     *
     * @return the byte[]
     */
    public byte[] next() {
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
      putLongB(0, stafford13(upper));
      putLongB(8, stafford13(lower));
      return bytes;
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
     * @param index the index
     * @param value the value
     */
    private void putLongB(int index, long value) {
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
   * Instantiates a new byte scrambler using the SHA-256 algorithm.
   *
   * <p>For convenience the checked exceptions are caught and re-thrown as unchecked. The SHA-256
   * algorithm should be supported on any Java platform.
   *
   * @param seed the seed
   * @return the byte scrambler
   * @throws IllegalArgumentException If the SHA-256 algorithm is not supported or the digest is not
   *         cloneable
   */
  public static ByteScrambler getByteScrambler(byte[] seed) {
    try {
      return new ByteScrambler(seed, DEFAULT_ALGORITHM);
    } catch (NoSuchAlgorithmException | CloneNotSupportedException ex) {
      // This should not happen
      throw new IllegalArgumentException(DEFAULT_ALGORITHM + " algorithm not supported", ex);
    }
  }

  /**
   * Instantiates a new byte scrambler.
   *
   * <p>Note: The algorithm must support {@link MessageDigest#clone()} and
   * {@link MessageDigest#getDigestLength()}.
   *
   * @param seed the seed
   * @param algorithm the algorithm
   * @throws NoSuchAlgorithmException If the algorithm is not supported
   * @throws CloneNotSupportedException If the digest is not cloneable
   * @throws IllegalArgumentException If the algorithm digest length is unknown
   */
  public ByteScrambler(byte[] seed, String algorithm)
      throws NoSuchAlgorithmException, CloneNotSupportedException {
    // Try and create the digest
    final MessageDigest md = MessageDigest.getInstance(algorithm);

    // Determine the blocksize
    blockSize = md.getDigestLength();
    if (blockSize == 0) {
      throw new IllegalArgumentException("Unknown block size for algorithm: " + algorithm);
    }
    final int blocks = (seed.length + blockSize - 1) / blockSize;

    // Copy the seed
    this.seed = seed.clone();
    // Working space for digest output
    bytes = new byte[blocks * blockSize];

    // Create a digest for each block of the original seed
    messageDigest = new MessageDigest[blocks];
    // It must be cloneable to support staged computation, so clone all positions
    // including index 0.
    for (int i = 0; i < blocks; i++) {
      messageDigest[i] = (MessageDigest) md.clone();
    }
  }

  /**
   * Scramble the current bytes and return them.
   *
   * @return the bytes
   */
  public byte[] scramble() {
    // This changes all the bytes in the initial seed.
    // The operation does not matter as the digest does the scrambling,
    // it just needs to be fed (ideally) different bytes each round.
    for (int i = 0; i < seed.length; i++) {
      seed[i]++;
    }

    for (int i = 0; i < messageDigest.length; i++) {
      // Update the digest
      final int from = i * blockSize;
      final int to = Math.min(seed.length, from + blockSize);
      final int length = to - from;
      messageDigest[i].update(seed, from, length);

      // Clone the digest to allow the state to continue
      MessageDigest md;
      try {
        md = (MessageDigest) messageDigest[i].clone();
      } catch (final CloneNotSupportedException ex) {
        // This should not happen as it has already been tested to be cloneable
        throw new IllegalStateException("Clone not supported", ex);
      }

      // Finalise the clone and copy to the current random bytes
      final byte[] digest = md.digest();
      System.arraycopy(digest, 0, bytes, from, length);
    }

    return Arrays.copyOf(bytes, seed.length);
  }
}
