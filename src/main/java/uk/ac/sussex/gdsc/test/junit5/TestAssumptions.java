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
package uk.ac.sussex.gdsc.test.junit5;

import org.junit.jupiter.api.Assumptions;
import org.opentest4j.TestAbortedException;

import uk.ac.sussex.gdsc.test.LogLevel;
import uk.ac.sussex.gdsc.test.TestComplexity;
import uk.ac.sussex.gdsc.test.TestSettings;

/**
 * Adds additional helper assumptions to those provided by {@link org.junit.jupiter.api.Assumptions}.
 * <p>
 * Tests can be written to respond to the run-time configured {@link LogLevel} and {@link TestComplexity}, e.g.
 * <pre>
 * &#64;Test
 * public void myTest() {
 *     TestAssumptions.assume(LogLevel.INFO);
 *     TestAssumptions.assume(TestComplexity.MEDIUM);
 *     // ... do the test
 * }
 * </pre>
 */
public class TestAssumptions
{
	/**
	 * Assume logging is allowed at the given level.
	 * <p>
	 * Use this at the start of a test that only produces logging output (no assertions) to skip
	 * the test as logging will be ignored.
	 *
	 * @param level
	 *            the level
	 * @throws TestAbortedException
	 *             if the assumption is not {@code true}
	 */
	public static void assume(LogLevel level) throws TestAbortedException
	{
		Assumptions.assumeTrue(TestSettings.allow(level));
	}

	/**
	 * Assume testing is logging at the {@link LogLevel#WARN} level.
	 *
	 * @throws TestAbortedException
	 *             if the assumption is not {@code true}
	 */
	public static void assumeWarn() throws TestAbortedException
	{
		assume(LogLevel.WARN);
	}

	/**
	 * Assume testing is logging at the {@link LogLevel#INFO} level.
	 *
	 * @throws TestAbortedException
	 *             if the assumption is not {@code true}
	 */
	public static void assumeInfo() throws TestAbortedException
	{
		assume(LogLevel.INFO);
	}

	/**
	 * Assume testing is logging at the {@link LogLevel#DEBUG} level.
	 *
	 * @throws TestAbortedException
	 *             if the assumption is not {@code true}
	 */
	public static void assumeDebug() throws TestAbortedException
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
	 * @throws TestAbortedException
	 *             if the assumption is not {@code true}
	 */
	public static void assume(TestComplexity complexity) throws TestAbortedException
	{
		Assumptions.assumeTrue(TestSettings.allow(complexity));
	}

	/**
	 * Assume testing is allowed at low complexity.
	 *
	 * @throws TestAbortedException
	 *             if the assumption is not {@code true}
	 */
	public static void assumeLowComplexity() throws TestAbortedException
	{
		assume(TestComplexity.LOW);
	}

	/**
	 * Assume testing is allowed at medium complexity.
	 *
	 * @throws TestAbortedException
	 *             if the assumption is not {@code true}
	 */
	public static void assumeMediumComplexity() throws TestAbortedException
	{
		assume(TestComplexity.MEDIUM);
	}

	/**
	 * Assume testing is allowed at high complexity.
	 *
	 * @throws TestAbortedException
	 *             if the assumption is not {@code true}
	 */
	public static void assumeHighComplexity() throws TestAbortedException
	{
		assume(TestComplexity.HIGH);
	}

	/**
	 * Assume testing is allowed at very high complexity.
	 *
	 * @throws TestAbortedException
	 *             if the assumption is not {@code true}
	 */
	public static void assumeVeryHighComplexity() throws TestAbortedException
	{
		assume(TestComplexity.VERY_HIGH);
	}

	/**
	 * Assume testing is allowed at maximum complexity.
	 *
	 * @throws TestAbortedException
	 *             if the assumption is not {@code true}
	 */
	public static void assumeMaximumComplexity() throws TestAbortedException
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
	 * @throws TestAbortedException
	 *             if the assumption is not {@code true}
	 */
	public static void assume(LogLevel level, TestComplexity complexity) throws TestAbortedException
	{
		Assumptions.assumeTrue(TestSettings.allow(level, complexity));
	}

	/**
	 * Assume speed testing is allowed the {@link TestComplexity#MEDIUM} complexity level.
	 * <p>
	 * Use this at the start of a speed test that has a long run time or is otherwise complex
	 * enough to warrant skipping the test if not testing at that level of complexity.
	 * <p>
	 * This method is distinct from {@link #assume(TestComplexity)} so that speed tests can be optionally disabled.
	 *
	 * @throws TestAbortedException
	 *             if the assumption is not {@code true}
	 */
	public static void assumeSpeedTest() throws TestAbortedException
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
	 * @throws TestAbortedException
	 *             if the assumption is not {@code true}
	 */
	public static void assumeSpeedTest(TestComplexity complexity) throws TestAbortedException
	{
		assume(complexity);
	}
}
