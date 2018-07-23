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

import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.internal.ComparisonCriteria;

/**
 * Adds additional helper assert functions to those provided by {@link org.junit.Assert}.
 * <p>
 * Assert functions are provided to compare float values using relative error, and assert methods to support formatted messages, e.g.
 * <pre>
 * &#64;Test
 * public void myTest() {
 *     TestAssert.assertEqualsRelative(999.9, 1000.0, 1e-2); // passes
 *     TestAssert.fail("Failed number %d", 42);
 * }
 * </pre>
 */
public class TestAssert
{
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
	 * Asserts that two doubles are equal to within a positive relativeError.
	 * If they are not, an {@link AssertionError} is thrown with no
	 * message. If the expected value is infinity then the relativeError value is
	 * ignored. NaNs are considered equal:
	 * <code>assertEqualsRelative(Double.NaN, Double.NaN, *)</code> passes
	 *
	 * @param expected
	 *            expected value
	 * @param actual
	 *            the value to check against <code>expected</code>
	 * @param relativeError
	 *            the maximum relative error between <code>expected</code> and
	 *            <code>actual</code> for which both numbers are still
	 *            considered equal.
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	public static void assertEqualsRelative(double expected, double actual, double relativeError) throws AssertionError
	{
		assertEqualsRelative(null, expected, actual, relativeError);
	}

