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
 * Defines utilities for testing {@code float} equality.
 */
public final class FloatEquality {

  /**
   * Do not allow public construction.
   */
  private FloatEquality() {
    // No constructor
  }

  /**
   * Tests that two floats are equal.
   *
   * <p>Equality imposed by this method is consistent with {@link Float#equals(Object)} and,
   * {@link Float#compare(float, float)}.
   *
   * <p>For example {@code -0.0f} and {@code 0.0f} are not equal. {@code Float.NaN} and
   * {@code Float.NaN} are equal.
   *
   * @param value1 the first value
   * @param value2 the second value
   * @return true if equal
   */
  public static boolean areEqual(float value1, float value2) {
    return Float.floatToIntBits(value1) == Float.floatToIntBits(value2);
  }

  /**
   * Tests that two floats are equal within a ULP error.
   *
   * <p>This method counts the number of changes in the unit of least precision to adjust one value
   * to the other value. The values are considered equal if {@code count <= ulpError}. The sign of
   * zero is ignored, hence the method considers {@code -0.0f} and {@code 0.0f} to be equal.
   *
   * <p>If either value is NaN this returns false.
   *
   * @param value1 the first value
   * @param value2 the second value
   * @param ulpError the maximum ULP error between {@code value1} and {@code value2} for which both
   *        numbers are still considered equal.
   * @return true if within the error
   * @throws IllegalArgumentException if the ULP error is not positive
   */
  public static boolean areWithinUlp(float value1, float value2, short ulpError) {
    validateUlpError(ulpError);
    return testAreWithinUlp(value1, value2, ulpError);
  }

  /**
   * Check the error is positive.
   *
   * @param ulpError the maximum ULP error between two values
   * @throws IllegalArgumentException if the ULP error is not positive
   */
  static void validateUlpError(short ulpError) {
    if (ulpError < 0) {
      throw new IllegalArgumentException("ULP error must be positive but was: " + ulpError);
    }
  }

  /**
   * Tests that two floats are equal within a ULP error.
   *
   * <p>This method counts the number of changes in the unit of least precision to adjust one value
   * to the other value. The values are considered equal if {@code count <= ulpError}. The sign of
   * zero is ignored, hence the method considers {@code -0.0f} and {@code 0.0f} to be equal.
   *
   * <p>If either value is NaN this returns false.
   *
   * <p>It is assumed the errors have been validated with {@link #validateUlpError(short)}.
   *
   * @param value1 the first value
   * @param value2 the second value
   * @param ulpError the maximum ULP error between {@code value1} and {@code value2} for which both
   *        numbers are still considered equal.
   * @return true if equal within a ULP error
   */
  static boolean testAreWithinUlp(float value1, float value2, short ulpError) {
    // Note: Ignore NaNs only when equal within ulp error

    int a = Float.floatToRawIntBits(value1);
    int b = Float.floatToRawIntBits(value2);
    if ((a ^ b) < 0) {
      // Opposite signs. Count changes to zero, e.g.
      // ulp = Float.floatToRawIntBits(|value|) - Float.floatToRawIntBits(0.0)
      // Do this by removing the sign bit to get the ULP above positive zero.
      a &= Integer.MAX_VALUE;
      b &= Integer.MAX_VALUE;
      // Add the values and compare unsigned. Do this by adding 2^31 to both values.
      // See Integer.compareUnsigned.
      // Note:
      // If either value is NaN, the exponent bits are set to (255 << 24) and the
      // distance above 0.0 is always above a short ULP error. So omit the test
      // for NaN.
      return (a + b + Integer.MIN_VALUE) <= (ulpError + Integer.MIN_VALUE);
    }
    // Same sign, no overflow possible. Check for NaN last.
    return Math.abs(a - b) <= ulpError && !Float.isNaN(value1 + value2);
  }

  /**
   * Tests that two floats are equal within an absolute error.
   *
   * <p>If either value is NaN or Infinity this returns false as the distance between the values is
   * Infinite or not valid.
   *
   * <p>The difference between the values is computed using float precision.
   *
   * @param value1 the first value
   * @param value2 the second value
   * @param absoluteError the maximum absolute error between {@code value1} and {@code value2} for
   *        which both numbers are still considered equal.
   * @return true if within the error
   * @throws IllegalArgumentException if the absolute error is not positive and finite
   */
  public static boolean areWithin(float value1, float value2, float absoluteError) {
    validateAbsoluteError(absoluteError);
    return testAreWithin(value1, value2, absoluteError);
  }

