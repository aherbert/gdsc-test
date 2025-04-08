/*-
 * #%L
 * Genome Damage and Stability Centre Test API
 *
 * Contains a generic framework for test predicates.
 * %%
 * Copyright (C) 2018 - 2025 Alex Herbert
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
 * Defines utilities for testing {@code byte} equality.
 */
public final class ByteEquality {

  /**
   * The maximum absolute error between two {@code byte} values.
   */
  public static final int MAX_ABS_ERROR = 0xff;

  /**
   * Do not allow public construction.
   */
  private ByteEquality() {
    // No constructor
  }

  /**
   * Tests that two bytes are equal.
   *
   * @param value1 the first value
   * @param value2 the second value
   * @return true if equal
   */
  public static boolean areEqual(byte value1, byte value2) {
    return value1 == value2;
  }

  /**
   * Tests that two bytes are equal within an absolute error.
   *
   * <p>This test uses {@code int} arithmetic. If the absolute error is equal or above the maximum
   * difference between two {@code byte} values then it is not a valid test and an exception is
   * raised.
   *
   * @param value1 the first value
   * @param value2 the second value
   * @param absoluteError the maximum absolute error between {@code value1} and {@code value2} for
   *        which both numbers are still considered equal.
   * @return true if within the tolerance
   * @throws IllegalArgumentException if the absolute error is not positive or is {@code <=} than
   *         the maximum difference between byte primitives
   */
  public static boolean areWithin(byte value1, byte value2, int absoluteError) {
    validateAbsoluteError(absoluteError);
    return testAreWithin(value1, value2, absoluteError);
  }

  /**
   * Check the error is within the maximum difference between {@code byte} primitives.
   *
   * @param absoluteError the maximum absolute error between two values
   * @throws IllegalArgumentException if the absolute error is not positive or is {@code <=} than
   *         the maximum difference between byte primitives
   */
  static void validateAbsoluteError(int absoluteError) {
    NumberEquality.validateAbsoluteError(absoluteError, MAX_ABS_ERROR);
  }

  /**
   * Tests that two bytes are equal within an absolute error.
   *
   * <p>It is assumed the errors have been validated with {@link #validateAbsoluteError(int)}.
   *
   * @param value1 the first value
   * @param value2 the second value
   * @param absoluteError the maximum absolute error between {@code value1} and {@code value2} for
   *        which both numbers are still considered equal.
   * @return true if equal within an absolute error
   */
  static boolean testAreWithin(byte value1, byte value2, int absoluteError) {
    return NumberEquality.shortOrBytesTestAreWithin(value1, value2, absoluteError);
  }

  /**
   * Tests that two bytes are close using a relative and absolute error. The relative error between
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
   * @param value1 the first value
   * @param value2 the second value
   * @param relativeError the maximum relative error between {@code value1} and {@code value2} for
   *        which both numbers are still considered equal.
   * @param absoluteError the maximum absolute error between {@code value1} and {@code value2} for
   *        which both numbers are still considered equal.
   * @return true if close
   * @throws IllegalArgumentException if the relative error is not positive finite and below 2
   * @throws IllegalArgumentException if the absolute error is not positive finite
   */
  public static boolean areClose(byte value1, byte value2, double relativeError,
      int absoluteError) {
    validateAreClose(relativeError, absoluteError);
    return testAreClose(value1, value2, relativeError, absoluteError);
  }

  /**
   * Check the errors allow a test of {@code byte} equality using a symmetric relative error.
   *
   * @param relativeError the maximum relative error
   * @param absoluteError the maximum absolute error
   * @throws IllegalArgumentException if the relative error is not positive finite and below 2
   * @throws IllegalArgumentException if the absolute error is not positive
   */
  static void validateAreClose(double relativeError, int absoluteError) {
    NumberEquality.validateSymmetricRelativeError(relativeError);
    validateAbsoluteError(absoluteError);
  }

  /**
   * Tests that two bytes are close using a relative and/or absolute error. The relative error
   * between values {@code value1} and {@code value2} is relative to the largest magnitude of the
   * two values.
   *
   * <p>It is assumed the errors have been validated with {@link #validateAreClose(double, int)}.
   *
   * @param value1 the first value
   * @param value2 the second value
   * @param relativeError the maximum relative error between {@code value1} and {@code value2} for
   *        which both numbers are still considered equal.
   * @param absoluteError the maximum absolute error between {@code value1} and {@code value2} for
   *        which both numbers are still considered equal.
   * @return true if close
   */
  static boolean testAreClose(byte value1, byte value2, double relativeError, int absoluteError) {
    return NumberEquality.shortsOrBytesTestAreClose(value1, value2, relativeError, absoluteError);
  }

  /**
   * Tests that an byte is close to an expected value. The relative error between values
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
   * @param expected the expected value.
   * @param actual the actual value.
   * @param relativeError the maximum error, relative to {@code expected}, between {@code expected}
   *        and {@code actual} for which both numbers are still considered equal.
   * @param absoluteError the maximum absolute error between {@code expected} and {@code actual} for
   *        which both numbers are still considered equal.
   * @return true if actual is close to expected
   * @throws IllegalArgumentException if the relative error is not positive finite
   * @throws IllegalArgumentException if the absolute error is not positive or is {@code <=} than
   *         the maximum difference between byte primitives
   */
  public static boolean isCloseTo(byte expected, byte actual, double relativeError,
      int absoluteError) {
    validateIsCloseTo(relativeError, absoluteError);
    return testIsCloseTo(expected, actual, relativeError, absoluteError);
  }

  /**
   * Check the errors allow a test of {@code byte} equality using an asymmetric relative error.
   *
   * @param relativeError the maximum relative error
   * @param absoluteError the maximum absolute error
   * @throws IllegalArgumentException if the relative error is not positive finite
   * @throws IllegalArgumentException if the absolute error is not positive or is {@code <=} than
   *         the maximum difference between byte primitives
   */
  static void validateIsCloseTo(double relativeError, int absoluteError) {
    NumberEquality.validateAsymmetricRelativeError(relativeError);
    validateAbsoluteError(absoluteError);
  }

  /**
   * Tests a byte value is close to an expected value. The relative error between values
   * {@code expected} and {@code actual} is relative to the magnitude of {@code expected}.
   *
   * <p>It is assumed the errors have been validated with {@link #validateIsCloseTo(double, int)}.
   *
   * @param expected the expected value.
   * @param actual the actual value.
   * @param relativeError the maximum error, relative to {@code expected}, between {@code expected}
   *        and {@code actual} for which both numbers are still considered equal.
   * @param absoluteError the maximum absolute error between {@code expected} and {@code actual} for
   *        which both numbers are still considered equal.
   * @return true if actual is close to expected
   */
  static boolean testIsCloseTo(byte expected, byte actual, double relativeError,
      int absoluteError) {
    return NumberEquality.shortsOrBytesTestIsCloseTo(expected, actual, relativeError,
        absoluteError);
  }
}
