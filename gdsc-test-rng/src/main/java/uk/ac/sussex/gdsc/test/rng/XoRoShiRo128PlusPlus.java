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

import org.apache.commons.rng.RandomProviderState;
import org.apache.commons.rng.RestorableUniformRandomProvider;

/**
 * A fast all-purpose 64-bit generator.
 *
 * <p>This is a member of the Xor-Shift-Rotate family of generators. Memory footprint is 128 bits
 * and the period is 2<sup>128</sup>-1.
 *
 * @see <a href="http://xoshiro.di.unimi.it/xoroshiro128plusplus.c">Original source code</a>
 * @see <a href="http://xoshiro.di.unimi.it/">xorshiro / xoroshiro generators</a>
 */
public final class XoRoShiRo128PlusPlus implements RestorableUniformRandomProvider {
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
  /** The lower 32-bit mask for a long. */
  private static final long LOWER = 0xffffffffL;
  /** 2^32. */
  private static final long POW_32 = 1L << 32;

  /** State 0 of the generator. */
  private long state0;

  /** State 1 of the generator. */
  private long state1;

  /**
   * Create a new instance setting the 128-bit state by expansion and mixing of the provided 64-bit
   * seed.
   *
   * @param seed the seed for the state
   */
  public XoRoShiRo128PlusPlus(long seed) {
    this.state0 = RngUtils.rrmxmx(seed + GOLDEN_RATIO);
    this.state1 = RngUtils.rrmxmx(seed + 2 * GOLDEN_RATIO);
  }

  /**
   * Create a new instance setting the 128-bit state using the provided seed.
   *
   * <p>Note: This generator is invalid with all-zero bits in the state. If both seeds are zero the
   * results is the same as using the single argument constructor with a seed of zero where the
   * state is created by expansion and mixing of the single 64-bit seed.
   *
   * @param seed0 the seed for the first state
   * @param seed1 the seed for the second state
   */
  public XoRoShiRo128PlusPlus(long seed0, long seed1) {
    // Combine bits and check for zero seed
    if ((seed0 | seed1) == 0) {
      // Same result as single argument constructor with seed=0
      this.state0 = RngUtils.rrmxmx(GOLDEN_RATIO);
      this.state1 = RngUtils.rrmxmx(2 * GOLDEN_RATIO);
    } else {
      state0 = seed0;
      state1 = seed1;
    }
  }

  @Override
  public void nextBytes(byte[] bytes) {
    nextBytes(bytes, 0, bytes.length);
  }

  @Override
  public void nextBytes(byte[] bytes, int start, int len) {
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
      long random = nextLong();
      for (;;) {
        bytes[index++] = (byte) random;
        if (index < indexLimit) {
          random >>>= 8;
        } else {
          break;
        }
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
  public long nextLong() {
    final long s0 = state0;
    long s1 = state1;
    final long result = Long.rotateLeft(s0 + s1, 17) + s0;

    s1 ^= s0;
    state0 = Long.rotateLeft(s0, 49) ^ s1 ^ (s1 << 21); // a, b
    state1 = Long.rotateLeft(s1, 28); // c

    return result;
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
    return new RngState(state0, state1);
  }

  @Override
  public void restoreState(RandomProviderState state) {
    if (state instanceof RngState) {
      final RngState rngState = (RngState) state;
      this.state0 = rngState.state0;
      this.state1 = rngState.state1;
    } else {
      throw new IllegalArgumentException("Incompatible state");
    }
  }

  /**
   * The state of the generator.
   */
  private static class RngState implements RandomProviderState {
    /** State 0 of the generator. */
    final long state0;

    /** State 1 of the generator. */
    final long state1;

    /**
     * Create a new instance.
     *
     * @param state0 state 0 of the generator.
     * @param state1 state 1 of the generator.
     */
    RngState(long state0, long state1) {
      this.state0 = state0;
      this.state1 = state1;
    }
  }
}
