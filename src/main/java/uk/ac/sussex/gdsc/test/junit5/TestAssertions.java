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

import java.util.function.Supplier;

import org.junit.jupiter.api.Assertions;
import org.opentest4j.AssertionFailedError;

/**
 * Class providing additional assertion functions for tests.
 */
public class TestAssertions
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
	 * If they are not, an {@link AssertionFailedError} is thrown with no
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
	 * @throws AssertionFailedError
	 *             if the assertion is not {@code true}
	 */
	public static void assertEqualsRelative(double expected, double actual, double relativeError)
			throws AssertionFailedError
	{
		final double max = max(Math.abs(expected), Math.abs(actual));
		try
		{
			Assertions.assertEquals(expected, actual, max * relativeError);
		}
		catch (final AssertionFailedError ex)
		{
			wrapAssertionFailedErrorAppend(ex, " (error=%s)", Math.abs(expected - actual) / max);
		}
	}

	/**
	 * Asserts that two doubles are equal to within a positive relativeError.
	 * If they are not, an {@link AssertionFailedError} is thrown with no
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
	 * @param message
	 *            the message
	 * @throws AssertionFailedError
	 *             if the assertion is not {@code true}
	 */
	public static void assertEqualsRelative(double expected, double actual, double relativeError, String message)
			throws AssertionFailedError
	{
		final double max = max(Math.abs(expected), Math.abs(actual));
		try
		{
			Assertions.assertEquals(expected, actual, max * relativeError, message);
		}
		catch (final AssertionFailedError ex)
		{
			wrapAssertionFailedErrorAppend(ex, " (error=%s)", Math.abs(expected - actual) / max);
		}
	}

	/**
	 * Asserts that two doubles are equal to within a positive relativeError.
	 * If they are not, an {@link AssertionFailedError} is thrown with the formatted
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
	 * @param messageSupplier
	 *            the message supplier
	 * @throws AssertionFailedError
	 *             if the assertion is not {@code true}
	 */
	public static void assertEqualsRelative(double expected, double actual, double relativeError,
			Supplier<String> messageSupplier) throws AssertionFailedError
	{
		final double max = max(Math.abs(expected), Math.abs(actual));
		try
		{
			assertEqualsRelative(expected, actual, relativeError, messageSupplier);
		}
		catch (final AssertionFailedError ex)
		{
			wrapAssertionFailedErrorAppend(ex, " (error=%s)", Math.abs(expected - actual) / max);
		}
	}

	/**
	 * Asserts that two doubles are equal to within a positive relativeError.
	 * If they are not, an {@link AssertionFailedError} is thrown with the formatted
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
	 * @throws AssertionFailedError
	 *             if the assertion is not {@code true}
	 */
	public static void assertEqualsRelative(double expected, double actual, double relativeError, String format,
			Object... args) throws AssertionFailedError
	{
		final double max = max(Math.abs(expected), Math.abs(actual));
		try
		{
			assertEqualsRelative(expected, actual, relativeError, () -> {
				return String.format(format, args);
			});
		}
		catch (final AssertionFailedError ex)
		{
			wrapAssertionFailedErrorAppend(ex, " (error=%s)", Math.abs(expected - actual) / max);
		}
	}

	/**
	 * Asserts that two doubles are equal to within a positive relativeError.
	 * If they are not, an {@link AssertionFailedError} is thrown with no
	 * message. If the expected value is infinity then the relativeError value is
	 * ignored. NaNs are considered equal:
	 * <code>assertEqualsRelative(Float.NaN, Float.NaN, *)</code> passes
	 *
	 * @param expected
	 *            expected value
	 * @param actual
	 *            the value to check against <code>expected</code>
	 * @param relativeError
	 *            the maximum relative error between <code>expected</code> and
	 *            <code>actual</code> for which both numbers are still
	 *            considered equal.
	 * @throws AssertionFailedError
	 *             if the assertion is not {@code true}
	 */
	public static void assertEqualsRelative(float expected, float actual, double relativeError)
			throws AssertionFailedError
	{
		final double max = max(Math.abs(expected), Math.abs(actual));
		try
		{
			Assertions.assertEquals(expected, actual, max * relativeError);
		}
		catch (final AssertionFailedError ex)
		{
			wrapAssertionFailedErrorAppend(ex, " (error=%s)", Math.abs(expected - actual) / max);
		}
	}

	/**
	 * Asserts that two doubles are equal to within a positive relativeError.
	 * If they are not, an {@link AssertionFailedError} is thrown with no
	 * message. If the expected value is infinity then the relativeError value is
	 * ignored. NaNs are considered equal:
	 * <code>assertEqualsRelative(Float.NaN, Float.NaN, *)</code> passes
	 *
	 * @param expected
	 *            expected value
	 * @param actual
	 *            the value to check against <code>expected</code>
	 * @param relativeError
	 *            the maximum relative error between <code>expected</code> and
	 *            <code>actual</code> for which both numbers are still
	 *            considered equal.
	 * @param message
	 *            the message
	 * @throws AssertionFailedError
	 *             if the assertion is not {@code true}
	 */
	public static void assertEqualsRelative(float expected, float actual, double relativeError, String message)
			throws AssertionFailedError
	{
		final double max = max(Math.abs(expected), Math.abs(actual));
		try
		{
			Assertions.assertEquals(expected, actual, max * relativeError, message);
		}
		catch (final AssertionFailedError ex)
		{
			wrapAssertionFailedErrorAppend(ex, " (error=%s)", Math.abs(expected - actual) / max);
		}
	}

	/**
	 * Asserts that two doubles are equal to within a positive relativeError.
	 * If they are not, an {@link AssertionFailedError} is thrown with the formatted
	 * message. If the expected value is infinity then the relativeError value is
	 * ignored. NaNs are considered equal:
	 * <code>assertEqualsRelative(Float.NaN, Float.NaN, *)</code> passes
	 *
	 * @param expected
	 *            expected value
	 * @param actual
	 *            the value to check against <code>expected</code>
	 * @param relativeError
	 *            the maximum relative error between <code>expected</code> and
	 *            <code>actual</code> for which both numbers are still
	 *            considered equal.
	 * @param messageSupplier
	 *            the message supplier
	 * @throws AssertionFailedError
	 *             if the assertion is not {@code true}
	 */
	public static void assertEqualsRelative(float expected, float actual, double relativeError,
			Supplier<String> messageSupplier) throws AssertionFailedError
	{
		final double max = max(Math.abs(expected), Math.abs(actual));
		try
		{
			assertEqualsRelative(expected, actual, relativeError, messageSupplier);
		}
		catch (final AssertionFailedError ex)
		{
			wrapAssertionFailedErrorAppend(ex, " (error=%s)", Math.abs(expected - actual) / max);
		}
	}

	/**
	 * Asserts that two doubles are equal to within a positive relativeError.
	 * If they are not, an {@link AssertionFailedError} is thrown with the formatted
	 * message. If the expected value is infinity then the relativeError value is
	 * ignored. NaNs are considered equal:
	 * <code>assertEqualsRelative(Float.NaN, Float.NaN, *)</code> passes
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
	 * @throws AssertionFailedError
	 *             if the assertion is not {@code true}
	 */
	public static void assertEqualsRelative(float expected, float actual, double relativeError, String format,
			Object... args) throws AssertionFailedError
	{
		final double max = max(Math.abs(expected), Math.abs(actual));
		try
		{
			assertEqualsRelative(expected, actual, relativeError, () -> {
				return String.format(format, args);
			});
		}
		catch (final AssertionFailedError ex)
		{
			wrapAssertionFailedErrorAppend(ex, " (error=%s)", Math.abs(expected - actual) / max);
		}
	}

	// The new JUnit 5 framework is not easy to adapt for array comparisons.
	// JUnit 4 had a single class that could be overridden. JUnit 5 requires duplicating
	// utility functionality from classes that are private.

	/**
	 * Asserts that two double arrays are equal. If they are not, an
	 * {@link AssertionFailedError} is thrown with no message.
	 *
	 * @param expecteds
	 *            double array with expected values.
	 * @param actuals
	 *            double array with actual values
	 * @param relativeError
	 *            the relative error
	 * @throws AssertionFailedError
	 *             if the assertion is not {@code true}
	 */
	public static void assertArrayEqualsRelative(double[] expecteds, double[] actuals, double relativeError)
			throws AssertionFailedError
	{
		// TODO
	}

	/**
	 * Asserts that two double arrays are equal. If they are not, an
	 * {@link AssertionFailedError} is thrown with the given message.
	 *
	 * @param expecteds
	 *            double array with expected values.
	 * @param actuals
	 *            double array with actual values
	 * @param relativeError
	 *            the maximum relativeError between <code>expecteds[i]</code> and
	 *            <code>actuals[i]</code> for which both numbers are still
	 *            considered equal.
	 * @param message
	 *            the identifying message for the {@link AssertionFailedError} (<code>null</code>
	 *            okay)
	 * @throws AssertionFailedError
	 *             if the assertion is not {@code true}
	 */
	public static void assertArrayEqualsRelative(double[] expecteds, double[] actuals, double relativeError,
			String message) throws AssertionFailedError
	{
		// TODO
	}

	/**
	 * Asserts that two double arrays are equal. If they are not, an
	 * {@link AssertionFailedError} is thrown with the given message.
	 *
	 * @param expecteds
	 *            double array with expected values.
	 * @param actuals
	 *            double array with actual values
	 * @param relativeError
	 *            the maximum relativeError between <code>expecteds[i]</code> and
	 *            <code>actuals[i]</code> for which both numbers are still
	 *            considered equal.
	 * @param messageSupplier
	 *            the message supplier
	 * @throws AssertionFailedError
	 *             if the assertion is not {@code true}
	 */
	public static void assertArrayEqualsRelative(double[] expecteds, double[] actuals, double relativeError,
			Supplier<String> messageSupplier) throws AssertionFailedError
	{
		// TODO
	}

	/**
	 * Asserts that two double arrays are equal. If they are not, an
	 * {@link AssertionFailedError} is thrown with no message.
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
	 * @throws AssertionFailedError
	 *             if the assertion is not {@code true}
	 */
	public static void assertArrayEqualsRelative(double[] expecteds, double[] actuals, double relativeError,
			String format, Object... args) throws AssertionFailedError
	{
		// TODO
	}

	/**
	 * Asserts that two double arrays are equal. If they are not, an
	 * {@link AssertionFailedError} is thrown with no message.
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
	 * @throws AssertionFailedError
	 *             if the assertion is not {@code true}
	 */
	public static void assertDoubleArrayEqualsRelative(Object expecteds, Object actuals, double relativeError)
			throws AssertionFailedError
	{
		// TODO
	}

	/**
	 * Asserts that two double arrays are equal. If they are not, an
	 * {@link AssertionFailedError} is thrown with no message.
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
	 * @param message
	 *            the message
	 * @throws AssertionFailedError
	 *             if the assertion is not {@code true}
	 */
	public static void assertDoubleArrayEqualsRelative(Object expecteds, Object actuals, double relativeError,
			String message) throws AssertionFailedError
	{
		// TODO
	}

	/**
	 * Asserts that two double arrays are equal. If they are not, an
	 * {@link AssertionFailedError} is thrown with no message.
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
	 * @param messageSupplier
	 *            the message supplier
	 * @throws AssertionFailedError
	 *             if the assertion is not {@code true}
	 */
	public static void assertDoubleArrayEqualsRelative(Object expecteds, Object actuals, double relativeError,
			Supplier<String> messageSupplier) throws AssertionFailedError
	{
		// TODO
	}

	/**
	 * Asserts that two double arrays are equal. If they are not, an
	 * {@link AssertionFailedError} is thrown with no message.
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
	 * @throws AssertionFailedError
	 *             if the assertion is not {@code true}
	 */
	public static void assertDoubleArrayEqualsRelative(Object expecteds, Object actuals, double relativeError,
			String format, Object... args) throws AssertionFailedError
	{
		// TODO
	}

	// TODO - Add float equivalents

	////////////////////////////////////////////////////////////////////////////
	// Formatted methods.
	// These wrap the JUnit assert methods with an
	// assertion error containing a formatted message.
	// No checks are made that the format or arguments are not null.
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
	 * @throws AssertionFailedError
	 *             the wrapped assertion error
	 */
	public static void wrapAssertionFailedErrorAppend(AssertionFailedError error, String format, Object... args)
			throws AssertionFailedError
	{
		final String msg = error.getMessage();
		if (msg == null || msg.length() == 0)
			throw new AssertionFailedError(String.format(format, args), error);
		throw new AssertionFailedError(msg + " " + String.format(format, args), error);
	}

	/**
	 * Wraps an assertion error with a new error that has a formatted message appended to
	 * the input error's message.
	 *
	 * @param error
	 *            The error
	 * @param messageSupplier
	 *            the message supplier
	 * @throws AssertionFailedError
	 *             the wrapped assertion error
	 */
	public static void wrapAssertionFailedErrorAppend(AssertionFailedError error, Supplier<String> messageSupplier)
			throws AssertionFailedError
	{
		final String msg = error.getMessage();
		if (msg == null || msg.length() == 0)
			throw new AssertionFailedError(messageSupplier.get(), error);
		throw new AssertionFailedError(msg + " " + messageSupplier.get(), error);
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
	 * @throws AssertionFailedError
	 *             the wrapped assertion error
	 */
	public static void wrapAssertionFailedError(AssertionFailedError error, String format, Object... args)
			throws AssertionFailedError
	{
		final String msg = error.getMessage();
		if (msg == null || msg.length() == 0)
			throw new AssertionFailedError(String.format(format, args), error);
		throw new AssertionFailedError(String.format(format, args) + " " + msg, error);
	}

	/**
	 * Wraps an assertion error with a new error that has a formatted message prepended to
	 * the input error's message.
	 *
	 * @param error
	 *            The error
	 * @param messageSupplier
	 *            the message supplier
	 * @throws AssertionFailedError
	 *             the wrapped assertion error
	 */
	public static void wrapAssertionFailedError(AssertionFailedError error, Supplier<String> messageSupplier)
			throws AssertionFailedError
	{
		final String msg = error.getMessage();
		if (msg == null || msg.length() == 0)
			throw new AssertionFailedError(messageSupplier.get(), error);
		throw new AssertionFailedError(messageSupplier.get() + " " + msg, error);
	}
}
