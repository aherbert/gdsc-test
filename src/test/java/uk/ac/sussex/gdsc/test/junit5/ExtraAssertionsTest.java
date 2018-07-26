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

@SuppressWarnings("javadoc")
public class ExtraAssertionsTest
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
	public void canAssertEqualsRelativeDouble()
	{
		// Equal within relative error
		ExtraAssertions.assertEqualsRelative(doubleL, doubleH, relativeError);

		// Move closer together they should be equal
		ExtraAssertions.assertEqualsRelative(doubleLU, doubleH, relativeError);
		ExtraAssertions.assertEqualsRelative(doubleL, doubleHD, relativeError);
	}

	@Test
	public void canAssertEqualsRelativeDoubleThrowsWithFormattedMessage()
	{
		final double e = 1;
		final double o = 2;
		try
		{
			ExtraAssertions.assertEqualsRelative(e, o, Double.MIN_VALUE, "Fixed message");
		}
		catch (final AssertionFailedError ex)
		{
			Assertions.assertTrue(ex.getMessage().contains("Fixed message"));
		}
		try
		{
			ExtraAssertions.assertEqualsRelative(e, o, Double.MIN_VALUE, "Formatted message %d", 3);
		}
		catch (final AssertionFailedError ex)
		{
			Assertions.assertTrue(ex.getMessage().contains("Formatted message 3"));
		}
		try
		{
			ExtraAssertions.assertEqualsRelative(e, o, Double.MIN_VALUE, () -> {
				return "Lambda message";
			});
		}
		catch (final AssertionFailedError ex)
		{
			Assertions.assertTrue(ex.getMessage().contains("Lambda message"));
		}
	}

	@Test
	public void assertEqualsRelativeDoubleThrowsUsingBadThreshold()
	{
		final double e = 1;
		Assertions.assertThrows(AssertionFailedError.class, () -> {
			ExtraAssertions.assertEqualsRelative(e, e, 0);
		}, "0");
		Assertions.assertThrows(AssertionFailedError.class, () -> {
			ExtraAssertions.assertEqualsRelative(e, e, 2);
		}, "2");
		Assertions.assertThrows(AssertionFailedError.class, () -> {
			ExtraAssertions.assertEqualsRelative(e, e, Double.POSITIVE_INFINITY);
		}, "+Inf");
		Assertions.assertThrows(AssertionFailedError.class, () -> {
			ExtraAssertions.assertEqualsRelative(e, e, Double.NaN);
		}, "NaN");
		Assertions.assertThrows(AssertionFailedError.class, () -> {
			ExtraAssertions.assertEqualsRelative(e, e, -1);
		}, "-1");
	}

	@Test
	public void canAssertEqualsRelativeDoubleErrorsUp()
	{
		// Move further apart they should be not equal
		Assertions.assertThrows(AssertionFailedError.class, () -> {
			ExtraAssertions.assertEqualsRelative(doubleL, doubleHU, relativeError);
		});
	}

	@Test
	public void canAssertEqualsRelativeDoubleErrorsDown()
	{
		// Move further apart they should be not equal
		Assertions.assertThrows(AssertionFailedError.class, () -> {
			ExtraAssertions.assertEqualsRelative(doubleLD, doubleH, relativeError);
		});
	}

	@Test
	public void canAssertArrayEqualsRelativeDouble()
	{
		double[] e = new double[] { doubleL };
		ExtraAssertions.assertArrayEqualsRelative(e, e, Double.MIN_VALUE);

		double[] o = new double[] { doubleH };
		ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError);

		e = new double[] { doubleLU };
		o = new double[] { doubleH };
		ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError);

		e = new double[] { doubleL };
		o = new double[] { doubleHD };
		ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError);
	}

	@Test
	public void canAssertArrayEqualsRelativeDoubleThrowsWithFormattedMessage()
	{
		final double[] e = { 1 };
		final double[] o = { 2 };
		try
		{
			ExtraAssertions.assertArrayEqualsRelative(e, o, Double.MIN_VALUE, "Fixed message");
		}
		catch (final AssertionFailedError ex)
		{
			Assertions.assertTrue(ex.getMessage().contains("Fixed message"));
		}
		try
		{
			ExtraAssertions.assertArrayEqualsRelative(e, o, Double.MIN_VALUE, "Formatted message %d", 3);
		}
		catch (final AssertionFailedError ex)
		{
			Assertions.assertTrue(ex.getMessage().contains("Formatted message 3"));
		}
		try
		{
			ExtraAssertions.assertArrayEqualsRelative(e, o, Double.MIN_VALUE, () -> {
				return "Lambda message";
			});
		}
		catch (final AssertionFailedError ex)
		{
			Assertions.assertTrue(ex.getMessage().contains("Lambda message"));
		}
	}

	@Test
	public void canAssertArrayEqualsRelativeDoubleErrorsUp()
	{
		final double[] e = new double[] { doubleL };
		final double[] o = new double[] { doubleHU };
		Assertions.assertThrows(AssertionFailedError.class, () -> {
			ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError);
		});
	}

	@Test
	public void canAssertArrayEqualsRelativeDoubleErrorsDown()
	{
		final double[] e = new double[] { doubleLD };
		final double[] o = new double[] { doubleH };
		Assertions.assertThrows(AssertionFailedError.class, () -> {
			ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError);
		});
	}

	@Test
	public void canAssertArrayEqualsRelativeNestedDouble()
	{
		final double[][] e = new double[][] { { 1 } };
		final double[][] o = new double[][] { { 2 } };
		ExtraAssertions.assertArrayEqualsRelative(e, o, 0.5);
	}

	@Test
	public void canAssertArrayEqualsRelativeNestedDoubleThrows()
	{
		final double[][] e = new double[][] { { 1 } };
		final double[][] o = new double[][] { { 2 } };
		Assertions.assertThrows(AssertionFailedError.class, () -> {
			ExtraAssertions.assertArrayEqualsRelative(e, o, 0.1);
		});
		Assertions.assertThrows(AssertionFailedError.class, () -> {
			ExtraAssertions.assertArrayEqualsRelative(e, o, 0);
		});
	}

	@Test
	public void canAssertArrayEqualsRelativeNestedDoubleThrowsWithFormattedMessage()
	{
		final double[][] e = new double[][] { { 1 } };
		final double[][] o = new double[][] { { 2 } };
		try
		{
			ExtraAssertions.assertArrayEqualsRelative(e, o, Double.MIN_VALUE, "Fixed message");
		}
		catch (final AssertionFailedError ex)
		{
			Assertions.assertTrue(ex.getMessage().contains("Fixed message"));
		}
		try
		{
			ExtraAssertions.assertArrayEqualsRelative(e, o, Double.MIN_VALUE,
					"Formatted message %d", 3);
		}
		catch (final AssertionFailedError ex)
		{
			Assertions.assertTrue(ex.getMessage().contains("Formatted message 3"));
		}
		try
		{
			ExtraAssertions.assertArrayEqualsRelative(e, o, Double.MIN_VALUE, () -> {
				return "Lambda message";
			});
		}
		catch (final AssertionFailedError ex)
		{
			Assertions.assertTrue(ex.getMessage().contains("Lambda message"));
		}
	}

	@Test
	public void canAssertArrayEqualsNestedDoubleArrays()
	{
		final double low = 1;
		final double high = 2;
		final double delta = high - low;
		final double[][] e = new double[][] { { low } };
		final double[][] o = new double[][] { { high } };
		ExtraAssertions.assertArrayEquals(e, o, delta);
	}

	@Test
	public void canAssertArrayEqualsNestedDoubleArraysThrows()
	{
		final double low = 1;
		final double high = 2;
		final double delta = high - low;
		final double[][] e = new double[][] { { low } };
		final double[][] o = new double[][] { { high } };
		Assertions.assertThrows(AssertionFailedError.class, () -> {
			ExtraAssertions.assertArrayEquals(e, o, Math.nextDown(delta));
		});
		Assertions.assertThrows(AssertionFailedError.class, () -> {
			ExtraAssertions.assertArrayEquals(e, o, new Integer(4));
		});
		Assertions.assertThrows(AssertionFailedError.class, () -> {
			ExtraAssertions.assertArrayEquals(e, o, new Double(0));
		});
	}

	@Test
	public void canAssertEqualsNestedDoubleArraysThrowsWithFormattedMessage()
	{
		final double[][] e = new double[][] { { 1 } };
		final double[][] o = new double[][] { { 2 } };
		try
		{
			ExtraAssertions.assertArrayEquals(e, o, Double.MIN_VALUE, "Fixed message");
		}
		catch (final AssertionFailedError ex)
		{
			Assertions.assertTrue(ex.getMessage().contains("Fixed message"));
		}
		try
		{
			ExtraAssertions.assertArrayEquals(e, o, Double.MIN_VALUE,
					"Formatted message %d", 3);
		}
		catch (final AssertionFailedError ex)
		{
			Assertions.assertTrue(ex.getMessage().contains("Formatted message 3"));
		}
		try
		{
			ExtraAssertions.assertArrayEquals(e, o, Double.MIN_VALUE, () -> {
				return "Lambda message";
			});
		}
		catch (final AssertionFailedError ex)
		{
			Assertions.assertTrue(ex.getMessage().contains("Lambda message"));
		}
	}

	// XXX - Copy to here
	@Test
	public void canAssertEqualsRelativeFloat()
	{
		// Equal within relative error
		ExtraAssertions.assertEqualsRelative(floatL, floatH, relativeError);

		// Move closer together they should be equal
		ExtraAssertions.assertEqualsRelative(floatLU, floatH, relativeError);
		ExtraAssertions.assertEqualsRelative(floatL, floatHD, relativeError);
	}

	@Test
	public void canAssertEqualsRelativeFloatThrowsWithFormattedMessage()
	{
		final float e = 1;
		final float o = 2;
		try
		{
			ExtraAssertions.assertEqualsRelative(e, o, Float.MIN_VALUE, "Fixed message");
		}
		catch (final AssertionFailedError ex)
		{
			Assertions.assertTrue(ex.getMessage().contains("Fixed message"));
		}
		try
		{
			ExtraAssertions.assertEqualsRelative(e, o, Float.MIN_VALUE, "Formatted message %d", 3);
		}
		catch (final AssertionFailedError ex)
		{
			Assertions.assertTrue(ex.getMessage().contains("Formatted message 3"));
		}
		try
		{
			ExtraAssertions.assertEqualsRelative(e, o, Float.MIN_VALUE, () -> {
				return "Lambda message";
			});
		}
		catch (final AssertionFailedError ex)
		{
			Assertions.assertTrue(ex.getMessage().contains("Lambda message"));
		}
	}

	@Test
	public void assertEqualsRelativeFloatThrowsUsingBadThreshold()
	{
		final float e = 1;
		Assertions.assertThrows(AssertionFailedError.class, () -> {
			ExtraAssertions.assertEqualsRelative(e, e, 0);
		}, "0");
		Assertions.assertThrows(AssertionFailedError.class, () -> {
			ExtraAssertions.assertEqualsRelative(e, e, 2);
		}, "2");
		Assertions.assertThrows(AssertionFailedError.class, () -> {
			ExtraAssertions.assertEqualsRelative(e, e, Float.POSITIVE_INFINITY);
		}, "+Inf");
		Assertions.assertThrows(AssertionFailedError.class, () -> {
			ExtraAssertions.assertEqualsRelative(e, e, Float.NaN);
		}, "NaN");
		Assertions.assertThrows(AssertionFailedError.class, () -> {
			ExtraAssertions.assertEqualsRelative(e, e, -1);
		}, "-1");
	}

	@Test
	public void canAssertEqualsRelativeFloatErrorsUp()
	{
		// Move further apart they should be not equal
		Assertions.assertThrows(AssertionFailedError.class, () -> {
			ExtraAssertions.assertEqualsRelative(floatL, floatHU, relativeError);
		});
	}

	@Test
	public void canAssertEqualsRelativeFloatErrorsDown()
	{
		// Move further apart they should be not equal
		Assertions.assertThrows(AssertionFailedError.class, () -> {
			ExtraAssertions.assertEqualsRelative(floatLD, floatH, relativeError);
		});
	}

	@Test
	public void canAssertArrayEqualsRelativeFloat()
	{
		float[] e = new float[] { floatL };
		ExtraAssertions.assertArrayEqualsRelative(e, e, Float.MIN_VALUE);

		float[] o = new float[] { floatH };
		ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError);

		e = new float[] { floatLU };
		o = new float[] { floatH };
		ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError);

		e = new float[] { floatL };
		o = new float[] { floatHD };
		ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError);
	}

	@Test
	public void canAssertArrayEqualsRelativeFloatThrowsWithFormattedMessage()
	{
		final float[] e = { 1 };
		final float[] o = { 2 };
		try
		{
			ExtraAssertions.assertArrayEqualsRelative(e, o, Float.MIN_VALUE, "Fixed message");
		}
		catch (final AssertionFailedError ex)
		{
			Assertions.assertTrue(ex.getMessage().contains("Fixed message"));
		}
		try
		{
			ExtraAssertions.assertArrayEqualsRelative(e, o, Float.MIN_VALUE, "Formatted message %d", 3);
		}
		catch (final AssertionFailedError ex)
		{
			Assertions.assertTrue(ex.getMessage().contains("Formatted message 3"));
		}
		try
		{
			ExtraAssertions.assertArrayEqualsRelative(e, o, Float.MIN_VALUE, () -> {
				return "Lambda message";
			});
		}
		catch (final AssertionFailedError ex)
		{
			Assertions.assertTrue(ex.getMessage().contains("Lambda message"));
		}
	}

	@Test
	public void canAssertArrayEqualsRelativeFloatErrorsUp()
	{
		final float[] e = new float[] { floatL };
		final float[] o = new float[] { floatHU };
		Assertions.assertThrows(AssertionFailedError.class, () -> {
			ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError);
		});
	}

	@Test
	public void canAssertArrayEqualsRelativeFloatErrorsDown()
	{
		final float[] e = new float[] { floatLD };
		final float[] o = new float[] { floatH };
		Assertions.assertThrows(AssertionFailedError.class, () -> {
			ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError);
		});
	}

	@Test
	public void canAssertArrayEqualsRelativeNestedFloat()
	{
		final float[][] e = new float[][] { { 1 } };
		final float[][] o = new float[][] { { 2 } };
		ExtraAssertions.assertArrayEqualsRelative(e, o, 0.5);
	}

	@Test
	public void canAssertArrayEqualsRelativeNestedFloatThrows()
	{
		final float[][] e = new float[][] { { 1 } };
		final float[][] o = new float[][] { { 2 } };
		Assertions.assertThrows(AssertionFailedError.class, () -> {
			ExtraAssertions.assertArrayEqualsRelative(e, o, 0.1);
		});
		Assertions.assertThrows(AssertionFailedError.class, () -> {
			ExtraAssertions.assertArrayEqualsRelative(e, o, 0);
		});
	}

	@Test
	public void canAssertArrayEqualsRelativeNestedFloatThrowsWithFormattedMessage()
	{
		final float[][] e = new float[][] { { 1 } };
		final float[][] o = new float[][] { { 2 } };
		try
		{
			ExtraAssertions.assertArrayEqualsRelative(e, o, Float.MIN_VALUE, "Fixed message");
		}
		catch (final AssertionFailedError ex)
		{
			Assertions.assertTrue(ex.getMessage().contains("Fixed message"));
		}
		try
		{
			ExtraAssertions.assertArrayEqualsRelative(e, o, Float.MIN_VALUE,
					"Formatted message %d", 3);
		}
		catch (final AssertionFailedError ex)
		{
			Assertions.assertTrue(ex.getMessage().contains("Formatted message 3"));
		}
		try
		{
			ExtraAssertions.assertArrayEqualsRelative(e, o, Float.MIN_VALUE, () -> {
				return "Lambda message";
			});
		}
		catch (final AssertionFailedError ex)
		{
			Assertions.assertTrue(ex.getMessage().contains("Lambda message"));
		}
	}

	@Test
	public void canAssertArrayEqualsNestedFloatArrays()
	{
		final float low = 1;
		final float high = 2;
		final float delta = high - low;
		final float[][] e = new float[][] { { low } };
		final float[][] o = new float[][] { { high } };
		ExtraAssertions.assertArrayEquals(e, o, delta);
	}

	@Test
	public void canAssertArrayEqualsNestedFloatArraysThrows()
	{
		final float low = 1;
		final float high = 2;
		final float delta = high - low;
		final float[][] e = new float[][] { { low } };
		final float[][] o = new float[][] { { high } };
		Assertions.assertThrows(AssertionFailedError.class, () -> {
			ExtraAssertions.assertArrayEquals(e, o, Math.nextDown(delta));
		});
		Assertions.assertThrows(AssertionFailedError.class, () -> {
			ExtraAssertions.assertArrayEquals(e, o, new Integer(4));
		});
		Assertions.assertThrows(AssertionFailedError.class, () -> {
			ExtraAssertions.assertArrayEquals(e, o, new Float(0));
		});
	}

	@Test
	public void canAssertEqualsNestedFloatArraysThrowsWithFormattedMessage()
	{
		final float[][] e = new float[][] { { 1 } };
		final float[][] o = new float[][] { { 2 } };
		try
		{
			ExtraAssertions.assertArrayEquals(e, o, Float.MIN_VALUE, "Fixed message");
		}
		catch (final AssertionFailedError ex)
		{
			Assertions.assertTrue(ex.getMessage().contains("Fixed message"));
		}
		try
		{
			ExtraAssertions.assertArrayEquals(e, o, Float.MIN_VALUE,
					"Formatted message %d", 3);
		}
		catch (final AssertionFailedError ex)
		{
			Assertions.assertTrue(ex.getMessage().contains("Formatted message 3"));
		}
		try
		{
			ExtraAssertions.assertArrayEquals(e, o, Float.MIN_VALUE, () -> {
				return "Lambda message";
			});
		}
		catch (final AssertionFailedError ex)
		{
			Assertions.assertTrue(ex.getMessage().contains("Lambda message"));
		}
	}
}
