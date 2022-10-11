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
import java.util.concurrent.atomic.AtomicLong;

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
 * <blockquote>Steele and Vigna (2021) LXM: better splittable pseudorandom number generators (and
 * almost as fast). Proceedings of the ACM on Programming Languages, Volume 5, Article 148, pp
 * 1–31.</blockquote>
 *
 * <p>Memory footprint is 256 bits and the period is 2<sup>64</sup> 2<sup>128</sup>-1.
 *
 * <p>Note: This class only supports the save/restore functionality of
 * {@link org.apache.commons.rng.RestorableUniformRandomProvider RestorableUniformRandomProvider}
 * when the saved state is used on the <em>same</em> instance of the generator.
 *
 * @see <a href="https://prng.di.unimi.it/">xorshiro / xoroshiro generators</a>
 * @see <a href="https://doi.org/10.1145/3485525">Steele &amp; Vigna (2021) Proc. ACM Programming
 *      Languages 5, 1-31</a>
 */
public final class L64X128M extends LongUniformRandomProvider {
  /** LCG multiplier. Note: (M % 8) = 5. */
  private static final long M = 0xd1342543de82ef95L;

  /**
   * Provide lazy loading of random seeds.
   */
  private static class RandomSeed {
    /** The seed. */
    private static final AtomicLong SEED = new AtomicLong(RngFactory.createSeed());
    /** The increment. */
    private static final long INC = RngFactory.createIncrement() | 1;

    /**
     * Get the next random seed.
     *
     * @return the seed
     */
    static long next() {
      return SEED.getAndAdd(INC);
    }
  }

  /** Per-instance LCG additive parameter (must be odd). */
  private final long a;
  /** State of the LCG generator. */
  private long s;
  /** State 0 of the XBG generator (x0 and x1 are never both zero). */
  private long x0;
  /** State 1 of the XBG generator (x0 and x1 are never both zero). */
  private long x1;

  /**
   * Create a new randomly seeded instance. Instances created using this constructor will start at a
   * unique point in the state cycle and are likely to generate sequences that are independent from
   * other similarly created instances. The instances will vary across program executions.
   */
  public L64X128M() {
    this(RandomSeed.next());
  }

  /**
   * Create a new instance setting the state by expansion and mixing of the provided 64-bit seed.
   *
   * @param seed the seed for the state
   */
  public L64X128M(long seed) {
    // Mix in irregular bit spacing will improve simple seeds, e.g. 0, 1, 2.
    seed ^= RngFactory.GOLDEN_RATIO;
    // Must be odd
    this.a = RngFactory.rrmxmx(seed) | 1;
    this.s = seed;
    // Will output 0 only once.
    this.x0 = RngFactory.stafford13(s + RngFactory.GOLDEN_RATIO);
    this.x1 = RngFactory.stafford13(s + 2 * RngFactory.GOLDEN_RATIO);
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
  public L64X128M(long seed0, long seed1) {
    // Mix in irregular bit spacing will improve simple seeds, e.g. 0, 1, 2.
    seed0 ^= RngFactory.GOLDEN_RATIO;
    seed1 ^= RngFactory.GOLDEN_RATIO;
    // Must be odd
    this.a = RngFactory.rrmxmx(seed0) | 1;
    this.s = seed1;
    // Will output 0 only once.
    this.x0 = RngFactory.stafford13(s + RngFactory.GOLDEN_RATIO);
    this.x1 = RngFactory.stafford13(s + 2 * RngFactory.GOLDEN_RATIO);
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
  public L64X128M(long a, long s, long x0, long x1) {
    // Must be odd
    this.a = a | 1;
    this.s = s;
    // Combine bits and check for zero seed
    if ((x0 | x1) == 0) {
      // Will output 0 only once.
      this.x0 = RngFactory.stafford13(s + RngFactory.GOLDEN_RATIO);
      this.x1 = RngFactory.stafford13(s + 2 * RngFactory.GOLDEN_RATIO);
    } else {
      this.x0 = x0;
      this.x1 = x1;
    }
  }

  @Override
  public long nextLong() {
    // See Steele and Vigna (2021), figure 1.
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
  int getStateSize() {
    return 3 * Long.BYTES;
  }

  @Override
  void saveState(ByteBuffer bb) {
    bb.putLong(s);
    bb.putLong(x0);
    bb.putLong(x1);
  }

  @Override
  void restoreState(ByteBuffer bb) {
    s = bb.getLong();
    x0 = bb.getLong();
    x1 = bb.getLong();
  }
}
