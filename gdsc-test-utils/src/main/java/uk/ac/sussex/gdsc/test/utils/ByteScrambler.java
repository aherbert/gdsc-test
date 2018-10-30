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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Class used for scrambling bytes.
 *
 * <p>This is done using the a {@link MessageDigest} algorithm to scramble consecutive n-bit blocks
 * from a source seed.
 *
 * <p>Each block from the source seed is used to initialise a {@link MessageDigest}. Each call to
 * scramble the seed will update the digest with bytes from a sequence. The current digest state
 * from each block are then concatenated to create the scrambled sequence.
 */
public class ByteScrambler {

  /** The MD5 digest algorithm. */
  private static final String MD5_ALGORITHM = "MD5";

  /** The message digest. */
  private final MessageDigest[] messageDigest;

  /** The block size. */
  private final int blockSize;

  /** The seed bytes. */
  private final byte[] seed;

  /** The output bytes. */
  private final byte[] bytes;

  /**
   * Instantiates a new byte scrambler using the MD5 algorithm.
   *
   * <p>For convenience the checked exceptions are caught and re-thrown as unchecked. The MD5
   * algorithm should be supported on any Java platform.
   *
   * @param seed the seed
   * @return the byte scrambler
   * @throws IllegalArgumentException If the MD5 algorithm is not supported or the digest is not
   *         cloneable
   */
  public static ByteScrambler getByteScrambler(byte[] seed) {
    try {
      return new ByteScrambler(seed, MD5_ALGORITHM);
    } catch (NoSuchAlgorithmException | CloneNotSupportedException ex) {
      // This should not happen
      throw new IllegalArgumentException("MD5 algorithm not supported", ex);
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
        throw new RuntimeException("Clone not supported", ex);
      }

      // Finalise the clone and copy to the current random bytes
      final byte[] digest = md.digest();
      System.arraycopy(digest, 0, bytes, from, to - from);
    }

    return Arrays.copyOf(bytes, seed.length);
  }
}
