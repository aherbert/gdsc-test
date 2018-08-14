/*-
 * #%L
 * Genome Damage and Stability Centre Test Package
 *
 * The GDSC Test package contains code for use with the JUnit test framework.
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
package uk.ac.sussex.gdsc.test.junit4;

import org.junit.internal.ComparisonCriteria;

/**
 * Adds additional helper assert functions to those provided by
 * {@link org.junit.Assert}.
 * <p>
 * Assert functions are provided to compare float values using relative error,
 * and assert methods to support formatted messages, e.g.
 *
 * <pre>
 * &#64;Test
 * public void myTest()
 * {
 *     ExtraAssert.assertEqualsRelative(999.9, 1000.0, 1e-2); // passes
 *     Extrathrow new AssertionError("Failed number %d", 42);
 * }
 * </pre>
 */
public class ExtraAssert {
    /**
     * Do not allow public construction.
     */
    private ExtraAssert() {
    }

    /**
     * Get the maximum
     *
     * @param a the first number
     * @param b the second number
     * @return the maximum
     */
    private static double max(double a, double b) {
        return (a >= b) ? a : b;
    }

    /**
     * Get the maximum
     *
     * @param a the first number
     * @param b the second number
     * @return the maximum
     */
    private static float max(float a, float b) {
        return (a >= b) ? a : b;
    }

    /**
     * Asserts that two doubles are equal to within a positive relativeError. If
     * they are not, an {@link AssertionError} is thrown with no message. If the
     * expected value is infinity then the relativeError value is ignored. NaNs are
     * considered equal:
     * <code>assertEqualsRelative(Double.NaN, Double.NaN, *)</code> passes
     *
     * @param expected      expected value
     * @param actual        the value to check against <code>expected</code>
     * @param relativeError the maximum relative error between <code>expected</code>
     *                      and <code>actual</code> for which both numbers are still
     *                      considered equal.
     * @throws AssertionError if the assertion is not {@code true}
     */
    public static void assertEqualsRelative(double expected, double actual, double relativeError)
            throws AssertionError {
        assertEqualsRelative(null, expected, actual, relativeError);
    }

    /**
     * Asserts that two doubles are equal to within a positive relativeError. If
     * they are not, an {@link AssertionError} is thrown with the given message. If
     * the expected value is infinity then the relativeError value is ignored. NaNs
     * are considered equal:
     * <code>assertEqualsRelative(Double.NaN, Double.NaN, *)</code> passes
     *
     * @param message       the identifying message for the {@link AssertionError}
     *                      (<code>null</code> okay)
     * @param expected      expected value
     * @param actual        the value to check against <code>expected</code>
     * @param relativeError the maximum relative error between <code>expected</code>
     *                      and <code>actual</code> for which both numbers are still
     *                      considered equal.
     * @throws AssertionError if the assertion is not {@code true}
     */
    public static void assertEqualsRelative(String message, double expected, double actual, double relativeError)
            throws AssertionError {
        final double max = max(Math.abs(expected), Math.abs(actual));
        if (doubleIsDifferent(expected, actual, max * relativeError)) {
            throw new AssertionError(formatNotEquals(message, Double.valueOf(expected), Double.valueOf(actual),
                    Math.abs(expected - actual) / max));
        }
    }

    /**
     * Asserts that two floats are equal to within a positive relativeError. If they
     * are not, an {@link AssertionError} is thrown with no message. If the expected
     * value is infinity then the relativeError value is ignored. NaNs are
     * considered equal: <code>assertEquals(Float.NaN, Float.NaN, *)</code> passes
     *
     * @param expected      expected value
     * @param actual        the value to check against <code>expected</code>
     * @param relativeError the maximum relative error between <code>expected</code>
     *                      and <code>actual</code> for which both numbers are still
     *                      considered equal.
     * @throws AssertionError if the assertion is not {@code true}
     */
    public static void assertEqualsRelative(float expected, float actual, double relativeError) throws AssertionError {
        assertEqualsRelative(null, expected, actual, relativeError);
    }

