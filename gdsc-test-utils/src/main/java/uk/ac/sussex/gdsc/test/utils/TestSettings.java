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
package uk.ac.sussex.gdsc.test.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class used to control test settings.
 * <p>
 * This class is configured once using system properties. For example to
 * configure the settings for tests run using
 * <a href="https://maven.apache.org/">Maven</a>:
 *
 * <pre>
 * mvn test -Dgdsc.test.logging=1 -Dgdsc.test.level=2 -Dgdsc.test.seed=12345 -Dgdsc.test.repeats=5
 * </pre>
 *
 * Test classes can then be coded to respond to this run-time configuration.
 */
public class TestSettings {
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
     * The runtime property used to set the number of repeats for tests using the
     * seeded uniform random generator, e.g.
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

    /**
     * The number of repeats for tests using the seeded uniform random generator.
     */
    private static int repeats;

    static {
        testComplexity = getProperty(PROPERTY_TEST_COMPLEXITY, TestComplexity.NONE.getValue());
        seed = getProperty(PROPERTY_RANDOM_SEED, 0L);
        repeats = getProperty(PROPERTY_RANDOM_REPEATS, 1);
        // Ensure repeated tests run once. They should be disabled using other
        // mechanisms.
        repeats = Math.max(1, repeats);
    }

    /**
     * Gets the system property or a default value.
     *
     * @param name         the name
     * @param defaultValue the default value
     * @return the property
     */
    static int getProperty(String name, int defaultValue) {
        final String text = System.getProperty(name);
        if (text != null)
            try {
                return Integer.parseInt(text);
            } catch (final NumberFormatException e) {
                // Do nothing
            }
        return defaultValue;
    }

    /**
     * Gets the system property or a default value.
     *
     * @param name         the name
     * @param defaultValue the default value
     * @return the property
     */
    static long getProperty(String name, long defaultValue) {
        final String text = System.getProperty(name);
        if (text != null)
            try {
                return Long.parseLong(text);
            } catch (final NumberFormatException e) {
                // Do nothing
            }
        return defaultValue;
    }

    /**
     * Do not allow public construction.
     */
    private TestSettings() {
    }

    /**
     * Gets the test complexity.
     * <p>
     * This is set using the system property {@value #PROPERTY_TEST_COMPLEXITY}.
     *
     * @return the test complexity
     */
    public static int getTestComplexity() {
        return testComplexity;
    }

    /**
     * Gets the seed.
     * <p>
     * This is set using the system property {@value #PROPERTY_RANDOM_SEED}.
     * <p>
     * If not set then a random seed will be generated and logged at the
     * {@link Level#INFO} level to allow tests to be repeated (if a failure occurs).
     * <p>
     * Note: It is not recommended to use the same seed for random tests to ensure
     * the tests are robust to failure introduced by randomness.
     *
     * @return the seed
     */
    public static long getSeed() {
        if (seed == 0) {
            // Seed generation copied from Commons RNG
            // org.apache.commons.rng.simple.internal.SeedFactory
            final long t = System.currentTimeMillis();
            final int h = System.identityHashCode(Runtime.getRuntime());
            seed = t ^ makeLong(h, ~h);
            final Logger logger = Logger.getLogger(TestSettings.class.getName());
            logger.log(Level.INFO, String.format("-D%s=%d", PROPERTY_RANDOM_SEED, seed));
        }
        return seed;
    }

    /**
     * Copied from org.apache.commons.rng.core.util.NumberFactory.
     * 
     * @param v Number (high order bits).
     * @param w Number (low order bits).
     * @return a {@code long} value.
     */
    private static long makeLong(int v, int w) {
        return (((long) v) << 32) | (w & 0xffffffffL);
    }

    /**
     * Gets the repeats.
     * <p>
     * This is set using the system property {@value #PROPERTY_RANDOM_REPEATS}.
     * <p>
     * The returned value is {@code >= 1} even when the system property is not set.
     *
     * @return the repeats
     */
    public static int getRepeats() {
        return repeats;
    }

    /**
     * Check if testing is allowed at the given complexity.
     *
     * @param complexity the test complexity
     * @return true, if successful
     */
    public static boolean allow(TestComplexity complexity) {
        return complexity.getValue() <= testComplexity;
    }
}
