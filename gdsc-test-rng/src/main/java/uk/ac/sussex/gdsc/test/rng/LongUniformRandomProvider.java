/*-
 * #%L
 * Genome Damage and Stability Centre Test RNG
 *
 * Contains utilities for use with Commons RNG for random tests.
 * %%
 * Copyright (C) 2018 - 2022 Alex Herbert
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

package uk.ac.sussex.gdsc.test.rng;

import java.nio.ByteBuffer;
import org.apache.commons.rng.RandomProviderState;
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

  @Override
  public RandomProviderState saveState() {
    final ByteBuffer bb = ByteBuffer.allocate(getStateSize());
    saveState(bb);
    return new RngState(bb.array());
  }

  @Override
  public void restoreState(RandomProviderState state) {
    if (state instanceof RngState) {
      final RngState rngState = (RngState) state;
      final ByteBuffer bb = ByteBuffer.wrap(rngState.state);
      restoreState(bb);
    } else {
      throw new IllegalArgumentException("Incompatible state");
    }
  }

  /**
   * Gets the state size in bytes. This is the number of bytes required to save the state
   * to a {@link ByteBuffer}.
   *
   * @return the state size
   */
  abstract int getStateSize();

  /**
   * Save state to the byte buffer. The buffer byte order is big endian.
   *
   * @param bb the byte buffer
   */
  abstract void saveState(ByteBuffer bb);

  /**
   * Restore state from the byte buffer. The buffer byte order is big endian.
   *
   * @param bb the byte buffer
   */
  abstract void restoreState(ByteBuffer bb);

  /**
   * The state of the generator.
   */
  private static class RngState implements RandomProviderState {
    /** State of the generator. */
    final byte[] state;

    /**
     * Create a new instance.
     *
     * @param state the state
     */
    RngState(byte[] state) {
      this.state = state;
    }
  }
}
