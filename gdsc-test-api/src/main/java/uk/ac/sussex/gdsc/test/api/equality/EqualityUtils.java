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

package uk.ac.sussex.gdsc.test.api.equality;

import java.math.BigInteger;

/**
 * Defines utilities for testing numeric equality.
 */
public final class EqualityUtils {

  /** Constant used to ignore the relative error. */
  public static final double IGNORE_RELATIVE_ERROR = -1;
  /** Constant used to ignore the double absolute error. */
  public static final double IGNORE_DOUBLE_ABSOLUTE_ERROR = -1;
  /** Constant used to ignore the float absolute error. */
  public static final float IGNORE_FLOAT_ABSOLUTE_ERROR = -1;
  /** Constant for the maximum relative error. */
  private static final double MAX_RELATIVE_ERROR = 2;
  /**
   * Constant for the maximum absolute error between two {@code int} values.
   */
  public static final long MAX_INT_ABS_ERROR = 0xffffffffL;
  /**
   * Constant for the maximum absolute error between two {@code short} values.
   */
  public static final int MAX_SHORT_ABS_ERROR = 0xffff;
  /**
   * Constant for the maximum absolute error between two {@code byte} values.
   */
  public static final int MAX_BYTE_ABS_ERROR = 0xff;
  /**
   * Constant for the description of absolution error {@code <=}.
   */
  private static final String DESCRIPTION_REL_ERROR_LTE = "Rel.Error <= ";
  /**
   * Constant for the description of absolution error {@code <=}.
   */
  private static final String DESCRIPTION_ABS_ERROR_LTE = "Abs.Error <= ";
  /** Constant for the description of absolution error equals 0. */
  private static final String DESCRIPTION_ABS_ERROR_0 = "Abs.Error == 0";

