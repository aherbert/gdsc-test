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
 * Defines utilities for describing comparison predicates.
 */
final class DescriptionHelper {

  /** Constant used to ignore the relative error. */
  private static final double IGNORE_RELATIVE_ERROR = -1;
  /** Constant used to ignore the double absolute error. */
  private static final double IGNORE_DOUBLE_ABSOLUTE_ERROR = -1;
  /** Constant used to ignore the float absolute error. */
  private static final float IGNORE_FLOAT_ABSOLUTE_ERROR = -1;
  /** Constant used to ignore the long absolute error. */
  private static final long IGNORE_LONG_ABSOLUTE_ERROR = -1;
  /** Constant used to ignore the int absolute error. */
  private static final int IGNORE_INT_ABSOLUTE_ERROR = -1;
  /**
   * The description of symmetric relative error {@code <=}.
   */
  private static final String DESCRIPTION_REL_ERROR_LTE = "|v1-v2|/max(|v1|,|v2|) <= ";
  /**
   * The description of asymmetric relative error {@code <=}.
   */
  private static final String DESCRIPTION_ASYM_REL_ERROR_LTE = "|v1-v2|/|v1| <= ";
  /**
   * The description of absolute error {@code <=}.
   */
  private static final String DESCRIPTION_ABS_ERROR_LTE = "|v1-v2| <= ";
  /**
   * The description of absolute error {@code == 0}.
   */
  private static final String DESCRIPTION_ABS_ERROR_0 = "|v1-v2| == 0";

  /**
   * Do not allow public construction.
   */
  private DescriptionHelper() {
    // No constructor
  }

  /**
   * Get the description of the test that two doubles are equal within an absolute error.
   *
   * <p>It is assumed the error has been validated (is positive finite).
   *
   * @param absoluteError The maximum absolute error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @return the description
   */
  static String getDescriptionWithin(double absoluteError) {
    return (absoluteError == 0) ? DESCRIPTION_ABS_ERROR_0
        : DESCRIPTION_ABS_ERROR_LTE + absoluteError;
  }

  /**
   * Get the description of the test that two floats are equal within an absolute error.
   *
   * <p>It is assumed the error has been validated (is positive finite).
   *
   * @param absoluteError The maximum absolute error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @return the description
   */
  static String getDescriptionWithin(float absoluteError) {
    return (absoluteError == 0) ? DESCRIPTION_ABS_ERROR_0
        : DESCRIPTION_ABS_ERROR_LTE + absoluteError;
  }

  /**
   * Get the description of the test that two values are equal within an absolute error.
   *
   * <p>It is assumed the error has been validated (is positive).
   *
   * @param absoluteError The maximum absolute error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @return the description
   */
  static String getDescriptionWithin(BigInteger absoluteError) {
    return (absoluteError.signum() == 0) ? DESCRIPTION_ABS_ERROR_0
        : DESCRIPTION_ABS_ERROR_LTE + absoluteError;
  }

  /**
   * Get the description of the test that two values are equal within an absolute error.
   *
   * <p>It is assumed the error has been validated (is positive).
   *
   * @param absoluteError The maximum absolute error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @return the description
   */
  static String getDescriptionWithin(long absoluteError) {
    return (absoluteError == 0) ? DESCRIPTION_ABS_ERROR_0
        : DESCRIPTION_ABS_ERROR_LTE + absoluteError;
  }

  /**
   * Get the description of the test that two values are equal within an absolute error.
   *
   * <p>It is assumed the error has been validated (is positive).
   *
   * @param absoluteError The maximum absolute error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @return the description
   */
  static String getDescriptionWithin(int absoluteError) {
    return (absoluteError == 0) ? DESCRIPTION_ABS_ERROR_0
        : DESCRIPTION_ABS_ERROR_LTE + absoluteError;
  }

