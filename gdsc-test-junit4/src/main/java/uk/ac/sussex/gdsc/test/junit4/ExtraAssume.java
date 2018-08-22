/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
 *
 * Contains utilities for use with JUnit 4.
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

import org.junit.Assume;
import org.junit.AssumptionViolatedException;

import uk.ac.sussex.gdsc.test.utils.TestComplexity;
import uk.ac.sussex.gdsc.test.utils.TestSettings;

/**
 * Adds additional helper assumptions to those provided by
 * {@link org.junit.Assume}.
 * <p>
 * Tests can be written to respond to the run-time configured {@link Level} and
 * {@link TestComplexity}, e.g.
 *
 * <pre>
 * &#64;Test
 * public void myTest() {
 *     Logger logger = Logger.getLogger(getClass().getName());
 *     ExtraAssume.assume(logger, Level.INFO);
 *     ExtraAssume.assume(TestComplexity.MEDIUM);
 *     // ... do the test
 * }
 * </pre>
 */
public class ExtraAssume {
    /**
     * Do not allow public construction.
     */
    private ExtraAssume() {
    }

    /**
     * Assume testing is allowed at the given complexity.
     * <p>
     * Use this at the start of a test that has a long run time or is otherwise
     * complex enough to warrant skipping the test if not testing at that level of
     * complexity.
     *
     * @param complexity the complexity
     * @throws AssumptionViolatedException Thrown if the assumption is invalid to
     *                                     stop the test and ignore it
     */
    public static void assume(TestComplexity complexity) throws AssumptionViolatedException {
        Assume.assumeTrue(TestSettings.allow(complexity));
    }

    /**
     * Assume logging is allowed at the given level.
     * <p>
     * Use this at the start of a test that only produces logging output (no
     * assertions) to skip the test as logging will be ignored.
     *
     * @param logger the logger
     * @param level  the level
     * @throws AssumptionViolatedException Thrown if the assumption is invalid to
     *                                     stop the test and ignore it
     */
    public static void assume(Logger logger, Level level) {
        Assume.assumeTrue(logger.isLoggable(level));
    }
}
