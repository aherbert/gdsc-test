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
 * The test complexity. Lower complexity tests are assumed to be faster.
 */
public enum TestComplexity
{
	//@formatter:off

	/**  No complexity. */
	NONE { @Override public int getValue() { return 0; }},
	/**  Low complexity. */
	LOW { @Override	public int getValue() {	return 1; }},
	/**  Medium complexity. */
	MEDIUM { @Override	public int getValue() { return 2; }},
	/**  High complexity. */
	HIGH { @Override public int getValue() { return 3; }},
	/**  Very high complexity. */
	VERY_HIGH {	@Override public int getValue()	{ return 4;	}},
	/** Maximum. Used to run any test that checks complexity settings */
	MAXIMUM { @Override public int getValue() { return Integer.MAX_VALUE; }};

	//@formatter:on

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public abstract int getValue();
}
