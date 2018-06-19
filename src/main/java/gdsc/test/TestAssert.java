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
package gdsc.test;

import org.junit.Assert;
import org.junit.internal.ComparisonCriteria;

/**
 * Class providing additional assert functions for tests.
 */
public class TestAssert
{
	/**
	 * Asserts that two doubles are equal to within a positive relativeError.
	 * If they are not, an {@link AssertionError} is thrown with the given
	 * message. If the expected value is infinity then the relativeError value is
	 * ignored. NaNs are considered equal:
	 * <code>assertEquals(Double.NaN, Double.NaN, *)</code> passes
	 *
	 * @param expected
	 *            expected value
	 * @param actual
	 *            the value to check against <code>expected</code>
	 * @param relativeError
	 *            the maximum relative error between <code>expected</code> and
	 *            <code>actual</code> for which both numbers are still
	 *            considered equal.
	 */
	public static void assertEquals(double expected, double actual, double relativeError)
	{
		assertEquals(null, expected, actual, relativeError);
	}

	/**
	 * Asserts that two doubles are equal to within a positive relativeError.
	 * If they are not, an {@link AssertionError} is thrown with the given
	 * message. If the expected value is infinity then the relativeError value is
	 * ignored. NaNs are considered equal:
	 * <code>assertEquals(Double.NaN, Double.NaN, *)</code> passes
	 *
	 * @param message
	 *            the identifying message for the {@link AssertionError} (<code>null</code>
	 *            okay)
	 * @param expected
	 *            expected value
	 * @param actual
	 *            the value to check against <code>expected</code>
	 * @param relativeError
	 *            the maximum relative error between <code>expected</code> and
	 *            <code>actual</code> for which both numbers are still
	 *            considered equal.
	 */
	public static void assertEquals(String message, double expected, double actual, double relativeError)
	{
		final double difference = max(Math.abs(expected), Math.abs(actual)) * relativeError;
		Assert.assertEquals(message, expected, actual, difference);
	}

	/**
	 * Get the maximum
	 *
	 * @param a
	 *            the first number
	 * @param b
	 *            the second number
	 * @return the maximum
	 */
	private static double max(double a, double b)
	{
		return (a >= b) ? a : b;
	}

	/**
	 * Asserts that two floats are equal to within a positive relativeError.
	 * If they are not, an {@link AssertionError} is thrown with the given
	 * message. If the expected value is infinity then the relativeError value is
	 * ignored. NaNs are considered equal:
	 * <code>assertEquals(Float.NaN, Float.NaN, *)</code> passes
	 *
	 * @param expected
	 *            expected value
	 * @param actual
	 *            the value to check against <code>expected</code>
	 * @param relativeError
	 *            the maximum relative error between <code>expected</code> and
	 *            <code>actual</code> for which both numbers are still
	 *            considered equal.
	 */
	public static void assertEquals(float expected, float actual, double relativeError)
	{
		assertEquals(null, expected, actual, relativeError);
	}

	/**
	 * Asserts that two floats are equal to within a positive relativeError.
	 * If they are not, an {@link AssertionError} is thrown with the given
	 * message. If the expected value is infinity then the relativeError value is
	 * ignored. NaNs are considered equal:
	 * <code>assertEquals(Float.NaN, Float.NaN, *)</code> passes
	 *
	 * @param message
	 *            the identifying message for the {@link AssertionError} (<code>null</code>
	 *            okay)
	 * @param expected
	 *            expected value
	 * @param actual
	 *            the value to check against <code>expected</code>
	 * @param relativeError
	 *            the maximum relative error between <code>expected</code> and
	 *            <code>actual</code> for which both numbers are still
	 *            considered equal.
	 */
	public static void assertEquals(String message, float expected, float actual, double relativeError)
	{
		final float difference = (float) (max(Math.abs(expected), Math.abs(actual)) * relativeError);
		Assert.assertEquals(message, expected, actual, difference);
	}

