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

import static java.util.stream.Collectors.joining;

import java.util.Deque;
import java.util.function.Supplier;

import org.junit.platform.commons.util.StringUtils;
import org.opentest4j.AssertionFailedError;

/**
 * Adds additional helper assertion utility functions.
 * <p>
 * Note: Many methods have been copied from the {@code org.junit.jupiter.api} package.
 */
class ExtraAssertionUtils
{
	/**
	 * Assert that the relative error is valid.
	 * The value must be in the range {@code 0} exclusive to {@code 2} exclusive.
	 * Values outside this range are meaningless.
	 * <p>
	 * Note that 2 is the maximum relative error of two values if they are opposite signs:<br>
	 * {@code abs(a-b) / max(abs(a), abs(b))}.
	 *
	 * @param relativeError
	 *            the maximum relative error between <code>expected</code> and
	 *            <code>actual</code> for which both numbers are still
	 *            considered equal.
	 */
	static void assertValidRelativeError(double relativeError)
	{
		if (!(relativeError > 0 && relativeError < 2))
			fail("relative error expected but was: <" + relativeError + ">");
	}

	/**
	 * <em>Asserts</em> that {@code expected} and {@code actual} are equal within the given {@code relativeError}.
	 * <p>
	 * Equality imposed by this method is consistent with {@link Double#equals(Object)} and
	 * {@link Double#compare(double, double)}.
	 * </p>
	 * <p>
	 * Fails with the supplied failure {@code message}.
	 * <p>
	 *
	 * @param expected
	 *            The expected value.
	 * @param actual
	 *            The value to check against <code>expected</code>.
	 * @param relativeError
	 *            the maximum relative error between <code>expected</code> and
	 *            <code>actual</code> for which both numbers are still
	 *            considered equal.
	 * @param message
	 *            The message.
	 */
	static void assertEqualsRelative(double expected, double actual, double relativeError, String message)
	{
		if (!doublesAreEqualRelative(expected, actual, relativeError))
			failNotEqual(expected, actual, relativeError, message);
	}

	/**
	 * <em>Asserts</em> that {@code expected} and {@code actual} are equal within the given {@code relativeError}.
	 * <p>
	 * Equality imposed by this method is consistent with {@link Double#equals(Object)} and
	 * {@link Double#compare(double, double)}.
	 * </p>
	 * <p>
	 * If necessary, the failure message will be retrieved lazily from the supplied {@code messageSupplier}.
	 * </p>
	 *
	 * @param expected
	 *            The expected value.
	 * @param actual
	 *            The value to check against <code>expected</code>.
	 * @param relativeError
	 *            the maximum relative error between <code>expected</code> and
	 *            <code>actual</code> for which both numbers are still
	 *            considered equal.
	 * @param messageSupplier
	 *            The message supplier.
	 */
	static void assertEqualsRelative(double expected, double actual, double relativeError,
			Supplier<String> messageSupplier)
	{
		if (!doublesAreEqualRelative(expected, actual, relativeError))
			failNotEqual(expected, actual, relativeError, messageSupplier);
	}

	/**
	 * Tests that two doubles are equal to within a positive relativeError.
	 * If the expected value is infinity then the relativeError value is
	 * ignored. NaNs are considered equal:
	 * <code>assertEqualsRelative(Double.NaN, Double.NaN, *)</code> passes.
	 *
	 * @param value1
	 *            the first value
	 * @param value2
	 *            the second value
	 * @param relativeError
	 *            the maximum relative error between <code>value1</code> and
	 *            <code>value2</code> for which both numbers are still
	 *            considered equal.
	 * @return true, if equal
	 */
	static boolean doublesAreEqualRelative(double value1, double value2, double relativeError)
	{
		if (doublesAreEqual(value1, value2))
			return true;
		assertValidRelativeError(relativeError);
		final double max = max(Math.abs(value1), Math.abs(value2));
		return Math.abs(value1 - value2) <= max * relativeError;
	}

	/**
	 * Tests that two doubles are equal to within a positive relativeError.
	 * If the expected value is infinity then the relativeError value is
	 * ignored. NaNs are considered equal:
	 * <code>assertEqualsRelative(Double.NaN, Double.NaN, *)</code> passes.
	 * <p>
	 * It is assumed the relative error has been checked with {@link #assertValidRelativeError(double)}.
	 *
	 * @param value1
	 *            the first value
	 * @param value2
	 *            the second value
	 * @param relativeError
	 *            the maximum relative error between <code>value1</code> and
	 *            <code>value2</code> for which both numbers are still
	 *            considered equal.
	 * @return true, if equal
	 */
	static boolean doublesAreEqualRelativeValid(double value1, double value2, double relativeError)
	{
		if (doublesAreEqual(value1, value2))
			return true;
		final double max = max(Math.abs(value1), Math.abs(value2));
		return Math.abs(value1 - value2) <= max * relativeError;
	}

