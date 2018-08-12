/*-
 * #%L
 * Genome Damage and Stability Centre Test Package
 *
 * The GDSC Test package contains code for use with the JUnit test framework.
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
package uk.ac.sussex.gdsc.test;

import java.util.function.Function;

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.core.source64.SplitMix64;
import org.apache.commons.rng.simple.RandomSource;

/**
 * Class used to control test settings.
 * <p>
 * This class is configured once using system properties.
 * For example to configure the settings for tests run using
 * <a href="https://maven.apache.org/">Maven</a>:
 *
 * <pre>
 * mvn test -Dgdsc.test.logging=1 -Dgdsc.test.level=2 -Dgdsc.test.seed=12345 -Dgdsc.test.repeats=5
 * </pre>
 *
 * Test classes can then be coded to respond to this run-time configuration.
 */
public class TestSettings
{
    /**
     * The runtime property used for the test complexity level, e.g.
     *
     * <pre>
     * -Dgdsc.test.level=1
     * </pre>
     */
    public static final String PROPERTY_TEST_COMPLEXITY = "gdsc.test.level";
    /**
     * The runtime property used to seed the uniform random generator, e.g.
     *
     * <pre>
     * -Dgdsc.test.seed=12345
     * </pre>
     */
    public static final String PROPERTY_RANDOM_SEED = "gdsc.test.seed";
    /**
     * The runtime property used to set the number of repeats for tests
     * using the seeded uniform random generator, e.g.
     *
     * <pre>
     * -Dgdsc.test.repeats=10
     * </pre>
     */
    public static final String PROPERTY_RANDOM_REPEATS = "gdsc.test.repeats";

    /** The allowed test complexity. */
    private static int testComplexity;

    /** The fixed seed for uniform random generator. */
    private static long seed;

    /** The number of repeats for tests using the seeded uniform random generator. */
    private static int repeats;

    static
    {
        testComplexity = getProperty(PROPERTY_TEST_COMPLEXITY, TestComplexity.NONE.getValue());
        seed = getProperty(PROPERTY_RANDOM_SEED, 30051977L);
        repeats = getProperty(PROPERTY_RANDOM_REPEATS, 1);
        // Ensure repeated tests run once. They should be disabled using other mechanisms.
        repeats = Math.max(1, repeats);
    }

    /**
     * Gets the system property or a default value.
     *
     * @param name
     *            the name
     * @param defaultValue
     *            the default value
     * @return the property
     */
    static int getProperty(String name, int defaultValue)
    {
        final String text = System.getProperty(name);
        if (text != null)
            try
            {
                return Integer.parseInt(text);
            }
            catch (final NumberFormatException e)
            {
                // Do nothing
            }
        return defaultValue;
    }

    /**
     * Gets the system property or a default value.
     *
     * @param name
     *            the name
     * @param defaultValue
     *            the default value
     * @return the property
     */
    static long getProperty(String name, long defaultValue)
    {
        final String text = System.getProperty(name);
        if (text != null)
            try
            {
                return Long.parseLong(text);
            }
            catch (final NumberFormatException e)
            {
                // Do nothing
            }
        return defaultValue;
    }

    /**
     * Do not allow public construction.
     */
    private TestSettings()
    {
    }

    /**
     * Gets the test complexity.
     * <p>
     * This is set using the system property {@code uk.ac.sussex.gdsc.test.level}.
     *
     * @return the test complexity
     */
    public static int getTestComplexity()
    {
        return testComplexity;
    }

    /**
     * Gets the seed.
     * <p>
     * This is set using the system property {@code uk.ac.sussex.gdsc.test.seed}.
     *
     * @return the seed
     */
    public static long getSeed()
    {
        return seed;
    }

    /**
     * Gets the repeats.
     * <p>
     * This is set using the system property {@code uk.ac.sussex.gdsc.test.repeats}.
     *
     * @return the repeats
     */
    public static int getRepeats()
    {
        return repeats;
    }

    /**
     * Check if testing is allowed at the given complexity.
     *
     * @param complexity
     *            the test complexity
     * @return true, if successful
     */
    public static boolean allow(TestComplexity complexity)
    {
        return complexity.getValue() <= testComplexity;
    }

    /** Store the seeds for the UniformRandomProvider. */
    private static final DataCache<Long, int[]> seedCache = new DataCache<>();

    /**
     * Class for generating full length seeds.
     */
    private static class SeedGenerator implements Function<Long, int[]>
    {
        @Override
        public int[] apply(Long source)
        {
            // This has been copied from org.apache.commons.rng.simple.internal.SeedFactory

            // Generate a full length seed using another RNG
            final SplitMix64 rng = new SplitMix64(source);
            final int n = 624; // Size of the state array of "Well19937c".
            final int[] array = new int[n];
            for (int i = 0; i < n; i++)
            {
                array[i] = rng.nextInt();
            }
            return array;
        }
    }

    /** The seed generator. */
    private static SeedGenerator SEED_GENERATOR = new SeedGenerator();

    /**
     * Gets the uniform random provider.
     * <p>
     * If the {@code seed} is {@code 0} then a random seed will be used.
     *
     * @param seed
     *            the seed
     * @return the uniform random provider
     */
    public static UniformRandomProvider getRandomGenerator(long seed)
    {
        if (seed == 0)
            return RandomSource.create(RandomSource.WELL_19937_C);
        final int[] fullSeed = seedCache.getOrComputeIfAbsent(seed, SEED_GENERATOR);
        return RandomSource.create(RandomSource.WELL_19937_C, fullSeed);
    }

    /**
     * Gets a uniform random provider with a fixed seed set using the
     * system property {@code uk.ac.sussex.gdsc.test.seed}.
     * <p>
     * Note: To obtain a randomly seeded provider use {@link #getRandomGenerator(long)}
     * using zero as the seed.
     *
     * @return the uniform random provider
     */
    public static UniformRandomProvider getRandomGenerator()
    {
        return getRandomGenerator(seed);
    }
}
