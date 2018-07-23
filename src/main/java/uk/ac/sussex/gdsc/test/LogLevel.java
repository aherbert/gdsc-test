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

/**
 * The Log Level. Lower levels result is less verbose output.
 */
public enum LogLevel
{
	//@formatter:off

	/** Silent. Use this level to output information even if logging is disabled. */
	SILENT { @Override public int getValue() { return 0; }},
	/** Warning logging. For example this can be used to log test results that fail but are not critical. */
	WARN { @Override public int getValue() { return 1; }},
	/** Information logging. */
	INFO {@Override public int getValue() {	return 2; }},
	/** Debug logging. */
	DEBUG {	@Override public int getValue()	{ return 3; }};

	//@formatter:on

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public abstract int getValue();
}
