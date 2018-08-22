/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
 *
 * Contains utilities for use with JUnit 5.
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

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.TestAbortedException;

import uk.ac.sussex.gdsc.test.utils.TestComplexity;
import uk.ac.sussex.gdsc.test.utils.TestSettings;

@SuppressWarnings("javadoc")
public class ExtraAssumptionsTest
{
    private static class TestLevel extends Level
    {
        private static final long serialVersionUID = 1L;
        final static TestLevel SEVERE_1 = new TestLevel("SEVERE_1", Level.SEVERE.intValue() + 1);
        final static TestLevel WARNING_1 = new TestLevel("WARNING_1", Level.WARNING.intValue() + 1);
        final static TestLevel INFO_1 = new TestLevel("INFO_1", Level.INFO.intValue() + 1);
        final static TestLevel FINE_1 = new TestLevel("FINE_1", Level.FINE.intValue() + 1);
        final static TestLevel FINER_1 = new TestLevel("FINER_1", Level.FINER.intValue() + 1);
        final static TestLevel FINEST_1 = new TestLevel("FINEST_1", Level.FINEST.intValue() + 1);

        protected TestLevel(String name, int value)
        {
            super(name, value);
        }
    }

    @Test
    public void canAssumeLevel()
    {
        final Logger logger = Logger.getLogger(ExtraAssumptionsTest.class.getName());
        // Test all levels
        canAssume(logger, Level.SEVERE, TestLevel.SEVERE_1);
        canAssume(logger, Level.WARNING, TestLevel.WARNING_1);
        canAssume(logger, Level.INFO, TestLevel.INFO_1);
        canAssume(logger, Level.FINE, TestLevel.FINE_1);
        canAssume(logger, Level.FINER, TestLevel.FINER_1);
        canAssume(logger, Level.FINEST, TestLevel.FINEST_1);
    }

    private static void canAssume(Logger logger, Level allowed, TestLevel notAllowed)
    {
        logger.setLevel(allowed);
        ExtraAssumptions.assume(logger, allowed);
        logger.setLevel(notAllowed);
        Assertions.assertThrows(TestAbortedException.class, () -> {
            ExtraAssumptions.assume(logger, allowed);
        });
    }

    @Test
    public void canAssumeTestComplexity()
    {
        for (final TestComplexity tc : TestComplexity.values())
            if (TestSettings.allow(tc))
                try
                {
                    ExtraAssumptions.assume(tc);
                }
                catch (final TestAbortedException e)
                {
                    fail("Test complexity is allowed: " + tc);
                }
            else
            {
                try
                {
                    ExtraAssumptions.assume(tc);
                }
                catch (final TestAbortedException e)
                {
                    continue; // This is expected
                }
                fail("Test complexity is not allowed: " + tc);
            }
    }
}
