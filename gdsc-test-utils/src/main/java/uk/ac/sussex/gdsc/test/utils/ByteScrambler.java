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
 * <p>This is done using the digest algorithm to scramble consecutive n-bit blocks from a source
 * seed.
 *
 * <p>Each block is used to initialise an MD5 digest. Each call to scramble the seed will update the
 * digest with a random byte. The current digest state from each block are then concatenated to
 * create the scrambled sequence.
 */
public class ByteScrambler {

  /** The MD5 digest algorithm. */
  private static final String MD5_ALGORITHM = "MD5";

  /** The message digest. */
  private final MessageDigest[] messageDigest;

  /** The seed size. */
  private final int seedSize;

  /** The block size. */
  private final int blockSize;

  /** The bytes. */
  private final byte[] bytes;

  /** The next byte. */
  private byte nextByte;

  /**
   * Instantiates a new byte scrambler using the MD5 algorithm.
   *
   * <p>For convenience the checked exceptions are caught and rethrown as unchecked. The MD5
   * algorithm should be supported on any Java platform.
   *
   * @param seed the seed
   * @return the byte scrambler
   * @throws RuntimeException If the MD5 algorithm is not supported or the the digest is not
   *         cloneable
   */
  public static ByteScrambler getByteScrambler(byte[] seed) {
    try {
      return new ByteScrambler(seed, MD5_ALGORITHM);
    } catch (NoSuchAlgorithmException | CloneNotSupportedException ex) {
      // This should not happen
      throw new RuntimeException("MD5 algorithm not supported", ex);
    }
  }

  /**
   * Instantiates a new byte scrambler.
   *
   * <p>Note: The algorithm must support {@link MessageDigest#clone()}.
   *
   * @param seed the seed
   * @param algorithm the algorithm
   * @throws NoSuchAlgorithmException If the algorithm is not supported
   * @throws CloneNotSupportedException If the digest is not cloneable
   */
  public ByteScrambler(byte[] seed, String algorithm)
      throws NoSuchAlgorithmException, CloneNotSupportedException {
    seedSize = seed.length;

    // Try and create the digest
    final MessageDigest md = MessageDigest.getInstance(algorithm);

    // Determine the blocksize
    blockSize = md.getDigestLength();
    if (blockSize == 0) {
      throw new IllegalArgumentException("Unknown block size for algorithm: " + algorithm);
    }
    final int blocks = (seed.length + blockSize - 1) / blockSize;
    bytes = new byte[blocks * blockSize];
    messageDigest = new MessageDigest[blocks];

    // Create a digest for each block of the original seed
    for (int i = 0; i < blocks; i++) {
      // It must be cloneable to support staged computation
      messageDigest[i] = (MessageDigest) md.clone();

      // Fill will a block from the seed
      final int from = i * blockSize;
      final int to = Math.min(seed.length, from + blockSize);
      messageDigest[i].update(seed, from, to - from);
    }
  }

  /**
   * Scramble the current bytes and return them.
   *
   * @return the bytes
   */
  public byte[] scramble() {
    for (int i = 0; i < messageDigest.length; i++) {
      messageDigest[i].update(++nextByte);

      // Clone the digest to allow the state to continue
      MessageDigest md;
      try {
        md = (MessageDigest) messageDigest[i].clone();
      } catch (final CloneNotSupportedException e) {
        // This should not happen as it has already been tested to be cloneable
        throw new RuntimeException("Clone not supported", e);
      }

      // Finalise the clone and copy to the current random bytes
      final byte[] digest = md.digest();
      final int from = i * blockSize;
      final int to = Math.min(bytes.length, from + blockSize);
      System.arraycopy(digest, 0, bytes, from, to - from);
    }
    return Arrays.copyOf(bytes, seedSize);
  }
}
