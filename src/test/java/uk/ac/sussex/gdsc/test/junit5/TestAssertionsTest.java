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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import uk.ac.sussex.gdsc.test.junit5.TestAssertions;

@SuppressWarnings("javadoc")
public class TestAssertionsTest
{
	// Testing of relative error.

	// Observed and expected
	static double doubleL = 1;
	static double doubleH = 2;
	static double relativeError;
	// Create next up/down for each that changes the relative error
	static double doubleLU, doubleLD, doubleHU, doubleHD;

	// Since the relative error still uses a double the float values
	// are computed using the double relative error.
	static float floatL = (float) doubleL;
	static float floatH = (float) doubleH;
	// Create next up/down for each that changes the relative error
	static float floatLU, floatLD, floatHU, floatHD;

	static
	{
		// Note the relative error is diff / max(e, o).
		doubleLU = doubleLD = doubleL;
		doubleHU = doubleHD = doubleH;
		relativeError = relativeError(doubleL, doubleH);
		while (relativeError(doubleLU, doubleH) == relativeError)
			doubleLU = Math.nextUp(doubleLU);
		while (relativeError(doubleLD, doubleH) == relativeError)
			doubleLD = Math.nextDown(doubleLD);
		while (relativeError(doubleHU, doubleL) == relativeError)
			doubleHU = Math.nextUp(doubleHU);
		while (relativeError(doubleHD, doubleL) == relativeError)
			doubleHD = Math.nextDown(doubleHD);

		// Since the relative error still uses a double the float values
		// are computed using the double relative error.
		floatLU = floatLD = floatL;
		floatHU = floatHD = floatH;
		while (relativeError(floatLU, floatH) == relativeError)
			floatLU = Math.nextUp(floatLU);
		while (relativeError(floatLD, floatH) == relativeError)
			floatLD = Math.nextDown(floatLD);
		while (relativeError(floatHU, floatL) == relativeError)
			floatHU = Math.nextUp(floatHU);
		while (relativeError(floatHD, floatL) == relativeError)
			floatHD = Math.nextDown(floatHD);
	}

	static double relativeError(double e, double o)
	{
		// Assumes o and e are positive
		return Math.abs(o - e) / Math.max(o, e);
	}

	static double relativeError(float e, float o)
	{
		// This method is required to ensure float arithmetic in computing the error

		// Assumes o and e are positive
		return Math.abs(o - e) / Math.max(o, e);
	}

	// XXX - Copy from here
	@Test
	public void canAssertEqualsDoubleUsingRelativeError()
	{
		// Equal within relative error
		TestAssertions.assertEqualsRelative(doubleL, doubleH, relativeError);

		// Move closer together they should be equal
		TestAssertions.assertEqualsRelative(doubleLU, doubleH, relativeError);
		TestAssertions.assertEqualsRelative(doubleL, doubleHD, relativeError);
	}

	@Test
	public void canAssertEqualsDoubleWithFormattedMessage()
	{
		final double e = 1;
		final double o = 1.1;
		try
		{
			TestAssertions.assertEqualsRelative(e, o, 0, "Fixed message");
		}
		catch (final AssertionFailedError ex)
		{
			Assertions.assertTrue(ex.getMessage().contains("Fixed message"));
		}
		try
		{
			TestAssertions.assertEqualsRelative(e, o, 0, "Formatted message %d", 3);
		}
		catch (final AssertionFailedError ex)
		{
			Assertions.assertTrue(ex.getMessage().contains("Formatted message 3"));
		}
		try
		{
			TestAssertions.assertEqualsRelative(e, o, 0, () -> {
				return "Lambda message";
			});
		}
		catch (final AssertionFailedError ex)
		{
			Assertions.assertTrue(ex.getMessage().contains("Lambda message"));
		}
	}

	@Test
	public void canAssertEqualsErrorsUpDoubleUsingRelativeError()
	{
		// Move further apart they should be not equal
		Assertions.assertThrows(AssertionFailedError.class, () -> {
			TestAssertions.assertEqualsRelative(doubleL, doubleHU, relativeError);
		});
	}

	@Test
	public void canAssertEqualsErrorsDownDoubleUsingRelativeError()
	{
		// Move further apart they should be not equal
		Assertions.assertThrows(AssertionFailedError.class, () -> {
			TestAssertions.assertEqualsRelative(doubleLD, doubleH, relativeError);
		});
	}

	@Test
	public void canAssertEqualsDoubleArraysUsingRelativeError()
	{
		double[] e = new double[] { doubleL };
		TestAssertions.assertArrayEqualsRelative(e, e, 0);

		double[] o = new double[] { doubleH };
		TestAssertions.assertArrayEqualsRelative(e, o, relativeError);

		e = new double[] { doubleLU };
		o = new double[] { doubleH };
		TestAssertions.assertArrayEqualsRelative(e, o, relativeError);

		e = new double[] { doubleL };
		o = new double[] { doubleHD };
		TestAssertions.assertArrayEqualsRelative(e, o, relativeError);
	}

