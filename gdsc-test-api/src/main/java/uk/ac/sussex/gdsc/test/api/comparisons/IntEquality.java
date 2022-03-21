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
 * Defines utilities for testing {@code int} equality.
 */
public final class IntEquality {

  /**
   * The maximum absolute error between two {@code int} values.
   */
  public static final long MAX_ABS_ERROR = 0xffffffffL;

  /**
   * Do not allow public construction.
   */
  private IntEquality() {
    // No constructor
  }

  /**
   * Tests that two ints are equal.
   *
   * @param value1 The first value.
   * @param value2 The second value.
   * @return true if equal
   */
  public static boolean areEqual(int value1, int value2) {
    return value1 == value2;
  }

  /**
   * Tests that two ints are equal within an absolute error.
   *
   * <p>This test uses {@code long} arithmetic. If the absolute error is equal or above the maximum
   * difference between two {@code int} values then it is not a valid test and an exception is
   * raised.
   *
   * @param value1 The first value.
   * @param value2 The second value.
   * @param absoluteError The maximum absolute error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @return true if within the error
   * @throws IllegalArgumentException If the absolute error is not positive or is <code>&lt;=</code>
   *         than the maximum difference between int primitives
   */
  public static boolean areWithin(int value1, int value2, long absoluteError) {
    validateAbsoluteError(absoluteError);
    return testAreWithin(value1, value2, absoluteError);
  }

  /**
   * Check the error is within the maximum difference between {@code int} primitives.
   *
   * @param absoluteError The maximum absolute error between two values
   * @throws IllegalArgumentException If the absolute error is not positive or is <code>&lt;=</code>
   *         than the maximum difference between int primitives
   */
  static void validateAbsoluteError(long absoluteError) {
    NumberEquality.validateAbsoluteError(absoluteError, MAX_ABS_ERROR);
  }

  /**
   * Tests that two ints are equal within an absolute error.
   *
   * <p>It is assumed the errors have been validated with {@link #validateAbsoluteError(long)}.
   *
   * @param value1 The first value.
   * @param value2 The second value.
   * @param absoluteError The maximum absolute error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @return true if equal within an absolute error
   */
  static boolean testAreWithin(int value1, int value2, long absoluteError) {
    // Use long arithmetic
    final long delta = (value1 > value2) ? (long) value1 - value2 : (long) value2 - value1;
    return (delta <= absoluteError);
  }

  /**
   * Tests that two ints are close using a relative and absolute error. The relative error between
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
  public static boolean areClose(int value1, int value2, double relativeError, long absoluteError) {
    validateAreClose(relativeError, absoluteError);
    return testAreClose(value1, value2, relativeError, absoluteError);
  }

  /**
   * Check the errors allow a test of {@code int} equality using a symmetric relative error.
   *
   * @param relativeError The maximum relative error
   * @param absoluteError The maximum absolute error
   * @throws IllegalArgumentException If the relative error is not positive finite and below 2
   * @throws IllegalArgumentException If the absolute error is not positive
   */
  static void validateAreClose(double relativeError, long absoluteError) {
    NumberEquality.validateSymmetricRelativeError(relativeError);
    validateAbsoluteError(absoluteError);
  }

  /**
   * Tests that two ints are close using a relative and/or absolute error. The relative error
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
  static boolean testAreClose(int value1, int value2, double relativeError, long absoluteError) {
    // Use long arithmetic
    final long delta = (value1 > value2) ? (long) value1 - value2 : (long) value2 - value1;
    if (delta <= absoluteError) {
      return true;
    }
    // Compute using double precision the equivalent of:
    // delta <= Math.max(Math.abs(value1), Math.abs(value2)) * relativeError.
    // Note that Math.abs(int) will be negative for Integer.MIN_VALUE so
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
  private static int negativeAbs(int value) {
    return (value <= 0) ? value : -value;
  }

  /**
   * Tests that an int is close to an expected value. The relative error between values
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
   * @throws IllegalArgumentException If the absolute error is not positive or is <code>&lt;=</code>
   *         than the maximum difference between int primitives
   */
  public static boolean isCloseTo(int expected, int actual, double relativeError,
      long absoluteError) {
    validateIsCloseTo(relativeError, absoluteError);
    return testIsCloseTo(expected, actual, relativeError, absoluteError);
  }

  /**
   * Check the errors allow a test of {@code int} equality using an asymmetric relative error.
   *
   * @param relativeError The maximum relative error
   * @param absoluteError The maximum absolute error
   * @throws IllegalArgumentException If the relative error is not positive finite
   * @throws IllegalArgumentException If the absolute error is not positive or is <code>&lt;=</code>
   *         than the maximum difference between int primitives
   */
  static void validateIsCloseTo(double relativeError, long absoluteError) {
    NumberEquality.validateAsymmetricRelativeError(relativeError);
    validateAbsoluteError(absoluteError);
  }

  /**
   * Tests an int value is close to an expected value. The relative error between values
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
  static boolean testIsCloseTo(int expected, int actual, double relativeError, long absoluteError) {
    // Use long arithmetic
    final long delta = (expected > actual) ? (long) expected - actual : (long) actual - expected;
    if (delta <= absoluteError) {
      return true;
    }
    // Compute using double precision.
    // Note that Math.abs(int) will be negative for Integer.MIN_VALUE so abs
    // is used after the conversion to double.
    return delta <= Math.abs(expected * relativeError);
  }
}
