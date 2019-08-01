/*-
 * #%L
 * Genome Damage and Stability Centre Test RNG
 *
 * Contains utilities for use with Commons RNG for random tests.
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

package uk.ac.sussex.gdsc.test.rng;

import uk.ac.sussex.gdsc.test.utils.SeedUtils;
import uk.ac.sussex.gdsc.test.utils.TestSettings;

import org.apache.commons.rng.RestorableUniformRandomProvider;
import org.apache.commons.rng.simple.internal.SeedFactory;

import java.util.Arrays;

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
   * <p>If the {@code seed} is null or empty then a random seed will be used.
   *
   * @param seed the seed
   * @return the uniform random provider
   */
  public static RestorableUniformRandomProvider create(byte[] seed) {
    if (SeedUtils.nullOrEmpty(seed)) {
      return new PcgXshRr32(SeedFactory.createLong(), SeedFactory.createLong());
    }

    // Currently the factory only supports limited functionality.
    // Convert seed to a long array. This may be zero padded.
    final long[] longSeed = Arrays.copyOf(SeedUtils.makeLongArray(seed), 2);
    return new PcgXshRr32(longSeed[0], longSeed[1]);
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
    return new PcgXshRr32(seed, mix(seed));
  }

  /**
   * Perform the 64-bit R-R-X-M-R-R-X-M-S-X (Rotate: Rotate; Xor; Multiply; Rotate: Rotate; Xor;
   * Multiply; Shift; Xor) mix function of Pelle Evensen.
   *
   * @param value the input value
   * @return the output value
   * @see <a
   *      href="http://mostlymangling.blogspot.com/2019/01/better-stronger-mixer-and-test-procedure.html">
   *      Better, stronger mixer and a test procedure.</a>
   */
  private static long mix(long value) {
    long out = value ^ Long.rotateRight(value, 25) ^ Long.rotateRight(value, 50);
    out *= 0xa24baed4963ee407L;
    out ^= Long.rotateRight(out, 24) ^ Long.rotateRight(out, 49);
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