	/**
	 * Get the maximum
	 *
	 * @param value1
	 *            the first value
	 * @param value2
	 *            the second value
	 * @return the maximum
	 */
	private static double max(double value1, double value2)
	{
		return (value1 >= value2) ? value1 : value2;
	}

	private static void failNotEqual(double expected, double actual, double relativeError, String message)
			throws AssertionFailedError
	{
		fail(format(expected, actual, relativeError, message), expected, actual);
	}

	private static void failNotEqual(double expected, double actual, double relativeError,
			Supplier<String> messageSupplier) throws AssertionFailedError
	{
		fail(format(expected, actual, relativeError, nullSafeGet(messageSupplier)), expected, actual);
	}

	/**
	 * Format the message.
	 * <p>
	 * Adapted from {@code format(Object, Object, String)}.
	 *
	 * @param expected
	 *            expected value
	 * @param actual
	 *            the value to check against <code>expected</code>
	 * @param relativeError
	 *            the relative error
	 * @param message
	 *            the message
	 * @return the formatted message
	 */
	static String format(double expected, double actual, double relativeError, String message)
	{
		return buildPrefix(message) + formatValues(expected, actual, relativeError);
	}

	/**
	 * Format the values.
	 * <p>
	 * Adapted from {@code formatValues(Object, Object)}.
	 *
	 * @param expected
	 *            expected value
	 * @param actual
	 *            the value to check against <code>expected</code>
	 * @param relativeError
	 *            the relative error
	 * @return the formatted values
	 */
	static String formatValues(double expected, double actual, double relativeError)
	{
		final String expectedString = Double.toString(expected);
		final String actualString = Double.toString(actual);
		final double max = max(Math.abs(expected), Math.abs(actual));
		final String error = Double.toString(Math.abs(expected - actual) / max);
		return String.format("expected: <%s> but was: <%s> (error=%s)", expectedString, actualString, error);
	}

	/**
	 * <em>Asserts</em> that {@code expected} and {@code actual} are equal within the given {@code relativeError}.
	 * <p>
	 * Equality imposed by this method is consistent with {@link Float#equals(Object)} and
	 * {@link Float#compare(float, float)}.
	 * </p>
	 * <p>
	 * Fails with the supplied failure {@code message}.
	 * <p>
	 *
	 * @param expected
	 *            The expected value.
	 * @param actual
	 *            The value to check against <code>expected</code>.
	 * @param relativeError
	 *            the maximum relative error between <code>expected</code> and
	 *            <code>actual</code> for which both numbers are still
	 *            considered equal.
	 * @param message
	 *            The message.
	 */
	static void assertEqualsRelative(float expected, float actual, double relativeError, String message)
	{
		if (!floatsAreEqualRelative(expected, actual, relativeError))
			failNotEqual(expected, actual, relativeError, message);
	}

	/**
	 * <em>Asserts</em> that {@code expected} and {@code actual} are equal within the given {@code relativeError}.
	 * <p>
	 * Equality imposed by this method is consistent with {@link Float#equals(Object)} and
	 * {@link Float#compare(float, float)}.
	 * </p>
	 * <p>
	 * If necessary, the failure message will be retrieved lazily from the supplied {@code messageSupplier}.
	 * </p>
	 *
	 * @param expected
	 *            The expected value.
	 * @param actual
	 *            The value to check against <code>expected</code>.
	 * @param relativeError
	 *            the maximum relative error between <code>expected</code> and
	 *            <code>actual</code> for which both numbers are still
	 *            considered equal.
	 * @param messageSupplier
	 *            The message supplier.
	 */
	static void assertEqualsRelative(float expected, float actual, double relativeError,
			Supplier<String> messageSupplier)
	{
		if (!floatsAreEqualRelative(expected, actual, relativeError))
			failNotEqual(expected, actual, relativeError, messageSupplier);
	}

	/**
	 * Tests that two floats are equal to within a positive relativeError.
	 * If the expected value is infinity then the relativeError value is
	 * ignored. NaNs are considered equal:
	 * <code>assertEqualsRelative(Float.NaN, Float.NaN, *)</code> passes.
	 *
	 * @param value1
	 *            the first value
	 * @param value2
	 *            the second value
	 * @param relativeError
	 *            the maximum relative error between <code>value1</code> and
	 *            <code>value2</code> for which both numbers are still
	 *            considered equal.
	 * @return true, if equal
	 */
	static boolean floatsAreEqualRelative(float value1, float value2, double relativeError)
	{
		if (floatsAreEqual(value1, value2))
			return true;
		assertValidRelativeError(relativeError);
		final float max = max(Math.abs(value1), Math.abs(value2));
		return Math.abs(value1 - value2) <= (float) (max * relativeError);
	}

