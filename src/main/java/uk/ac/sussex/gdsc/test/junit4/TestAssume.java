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
package uk.ac.sussex.gdsc.test.junit4;

import org.junit.Assume;
import org.junit.AssumptionViolatedException;

import uk.ac.sussex.gdsc.test.LogLevel;
import uk.ac.sussex.gdsc.test.TestComplexity;
import uk.ac.sussex.gdsc.test.TestSettings;

/**
 * Adds additional helper assumptions to those provided by {@link org.junit.Assume}.
 * <p>
 * Tests can be written to respond to the run-time configured {@link LogLevel} and {@link TestComplexity}, e.g.
 * <pre>
 * &#64;Test
 * public void myTest() {
 *     TestAssume.assume(LogLevel.INFO);
 *     TestAssume.assume(TestComplexity.MEDIUM);
 *     // ... do the test
 * }
 * </pre>
 */
public class TestAssume
{
	/**
	 * Assume logging is allowed at the given level.
	 * <p>
	 * Use this at the start of a test that only produces logging output (no assertions) to skip
	 * the test as logging will be ignored.
	 *
	 * @param level
	 *            the level
	 * @throws AssumptionViolatedException
	 *             Thrown if the assumption is invalid to stop the test and ignore it
	 */
	public static void assume(LogLevel level) throws AssumptionViolatedException
	{
		Assume.assumeTrue(TestSettings.allow(level));
	}

	/**
	 * Assume testing is logging at the {@link LogLevel#WARN} level.
	 *
	 * @throws AssumptionViolatedException
	 *             Thrown if the assumption is invalid to stop the test and ignore it
	 */
	public static void assumeWarn() throws AssumptionViolatedException
	{
		assume(LogLevel.WARN);
	}

	/**
	 * Assume testing is logging at the {@link LogLevel#INFO} level.
	 *
	 * @throws AssumptionViolatedException
	 *             Thrown if the assumption is invalid to stop the test and ignore it
	 */
	public static void assumeInfo() throws AssumptionViolatedException
	{
		assume(LogLevel.INFO);
	}

	/**
	 * Assume testing is logging at the {@link LogLevel#DEBUG} level.
	 *
	 * @throws AssumptionViolatedException
	 *             Thrown if the assumption is invalid to stop the test and ignore it
	 */
	public static void assumeDebug() throws AssumptionViolatedException
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
	 * @throws AssumptionViolatedException
	 *             Thrown if the assumption is invalid to stop the test and ignore it
	 */
	public static void assume(TestComplexity complexity) throws AssumptionViolatedException
	{
		Assume.assumeTrue(TestSettings.allow(complexity));
	}

	/**
	 * Assume testing is allowed at low complexity.
	 *
	 * @throws AssumptionViolatedException
	 *             Thrown if the assumption is invalid to stop the test and ignore it
	 */
	public static void assumeLowComplexity() throws AssumptionViolatedException
	{
		assume(TestComplexity.LOW);
	}

	/**
	 * Assume testing is allowed at medium complexity.
	 *
	 * @throws AssumptionViolatedException
	 *             Thrown if the assumption is invalid to stop the test and ignore it
	 */
	public static void assumeMediumComplexity() throws AssumptionViolatedException
	{
		assume(TestComplexity.MEDIUM);
	}

	/**
	 * Assume testing is allowed at high complexity.
	 *
	 * @throws AssumptionViolatedException
	 *             Thrown if the assumption is invalid to stop the test and ignore it
	 */
	public static void assumeHighComplexity() throws AssumptionViolatedException
	{
		assume(TestComplexity.HIGH);
	}

	/**
	 * Assume testing is allowed at very high complexity.
	 *
	 * @throws AssumptionViolatedException
	 *             Thrown if the assumption is invalid to stop the test and ignore it
	 */
	public static void assumeVeryHighComplexity() throws AssumptionViolatedException
	{
		assume(TestComplexity.VERY_HIGH);
	}

	/**
	 * Assume testing is allowed at maximum complexity.
	 *
	 * @throws AssumptionViolatedException
	 *             Thrown if the assumption is invalid to stop the test and ignore it
	 */
	public static void assumeMaximumComplexity() throws AssumptionViolatedException
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
	 * @throws AssumptionViolatedException
	 *             Thrown if the assumption is invalid to stop the test and ignore it
	 */
	public static void assume(LogLevel level, TestComplexity complexity) throws AssumptionViolatedException
	{
		Assume.assumeTrue(TestSettings.allow(level, complexity));
	}

	/**
	 * Assume speed testing is allowed the {@link TestComplexity#MEDIUM} complexity level.
	 * <p>
	 * Use this at the start of a speed test that has a long run time or is otherwise complex
	 * enough to warrant skipping the test if not testing at that level of complexity.
	 * <p>
	 * This method is distinct from {@link #assume(TestComplexity)} so that speed tests can be optionally disabled.
	 *
	 * @throws AssumptionViolatedException
	 *             Thrown if the assumption is invalid to stop the test and ignore it
	 */
	public static void assumeSpeedTest() throws AssumptionViolatedException
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
	 * @throws AssumptionViolatedException
	 *             Thrown if the assumption is invalid to stop the test and ignore it
	 */
	public static void assumeSpeedTest(TestComplexity complexity) throws AssumptionViolatedException
	{
		assume(complexity);
	}
}
