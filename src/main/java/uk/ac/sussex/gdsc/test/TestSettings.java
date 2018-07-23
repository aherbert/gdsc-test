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

import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

/**
 * Class used to control test settings.
 * <p>
 * This class is configured once using system properties.
 * For example to configure the settings for tests run using
 * <a href="https://maven.apache.org/">Maven</a>:
 *
 * <pre>
 * mvn test -Dgdsc.test.logging=1 -Dgdsc.test.level=2 -Dgdsc.test.seed=12345
 * </pre>
 *
 * Test classes can then be coded to respond to this run-time configuration.
 */
public class TestSettings
{
	/**
	 * The runtime property used for the log level, e.g.
	 *
	 * <pre>
	 * -Dgdsc.test.logging=1
	 * </pre>
	 */
	public static final String PROPERTY_LOG_LEVEL = "gdsc.test.logging";
	/**
	 * The runtime property used for the test complexity level, e.g.
	 *
	 * <pre>
	 * -Dgdsc.test.level=1
	 * </pre>
	 */
	public static final String PROPERTY_TEST_COMPLEXITY = "gdsc.test.level";
	/**
	 * The runtime property used to seed the random generator, e.g.
	 *
	 * <pre>
	 * -Dgdsc.test.seed=12345
	 * </pre>
	 */
	public static final String PROPERTY_RANDOM_SEED = "gdsc.test.seed";

	/** The allowed log level. */
	private static int logLevel;

	/** The allowed test complexity. */
	private static int testComplexity;

	/** The fixed seed for random generator. */
	private static long seed;

	static
	{
		logLevel = LogLevel.SILENT.getValue();
		testComplexity = TestComplexity.NONE.getValue();
		seed = 30051977;

		try
		{
			logLevel = Integer.parseInt(System.getProperty(PROPERTY_LOG_LEVEL));
		}
		catch (final Exception e)
		{
			// Do nothing
		}
		try
		{
			testComplexity = Integer.parseInt(System.getProperty(PROPERTY_TEST_COMPLEXITY));
		}
		catch (final Exception e)
		{
			// Do nothing
		}
		try
		{
			seed = Long.parseLong(System.getProperty(PROPERTY_RANDOM_SEED));
		}
		catch (final Exception e)
		{
			// Do nothing
		}
	}

	/**
	 * Sets the log level. Package scope for testing.
	 *
	 * @param level
	 *            the new log level
	 */
	static void setLogLevel(LogLevel level)
	{
		logLevel = level.getValue();
	}

	/**
	 * Sets the text complexity. Package scope for testing.
	 *
	 * @param complexity
	 *            the new text complexity
	 */
	static void setTextComplexity(TestComplexity complexity)
	{
		testComplexity = complexity.getValue();
	}

	/**
	 * Gets the log level. This is setting using the system property uk.ac.sussex.gdsc.test.logging.
	 *
	 * @return the log level
	 */
	public static int getLogLevel()
	{
		return logLevel;
	}

	/**
	 * Gets the test complexity. This is setting using the system property uk.ac.sussex.gdsc.test.level.
	 *
	 * @return the test complexity
	 */
	public static int getTestComplexity()
	{
		return testComplexity;
	}

	/**
	 * Gets the seed. This is setting using the system property uk.ac.sussex.gdsc.test.seed.
	 *
	 * @return the seed
	 */
	public static long getSeed()
	{
		return seed;
	}

	/**
	 * Check if logging is allowed at the given level.
	 *
	 * @param level
	 *            the level
	 * @return true, if successful
	 */
	public static boolean allow(LogLevel level)
	{
		return level.getValue() <= logLevel;
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

	/**
	 * Check if logging and testing is allowed at the given level and complexity.
	 *
	 * @param level
	 *            the level
	 * @param complexity
	 *            the complexity
	 * @return true, if successful
	 */
	public static boolean allow(LogLevel level, TestComplexity complexity)
	{
		return allow(level) && allow(complexity);
	}

	/**
	 * Gets the random generator. If the seed is 0 then a random seed will be used.
	 *
	 * @param seed
	 *            the seed
	 * @return the random generator
	 */
	public static RandomGenerator getRandomGenerator(long seed)
	{
		return (seed == 0) ? new Well19937c() : new Well19937c(seed);
	}

	/**
	 * Gets a random generator with a fixed seed set using the system property uk.ac.sussex.gdsc.test.seed.
	 *
	 * @return the random generator
	 */
	public static RandomGenerator getRandomGenerator()
	{
		return getRandomGenerator(seed);
	}

}