    /**
     * Asserts that two floats are equal to within a positive relativeError. If they
     * are not, an {@link AssertionError} is thrown with the given message. If the
     * expected value is infinity then the relativeError value is ignored. NaNs are
     * considered equal: <code>assertEquals(Float.NaN, Float.NaN, *)</code> passes
     *
     * @param message       the identifying message for the {@link AssertionError}
     *                      (<code>null</code> okay)
     * @param expected      expected value
     * @param actual        the value to check against <code>expected</code>
     * @param relativeError the maximum relative error between <code>expected</code>
     *                      and <code>actual</code> for which both numbers are still
     *                      considered equal.
     * @throws AssertionError if the assertion is not {@code true}
     */
    public static void assertEqualsRelative(String message, float expected, float actual, double relativeError)
            throws AssertionError {
        final float max = max(Math.abs(expected), Math.abs(actual));
        if (floatIsDifferent(expected, actual, (float) (max * relativeError))) {
            throw new AssertionError(formatNotEquals(message, Float.valueOf(expected), Float.valueOf(actual),
                    Math.abs(expected - actual) / max));
        }
    }

    /**
     * Asserts that two doubles are <b>not</b> equal to within a positive
     * relativeError. If they are, an {@link AssertionError} is thrown with no
     * message. If the expected value is infinity then the relativeError value is
     * ignored. NaNs are considered equal:
     * <code>assertNotEquals(Double.NaN, Double.NaN, *)</code> fails
     *
     * @param unexpected    unexpected value
     * @param actual        the value to check against <code>unexpected</code>
     * @param relativeError the maximum relative error between
     *                      <code>unexpected</code> and <code>actual</code> for
     *                      which both numbers are still considered equal.
     * @throws AssertionError if the assertion is not {@code true}
     */
    public static void assertNotEqualsRelative(double unexpected, double actual, double relativeError)
            throws AssertionError {
        assertNotEqualsRelative(null, unexpected, actual, relativeError);
    }

    /**
     * Asserts that two doubles are <b>not</b> equal to within a positive
     * relativeError. If they are, an {@link AssertionError} is thrown with the
     * given message. If the expected value is infinity then the relativeError value
     * is ignored. NaNs are considered equal:
     * <code>assertNotEquals(Double.NaN, Double.NaN, *)</code> fails
     *
     * @param message       the identifying message for the {@link AssertionError}
     *                      (<code>null</code> okay)
     * @param unexpected    unexpected value
     * @param actual        the value to check against <code>unexpected</code>
     * @param relativeError the maximum relative error between
     *                      <code>unexpected</code> and <code>actual</code> for
     *                      which both numbers are still considered equal.
     * @throws AssertionError if the assertion is not {@code true}
     */
    public static void assertNotEqualsRelative(String message, double unexpected, double actual, double relativeError)
            throws AssertionError {
        final double max = max(Math.abs(unexpected), Math.abs(actual));
        if (!doubleIsDifferent(unexpected, actual, max * relativeError)) {
            throw new AssertionError(formatEquals(message, Double.valueOf(unexpected), Double.valueOf(actual),
                    Math.abs(unexpected - actual) / max));
        }
    }

    /**
     * Asserts that two floats are <b>not</b> equal to within a positive
     * relativeError. If they are, an {@link AssertionError} is thrown with no
     * message. If the expected value is infinity then the relativeError value is
     * ignored. NaNs are considered equal:
     * <code>assertNotEquals(Float.NaN, Float.NaN, *)</code> fails
     *
     * @param unexpected    unexpected value
     * @param actual        the value to check against <code>expected</code>
     * @param relativeError the maximum relative error between
     *                      <code>unexpected</code> and <code>actual</code> for
     *                      which both numbers are still considered equal.
     * @throws AssertionError if the assertion is not {@code true}
     */
    public static void assertNotEqualsRelative(float unexpected, float actual, double relativeError)
            throws AssertionError {
        assertNotEqualsRelative(null, unexpected, actual, relativeError);
    }

    /**
     * Asserts that two floats are <b>not</b> equal to within a positive
     * relativeError. If they are, an {@link AssertionError} is thrown with the
     * given message. If the expected value is infinity then the relativeError value
     * is ignored. NaNs are considered equal:
     * <code>assertNotEquals(Float.NaN, Float.NaN, *)</code> fails
     *
     * @param message       the identifying message for the {@link AssertionError}
     *                      (<code>null</code> okay)
     * @param unexpected    unexpected value
     * @param actual        the value to check against <code>unexpected</code>
     * @param relativeError the maximum relative error between
     *                      <code>unexpected</code> and <code>actual</code> for
     *                      which both numbers are still considered equal.
     * @throws AssertionError if the assertion is not {@code true}
     */
    public static void assertNotEqualsRelative(String message, float unexpected, float actual, double relativeError)
            throws AssertionError {
        final float max = max(Math.abs(unexpected), Math.abs(actual));
        if (!floatIsDifferent(unexpected, actual, (float) (max * relativeError))) {
            throw new AssertionError(formatEquals(message, Float.valueOf(unexpected), Float.valueOf(actual),
                    Math.abs(unexpected - actual) / max));
        }
    }

