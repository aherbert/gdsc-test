package uk.ac.sussex.gdsc.test.rng;

import java.util.function.Function;

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.core.source64.SplitMix64;
import org.apache.commons.rng.simple.RandomSource;

import uk.ac.sussex.gdsc.test.utils.DataCache;
import uk.ac.sussex.gdsc.test.utils.TestSettings;

/**
 * A factory for creation of {@link UniformRandomProvider}.
 */
public class RNGFactory {

    /**
     * Class for generating full length seeds.
     */
    private static class SeedGenerator implements Function<Long, int[]> {
        @Override
        public int[] apply(Long source) {
            // This has been copied from org.apache.commons.rng.simple.internal.SeedFactory

            // Generate a full length seed using another RNG
            final SplitMix64 rng = new SplitMix64(source);
            final int n = 257; // Size of the state array of "MultiplyWithCarry256".
            final int[] array = new int[n];
            for (int i = 0; i < n; i++) {
                array[i] = rng.nextInt();
            }
            return array;
        }
    }

    /**
     * Do not allow public construction.
     */
    private RNGFactory() {
    }

    /** Store the seeds for the UniformRandomProvider. */
    private static final DataCache<Long, int[]> seedCache = new DataCache<>();

    /** The seed generator. */
    private static SeedGenerator SEED_GENERATOR = new SeedGenerator();

    /**
     * Gets the uniform random provider using the given seed.
     * <p>
     * If the {@code seed} is {@code 0} then a random seed will be used.
     * <p>
     * This optionally makes use of a cache to improve construction performance by
     * storing (and reusing) the full seed state of the provider for each input seed.
     *
     * @param seed  the seed
     * @param cache Set to true to enable the cache
     * @return the uniform random provider
     */
    public static UniformRandomProvider create(long seed, boolean cache) {
        if (seed == 0)
            return RandomSource.create(RandomSource.MWC_256);
        final int[] fullSeed;
        if (cache) {
            fullSeed = seedCache.getOrComputeIfAbsent(seed, SEED_GENERATOR);
        } else {
            fullSeed = SEED_GENERATOR.apply(seed);
        }
        return RandomSource.create(RandomSource.MWC_256, fullSeed);
    }

    /**
     * Gets the uniform random provider using the given seed.
     * <p>
     * If the {@code seed} is {@code 0} then a random seed will be used.
     * <p>
     * This makes use of a cache to improve construction performance.
     *
     * @param seed the seed
     * @return the uniform random provider
     * @see #create(long, boolean)
     */
    public static UniformRandomProvider create(long seed) {
        return create(seed, true);
    }

    /**
     * Gets a uniform random provider with a fixed seed set using the system
     * property {@link TestSettings#PROPERTY_RANDOM_SEED}.
     * <p>
     * Note: To obtain a randomly seeded provider use {@link #create(long)} using
     * zero as the seed.
     *
     * @return the uniform random provider
     */
    public static UniformRandomProvider createWithFixedSeed() {
        return create(TestSettings.getSeed());
    }
}
