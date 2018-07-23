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

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Supplier;

import org.opentest4j.AssertionFailedError;

/**
 * Adds additional helper assert array functions.
 * <p>
 * Note: Many methods have been copied from the {@code org.junit.jupiter.api} package.
 */
class ExtraAssertArrayEquals
{
	/**
	 * <em>Asserts</em> that {@code expected} and {@code actual} double arrays are equal within the given
	 * {@code relativeError}.
	 * <p>
	 * Equality imposed by this method is consistent with {@link Double#equals(Object)} and
	 * {@link Double#compare(double, double)}.
	 * </p>
	 * <p>
	 * Adapted from @code AssertArrayEquals.assertArrayEquals(double[], double[], Deque<Integer>, Object)}.
	 *
	 * @param expected
	 *            The double array with expected values.
	 * @param actual
	 *            The double array with actual values.
	 * @param relativeError
	 *            The maximum relative error between <code>expected[i]</code> and
	 *            <code>actual[i]</code> for which both numbers are still
	 *            considered equal.
	 * @param indexes
	 *            The indexes.
	 * @param messageOrSupplier
	 *            The message or supplier.
	 */
	static void assertArrayEqualsRelative(double[] expected, double[] actual, double relativeError,
			Deque<Integer> indexes, Object messageOrSupplier)
	{
		if (expected == actual)
			return;
		assertArraysNotNull(expected, actual, indexes, messageOrSupplier);
		assertArraysHaveSameLength(expected.length, actual.length, indexes, messageOrSupplier);

		ExtraAssertionUtils.assertValidRelativeError(relativeError);
		for (int i = 0; i < expected.length; i++)
			if (!ExtraAssertionUtils.doublesAreEqualRelativeValid(expected[i], actual[i], relativeError))
				failArraysNotEqual(expected[i], actual[i], relativeError, nullSafeIndexes(indexes, i),
						messageOrSupplier);
	}

	/**
	 * Asserts that two float arrays are equal. If they are not, an
	 * {@link AssertionFailedError} is thrown with the given message.
	 * <p>
	 * Adapted from @code AssertArrayEquals.assertArrayEquals(float[], float[], Deque<Integer>, Object)}.
	 *
	 * @param expected
	 *            float array with expected values.
	 * @param actual
	 *            float array with actual values
	 * @param relativeError
	 *            the maximum relativeError between <code>expected[i]</code> and
	 *            <code>actual[i]</code> for which both numbers are still
	 *            considered equal.
	 * @param indexes
	 *            the indexes
	 * @param messageOrSupplier
	 *            the message or supplier
	 */
	static void assertArrayEqualsRelative(float[] expected, float[] actual, double relativeError,
			Deque<Integer> indexes, Object messageOrSupplier)
	{
		if (expected == actual)
			return;
		assertArraysNotNull(expected, actual, indexes, messageOrSupplier);
		assertArraysHaveSameLength(expected.length, actual.length, indexes, messageOrSupplier);

		ExtraAssertionUtils.assertValidRelativeError(relativeError);
		for (int i = 0; i < expected.length; i++)
			if (!ExtraAssertionUtils.floatsAreEqualRelativeValid(expected[i], actual[i], relativeError))
				failArraysNotEqual(expected[i], actual[i], relativeError, nullSafeIndexes(indexes, i),
						messageOrSupplier);
	}

	/**
	 * Asserts that two float/double arrays are equal. If they are not, an
	 * {@link AssertionFailedError} is thrown.
	 * <p>
	 * This supports nested arrays, e.g. {@code double[][]}.
	 *
	 * @param expected
	 *            float/double array with expected values.
	 * @param actual
	 *            float/double array with actual values
	 * @param relativeError
	 *            the maximum relativeError between <code>expected[i]</code> and
	 *            <code>actual[i]</code> for which both numbers are still
	 *            considered equal.
	 */
	static void assertArrayEqualsRelative(Object[] expected, Object[] actual, double relativeError)
	{
		assertArrayEqualsRelative(expected, actual, relativeError, new ArrayDeque<>(), (String) null);
	}

	/**
	 * Asserts that two float/double arrays are equal. If they are not, an
	 * {@link AssertionFailedError} is thrown with the given message.
	 * <p>
	 * This supports nested arrays, e.g. {@code double[][]}.
	 *
	 * @param expected
	 *            float/double array with expected values.
	 * @param actual
	 *            float/double array with actual values
	 * @param relativeError
	 *            the maximum relativeError between <code>expected[i]</code> and
	 *            <code>actual[i]</code> for which both numbers are still
	 *            considered equal.
	 * @param message
	 *            the message
	 */
	static void assertArrayEqualsRelative(Object[] expected, Object[] actual, double relativeError, String message)
	{
		assertArrayEqualsRelative(expected, actual, relativeError, new ArrayDeque<>(), message);
	}

	/**
	 * Asserts that two float/double arrays are equal. If they are not, an
	 * {@link AssertionFailedError} is thrown with the given message.
	 * <p>
	 * This supports nested arrays, e.g. {@code double[][]}.
	 *
	 * @param expected
	 *            float/double array with expected values.
	 * @param actual
	 *            float/double array with actual values
	 * @param relativeError
	 *            the maximum relativeError between <code>expected[i]</code> and
	 *            <code>actual[i]</code> for which both numbers are still
	 *            considered equal.
	 * @param messageSupplier
	 *            the message supplier
	 */
	static void assertArrayEqualsRelative(Object[] expected, Object[] actual, double relativeError,
			Supplier<String> messageSupplier)
	{
		assertArrayEqualsRelative(expected, actual, relativeError, new ArrayDeque<>(), messageSupplier);
	}

