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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("javadoc")
public class EqualityUtilsTest {

  // COPY FROM HERE

  @Test
  public void testFloatAreEqual() {
    float[] values = {0, 1, 1.5f, (float) Math.PI, Float.NaN, Float.POSITIVE_INFINITY,
        Float.NEGATIVE_INFINITY, Float.MAX_VALUE, Float.MIN_VALUE};
    for (float v1 : values) {
      for (float v2 : values) {
        Assertions.assertEquals(Float.compare(v1, v2) == 0, EqualityUtils.floatsAreEqual(v1, v2));
      }
    }
  }

  @Test
  public void testFloatsAreAlmostEqualThrows() {
    float expected = 0;
    float actual = 0;
    EqualityUtils.floatsAreAlmostEqual(expected, actual, 0, 0);

    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      float relativeError = -1;
      float absoluteError = -1;
      EqualityUtils.floatsAreAlmostEqual(expected, actual, relativeError, absoluteError);
    });
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      float relativeError = 2;
      float absoluteError = 0;
      EqualityUtils.floatsAreAlmostEqual(expected, actual, relativeError, absoluteError);
    });
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      float relativeError = 0;
      float absoluteError = Float.POSITIVE_INFINITY;
      EqualityUtils.floatsAreAlmostEqual(expected, actual, relativeError, absoluteError);
    });
  }

  @Test
  public void testFloatsAreAlmostEqual() {
    // Test exact
    double relativeError = 0;
    float absoluteError = 0;

    // Use a range of values
    for (float value : new float[] {0, 1, (float) Math.PI, Float.MAX_VALUE, Float.MIN_VALUE}) {
      for (int i = 0; i < 2; i++) {
        value *= -1;
        Assertions.assertTrue(
            EqualityUtils.floatsAreAlmostEqual(value, value, relativeError, absoluteError));
        Assertions.assertFalse(EqualityUtils.floatsAreAlmostEqual(value, Math.nextUp(value),
            relativeError, absoluteError));
        Assertions.assertFalse(EqualityUtils.floatsAreAlmostEqual(value, Math.nextDown(value),
            relativeError, absoluteError));
        Assertions.assertFalse(EqualityUtils.floatsAreAlmostEqual(Math.nextUp(value), value,
            relativeError, absoluteError));
        Assertions.assertFalse(EqualityUtils.floatsAreAlmostEqual(Math.nextDown(value), value,
            relativeError, absoluteError));
      }
    }
    // Special floats are not equal as they are not within a real delta.
    for (float value : new float[] {Float.NaN, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY}) {
      Assertions.assertFalse(
          EqualityUtils.floatsAreAlmostEqual(value, value, relativeError, absoluteError));
    }

    float expected = 0;
    float actual = 0;

    // Test the absolute error
    expected = 1;
    actual = 2;
    relativeError = -1;
    absoluteError = actual - expected;

    // Order insensitive
    Assertions.assertTrue(
        EqualityUtils.floatsAreAlmostEqual(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(
        EqualityUtils.floatsAreAlmostEqual(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    float delta = Math.nextDown(absoluteError);
    Assertions
        .assertFalse(EqualityUtils.floatsAreAlmostEqual(expected, actual, relativeError, delta));
    Assertions
        .assertFalse(EqualityUtils.floatsAreAlmostEqual(actual, expected, relativeError, delta));

    // Make the gap bigger
    // Calling Math.nextDown(expected) creates a value that still is within 1 of 2 due to float
    // error.
    // So instead get the next float above 1 and subtract from 2.
    float expected2 = actual - Math.nextUp(absoluteError);
    Assertions.assertFalse(
        EqualityUtils.floatsAreAlmostEqual(expected2, actual, relativeError, absoluteError));
    Assertions.assertFalse(
        EqualityUtils.floatsAreAlmostEqual(actual, expected2, relativeError, absoluteError));

    // Test the relative error
    expected = 1;
    actual = 2;
    relativeError = 0.5;
    absoluteError = 0;
    Assertions.assertTrue(
        EqualityUtils.floatsAreAlmostEqual(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(
        EqualityUtils.floatsAreAlmostEqual(actual, expected, relativeError, absoluteError));
    Assertions.assertFalse(EqualityUtils.floatsAreAlmostEqual(expected, Math.nextUp(actual),
        relativeError, absoluteError));
    // Calling Math.nextDown(1) creates a value that still is within 1 of 2 due to float error.
    // So instead get the next float above 1 and subtract from 2.
    Assertions.assertFalse(EqualityUtils.floatsAreAlmostEqual(actual - Math.nextUp(1), actual,
        relativeError, absoluteError));
  }

  @Test
  public void testFloatsAreAlmostEqualUsingNoError() {
    float relativeError = 0;
    float absoluteError = 0;

    // Use a range of values
    for (float value : new float[] {0, 1, (float) Math.PI, Float.MAX_VALUE, Float.MIN_VALUE}) {
      for (int i = 0; i < 2; i++) {
        value *= -1;
        Assertions.assertTrue(
            EqualityUtils.floatsAreAlmostEqual(value, value, relativeError, absoluteError));
        Assertions.assertFalse(EqualityUtils.floatsAreAlmostEqual(value, Math.nextUp(value),
            relativeError, absoluteError));
        Assertions.assertFalse(EqualityUtils.floatsAreAlmostEqual(value, Math.nextDown(value),
            relativeError, absoluteError));
        Assertions.assertFalse(EqualityUtils.floatsAreAlmostEqual(Math.nextUp(value), value,
            relativeError, absoluteError));
        Assertions.assertFalse(EqualityUtils.floatsAreAlmostEqual(Math.nextDown(value), value,
            relativeError, absoluteError));
      }
    }
    // Special floats are not equal as they are not within a real delta.
    for (float value : new float[] {Float.NaN, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY}) {
      Assertions.assertFalse(
          EqualityUtils.floatsAreAlmostEqual(value, value, relativeError, absoluteError));
    }
  }

  @Test
  public void testFloatsAreAlmostEqualUsingAbsoluteError() {
    float expected = 1;
    float actual = 2;
    float relativeError = -1;
    float absoluteError = actual - expected;

    // Order insensitive
    Assertions.assertTrue(
        EqualityUtils.floatsAreAlmostEqual(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(
        EqualityUtils.floatsAreAlmostEqual(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    float delta = Math.nextDown(absoluteError);
    Assertions
        .assertFalse(EqualityUtils.floatsAreAlmostEqual(expected, actual, relativeError, delta));
    Assertions
        .assertFalse(EqualityUtils.floatsAreAlmostEqual(actual, expected, relativeError, delta));

    // Make the gap bigger
    // Calling Math.nextDown(expected) creates a value that still is within 1 of 2 due to float
    // error.
    // So instead get the next delta above and subtract from actual.
    float expected2 = actual - Math.nextUp(absoluteError);
    Assertions.assertFalse(
        EqualityUtils.floatsAreAlmostEqual(expected2, actual, relativeError, absoluteError));
    Assertions.assertFalse(
        EqualityUtils.floatsAreAlmostEqual(actual, expected2, relativeError, absoluteError));
  }

  @Test
  public void testFloatsAreAlmostEqualUsingRelativeError() {
    float expected = 1;
    float actual = 2;
    double relativeError = 0.5;
    float absoluteError = -1;

    // Order insensitive
    Assertions.assertTrue(
        EqualityUtils.floatsAreAlmostEqual(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(
        EqualityUtils.floatsAreAlmostEqual(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    double delta = Math.nextDown(relativeError);
    Assertions
        .assertFalse(EqualityUtils.floatsAreAlmostEqual(expected, actual, delta, absoluteError));
    Assertions
        .assertFalse(EqualityUtils.floatsAreAlmostEqual(actual, expected, delta, absoluteError));

    // Make the gap bigger
    // Calling Math.nextDown(expected) creates a value that still is within 1 of 2 due to float
    // error.
    // So instead get the next delta above and subtract from actual.
    float expected2 = actual - Math.nextUp(actual - expected);
    Assertions.assertFalse(
        EqualityUtils.floatsAreAlmostEqual(expected2, actual, relativeError, absoluteError));
    Assertions.assertFalse(
        EqualityUtils.floatsAreAlmostEqual(actual, expected2, relativeError, absoluteError));
  }

  @Test
  public void testFloatsGetDescription() {
    // These must be distinguishable as strings
    Float[] values1 = {-1.1f, 0.0f, 1.1f};
    Integer[] values2 = {-2, 0, 2};
    for (Number v1 : values1) {
      for (Number v2 : values2) {
        testFloatsGetDescription(v1, v2);
      }
    }
  }

  private static void testFloatsGetDescription(Number relativeError, Number absoluteError) {

    double inputRelError = relativeError.doubleValue();
    float inputAbsError = absoluteError.floatValue();

    // The method assumes the errors must pass the validation in checkErrors(...)
    try {
      EqualityUtils.floatsCheckErrors(inputRelError, inputAbsError);
    } catch (IllegalArgumentException ex) {
      return;
    }

    // The raw input
    String result = EqualityUtils.floatsGetDescription(inputRelError, inputAbsError);
    String msg = String.format("Rel.Error = %s, Abs.Error = %ss", relativeError, absoluteError);

    // Handle special case
    if (inputRelError == 0) {
      relativeError = -1.1;
      if (inputAbsError < 0) {
        absoluteError = 0;
      }
    }
    if (absoluteError.doubleValue() == 0 && relativeError.doubleValue() > 0) {
      absoluteError = -2;
    }

    // Get the expected values in the message
    double relError = relativeError.doubleValue();
    float absError = absoluteError.floatValue();

    boolean hasAbs = absError >= 0;
    boolean hasRel = relError > 0;

    String relString = String.valueOf(relError);
    String absString = String.valueOf(absError);

    Assertions.assertEquals(hasRel, result.contains("Rel"), () -> msg + " Missing relative");
    Assertions.assertEquals(hasRel, result.contains(relString),
        () -> msg + " Missing relative value");
    Assertions.assertEquals(hasAbs, result.contains("Abs"), () -> msg + " Missing absolute");
    Assertions.assertEquals(hasAbs, result.contains(absString),
        () -> msg + " Missing absolute value");
    Assertions.assertEquals(hasRel && hasAbs, result.contains("||"), () -> msg + " Missing ||");
  }

  // COPY TO HERE

  @Test
  public void testDoubleAreEqual() {
    double[] values = {0, 1, 1.5, Math.PI, Double.NaN, Double.POSITIVE_INFINITY,
        Double.NEGATIVE_INFINITY, Double.MAX_VALUE, Double.MIN_VALUE};
    for (double v1 : values) {
      for (double v2 : values) {
        Assertions.assertEquals(Double.compare(v1, v2) == 0, EqualityUtils.doublesAreEqual(v1, v2));
      }
    }
  }

  @Test
  public void testDoublesAreAlmostEqualThrows() {
    double expected = 0;
    double actual = 0;
    EqualityUtils.doublesAreAlmostEqual(expected, actual, 0, 0);

    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      double relativeError = -1;
      double absoluteError = -1;
      EqualityUtils.doublesAreAlmostEqual(expected, actual, relativeError, absoluteError);
    });
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      double relativeError = 2;
      double absoluteError = 0;
      EqualityUtils.doublesAreAlmostEqual(expected, actual, relativeError, absoluteError);
    });
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      double relativeError = 0;
      double absoluteError = Double.POSITIVE_INFINITY;
      EqualityUtils.doublesAreAlmostEqual(expected, actual, relativeError, absoluteError);
    });
  }

  @Test
  public void testDoublesAreAlmostEqual() {
    // Test exact
    double relativeError = 0;
    double absoluteError = 0;

    // Use a range of values
    for (double value : new double[] {0, 1, Math.PI, Double.MAX_VALUE, Double.MIN_VALUE}) {
      for (int i = 0; i < 2; i++) {
        value *= -1;
        Assertions.assertTrue(
            EqualityUtils.doublesAreAlmostEqual(value, value, relativeError, absoluteError));
        Assertions.assertFalse(EqualityUtils.doublesAreAlmostEqual(value, Math.nextUp(value),
            relativeError, absoluteError));
        Assertions.assertFalse(EqualityUtils.doublesAreAlmostEqual(value, Math.nextDown(value),
            relativeError, absoluteError));
        Assertions.assertFalse(EqualityUtils.doublesAreAlmostEqual(Math.nextUp(value), value,
            relativeError, absoluteError));
        Assertions.assertFalse(EqualityUtils.doublesAreAlmostEqual(Math.nextDown(value), value,
            relativeError, absoluteError));
      }
    }
    // Special doubles are not equal as they are not within a real delta.
    for (double value : new double[] {Double.NaN, Double.POSITIVE_INFINITY,
        Double.NEGATIVE_INFINITY}) {
      Assertions.assertFalse(
          EqualityUtils.doublesAreAlmostEqual(value, value, relativeError, absoluteError));
    }

    double expected = 0;
    double actual = 0;

    // Test the absolute error
    expected = 1;
    actual = 2;
    relativeError = -1;
    absoluteError = actual - expected;

    // Order insensitive
    Assertions.assertTrue(
        EqualityUtils.doublesAreAlmostEqual(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(
        EqualityUtils.doublesAreAlmostEqual(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    double delta = Math.nextDown(absoluteError);
    Assertions
        .assertFalse(EqualityUtils.doublesAreAlmostEqual(expected, actual, relativeError, delta));
    Assertions
        .assertFalse(EqualityUtils.doublesAreAlmostEqual(actual, expected, relativeError, delta));

    // Make the gap bigger
    // Calling Math.nextDown(expected) creates a value that still is within 1 of 2 due to float
    // error.
    // So instead get the next double above 1 and subtract from 2.
    double expected2 = actual - Math.nextUp(absoluteError);
    Assertions.assertFalse(
        EqualityUtils.doublesAreAlmostEqual(expected2, actual, relativeError, absoluteError));
    Assertions.assertFalse(
        EqualityUtils.doublesAreAlmostEqual(actual, expected2, relativeError, absoluteError));

    // Test the relative error
    expected = 1;
    actual = 2;
    relativeError = 0.5;
    absoluteError = 0;
    Assertions.assertTrue(
        EqualityUtils.doublesAreAlmostEqual(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(
        EqualityUtils.doublesAreAlmostEqual(actual, expected, relativeError, absoluteError));
    Assertions.assertFalse(EqualityUtils.doublesAreAlmostEqual(expected, Math.nextUp(actual),
        relativeError, absoluteError));
    // Calling Math.nextDown(1) creates a value that still is within 1 of 2 due to float error.
    // So instead get the next double above 1 and subtract from 2.
    Assertions.assertFalse(EqualityUtils.doublesAreAlmostEqual(actual - Math.nextUp(1), actual,
        relativeError, absoluteError));
  }

  @Test
  public void testDoublesAreAlmostEqualUsingNoError() {
    double relativeError = 0;
    double absoluteError = 0;

    // Use a range of values
    for (double value : new double[] {0, 1, Math.PI, Double.MAX_VALUE, Double.MIN_VALUE}) {
      for (int i = 0; i < 2; i++) {
        value *= -1;
        Assertions.assertTrue(
            EqualityUtils.doublesAreAlmostEqual(value, value, relativeError, absoluteError));
        Assertions.assertFalse(EqualityUtils.doublesAreAlmostEqual(value, Math.nextUp(value),
            relativeError, absoluteError));
        Assertions.assertFalse(EqualityUtils.doublesAreAlmostEqual(value, Math.nextDown(value),
            relativeError, absoluteError));
        Assertions.assertFalse(EqualityUtils.doublesAreAlmostEqual(Math.nextUp(value), value,
            relativeError, absoluteError));
        Assertions.assertFalse(EqualityUtils.doublesAreAlmostEqual(Math.nextDown(value), value,
            relativeError, absoluteError));
      }
    }
    // Special doubles are not equal as they are not within a real delta.
    for (double value : new double[] {Double.NaN, Double.POSITIVE_INFINITY,
        Double.NEGATIVE_INFINITY}) {
      Assertions.assertFalse(
          EqualityUtils.doublesAreAlmostEqual(value, value, relativeError, absoluteError));
    }
  }

  @Test
  public void testDoublesAreAlmostEqualUsingAbsoluteError() {
    double expected = 1;
    double actual = 2;
    double relativeError = -1;
    double absoluteError = actual - expected;

    // Order insensitive
    Assertions.assertTrue(
        EqualityUtils.doublesAreAlmostEqual(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(
        EqualityUtils.doublesAreAlmostEqual(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    double delta = Math.nextDown(absoluteError);
    Assertions
        .assertFalse(EqualityUtils.doublesAreAlmostEqual(expected, actual, relativeError, delta));
    Assertions
        .assertFalse(EqualityUtils.doublesAreAlmostEqual(actual, expected, relativeError, delta));

    // Make the gap bigger
    // Calling Math.nextDown(expected) creates a value that still is within 1 of 2 due to float
    // error.
    // So instead get the next delta above and subtract from actual.
    double expected2 = actual - Math.nextUp(absoluteError);
    Assertions.assertFalse(
        EqualityUtils.doublesAreAlmostEqual(expected2, actual, relativeError, absoluteError));
    Assertions.assertFalse(
        EqualityUtils.doublesAreAlmostEqual(actual, expected2, relativeError, absoluteError));
  }

  @Test
  public void testDoublesAreAlmostEqualUsingRelativeError() {
    double expected = 1;
    double actual = 2;
    double relativeError = 0.5;
    double absoluteError = -1;

    // Order insensitive
    Assertions.assertTrue(
        EqualityUtils.doublesAreAlmostEqual(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(
        EqualityUtils.doublesAreAlmostEqual(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    double delta = Math.nextDown(relativeError);
    Assertions
        .assertFalse(EqualityUtils.doublesAreAlmostEqual(expected, actual, delta, absoluteError));
    Assertions
        .assertFalse(EqualityUtils.doublesAreAlmostEqual(actual, expected, delta, absoluteError));

    // Make the gap bigger
    // Calling Math.nextDown(expected) creates a value that still is within 1 of 2 due to float
    // error.
    // So instead get the next delta above and subtract from actual.
    double expected2 = actual - Math.nextUp(actual - expected);
    Assertions.assertFalse(
        EqualityUtils.doublesAreAlmostEqual(expected2, actual, relativeError, absoluteError));
    Assertions.assertFalse(
        EqualityUtils.doublesAreAlmostEqual(actual, expected2, relativeError, absoluteError));
  }

  @Test
  public void testDoublesGetDescription() {
    // These must be distinguishable as strings
    Double[] values1 = {-1.1, 0.0, 1.1};
    Integer[] values2 = {-2, 0, 2};
    for (Number v1 : values1) {
      for (Number v2 : values2) {
        testDoublesGetDescription(v1, v2);
      }
    }
  }

  private static void testDoublesGetDescription(Number relativeError, Number absoluteError) {

    double inputRelError = relativeError.doubleValue();
    double inputAbsError = absoluteError.doubleValue();

    // The method assumes the errors must pass the validation in checkErrors(...)
    try {
      EqualityUtils.doublesCheckErrors(inputRelError, inputAbsError);
    } catch (IllegalArgumentException ex) {
      return;
    }

    // The raw input
    String result = EqualityUtils.doublesGetDescription(inputRelError, inputAbsError);
    String msg = String.format("Rel.Error = %s, Abs.Error = %ss", relativeError, absoluteError);

    // Handle special case
    if (inputRelError == 0) {
      relativeError = -1.1;
      if (inputAbsError < 0) {
        absoluteError = 0;
      }
    }
    if (absoluteError.doubleValue() == 0 && relativeError.doubleValue() > 0) {
      absoluteError = -2;
    }

    // Get the expected values in the message
    double relError = relativeError.doubleValue();
    double absError = absoluteError.doubleValue();

    boolean hasAbs = absError >= 0;
    boolean hasRel = relError > 0;

    String relString = String.valueOf(relError);
    String absString = String.valueOf(absError);

    Assertions.assertEquals(hasRel, result.contains("Rel"), () -> msg + " Missing relative");
    Assertions.assertEquals(hasRel, result.contains(relString),
        () -> msg + " Missing relative value");
    Assertions.assertEquals(hasAbs, result.contains("Abs"), () -> msg + " Missing absolute");
    Assertions.assertEquals(hasAbs, result.contains(absString),
        () -> msg + " Missing absolute value");
    Assertions.assertEquals(hasRel && hasAbs, result.contains("||"), () -> msg + " Missing ||");
  }
}