  /**
   * Do not allow public construction.
   */
  private EqualityUtils() {
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
   * @param expected The expected value.
   * @param actual The actual value.
   * @return true if equal
   */
  public static boolean doublesAreEqual(double expected, double actual) {
    return Double.doubleToLongBits(expected) == Double.doubleToLongBits(actual);
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
   * @param expected The expected value.
   * @param actual The actual value.
   * @return true if equal
   */
  public static boolean floatsAreEqual(float expected, float actual) {
    return Float.floatToIntBits(expected) == Float.floatToIntBits(actual);
  }

  /**
   * Tests that two doubles are almost equal.
   *
   * <p>Setting the relative error to negative tests equality using only absolute error.
   *
   * <p>Setting the absolute error to negative tests equality using only relative error.
   *
   * <p>If both errors are disabled an exception is thrown since no equality test is performed.
   *
   * <p>Equality imposed by this method assumes the values are close and finite. This is not
   * consistent with {@link Double#equals(Object)} and, {@link Double#compare(double, double)}. For
   * example {@code -0} and {@code 0} are equal. {@code Double.NaN} and {@code Double.NaN} are not
   * equal.
   *
   * <p>Note: The relative error is a double since the relative error computation is performed in
   * double precision.
   *
   * @param expected The expected value.
   * @param actual The actual value.
   * @param relativeError The maximum relative error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal. Ignored if set to
   *        negative.
   * @param absoluteError The maximum absolute error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal. Ignored if set to
   *        negative.
   * @return true if equal
   * @throws IllegalArgumentException If both tolerances are ignored (i.e. no equality test is
   *         performed)
   */
  public static boolean doublesAreAlmostEqual(double expected, double actual, double relativeError,
      double absoluteError) {
    doublesValidateAlmostEqual(relativeError, absoluteError);
    return doublesTestAlmostEqual(expected, actual, relativeError, absoluteError);
  }

  /**
   * Tests that two floats are almost equal.
   *
   * <p>Setting the relative error to negative tests equality using only absolute error.
   *
   * <p>Setting the absolute error to negative tests equality using only relative error.
   *
   * <p>If both errors are disabled an exception is thrown since no equality test is performed.
   *
   * <p>Equality imposed by this method assumes the values are close and finite. This is not
   * consistent with {@link Float#equals(Object)} and, {@link Float#compare(float, float)}. For
   * example {@code -0} and {@code 0} are equal. {@code Float.NaN} and {@code Float.NaN} are not
   * equal.
   *
   * <p>Note: The relative error is a double since the relative error computation is performed in
   * double precision.
   *
   * @param expected The expected value.
   * @param actual The actual value.
   * @param relativeError The maximum relative error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal. Ignored if set to
   *        negative.
   * @param absoluteError The maximum absolute error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal. Ignored if set to
   *        negative.
   * @return true if equal
   * @throws IllegalArgumentException If both tolerances are ignored (i.e. no equality test is
   *         performed)
   */
  public static boolean floatsAreAlmostEqual(float expected, float actual, double relativeError,
      float absoluteError) {
    floatsValidateAlmostEqual(relativeError, absoluteError);
    return floatsTestAlmostEqual(expected, actual, relativeError, absoluteError);
  }

  /**
   * Check the errors allow a test of {@code double} equality.
   *
   * <p>Setting the relative error to negative tests equality using only absolute error.
   *
   * <p>Setting the absolute error to negative tests equality using only relative error.
   *
   * <p>If both errors are disabled an exception is thrown since no equality test is performed.
   *
   * @param relativeError The maximum relative error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal. Ignored if set to
   *        negative.
   * @param absoluteError The maximum absolute error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal. Ignored if set to
   *        negative.
   * @throws IllegalArgumentException If the relative error is not below 2
   * @throws IllegalArgumentException If the absolute error is not finite
   * @throws IllegalArgumentException If both tolerances are ignored (i.e. no equality test is
   *         performed)
   */
  static void doublesValidateAlmostEqual(double relativeError, double absoluteError) {
    if (relativeError < 0 && absoluteError < 0) {
      throw new IllegalArgumentException("No valid equality criteria");
    }
    if (relativeError >= MAX_RELATIVE_ERROR) {
      throw new IllegalArgumentException(
          "Relative error must be less than 2 but was: " + relativeError);
    }
    if (!Double.isFinite(absoluteError)) {
      throw new IllegalArgumentException("Absolute error must be finite but was: " + absoluteError);
    }
  }

  /**
   * Check the errors allow a test of equality.
   *
   * <p>Setting the relative error to negative tests equality using only absolute error.
   *
   * <p>Setting the absolute error to negative tests equality using only relative error.
   *
   * <p>If both errors are disabled an exception is thrown since no equality test is performed.
   *
   * @param relativeError The maximum relative error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal. Ignored if set to
   *        negative.
   * @param absoluteError The maximum absolute error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal. Ignored if set to
   *        negative.
   * @throws IllegalArgumentException If the relative error is not below 2
   * @throws IllegalArgumentException If the absolute error is not finite
   * @throws IllegalArgumentException If both tolerances are ignored (i.e. no equality test is
   *         performed)
   */
  static void floatsValidateAlmostEqual(double relativeError, float absoluteError) {
    if (relativeError < 0 && absoluteError < 0) {
      throw new IllegalArgumentException("No valid equality criteria");
    }
    if (relativeError >= MAX_RELATIVE_ERROR) {
      throw new IllegalArgumentException(
          "Relative error must be less than 2 but was: " + relativeError);
    }
    if (!Float.isFinite(absoluteError)) {
      throw new IllegalArgumentException("Absolute error must be finite but was: " + absoluteError);
    }
  }

  /**
   * Tests that two doubles are almost equal.
   *
   * <p>If either value is NaN this returns false.
   *
   * @param expected The expected value.
   * @param actual The actual value.
   * @param relativeError The maximum relative error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal. Ignored if set to
   *        negative.
   * @param absoluteError The maximum absolute error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal. Ignored if set to
   *        negative.
   * @return true if equal
   */
  static boolean doublesTestAlmostEqual(double expected, double actual, double relativeError,
      double absoluteError) {
    // Ignore NaNs. This is OK since if either number is a NaN the difference
    // will be NaN and we end up returning false.
    final double delta = Math.abs(expected - actual);
    if (delta <= absoluteError) {
      return true;
    }
    final double max = doublesMax(Math.abs(expected), Math.abs(actual));
    return (delta / max <= relativeError);
  }

  /**
   * Tests that two floats are almost equal.
   *
   * <p>If either value is NaN this returns false.
   *
   * @param expected The expected value.
   * @param actual The actual value.
   * @param relativeError The maximum relative error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal. Ignored if set to
   *        negative.
   * @param absoluteError The maximum absolute error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal. Ignored if set to
   *        negative.
   * @return true if equal
   */
  static boolean floatsTestAlmostEqual(float expected, float actual, double relativeError,
      float absoluteError) {
    // Ignore NaNs. This is OK since if either number is a NaN the difference
    // will be NaN and we end up returning false.
    final float delta = Math.abs(expected - actual);
    if (delta <= absoluteError) {
      return true;
    }
    // Compute using double precision
    final double max = floatsMax(Math.abs(expected), Math.abs(actual));
    return (delta / max <= relativeError);
  }

  /**
   * Compute the maximum of two values without NaN checks.
   *
   * @param value1 first value
   * @param value2 second value
   * @return b if a is lesser or equal to b, a otherwise
   */
  private static double doublesMax(final double value1, final double value2) {
    return (value1 > value2) ? value1 : value2;
  }

  /**
   * Compute the maximum of two values without NaN checks.
   *
   * @param value1 first value
   * @param value2 second value
   * @return b if a is lesser or equal to b, a otherwise
   */
  private static float floatsMax(final float value1, final float value2) {
    return (value1 > value2) ? value1 : value2;
  }

  //@formatter:off
  /**
   * Get the description of the test that two doubles are almost equal.
   *
   * <p>It is assumed the errors have been validated with
   * {@link #doublesValidateAlmostEqual(double, double)}.
   *
   * <p>Note the special cases:
   *
   * <ul>
   * <li>When the relative error is zero this is equivalent to an absolute error of zero.
   * If the absolute error was negative it will be set to zero. The resulting description just
   * contains the absolute error.
   * <li>When the absolute error is zero this is effectively ignored if the relative error
   * is above zero. The resulting description just contains the relative error.
   * </ul>
   *
   * @param relativeError The maximum relative error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal. Ignored if set to
   *        negative.
   * @param absoluteError The maximum absolute error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal. Ignored if set to
   *        negative.
   * @return the description
   */
  //@formatter:on
  static String doublesGetDescriptionAlmostEqual(double relativeError, double absoluteError) {
    // Assume either, or both, are >= 0

    // Handle special cases where the test was actual equivalent to something different.
    double testRelativeError = relativeError;
    double testAbsoluteError = absoluteError;

    // 1. A relative error of 0 is the equivalent of absolute error 0.
    if (testRelativeError == 0) {
      testRelativeError = IGNORE_RELATIVE_ERROR;
      if (testAbsoluteError < 0) {
        testAbsoluteError = 0;
      }
    }
    // 2. An absolute error of 0 is not used if the relative error is >0
    if (testAbsoluteError == 0 && testRelativeError > 0) {
      testAbsoluteError = IGNORE_DOUBLE_ABSOLUTE_ERROR;
    }

    // Build the description
    final StringBuilder sb = new StringBuilder();
    if (testRelativeError > 0) {
      // This is always <=
      sb.append(DESCRIPTION_REL_ERROR_LTE).append(testRelativeError);
    }
    if (testAbsoluteError >= 0) {
      // Add combined operator
      if (sb.length() != 0) {
        sb.append(" || ");
      }
      // This may be == or <=
      if (testAbsoluteError == 0) {
        sb.append(DESCRIPTION_ABS_ERROR_0);
      } else {
        sb.append(DESCRIPTION_ABS_ERROR_LTE);
        sb.append(testAbsoluteError);
      }
    }

    return sb.toString();
  }

  //@formatter:off
  /**
   * Get the description of the test that two doubles are almost equal.
   *
   * <p>It is assumed the error shave been validated with {@link #floatsValidateAlmostEqual(double, float)}.
   *
   * <p>Note the special cases:
   *
   * <ul>
   * <li>When the relative error is zero this is equivalent to an absolute error of zero.
   * If the absolute error was negative it will be set to zero. The resulting description just
   * contains the absolute error.
   * <li>When the absolute error is zero this is effectively ignored if the relative error
   * is above zero. The resulting description just contains the relative error.
   * </ul>
   *
   * @param relativeError The maximum relative error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal. Ignored if set to
   *        negative.
   * @param absoluteError The maximum absolute error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal. Ignored if set to
   *        negative.
   * @return the description
   */
  //@formatter:on
  static String floatsGetDescriptionAlmostEqual(double relativeError, float absoluteError) {
    // Assume either, or both, are >= 0

    // Handle special cases where the test was actual equivalent to something different.
    double testRelativeError = relativeError;
    float testAbsoluteError = absoluteError;

    // 1. A relative error of 0 is the equivalent of absolute error 0.
    if (testRelativeError == 0) {
      testRelativeError = IGNORE_RELATIVE_ERROR;
      if (testAbsoluteError < 0) {
        testAbsoluteError = 0;
      }
    }
    // 2. An absolute error of 0 is not used if the relative error is >0
    if (testAbsoluteError == 0 && testRelativeError > 0) {
      testAbsoluteError = IGNORE_FLOAT_ABSOLUTE_ERROR;
    }

    // Build the description
    final StringBuilder sb = new StringBuilder();
    if (testRelativeError > 0) {
      // This is always <=
      sb.append(DESCRIPTION_REL_ERROR_LTE).append(testRelativeError);
    }
    if (testAbsoluteError >= 0) {
      // Add combined operator
      if (sb.length() != 0) {
        sb.append(" || ");
      }
      // This may be == or <=
      if (testAbsoluteError == 0) {
        sb.append(DESCRIPTION_ABS_ERROR_0);
      } else {
        sb.append(DESCRIPTION_ABS_ERROR_LTE);
        sb.append(testAbsoluteError);
      }
    }

    return sb.toString();
  }

  /**
   * Tests that two doubles are equal within an absolute error.
   *
   * @param expected The expected value.
   * @param actual The actual value.
   * @param absoluteError The maximum absolute error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal.
   * @return true if within the error
   * @throws IllegalArgumentException If the absolute error is not positive and finite
   */
  public static boolean doublesAreWithin(double expected, double actual, double absoluteError) {
    doublesValidateWithin(absoluteError);
    return doublesTestWithin(expected, actual, absoluteError);
  }

  /**
   * Tests that two floats are equal within an absolute error.
   *
   * @param expected The expected value.
   * @param actual The actual value.
   * @param absoluteError The maximum absolute error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal.
   * @return true if within the error
   * @throws IllegalArgumentException If the absolute error is not positive and finite
   */
  public static boolean floatsAreWithin(float expected, float actual, float absoluteError) {
    floatsValidateWithin(absoluteError);
    return floatsTestWithin(expected, actual, absoluteError);
  }

  /**
   * Tests that two longs are equal within an absolute error.
   *
   * <p>This test does not use {@link BigInteger} arithmetic so is restricted to testing differences
   * up to {@link Long#MAX_VALUE}.
   *
   * @param expected The expected value.
   * @param actual The actual value.
   * @param absoluteError The maximum absolute error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal.
   * @return true if within the error
   * @throws IllegalArgumentException If the absolute error is not positive
   */
  public static boolean longsAreWithin(long expected, long actual, long absoluteError) {
    longsValidateWithin(absoluteError);
    return longsTestWithin(expected, actual, absoluteError);
  }

  /**
   * Tests that two ints are equal within an absolute error.
   *
   * <p>This test uses {@code long} arithmetic. If the absolute error is equal or above the maximum
   * difference between two {@code int} values then it is not a valid test and an exception is
   * raised.
   *
   * @param expected The expected value.
   * @param actual The actual value.
   * @param absoluteError The maximum absolute error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal.
   * @return true if within the error
   * @throws IllegalArgumentException If the absolute error is not positive or is <code>>=</code>
   *         than the maximum difference between int primitives
   */
  public static boolean intsAreWithin(int expected, int actual, long absoluteError) {
    intsValidateWithin(absoluteError);
    return intsTestWithin(expected, actual, absoluteError);
  }

  /**
   * Tests that two shorts are equal within an absolute error.
   *
   * <p>This test uses {@code int} arithmetic. If the absolute error is equal or above the maximum
   * difference between two {@code short} values then it is not a valid test and an exception is
   * raised.
   *
   * @param expected The expected value.
   * @param actual The actual value.
   * @param absoluteError The maximum absolute error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal.
   * @return true if within the error
   * @throws IllegalArgumentException If the absolute error is not positive or is <code>>=</code>
   *         than the maximum difference between short primitives
   */
  public static boolean shortsAreWithin(short expected, short actual, int absoluteError) {
    shortsValidateWithin(absoluteError);
    return shortsTestWithin(expected, actual, absoluteError);
  }

  /**
   * Tests that two bytes are equal within an absolute error.
   *
   * <p>This test uses {@code int} arithmetic. If the absolute error is equal or above the maximum
   * difference between two {@code byte} values then it is not a valid test and an exception is
   * raised.
   *
   * @param expected The expected value.
   * @param actual The actual value.
   * @param absoluteError The maximum absolute error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal.
   * @return true if within the tolerance
   * @throws IllegalArgumentException If the absolute error is not positive or is <code>>=</code>
   *         than the maximum difference between byte primitives
   */
  public static boolean bytesAreWithin(byte expected, byte actual, int absoluteError) {
    bytesValidateWithin(absoluteError);
    return bytesTestWithin(expected, actual, absoluteError);
  }

  /**
   * Check the error allows a test of {@code double} equality.
   *
   * @param absoluteError The maximum absolute error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal.
   * @throws IllegalArgumentException If the absolute error is not positive and finite
   */
  static void doublesValidateWithin(double absoluteError) {
    if (absoluteError < 0 || !Double.isFinite(absoluteError)) {
      throw new IllegalArgumentException(
          "Absolute error must be positive finite but was: " + absoluteError);
    }
  }

  /**
   * Check the error allows a test of {@code float} equality.
   *
   * @param absoluteError The maximum absolute error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal.
   * @throws IllegalArgumentException If the absolute error is not positive and finite
   */
  static void floatsValidateWithin(float absoluteError) {
    if (absoluteError < 0 || !Float.isFinite(absoluteError)) {
      throw new IllegalArgumentException(
          "Absolute error must be positive finite but was: " + absoluteError);
    }
  }

  /**
   * Check the error allows a test of {@code long} equality.
   *
   * @param absoluteError The maximum absolute error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal.
   * @throws IllegalArgumentException If the absolute error is not positive
   */
  static void longsValidateWithin(long absoluteError) {
    if (absoluteError < 0) {
      throw new IllegalArgumentException(
          "Absolute error must be positive but was: " + absoluteError);
    }
  }

  /**
   * Check the error allows a test of {@code int} equality.
   *
   * @param absoluteError The maximum absolute error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal.
   * @throws IllegalArgumentException If the absolute error is not positive or is <code>>=</code>
   *         than the maximum difference between int primitives
   * @see #MAX_INT_ABS_ERROR
   */
  static void intsValidateWithin(long absoluteError) {
    if (absoluteError < 0) {
      throw new IllegalArgumentException(
          "Absolute error must be positive but was: " + absoluteError);
    }
    if (absoluteError >= MAX_INT_ABS_ERROR) {
      throw new IllegalArgumentException(
          "Absolute error must be less than the maximum difference for integer values: "
              + absoluteError + " >= " + MAX_INT_ABS_ERROR);
    }
  }

  /**
   * Check the error allows a test of {@code short} equality.
   *
   * @param absoluteError The maximum absolute error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal.
   * @throws IllegalArgumentException If the absolute error is not positive or is <code>>=</code>
   *         than the maximum difference between short primitives
   * @see #MAX_SHORT_ABS_ERROR
   */
  static void shortsValidateWithin(int absoluteError) {
    if (absoluteError < 0) {
      throw new IllegalArgumentException(
          "Absolute error must be positive but was: " + absoluteError);
    }
    if (absoluteError >= MAX_SHORT_ABS_ERROR) {
      throw new IllegalArgumentException(
          "Absolute error must be less than the maximum difference for short integer values: "
              + absoluteError + " >= " + MAX_SHORT_ABS_ERROR);
    }
  }

  /**
   * Check the error allows a test of {@code byte} equality.
   *
   * @param absoluteError The maximum absolute error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal.
   * @throws IllegalArgumentException If the absolute error is not positive or is <code>>=</code>
   *         than the maximum difference between byte primitives
   * @see #MAX_BYTE_ABS_ERROR
   */
  static void bytesValidateWithin(int absoluteError) {
    if (absoluteError < 0) {
      throw new IllegalArgumentException(
          "Absolute error must be positive but was: " + absoluteError);
    }
    if (absoluteError >= MAX_BYTE_ABS_ERROR) {
      throw new IllegalArgumentException(
          "Absolute error must be less than the maximum difference for byte values: "
              + absoluteError + " >= " + MAX_BYTE_ABS_ERROR);
    }
  }

  /**
   * Tests that two doubles are equal within an absolute tolerance.
   *
   * <p>If either value is NaN this returns false.
   *
   * @param expected The expected value.
   * @param actual The actual value.
   * @param absoluteError The maximum absolute error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal.
   * @return true if equal
   */
  static boolean doublesTestWithin(double expected, double actual, double absoluteError) {
    // Ignore NaNs. This is OK since if either number is a NaN the difference
    // will be NaN and we end up returning false.
    final double delta = Math.abs(expected - actual);
    return (delta <= absoluteError);
  }

  /**
   * Tests that two floats are equal within an absolute tolerance.
   *
   * <p>If either value is NaN this returns false.
   *
   * @param expected The expected value.
   * @param actual The actual value.
   * @param absoluteError The maximum absolute error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal.
   * @return true if equal
   */
  static boolean floatsTestWithin(float expected, float actual, float absoluteError) {
    // Ignore NaNs. This is OK since if either number is a NaN the difference
    // will be NaN and we end up returning false.
    final float delta = Math.abs(expected - actual);
    return (delta <= absoluteError);
  }

  /**
   * Tests that two longs are equal within an absolute tolerance.
   *
   * <p>If either value is NaN this returns false.
   *
   * @param expected The expected value.
   * @param actual The actual value.
   * @param absoluteError The maximum absolute error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal.
   * @return true if equal
   */
  static boolean longsTestWithin(long expected, long actual, long absoluteError) {
    final long delta = (expected > actual) ? expected - actual : actual - expected;
    // Note: Also check delta for overflow. It should be positive.
    return (delta <= absoluteError && delta >= 0);
  }

  /**
   * Tests that two ints are equal within an absolute tolerance.
   *
   * <p>If either value is NaN this returns false.
   *
   * @param expected The expected value.
   * @param actual The actual value.
   * @param absoluteError The maximum absolute error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal.
   * @return true if equal
   */
  static boolean intsTestWithin(int expected, int actual, long absoluteError) {
    // Use long arithmetic
    final long delta = (expected > actual) ? (long) expected - actual : (long) actual - expected;
    return (delta <= absoluteError);
  }

  /**
   * Tests that two shorts are equal within an absolute tolerance.
   *
   * <p>If either value is NaN this returns false.
   *
   * @param expected The expected value.
   * @param actual The actual value.
   * @param absoluteError The maximum absolute error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal.
   * @return true if equal
   */
  static boolean shortsTestWithin(short expected, short actual, int absoluteError) {
    // short uses int arithmetic by default
    final int delta = (expected > actual) ? expected - actual : actual - expected;
    return (delta <= absoluteError);
  }

  /**
   * Tests that two bytes are equal within an absolute tolerance.
   *
   * <p>If either value is NaN this returns false.
   *
   * @param expected The expected value.
   * @param actual The actual value.
   * @param absoluteError The maximum absolute error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal.
   * @return true if equal
   */
  static boolean bytesTestWithin(byte expected, byte actual, int absoluteError) {
    // byte uses int arithmetic by default
    final int delta = (expected > actual) ? expected - actual : actual - expected;
    return (delta <= absoluteError);
  }

  /**
   * Get the description of the test that two doubles are equal within an absolute tolerance.
   *
   * <p>It is assumed the errors have been validated with {@link #doublesValidateWithin(double)}.
   *
   * @param absoluteError The maximum absolute error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal.
   * @return the description
   */
  static String doublesGetDescriptionWithin(double absoluteError) {
    return (absoluteError == 0) ? DESCRIPTION_ABS_ERROR_0
        : DESCRIPTION_ABS_ERROR_LTE + String.valueOf(absoluteError);
  }

  /**
   * Get the description of the test that two floats are equal within an absolute tolerance.
   *
   * <p>It is assumed the errors have been validated with {@link #floatsValidateWithin(float)}.
   *
   * @param absoluteError The maximum absolute error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal.
   * @return the description
   */
  static String floatsGetDescriptionWithin(float absoluteError) {
    return (absoluteError == 0) ? DESCRIPTION_ABS_ERROR_0
        : DESCRIPTION_ABS_ERROR_LTE + String.valueOf(absoluteError);
  }

  /**
   * Get the description of the test that two longs are equal within an absolute tolerance.
   *
   * <p>It is assumed the errors have been validated with {@link #longsValidateWithin(long)}.
   *
   * @param absoluteError The maximum absolute error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal.
   * @return the description
   */
  static String longsGetDescriptionWithin(long absoluteError) {
    return (absoluteError == 0) ? DESCRIPTION_ABS_ERROR_0
        : DESCRIPTION_ABS_ERROR_LTE + String.valueOf(absoluteError);
  }

  /**
   * Get the description of the test that two ints are equal within an absolute tolerance.
   *
   * <p>It is assumed the errors have been validated with {@link #intsValidateWithin(long)}.
   *
   * @param absoluteError The maximum absolute error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal.
   * @return the description
   */
  static String intsGetDescriptionWithin(long absoluteError) {
    return (absoluteError == 0) ? DESCRIPTION_ABS_ERROR_0
        : DESCRIPTION_ABS_ERROR_LTE + String.valueOf(absoluteError);
  }

  /**
   * Get the description of the test that two shorts are equal within an absolute tolerance.
   *
   * <p>It is assumed the errors have been validated with {@link #shortsValidateWithin(int)}.
   *
   * @param absoluteError The maximum absolute error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal.
   * @return the description
   */
  static String shortsGetDescriptionWithin(int absoluteError) {
    return (absoluteError == 0) ? DESCRIPTION_ABS_ERROR_0
        : DESCRIPTION_ABS_ERROR_LTE + String.valueOf(absoluteError);
  }

  /**
   * Get the description of the test that two bytes are equal within an absolute tolerance.
   *
   * <p>It is assumed the errors have been validated with {@link #bytesValidateWithin(int)}.
   *
   * @param absoluteError The maximum absolute error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal.
   * @return the description
   */
  static String bytesGetDescriptionWithin(int absoluteError) {
    return (absoluteError == 0) ? DESCRIPTION_ABS_ERROR_0
        : DESCRIPTION_ABS_ERROR_LTE + String.valueOf(absoluteError);
  }
}
