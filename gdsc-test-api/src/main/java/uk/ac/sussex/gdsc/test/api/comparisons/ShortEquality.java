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
 * Defines utilities for testing {@code short} equality.
 */
public final class ShortEquality {

  /**
   * The maximum absolute error between two {@code short} values.
   */
  public static final int MAX_ABS_ERROR = 0xffff;

  /**
   * Do not allow public construction.
   */
  private ShortEquality() {
    // No constructor
  }

  /**
   * Tests that two shorts are equal.
   *
   * @param value1 The first value.
   * @param value2 The second value.
   * @return true if equal
   */
  public static boolean areEqual(short value1, short value2) {
    return value1 == value2;
  }

  /**
   * Tests that two shorts are equal within an absolute error.
   *
   * <p>This test uses {@code int} arithmetic. If the absolute error is equal or above the maximum
   * difference between two {@code short} values then it is not a valid test and an exception is
   * raised.
   *
   * @param value1 The first value.
   * @param value2 The second value.
   * @param absoluteError The maximum absolute error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @return true if within the tolerance
   * @throws IllegalArgumentException If the absolute error is not positive or is <code>&lt;=</code>
   *         than the maximum difference between short primitives
   */
  public static boolean areWithin(short value1, short value2, int absoluteError) {
    validateAbsoluteError(absoluteError);
    return testAreWithin(value1, value2, absoluteError);
  }

  /**
   * Check the error is within the maximum difference between {@code short} primitives.
   *
   * @param absoluteError The maximum absolute error between two values
   * @throws IllegalArgumentException If the absolute error is not positive or is <code>&lt;=</code>
   *         than the maximum difference between short primitives
   */
  static void validateAbsoluteError(int absoluteError) {
    NumberEquality.validateAbsoluteError(absoluteError, MAX_ABS_ERROR);
  }

  /**
   * Tests that two shorts are equal within an absolute error.
   *
   * <p>It is assumed the errors have been validated with {@link #validateAbsoluteError(int)}.
   *
   * @param value1 The first value.
   * @param value2 The second value.
   * @param absoluteError The maximum absolute error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @return true if equal within an absolute error
   */
  static boolean testAreWithin(short value1, short value2, int absoluteError) {
    return NumberEquality.shortOrBytesTestAreWithin(value1, value2, absoluteError);
  }

  /**
   * Tests that two shorts are close using a relative and absolute error. The relative error between
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
  public static boolean areClose(short value1, short value2, double relativeError,
      int absoluteError) {
    validateAreClose(relativeError, absoluteError);
    return testAreClose(value1, value2, relativeError, absoluteError);
  }

  /**
   * Check the errors allow a test of {@code short} equality using a symmetric relative error.
   *
   * @param relativeError The maximum relative error
   * @param absoluteError The maximum absolute error
   * @throws IllegalArgumentException If the relative error is not positive finite and below 2
   * @throws IllegalArgumentException If the absolute error is not positive
   */
  static void validateAreClose(double relativeError, int absoluteError) {
    NumberEquality.validateSymmetricRelativeError(relativeError);
    validateAbsoluteError(absoluteError);
  }

  /**
   * Tests that two shorts are close using a relative and/or absolute error. The relative error
   * between values {@code value1} and {@code value2} is relative to the largest magnitude of the
   * two values.
   *
   * <p>It is assumed the errors have been validated with {@link #validateAreClose(double, int)}.
   *
   * @param value1 The first value.
   * @param value2 The second value.
   * @param relativeError The maximum relative error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @param absoluteError The maximum absolute error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @return true if close
   */
  static boolean testAreClose(short value1, short value2, double relativeError, int absoluteError) {
    return NumberEquality.shortsOrBytesTestAreClose(value1, value2, relativeError,
        absoluteError);
  }

  /**
   * Tests that an short is close to an expected value. The relative error between values
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
   *         than the maximum difference between short primitives
   */
  public static boolean isCloseTo(short expected, short actual, double relativeError,
      int absoluteError) {
    validateIsCloseTo(relativeError, absoluteError);
    return testIsCloseTo(expected, actual, relativeError, absoluteError);
  }

  /**
   * Check the errors allow a test of {@code short} equality using an asymmetric relative error.
   *
   * @param relativeError The maximum relative error
   * @param absoluteError The maximum absolute error
   * @throws IllegalArgumentException If the relative error is not positive finite
   * @throws IllegalArgumentException If the absolute error is not positive or is <code>&lt;=</code>
   *         than the maximum difference between short primitives
   */
  static void validateIsCloseTo(double relativeError, int absoluteError) {
    NumberEquality.validateAsymmetricRelativeError(relativeError);
    validateAbsoluteError(absoluteError);
  }

  /**
   * Tests a short value is close to an expected value. The relative error between values
   * {@code expected} and {@code actual} is relative to the magnitude of {@code expected}.
   *
   * <p>It is assumed the errors have been validated with {@link #validateIsCloseTo(double, int)}.
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
  static boolean testIsCloseTo(short expected, short actual, double relativeError,
      int absoluteError) {
    return NumberEquality.shortsOrBytesTestIsCloseTo(expected, actual, relativeError,
        absoluteError);
  }
}