	/**
	 * Asserts that two doubles are equal to within a positive relativeError.
	 * If they are not, an {@link AssertionError} is thrown with the formatted
	 * message. If the expected value is infinity then the relativeError value is
	 * ignored. NaNs are considered equal:
	 * <code>assertEqualsRelative(Double.NaN, Double.NaN, *)</code> passes
	 *
	 * @param expected
	 *            expected value
	 * @param actual
	 *            the value to check against <code>expected</code>
	 * @param relativeError
	 *            the maximum relative error between <code>expected</code> and
	 *            <code>actual</code> for which both numbers are still
	 *            considered equal.
	 * @param format
	 *            the message format
	 * @param args
	 *            the arguments
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	public static void assertEqualsRelative(double expected, double actual, double relativeError, String format,
			Object... args) throws AssertionError
	{
		try
		{
			assertEqualsRelative(null, expected, actual, relativeError);
		}
		catch (final AssertionError ex)
		{
			wrapAssertionError(ex, format, args);
		}
	}

	/**
	 * Asserts that two doubles are equal to within a positive relativeError.
	 * If they are not, an {@link AssertionError} is thrown with the given
	 * message. If the expected value is infinity then the relativeError value is
	 * ignored. NaNs are considered equal:
	 * <code>assertEqualsRelative(Double.NaN, Double.NaN, *)</code> passes
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
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	public static void assertEqualsRelative(String message, double expected, double actual, double relativeError)
			throws AssertionError
	{
		//final double difference = max(Math.abs(expected), Math.abs(actual)) * relativeError;
		//Assert.assertEquals(message, expected, actual, difference);

		final double max = max(Math.abs(expected), Math.abs(actual));
		try
		{
			Assert.assertEquals(message, expected, actual, max * relativeError);
		}
		catch (final AssertionError ex)
		{
			wrapAssertionErrorAppend(ex, " (error=%s)", Math.abs(expected - actual) / max);
		}
	}

	/**
	 * Asserts that two floats are equal to within a positive relativeError.
	 * If they are not, an {@link AssertionError} is thrown with no
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
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	public static void assertEqualsRelative(float expected, float actual, double relativeError) throws AssertionError
	{
		assertEqualsRelative(null, expected, actual, relativeError);
	}

	/**
	 * Asserts that two floats are equal to within a positive relativeError.
	 * If they are not, an {@link AssertionError} is thrown with the formatted
	 * message. If the expected value is infinity then the relativeError value is
	 * ignored. NaNs are considered equal:
	 * <code>assertEqualsRelative(Double.NaN, Double.NaN, *)</code> passes
	 *
	 * @param expected
	 *            expected value
	 * @param actual
	 *            the value to check against <code>expected</code>
	 * @param relativeError
	 *            the maximum relative error between <code>expected</code> and
	 *            <code>actual</code> for which both numbers are still
	 *            considered equal.
	 * @param format
	 *            the message format
	 * @param args
	 *            the arguments
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	public static void assertEqualsRelative(float expected, float actual, double relativeError, String format,
			Object... args) throws AssertionError
	{
		try
		{
			assertEqualsRelative(null, expected, actual, relativeError);
		}
		catch (final AssertionError ex)
		{
			wrapAssertionError(ex, format, args);
		}
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
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	public static void assertEqualsRelative(String message, float expected, float actual, double relativeError)
			throws AssertionError
	{
		//final float difference = (float) (max(Math.abs(expected), Math.abs(actual)) * relativeError);
		//Assert.assertEquals(message, expected, actual, difference);

		final float max = max(Math.abs(expected), Math.abs(actual));
		try
		{
			Assert.assertEquals(message, expected, actual, (float) (max * relativeError));
		}
		catch (final AssertionError ex)
		{
			wrapAssertionErrorAppend(ex, " (error=%s)", Math.abs(expected - actual) / max);
		}
	}

	/**
	 * Asserts that two doubles are <b>not</b> equal to within a positive relativeError.
	 * If they are, an {@link AssertionError} is thrown with no
	 * message. If the expected value is infinity then the relativeError value is
	 * ignored. NaNs are considered equal:
	 * <code>assertNotEquals(Double.NaN, Double.NaN, *)</code> fails
	 *
	 * @param expected
	 *            expected value
	 * @param actual
	 *            the value to check against <code>expected</code>
	 * @param relativeError
	 *            the maximum relative error between <code>expected</code> and
	 *            <code>actual</code> for which both numbers are still
	 *            considered equal.
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	public static void assertNotEqualsRelative(double expected, double actual, double relativeError)
			throws AssertionError
	{
		assertNotEqualsRelative(null, expected, actual, relativeError);
	}

	/**
	 * Asserts that two doubles are <b>not</b> equal to within a positive relativeError.
	 * If they are, an {@link AssertionError} is thrown with the formatted
	 * message. If the expected value is infinity then the relativeError value is
	 * ignored. NaNs are considered equal:
	 * <code>assertNotEquals(Double.NaN, Double.NaN, *)</code> fails
	 *
	 * @param expected
	 *            expected value
	 * @param actual
	 *            the value to check against <code>expected</code>
	 * @param relativeError
	 *            the maximum relative error between <code>expected</code> and
	 *            <code>actual</code> for which both numbers are still
	 *            considered equal.
	 * @param format
	 *            the message format
	 * @param args
	 *            the arguments
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	public static void assertNotEqualsRelative(double expected, double actual, double relativeError, String format,
			Object... args) throws AssertionError
	{
		try
		{
			assertNotEqualsRelative(null, expected, actual, relativeError);
		}
		catch (final AssertionError ex)
		{
			wrapAssertionError(ex, format, args);
		}
	}

	/**
	 * Asserts that two doubles are <b>not</b> equal to within a positive relativeError.
	 * If they are, an {@link AssertionError} is thrown with the given
	 * message. If the expected value is infinity then the relativeError value is
	 * ignored. NaNs are considered equal:
	 * <code>assertNotEquals(Double.NaN, Double.NaN, *)</code> fails
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
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	public static void assertNotEqualsRelative(String message, double expected, double actual, double relativeError)
			throws AssertionError
	{
		//final double difference = max(Math.abs(expected), Math.abs(actual)) * relativeError;
		//Assert.assertNotEquals(message, expected, actual, difference);

		final double max = max(Math.abs(expected), Math.abs(actual));
		try
		{
			Assert.assertNotEquals(message, expected, actual, max * relativeError);
		}
		catch (final AssertionError ex)
		{
			wrapAssertionErrorAppend(ex, " (error=%s)", Math.abs(expected - actual) / max);
		}
	}

	/**
	 * Asserts that two floats are <b>not</b> equal to within a positive relativeError.
	 * If they are, an {@link AssertionError} is thrown with no
	 * message. If the expected value is infinity then the relativeError value is
	 * ignored. NaNs are considered equal:
	 * <code>assertNotEquals(Float.NaN, Float.NaN, *)</code> fails
	 *
	 * @param expected
	 *            expected value
	 * @param actual
	 *            the value to check against <code>expected</code>
	 * @param relativeError
	 *            the maximum relative error between <code>expected</code> and
	 *            <code>actual</code> for which both numbers are still
	 *            considered equal.
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	public static void assertNotEqualsRelative(float expected, float actual, double relativeError) throws AssertionError
	{
		assertNotEqualsRelative(null, expected, actual, relativeError);
	}

	/**
	 * Asserts that two floats are <b>not</b> equal to within a positive relativeError.
	 * If they are, an {@link AssertionError} is thrown with the formatted
	 * message. If the expected value is infinity then the relativeError value is
	 * ignored. NaNs are considered equal:
	 * <code>assertNotEquals(Double.NaN, Double.NaN, *)</code> fails
	 *
	 * @param expected
	 *            expected value
	 * @param actual
	 *            the value to check against <code>expected</code>
	 * @param relativeError
	 *            the maximum relative error between <code>expected</code> and
	 *            <code>actual</code> for which both numbers are still
	 *            considered equal.
	 * @param format
	 *            the message format
	 * @param args
	 *            the arguments
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	public static void assertNotEqualsRelative(float expected, float actual, double relativeError, String format,
			Object... args) throws AssertionError
	{
		try
		{
			assertNotEqualsRelative(null, expected, actual, relativeError);
		}
		catch (final AssertionError ex)
		{
			wrapAssertionError(ex, format, args);
		}
	}

	/**
	 * Asserts that two floats are <b>not</b> equal to within a positive relativeError.
	 * If they are, an {@link AssertionError} is thrown with the given
	 * message. If the expected value is infinity then the relativeError value is
	 * ignored. NaNs are considered equal:
	 * <code>assertNotEquals(Float.NaN, Float.NaN, *)</code> fails
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
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	public static void assertNotEqualsRelative(String message, float expected, float actual, double relativeError)
			throws AssertionError
	{
		//final float difference = (float) (max(Math.abs(expected), Math.abs(actual)) * relativeError);
		//Assert.assertNotEquals(message, expected, actual, difference);

		final float max = max(Math.abs(expected), Math.abs(actual));
		try
		{
			Assert.assertNotEquals(message, expected, actual, (float) (max * relativeError));
		}
		catch (final AssertionError ex)
		{
			wrapAssertionErrorAppend(ex, " (error=%s)", Math.abs(expected - actual) / max);
		}
	}

	/**
	 * A class for comparing double arrays using a relative threshold.
	 * <p>
	 * Based on org.junit.internal.InexactComparisonCriteria
	 */
	public static class DoubleRelativeComparisonCriteria extends ComparisonCriteria
	{
		/** The relative delta. */
		public final double delta;