  /**
   * Check the error is within the maximum difference between {@code float} primitives.
   *
   * @param absoluteError the maximum absolute error between two values
   * @throws IllegalArgumentException if the absolute error is not positive finite
   */
  static void validateAbsoluteError(float absoluteError) {
    if (Float.isNaN(absoluteError) || absoluteError < 0
        || absoluteError == Float.POSITIVE_INFINITY) {
      throw new IllegalArgumentException(
          "Absolute error must be positive finite but was: " + absoluteError);
    }
  }

  /**
   * Tests that two floats are equal within an absolute error.
   *
   * <p>If either value is NaN or Infinity this returns false as the distance between the values is
   * Infinite or not valid.
   *
   * <p>The difference between the values is computed using float precision.
   *
   * <p>It is assumed the errors have been validated with {@link #validateAbsoluteError(float)}.
   *
   * @param value1 the first value
   * @param value2 the second value
   * @param absoluteError the maximum absolute error between {@code value1} and {@code value2} for
   *        which both numbers are still considered equal.
   * @return true if equal within an absolute error
   */
  static boolean testAreWithin(float value1, float value2, float absoluteError) {
    // Ignore NaNs. This is OK since if either number is a NaN the difference
    // will be NaN and we end up returning false. An infinite value creates a difference
    // that is infinite or NaN and also returns false.
    final float delta = Math.abs(value1 - value2);
    return delta <= absoluteError;
  }

  /**
   * Tests that two floats are close using a relative and absolute error. The relative error between
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
   * <p>If either value is NaN or Infinity this returns false as the distance between the values is
   * Infinite or not valid.
   *
   * <p>The difference between the values is computed using float precision. The relative limit is
   * computed using double precision as {@code max(|value1|, |value2|) * relativeError}. These are
   * computed without overflow, i.e. as if rounded with an unbounded exponent.
   *
   * <p>Equality imposed by this method assumes the values are close and finite. This is not
   * consistent with {@link Float#equals(Object)} and, {@link Float#compare(float, float)}. For
   * example {@code -0} and {@code 0} are equal using an error of {@code 0}. {@code Float.NaN} and
   * {@code Float.NaN} are not equal.
   *
   * <p>Note: The relative error is a double to allow the error to be specified with more precision
   * than a float.
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
  public static boolean areClose(float value1, float value2, double relativeError,
      float absoluteError) {
    validateAreClose(relativeError, absoluteError);
    return testAreClose(value1, value2, relativeError, absoluteError);
  }

  /**
   * Check the errors allow a test of {@code float} equality using a symmetric relative error.
   *
   * @param relativeError the maximum relative error
   * @param absoluteError the maximum absolute error
   * @throws IllegalArgumentException if the relative error is not positive finite and below 2
   * @throws IllegalArgumentException if the absolute error is not positive finite
   */
  static void validateAreClose(double relativeError, float absoluteError) {
    NumberEquality.validateSymmetricRelativeError(relativeError);
    validateAbsoluteError(absoluteError);
  }

  /**
   * Tests that two floats are close using a relative and/or absolute error. The relative error
   * between values {@code value1} and {@code value2} is relative to the largest magnitude of the
   * two values.
   *
   * <p>If either value is NaN or Infinity this returns false as the distance between the values is
   * Infinite or not valid.
   *
   * <p>The difference between the values is computed using float precision. The relative limit is
   * computed using double precision as {@code max(|value1|, |value2|) * relativeError}. These are
   * computed without overflow, i.e. as if rounded with an unbounded exponent.
   *
   * <p>It is assumed the errors have been validated with {@link #validateAreClose(double, float)}.
   *
   * @param value1 the first value
   * @param value2 the second value
   * @param relativeError the maximum relative error between {@code value1} and {@code value2} for
   *        which both numbers are still considered equal.
   * @param absoluteError the maximum absolute error between {@code value1} and {@code value2} for
   *        which both numbers are still considered equal.
   * @return true if close
   */
  static boolean testAreClose(float value1, float value2, double relativeError,
      float absoluteError) {
    final float delta = Math.abs(value1 - value2);
    if (delta <= absoluteError) {
      return true;
    }
    return testRelative(value1, value2, delta, Math.max(Math.abs(value1), Math.abs(value2)),
        relativeError);
  }