	/**
	 * Get the maximum
	 *
	 * @param a
	 *            the first number
	 * @param b
	 *            the second number
	 * @return the maximum
	 */
	private static float max(float a, float b)
	{
		return (a >= b) ? a : b;
	}

	/**
	 * A class for comparing double arrays using a relative threshold.
	 * <p>
	 * Based on org.junit.internal.InexactComparisonCriteria
	 */
	public static class DoubleRelativeComparisonCriteria extends ComparisonCriteria
	{
		public double delta;

		public DoubleRelativeComparisonCriteria(double delta)
		{
			this.delta = delta;
		}

		@Override
		protected void assertElementsEqual(Object expected, Object actual)
		{
			assertEquals(null, (Double) expected, (Double) actual, delta);
		}
	}

	/**
	 * A class for comparing float arrays using a relative threshold.
	 * <p>
	 * Based on org.junit.internal.InexactComparisonCriteria
	 */
	public static class FloatRelativeComparisonCriteria extends ComparisonCriteria
	{
		public double delta;

		public FloatRelativeComparisonCriteria(double delta)
		{
			this.delta = delta;
		}

		@Override
		protected void assertElementsEqual(Object expected, Object actual)
		{
			assertEquals(null, (Float) expected, (Float) actual, delta);
		}
	}

	/**
	 * Asserts that two double arrays are equal. If they are not, an
	 * {@link AssertionError} is thrown with the given message.
	 *
	 * @param expecteds
	 *            double array with expected values.
	 * @param actuals
	 *            double array with actual values
	 * @param relativeError
	 *            the relative error
	 */
	public static void assertArrayEquals(double[] expecteds, double[] actuals, double relativeError)
	{
		assertArrayEquals(null, expecteds, actuals, relativeError);
	}

	/**
	 * Asserts that two double arrays are equal. If they are not, an
	 * {@link AssertionError} is thrown with the given message.
	 *
	 * @param message
	 *            the identifying message for the {@link AssertionError} (<code>null</code>
	 *            okay)
	 * @param expecteds
	 *            double array with expected values.
	 * @param actuals
	 *            double array with actual values
	 * @param relativeError
	 *            the maximum relativeError between <code>expecteds[i]</code> and
	 *            <code>actuals[i]</code> for which both numbers are still
	 *            considered equal.
	 */
	public static void assertArrayEquals(String message, double[] expecteds, double[] actuals, double relativeError)
	{
		new DoubleRelativeComparisonCriteria(relativeError).arrayEquals(message, expecteds, actuals);
	}

	/**
	 * Asserts that two double arrays are equal. If they are not, an
	 * {@link AssertionError} is thrown with the given message.
	 * <p>
	 * This supports nested arrays, e.g. double[][].
	 *
	 * @param expecteds
	 *            double array with expected values.
	 * @param actuals
	 *            double array with actual values
	 * @param relativeError
	 *            the maximum relativeError between <code>expecteds[i]</code> and
	 *            <code>actuals[i]</code> for which both numbers are still
	 *            considered equal.
	 */
	public static void assertDoubleArrayEquals(Object expecteds, Object actuals, double relativeError)
	{
		assertDoubleArrayEquals(null, expecteds, actuals, relativeError);
	}

	/**
	 * Asserts that two double arrays are equal. If they are not, an
	 * {@link AssertionError} is thrown with the given message.
	 * <p>
	 * This supports nested arrays, e.g. double[][].
	 *
	 * @param message
	 *            the identifying message for the {@link AssertionError} (<code>null</code>
	 *            okay)
	 * @param expecteds
	 *            double array with expected values.
	 * @param actuals
	 *            double array with actual values
	 * @param relativeError
	 *            the maximum relativeError between <code>expecteds[i]</code> and
	 *            <code>actuals[i]</code> for which both numbers are still
	 *            considered equal.
	 */
	public static void assertDoubleArrayEquals(String message, Object expecteds, Object actuals, double relativeError)
	{
		new DoubleRelativeComparisonCriteria(relativeError).arrayEquals(message, expecteds, actuals);
	}