		/**
		 * Instantiates a new double relative comparison criteria.
		 *
		 * @param delta
		 *            the delta
		 */
		public DoubleRelativeComparisonCriteria(double delta)
		{
			this.delta = delta;
		}

		@Override
		protected void assertElementsEqual(Object expected, Object actual) throws AssertionError
		{
			assertEqualsRelative(null, (Double) expected, (Double) actual, delta);
		}
	}

	/**
	 * A class for comparing float arrays using a relative threshold.
	 * <p>
	 * Based on org.junit.internal.InexactComparisonCriteria
	 */
	public static class FloatRelativeComparisonCriteria extends ComparisonCriteria
	{
		/** The relative delta. */
		public final double delta;

		/**
		 * Instantiates a new float relative comparison criteria.
		 *
		 * @param delta
		 *            the delta
		 */
		public FloatRelativeComparisonCriteria(double delta)
		{
			this.delta = delta;
		}

		@Override
		protected void assertElementsEqual(Object expected, Object actual) throws AssertionError
		{
			assertEqualsRelative(null, (Float) expected, (Float) actual, delta);
		}
	}

	/**
	 * Asserts that two double arrays are equal. If they are not, an
	 * {@link AssertionError} is thrown with no message.
	 *
	 * @param expecteds
	 *            double array with expected values.
	 * @param actuals
	 *            double array with actual values
	 * @param relativeError
	 *            the relative error
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	public static void assertArrayEqualsRelative(double[] expecteds, double[] actuals, double relativeError)
			throws AssertionError
	{
		assertArrayEqualsRelative(null, expecteds, actuals, relativeError);
	}

	/**
	 * Asserts that two double arrays are equal. If they are not, an
	 * {@link AssertionError} is thrown with no message.
	 *
	 * @param expecteds
	 *            double array with expected values.
	 * @param actuals
	 *            double array with actual values
	 * @param relativeError
	 *            the relative error
	 * @param format
	 *            the message format
	 * @param args
	 *            the arguments
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	public static void assertArrayEqualsRelative(double[] expecteds, double[] actuals, double relativeError,
			String format, Object... args) throws AssertionError
	{
		try
		{
			assertArrayEqualsRelative(null, expecteds, actuals, relativeError);
		}
		catch (final AssertionError ex)
		{
			wrapAssertionError(ex, format, args);
		}
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
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	public static void assertArrayEqualsRelative(String message, double[] expecteds, double[] actuals,
			double relativeError) throws AssertionError
	{
		new DoubleRelativeComparisonCriteria(relativeError).arrayEquals(message, expecteds, actuals);
	}

	/**
	 * Asserts that two double arrays are equal. If they are not, an
	 * {@link AssertionError} is thrown with no message.
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
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	public static void assertDoubleArrayEqualsRelative(Object expecteds, Object actuals, double relativeError)
			throws AssertionError
	{
		assertDoubleArrayEqualsRelative(null, expecteds, actuals, relativeError);
	}

	/**
	 * Asserts that two double arrays are equal. If they are not, an
	 * {@link AssertionError} is thrown with no message.
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
	 * @param format
	 *            the message format
	 * @param args
	 *            the arguments
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	public static void assertDoubleArrayEqualsRelative(Object expecteds, Object actuals, double relativeError,
			String format, Object... args) throws AssertionError
	{
		try
		{
			assertDoubleArrayEqualsRelative(null, expecteds, actuals, relativeError);
		}
		catch (final AssertionError ex)
		{
			wrapAssertionError(ex, format, args);
		}
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
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	public static void assertDoubleArrayEqualsRelative(String message, Object expecteds, Object actuals,
			double relativeError) throws AssertionError
	{
		new DoubleRelativeComparisonCriteria(relativeError).arrayEquals(message, expecteds, actuals);
	}

	/**
	 * Asserts that two float arrays are equal. If they are not, an
	 * {@link AssertionError} is thrown with no message.
	 *
	 * @param expecteds
	 *            float array with expected values.
	 * @param actuals
	 *            float array with actual values
	 * @param relativeError
	 *            the relative error
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	public static void assertArrayEqualsRelative(float[] expecteds, float[] actuals, double relativeError)
			throws AssertionError
	{
		assertArrayEqualsRelative(null, expecteds, actuals, relativeError);
	}

	/**
	 * Asserts that two float arrays are equal. If they are not, an
	 * {@link AssertionError} is thrown with no message.
	 *
	 * @param expecteds
	 *            float array with expected values.
	 * @param actuals
	 *            float array with actual values
	 * @param relativeError
	 *            the relative error
	 * @param format
	 *            the message format
	 * @param args
	 *            the arguments
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	public static void assertArrayEqualsRelative(float[] expecteds, float[] actuals, double relativeError,
			String format, Object... args) throws AssertionError
	{
		try
		{
			assertArrayEqualsRelative(null, expecteds, actuals, relativeError);
		}
		catch (final AssertionError ex)
		{
			wrapAssertionError(ex, format, args);
		}
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
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	public static void assertArrayEqualsRelative(String message, float[] expecteds, float[] actuals,
			double relativeError) throws AssertionError
	{
		new FloatRelativeComparisonCriteria(relativeError).arrayEquals(message, expecteds, actuals);
	}

	/**
	 * Asserts that two float arrays are equal. If they are not, an
	 * {@link AssertionError} is thrown with no message.
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
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	public static void assertFloatArrayEqualsRelative(Object expecteds, Object actuals, double relativeError)
			throws AssertionError
	{
		assertFloatArrayEqualsRelative(null, expecteds, actuals, relativeError);
	}

	/**
	 * Asserts that two float arrays are equal. If they are not, an
	 * {@link AssertionError} is thrown with no message.
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
	 * @param format
	 *            the message format
	 * @param args
	 *            the arguments
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	public static void assertFloatArrayEqualsRelative(Object expecteds, Object actuals, double relativeError,
			String format, Object... args) throws AssertionError
	{
		try
		{
			assertFloatArrayEqualsRelative(null, expecteds, actuals, relativeError);
		}
		catch (final AssertionError ex)
		{
			wrapAssertionError(ex, format, args);
		}
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
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	public static void assertFloatArrayEqualsRelative(String message, Object expecteds, Object actuals,
			double relativeError) throws AssertionError
	{
		new FloatRelativeComparisonCriteria(relativeError).arrayEquals(message, expecteds, actuals);
	}

	////////////////////////////////////////////////////////////////////////////
	// Formatted methods.
	// These wrap the JUnit assert methods with an
	// assertion error containing a formatted message.
	// No checks are made that the format or args are not null.
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Wraps an assertion error with a new error that has a formatted message appended to
	 * the input error's message.
	 *
	 * @param error
	 *            The error
	 * @param format
	 *            The format
	 * @param args
	 *            The arguments
	 * @throws AssertionError
	 *             the wrapped assertion error
	 */
	public static void wrapAssertionErrorAppend(AssertionError error, String format, Object... args)
			throws AssertionError
	{
		final String msg = error.getMessage();
		if (msg == null || msg.length() == 0)
			throw new AssertionError(String.format(format, args), error);
		throw new AssertionError(msg + " " + String.format(format, args), error);
	}

