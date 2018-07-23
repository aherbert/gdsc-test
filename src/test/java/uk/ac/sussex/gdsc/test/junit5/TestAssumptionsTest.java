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

import static org.junit.jupiter.api.Assertions.fail;
import static uk.ac.sussex.gdsc.test.junit5.TestAssumptions.assume;

import org.junit.jupiter.api.Test;
import org.opentest4j.TestAbortedException;

import uk.ac.sussex.gdsc.test.LogLevel;
import uk.ac.sussex.gdsc.test.TestComplexity;
import uk.ac.sussex.gdsc.test.TestSettings;

@SuppressWarnings("javadoc")
public class TestAssumptionsTest
{
	@Test
	public void canAssumeLogLevel()
	{
		for (final LogLevel l : LogLevel.values())
			if (TestSettings.allow(l))
				try
				{
					assume(l);
				}
				catch (final TestAbortedException e)
				{
					fail("Log level is allowed: " + l);
				}
			else
			{
				try
				{
					assume(l);
				}
				catch (final TestAbortedException e)
				{
					continue; // This is expected
				}
				fail("Log level is not allowed: " + l);
			}
	}

	@Test
	public void canAssumesTestComplexity()
	{
		for (final TestComplexity tc : TestComplexity.values())
			if (TestSettings.allow(tc))
				try
				{
					assume(tc);
				}
				catch (final TestAbortedException e)
				{
					fail("Test complexity is allowed: " + tc);
				}
			else
			{
				try
				{
					assume(tc);
				}
				catch (final TestAbortedException e)
				{
					continue; // This is expected
				}
				fail("Test complexity is not allowed: " + tc);
			}
	}
}