  /**
   * Tests that a float is close to an expected value. The relative error between values
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
   * testing {@code actual} falls within a relative and/or absolute range of
   * {@code expected}.
   *
   * <p>If either value is NaN or Infinity this returns false as the distance between the values is
   * Infinite or not valid.
   *
   * <p>The difference between the values is computed using float precision. The relative limit is
   * computed using double precision as {@code |expected| * relativeError}. These are computed
   * without overflow, i.e. as if rounded with an unbounded exponent.
   *
   * <p>Equality imposed by this method assumes the values are close and finite. This is not
   * consistent with {@link Double#equals(Object)} and, {@link Double#compare(double, double)}. For
   * example {@code -0} and {@code 0} are equal using an error of {@code 0}. {@code Double.NaN} and
   * {@code Double.NaN} are not equal.
   *
   * <p>Note: The relative error is a double to allow the error to be specified with more precision
   * than a float.
   *
   * @param expected the expected value.
   * @param actual the actual value.
   * @param relativeError the maximum error, relative to {@code expected}, between {@code expected}
   *        and {@code actual} for which both numbers are still considered equal.
   * @param absoluteError the maximum absolute error between {@code expected} and {@code actual} for
   *        which both numbers are still considered equal.
   * @return true if actual is close to expected
   * @throws IllegalArgumentException if the relative error is not positive finite
   * @throws IllegalArgumentException if the absolute error is not positive finite
   */
  public static boolean isCloseTo(float expected, float actual, double relativeError,
      float absoluteError) {
    validateIsCloseTo(relativeError, absoluteError);
    return testIsCloseTo(expected, actual, relativeError, absoluteError);
  }

  /**
   * Check the errors allow a test of {@code float} equality using an asymmetric relative error.
   *
   * @param relativeError the maximum relative error
   * @param absoluteError the maximum absolute error
   * @throws IllegalArgumentException if the relative error is not positive finite
   * @throws IllegalArgumentException if the absolute error is not positive
   */
  static void validateIsCloseTo(double relativeError, float absoluteError) {
    NumberEquality.validateAsymmetricRelativeError(relativeError);
    validateAbsoluteError(absoluteError);
  }

  /**
   * Tests a float value is close to an expected value. The relative error between values
   * {@code expected} and {@code actual} is relative to the magnitude of {@code expected}.
   *
   * <p>If either value is NaN or Infinity this returns false as the distance between the values is
   * Infinite or not valid.
   *
   * <p>The difference between the values is computed using float precision. The relative limit is
   * computed using double precision as {@code |expected| * relativeError}. These are computed
   * without overflow, i.e. as if rounded with an unbounded exponent.
   *
   * <p>It is assumed the errors have been validated with
   * {@link #validateIsCloseTo(double, float)}.
   *
   * @param expected the expected value.
   * @param actual the actual value.
   * @param relativeError the maximum error, relative to {@code expected}, between {@code expected}
   *        and {@code actual} for which both numbers are still considered equal.
   * @param absoluteError the maximum absolute error between {@code expected} and {@code actual} for
   *        which both numbers are still considered equal.
   * @return true if actual is close to expected
   */
  static boolean testIsCloseTo(float expected, float actual, double relativeError,
      float absoluteError) {
    final float delta = Math.abs(expected - actual);
    if (delta <= absoluteError) {
      return true;
    }
    return testRelative(expected, actual, delta, Math.abs(expected), relativeError);
  }

  /**
   * Tests that {@code |value1-value2| <= magnitude * relativeError}. The difference between the
   * values is computed using float precision. The relative limit is computed using double
   * precision. These are computed without overflow, i.e. as if rounded with an unbounded exponent.
   *
   * <p>If either value is NaN or Infinity this returns false.
   *
   * @param value1 the first value
   * @param value2 the second value
   * @param delta the absolute difference {@code |value1-value2|} computed using float precision
   * @param magnitude the magnitude used to scale the relative error
   * @param relativeError the relative error
   * @return true if the difference is within the relative limit
   */
  private static boolean testRelative(float value1, float value2, float delta, float magnitude,
      double relativeError) {
    // The difference identifies non-finite values without testing each value:
    // - NaN if either value is NaN, or the values are infinite with the same sign
    // - Infinite if either value is infinite, or the difference of finite values overflows
    // - Finite only if both values are finite
    if (delta != Float.POSITIVE_INFINITY) {
      // Finite values: the magnitude is finite and the limit is not NaN.
      // An infinite limit (overflow) is above any finite difference.
      // A NaN difference fails the comparison and returns false.
      return delta <= magnitude * relativeError;
    }
    // Infinite difference. An infinite value cannot be close to any value. This must be
    // checked as the comparison below would otherwise compute infinity <= infinity, e.g.
    // isCloseTo with a finite expected value, an infinite actual value and a large
    // relative error.
    if (!Float.isFinite(value1) || !Float.isFinite(value2)) {
      return false;
    }
    // The difference of finite values overflows. The values have opposite signs and
    // magnitudes that can be halved exactly. Compare half the difference to half the limit.
    return Math.abs(value1 * 0.5f - value2 * 0.5f) <= magnitude * 0.5f * relativeError;
  }
}