    /**
     * A class for comparing double arrays using a relative threshold.
     * <p>
     * Based on org.junit.internal.InexactComparisonCriteria
     */
    public static class DoubleRelativeComparisonCriteria extends ComparisonCriteria {
        /** The relative delta. */
        public final double delta;

        /**
         * Instantiates a new double relative comparison criteria.
         *
         * @param delta the delta
         */
        public DoubleRelativeComparisonCriteria(double delta) {
            this.delta = delta;
        }

        @Override
        protected void assertElementsEqual(Object expected, Object actual) throws AssertionError {
            assertEqualsRelative(null, (Double) expected, (Double) actual, delta);
        }
    }

    /**
     * A class for comparing float arrays using a relative threshold.
     * <p>
     * Based on org.junit.internal.InexactComparisonCriteria
     */
    public static class FloatRelativeComparisonCriteria extends ComparisonCriteria {
        /** The relative delta. */
        public final double delta;

        /**
         * Instantiates a new float relative comparison criteria.
         *
         * @param delta the delta
         */
        public FloatRelativeComparisonCriteria(double delta) {
            this.delta = delta;
        }

        @Override
        protected void assertElementsEqual(Object expected, Object actual) throws AssertionError {
            assertEqualsRelative(null, (Float) expected, (Float) actual, delta);
        }
    }

    /**
     * Asserts that two double arrays are equal. If they are not, an
     * {@link AssertionError} is thrown with no message.
     *
     * @param expecteds     double array with expected values.
     * @param actuals       double array with actual values
     * @param relativeError the relative error
     * @throws AssertionError if the assertion is not {@code true}
     */
    public static void assertArrayEqualsRelative(double[] expecteds, double[] actuals, double relativeError)
            throws AssertionError {
        assertArrayEqualsRelative(null, expecteds, actuals, relativeError);
    }

    /**
     * Asserts that two double arrays are equal. If they are not, an
     * {@link AssertionError} is thrown with the given message.
     *
     * @param message       the identifying message for the {@link AssertionError}
     *                      (<code>null</code> okay)
     * @param expecteds     double array with expected values.
     * @param actuals       double array with actual values
     * @param relativeError the maximum relative error between
     *                      <code>expecteds[i]</code> and <code>actuals[i]</code>
     *                      for which both numbers are still considered equal.
     * @throws AssertionError if the assertion is not {@code true}
     */
    public static void assertArrayEqualsRelative(String message, double[] expecteds, double[] actuals,
            double relativeError) throws AssertionError {
        new DoubleRelativeComparisonCriteria(relativeError).arrayEquals(message, expecteds, actuals);
    }

    /**
     * Asserts that two float arrays are equal. If they are not, an
     * {@link AssertionError} is thrown with no message.
     *
     * @param expecteds     float array with expected values.
     * @param actuals       float array with actual values
     * @param relativeError the relative error
     * @throws AssertionError if the assertion is not {@code true}
     */
    public static void assertArrayEqualsRelative(float[] expecteds, float[] actuals, double relativeError)
            throws AssertionError {
        assertArrayEqualsRelative(null, expecteds, actuals, relativeError);
    }

    /**
     * Asserts that two float arrays are equal. If they are not, an
     * {@link AssertionError} is thrown with the given message.
     *
     * @param message       the identifying message for the {@link AssertionError}
     *                      (<code>null</code> okay)
     * @param expecteds     float array with expected values.
     * @param actuals       float array with actual values
     * @param relativeError the maximum relative error between
     *                      <code>expecteds[i]</code> and <code>actuals[i]</code>
     *                      for which both numbers are still considered equal.
     * @throws AssertionError if the assertion is not {@code true}
     */
    public static void assertArrayEqualsRelative(String message, float[] expecteds, float[] actuals,
            double relativeError) throws AssertionError {
        new FloatRelativeComparisonCriteria(relativeError).arrayEquals(message, expecteds, actuals);
    }

    /**
     * Asserts that two double arrays are equal. If they are not, an
     * {@link AssertionError} is thrown with no message.
     * <p>
     * This supports nested arrays, e.g. double[][].
     *
     * @param expecteds     double array with expected values.
     * @param actuals       double array with actual values.
     * @param relativeError the maximum relative error between
     *                      <code>expecteds[i]</code> and <code>actuals[i]</code>
     *                      for which both numbers are still considered equal.
     * @throws AssertionError if the assertion is not {@code true}
     */
    public static void assertDoubleArrayEqualsRelative(Object expecteds, Object actuals, double relativeError)
            throws AssertionError {
        assertDoubleArrayEqualsRelative(null, expecteds, actuals, relativeError);
    }