	@Test
	public void canAssertEqualsDoubleArraysWithFormattedMessage()
	{
		final double[] e = { 1 };
		final double[] o = { 1.1 };
		try
		{
			TestAssertions.assertArrayEqualsRelative(e, o, 0, "Fixed message");
		}
		catch (final AssertionFailedError ex)
		{
			Assertions.assertTrue(ex.getMessage().contains("Fixed message"));
		}
		try
		{
			TestAssertions.assertArrayEqualsRelative(e, o, 0, "Formatted message %d", 3);
		}
		catch (final AssertionFailedError ex)
		{
			Assertions.assertTrue(ex.getMessage().contains("Formatted message 3"));
		}
		try
		{
			TestAssertions.assertArrayEqualsRelative(e, o, 0, () -> {
				return "Lambda message";
			});
		}
		catch (final AssertionFailedError ex)
		{
			Assertions.assertTrue(ex.getMessage().contains("Lambda message"));
		}
	}

	@Test
	public void canAssertEqualsErrorsUpDoubleArraysUsingRelativeError()
	{
		final double[] e = new double[] { doubleL };
		final double[] o = new double[] { doubleHU };
		Assertions.assertThrows(AssertionFailedError.class, () -> {
			TestAssertions.assertArrayEqualsRelative(e, o, relativeError);
			throw new AssertionFailedError(); // TODO - remove this when the implementation is done
		});
	}

	@Test
	public void canAssertEqualsErrorsDownDoubleArraysUsingRelativeError()
	{
		final double[] e = new double[] { doubleLD };
		final double[] o = new double[] { doubleH };
		Assertions.assertThrows(AssertionFailedError.class, () -> {
			TestAssertions.assertArrayEqualsRelative(e, o, relativeError);
			throw new AssertionFailedError(); // TODO - remove this when the implementation is done
		});
	}

	@Test
	public void canAssertEqualsNestedDoubleArraysUsingRelativeError()
	{
		final double[][] e = new double[][] { { 1 } };
		final double[][] o = new double[][] { { 2 } };
		TestAssertions.assertArrayEqualsRelative(e, o, 0.5);
	}

	@Test
	public void canAssertEqualsNestedDoubleArraysWithFormattedMessage()
	{
		final double[][] e = new double[][] { { 1 } };
		final double[][] o = new double[][] { { 1.1 } };
		try
		{
			TestAssertions.assertArrayEqualsRelative(e, o, 0, "Fixed message");
		}
		catch (final AssertionFailedError ex)
		{
			Assertions.assertTrue(ex.getMessage().contains("Fixed message"));
		}
		try
		{
			TestAssertions.assertArrayEqualsRelative((Object[]) e, (Object[]) o, 0, "Formatted message %d", 3);
		}
		catch (final AssertionFailedError ex)
		{
			Assertions.assertTrue(ex.getMessage().contains("Formatted message 3"));
		}
		try
		{
			TestAssertions.assertArrayEqualsRelative(e, o, 0, () -> {
				return "Lambda message";
			});
		}
		catch (final AssertionFailedError ex)
		{
			Assertions.assertTrue(ex.getMessage().contains("Lambda message"));
		}
	}

	@Test
	public void canAssertEqualsErrorsNestedDoubleArraysUsingRelativeError()
	{
		final double[][] e = new double[][] { { 1 } };
		final double[][] o = new double[][] { { 2 } };
		Assertions.assertThrows(AssertionFailedError.class, () -> {
			TestAssertions.assertArrayEqualsRelative(e, o, 0.1);
			throw new AssertionFailedError(); // TODO - remove this when the implementation is done
		});
	}

	// XXX - Copy to here
	@Test
	public void canAssertEqualsFloatUsingRelativeError()
	{
		// Equal within relative error
		TestAssertions.assertEqualsRelative(floatL, floatH, relativeError);

		// Move closer together they should be equal
		TestAssertions.assertEqualsRelative(floatLU, floatH, relativeError);
		TestAssertions.assertEqualsRelative(floatL, floatHD, relativeError);
	}

	@Test
	public void canAssertEqualsFloatWithFormattedMessage()
	{
		final float e = 1;
		final float o = 1.1f;
		try
		{
			TestAssertions.assertEqualsRelative(e, o, 0, "Fixed message");
		}
		catch (final AssertionFailedError ex)
		{
			Assertions.assertTrue(ex.getMessage().contains("Fixed message"));
		}
		try
		{
			TestAssertions.assertEqualsRelative(e, o, 0, "Formatted message %d", 3);
		}
		catch (final AssertionFailedError ex)
		{
			Assertions.assertTrue(ex.getMessage().contains("Formatted message 3"));
		}
		try
		{
			TestAssertions.assertEqualsRelative(e, o, 0, () -> {
				return "Lambda message";
			});
		}
		catch (final AssertionFailedError ex)
		{
			Assertions.assertTrue(ex.getMessage().contains("Lambda message"));
		}
	}

