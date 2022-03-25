/*-
 * #%L
 * Genome Damage and Stability Centre Test API
 *
 * Contains a generic framework for test predicates.
 * %%
 * Copyright (C) 2018 - 2022 Alex Herbert
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package uk.ac.sussex.gdsc.test.api.comparisons;

/**
 * Defines utilities for testing numeric equality.
 */
final class NumberEquality {

  /** The maximum symmetric relative error. */
  private static final double MAX_RELATIVE_ERROR = 2;

  /**
   * Do not allow public construction.
   */
  private NumberEquality() {
    // No constructor
  }

  /**
   * Check the error is positive and less than the the maximum error.
   *
   * @param absoluteError the absolute error between two values
   * @param maximumDifference the maximum difference
   * @throws IllegalArgumentException if the absolute error is not positive or is {@code <=}
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
   * @param absoluteError the absolute error between two values
   * @param maximumDifference the maximum difference
   * @throws IllegalArgumentException if the absolute error is not positive or is {@code <=}
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
   * @param relativeError the maximum relative error
   * @throws IllegalArgumentException if the relative error is not positive finite and below 2
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
   * @param relativeError the maximum relative error
   * @throws IllegalArgumentException if the relative error is not positive finite
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
   * @param value1 the first value
   * @param value2 the second value
   * @param absoluteError the maximum absolute error between {@code value1} and
   *        {@code value2} for which both numbers are still considered equal.
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
   * @param value1 the first value
   * @param value2 the second value
   * @param relativeError the maximum relative error between {@code value1} and
   *        {@code value2} for which both numbers are still considered equal.
   * @param absoluteError the maximum absolute error between {@code value1} and
   *        {@code value2} for which both numbers are still considered equal.
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
   * @param expected the expected value.
   * @param actual the actual value.
   * @param relativeError the maximum error, relative to {@code expected}, between
   *        {@code expected} and {@code actual} for which both numbers are still
   *        considered equal.
   * @param absoluteError the maximum absolute error between {@code expected} and
   *        {@code actual} for which both numbers are still considered equal.
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