  //@formatter:off
  /**
   * Get the description of the test that two values are close using a relative and
   * absolute error. The relative error is symmetric for {@code value1} and {@code value2}.
   *
   * <p>It is assumed the errors have been validated (are positive and finite).
   *
   * <p>Note the special cases:
   *
   * <ul>
   * <li>When the relative error is zero this is equivalent to an absolute error of zero.
   * The resulting description just contains the absolute error.
   * <li>When the absolute error is zero and the relative error is above zero the resulting
   * description just contains the relative error.
   * </ul>
   *
   * @param relativeError The maximum relative error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @param absoluteError The maximum absolute error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @return the description
   */
  //@formatter:on
  static String getDescriptionClose(double relativeError, double absoluteError) {
    return getDescriptionAreClose(DESCRIPTION_REL_ERROR_LTE, relativeError, absoluteError);
  }

  //@formatter:off
  /**
   * Get the description of the test that two values are close using a relative and/or
   * absolute error. The relative error is symmetric for {@code value1} and {@code value2}.
   *
   * <p>It is assumed the errors have been validated (are positive and finite).
   *
   * <p>Note the special cases:
   *
   * <ul>
   * <li>When the relative error is zero this is equivalent to an absolute error of zero.
   * The resulting description just contains the absolute error.
   * <li>When the absolute error is zero and the relative error is above zero the resulting
   * description just contains the relative error.
   * </ul>
   *
   * @param relativeError The maximum relative error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @param absoluteError The maximum absolute error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @return the description
   */
  //@formatter:on
  static String getDescriptionClose(double relativeError, float absoluteError) {
    return getDescriptionAreClose(DESCRIPTION_REL_ERROR_LTE, relativeError, absoluteError);
  }

  //@formatter:off
  /**
   * Get the description of the test that two values are close using a relative and/or
   * absolute error. The relative error is symmetric for {@code value1} and {@code value2}.
   *
   * <p>It is assumed the errors have been validated (are positive and finite).
   *
   * <p>Note the special cases:
   *
   * <ul>
   * <li>When the relative error is zero this is equivalent to an absolute error of zero.
   * The resulting description just contains the absolute error.
   * <li>When the absolute error is zero and the relative error is above zero the resulting
   * description just contains the relative error.
   * </ul>
   *
   * @param relativeError The maximum relative error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @param absoluteError The maximum absolute error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @return the description
   */
  //@formatter:on
  static String getDescriptionClose(double relativeError, long absoluteError) {
    return getDescriptionAreClose(DESCRIPTION_REL_ERROR_LTE, relativeError, absoluteError);
  }

  //@formatter:off
  /**
   * Get the description of the test that two values are close using a relative and/or
   * absolute error. The relative error is symmetric for {@code value1} and {@code value2}.
   *
   * <p>It is assumed the errors have been validated (are positive and finite).
   *
   * <p>Note the special cases:
   *
   * <ul>
   * <li>When the relative error is zero this is equivalent to an absolute error of zero.
   * The resulting description just contains the absolute error.
   * <li>When the absolute error is zero and the relative error is above zero the resulting
   * description just contains the relative error.
   * </ul>
   *
   * @param relativeError The maximum relative error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @param absoluteError The maximum absolute error between <code>value1</code> and
   *        <code>value2</code> for which both numbers are still considered equal.
   * @return the description
   */
  //@formatter:on
  static String getDescriptionClose(double relativeError, int absoluteError) {
    return getDescriptionAreClose(DESCRIPTION_REL_ERROR_LTE, relativeError, absoluteError);
  }


  //@formatter:off
  /**
   * Get the description of the test that a value is close to an expected value using a relative
   * and absolute error. The relative error is asymmetric for {@code value1} and {@code value2}.
   *
   * <p>It is assumed the errors have been validated (are positive and finite).
   *
   * <p>Note the special cases:
   *
   * <ul>
   * <li>When the relative error is zero this is equivalent to an absolute error of zero.
   * The resulting description just contains the absolute error.
   * <li>When the absolute error is zero and the relative error is above zero the resulting
   * description just contains the relative error.
   * </ul>
   *
   * @param relativeError The maximum error, relative to <code>expected</code>, between
   *        <code>expected</code> and <code>actual</code> for which both numbers are still
   *        considered equal.
   * @param absoluteError The maximum absolute error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal.
   * @return the description
   */
  //@formatter:on
  static String getDescriptionIsCloseTo(double relativeError, double absoluteError) {
    return getDescriptionAreClose(DESCRIPTION_ASYM_REL_ERROR_LTE, relativeError,
        absoluteError);
  }

