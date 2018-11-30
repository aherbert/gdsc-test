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

import java.math.BigInteger;

/**
 * Defines utilities for testing {@code long} equality.
 */
public final class LongEqualityUtils {

  /**
   * The maximum absolute error between two {@code long} values.
   */
  public static final BigInteger MAX_ABS_ERROR =
      BigInteger.valueOf(Long.MAX_VALUE).subtract(BigInteger.valueOf(Long.MIN_VALUE));

  /**
   * Do not allow public construction.
   */
  private LongEqualityUtils() {
    // No constructor
  }

  /**
   * Tests that two longs are equal.
   *
   * @param value1 The first value.
   * @param value2 The second value.
   * @return true if equal
   */
  public static boolean areEqual(long value1, long value2) {
    return value1 == value2;
  }

  /**
   * Tests that two longs are equal within an absolute error.
   *
   * @param value1 The first value.
   * @param value2 The second value.
   * @param absoluteError The maximum absolute error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @return true if within the error
   * @throws IllegalArgumentException If the absolute error is not positive or is <code>&lt;=</code>
   *         than the maximum difference between byte primitives
   */
  public static boolean areWithin(long value1, long value2, BigInteger absoluteError) {
    validateAbsoluteError(absoluteError);
    return testAreWithin(value1, value2, absoluteError);
  }

  /**
   * Tests that two longs are equal within an absolute error.
   *
   * <p>This test does not use {@link BigInteger} arithmetic so is restricted to testing differences
   * up to {@link Long#MAX_VALUE}. Larger differences cause overflow and return false. These can be
   * tested using {@link #areWithin(long, long, BigInteger)}.
   *
   * @param value1 The first value.
   * @param value2 The second value.
   * @param absoluteError The maximum absolute error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @return true if within the error
   * @throws IllegalArgumentException If the absolute error is not positive
   * @see #areWithin(long, long, BigInteger)
   */
  public static boolean areWithin(long value1, long value2, long absoluteError) {
    validateAbsoluteError(absoluteError);
    return testAreWithin(value1, value2, absoluteError);
  }

  /**
   * Check the error is within the maximum difference between {@code long} primitives.
   *
   * @param absoluteError The maximum absolute error between two values
   * @throws IllegalArgumentException If the absolute error is not positive or is <code>&lt;=</code>
   *         than the maximum difference between long primitives
   */
  static void validateAbsoluteError(BigInteger absoluteError) {
    if (absoluteError.signum() < 0 || absoluteError.compareTo(MAX_ABS_ERROR) >= 0) {
      throw new IllegalArgumentException(
          "Absolute error must be positive and less than the maximum difference for long values: "
              + absoluteError);
    }
  }

  /**
   * Check the error is within the maximum difference between {@code long} primitives.
   *
   * @param absoluteError The maximum absolute error between two values
   * @throws IllegalArgumentException If the absolute error is not positive
   */
  static void validateAbsoluteError(long absoluteError) {
    if (absoluteError < 0) {
      throw new IllegalArgumentException("Absolute error must be positive: " + absoluteError);
    }
  }

  /**
   * Tests that two longs are equal within an absolute error.
   *
   * <p>It is assumed the errors have been validated with
   * {@link #validateAbsoluteError(BigInteger)}.
   *
   * @param value1 The first value.
   * @param value2 The second value.
   * @param absoluteError The maximum absolute error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @return true if equal within an absolute error
   */
  static boolean testAreWithin(long value1, long value2, BigInteger absoluteError) {
    // bitlength() excludes the sign bit. Note: This method assumes the error is positive.
    // If the bit length is above 63 then it cannot be a single long.
    if (absoluteError.bitLength() >= Long.SIZE) {
      // Error is too large for a long so use BigInteger
      final BigInteger delta = (value1 > value2)
          // value1 - value2
          ? BigInteger.valueOf(value1).subtract(BigInteger.valueOf(value2))
          // value2 - value1
          : BigInteger.valueOf(value2).subtract(BigInteger.valueOf(value1));
      return (delta.compareTo(absoluteError) <= 0);
    }
    return testAreWithin(value1, value2, absoluteError.longValue());
  }

  /**
   * Tests that two longs are equal within an absolute error.
   *
   * <p>It is assumed the errors have been validated with {@link #validateAbsoluteError(long)}.
   *
   * @param value1 The first value.
   * @param value2 The second value.
   * @param absoluteError The maximum absolute error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @return true if equal within an absolute error
   */
  static boolean testAreWithin(long value1, long value2, long absoluteError) {
    final long delta = (value1 > value2) ? value1 - value2 : value2 - value1;
    // Note: Also check delta for overflow. It should be positive.
    return (delta <= absoluteError && delta >= 0);
  }