    /**
     * Asserts that two double arrays are equal. If they are not, an
     * {@link AssertionError} is thrown with the given message.
     * <p>
     * This supports nested arrays, e.g. double[][].
     *
     * @param message       the identifying message for the {@link AssertionError}
     *                      (<code>null</code> okay)
     * @param expecteds     double array with expected values.
     * @param actuals       double array with actual values.
     * @param relativeError the maximum relative error between
     *                      <code>expecteds[i]</code> and <code>actuals[i]</code>
     *                      for which both numbers are still considered equal.
     * @throws AssertionError if the assertion is not {@code true}
     */
    public static void assertDoubleArrayEqualsRelative(String message, Object expecteds, Object actuals,
            double relativeError) throws AssertionError {
        new DoubleRelativeComparisonCriteria(relativeError).arrayEquals(message, expecteds, actuals);
    }

    /**
     * Asserts that two float arrays are equal. If they are not, an
     * {@link AssertionError} is thrown with no message.
     * <p>
     * This supports nested arrays, e.g. double[][].
     *
     * @param expecteds     float array with expected values.
     * @param actuals       float array with actual values.
     * @param relativeError the maximum relative error between
     *                      <code>expecteds[i]</code> and <code>actuals[i]</code>
     *                      for which both numbers are still considered equal.
     * @throws AssertionError if the assertion is not {@code true}
     */
    public static void assertFloatArrayEqualsRelative(Object expecteds, Object actuals, double relativeError)
            throws AssertionError {
        assertFloatArrayEqualsRelative(null, expecteds, actuals, relativeError);
    }

    /**
     * Asserts that two float arrays are equal. If they are not, an
     * {@link AssertionError} is thrown with the given message.
     * <p>
     * This supports nested arrays, e.g. double[][].
     *
     * @param message       the identifying message for the {@link AssertionError}
     *                      (<code>null</code> okay)
     * @param expecteds     float array with expected values.
     * @param actuals       float array with actual values.
     * @param relativeError the maximum relative error between
     *                      <code>expecteds[i]</code> and <code>actuals[i]</code>
     *                      for which both numbers are still considered equal.
     * @throws AssertionError if the assertion is not {@code true}
     */
    public static void assertFloatArrayEqualsRelative(String message, Object expecteds, Object actuals,
            double relativeError) throws AssertionError {
        new FloatRelativeComparisonCriteria(relativeError).arrayEquals(message, expecteds, actuals);
    }

    // Adapted from org.junit.Assert

    private static boolean doubleIsDifferent(double d1, double d2, double delta) {
        if (Double.compare(d1, d2) == 0) {
            return false;
        }
        if ((Math.abs(d1 - d2) <= delta)) {
            return false;
        }
        return true;
    }

    private static boolean floatIsDifferent(float f1, float f2, float delta) {
        if (Float.compare(f1, f2) == 0) {
            return false;
        }
        if ((Math.abs(f1 - f2) <= delta)) {
            return false;
        }
        return true;
    }

    /**
     * Create a formatted error message when expected do not equal actual with the
     * given error.
     *
     * @param message  the message
     * @param expected the expected
     * @param actual   the actual
     * @param error    the error
     * @return the string
     */
    private static String formatNotEquals(String message, Object expected, Object actual, double error) {
        String formatted = "";
        if (message != null && !message.equals("")) {
            formatted = message + " ";
        }
        String expectedString = String.valueOf(expected);
        String actualString = String.valueOf(actual);
        return formatted + "expected:<" + expectedString + "> but was:<" + actualString + "> (error=" + error + ")";
    }

    /**
     * Create a formatted error message when expected does equal actual with the
     * given error.
     *
     * @param message    the message
     * @param unexpected the unexpected
     * @param actual     the actual
     * @param error      the error
     * @return the string
     */
    private static String formatEquals(String message, Object unexpected, Object actual, double error) {
        String formatted = "Values should be different. ";
        if (message != null && !message.equals("")) {
            formatted = message + " ";
        }
        String unexpectedString = String.valueOf(unexpected);
        String actualString = String.valueOf(actual);
        return formatted + "unexpected:<" + unexpectedString + "> but was:<" + actualString + "> (error=" + error + ")";
    }
}
