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
 * Defines utilities for testing numeric equality.
 */
public final class EqualityUtils {

  /** The maximum symmetric relative error. */
  private static final double MAX_RELATIVE_ERROR = 2;
  /**
   * The maximum absolute error between two {@code long} values.
   */
  public static final BigInteger MAX_LONG_ABS_ERROR =
      BigInteger.valueOf(Long.MAX_VALUE).subtract(BigInteger.valueOf(Long.MIN_VALUE));
  /**
   * The maximum absolute error between two {@code int} values.
   */
  public static final long MAX_INT_ABS_ERROR = 0xffffffffL;
  /**
   * The maximum absolute error between two {@code short} values.
   */
  public static final int MAX_SHORT_ABS_ERROR = 0xffff;
  /**
   * The maximum absolute error between two {@code byte} values.
   */
  public static final int MAX_BYTE_ABS_ERROR = 0xff;

  /**
   * Do not allow public construction.
   */
  private EqualityUtils() {
    // No constructor
  }

  ///////////////////////////////////////////////////////
  // Tests for binary equality of floating point values.
  // Supports: double, float
  ///////////////////////////////////////////////////////

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
   * Tests that two floats are equal.
   *
   * <p>Equality imposed by this method is consistent with {@link Float#equals(Object)} and,
   * {@link Float#compare(float, float)}.
   *
   * <p>For example {@code -0} and {@code 0} are not equal. {@code Float.NaN} and {@code Float.NaN}
   * are equal.
   *
   * @param value1 The first value.
   * @param value2 The second value.
   * @return true if equal
   */
  public static boolean areEqual(float value1, float value2) {
    return Float.floatToIntBits(value1) == Float.floatToIntBits(value2);
  }

  ///////////////////////////////////////////////////
  // Tests for equality within an absolute error.
  // Supports: double, float, long, int, short, byte
  ///////////////////////////////////////////////////

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
    doublesValidateAbsoluteError(absoluteError);
    return doublesTestWithin(value1, value2, absoluteError);
  }

  /**
   * Tests that two floats are equal within an absolute error.
   *
   * @param value1 The first value.
   * @param value2 The second value.
   * @param absoluteError The maximum absolute error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @return true if within the error
   * @throws IllegalArgumentException If the absolute error is not positive and finite
   */
  public static boolean areWithin(float value1, float value2, float absoluteError) {
    floatsValidateAbsoluteError(absoluteError);
    return floatsTestWithin(value1, value2, absoluteError);
  }

  /**
   * Tests that two longs are equal within an absolute error.
   *
   * @param value1 The first value.
   * @param value2 The second value.
   * @param absoluteError The maximum absolute error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @return true if within the error
   * @throws IllegalArgumentException If the absolute error is not positive or is <code>>=</code>
   *         than the maximum difference between byte primitives
   */
  public static boolean areWithin(long value1, long value2, BigInteger absoluteError) {
    longsValidateAbsoluteError(absoluteError);
    return longsTestWithin(value1, value2, absoluteError);
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
    longsValidateAbsoluteError(absoluteError);
    return longsTestWithin(value1, value2, absoluteError);
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
   * @throws IllegalArgumentException If the absolute error is not positive or is <code>>=</code>
   *         than the maximum difference between int primitives
   */
  public static boolean areWithin(int value1, int value2, long absoluteError) {
    intsValidateAbsoluteError(absoluteError);
    return intsTestWithin(value1, value2, absoluteError);
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
   * @return true if within the error
   * @throws IllegalArgumentException If the absolute error is not positive or is <code>>=</code>
   *         than the maximum difference between short primitives
   */
  public static boolean areWithin(short value1, short value2, int absoluteError) {
    shortsValidateAbsoluteError(absoluteError);
    return shortsTestWithin(value1, value2, absoluteError);
  }

  /**
   * Tests that two bytes are equal within an absolute error.
   *
   * <p>This test uses {@code int} arithmetic. If the absolute error is equal or above the maximum
   * difference between two {@code byte} values then it is not a valid test and an exception is
   * raised.
   *
   * @param value1 The first value.
   * @param value2 The second value.
   * @param absoluteError The maximum absolute error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @return true if within the tolerance
   * @throws IllegalArgumentException If the absolute error is not positive or is <code>>=</code>
   *         than the maximum difference between byte primitives
   */
  public static boolean areWithin(byte value1, byte value2, int absoluteError) {
    bytesValidateAbsoluteError(absoluteError);
    return bytesTestWithin(value1, value2, absoluteError);
  }

  /**
   * Check the error is within the maximum difference between {@code double} primitives.
   *
   * @param absoluteError The maximum absolute error between two values
   * @throws IllegalArgumentException If the absolute error is not positive finite
   */
  static void doublesValidateAbsoluteError(double absoluteError) {
    if (Double.isNaN(absoluteError) || absoluteError < 0
        || absoluteError == Double.POSITIVE_INFINITY) {
      throw new IllegalArgumentException(
          "Absolute error must be positive finite but was: " + absoluteError);
    }
  }

  /**
   * Check the error is within the maximum difference between {@code float} primitives.
   *
   * @param absoluteError The maximum absolute error between two values
   * @throws IllegalArgumentException If the absolute error is not positive finite
   */
  static void floatsValidateAbsoluteError(float absoluteError) {
    if (Float.isNaN(absoluteError) || absoluteError < 0
        || absoluteError == Float.POSITIVE_INFINITY) {
      throw new IllegalArgumentException(
          "Absolute error must be positive finite but was: " + absoluteError);
    }
  }

  /**
   * Check the error is within the maximum difference between {@code long} primitives.
   *
   * @param absoluteError The maximum absolute error between two values
   * @throws IllegalArgumentException If the absolute error is not positive or is <code>>=</code>
   *         than the maximum difference between long primitives
   */
  static void longsValidateAbsoluteError(BigInteger absoluteError) {
    if (absoluteError.signum() < 0 || absoluteError.compareTo(MAX_LONG_ABS_ERROR) >= 0) {
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
  static void longsValidateAbsoluteError(long absoluteError) {
    if (absoluteError < 0) {
      throw new IllegalArgumentException("Absolute error must be positive: " + absoluteError);
    }
  }

  /**
   * Check the error is within the maximum difference between {@code int} primitives.
   *
   * @param absoluteError The maximum absolute error between two values
   * @throws IllegalArgumentException If the absolute error is not positive or is <code>>=</code>
   *         than the maximum difference between int primitives
   */
  static void intsValidateAbsoluteError(long absoluteError) {
    if (absoluteError < 0 || absoluteError >= MAX_INT_ABS_ERROR) {
      throw new IllegalArgumentException(
          "Absolute error must be positive and less than the maximum difference for int values: "
              + absoluteError);
    }
  }

  /**
   * Check the error is within the maximum difference between {@code short} primitives.
   *
   * @param absoluteError The maximum absolute error between two values
   * @throws IllegalArgumentException If the absolute error is not positive or is <code>>=</code>
   *         than the maximum difference between short primitives
   */
  static void shortsValidateAbsoluteError(int absoluteError) {
    if (absoluteError < 0 || absoluteError >= MAX_SHORT_ABS_ERROR) {
      throw new IllegalArgumentException(
          "Absolute error must be positive and less than the maximum difference for short values: "
              + absoluteError);
    }
  }

  /**
   * Check the error is within the maximum difference between {@code byte} primitives.
   *
   * @param absoluteError The maximum absolute error between two values
   * @throws IllegalArgumentException If the absolute error is not positive or is <code>>=</code>
   *         than the maximum difference between byte primitives
   */
  static void bytesValidateAbsoluteError(int absoluteError) {
    if (absoluteError < 0 || absoluteError >= MAX_BYTE_ABS_ERROR) {
      throw new IllegalArgumentException(
          "Absolute error must be positive and less than the maximum difference for byte values: "
              + absoluteError);
    }
  }

  /**
   * Tests that two doubles are equal within an absolute error.
   *
   * <p>It is assumed the errors have been validated with
   * {@link #doublesValidateAbsoluteError(double)}.
   *
   * <p>If either value is NaN this returns false.
   *
   * @param value1 The first value.
   * @param value2 The second value.
   * @param absoluteError The maximum absolute error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @return true if equal within an absolute error
   */
  static boolean doublesTestWithin(double value1, double value2, double absoluteError) {
    // Ignore NaNs. This is OK since if either number is a NaN the difference
    // will be NaN and we end up returning false.
    final double delta = Math.abs(value1 - value2);
    return (delta <= absoluteError);
  }

  /**
   * Tests that two floats are equal within an absolute error.
   *
   * <p>It is assumed the errors have been validated with
   * {@link #floatsValidateAbsoluteError(float)}.
   *
   * <p>If either value is NaN this returns false.
   *
   * @param value1 The first value.
   * @param value2 The second value.
   * @param absoluteError The maximum absolute error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @return true if equal within an absolute error
   */
  static boolean floatsTestWithin(float value1, float value2, float absoluteError) {
    // Ignore NaNs. This is OK since if either number is a NaN the difference
    // will be NaN and we end up returning false.
    final float delta = Math.abs(value1 - value2);
    return (delta <= absoluteError);
  }

  /**
   * Tests that two longs are equal within an absolute error.
   *
   * <p>It is assumed the errors have been validated with
   * {@link #longsValidateAbsoluteError(BigInteger)}.
   *
   * @param value1 The first value.
   * @param value2 The second value.
   * @param absoluteError The maximum absolute error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @return true if equal within an absolute error
   */
  static boolean longsTestWithin(long value1, long value2, BigInteger absoluteError) {
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
    return longsTestWithin(value1, value2, absoluteError.longValue());
  }

  /**
   * Tests that two longs are equal within an absolute error.
   *
   * <p>It is assumed the errors have been validated with {@link #longsValidateAbsoluteError(long)}.
   *
   * @param value1 The first value.
   * @param value2 The second value.
   * @param absoluteError The maximum absolute error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @return true if equal within an absolute error
   */
  static boolean longsTestWithin(long value1, long value2, long absoluteError) {
    final long delta = (value1 > value2) ? value1 - value2 : value2 - value1;
    // Note: Also check delta for overflow. It should be positive.
    return (delta <= absoluteError && delta >= 0);
  }

  /**
   * Tests that two ints are equal within an absolute error.
   *
   * <p>It is assumed the errors have been validated with {@link #intsValidateAbsoluteError(long)}.
   *
   * @param value1 The first value.
   * @param value2 The second value.
   * @param absoluteError The maximum absolute error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @return true if equal within an absolute error
   */
  static boolean intsTestWithin(int value1, int value2, long absoluteError) {
    // Use long arithmetic
    final long delta = (value1 > value2) ? (long) value1 - value2 : (long) value2 - value1;
    return (delta <= absoluteError);
  }

  /**
   * Tests that two shorts are equal within an absolute error.
   *
   * <p>It is assumed the errors have been validated with {@link #shortsValidateAbsoluteError(int)}.
   *
   * <p>If either value is NaN this returns false.
   *
   * @param value1 The first value.
   * @param value2 The second value.
   * @param absoluteError The maximum absolute error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @return true if equal within an absolute error
   */
  static boolean shortsTestWithin(short value1, short value2, int absoluteError) {
    // short uses int arithmetic by default
    final int delta = (value1 > value2) ? value1 - value2 : value2 - value1;
    return (delta <= absoluteError);
  }

  /**
   * Tests that two bytes are equal within an absolute error.
   *
   * <p>It is assumed the errors have been validated with {@link #bytesValidateAbsoluteError(int)}.
   *
   * @param value1 The first value.
   * @param value2 The second value.
   * @param absoluteError The maximum absolute error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @return true if equal within an absolute error
   */
  static boolean bytesTestWithin(byte value1, byte value2, int absoluteError) {
    // byte uses int arithmetic by default
    final int delta = (value1 > value2) ? value1 - value2 : value2 - value1;
    return (delta <= absoluteError);
  }

  ////////////////////////////////////////////////////////////////////////
  // Tests for equality using a symmetric relative and/or absolute error.
  // Supports: double, float, long, int, short, byte
  ////////////////////////////////////////////////////////////////////////

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
    doublesValidateClose(relativeError, absoluteError);
    return doublesTestClose(value1, value2, relativeError, absoluteError);
  }

  /**
   * Tests that two floats are close using a relative and absolute error. The relative error between
   * values {@code value1} and {@code value2} is relative to the largest magnitude of the two values
   * and the test is:
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
   * consistent with {@link Float#equals(Object)} and, {@link Float#compare(float, float)}. For
   * example {@code -0} and {@code 0} are equal using an error of {@code 0}. {@code Float.NaN} and
   * {@code Float.NaN} are not equal.
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
  public static boolean areClose(float value1, float value2, double relativeError,
      float absoluteError) {
    floatsValidateClose(relativeError, absoluteError);
    return floatsTestClose(value1, value2, relativeError, absoluteError);
  }

  /**
   * Tests that two longs are close using a relative and absolute error. The relative error between
   * values {@code value1} and {@code value2} is relative to the largest magnitude of the two values
   * and the test is:
   *
   * <pre>
   * |value1-value2| <= max(|value1|, |value2|) * relativeError
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
    longsValidateClose(relativeError, absoluteError);
    return longsTestClose(value1, value2, relativeError, absoluteError);
  }

  /**
   * Tests that two ints are close using a relative and absolute error. The relative error between
   * values {@code value1} and {@code value2} is relative to the largest magnitude of the two values
   * and the test is:
   *
   * <pre>
   * |value1-value2| <= max(|value1|, |value2|) * relativeError
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
    intsValidateClose(relativeError, absoluteError);
    return intsTestClose(value1, value2, relativeError, absoluteError);
  }

  /**
   * Tests that two shorts are close using a relative and absolute error. The relative error between
   * values {@code value1} and {@code value2} is relative to the largest magnitude of the two values
   * and the test is:
   *
   * <pre>
   * |value1-value2| <= max(|value1|, |value2|) * relativeError
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
    shortsValidateClose(relativeError, absoluteError);
    return shortsTestClose(value1, value2, relativeError, absoluteError);
  }

  /**
   * Tests that two bytes are close using a relative and absolute error. The relative error between
   * values {@code value1} and {@code value2} is relative to the largest magnitude of the two values
   * and the test is:
   *
   * <pre>
   * |value1-value2| <= max(|value1|, |value2|) * relativeError
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
  public static boolean areClose(byte value1, byte value2, double relativeError,
      int absoluteError) {
    bytesValidateClose(relativeError, absoluteError);
    return bytesTestClose(value1, value2, relativeError, absoluteError);
  }

  /**
   * Validate the symmetric relative error.
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
   * Validate the asymmetric relative error.
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
   * Check the errors allow a test of {@code double} equality using a symmetric relative error.
   *
   * @param relativeError The maximum relative error
   * @param absoluteError The maximum absolute error
   * @throws IllegalArgumentException If the relative error is not positive finite and below 2
   * @throws IllegalArgumentException If the absolute error is not positive finite
   */
  static void doublesValidateClose(double relativeError, double absoluteError) {
    validateSymmetricRelativeError(relativeError);
    doublesValidateAbsoluteError(absoluteError);
  }

  /**
   * Check the errors allow a test of {@code float} equality using a symmetric relative error.
   *
   * @param relativeError The maximum relative error
   * @param absoluteError The maximum absolute error
   * @throws IllegalArgumentException If the relative error is not positive finite and below 2
   * @throws IllegalArgumentException If the absolute error is not positive finite
   */
  static void floatsValidateClose(double relativeError, float absoluteError) {
    validateSymmetricRelativeError(relativeError);
    floatsValidateAbsoluteError(absoluteError);
  }

  /**
   * Check the errors allow a test of {@code long} equality using a symmetric relative error.
   *
   * @param relativeError The maximum relative error
   * @param absoluteError The maximum absolute error
   * @throws IllegalArgumentException If the relative error is not positive finite and below 2
   * @throws IllegalArgumentException If the absolute error is not positive
   */
  static void longsValidateClose(double relativeError, long absoluteError) {
    validateSymmetricRelativeError(relativeError);
    longsValidateAbsoluteError(absoluteError);
  }

  /**
   * Check the errors allow a test of {@code int} equality using a symmetric relative error.
   *
   * @param relativeError The maximum relative error
   * @param absoluteError The maximum absolute error
   * @throws IllegalArgumentException If the relative error is not positive finite and below 2
   * @throws IllegalArgumentException If the absolute error is not positive
   */
  static void intsValidateClose(double relativeError, long absoluteError) {
    validateSymmetricRelativeError(relativeError);
    intsValidateAbsoluteError(absoluteError);
  }

  /**
   * Check the errors allow a test of {@code short} equality using a symmetric relative error.
   *
   * @param relativeError The maximum relative error
   * @param absoluteError The maximum absolute error
   * @throws IllegalArgumentException If the relative error is not positive finite and below 2
   * @throws IllegalArgumentException If the absolute error is not positive
   */
  static void shortsValidateClose(double relativeError, int absoluteError) {
    validateSymmetricRelativeError(relativeError);
    shortsValidateAbsoluteError(absoluteError);
  }

  /**
   * Check the errors allow a test of {@code byte} equality using a symmetric relative error.
   *
   * @param relativeError The maximum relative error
   * @param absoluteError The maximum absolute error
   * @throws IllegalArgumentException If the relative error is not positive finite and below 2
   * @throws IllegalArgumentException If the absolute error is not positive
   */
  static void bytesValidateClose(double relativeError, int absoluteError) {
    validateSymmetricRelativeError(relativeError);
    bytesValidateAbsoluteError(absoluteError);
  }

  /**
   * Tests that two doubles are close using a relative and/or absolute error. The relative error
   * between values {@code value1} and {@code value2} is relative to the largest magnitude of the
   * two values.
   *
   * <p>If either value is NaN or Infinity this returns false as the distance between the values is
   * Infinite or not valid.
   *
   * <p>It is assumed the errors have been validated with
   * {@link #doublesValidateClose(double, double)}.
   *
   * @param value1 The first value.
   * @param value2 The second value.
   * @param relativeError The maximum relative error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @param absoluteError The maximum absolute error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @return true if close
   */
  static boolean doublesTestClose(double value1, double value2, double relativeError,
      double absoluteError) {
    // Ignore NaNs. This is OK since if either number is a NaN the difference
    // will be NaN and we end up returning false.
    final double delta = Math.abs(value1 - value2);
    if (delta <= absoluteError) {
      return true;
    }
    return delta <= max(Math.abs(value1), Math.abs(value2)) * relativeError;
  }

  /**
   * Tests that two floats are close using a relative and/or absolute error. The relative error
   * between values {@code value1} and {@code value2} is relative to the largest magnitude of the
   * two values.
   *
   * <p>If either value is NaN or Infinity this returns false as the distance between the values is
   * Infinite or not valid.
   *
   * <p>It is assumed the errors have been validated with
   * {@link #floatsValidateClose(double, float)}.
   *
   * @param value1 The first value.
   * @param value2 The second value.
   * @param relativeError The maximum relative error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @param absoluteError The maximum absolute error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @return true if close
   */
  static boolean floatsTestClose(float value1, float value2, double relativeError,
      float absoluteError) {
    // Ignore NaNs. This is OK since if either number is a NaN the difference
    // will be NaN and we end up returning false.
    final float delta = Math.abs(value1 - value2);
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
   * Compute the maximum of two values without NaN checks.
   *
   * @param value1 first value
   * @param value2 second value
   * @return The maximum value
   */
  private static float max(final float value1, final float value2) {
    return (value1 >= value2) ? value1 : value2;
  }

  /**
   * Tests that two longs are close using a relative and/or absolute error. The relative error
   * between values {@code value1} and {@code value2} is relative to the largest magnitude of the
   * two values.
   *
   * <p>It is assumed the errors have been validated with {@link #longsValidateClose(double, long)}.
   *
   * @param value1 The first value.
   * @param value2 The second value.
   * @param relativeError The maximum relative error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @param absoluteError The maximum absolute error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @return true if close
   */
  static boolean longsTestClose(long value1, long value2, double relativeError,
      long absoluteError) {
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
   * Tests that two ints are close using a relative and/or absolute error. The relative error
   * between values {@code value1} and {@code value2} is relative to the largest magnitude of the
   * two values.
   *
   * <p>It is assumed the errors have been validated with {@link #intsValidateClose(double, long)}.
   *
   * @param value1 The first value.
   * @param value2 The second value.
   * @param relativeError The maximum relative error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @param absoluteError The maximum absolute error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @return true if close
   */
  static boolean intsTestClose(int value1, int value2, double relativeError, long absoluteError) {
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
   * Tests that two shorts are close using a relative and/or absolute error. The relative error
   * between values {@code value1} and {@code value2} is relative to the largest magnitude of the
   * two values.
   *
   * <p>It is assumed the errors have been validated with {@link #shortsValidateClose(double, int)}.
   *
   * @param value1 The first value.
   * @param value2 The second value.
   * @param relativeError The maximum relative error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @param absoluteError The maximum absolute error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @return true if close
   */
  static boolean shortsTestClose(short value1, short value2, double relativeError,
      int absoluteError) {
    return shortsOrBytesTestClose(value1, value2, relativeError, absoluteError);
  }

  /**
   * Tests that two bytes are close using a relative and/or absolute error. The relative error
   * between values {@code value1} and {@code value2} is relative to the largest magnitude of the
   * two values.
   *
   * <p>It is assumed the errors have been validated with {@link #bytesValidateClose(double, int)}.
   *
   * @param value1 The first value.
   * @param value2 The second value.
   * @param relativeError The maximum relative error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @param absoluteError The maximum absolute error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @return true if close
   */
  static boolean bytesTestClose(byte value1, byte value2, double relativeError, int absoluteError) {
    return shortsOrBytesTestClose(value1, value2, relativeError, absoluteError);
  }

  /**
   * Tests that two short/bytes are close using a relative and/or absolute error.
   *
   * <p>This is to be used when values are from byte or short primitives as no overflow issues are
   * handled.
   *
   * @param value1 The first value.
   * @param value2 The second value.
   * @param relativeError The maximum relative error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @param absoluteError The maximum absolute error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @return true if close
   */
  private static boolean shortsOrBytesTestClose(int value1, int value2, double relativeError,
      int absoluteError) {
    final int delta = (value1 > value2) ? value1 - value2 : value2 - value1;
    if (delta <= absoluteError) {
      return true;
    }
    // Compute using double precision.
    return delta <= Math.max(Math.abs(value1), Math.abs(value2)) * relativeError;
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

  //////////////////////////////////////////////////////////////////////////
  // Tests for equality using an asymmetric relative and/or absolute error.
  // Supports: double, float
  //////////////////////////////////////////////////////////////////////////

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
    doublesValidateIsCloseTo(relativeError, absoluteError);
    return doublesTestIsCloseTo(expected, actual, relativeError, absoluteError);
  }

  /**
   * Tests that a float is close to an expected value. The relative error between values
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
  public static boolean isCloseTo(float expected, float actual, double relativeError,
      float absoluteError) {
    floatsValidateIsCloseTo(relativeError, absoluteError);
    return floatsTestIsCloseTo(expected, actual, relativeError, absoluteError);
  }

  /**
   * Tests that a long is close to an expected value. The relative error between values
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
    longsValidateIsCloseTo(relativeError, absoluteError);
    return longsTestIsCloseTo(expected, actual, relativeError, absoluteError);
  }

  /**
   * Tests that an int is close to an expected value. The relative error between values
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
   * @param expected The expected value.
   * @param actual The actual value.
   * @param relativeError The maximum error, relative to <code>expected</code>, between
   *        <code>expected</code> and <code>actual</code> for which both numbers are still
   *        considered equal.
   * @param absoluteError The maximum absolute error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal.
   * @return true if actual is close to expected
   * @throws IllegalArgumentException If the relative error is not positive finite
   * @throws IllegalArgumentException If the absolute error is not positive or is <code>>=</code>
   *         than the maximum difference between int primitives
   */
  public static boolean isCloseTo(int expected, int actual, double relativeError,
      long absoluteError) {
    intsValidateIsCloseTo(relativeError, absoluteError);
    return intsTestIsCloseTo(expected, actual, relativeError, absoluteError);
  }

  /**
   * Tests that an short is close to an expected value. The relative error between values
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
   * @param expected The expected value.
   * @param actual The actual value.
   * @param relativeError The maximum error, relative to <code>expected</code>, between
   *        <code>expected</code> and <code>actual</code> for which both numbers are still
   *        considered equal.
   * @param absoluteError The maximum absolute error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal.
   * @return true if actual is close to expected
   * @throws IllegalArgumentException If the relative error is not positive finite
   * @throws IllegalArgumentException If the absolute error is not positive or is <code>>=</code>
   *         than the maximum difference between short primitives
   */
  public static boolean isCloseTo(short expected, short actual, double relativeError,
      int absoluteError) {
    shortsValidateIsCloseTo(relativeError, absoluteError);
    return shortsTestIsCloseTo(expected, actual, relativeError, absoluteError);
  }

  /**
   * Tests that an byte is close to an expected value. The relative error between values
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
   * @param expected The expected value.
   * @param actual The actual value.
   * @param relativeError The maximum error, relative to <code>expected</code>, between
   *        <code>expected</code> and <code>actual</code> for which both numbers are still
   *        considered equal.
   * @param absoluteError The maximum absolute error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal.
   * @return true if actual is close to expected
   * @throws IllegalArgumentException If the relative error is not positive finite
   * @throws IllegalArgumentException If the absolute error is not positive or is <code>>=</code>
   *         than the maximum difference between byte primitives
   */
  public static boolean isCloseTo(byte expected, byte actual, double relativeError,
      int absoluteError) {
    bytesValidateIsCloseTo(relativeError, absoluteError);
    return bytesTestIsCloseTo(expected, actual, relativeError, absoluteError);
  }

  /**
   * Check the errors allow a test of {@code double} equality using an asymmetric relative error.
   *
   * @param relativeError The maximum relative error
   * @param absoluteError The maximum absolute error
   * @throws IllegalArgumentException If the relative error is not positive finite
   * @throws IllegalArgumentException If the absolute error is not positive finite
   */
  static void doublesValidateIsCloseTo(double relativeError, double absoluteError) {
    validateAsymmetricRelativeError(relativeError);
    doublesValidateAbsoluteError(absoluteError);
  }

  /**
   * Check the errors allow a test of {@code float} equality using an asymmetric relative error.
   *
   * @param relativeError The maximum relative error
   * @param absoluteError The maximum absolute error
   * @throws IllegalArgumentException If the relative error is not positive finite
   * @throws IllegalArgumentException If the absolute error is not positive
   */
  static void floatsValidateIsCloseTo(double relativeError, float absoluteError) {
    validateAsymmetricRelativeError(relativeError);
    floatsValidateAbsoluteError(absoluteError);
  }

  /**
   * Check the errors allow a test of {@code long} equality using an asymmetric relative error.
   *
   * @param relativeError The maximum relative error
   * @param absoluteError The maximum absolute error
   * @throws IllegalArgumentException If the relative error is not positive finite
   * @throws IllegalArgumentException If the absolute error is not positive
   */
  static void longsValidateIsCloseTo(double relativeError, long absoluteError) {
    validateAsymmetricRelativeError(relativeError);
    longsValidateAbsoluteError(absoluteError);
  }

  /**
   * Check the errors allow a test of {@code int} equality using an asymmetric relative error.
   *
   * @param relativeError The maximum relative error
   * @param absoluteError The maximum absolute error
   * @throws IllegalArgumentException If the relative error is not positive finite
   * @throws IllegalArgumentException If the absolute error is not positive or is <code>>=</code>
   *         than the maximum difference between int primitives
   */
  static void intsValidateIsCloseTo(double relativeError, long absoluteError) {
    validateAsymmetricRelativeError(relativeError);
    intsValidateAbsoluteError(absoluteError);
  }

  /**
   * Check the errors allow a test of {@code short} equality using an asymmetric relative error.
   *
   * @param relativeError The maximum relative error
   * @param absoluteError The maximum absolute error
   * @throws IllegalArgumentException If the relative error is not positive finite
   * @throws IllegalArgumentException If the absolute error is not positive or is <code>>=</code>
   *         than the maximum difference between short primitives
   */
  static void shortsValidateIsCloseTo(double relativeError, int absoluteError) {
    validateAsymmetricRelativeError(relativeError);
    shortsValidateAbsoluteError(absoluteError);
  }

  /**
   * Check the errors allow a test of {@code byte} equality using an asymmetric relative error.
   *
   * @param relativeError The maximum relative error
   * @param absoluteError The maximum absolute error
   * @throws IllegalArgumentException If the relative error is not positive finite
   * @throws IllegalArgumentException If the absolute error is not positive or is <code>>=</code>
   *         than the maximum difference between byte primitives
   */
  static void bytesValidateIsCloseTo(double relativeError, int absoluteError) {
    validateAsymmetricRelativeError(relativeError);
    bytesValidateAbsoluteError(absoluteError);
  }

  /**
   * Tests a double value is close to an expected value. The relative error between values
   * {@code expected} and {@code actual} is relative to the magnitude of {@code expected}.
   *
   * <p>If either value is NaN or Infinity this returns false as the distance between the values is
   * Infinite or not valid.
   *
   * <p>It is assumed the errors have been validated with
   * {@link #doublesValidateIsCloseTo(double, double)}.
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
  static boolean doublesTestIsCloseTo(double expected, double actual, double relativeError,
      double absoluteError) {
    // Ignore NaNs. This is OK since if either number is a NaN the difference
    // will be NaN and we end up returning false.
    final double delta = Math.abs(expected - actual);
    if (delta <= absoluteError) {
      return true;
    }
    return delta <= Math.abs(expected) * relativeError;
  }

  /**
   * Tests a float value is close to an expected value. The relative error between values
   * {@code expected} and {@code actual} is relative to the magnitude of {@code expected}.
   *
   * <p>If either value is NaN or Infinity this returns false as the distance between the values is
   * Infinite or not valid.
   *
   * <p>It is assumed the errors have been validated with
   * {@link #floatsValidateIsCloseTo(double, float)}.
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
  static boolean floatsTestIsCloseTo(float expected, float actual, double relativeError,
      float absoluteError) {
    // Ignore NaNs. This is OK since if either number is a NaN the difference
    // will be NaN and we end up returning false.
    final float delta = Math.abs(expected - actual);
    if (delta <= absoluteError) {
      return true;
    }
    // Compute using double precision
    return delta <= Math.abs(expected) * relativeError;
  }

  /**
   * Tests a long value is close to an expected value. The relative error between values
   * {@code expected} and {@code actual} is relative to the magnitude of {@code expected}.
   *
   * <p>It is assumed the errors have been validated with
   * {@link #longsValidateIsCloseTo(double, long)}.
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
  static boolean longsTestIsCloseTo(long expected, long actual, double relativeError,
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

  /**
   * Tests an int value is close to an expected value. The relative error between values
   * {@code expected} and {@code actual} is relative to the magnitude of {@code expected}.
   *
   * <p>It is assumed the errors have been validated with
   * {@link #intsValidateIsCloseTo(double, long)}.
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
  static boolean intsTestIsCloseTo(int expected, int actual, double relativeError,
      long absoluteError) {
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

  /**
   * Tests a short value is close to an expected value. The relative error between values
   * {@code expected} and {@code actual} is relative to the magnitude of {@code expected}.
   *
   * <p>It is assumed the errors have been validated with
   * {@link #shortsValidateIsCloseTo(double, int)}.
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
  static boolean shortsTestIsCloseTo(short expected, short actual, double relativeError,
      int absoluteError) {
    return shortsOrBytesTestIsCloseTo(expected, actual, relativeError, absoluteError);
  }

  /**
   * Tests a byte value is close to an expected value. The relative error between values
   * {@code expected} and {@code actual} is relative to the magnitude of {@code expected}.
   *
   * <p>It is assumed the errors have been validated with
   * {@link #bytesValidateIsCloseTo(double, int)}.
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
  static boolean bytesTestIsCloseTo(byte expected, byte actual, double relativeError,
      int absoluteError) {
    return shortsOrBytesTestIsCloseTo(expected, actual, relativeError, absoluteError);
  }

  /**
   * Tests a short/byte value is close to an expected value. The relative error between values
   * {@code expected} and {@code actual} is relative to the magnitude of {@code expected}.
   *
   * <p>This is to be used when values are from byte or short primitives as no overflow issues are
   * handled.
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
  private static boolean shortsOrBytesTestIsCloseTo(int expected, int actual, double relativeError,
      int absoluteError) {
    final int delta = (expected > actual) ? expected - actual : actual - expected;
    if (delta <= absoluteError) {
      return true;
    }
    // Compute using double precision.
    return delta <= Math.abs(expected) * relativeError;
  }
}
