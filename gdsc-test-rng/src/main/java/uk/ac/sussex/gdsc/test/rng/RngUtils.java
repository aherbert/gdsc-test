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

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.commons.rng.RestorableUniformRandomProvider;
import uk.ac.sussex.gdsc.test.utils.SeedUtils;
import uk.ac.sussex.gdsc.test.utils.TestSettings;

/**
 * A factory for creation of random number generators (RNG) that implement
 * {@link RestorableUniformRandomProvider}.
 */
public final class RngUtils {
  /** Do not allow public construction. */
  private RngUtils() {}

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
    if (SeedUtils.nullOrEmpty(seed)) {
      return new XoRoShiRo128PlusPlus(ThreadLocalRandom.current().nextLong());
    }

    // Currently the factory only supports limited functionality.
    // Convert seed to a long array. This may be zero padded.
    final long[] longSeed = Arrays.copyOf(SeedUtils.makeLongArray(seed), 2);
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
}