	/**
	 * Wraps an assertion error with a new error that has a formatted message prepended to
	 * the input error's message.
	 *
	 * @param error
	 *            The error
	 * @param format
	 *            The format
	 * @param args
	 *            The arguments
	 * @throws AssertionError
	 *             the wrapped assertion error
	 */
	public static void wrapAssertionError(AssertionError error, String format, Object... args) throws AssertionError
	{
		final String msg = error.getMessage();
		if (msg == null || msg.length() == 0)
			throw new AssertionError(String.format(format, args), error);
		throw new AssertionError(String.format(format, args) + " " + msg, error);
	}

	/**
	 * Asserts that a condition is true. If it isn't it throws an
	 * {@link AssertionError} with a formatted message.
	 *
	 * @param condition
	 *            condition to be checked
	 * @param format
	 *            the message format
	 * @param args
	 *            the arguments
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	static public void assertTrue(boolean condition, String format, Object... args) throws AssertionError
	{
		if (condition)
			return;
		fail(format, args);
	}

	/**
	 * Assert.assert that a condition is false. If it isn't it throws an
	 * {@link AssertionError} with a formatted message.
	 *
	 * @param condition
	 *            condition to be checked
	 * @param format
	 *            the message format
	 * @param args
	 *            the arguments
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	static public void assertFalse(boolean condition, String format, Object... args) throws AssertionError
	{
		if (condition)
			fail(format, args);
	}

	/**
	 * Fails a test with a formatted message.
	 *
	 * @param format
	 *            the message format
	 * @param args
	 *            the arguments
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	static public void fail(String format, Object... args) throws AssertionError
	{
		throw new AssertionError(String.format(format, args));
	}

	/**
	 * Assert.assert that two objects are equal. If they are not, an
	 * {@link AssertionError} with a formatted message is thrown. If
	 * <code>expected</code> and <code>actual</code> are <code>null</code>,
	 * they are considered equal.
	 *
	 * @param expected
	 *            expected value
	 * @param actual
	 *            the value to check against <code>expected</code>
	 * @param format
	 *            the message format
	 * @param args
	 *            the arguments
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	static public void assertEquals(Object expected, Object actual, String format, Object... args) throws AssertionError
	{
		try
		{
			Assert.assertEquals(null, expected, actual);
		}
		catch (final AssertionError ex)
		{
			wrapAssertionError(ex, format, args);
		}
	}

	/**
	 * Assert.assert that two objects are <b>not</b> equals. If they are, an
	 * {@link AssertionError} with a formatted message is thrown. If
	 * <code>unexpected</code> and <code>actual</code> are <code>null</code>,
	 * they are considered equal.
	 *
	 * @param unexpected
	 *            unexpected value to check
	 * @param actual
	 *            the value to check against <code>unexpected</code>
	 * @param format
	 *            the message format
	 * @param args
	 *            the arguments
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	static public void assertNotEquals(Object unexpected, Object actual, String format, Object... args)
			throws AssertionError
	{
		try
		{
			Assert.assertNotEquals(null, unexpected, actual);
		}
		catch (final AssertionError ex)
		{
			wrapAssertionError(ex, format, args);
		}
	}

	/**
	 * Assert.assert that two longs are <b>not</b> equals. If they are, an
	 * {@link AssertionError} with a formatted message is thrown.
	 *
	 * @param unexpected
	 *            unexpected value to check
	 * @param actual
	 *            the value to check against <code>unexpected</code>
	 * @param format
	 *            the message format
	 * @param args
	 *            the arguments
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	static public void assertNotEquals(long unexpected, long actual, String format, Object... args)
			throws AssertionError
	{
		try
		{
			Assert.assertNotEquals(null, unexpected, actual);
		}
		catch (final AssertionError ex)
		{
			wrapAssertionError(ex, format, args);
		}
	}

	/**
	 * Assert.assert that two doubles are <b>not</b> equal to within a positive delta.
	 * If they are, an {@link AssertionError} is thrown. If the unexpected
	 * value is infinity then the delta value is ignored.NaNs are considered
	 * equal: <code>assertNotEquals(Double.NaN, Double.NaN, *)</code> fails
	 *
	 * @param unexpected
	 *            unexpected value
	 * @param actual
	 *            the value to check against <code>unexpected</code>
	 * @param delta
	 *            the maximum delta between <code>unexpected</code> and
	 *            <code>actual</code> for which both numbers are still
	 *            considered equal.
	 * @param format
	 *            the message format
	 * @param args
	 *            the arguments
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	static public void assertNotEquals(double unexpected, double actual, double delta, String format, Object... args)
			throws AssertionError
	{
		try
		{
			Assert.assertNotEquals(null, unexpected, actual, delta);
		}
		catch (final AssertionError ex)
		{
			wrapAssertionError(ex, format, args);
		}
	}

	/**
	 * Assert.assert that two floats are <b>not</b> equal to within a positive delta.
	 * If they are, an {@link AssertionError} is thrown. If the unexpected
	 * value is infinity then the delta value is ignored.NaNs are considered
	 * equal: <code>assertNotEquals(Float.NaN, Float.NaN, *)</code> fails
	 *
	 * @param unexpected
	 *            unexpected value
	 * @param actual
	 *            the value to check against <code>unexpected</code>
	 * @param delta
	 *            the maximum delta between <code>unexpected</code> and
	 *            <code>actual</code> for which both numbers are still
	 *            considered equal.
	 * @param format
	 *            the message format
	 * @param args
	 *            the arguments
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	static public void assertNotEquals(float unexpected, float actual, float delta, String format, Object... args)
			throws AssertionError
	{
		try
		{
			Assert.assertNotEquals(null, unexpected, actual, delta);
		}
		catch (final AssertionError ex)
		{
			wrapAssertionError(ex, format, args);
		}
	}

	/**
	 * Assert.assert that two object arrays are equal. If they are not, an
	 * {@link AssertionError} is thrown. If <code>expected</code> and
	 * <code>actual</code> are <code>null</code>, they are considered
	 * equal.
	 *
	 * @param expecteds
	 *            Object array or array of arrays (multi-dimensional array) with
	 *            expected values
	 * @param actuals
	 *            Object array or array of arrays (multi-dimensional array) with
	 *            actual values
	 * @param format
	 *            the message format
	 * @param args
	 *            the arguments
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	public static void assertArrayEquals(Object[] expecteds, Object[] actuals, String format, Object... args)
			throws AssertionError
	{
		try
		{
			Assert.assertArrayEquals(null, expecteds, actuals);
		}
		catch (final AssertionError ex)
		{
			wrapAssertionError(ex, format, args);
		}
	}

	/**
	 * Assert.assert that two boolean arrays are equal. If they are not, an
	 * {@link AssertionError} is thrown. If <code>expected</code> and
	 * <code>actual</code> are <code>null</code>, they are considered
	 * equal.
	 *
	 * @param expecteds
	 *            boolean array with expected values.
	 * @param actuals
	 *            boolean array with expected values.
	 * @param format
	 *            the message format
	 * @param args
	 *            the arguments
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	public static void assertArrayEquals(boolean[] expecteds, boolean[] actuals, String format, Object... args)
			throws AssertionError
	{
		try
		{
			Assert.assertArrayEquals(null, expecteds, actuals);
		}
		catch (final AssertionError ex)
		{
			wrapAssertionError(ex, format, args);
		}
	}

	/**
	 * Assert.assert that two byte arrays are equal. If they are not, an
	 * {@link AssertionError} is thrown.
	 *
	 * @param expecteds
	 *            byte array with expected values.
	 * @param actuals
	 *            byte array with actual values
	 * @param format
	 *            the message format
	 * @param args
	 *            the arguments
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	public static void assertArrayEquals(byte[] expecteds, byte[] actuals, String format, Object... args)
			throws AssertionError
	{
		try
		{
			Assert.assertArrayEquals(null, expecteds, actuals);
		}
		catch (final AssertionError ex)
		{
			wrapAssertionError(ex, format, args);
		}
	}

	/**
	 * Assert.assert that two char arrays are equal. If they are not, an
	 * {@link AssertionError} is thrown.
	 *
	 * @param expecteds
	 *            char array with expected values.
	 * @param actuals
	 *            char array with actual values
	 * @param format
	 *            the message format
	 * @param args
	 *            the arguments
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	public static void assertArrayEquals(char[] expecteds, char[] actuals, String format, Object... args)
			throws AssertionError
	{
		try
		{
			Assert.assertArrayEquals(null, expecteds, actuals);
		}
		catch (final AssertionError ex)
		{
			wrapAssertionError(ex, format, args);
		}
	}

	/**
	 * Assert.assert that two short arrays are equal. If they are not, an
	 * {@link AssertionError} is thrown.
	 *
	 * @param expecteds
	 *            short array with expected values.
	 * @param actuals
	 *            short array with actual values
	 * @param format
	 *            the message format
	 * @param args
	 *            the arguments
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	public static void assertArrayEquals(short[] expecteds, short[] actuals, String format, Object... args)
			throws AssertionError
	{
		try
		{
			Assert.assertArrayEquals(null, expecteds, actuals);
		}
		catch (final AssertionError ex)
		{
			wrapAssertionError(ex, format, args);
		}
	}

	/**
	 * Assert.assert that two int arrays are equal. If they are not, an
	 * {@link AssertionError} is thrown.
	 *
	 * @param expecteds
	 *            int array with expected values.
	 * @param actuals
	 *            int array with actual values
	 * @param format
	 *            the message format
	 * @param args
	 *            the arguments
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	public static void assertArrayEquals(int[] expecteds, int[] actuals, String format, Object... args)
			throws AssertionError
	{
		try
		{
			Assert.assertArrayEquals(null, expecteds, actuals);
		}
		catch (final AssertionError ex)
		{
			wrapAssertionError(ex, format, args);
		}
	}

	/**
	 * Assert.assert that two long arrays are equal. If they are not, an
	 * {@link AssertionError} is thrown.
	 *
	 * @param expecteds
	 *            long array with expected values.
	 * @param actuals
	 *            long array with actual values
	 * @param format
	 *            the message format
	 * @param args
	 *            the arguments
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	public static void assertArrayEquals(long[] expecteds, long[] actuals, String format, Object... args)
			throws AssertionError
	{
		try
		{
			Assert.assertArrayEquals(null, expecteds, actuals);
		}
		catch (final AssertionError ex)
		{
			wrapAssertionError(ex, format, args);
		}
	}

	/**
	 * Assert.assert that two double arrays are equal. If they are not, an
	 * {@link AssertionError} is thrown.
	 *
	 * @param expecteds
	 *            double array with expected values.
	 * @param actuals
	 *            double array with actual values
	 * @param delta
	 *            the maximum delta between <code>expecteds[i]</code> and
	 *            <code>actuals[i]</code> for which both numbers are still
	 *            considered equal.
	 * @param format
	 *            the message format
	 * @param args
	 *            the arguments
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	public static void assertArrayEquals(double[] expecteds, double[] actuals, double delta, String format,
			Object... args) throws AssertionError
	{
		try
		{
			Assert.assertArrayEquals(null, expecteds, actuals, delta);
		}
		catch (final AssertionError ex)
		{
			wrapAssertionError(ex, format, args);
		}
	}

	/**
	 * Assert.assert that two float arrays are equal. If they are not, an
	 * {@link AssertionError} is thrown.
	 *
	 * @param expecteds
	 *            float array with expected values.
	 * @param actuals
	 *            float array with actual values
	 * @param delta
	 *            the maximum delta between <code>expecteds[i]</code> and
	 *            <code>actuals[i]</code> for which both numbers are still
	 *            considered equal.
	 * @param format
	 *            the message format
	 * @param args
	 *            the arguments
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	public static void assertArrayEquals(float[] expecteds, float[] actuals, float delta, String format, Object... args)
			throws AssertionError
	{
		try
		{
			Assert.assertArrayEquals(null, expecteds, actuals, delta);
		}
		catch (final AssertionError ex)
		{
			wrapAssertionError(ex, format, args);
		}
	}

	/**
	 * Assert.assert that two longs are equal. If they are not, an
	 * {@link AssertionError} is thrown.
	 *
	 * @param expected
	 *            expected long value.
	 * @param actual
	 *            actual long value
	 * @param format
	 *            the message format
	 * @param args
	 *            the arguments
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	static public void assertLongEquals(long expected, long actual, String format, Object... args) throws AssertionError
	{
		try
		{
			Assert.assertEquals(null, expected, actual);
		}
		catch (final AssertionError ex)
		{
			wrapAssertionError(ex, format, args);
		}
	}

	/**
	 * Assert.assert that two doubles are equal to within a positive delta.
	 * If they are not, an {@link AssertionError} is thrown. If the expected
	 * value is infinity then the delta value is ignored.NaNs are considered
	 * equal: <code>assertEqualsRelative(Double.NaN, Double.NaN, *)</code> passes
	 *
	 * @param expected
	 *            expected value
	 * @param actual
	 *            the value to check against <code>expected</code>
	 * @param delta
	 *            the maximum delta between <code>expected</code> and
	 *            <code>actual</code> for which both numbers are still
	 *            considered equal.
	 * @param format
	 *            the message format
	 * @param args
	 *            the arguments
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	static public void assertEquals(double expected, double actual, double delta, String format, Object... args)
			throws AssertionError
	{
		try
		{
			Assert.assertEquals(null, expected, actual, delta);
		}
		catch (final AssertionError ex)
		{
			wrapAssertionError(ex, format, args);
		}
	}

	/**
	 * Assert.assert that two floats are equal to within a positive delta.
	 * If they are not, an {@link AssertionError} is thrown. If the expected
	 * value is infinity then the delta value is ignored. NaNs are considered
	 * equal: <code>assertEquals(Float.NaN, Float.NaN, *)</code> passes
	 *
	 * @param expected
	 *            expected value
	 * @param actual
	 *            the value to check against <code>expected</code>
	 * @param delta
	 *            the maximum delta between <code>expected</code> and
	 *            <code>actual</code> for which both numbers are still
	 *            considered equal.
	 * @param format
	 *            the message format
	 * @param args
	 *            the arguments
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	static public void assertEquals(float expected, float actual, float delta, String format, Object... args)
			throws AssertionError
	{
		try
		{
			Assert.assertEquals(null, expected, actual, delta);
		}
		catch (final AssertionError ex)
		{
			wrapAssertionError(ex, format, args);
		}
	}

	/**
	 * Assert.assert that an object isn't null. If it is an {@link AssertionError} is
	 * thrown.
	 *
	 * @param object
	 *            Object to check or <code>null</code>
	 * @param format
	 *            the message format
	 * @param args
	 *            the arguments
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	static public void assertNotNull(Object object, String format, Object... args) throws AssertionError
	{
		try
		{
			Assert.assertNotNull(null, object);
		}
		catch (final AssertionError ex)
		{
			wrapAssertionError(ex, format, args);
		}
	}

	/**
	 * Assert.assert that an object is null. If it isn't an {@link AssertionError} is
	 * thrown.
	 *
	 * @param object
	 *            Object to check or <code>null</code>
	 * @param format
	 *            the message format
	 * @param args
	 *            the arguments
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	static public void assertNull(Object object, String format, Object... args) throws AssertionError
	{
		try
		{
			Assert.assertNull(null, object);
		}
		catch (final AssertionError ex)
		{
			wrapAssertionError(ex, format, args);
		}
	}

	/**
	 * Assert.assert that two objects refer to the same object. If they are not the
	 * same, an {@link AssertionError} with a formatted message is thrown.
	 *
	 * @param expected
	 *            the expected object
	 * @param actual
	 *            the object to compare to <code>expected</code>
	 * @param format
	 *            the message format
	 * @param args
	 *            the arguments
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	static public void assertSame(Object expected, Object actual, String format, Object... args) throws AssertionError
	{
		try
		{
			Assert.assertSame(null, expected, actual);
		}
		catch (final AssertionError ex)
		{
			wrapAssertionError(ex, format, args);
		}
	}

	/**
	 * Assert.assert that two objects do not refer to the same object. If they do
	 * refer to the same object, an {@link AssertionError} with a formatted message is
	 * thrown.
	 *
	 * @param unexpected
	 *            the object you don't expect
	 * @param actual
	 *            the object to compare to <code>unexpected</code>
	 * @param format
	 *            the message format
	 * @param args
	 *            the arguments
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 */
	static public void assertNotSame(Object unexpected, Object actual, String format, Object... args)
			throws AssertionError
	{
		try
		{
			Assert.assertNotSame(null, unexpected, actual);
		}
		catch (final AssertionError ex)
		{
			wrapAssertionError(ex, format, args);
		}
	}