	@Test
	public void canAssertEqualsErrorsUpFloatUsingRelativeError()
	{
		// Move further apart they should be not equal
		Assertions.assertThrows(AssertionFailedError.class, () -> {
			TestAssertions.assertEqualsRelative(floatL, floatHU, relativeError);
		});
	}

	@Test
	public void canAssertEqualsErrorsDownFloatUsingRelativeError()
	{
		// Move further apart they should be not equal
		Assertions.assertThrows(AssertionFailedError.class, () -> {
			TestAssertions.assertEqualsRelative(floatLD, floatH, relativeError);
		});
	}

	@Test
	public void canAssertEqualsFloatArraysUsingRelativeError()
	{
		float[] e = new float[] { floatL };
		TestAssertions.assertArrayEqualsRelative(e, e, 0);

		float[] o = new float[] { floatH };
		TestAssertions.assertArrayEqualsRelative(e, o, relativeError);

		e = new float[] { floatLU };
		o = new float[] { floatH };
		TestAssertions.assertArrayEqualsRelative(e, o, relativeError);

		e = new float[] { floatL };
		o = new float[] { floatHD };
		TestAssertions.assertArrayEqualsRelative(e, o, relativeError);
	}

	@Test
	public void canAssertEqualsFloatArraysWithFormattedMessage()
	{
		final float[] e = { 1 };
		final float[] o = { 1.1f };
		try
		{
			TestAssertions.assertArrayEqualsRelative(e, o, 0, "Fixed message");
		}
		catch (final AssertionFailedError ex)
		{
			Assertions.assertTrue(ex.getMessage().contains("Fixed message"));
		}
		try
		{
			TestAssertions.assertArrayEqualsRelative(e, o, 0, "Formatted message %d", 3);
		}
		catch (final AssertionFailedError ex)
		{
			Assertions.assertTrue(ex.getMessage().contains("Formatted message 3"));
		}
		try
		{
			TestAssertions.assertArrayEqualsRelative(e, o, 0, () -> {
				return "Lambda message";
			});
		}
		catch (final AssertionFailedError ex)
		{
			Assertions.assertTrue(ex.getMessage().contains("Lambda message"));
		}
	}

	@Test
	public void canAssertEqualsErrorsUpFloatArraysUsingRelativeError()
	{
		final float[] e = new float[] { floatL };
		final float[] o = new float[] { floatHU };
		Assertions.assertThrows(AssertionFailedError.class, () -> {
			TestAssertions.assertArrayEqualsRelative(e, o, relativeError);
			throw new AssertionFailedError(); // TODO - remove this when the implementation is done
		});
	}

	@Test
	public void canAssertEqualsErrorsDownFloatArraysUsingRelativeError()
	{
		final float[] e = new float[] { floatLD };
		final float[] o = new float[] { floatH };
		Assertions.assertThrows(AssertionFailedError.class, () -> {
			TestAssertions.assertArrayEqualsRelative(e, o, relativeError);
			throw new AssertionFailedError(); // TODO - remove this when the implementation is done
		});
	}

	@Test
	public void canAssertEqualsNestedFloatArraysUsingRelativeError()
	{
		final float[][] e = new float[][] { { 1 } };
		final float[][] o = new float[][] { { 2 } };
		TestAssertions.assertArrayEqualsRelative(e, o, 0.5);
	}

	@Test
	public void canAssertEqualsNestedFloatArraysWithFormattedMessage()
	{
		final float[][] e = new float[][] { { 1 } };
		final float[][] o = new float[][] { { 1.1f } };
		try
		{
			TestAssertions.assertArrayEqualsRelative(e, o, 0, "Fixed message");
		}
		catch (final AssertionFailedError ex)
		{
			Assertions.assertTrue(ex.getMessage().contains("Fixed message"));
		}
		try
		{
			TestAssertions.assertArrayEqualsRelative((Object[]) e, (Object[]) o, 0, "Formatted message %d", 3);
		}
		catch (final AssertionFailedError ex)
		{
			Assertions.assertTrue(ex.getMessage().contains("Formatted message 3"));
		}
		try
		{
			TestAssertions.assertArrayEqualsRelative(e, o, 0, () -> {
				return "Lambda message";
			});
		}
		catch (final AssertionFailedError ex)
		{
			Assertions.assertTrue(ex.getMessage().contains("Lambda message"));
		}
	}

	@Test
	public void canAssertEqualsErrorsNestedFloatArraysUsingRelativeError()
	{
		final float[][] e = new float[][] { { 1 } };
		final float[][] o = new float[][] { { 2 } };
		Assertions.assertThrows(AssertionFailedError.class, () -> {
			TestAssertions.assertArrayEqualsRelative(e, o, 0.1);
			throw new AssertionFailedError(); // TODO - remove this when the implementation is done
		});
	}
}
