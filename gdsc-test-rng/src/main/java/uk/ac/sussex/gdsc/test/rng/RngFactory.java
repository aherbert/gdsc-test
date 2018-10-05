/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
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

import uk.ac.sussex.gdsc.test.utils.DataCache;
import uk.ac.sussex.gdsc.test.utils.SeedUtils;
import uk.ac.sussex.gdsc.test.utils.TestSettings;

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.core.source64.SplitMix64;
import org.apache.commons.rng.simple.RandomSource;

/**
 * A factory for creation of random number generators (RNG) that implement
 * {@link UniformRandomProvider}.
 */
public final class RngFactory {

  /** Store the seeds for the UniformRandomProvider. */
  private static final DataCache<Long, int[]> seedCache = new DataCache<>();

  /**
   * The size of the state array of {@link RandomSource#MWC_256}.
   */
  private static final int MWC_256_SEED_SIZE = 257;

  /**
   * Do not allow public construction.
   */
  private RngFactory() {}

  /**
   * Gets the uniform random provider using the given seed.
   *
   * <p>If the {@code seed} is null or empty then a random seed will be used.
   *
   * <p>This makes use of a cache to improve construction performance.
   *
   * @param seed the seed
   * @return the uniform random provider
   * @see #create(long, boolean)
   */
  public static UniformRandomProvider create(byte[] seed) {
    return create(seed, true);
  }

  /**
   * Gets the uniform random provider using the given seed.
   *
   * <p>If the {@code seed} is null or empty then a random seed will be used.
   *
   * <p>This optionally makes use of a cache to improve construction performance by storing (and
   * reusing) the full seed state of the provider for each input seed. Set {@code cache} to false to
   * limit memory usage.
   *
   * @param seed the seed
   * @param cache Set to true to enable the cache
   * @return the uniform random provider
   */
  public static UniformRandomProvider create(byte[] seed, boolean cache) {
    if (SeedUtils.nullOrEmpty(seed)) {
      return RandomSource.create(RandomSource.MWC_256);
    }

    // Currently the factory only supports limited functionality from the
    // RNG library. Convert seed to a long.
    final long longSeed = SeedUtils.makeLong(seed);

    final int[] fullSeed = (cache)
        // Use the cache
        ? seedCache.getOrComputeIfAbsent(longSeed, RngFactory::generateMwc256Seed)
        // Create a new seed
        : generateMwc256Seed(longSeed);
    return RandomSource.create(RandomSource.MWC_256, fullSeed);
  }

  /**
   * Gets the uniform random provider using the given seed.
   *
   * <p>Note: A value of {@code 0} for {@code seed} is valid. To obtain a randomly seeded provider
   * use {@link #create(byte[])} using null as the seed.
   *
   * <p>This makes use of a cache to improve construction performance.
   *
   * @param seed the seed
   * @return the uniform random provider
   * @see #create(long, boolean)
   */
  public static UniformRandomProvider create(long seed) {
    return create(seed, true);
  }

  /**
   * Gets the uniform random provider using the given seed.
   *
   * <p>Note: A value of {@code 0} for {@code seed} is valid. To obtain a randomly seeded provider
   * use {@link #create(byte[])} using null as the seed.
   *
   * <p>This optionally makes use of a cache to improve construction performance by storing (and
   * reusing) the full seed state of the provider for each input seed. Set {@code cache} to false to
   * limit memory usage.
   *
   * @param seed the seed
   * @param cache Set to true to enable the cache
   * @return the uniform random provider
   */
  public static UniformRandomProvider create(long seed, boolean cache) {
    final int[] fullSeed = (cache)
        // Use the cache
        ? seedCache.getOrComputeIfAbsent(seed, RngFactory::generateMwc256Seed)
        // Create a new seed
        : generateMwc256Seed(seed);
    return RandomSource.create(RandomSource.MWC_256, fullSeed);
  }

  /**
   * Generating a full length seed for the {@link RandomSource#MWC_256} algorithm.
   *
   * @param seed the seed
   * @return the full length seed
   */
  private static int[] generateMwc256Seed(long seed) {
    // This has been copied from org.apache.commons.rng.simple.internal.SeedFactory

    // Generate a full length seed using another RNG
    final SplitMix64 rng = new SplitMix64(seed);
    final int[] array = new int[MWC_256_SEED_SIZE];
    for (int i = 0; i < MWC_256_SEED_SIZE; i++) {
      array[i] = rng.nextInt();
    }
    return array;
  }

  /**
   * Gets a uniform random provider with a fixed seed set using the system property
   * {@link TestSettings#PROPERTY_RANDOM_SEED}.
   *
   * <p>Note: To obtain a randomly seeded provider use {@link #create(byte[])} using null as the
   * seed.
   *
   * @return the uniform random provider
   */
  public static UniformRandomProvider createWithFixedSeed() {
    return create(TestSettings.getSeed());
  }
}
