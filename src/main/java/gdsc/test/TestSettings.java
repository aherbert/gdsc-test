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
package gdsc.test;

import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assume;

/**
 * Class used to control test settings.
 * <p>
 * This class is configured once using system properties.
 */
public class TestSettings
{
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
			logLevel = Integer.parseInt(System.getProperty("gdsc.test.logging"));
		}
		catch (final Exception e)
		{
			// Do nothing
		}
		try
		{
			testComplexity = Integer.parseInt(System.getProperty("gdsc.test.level"));
		}
		catch (final Exception e)
		{
			// Do nothing
		}
		try
		{
			seed = Long.parseLong(System.getProperty("gdsc.test.seed"));
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
	 * Gets the log level. This is setting using the system property gdsc.test.logging.
	 *
	 * @return the log level
	 */
	public static int getLogLevel()
	{
		return logLevel;
	}

	/**
	 * Gets the test complexity. This is setting using the system property gdsc.test.level.
	 *
	 * @return the test complexity
	 */
	public static int getTestComplexity()
	{
		return testComplexity;
	}

	/**
	 * Gets the seed. This is setting using the system property gdsc.test.seed.
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
	 * Assume logging is allowed at the given level.
	 * <p>
	 * Use this at the start of a test that only produces logging output (no assertions) to skip
	 * the test as logging will be ignored.
	 *
	 * @param level
	 *            the level
	 */
	public static void assume(LogLevel level)
	{
		Assume.assumeTrue(allow(level));
	}

	/**
	 * Assume testing is logging at the {@link LogLevel#WARN} level.
	 */
	public static void assumeWarn()
	{
		assume(LogLevel.WARN);
	}

	/**
	 * Assume testing is logging at the {@link LogLevel#INFO} level.
	 */
	public static void assumeInfo()
	{
		assume(LogLevel.INFO);
	}

	/**
	 * Assume testing is logging at the {@link LogLevel#DEBUG} level.
	 */
	public static void assumeDebug()
	{
		assume(LogLevel.DEBUG);
	}

	/**
	 * Assume testing is allowed at the given complexity.
	 * <p>
	 * Use this at the start of a test that has a long run time or is otherwise complex
	 * enough to warrant skipping the test if not testing at that level of complexity.
	 *
	 * @param complexity
	 *            the complexity
	 */
	public static void assume(TestComplexity complexity)
	{
		Assume.assumeTrue(allow(complexity));
	}

	/**
	 * Assume testing is allowed at low complexity.
	 */
	public static void assumeLowComplexity()
	{
		assume(TestComplexity.LOW);
	}

	/**
	 * Assume testing is allowed at medium complexity.
	 */
	public static void assumeMediumComplexity()
	{
		assume(TestComplexity.MEDIUM);
	}

	/**
	 * Assume testing is allowed at high complexity.
	 */
	public static void assumeHighComplexity()
	{
		assume(TestComplexity.HIGH);
	}

	/**
	 * Assume testing is allowed at very high complexity.
	 */
	public static void assumeVeryHighComplexity()
	{
		assume(TestComplexity.VERY_HIGH);
	}

	/**
	 * Assume testing is allowed at maximum complexity.
	 */
	public static void assumeMaximumComplexity()
	{
		assume(TestComplexity.MAXIMUM);
	}

	/**
	 * Assume logging and testing is allowed at the given level and complexity.
	 *
	 * @param level
	 *            the level
	 * @param complexity
	 *            the complexity
	 */
	public static void assume(LogLevel level, TestComplexity complexity)
	{
		Assume.assumeTrue(allow(level, complexity));
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
	 * Gets a random generator with a fixed seed set using the system property gdsc.test.seed.
	 *
	 * @return the random generator
	 */
	public static RandomGenerator getRandomGenerator()
	{
		return getRandomGenerator(seed);
	}

	/**
	 * Assume speed testing is allowed the {@link TestComplexity#MEDIUM} complexity level.
	 * <p>
	 * Use this at the start of a speed test that has a long run time or is otherwise complex
	 * enough to warrant skipping the test if not testing at that level of complexity.
	 * <p>
	 * This method is distinct from {@link #assume(TestComplexity)} so that speed tests can be optionally disabled.
	 */
	public static void assumeSpeedTest()
	{
		assumeSpeedTest(TestComplexity.MEDIUM);
	}

	/**
	 * Assume speed testing is allowed at the given complexity.
	 * <p>
	 * Use this at the start of a speed test that has a long run time or is otherwise complex
	 * enough to warrant skipping the test if not testing at that level of complexity.
	 * <p>
	 * This method is distinct from {@link #assume(TestComplexity)} so that speed tests can be optionally disabled.
	 *
	 * @param complexity
	 *            the complexity
	 */
	public static void assumeSpeedTest(TestComplexity complexity)
	{
		assume(complexity);
	}
}
