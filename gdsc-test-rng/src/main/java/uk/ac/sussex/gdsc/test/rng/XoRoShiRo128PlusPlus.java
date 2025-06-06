/*-
 * #%L
 * Genome Damage and Stability Centre Test RNG
 *
 * Contains utilities for use with Commons RNG for random tests.
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

package uk.ac.sussex.gdsc.test.rng;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A fast all-purpose 64-bit generator.
 *
 * <p>This is a member of the Xor-Shift-Rotate family of generators. Memory footprint is 128 bits
 * and the period is 2<sup>128</sup>-1.
 *
 * @see <a href="http://xoshiro.di.unimi.it/xoroshiro128plusplus.c">Original source code</a>
 * @see <a href="http://xoshiro.di.unimi.it/">xorshiro / xoroshiro generators</a>
 */
public final class XoRoShiRo128PlusPlus extends LongUniformRandomProvider {

  /** State 0 of the generator. */
  private long state0;

  /** State 1 of the generator. */
  private long state1;

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

  /**
   * Create a new randomly seeded instance. Instances created using this constructor will start at a
   * unique point in the state cycle and are likely to generate sequences that are independent from
   * other similarly created instances. The instances will vary across program executions.
   */
  public XoRoShiRo128PlusPlus() {
    this(RandomSeed.next());
  }

  /**
   * Create a new instance setting the 128-bit state by expansion and mixing of the provided 64-bit
   * seed.
   *
   * @param seed the seed for the state
   */
  public XoRoShiRo128PlusPlus(long seed) {
    this.state0 = RngFactory.rrmxmx(seed + RngFactory.GOLDEN_RATIO);
    this.state1 = RngFactory.rrmxmx(seed + 2 * RngFactory.GOLDEN_RATIO);
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
      this.state0 = RngFactory.rrmxmx(RngFactory.GOLDEN_RATIO);
      this.state1 = RngFactory.rrmxmx(2 * RngFactory.GOLDEN_RATIO);
    } else {
      state0 = seed0;
      state1 = seed1;
    }
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
  int getStateSize() {
    return 2 * Long.BYTES;
  }

  @Override
  void saveState(ByteBuffer bb) {
    bb.putLong(state0);
    bb.putLong(state1);
  }

  @Override
  void restoreState(ByteBuffer bb) {
    state0 = bb.getLong();
    state1 = bb.getLong();
  }
}
