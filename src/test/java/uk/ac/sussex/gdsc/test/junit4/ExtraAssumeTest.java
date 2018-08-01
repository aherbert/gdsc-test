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

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.AssumptionViolatedException;
import org.junit.Test;

import uk.ac.sussex.gdsc.test.TestComplexity;
import uk.ac.sussex.gdsc.test.TestLogTest;
import uk.ac.sussex.gdsc.test.TestSettings;

@SuppressWarnings("javadoc")
public class ExtraAssumeTest
{
	@Test
	public void canAssumeLogLevel()
	{
		final Logger logger = Logger.getLogger(TestLogTest.class.getName());
		final Level[] levels = { Level.SEVERE, Level.INFO, Level.FINEST };
		for (final Level l : levels)
			if (logger.isLoggable(l))
				try
				{
					ExtraAssume.assume(logger, l);
				}
				catch (final AssumptionViolatedException e)
				{
					Assert.fail("Log level is allowed: " + l);
				}
			else
			{
				try
				{
					ExtraAssume.assume(logger, l);
				}
				catch (final AssumptionViolatedException e)
				{
					continue; // This is expected
				}
				Assert.fail("Log level is not allowed: " + l);
			}
	}

	@Test
	public void canAssumeTestComplexity()
	{
		for (final TestComplexity tc : TestComplexity.values())
			if (TestSettings.allow(tc))
				try
				{
					ExtraAssume.assume(tc);
				}
				catch (final AssumptionViolatedException e)
				{
					Assert.fail("Test complexity is allowed: " + tc);
				}
			else
			{
				try
				{
					ExtraAssume.assume(tc);
				}
				catch (final AssumptionViolatedException e)
				{
					continue; // This is expected
				}
				Assert.fail("Test complexity is not allowed: " + tc);
			}
	}
}
