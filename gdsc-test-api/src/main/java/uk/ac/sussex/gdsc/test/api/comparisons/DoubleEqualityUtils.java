/*-
 * #%L
 * Genome Damage and Stability Centre Test API
 *
 * Contains a generic framework for test predicates.
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

package uk.ac.sussex.gdsc.test.api.comparisons;

/**
 * Defines utilities for testing {@code double} equality.
 */
public final class DoubleEqualityUtils {

  /**
   * Do not allow public construction.
   */
  private DoubleEqualityUtils() {
    // No constructor
  }

  /**
   * Tests that two doubles are equal.
   *
   * <p>Equality imposed by this method is consistent with {@link Double#equals(Object)} and,
   * {@link Double#compare(double, double)}.
   *
   * <p>For example {@code -0} and {@code 0} are not equal. {@code Double.NaN} and
   * {@code Double.NaN} are equal.
   *
   * @param value1 The first value.
   * @param value2 The second value.
   * @return true if equal
   */
  public static boolean areEqual(double value1, double value2) {
    return Double.doubleToLongBits(value1) == Double.doubleToLongBits(value2);
  }

  /**
   * Tests that two doubles are equal within an absolute error.
   *
   * @param value1 The first value.
   * @param value2 The second value.
   * @param absoluteError The maximum absolute error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @return true if within the error
   * @throws IllegalArgumentException If the absolute error is not positive and finite
   */
  public static boolean areWithin(double value1, double value2, double absoluteError) {
    validateAbsoluteError(absoluteError);
    return testAreWithin(value1, value2, absoluteError);
  }

  /**
   * Check the error is within the maximum difference between {@code double} primitives.
   *
   * @param absoluteError The maximum absolute error between two values
   * @throws IllegalArgumentException If the absolute error is not positive finite
   */
  static void validateAbsoluteError(double absoluteError) {
    if (Double.isNaN(absoluteError) || absoluteError < 0
        || absoluteError == Double.POSITIVE_INFINITY) {
      throw new IllegalArgumentException(
          "Absolute error must be positive finite but was: " + absoluteError);
    }
  }

  /**
   * Tests that two doubles are equal within an absolute error.
   *
   * <p>It is assumed the errors have been validated with {@link #validateAbsoluteError(double)}.
   *
   * <p>If either value is NaN this returns false.
   *
   * @param value1 The first value.
   * @param value2 The second value.
   * @param absoluteError The maximum absolute error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @return true if equal within an absolute error
   */
  static boolean testAreWithin(double value1, double value2, double absoluteError) {
    // Ignore NaNs. This is OK since if either number is a NaN the difference
    // will be NaN and we end up returning false.
    final double delta = Math.abs(value1 - value2);
    return (delta <= absoluteError);
  }

  /**
   * Tests that two doubles are close using a relative and absolute error. The relative error
   * between values {@code value1} and {@code value2} is relative to the largest magnitude of the
   * two values and the test is:
   *
   * <pre>
   * |value1-value2| <= max(|value1|, |value2|) * relativeError
   * </pre>
   *
   * <p>The test is symmetric for {@code value1} and {@code value2} and equivalent to testing
   * convergence of two values.
   *
   * <p>If either value is NaN or Infinity this returns false as the distance between the values is
   * Infinite or not valid.
   *
   * <p>Equality imposed by this method assumes the values are close and finite. This is not
   * consistent with {@link Double#equals(Object)} and, {@link Double#compare(double, double)}. For
   * example {@code -0} and {@code 0} are equal using an error of {@code 0}. {@code Double.NaN} and
   * {@code Double.NaN} are not equal.
   *
   * <p>Note: The relative error is a double since the relative error computation is performed in
   * double precision.
   *
   * @param value1 The first value.
   * @param value2 The second value.
   * @param relativeError The maximum relative error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @param absoluteError The maximum absolute error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @return true if close
   * @throws IllegalArgumentException If the relative error is not positive finite and below 2
   * @throws IllegalArgumentException If the absolute error is not positive finite
   */
  public static boolean areClose(double value1, double value2, double relativeError,
      double absoluteError) {
    validateAreClose(relativeError, absoluteError);
    return testAreClose(value1, value2, relativeError, absoluteError);
  }

  /**
   * Check the errors allow a test of {@code double} equality using a symmetric relative error.
   *
   * @param relativeError The maximum relative error
   * @param absoluteError The maximum absolute error
   * @throws IllegalArgumentException If the relative error is not positive finite and below 2
   * @throws IllegalArgumentException If the absolute error is not positive finite
   */
  static void validateAreClose(double relativeError, double absoluteError) {
    NumberEqualityUtils.validateSymmetricRelativeError(relativeError);
    validateAbsoluteError(absoluteError);
  }

