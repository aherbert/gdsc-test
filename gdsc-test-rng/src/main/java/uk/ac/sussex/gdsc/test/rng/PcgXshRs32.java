/*-
 * #%L
 * Genome Damage and Stability Centre ImageJ Core Package
 *
 * Contains code used by:
 *
 * GDSC ImageJ Plugins - Microscopy image analysis
 *
 * GDSC SMLM ImageJ Plugins - Single molecule localisation microscopy (SMLM)
 * %%
 * Copyright (C) 2011 - 2019 Alex Herbert
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
 * Implement a Permuted Congruential Generator (PCG) using a 64-bit Linear Congruential Generator
 * (LCG) and the output XSH RS (Xor Shift; Random Shift) transform function.
 *
 * <p>This generator has 128-bits of state, outputs 32-bits per cycle and a period of
 * 2<sup>64</sup>.
 *
 * @see <a href="http://www.pcg-random.org/">PCG, A Family of Better Random Number Generators</a>
 */
public final class PcgXshRs32 implements RestorableUniformRandomProvider {
  /** The upper 32-bit mask for a long. */
  private static final long UPPER = 0xffffffff00000000L;
  /** The lower 32-bit mask for a long. */
  private static final long LOWER = 0xffffffffL;
  /** The upper 32-bit mask for a long right shifted 11-bits. */
  private static final long UPPER_SHIFT_11 = UPPER >>> 11;
  /** The lower 32-bit mask for a long right shifted 11-bits. */
  private static final long LOWER_SHIFT_11 = LOWER >>> 11;
  /** 2^32. */
  private static final long POW_32 = 1L << 32;

  /** The LCG multiplier. */
  private static final long MULTIPLIER = 6364136223846793005L;

  /** The default LCG increment. */
  private static final long DEFAULT_INCREMENT = 1442695040888963407L;

  /** The state of the LCG. */
  private long state;

  /** The increment of the LCG. */
  private long increment;

  /**
   * Create a new instance with the default increment.
   *
   * @param seedState the seed for the state
   */
  public PcgXshRs32(long seedState) {
    this.increment = DEFAULT_INCREMENT;
    this.state = bump(seedState + increment);
  }

  /**
   * Create a new instance.
   *
   * <p>The increment for the LCG is created using the upper 63-bits and setting the lowest bit to
   * odd. This ensures a full period generator and support for 2<sup>63</sup> increments.
   *
   * @param seedState the seed for the state
   * @param seedIncrement the seed for the increment
   */
  public PcgXshRs32(long seedState, long seedIncrement) {
    this.increment = (seedIncrement << 1) | 1;
    this.state = bump(seedState + increment);
  }

  /**
   * Advance the state of the LCG.
   *
   * @param state current state
   * @return next state
   */
  private long bump(long state) {
    return state * MULTIPLIER + increment;
  }

  @Override
  public void nextBytes(byte[] bytes) {
    nextBytes(bytes, 0, bytes.length);
  }

  @Override
  public void nextBytes(byte[] bytes, int start, int len) {
    int index = start; // Index of first insertion.

    // Index of first insertion plus multiple of 4 part of length
    // (i.e. length with 2 least significant bits unset).
    final int indexLoopLimit = index + (len & 0x7ffffffc);

    // Start filling in the byte array, 4 bytes at a time.
    while (index < indexLoopLimit) {
      final int random = nextInt();
      bytes[index++] = (byte) random;
      bytes[index++] = (byte) (random >>> 8);
      bytes[index++] = (byte) (random >>> 16);
      bytes[index++] = (byte) (random >>> 24);
    }

    final int indexLimit = start + len; // Index of last insertion + 1.

    // Fill in the remaining bytes.
    if (index < indexLimit) {
      long random = nextInt();
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
    final long x = state;
    state = bump(state);
    return (int) ((x ^ (x >>> 22)) >>> (22 + (int) (x >>> 61)));
  }

  @Override
  public int nextInt(int limit) {
    if (limit <= 0) {
      throw new IllegalArgumentException("Not positive: " + limit);
    }
    // Lemire (2019): Fast Random Integer Generation in an Interval
    // https://arxiv.org/abs/1805.10941
    long mult = (nextInt() & LOWER) * limit;
    long left = mult & LOWER;
    if (left < limit) {
      // 2^32 % limit
      final long t = POW_32 % limit;
      while (left < t) {
        mult = (nextInt() & LOWER) * limit;
        left = mult & LOWER;
      }
    }
    return (int) (mult >>> 32);
  }

  @Override
  public long nextLong() {
    // Get two values from the LCG
    final long x = state;
    final long y = bump(state);
    state = bump(y);
    // Perform mix function.
    // For a 32-bit output the x bits should be shifted down (22 + (int) (x >>> 61)).
    // Leave in the upper bits by shift up 32 - (22 + (int) (x >>> 61))
    final long upper = ((x ^ (x >>> 22)) << (10 - (int) (x >>> 61)));
    final long lower = ((y ^ (y >>> 22)) >>> (22 + (int) (y >>> 61)));
    return (upper & UPPER) | (lower & LOWER);
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
    return nextInt() < 0;
  }

  @Override
  public float nextFloat() {
    return (nextInt() >>> 8) * 0x1.0p-24f;
  }

  @Override
  public double nextDouble() {
    // Get two values from the LCG
    final long x = state;
    final long y = bump(state);
    state = bump(y);
    // Perform mix function as per nextLong() but righted shift 11 bits.
    // The masks must be shifted 11-bits too. This saves 1 shift operation over
    // calling (nextLong() >>> 11).
    final long upper = ((x ^ (x >>> 22)) >>> (1 + (int) (x >>> 61)));
    final long lower = ((y ^ (y >>> 22)) >>> (33 + (int) (y >>> 61)));
    return ((upper & UPPER_SHIFT_11) | (lower & LOWER_SHIFT_11)) * 0x1.0p-53;
  }

  @Override
  public RandomProviderState saveState() {
    // Transform increment when saving
    return new PcgState(state, increment >> 1);
  }

  @Override
  public void restoreState(RandomProviderState state) {
    if (state instanceof PcgState) {
      final PcgState pcgState = (PcgState) state;
      this.state = pcgState.state;
      // Reverse increment transform.
      // This ensures the full period is maintained if the state increment is not
      // odd.
      this.increment = (pcgState.increment << 1) | 1;
    } else {
      throw new IllegalArgumentException("Incompatible state");
    }
  }

  /**
   * The state of the generator.
   */
  private static class PcgState implements RandomProviderState {
    /** The state. */
    final long state;
    /** The increment. */
    final long increment;

    /**
     * Create a new instance.
     *
     * @param state the state
     * @param increment the increment
     */
    PcgState(long state, long increment) {
      this.state = state;
      this.increment = increment;
    }
  }
}
