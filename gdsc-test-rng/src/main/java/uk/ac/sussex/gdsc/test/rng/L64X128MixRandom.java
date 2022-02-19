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

/**
 * A fast all-purpose 64-bit generator.
 *
 * <p>This is a member of the LXM family of generators: L represents a Linear Congruential Generator
 * (LCG); X represents a Xor Based Generator (XBG); and M represents a mixing function.
 *
 * <p>This generator uses a 64-bit LCG; a 128-bit xoroshiro128 XBG; and a mix function based on the
 * MurmurHash3 mixer with constants optimised by Doug Lea. This generator is implemented as
 * algorithm "L64X128MixRandom" in JDK 17 and the source is provided in the paper:
 *
 * <blockquote>Blackman and Vigna (2021) Scrambled Linear Psuedorandom Number Generators. ACM
 * Transactions on Mathematical Software, vol 47, pp 1–32.</blockquote>
 *
 * <p>Memory footprint is 256 bits and the period is 2<sup>64</sup> 2<sup>128</sup>-1.
 *
 * @see <a href="https://prng.di.unimi.it/">xorshiro / xoroshiro generators</a>
 * @see <a href="https://dl.acm.org/doi/10.1145/3460772">Blackman &amp; Vigna (2021) ACM Trans.
 *      Math. Soft 47, 1-32</a>
 */
public final class L64X128MixRandom extends LongUniformRandomProvider {
  /** LCG multiplier. Note: (M % 8) = 5. */
  private static final long M = 0xd1342543de82ef95L;

  /** Per-instance LCG additive parameter (must be odd). */
  private final long a;
  /** State of the LCG generator. */
  private long s;
  /** State 0 of the XBG generator (x0 and x1 are never both zero). */
  private long x0;
  /** State 2 of the XBG generator (x0 and x1 are never both zero). */
  private long x1;

  /**
   * Create a new instance setting the state by expansion and mixing of the provided 64-bit seed.
   *
   * @param seed the seed for the state
   */
  public L64X128MixRandom(long seed) {
    // Mix in irregular bit spacing will improve simple seeds, e.g. 0, 1, 2.
    seed ^= RngUtils.GOLDEN_RATIO;
    // Must be odd
    this.a = RngUtils.rrmxmx(seed) | 1;
    this.s = seed;
    // Will output 0 only once.
    this.x0 = RngUtils.stafford13(s + RngUtils.GOLDEN_RATIO);
    this.x1 = RngUtils.stafford13(s + 2 * RngUtils.GOLDEN_RATIO);
  }

  /**
   * Create a new instance setting the state using the provided seed.
   *
   * <p>Note: This generator is invalid with all-zero bits in the state. If both seeds are zero the
   * results is the same as using the single argument constructor with a seed of zero where the
   * state is created by expansion and mixing of the single 64-bit seed.
   *
   * @param seed0 the first seed for the state
   * @param seed1 the second seed for the state
   */
  public L64X128MixRandom(long seed0, long seed1) {
    // Mix in irregular bit spacing will improve simple seeds, e.g. 0, 1, 2.
    seed0 ^= RngUtils.GOLDEN_RATIO;
    seed1 ^= RngUtils.GOLDEN_RATIO;
    // Must be odd
    this.a = RngUtils.rrmxmx(seed0) | 1;
    this.s = seed1;
    // Will output 0 only once.
    this.x0 = RngUtils.stafford13(s + RngUtils.GOLDEN_RATIO);
    this.x1 = RngUtils.stafford13(s + 2 * RngUtils.GOLDEN_RATIO);
  }

  /**
   * Create a new instance setting the state using the provided seed.
   *
   * <p>Note: This generator is invalid with all-zero bits in the state of the XBG generator. If
   * both seeds are zero the state is populated using the seeded LCG.
   *
   * @param a the LCG addition (set to odd which loses 1 bit of the seed)
   * @param s the LCG state
   * @param x0 the XBG state 0
   * @param x1 the XBG state 1
   */
  public L64X128MixRandom(long a, long s, long x0, long x1) {
    // Must be odd
    this.a = a | 1;
    this.s = s;
    // Combine bits and check for zero seed
    if ((x0 | x1) == 0) {
      // Will output 0 only once.
      this.x0 = RngUtils.stafford13(s + RngUtils.GOLDEN_RATIO);
      this.x1 = RngUtils.stafford13(s + 2 * RngUtils.GOLDEN_RATIO);
    } else {
      this.x0 = x0;
      this.x1 = x1;
    }
  }

  @Override
  public long nextLong() {
    // See Blackman and Vigna (2021), figure 1.
    // Combining operation
    long z = s + x0;
    // Mixing function (lea64)
    z = (z ^ (z >>> 32)) * 0xdaba0b6eb09322e3L;
    z = (z ^ (z >>> 32)) * 0xdaba0b6eb09322e3L;
    z = (z ^ (z >>> 32));
    // Update the LCG subgenerator
    s = M * s + a;
    // Update the XBG subgenerator (xoroshiro128v1_0)
    final long q0 = x0;
    long q1 = x1;
    q1 ^= q0;
    x0 = Long.rotateLeft(q0, 24) ^ q1 ^ (q1 << 16);
    x1 = Long.rotateLeft(q1, 37);
    // Return result
    return z;
  }

  @Override
  public RandomProviderState saveState() {
    return new RngState(s, x0, x1);
  }

  @Override
  public void restoreState(RandomProviderState state) {
    if (state instanceof RngState) {
      final RngState rngState = (RngState) state;
      this.s = rngState.s;
      this.x0 = rngState.x0;
      this.x1 = rngState.x1;
    } else {
      throw new IllegalArgumentException("Incompatible state");
    }
  }

  /**
   * The state of the generator.
   */
  private static class RngState implements RandomProviderState {
    /** State of the LCG generator. */
    final long s;
    /** State 0 of the XBG generator. */
    final long x0;
    /** State 1 of the XBG generator. */
    final long x1;

    /**
     * Create a new instance.
     *
     * @param s state s
     * @param x0 state x0
     * @param x1 state x1
     */
    RngState(long s, long x0, long x1) {
      this.s = s;
      this.x0 = x0;
      this.x1 = x1;
    }
  }
}