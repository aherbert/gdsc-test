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

import org.junit.Assert;
import org.junit.Test;

import uk.ac.sussex.gdsc.test.TestLog;
import uk.ac.sussex.gdsc.test.junit4.TestAssert;

@SuppressWarnings("javadoc")
public class TestAssertTest
{
	@Test
	public void canAssertWithFormattedMessage()
	{
		try
		{
			TestAssert.assertLongEquals(0, 1, "[%d] == %.2f", 2, 3.5);
		}
		catch (final AssertionError e)
		{
			final String msg = e.getMessage();
			TestLog.info(msg);
			Assert.assertTrue("Unexpected message", msg.startsWith("[2] == 3.50"));
		}
	}

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
	public void canAssertEqualsAndNotEqualsDoubleUsingRelativeError()
	{
		// Equal within relative error
		TestAssert.assertEqualsRelative(doubleL, doubleH, relativeError);

		// Move closer together they should be equal
		TestAssert.assertEqualsRelative(doubleLU, doubleH, relativeError);
		TestAssert.assertEqualsRelative(doubleL, doubleHD, relativeError);

		// Move further apart they should be not equal
		TestAssert.assertNotEqualsRelative(doubleL, doubleHU, relativeError);
		TestAssert.assertNotEqualsRelative(doubleLD, doubleH, relativeError);
	}

	@Test(expected = AssertionError.class)
	public void canAssertEqualsErrorsUpDoubleUsingRelativeError()
	{
		// Move further apart they should be not equal
		TestAssert.assertEqualsRelative(doubleL, doubleHU, relativeError);
	}

	@Test(expected = AssertionError.class)
	public void canAssertEqualsErrorsDownDoubleUsingRelativeError()
	{
		// Move further apart they should be not equal
		TestAssert.assertEqualsRelative(doubleLD, doubleH, relativeError);
	}

	@Test(expected = AssertionError.class)
	public void canAssertNotEqualsErrorsDoubleUsingRelativeError()
	{
		// Equal within relative error
		TestAssert.assertNotEqualsRelative(doubleL, doubleH, relativeError);
	}

	@Test(expected = AssertionError.class)
	public void canAssertNotEqualsErrorsUpDoubleUsingRelativeError()
	{
		// Move closer together they should be equal
		TestAssert.assertNotEqualsRelative(doubleLU, doubleH, relativeError);
	}

	@Test(expected = AssertionError.class)
	public void canAssertNotEqualsErrorsDownDoubleUsingRelativeError()
	{
		// Move closer together they should be equal
		TestAssert.assertNotEqualsRelative(doubleL, doubleHD, relativeError);
	}

	@Test
	public void canAssertEqualsDoubleArraysUsingRelativeError()
	{
		double[] e = new double[] { doubleL };
		TestAssert.assertArrayEqualsRelative(e, e, 0);

		double[] o = new double[] { doubleH };
		TestAssert.assertArrayEqualsRelative(e, o, relativeError);

		e = new double[] { doubleLU };
		o = new double[] { doubleH };
		TestAssert.assertArrayEqualsRelative(e, o, relativeError);

		e = new double[] { doubleL };
		o = new double[] { doubleHD };
		TestAssert.assertArrayEqualsRelative(e, o, relativeError);
	}

	@Test(expected = AssertionError.class)
	public void canAssertEqualsErrorsUpDoubleArraysUsingRelativeError()
	{
		final double[] e = new double[] { doubleL };
		final double[] o = new double[] { doubleHU };
		TestAssert.assertArrayEqualsRelative(e, o, relativeError);
	}

	@Test(expected = AssertionError.class)
	public void canAssertEqualsErrorsDownDoubleArraysUsingRelativeError()
	{
		final double[] e = new double[] { doubleLD };
		final double[] o = new double[] { doubleH };
		TestAssert.assertArrayEqualsRelative(e, o, relativeError);
	}

	@Test
	public void canAssertEqualsObjectDoubleArraysUsingRelativeError()
	{
		final double[] e = new double[] { 1 };
		final double[] o = new double[] { 2 };
		TestAssert.assertDoubleArrayEqualsRelative(e, o, 0.5);
	}

	@Test(expected = AssertionError.class)
	public void canAssertEqualsErrorsObjectDoubleArraysUsingRelativeError()
	{
		final double[] e = new double[] { 1 };
		final double[] o = new double[] { 2 };
		TestAssert.assertDoubleArrayEqualsRelative(e, o, 0.1);
	}

	@Test
	public void canAssertEqualsObjectDoubleDoubleArraysUsingRelativeError()
	{
		final double[][] e = new double[][] { { 1 } };
		final double[][] o = new double[][] { { 2 } };
		TestAssert.assertDoubleArrayEqualsRelative(e, o, 0.5);
	}