	/**
	 * Asserts that two float arrays are equal. If they are not, an
	 * {@link AssertionError} is thrown with the given message.
	 *
	 * @param expecteds
	 *            float array with expected values.
	 * @param actuals
	 *            float array with actual values
	 * @param relativeError
	 *            the maximum relativeError between <code>expecteds[i]</code> and
	 *            <code>actuals[i]</code> for which both numbers are still
	 *            considered equal.
	 */
	public static void assertArrayEquals(float[] expecteds, float[] actuals, double relativeError)
	{
		assertArrayEquals(null, expecteds, actuals, relativeError);
	}

	/**
	 * Asserts that two float arrays are equal. If they are not, an
	 * {@link AssertionError} is thrown with the given message.
	 *
	 * @param message
	 *            the identifying message for the {@link AssertionError} (<code>null</code>
	 *            okay)
	 * @param expecteds
	 *            float array with expected values.
	 * @param actuals
	 *            float array with actual values
	 * @param relativeError
	 *            the maximum relativeError between <code>expecteds[i]</code> and
	 *            <code>actuals[i]</code> for which both numbers are still
	 *            considered equal.
	 */
	public static void assertArrayEquals(String message, float[] expecteds, float[] actuals, double relativeError)
	{
		new FloatRelativeComparisonCriteria(relativeError).arrayEquals(message, expecteds, actuals);
	}

	/**
	 * Asserts that two float arrays are equal. If they are not, an
	 * {@link AssertionError} is thrown with the given message.
	 * <p>
	 * This supports nested arrays, e.g. float[][].
	 *
	 * @param expecteds
	 *            float array with expected values.
	 * @param actuals
	 *            float array with actual values
	 * @param relativeError
	 *            the maximum relativeError between <code>expecteds[i]</code> and
	 *            <code>actuals[i]</code> for which both numbers are still
	 *            considered equal.
	 */
	public static void assertFloatArrayEquals(Object expecteds, Object actuals, double relativeError)
	{
		assertFloatArrayEquals(null, expecteds, actuals, relativeError);
	}

	/**
	 * Asserts that two float arrays are equal. If they are not, an
	 * {@link AssertionError} is thrown with the given message.
	 * <p>
	 * This supports nested arrays, e.g. float[][].
	 *
	 * @param message
	 *            the identifying message for the {@link AssertionError} (<code>null</code>
	 *            okay)
	 * @param expecteds
	 *            float array with expected values.
	 * @param actuals
	 *            float array with actual values
	 * @param relativeError
	 *            the maximum relativeError between <code>expecteds[i]</code> and
	 *            <code>actuals[i]</code> for which both numbers are still
	 *            considered equal.
	 */
	public static void assertFloatArrayEquals(String message, Object expecteds, Object actuals, double relativeError)
	{
		new FloatRelativeComparisonCriteria(relativeError).arrayEquals(message, expecteds, actuals);
	}

	/**
	 * Assert that the condition is true. If not an
	 * {@link AssertionError} is thrown with the given formatted message.
	 *
	 * @param result
	 *            the result
	 * @param format
	 *            the format
	 * @param args
	 *            the arguments
	 */
	public static void assertTrue(boolean result, String format, Object... args)
	{
		if (result)
			return;
		Assert.fail(String.format(format, args));
	}

	/**
	 * Assert that the condition is false. If not an
	 * {@link AssertionError} is thrown with the given formatted message.
	 *
	 * @param result
	 *            the result
	 * @param format
	 *            the format
	 * @param args
	 *            the arguments
	 */
	public static void assertFalse(boolean result, String format, Object... args)
	{
		if (!result)
			return;
		Assert.fail(String.format(format, args));
	}
}