	/**
	 * Tests that two doubles are equal to within a positive relativeError.
	 * If the expected value is infinity then the relativeError value is
	 * ignored. NaNs are considered equal:
	 * <code>assertEqualsRelative(Double.NaN, Double.NaN, *)</code> passes.
	 * <p>
	 * It is assumed the relative error has been checked with {@link #assertValidRelativeError(double)}.
	 *
	 * @param value1
	 *            the first value
	 * @param value2
	 *            the second value
	 * @param relativeError
	 *            the maximum relative error between <code>value1</code> and
	 *            <code>value2</code> for which both numbers are still
	 *            considered equal.
	 * @return true, if equal
	 */
	static boolean floatsAreEqualRelativeValid(float value1, float value2, double relativeError)
	{
		if (floatsAreEqual(value1, value2))
			return true;
		final float max = max(Math.abs(value1), Math.abs(value2));
		return Math.abs(value1 - value2) <= (float) (max * relativeError);
	}

	/**
	 * Get the maximum
	 *
	 * @param value1
	 *            the first value
	 * @param value2
	 *            the second value
	 * @return the maximum
	 */
	private static float max(float value1, float value2)
	{
		return (value1 >= value2) ? value1 : value2;
	}

	private static void failNotEqual(float expected, float actual, double relativeError, String message)
			throws AssertionFailedError
	{
		fail(format(expected, actual, relativeError, message), expected, actual);
	}

	private static void failNotEqual(float expected, float actual, double relativeError,
			Supplier<String> messageSupplier) throws AssertionFailedError
	{
		fail(format(expected, actual, relativeError, nullSafeGet(messageSupplier)), expected, actual);
	}

	/**
	 * Format the message.
	 * <p>
	 * Adapted from {@code format(Object, Object, String)}.
	 *
	 * @param expected
	 *            expected value
	 * @param actual
	 *            the value to check against <code>expected</code>
	 * @param relativeError
	 *            the relative error
	 * @param message
	 *            the message
	 * @return the formatted message
	 */
	static String format(float expected, float actual, double relativeError, String message)
	{
		return buildPrefix(message) + formatValues(expected, actual, relativeError);
	}

	/**
	 * Format the values.
	 * <p>
	 * Adapted from {@code formatValues(Object, Object)}.
	 *
	 * @param expected
	 *            expected value
	 * @param actual
	 *            the value to check against <code>expected</code>
	 * @param relativeError
	 *            the relative error
	 * @return the formatted values
	 */
	static String formatValues(float expected, float actual, double relativeError)
	{
		final String expectedString = Float.toString(expected);
		final String actualString = Float.toString(actual);
		final float max = max(Math.abs(expected), Math.abs(actual));
		final String error = Float.toString(Math.abs(expected - actual) / max);
		return String.format("expected: <%s> but was: <%s> (error=%s)", expectedString, actualString, error);
	}

	// Methods below copied from AssertionUtils.

	/**
	 * Fail with the given message.
	 *
	 * @param message
	 *            the message
	 */
	static void fail(String message)
	{
		throw new AssertionFailedError(message);
	}

	// Taken from AssertArrayEquals
	private static void fail(String message, Object expected, Object actual)
	{
		throw new AssertionFailedError(message, expected, actual);
	}

	// Taken from AssertArrayEquals
	private static String nullSafeGet(Supplier<String> messageSupplier)
	{
		return (messageSupplier != null ? messageSupplier.get() : null);
	}

	/**
	 * Alternative to {@link #nullSafeGet(Supplier)} that is used to avoid
	 * wrapping a String in a lambda expression.
	 *
	 * @param messageOrSupplier
	 *            an object that is either a {@code String} or
	 *            {@code Supplier<String>}
	 * @return the string
	 */
	static String nullSafeGet(Object messageOrSupplier)
	{
		if (messageOrSupplier instanceof String)
			return (String) messageOrSupplier;
		if (messageOrSupplier instanceof Supplier)
		{
			final Object message = ((Supplier<?>) messageOrSupplier).get();
			if (message != null)
				return message.toString();
		}
		return null;
	}

	/**
	 * Builds the fail message prefix.
	 *
	 * @param message
	 *            the message
	 * @return the prefix
	 */
	static String buildPrefix(String message)
	{
		return (StringUtils.isNotBlank(message) ? message + " ==> " : "");
	}

	/**
	 * Format indexes for the array fail message.
	 *
	 * @param indexes
	 *            the indexes
	 * @return the formatted indices
	 */
	static String formatIndexes(Deque<Integer> indexes)
	{
		if (indexes == null || indexes.isEmpty())
			return "";
		final String indexesString = indexes.stream().map(Object::toString).collect(joining("][", "[", "]"));
		return " at index " + indexesString;
	}

	// Taken from AssertArrayEquals
	private static boolean floatsAreEqual(float value1, float value2)
	{
		return Float.floatToIntBits(value1) == Float.floatToIntBits(value2);
	}

	// Taken from AssertArrayEquals
	private static boolean doublesAreEqual(double value1, double value2)
	{
		return Double.doubleToLongBits(value1) == Double.doubleToLongBits(value2);
	}
}
