/*-
 * #%L
 * Genome Damage and Stability Centre Test API
 *
 * Contains a generic framework for test predicates.
 * %%
 * Copyright (C) 2018 - 2020 Alex Herbert
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
 * Defines utilities for testing numeric equality.
 */
final class NumberEqualityUtils {

  /** The maximum symmetric relative error. */
  private static final double MAX_RELATIVE_ERROR = 2;

  /**
   * Do not allow public construction.
   */
  private NumberEqualityUtils() {
    // No constructor
  }

  /**
   * Check the error is positive and less than the the maximum error.
   *
   * @param absoluteError The absolute error between two values
   * @param maximumDifference the maximum difference
   * @throws IllegalArgumentException If the absolute error is not positive or is <code>&lt;=</code>
   *         than the maximum difference
   */
  static void validateAbsoluteError(long absoluteError, long maximumDifference) {
    if (absoluteError < 0) {
      throw new IllegalArgumentException("Absolute error must be positive: " + absoluteError);
    }
    if (absoluteError >= maximumDifference) {
      throw new IllegalArgumentException("Absolute error must be less than the maximum difference: "
          + absoluteError + " >= " + maximumDifference);
    }
  }

  /**
   * Check the error is positive and less than the the maximum error.
   *
   * @param absoluteError The absolute error between two values
   * @param maximumDifference the maximum difference
   * @throws IllegalArgumentException If the absolute error is not positive or is <code>&lt;=</code>
   *         than the maximum difference
   */
  static void validateAbsoluteError(int absoluteError, int maximumDifference) {
    if (absoluteError < 0) {
      throw new IllegalArgumentException("Absolute error must be positive: " + absoluteError);
    }
    if (absoluteError >= maximumDifference) {
      throw new IllegalArgumentException("Absolute error must be less than the maximum difference: "
          + absoluteError + " >= " + maximumDifference);
    }
  }

  /**
   * Validate the symmetric relative error threshold. The relative error is computed using:
   *
   * <pre>
   * |value1-value2| / max(|value1|, |value2|)
   * </pre>
   *
   * <p>The maximum possible value is 2. The error threshold must be below this level otherwise it
   * cannot be used to test equality.
   *
   * @param relativeError The maximum relative error
   * @throws IllegalArgumentException If the relative error is not positive finite and below 2
   */
  static void validateSymmetricRelativeError(double relativeError) {
    if (negativeOrNaN(relativeError) || relativeError >= MAX_RELATIVE_ERROR) {
      throw new IllegalArgumentException(
          "Relative error must be positive and less than 2: " + relativeError);
    }
  }

  /**
   * Validate the asymmetric relative error threshold.
   *
   * @param relativeError The maximum relative error
   * @throws IllegalArgumentException If the relative error is not positive finite
   */
  static void validateAsymmetricRelativeError(double relativeError) {
    if (negativeOrNaN(relativeError) || relativeError == Double.POSITIVE_INFINITY) {
      throw new IllegalArgumentException(
          "Relative error must be positive finite: " + relativeError);
    }
  }

  /**
   * Test if negative or NaN.
   *
   * @param value the value
   * @return True if negative or NaN
   */
  private static boolean negativeOrNaN(final double value) {
    return (value < 0) || Double.isNaN(value);
  }

  /**
   * Tests that two shorts/bytes are equal within an absolute error.
   *
   * <p>This is to be used when values are from byte or short primitives as no overflow issues are
   * handled.
   *
   * <p>The error is assumed to be positive.
   *
   * @param value1 The first value.
   * @param value2 The second value.
   * @param absoluteError The maximum absolute error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @return true if equal within an absolute error
   */
  static boolean shortOrBytesTestAreWithin(int value1, int value2, int absoluteError) {
    final int delta = (value1 > value2) ? value1 - value2 : value2 - value1;
    return (delta <= absoluteError);
  }

  /**
   * Tests that two short/bytes are close using a relative and/or absolute error. The relative error
   * between values {@code value1} and {@code value2} is relative to the largest magnitude of the
   * two values.
   *
   * <p>This is to be used when values are from byte or short primitives as no overflow issues are
   * handled.
   *
   * <p>The error is assumed to be positive.
   *
   * @param value1 The first value.
   * @param value2 The second value.
   * @param relativeError The maximum relative error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @param absoluteError The maximum absolute error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @return true if close
   */
  static boolean shortsOrBytesTestAreClose(int value1, int value2, double relativeError,
      int absoluteError) {
    final int delta = (value1 > value2) ? value1 - value2 : value2 - value1;
    if (delta <= absoluteError) {
      return true;
    }
    // Compute using double precision.
    return delta <= Math.max(Math.abs(value1), Math.abs(value2)) * relativeError;
  }


  /**
   * Tests a short/byte value is close to an expected value. The relative error between values
   * {@code expected} and {@code actual} is relative to the magnitude of {@code expected}.
   *
   * <p>This is to be used when values are from byte or short primitives as no overflow issues are
   * handled.
   *
   * <p>The error is assumed to be positive.
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
  static boolean shortsOrBytesTestIsCloseTo(int expected, int actual, double relativeError,
      int absoluteError) {
    final int delta = (expected > actual) ? expected - actual : actual - expected;
    if (delta <= absoluteError) {
      return true;
    }
    // Compute using double precision.
    return delta <= Math.abs(expected) * relativeError;
  }
}