  /**
   * Tests that two longs are close using a relative and absolute error. The relative error between
   * values {@code value1} and {@code value2} is relative to the largest magnitude of the two values
   * and the test is:
   *
   * <pre>
   * {@code
   * |value1-value2| <= max(|value1|, |value2|) * relativeError
   * }
   * </pre>
   *
   * <p>The test is symmetric for {@code value1} and {@code value2} and equivalent to testing
   * convergence of two values.
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
  public static boolean areClose(long value1, long value2, double relativeError,
      long absoluteError) {
    validateAreClose(relativeError, absoluteError);
    return testAreClose(value1, value2, relativeError, absoluteError);
  }

  /**
   * Check the errors allow a test of {@code long} equality using a symmetric relative error.
   *
   * @param relativeError The maximum relative error
   * @param absoluteError The maximum absolute error
   * @throws IllegalArgumentException If the relative error is not positive finite and below 2
   * @throws IllegalArgumentException If the absolute error is not positive
   */
  static void validateAreClose(double relativeError, long absoluteError) {
    NumberEqualityUtils.validateSymmetricRelativeError(relativeError);
    validateAbsoluteError(absoluteError);
  }

  /**
   * Tests that two longs are close using a relative and/or absolute error. The relative error
   * between values {@code value1} and {@code value2} is relative to the largest magnitude of the
   * two values.
   *
   * <p>It is assumed the errors have been validated with {@link #validateAreClose(double, long)}.
   *
   * @param value1 The first value.
   * @param value2 The second value.
   * @param relativeError The maximum relative error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @param absoluteError The maximum absolute error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @return true if close
   */
  static boolean testAreClose(long value1, long value2, double relativeError, long absoluteError) {
    final long delta = (value1 > value2) ? value1 - value2 : value2 - value1;
    // Check delta for overflow. It should be positive.
    if (delta < 0) {
      return false;
    }
    if (delta <= absoluteError) {
      return true;
    }
    // Compute using double precision the equivalent of:
    // delta <= Math.max(Math.abs(value1), Math.abs(value2)) * relativeError.
    // Note that Math.abs(long) will be negative for Long.MIN_VALUE so
    // use negative abs instead which holds all magnitudes.
    // This is then negated afterwards to achieve the absolute relative tolerance.
    return delta <= 0.0 - (Math.min(negativeAbs(value1), negativeAbs(value2)) * relativeError);
  }

  /**
   * Return the negative of the absolute value.
   *
   * <p>This exists as the largest negative value for signed integers is bigger than the positive
   * value.
   *
   * @param value the value
   * @return the same magnitude as value but negative
   */
  private static long negativeAbs(long value) {
    return (value <= 0) ? value : -value;
  }

  /**
   * Tests that a long is close to an expected value. The relative error between values
   * {@code expected} and {@code actual} is relative to the magnitude of {@code expected} and the
   * test is:
   *
   * <pre>
   * {@code
   * |expected-actual| <= |expected| * relativeError
   * }
   * </pre>
   *
   * <p>The test is asymmetric for {@code expected} and {@code actual}. The test is equivalent to
   * testing testing {@code actual} falls within a relative and/or absolute range of
   * {@code expected}.
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
   * @throws IllegalArgumentException If the absolute error is not positive
   */
  public static boolean isCloseTo(long expected, long actual, double relativeError,
      long absoluteError) {
    validateIsCloseTo(relativeError, absoluteError);
    return testIsCloseTo(expected, actual, relativeError, absoluteError);
  }

  /**
   * Check the errors allow a test of {@code long} equality using an asymmetric relative error.
   *
   * @param relativeError The maximum relative error
   * @param absoluteError The maximum absolute error
   * @throws IllegalArgumentException If the relative error is not positive finite
   * @throws IllegalArgumentException If the absolute error is not positive
   */
  static void validateIsCloseTo(double relativeError, long absoluteError) {
    NumberEqualityUtils.validateAsymmetricRelativeError(relativeError);
    validateAbsoluteError(absoluteError);
  }

  /**
   * Tests a long value is close to an expected value. The relative error between values
   * {@code expected} and {@code actual} is relative to the magnitude of {@code expected}.
   *
   * <p>It is assumed the errors have been validated with {@link #validateIsCloseTo(double, long)}.
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
  static boolean testIsCloseTo(long expected, long actual, double relativeError,
      long absoluteError) {
    final long delta = (expected > actual) ? expected - actual : actual - expected;
    // Check delta for overflow. It should be positive.
    if (delta < 0) {
      return false;
    }
    if (delta <= absoluteError) {
      return true;
    }
    // Compute using double precision.
    // Note that Math.abs(long) will be negative for Long.MIN_VALUE so abs
    // is used after the conversion to double.
    return delta <= Math.abs(expected * relativeError);
  }
}
