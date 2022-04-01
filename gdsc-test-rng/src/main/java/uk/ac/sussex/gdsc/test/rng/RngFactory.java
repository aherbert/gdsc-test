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

import java.util.Arrays;
import java.util.SplittableRandom;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntUnaryOperator;
import org.apache.commons.rng.RestorableUniformRandomProvider;
import uk.ac.sussex.gdsc.test.utils.RandomSeeds;
import uk.ac.sussex.gdsc.test.utils.TestSettings;

/**
 * A factory for creation of random number generators (RNG) that implement
 * {@link RestorableUniformRandomProvider}.
 */
public final class RngFactory {
  /**
   * The golden ratio, phi, scaled to 64-bits and rounded to odd.
   *
   * <pre>
   * phi = (sqrt(5) - 1) / 2) * 2^64
   *     ~ 0.61803 * 2^64
   *     = 11400714819323198485 (unsigned 64-bit integer)
   * </pre>
   */
  static final long GOLDEN_RATIO = 0x9e3779b97f4a7c15L;

  /** Do not allow public construction. */
  private RngFactory() {}

  /**
   * Gets the uniform random provider using the given seed.
   *
   * <p>If the {@code seed} is null or empty then a random seed will be used. This is generated
   * using {@code ThreadLocalRandom.current().nextLong()}.
   *
   * @param seed the seed
   * @return the uniform random provider
   * @see ThreadLocalRandom
   */
  public static RestorableUniformRandomProvider create(byte[] seed) {
    if (RandomSeeds.nullOrEmpty(seed)) {
      // Require 128-bits of randomness. Both the JDK random generators are initialised
      // with random bits and the sequences of the seeds are different. Using only 1
      // with two calls would limit the maximum number of seeds to the RNG period which
      // is 2^64 for each. Using both with should be able to get 2^128 seeds.
      final long seed0 = ThreadLocalRandom.current().nextLong();
      final long seed1 = new SplittableRandom().nextLong();
      return new XoRoShiRo128PlusPlus(seed0, seed1);
    }

    // Currently the factory only supports limited functionality.
    // Convert seed to a long array. This may be zero padded.
    if (seed.length > 2 * Long.BYTES) {
      final long[] longSeed = Arrays.copyOf(RandomSeeds.makeLongArray(seed), 4);
      return new L64X128M(longSeed[0], longSeed[1], longSeed[2], longSeed[3]);
    }
    final long[] longSeed = Arrays.copyOf(RandomSeeds.makeLongArray(seed), 2);
    return new XoRoShiRo128PlusPlus(longSeed[0], longSeed[1]);
  }

  /**
   * Gets the uniform random provider using the given seed.
   *
   * <p>Note: A value of {@code 0} for {@code seed} is valid. To obtain a randomly seeded provider
   * use {@link #create(byte[])} passing null as the seed.
   *
   * @param seed the seed
   * @return the uniform random provider
   */
  public static RestorableUniformRandomProvider create(long seed) {
    return new XoRoShiRo128PlusPlus(seed);
  }

  /**
   * Perform the 64-bit R-R-M-X-M-X (Rotate: Rotate; Multiply; Xor shift; Multiply; Xor shift) mix
   * function of Pelle Evensen.
   *
   * <p>This mix procedure is a bijection (one-to-one mapping).
   *
   * @param value the input value
   * @return the output value
   * @see <a
   *      href="https://mostlymangling.blogspot.com/2018/07/on-mixing-functions-in-fast-splittable.html">
   *      On the mixing functions in "Fast Splittable Pseudorandom Number Generators", MurmurHash3
   *      and David Stafford&#39;s improved variants on the MurmurHash3 finalizer.</a>
   */
  static long rrmxmx(long value) {
    long out = value ^ Long.rotateRight(value, 49) ^ Long.rotateRight(value, 24);
    out *= 0x9fb21c651e98df25L;
    out ^= out >>> 28;
    out *= 0x9fb21c651e98df25L;
    return out ^ out >>> 28;
  }

  /**
   * Perform variant 13 of David Stafford's 64-bit mix function.
   *
   * <p>This is ranked first of the top 14 Stafford mixers.
   *
   * @param x the input value
   * @return the output value
   * @see <a href="http://zimbry.blogspot.com/2011/09/better-bit-mixing-improving-on.html">Better
   *      Bit Mixing - Improving on MurmurHash3&#39;s 64-bit Finalizer.</a>
   */
  static long stafford13(long x) {
    x = (x ^ (x >>> 30)) * 0xbf58476d1ce4e5b9L;
    x = (x ^ (x >>> 27)) * 0x94d049bb133111ebL;
    return x ^ (x >>> 31);
  }

  /**
   * Gets a uniform random provider with a fixed seed set using the system property
   * {@link TestSettings#PROPERTY_RANDOM_SEED}.
   *
   * <p>Note: To obtain a randomly seeded provider use {@link #create(byte[])} passing null as the
   * seed.
   *
   * @return the uniform random provider
   */
  public static RestorableUniformRandomProvider createWithFixedSeed() {
    return create(TestSettings.getSeed());
  }

  /**
   * Creates a random seed.
   *
   * @return the seed
   */
  static long createSeed() {
    return ThreadLocalRandom.current().nextLong();
  }

  /**
   * Creates an increment with a random combination of bits with many bit transitions.
   *
   * <p>For use as a Weyl sequence increment this should be set to odd.
   *
   * <pre>
   * long inc = RngUtils.createIncrement() | 1;
   * </pre>
   *
   * @return the increment
   */
  static long createIncrement() {
    final SplittableRandom r = new SplittableRandom();
    return createIncrement(n -> r.nextInt(n));
  }

  /**
   * Creates an increment with a random combination of bits with many bit transitions.
   *
   * @param nextInt Function to generate X in [0, n) for argument n
   * @return the increment
   */
  static long createIncrement(IntUnaryOperator nextInt) {
    // Any value with many bit transitions is suitable, i.e. the golden ratio.
    // However this method will not be used very often and can use an algorithm to
    // create a hex digit permutation.
    final byte[] index = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
    // This uses a partial Fisher-Yates shuffle of the top half of the index array.
    // This will generate 8 unique hex digits which are recorded into the increment.
    // This is done twice for a 16 hex digit increment with 0 to 2 occurrences of each
    // digit in total, and 0 or 1 occurrences of each digit in each 32-bit half.
    // Note:
    // Permutations(16, 8) = 518,918,400
    // Doing this twice is 2.69e17 permutations.
    // This is less than the period of a RNG with 2^64 states (1.84e19) so the nextInt
    // operation can use a small state RNG.
    long inc = 0;
    for (int k = 2; k-- != 0;) {
      for (int i = 16; i-- != 8;) {
        final int j = nextInt.applyAsInt(i + 1);
        final byte b = index[j];
        index[j] = index[i];
        index[i] = b;
        // No mask necessary using 'b & 0xf' as all bytes are positive
        // avoiding sign extension bugs
        inc = (inc << 4) | b;
      }
    }
    return inc;
  }
}
