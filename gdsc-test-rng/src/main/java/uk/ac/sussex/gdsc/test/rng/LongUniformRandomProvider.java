/*-
 * #%L
 * Genome Damage and Stability Centre Test RNG
 *
 * Contains utilities for use with Commons RNG for random tests.
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

package uk.ac.sussex.gdsc.test.rng;

import org.apache.commons.rng.RestorableUniformRandomProvider;

/**
 * Base class for 64-bit generators.
 */
abstract class LongUniformRandomProvider implements RestorableUniformRandomProvider {
  /** The lower 32-bit mask for a long. */
  private static final long LOWER = 0xffffffffL;
  /** 2^32. */
  private static final long POW_32 = 1L << 32;

  @Override
  public void nextBytes(byte[] bytes) {
    nextBytes(bytes, 0, bytes.length);
  }

  @Override
  public void nextBytes(byte[] bytes, int start, int len) {
    // Note: No range checks: e.g. Objects.checkFromIndexSize

    int index = start;

    // Index of first insertion plus multiple of 8 part of length
    // (i.e. length with 3 least significant bits unset).
    final int indexLoopLimit = index + (len & 0x7ffffff8);

    // Start filling in the byte array, 8 bytes at a time.
    while (index < indexLoopLimit) {
      final long random = nextLong();
      bytes[index++] = (byte) random;
      bytes[index++] = (byte) (random >>> 8);
      bytes[index++] = (byte) (random >>> 16);
      bytes[index++] = (byte) (random >>> 24);
      bytes[index++] = (byte) (random >>> 32);
      bytes[index++] = (byte) (random >>> 40);
      bytes[index++] = (byte) (random >>> 48);
      bytes[index++] = (byte) (random >>> 56);
    }

    final int indexLimit = start + len;

    // Fill in the remaining bytes.
    if (index < indexLimit) {
      for (long random = nextLong(); index < indexLimit; random >>>= 8) {
        bytes[index++] = (byte) random;
      }
    }
  }

  @Override
  public int nextInt() {
    return (int) (nextLong() >>> 32);
  }

  @Override
  public int nextInt(int limit) {
    if (limit <= 0) {
      throw new IllegalArgumentException("Not positive: " + limit);
    }
    // Lemire (2019): Fast Random Integer Generation in an Interval
    // https://arxiv.org/abs/1805.10941
    long mult = (nextLong() >>> 32) * limit;
    long left = mult & LOWER;
    if (left < limit) {
      // 2^32 % limit
      final long t = POW_32 % limit;
      while (left < t) {
        mult = (nextLong() >>> 32) * limit;
        left = mult & LOWER;
      }
    }
    return (int) (mult >>> 32);
  }

  @Override
  public long nextLong(long limit) {
    if (limit <= 0) {
      throw new IllegalArgumentException("Not positive: " + limit);
    }
    final long nm1 = limit - 1;
    if ((limit & nm1) == 0) {
      // Power of 2
      return nextLong() & nm1;
    }
    long bits;
    long val;
    do {
      bits = nextLong() >>> 1;
      val = bits % limit;
    } while (bits - val + nm1 < 0);

    return val;
  }

  @Override
  public boolean nextBoolean() {
    return nextLong() < 0;
  }

  @Override
  public float nextFloat() {
    return (nextLong() >>> 40) * 0x1.0p-24f;
  }

  @Override
  public double nextDouble() {
    return (nextLong() >>> 11) * 0x1.0p-53;
  }
}
