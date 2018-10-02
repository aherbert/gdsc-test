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

/**
 * Defines utilities for testing {@code double} and {@code float} equality.
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
      double absoluteError) throws IllegalArgumentException {
    doublesCheckErrors(relativeError, absoluteError);
    return doublesTestAlmostEqual(expected, actual, relativeError, absoluteError);
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
  static void doublesCheckErrors(double relativeError, double absoluteError)
      throws IllegalArgumentException {
    if (relativeError < 0 && absoluteError < 0) {
      throw new IllegalArgumentException("No valid equality criteria");
    }
    if (relativeError >= MAX_RELATIVE_ERROR) {
      throw new IllegalArgumentException(
          "Relative error must be less than 2 but was: [" + relativeError + "]");
    }
    if (!Double.isFinite(absoluteError)) {
      throw new IllegalArgumentException(
          "Absolute error must be finite but was: [" + absoluteError + "]");
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
   * Compute the maximum of two values without NaN checks.
   *
   * @param value1 first value
   * @param value2 second value
   * @return b if a is lesser or equal to b, a otherwise
   */
  private static double doublesMax(final double value1, final double value2) {
    return (value1 > value2) ? value1 : value2;
  }

  //@formatter:off
  /**
   * Get the description of the test that two doubles are almost equal.
   *
   * <p>It is assumed the errors have been validated with
   * {@link #doublesCheckErrors(double, double)}.
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
  static String doublesGetDescription(double relativeError, double absoluteError) {
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
      sb.append("Rel.Error <= ").append(testRelativeError);
    }
    if (testAbsoluteError >= 0) {
      // Add combined operator
      if (sb.length() != 0) {
        sb.append(" || ");
      }
      // This may be == or <=
      sb.append("Abs.Error");
      sb.append((testAbsoluteError == 0) ? " == " : " <= ");
      sb.append(testAbsoluteError);
    }

    return sb.toString();
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
      float absoluteError) throws IllegalArgumentException {
    floatsCheckErrors(relativeError, absoluteError);
    return floatsTestAlmostEqual(expected, actual, relativeError, absoluteError);
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
  static void floatsCheckErrors(double relativeError, float absoluteError)
      throws IllegalArgumentException {
    if (relativeError < 0 && absoluteError < 0) {
      throw new IllegalArgumentException("No valid equality criteria");
    }
    if (relativeError >= MAX_RELATIVE_ERROR) {
      throw new IllegalArgumentException(
          "Relative error must be less than 2 but was: [" + relativeError + "]");
    }
    if (!Float.isFinite(absoluteError)) {
      throw new IllegalArgumentException(
          "Absolute error must be finite but was: [" + absoluteError + "]");
    }
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
  private static float floatsMax(final float value1, final float value2) {
    return (value1 > value2) ? value1 : value2;
  }

  //@formatter:off
  /**
   * Get the description of the test that two doubles are almost equal.
   *
   * <p>It is assumed the error shave been validated with {@link #floatsCheckErrors(double, float)}.
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
  static String floatsGetDescription(double relativeError, float absoluteError) {
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
      sb.append("Rel.Error <= ").append(testRelativeError);
    }
    if (testAbsoluteError >= 0) {
      // Add combined operator
      if (sb.length() != 0) {
        sb.append(" || ");
      }
      // This may be == or <=
      sb.append("Abs.Error");
      sb.append((testAbsoluteError == 0) ? " == " : " <= ");
      sb.append(testAbsoluteError);
    }

    return sb.toString();
  }
}