	/**
	 * Assert.assert that <code>actual</code> satisfies the condition specified by
	 * <code>matcher</code>. If not, an {@link AssertionError} is thrown with
	 * information about the matcher and failing value. Example:
	 *
	 * <pre>
	 *   assertThat(0, is(1)); // fails:
	 *     // failure message:
	 *     // expected: is &lt;1&gt;
	 *     // got value: &lt;0&gt;
	 *   assertThat(0, is(not(1))) // passes
	 * </pre>
	 *
	 * <code>org.hamcrest.Matcher</code> does not currently document the meaning
	 * of its type parameter <code>T</code>. This method assumes that a matcher
	 * typed as <code>Matcher&lt;T&gt;</code> can be meaningfully applied only
	 * to values that could be assigned to a variable of type <code>T</code>.
	 *
	 * @param <T>
	 *            the static type accepted by the matcher (this can flag obvious
	 *            compile-time problems such as {@code assertThat(1, is("a"))}
	 * @param actual
	 *            the computed value being compared
	 * @param matcher
	 *            an expression, built of {@link Matcher}s, specifying allowed
	 *            values
	 * @param format
	 *            the message format
	 * @param args
	 *            the arguments
	 * @throws AssertionError
	 *             if the assertion is not {@code true}
	 * @see org.hamcrest.CoreMatchers
	 * @see org.hamcrest.MatcherAssert
	 */
	public static <T> void assertThat(T actual, Matcher<? super T> matcher, String format, Object... args)
			throws AssertionError
	{
		try
		{
			Assert.assertThat("", actual, matcher);
		}
		catch (final AssertionError ex)
		{
			wrapAssertionError(ex, format, args);
		}
	}
}