	@Test(expected = AssertionError.class)
	public void canAssertEqualsErrorsObjectDoubleDoubleArraysUsingRelativeError()
	{
		final double[][] e = new double[][] { { 1 } };
		final double[][] o = new double[][] { { 2 } };
		TestAssert.assertDoubleArrayEqualsRelative(e, o, 0.1);
	}

	// XXX - Copy to here

	@Test
	public void canAssertEqualsAndNotEqualsFloatUsingRelativeError()
	{
		// Equal within relative error
		TestAssert.assertEqualsRelative(floatL, floatH, relativeError);

		// Move closer together they should be equal
		TestAssert.assertEqualsRelative(floatLU, floatH, relativeError);
		TestAssert.assertEqualsRelative(floatL, floatHD, relativeError);

		// Move further apart they should be not equal
		TestAssert.assertNotEqualsRelative(floatL, floatHU, relativeError);
		TestAssert.assertNotEqualsRelative(floatLD, floatH, relativeError);
	}

	@Test(expected = AssertionError.class)
	public void canAssertEqualsErrorsUpFloatUsingRelativeError()
	{
		// Move further apart they should be not equal
		TestAssert.assertEqualsRelative(floatL, floatHU, relativeError);
	}

	@Test(expected = AssertionError.class)
	public void canAssertEqualsErrorsDownFloatUsingRelativeError()
	{
		// Move further apart they should be not equal
		TestAssert.assertEqualsRelative(floatLD, floatH, relativeError);
	}

	@Test(expected = AssertionError.class)
	public void canAssertNotEqualsErrorsFloatUsingRelativeError()
	{
		// Equal within relative error
		TestAssert.assertNotEqualsRelative(floatL, floatH, relativeError);
	}

	@Test(expected = AssertionError.class)
	public void canAssertNotEqualsErrorsUpFloatUsingRelativeError()
	{
		// Move closer together they should be equal
		TestAssert.assertNotEqualsRelative(floatLU, floatH, relativeError);
	}

	@Test(expected = AssertionError.class)
	public void canAssertNotEqualsErrorsDownFloatUsingRelativeError()
	{
		// Move closer together they should be equal
		TestAssert.assertNotEqualsRelative(floatL, floatHD, relativeError);
	}

	@Test
	public void canAssertEqualsFloatArraysUsingRelativeError()
	{
		float[] e = new float[] { floatL };
		TestAssert.assertArrayEqualsRelative(e, e, 0);

		float[] o = new float[] { floatH };
		TestAssert.assertArrayEqualsRelative(e, o, relativeError);

		e = new float[] { floatLU };
		o = new float[] { floatH };
		TestAssert.assertArrayEqualsRelative(e, o, relativeError);

		e = new float[] { floatL };
		o = new float[] { floatHD };
		TestAssert.assertArrayEqualsRelative(e, o, relativeError);
	}

	@Test(expected = AssertionError.class)
	public void canAssertEqualsErrorsUpFloatArraysUsingRelativeError()
	{
		final float[] e = new float[] { floatL };
		final float[] o = new float[] { floatHU };
		TestAssert.assertArrayEqualsRelative(e, o, relativeError);
	}

	@Test(expected = AssertionError.class)
	public void canAssertEqualsErrorsDownFloatArraysUsingRelativeError()
	{
		final float[] e = new float[] { floatLD };
		final float[] o = new float[] { floatH };
		TestAssert.assertArrayEqualsRelative(e, o, relativeError);
	}

	@Test
	public void canAssertEqualsObjectFloatArraysUsingRelativeError()
	{
		final float[] e = new float[] { 1 };
		final float[] o = new float[] { 2 };
		TestAssert.assertFloatArrayEqualsRelative(e, o, 0.5);
	}

	@Test(expected = AssertionError.class)
	public void canAssertEqualsErrorsObjectFloatArraysUsingRelativeError()
	{
		final float[] e = new float[] { 1 };
		final float[] o = new float[] { 2 };
		TestAssert.assertFloatArrayEqualsRelative(e, o, 0.1);
	}

	@Test
	public void canAssertEqualsObjectFloatFloatArraysUsingRelativeError()
	{
		final float[][] e = new float[][] { { 1 } };
		final float[][] o = new float[][] { { 2 } };
		TestAssert.assertFloatArrayEqualsRelative(e, o, 0.5);
	}

	@Test(expected = AssertionError.class)
	public void canAssertEqualsErrorsObjectFloatFloatArraysUsingRelativeError()
	{
		final float[][] e = new float[][] { { 1 } };
		final float[][] o = new float[][] { { 2 } };
		TestAssert.assertFloatArrayEqualsRelative(e, o, 0.1);
	}
}
