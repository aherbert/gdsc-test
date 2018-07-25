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

/**
 * Provides random seeds for a repeated test.
 */
public class RandomSeed
{
	private final long seed;
	private final int currentRepetition;
	private final int totalRepetitions;

	/**
	 * Instantiates a new random seed.
	 *
	 * @param seed
	 *            the seed
	 * @param currentRepetition
	 *            the current repetition
	 * @param totalRepetitions
	 *            the total repetitions
	 */
	public RandomSeed(long seed, int currentRepetition, int totalRepetitions)
	{
		this.seed=seed;
		this.currentRepetition = currentRepetition;
		this.totalRepetitions = totalRepetitions;
	}

	/**
	 * Gets the seed.
	 *
	 * @return the seed
	 */
	public long getSeed()
	{
		return seed;
	}

	/**
	 * Gets the current repetition.
	 *
	 * @return the current repetition
	 */
	public int getCurrentRepetition()
	{
		return currentRepetition;
	}

	/**
	 * Gets the total repetitions.
	 *
	 * @return the total repetitions
	 */
	public int getTotalRepetitions()
	{
		return totalRepetitions;
	}
}
