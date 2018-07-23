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

/**
 * Provides classes for use with the <a href="https://junit.org/">JUnit</a> test framework.
 * Support is provided for <a href="https://junit.org/junit4/">JUnit 4</a> and
 * <a href="https://junit.org/junit5/">JUnit 5</a>.
 * <p>
 * The package contains:
 * </p>
 * <ul>
 * <li>Additional assert functions for floating point equality using relative error (in contrast to the JUnit standard of absolute error)
 * <li>Run-time configurable logging functionality
 * <li>Run-time configurable test complexity
 * <li>Run-time configurable random generator
 * <li>A test timing service
 * </ul>
 * <p>
 * The package is configured using java runtime arguments (see {@link uk.ac.sussex.gdsc.test.TestSettings}).
 * </p>
 *
 * @see <a href="https://junit.org/junit4/">JUnit 4</a>
 * @see <a href="https://junit.org/junit5/">JUnit 5</a>
 * @see uk.ac.sussex.gdsc.test.TestSettings
 * @since 1.0.0
*/
package uk.ac.sussex.gdsc.test;