  //@formatter:off
  /**
   * Get the description of the test that a value is close to an expected value using a relative
   * and absolute error. The relative error is asymmetric for {@code value1} and {@code value2}.
   *
   * <p>It is assumed the errors have been validated (are positive and finite).
   *
   * <p>Note the special cases:
   *
   * <ul>
   * <li>When the relative error is zero this is equivalent to an absolute error of zero.
   * The resulting description just contains the absolute error.
   * <li>When the absolute error is zero and the relative error is above zero the resulting
   * description just contains the relative error.
   * </ul>
   *
   * @param relativeError The maximum error, relative to <code>expected</code>, between
   *        <code>expected</code> and <code>actual</code> for which both numbers are still
   *        considered equal.
   * @param absoluteError The maximum absolute error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal.
   * @return the description
   */
  //@formatter:on
  static String getDescriptionIsCloseTo(double relativeError, float absoluteError) {
    return getDescriptionAreClose(DESCRIPTION_ASYM_REL_ERROR_LTE, relativeError,
        absoluteError);
  }

  //@formatter:off
  /**
   * Get the description of the test that a value is close to an expected value using a relative
   * and absolute error. The relative error is asymmetric for {@code value1} and {@code value2}.
   *
   * <p>It is assumed the errors have been validated (are positive and finite).
   *
   * <p>Note the special cases:
   *
   * <ul>
   * <li>When the relative error is zero this is equivalent to an absolute error of zero.
   * The resulting description just contains the absolute error.
   * <li>When the absolute error is zero and the relative error is above zero the resulting
   * description just contains the relative error.
   * </ul>
   *
   * @param relativeError The maximum error, relative to <code>expected</code>, between
   *        <code>expected</code> and <code>actual</code> for which both numbers are still
   *        considered equal.
   * @param absoluteError The maximum absolute error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal.
   * @return the description
   */
  //@formatter:on
  static String getDescriptionIsCloseTo(double relativeError, long absoluteError) {
    return getDescriptionAreClose(DESCRIPTION_ASYM_REL_ERROR_LTE, relativeError,
        absoluteError);
  }

  //@formatter:off
  /**
   * Get the description of the test that a value is close to an expected value using a relative
   * and absolute error. The relative error is asymmetric for {@code value1} and {@code value2}.
   *
   * <p>It is assumed the errors have been validated (are positive and finite).
   *
   * <p>Note the special cases:
   *
   * <ul>
   * <li>When the relative error is zero this is equivalent to an absolute error of zero.
   * The resulting description just contains the absolute error.
   * <li>When the absolute error is zero and the relative error is above zero the resulting
   * description just contains the relative error.
   * </ul>
   *
   * @param relativeError The maximum error, relative to <code>expected</code>, between
   *        <code>expected</code> and <code>actual</code> for which both numbers are still
   *        considered equal.
   * @param absoluteError The maximum absolute error between <code>expected</code> and
   *        <code>actual</code> for which both numbers are still considered equal.
   * @return the description
   */
  //@formatter:on
  static String getDescriptionIsCloseTo(double relativeError, int absoluteError) {
    return getDescriptionAreClose(DESCRIPTION_ASYM_REL_ERROR_LTE, relativeError, absoluteError);
  }
  
