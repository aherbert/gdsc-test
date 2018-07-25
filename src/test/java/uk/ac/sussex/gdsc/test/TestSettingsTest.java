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

import org.apache.commons.rng.UniformRandomProvider;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.opentest4j.AssertionFailedError;

@SuppressWarnings("javadoc")
public class TestSettingsTest
{
	@Test
	public void canPrintSettings()
	{
		System.out.printf("TestSettings Log Level = %d\n", TestSettings.getLogLevel());
		System.out.printf("TestSettings Test Complexity = %d\n", TestSettings.getTestComplexity());
		System.out.printf("TestSettings Seed = %d\n", TestSettings.getSeed());
	}

	@Test
	public void canGetSameRandom()
	{
		final long seed = 5656787697789L;
		UniformRandomProvider r = TestSettings.getRandomGenerator(seed);
		final double[] e = { r.nextDouble(), r.nextDouble() };
		r = TestSettings.getRandomGenerator(seed);
		final double[] o = { r.nextDouble(), r.nextDouble() };
		Assertions.assertArrayEquals(e, o);
	}

	@Test
	public void canGetDifferentRandom()
	{
		final long seed = 5656787697789L;
		UniformRandomProvider r = TestSettings.getRandomGenerator(seed);
		final double[] e = { r.nextDouble(), r.nextDouble() };
		r = TestSettings.getRandomGenerator(seed * 2);
		final double[] o = { r.nextDouble(), r.nextDouble() };
		Assertions.assertThrows(AssertionFailedError.class, () -> {
			Assertions.assertArrayEquals(e, o);
		});
	}
}