	// Adapted from AssertArrayEquals
	private static void assertArrayEqualsRelative(Object[] expected, Object[] actual, double relativeError,
			Deque<Integer> indexes, Object messageOrSupplier)
	{
		if (expected == actual)
			return;
		assertArraysNotNull(expected, actual, indexes, messageOrSupplier);
		assertArraysHaveSameLength(expected.length, actual.length, indexes, messageOrSupplier);

		for (int i = 0; i < expected.length; i++)
		{
			final Object expectedElement = expected[i];
			final Object actualElement = actual[i];

			if (expectedElement == actualElement)
				continue;

			indexes.addLast(i);
			assertArrayElementsEqual(expectedElement, actualElement, relativeError, indexes, messageOrSupplier);
			indexes.removeLast();
		}
	}

	// Adapted from AssertArrayEquals
	private static void assertArrayElementsEqual(Object expected, Object actual, double relativeError,
			Deque<Integer> indexes, Object messageOrSupplier)
	{
		if (expected instanceof Object[] && actual instanceof Object[])
			assertArrayEqualsRelative((Object[]) expected, (Object[]) actual, relativeError, indexes,
					messageOrSupplier);
		else if (expected instanceof float[] && actual instanceof float[])
			assertArrayEqualsRelative((float[]) expected, (float[]) actual, relativeError, indexes, messageOrSupplier);
		else if (expected instanceof double[] && actual instanceof double[])
			assertArrayEqualsRelative((double[]) expected, (double[]) actual, relativeError, indexes,
					messageOrSupplier);
		else
			ExtraAssertionUtils.fail("relative error expected float/double array but was: <" + getClassName(expected) +
					"> and <" + getClassName(actual) + ">");
	}

	// Taken from ExtraAssertionUtils
	private static String getClassName(Object obj)
	{
		return (obj == null ? "null"
				: obj instanceof Class ? getCanonicalName((Class<?>) obj) : obj.getClass().getName());
	}

	// Taken from ExtraAssertionUtils
	private static String getCanonicalName(Class<?> clazz)
	{
		try
		{
			final String canonicalName = clazz.getCanonicalName();
			return (canonicalName != null ? canonicalName : clazz.getName());
		}
		catch (final Throwable t)
		{
			return clazz.getName();
		}
	}

	// Taken from AssertArrayEquals
	private static void assertArraysNotNull(Object expected, Object actual, Deque<Integer> indexes,
			Object messageOrSupplier)
	{
		if (expected == null)
			failExpectedArrayIsNull(indexes, messageOrSupplier);
		if (actual == null)
			failActualArrayIsNull(indexes, messageOrSupplier);
	}

	// Taken from AssertArrayEquals
	private static void failExpectedArrayIsNull(Deque<Integer> indexes, Object messageOrSupplier)
	{
		ExtraAssertionUtils.fail(ExtraAssertionUtils.buildPrefix(ExtraAssertionUtils.nullSafeGet(messageOrSupplier)) +
				"expected array was <null>" + ExtraAssertionUtils.formatIndexes(indexes));
	}

	// Taken from AssertArrayEquals
	private static void failActualArrayIsNull(Deque<Integer> indexes, Object messageOrSupplier)
	{
		ExtraAssertionUtils.fail(ExtraAssertionUtils.buildPrefix(ExtraAssertionUtils.nullSafeGet(messageOrSupplier)) +
				"actual array was <null>" + ExtraAssertionUtils.formatIndexes(indexes));
	}

	// Taken from AssertArrayEquals
	private static void assertArraysHaveSameLength(int expected, int actual, Deque<Integer> indexes,
			Object messageOrSupplier)
	{
		if (expected != actual)
		{
			final String prefix = ExtraAssertionUtils.buildPrefix(ExtraAssertionUtils.nullSafeGet(messageOrSupplier));
			final String message = "array lengths differ" + ExtraAssertionUtils.formatIndexes(indexes) + ", expected: <" +
					expected + "> but was: <" + actual + ">";
			ExtraAssertionUtils.fail(prefix + message);
		}
	}

	// Taken from AssertArrayEquals
	private static Deque<Integer> nullSafeIndexes(Deque<Integer> indexes, int newIndex)
	{
		final Deque<Integer> result = (indexes != null ? indexes : new ArrayDeque<>());
		result.addLast(newIndex);
		return result;
	}

	// Adapted from AssertArrayEquals
	private static void failArraysNotEqual(double expected, double actual, double relativeError, Deque<Integer> indexes,
			Object messageOrSupplier)
	{
		final String prefix = ExtraAssertionUtils.buildPrefix(ExtraAssertionUtils.nullSafeGet(messageOrSupplier));
		final String message = "array contents differ" + ExtraAssertionUtils.formatIndexes(indexes) + ", " +
				ExtraAssertionUtils.formatValues(expected, actual, relativeError);
		ExtraAssertionUtils.fail(prefix + message);
	}

	// Adapted from AssertArrayEquals
	private static void failArraysNotEqual(float expected, float actual, double relativeError, Deque<Integer> indexes,
			Object messageOrSupplier)
	{
		final String prefix = ExtraAssertionUtils.buildPrefix(ExtraAssertionUtils.nullSafeGet(messageOrSupplier));
		final String message = "array contents differ" + ExtraAssertionUtils.formatIndexes(indexes) + ", " +
				ExtraAssertionUtils.formatValues(expected, actual, relativeError);
		ExtraAssertionUtils.fail(prefix + message);
	}
}