  /**
   * Tests that two doubles are close using a relative and/or absolute error. The relative error
   * between values {@code value1} and {@code value2} is relative to the largest magnitude of the
   * two values.
   *
   * <p>If either value is NaN or Infinity this returns false as the distance between the values is
   * Infinite or not valid.
   *
   * <p>It is assumed the errors have been validated with {@link #validateAreClose(double, double)}.
   *
   * @param value1 The first value.
   * @param value2 The second value.
   * @param relativeError The maximum relative error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @param absoluteError The maximum absolute error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @return true if close
   */
  static boolean testAreClose(double value1, double value2, double relativeError,
      double absoluteError) {
    // Ignore NaNs. This is OK since if either number is a NaN the difference
    // will be NaN and we end up returning false.
    final double delta = Math.abs(value1 - value2);
    if (delta <= absoluteError) {
      return true;
    }
    // Compute using double precision
    return delta <= max(Math.abs(value1), Math.abs(value2)) * relativeError;
  }

  /**
   * Compute the maximum of two values without NaN checks.
   *
   * @param value1 first value
   * @param value2 second value
   * @return The maximum value
   */
  private static double max(final double value1, final double value2) {
    return (value1 >= value2) ? value1 : value2;
  }

  /**
   * Tests that a double is close to an expected value. The relative error between values
   * {@code expected} and {@code actual} is relative to the magnitude of {@code expected} and the
   * test is:
   *
   * <pre>
   * |expected-actual| <= |expected| * relativeError
   * </pre>
   *
   * <p>The test is asymmetric for {@code expected} and {@code actual}. The test is equivalent to
   * testing testing {@code actual} falls within a relative and/or absolute range of
   * {@code expected}.
   *
   * <p>If either value is NaN or Infinity this returns false as the distance between the values is
   * Infinite or not valid.
   *
   * <p>Equality imposed by this method assumes the values are close and finite. This is not
   * consistent with {@link Double#equals(Object)} and, {@link Double#compare(double, double)}. For
   * example {@code -0} and {@code 0} are equal using an error of {@code 0}. {@code Double.NaN} and
   * {@code Double.NaN} are not equal.
   *
   * <p>Note: The relative error is a double since the relative error computation is performed in
   * double precision.
   *
   * @param expected The expected value.
   * @param actual The actual value.
   * @param relativeError The maximum error, relative to <code>expected</code>, between
   *        <code>expected</code> and <code>actual</code> for which both numbers are still
   *        considered equal.
   * @param absoluteError The maximum absolute error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal.
   * @return true if actual is close to expected
   * @throws IllegalArgumentException If the relative error is not positive finite
   * @throws IllegalArgumentException If the absolute error is not positive finite
   */
  public static boolean isCloseTo(double expected, double actual, double relativeError,
      double absoluteError) {
    validateIsCloseTo(relativeError, absoluteError);
    return testIsCloseTo(expected, actual, relativeError, absoluteError);
  }

  /**
   * Check the errors allow a test of {@code double} equality using an asymmetric relative error.
   *
   * @param relativeError The maximum relative error
   * @param absoluteError The maximum absolute error
   * @throws IllegalArgumentException If the relative error is not positive finite
   * @throws IllegalArgumentException If the absolute error is not positive
   */
  static void validateIsCloseTo(double relativeError, double absoluteError) {
    NumberEqualityUtils.validateAsymmetricRelativeError(relativeError);
    validateAbsoluteError(absoluteError);
  }

  /**
   * Tests a double value is close to an expected value. The relative error between values
   * {@code expected} and {@code actual} is relative to the magnitude of {@code expected}.
   *
   * <p>If either value is NaN or Infinity this returns false as the distance between the values is
   * Infinite or not valid.
   *
   * <p>It is assumed the errors have been validated with
   * {@link #validateIsCloseTo(double, double)}.
   *
   * @param expected The expected value.
   * @param actual The actual value.
   * @param relativeError The maximum error, relative to <code>expected</code>, between
   *        <code>expected</code> and <code>actual</code> for which both numbers are still
   *        considered equal.
   * @param absoluteError The maximum absolute error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal.
   * @return true if actual is close to expected
   */
  static boolean testIsCloseTo(double expected, double actual, double relativeError,
      double absoluteError) {
    // Ignore NaNs. This is OK since if either number is a NaN the difference
    // will be NaN and we end up returning false.
    final double delta = Math.abs(expected - actual);
    if (delta <= absoluteError) {
      return true;
    }
    // Compute using double precision
    return delta <= Math.abs(expected) * relativeError;
  }
}