  /**
   * Get the description of the test that two values are close using a relative and absolute error.
   * The type of relative error is specified.
   *
   * @param relativeErrorPrefix the relative prefix
   * @param relativeError the relative error
   * @param absoluteError the absolute error
   * @return the description
   */
  private static String getDescriptionAreClose(String relativeErrorPrefix,
      double relativeError, double absoluteError) {
    // Assume both are >= 0

    // Handle special cases where the test was actually equivalent to something different.
    // 1. A relative error of 0 is the equivalent of absolute error 0, so is ignored
    final double testRelativeError = (relativeError == 0) ? IGNORE_RELATIVE_ERROR : relativeError;
    // 2. An absolute error of 0 is not used if the relative error is >0
    final double testAbsoluteError =
        (absoluteError == 0 && testRelativeError > 0) ? IGNORE_DOUBLE_ABSOLUTE_ERROR
            : absoluteError;

    // Build the description
    final StringBuilder sb = new StringBuilder();
    if (testRelativeError > 0) {
      // This is always <=
      sb.append(relativeErrorPrefix).append(testRelativeError);
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
   * Get the description of the test that two values are close using a relative and/or absolute
   * error. The type of relative error is specified.
   *
   * @param relativeErrorPrefix the relative error prefix
   * @param relativeError the relative error
   * @param absoluteError the absolute error
   * @return the description
   */
  private static String getDescriptionAreClose(String relativeErrorPrefix,
      double relativeError, float absoluteError) {
    // Assume both are >= 0

    // Handle special cases where the test was actually equivalent to something different.
    // 1. A relative error of 0 is the equivalent of absolute error 0, so is ignored
    final double testRelativeError = (relativeError == 0) ? IGNORE_RELATIVE_ERROR : relativeError;
    // 2. An absolute error of 0 is not used if the relative error is >0
    final float testAbsoluteError =
        (absoluteError == 0 && testRelativeError > 0) ? IGNORE_FLOAT_ABSOLUTE_ERROR : absoluteError;

    // Build the description
    final StringBuilder sb = new StringBuilder();
    if (testRelativeError > 0) {
      // This is always <=
      sb.append(relativeErrorPrefix).append(testRelativeError);
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
   * Get the description of the test that two values are close using a relative and/or absolute
   * error. The type of relative error is specified.
   *
   * @param relativeErrorPrefix the relative error prefix
   * @param relativeError the relative error
   * @param absoluteError the absolute error
   * @return the description
   */
  private static String getDescriptionAreClose(String relativeErrorPrefix,
      double relativeError, long absoluteError) {
    // Assume both are >= 0

    // Handle special cases where the test was actually equivalent to something different.
    // 1. A relative error of 0 is the equivalent of absolute error 0, so is ignored
    final double testRelativeError = (relativeError == 0) ? IGNORE_RELATIVE_ERROR : relativeError;
    // 2. An absolute error of 0 is not used if the relative error is >0
    final long testAbsoluteError =
        (absoluteError == 0 && testRelativeError > 0) ? IGNORE_LONG_ABSOLUTE_ERROR : absoluteError;

    // Build the description
    final StringBuilder sb = new StringBuilder();
    if (testRelativeError > 0) {
      // This is always <=
      sb.append(relativeErrorPrefix).append(testRelativeError);
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
   * Get the description of the test that two values are close using a relative and/or absolute
   * error. The type of relative error is specified.
   *
   * @param relativeErrorPrefix the relative error prefix
   * @param relativeError the relative error
   * @param absoluteError the absolute error
   * @return the description
   */
  private static String getDescriptionAreClose(String relativeErrorPrefix, double relativeError,
      int absoluteError) {
    // Assume both are >= 0

    // Handle special cases where the test was actually equivalent to something different.
    // 1. A relative error of 0 is the equivalent of absolute error 0, so is ignored
    final double testRelativeError = (relativeError == 0) ? IGNORE_RELATIVE_ERROR : relativeError;
    // 2. An absolute error of 0 is not used if the relative error is >0
    final int testAbsoluteError =
        (absoluteError == 0 && testRelativeError > 0) ? IGNORE_INT_ABSOLUTE_ERROR : absoluteError;

    // Build the description
    final StringBuilder sb = new StringBuilder();
    if (testRelativeError > 0) {
      // This is always <=
      sb.append(relativeErrorPrefix).append(testRelativeError);
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
}
