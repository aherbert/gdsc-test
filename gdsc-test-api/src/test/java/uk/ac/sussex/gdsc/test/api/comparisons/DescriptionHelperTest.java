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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("javadoc")
public class DescriptionHelperTest {

  /**
   * Assert the message contains the sub-string.
   *
   * <p>If missing fails with a description of the sub-string.
   *
   * @param message the message
   * @param subString the sub-string
   * @param description the description
   */
  private static void assertMessageContains(String message, String subString, String description) {
    Assertions.assertTrue(message.contains(subString),
        () -> String.format("Message '%s' is missing '%s' (%s)", message, subString, description));
  }

  /**
   * Assert the message contains the sub-string.
   *
   * <p>If missing fails with a description of the sub-string.
   *
   * @param expected the expected result of contains
   * @param message the message
   * @param subString the sub-string
   * @param description the description
   */
  private static void assertMessageContains(boolean expected, String message, String subString,
      String description, Number relativeError, Number absoluteError) {
    Assertions.assertEquals(expected, message.contains(subString),
        () -> String.format("Message '%s' is missing '%s' (%s. Rel.Error=%s, Abs.Error=%s)",
            message, subString, description, relativeError, absoluteError));
  }

  @Test
  public void testFloatsGetDescriptionWithin() {
    // These must be distinguishable as strings
    for (final float absError : new float[] {-0f, 0, 0.5f, 1, (float) Math.PI}) {
      final String result = DescriptionHelper.getDescriptionWithin(absError);
      final String absString = (absError == 0) ? "0" : String.valueOf(absError);
      assertMessageContains(result, "|v1-v2|", "Predicate description");
      assertMessageContains(result, (absError == 0) ? " == " : " <= ", "Predicate description");
      assertMessageContains(result, absString, "Absolute error");
    }
  }

  @Test
  public void testDoublesGetDescriptionWithin() {
    // These must be distinguishable as strings
    for (final double absError : new double[] {-0.0, 0, 0.5, 1, Math.PI}) {
      final String result = DescriptionHelper.getDescriptionWithin(absError);
      final String absString = (absError == 0) ? "0" : String.valueOf(absError);
      assertMessageContains(result, "|v1-v2|", "Predicate description");
      assertMessageContains(result, (absError == 0) ? " == " : " <= ", "Predicate description");
      assertMessageContains(result, absString, "Absolute error");
    }
  }

  @Test
  public void testBigIntegerGetDescriptionWithin() {
    // These must be distinguishable as strings
    for (BigInteger absError : new BigInteger[] {BigInteger.ZERO, BigInteger.ONE,
        BigInteger.valueOf(Long.MAX_VALUE),
        // Bigger than a long
        BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE)}) {
      final String result = DescriptionHelper.getDescriptionWithin(absError);
      final String absString = String.valueOf(absError);
      assertMessageContains(result, "|v1-v2|", "Predicate description");
      assertMessageContains(result, (absError.signum() == 0) ? " == " : " <= ",
          "Predicate description");
      assertMessageContains(result, absString, "Absolute error");
    }
  }

  @Test
  public void testLongsGetDescriptionWithin() {
    // These must be distinguishable as strings
    for (final long absError : new long[] {0, 1, Long.MAX_VALUE}) {
      final String result = DescriptionHelper.getDescriptionWithin(absError);
      final String absString = String.valueOf(absError);
      assertMessageContains(result, "|v1-v2|", "Predicate description");
      assertMessageContains(result, (absError == 0) ? " == " : " <= ", "Predicate description");
      assertMessageContains(result, absString, "Absolute error");
    }
  }

  @Test
  public void testIntsGetDescriptionWithin() {
    // These must be distinguishable as strings
    for (int absError : new int[] {0, 1, Integer.MAX_VALUE}) {
      final String result = DescriptionHelper.getDescriptionWithin(absError);
      final String absString = String.valueOf(absError);
      assertMessageContains(result, "|v1-v2|", "Predicate description");
      assertMessageContains(result, (absError == 0) ? " == " : " <= ", "Predicate description");
      assertMessageContains(result, absString, "Absolute error");
    }
  }

  @Test
  public void testFloatsGetDescriptionClose() {
    // These must be distinguishable as strings
    final Float[] values1 = {0.0f, 1.1f};
    final Integer[] values2 = {0, 2};
    for (final Number v1 : values1) {
      for (final Number v2 : values2) {
        testFloatsGetDescriptionClose(v1, v2);
      }
    }
  }

  private static void testFloatsGetDescriptionClose(Number relativeError, Number absoluteError) {

    final double inputRelError = relativeError.doubleValue();
    final float inputAbsError = absoluteError.floatValue();

    // The raw input
    final String result = DescriptionHelper.getDescriptionClose(inputRelError, inputAbsError);

    // Handle special case
    if (inputRelError == 0) {
      relativeError = -1;
      if (inputAbsError < 0) {
        absoluteError = 0;
      }
    }
    if (absoluteError.doubleValue() == 0 && relativeError.doubleValue() > 0) {
      absoluteError = -1;
    }

    // Get the expected values in the message
    final double relError = relativeError.doubleValue();
    final float absError = absoluteError.floatValue();

    final boolean hasAbs = absError >= 0;
    final boolean hasRel = relError > 0;

    final String relString = String.valueOf(relError);
    final String absString = (absError == 0) ? "0" : String.valueOf(absError);

    assertMessageContains(hasRel, result, "|v1-v2|/max", "Predicate description", relativeError,
        absoluteError);
    assertMessageContains(hasRel, result, relString, "Relative error", relativeError,
        absoluteError);
    assertMessageContains(hasAbs, result, "|v1-v2| ", "Predicate description", relativeError,
        absoluteError);
    assertMessageContains(hasAbs, result, absString, "Absolute error", relativeError,
        absoluteError);
    assertMessageContains(hasRel && hasAbs, result, "||", "Combination string", relativeError,
        absoluteError);
  }

  @Test
  public void testDoublesGetDescriptionClose() {
    // These must be distinguishable as strings
    final Double[] values1 = {0.0, 1.1};
    final Integer[] values2 = {0, 2};
    for (final Number v1 : values1) {
      for (final Number v2 : values2) {
        testDoublesGetDescriptionClose(v1, v2);
      }
    }
  }

  private static void testDoublesGetDescriptionClose(Number relativeError, Number absoluteError) {

    final double inputRelError = relativeError.doubleValue();
    final double inputAbsError = absoluteError.doubleValue();

    // The raw input
    final String result = DescriptionHelper.getDescriptionClose(inputRelError, inputAbsError);

    // Handle special case
    if (inputRelError == 0) {
      relativeError = -1;
      if (inputAbsError < 0) {
        absoluteError = 0;
      }
    }
    if (absoluteError.doubleValue() == 0 && relativeError.doubleValue() > 0) {
      absoluteError = -1;
    }

    // Get the expected values in the message
    final double relError = relativeError.doubleValue();
    final double absError = absoluteError.doubleValue();

    final boolean hasAbs = absError >= 0;
    final boolean hasRel = relError > 0;

    final String relString = String.valueOf(relError);
    final String absString = (absError == 0) ? "0" : String.valueOf(absError);

    assertMessageContains(hasRel, result, "|v1-v2|/max", "Predicate description", relativeError,
        absoluteError);
    assertMessageContains(hasRel, result, relString, "Relative error", relativeError,
        absoluteError);
    assertMessageContains(hasAbs, result, "|v1-v2| ", "Predicate description", relativeError,
        absoluteError);
    assertMessageContains(hasAbs, result, absString, "Absolute error", relativeError,
        absoluteError);
    assertMessageContains(hasRel && hasAbs, result, "||", "Combination string", relativeError,
        absoluteError);
  }

  @Test
  public void testLongsGetDescriptionClose() {
    // These must be distinguishable as strings
    final Double[] values1 = {0.0, 1.1};
    final Long[] values2 = {0L, 2L};
    for (final Number v1 : values1) {
      for (final Number v2 : values2) {
        testLongsGetDescriptionClose(v1, v2);
      }
    }
  }

  private static void testLongsGetDescriptionClose(Number relativeError, Number absoluteError) {

    final double inputRelError = relativeError.doubleValue();
    final long inputAbsError = absoluteError.longValue();

    // The raw input
    final String result = DescriptionHelper.getDescriptionClose(inputRelError, inputAbsError);

    // Handle special case
    if (inputRelError == 0) {
      relativeError = -1;
      if (inputAbsError < 0) {
        absoluteError = 0;
      }
    }
    if (absoluteError.longValue() == 0 && relativeError.doubleValue() > 0) {
      absoluteError = -1;
    }

    // Get the expected values in the message
    final double relError = relativeError.doubleValue();
    final long absError = absoluteError.longValue();

    final boolean hasAbs = absError >= 0;
    final boolean hasRel = relError > 0;

    final String relString = String.valueOf(relError);
    final String absString = String.valueOf(absError);

    assertMessageContains(hasRel, result, "|v1-v2|/max", "Predicate description", relativeError,
        absoluteError);
    assertMessageContains(hasRel, result, relString, "Relative error", relativeError,
        absoluteError);
    assertMessageContains(hasAbs, result, "|v1-v2| ", "Predicate description", relativeError,
        absoluteError);
    assertMessageContains(hasAbs, result, absString, "Absolute error", relativeError,
        absoluteError);
    assertMessageContains(hasRel && hasAbs, result, "||", "Combination string", relativeError,
        absoluteError);
  }

  @Test
  public void testIntsGetDescriptionClose() {
    // These must be distinguishable as strings
    final Double[] values1 = {0.0, 1.1};
    final Integer[] values2 = {0, 2};
    for (final Number v1 : values1) {
      for (final Number v2 : values2) {
        testIntsGetDescriptionClose(v1, v2);
      }
    }
  }

  private static void testIntsGetDescriptionClose(Number relativeError, Number absoluteError) {

    final double inputRelError = relativeError.doubleValue();
    final int inputAbsError = absoluteError.intValue();

    // The raw input
    final String result = DescriptionHelper.getDescriptionClose(inputRelError, inputAbsError);

    // Handle special case
    if (inputRelError == 0) {
      relativeError = -1;
      if (inputAbsError < 0) {
        absoluteError = 0;
      }
    }
    if (absoluteError.intValue() == 0 && relativeError.doubleValue() > 0) {
      absoluteError = -1;
    }

    // Get the expected values in the message
    final double relError = relativeError.doubleValue();
    final int absError = absoluteError.intValue();

    final boolean hasAbs = absError >= 0;
    final boolean hasRel = relError > 0;

    final String relString = String.valueOf(relError);
    final String absString = String.valueOf(absError);

    assertMessageContains(hasRel, result, "|v1-v2|/max", "Predicate description", relativeError,
        absoluteError);
    assertMessageContains(hasRel, result, relString, "Relative error", relativeError,
        absoluteError);
    assertMessageContains(hasAbs, result, "|v1-v2| ", "Predicate description", relativeError,
        absoluteError);
    assertMessageContains(hasAbs, result, absString, "Absolute error", relativeError,
        absoluteError);
    assertMessageContains(hasRel && hasAbs, result, "||", "Combination string", relativeError,
        absoluteError);
  }

  @Test
  public void testFloatsGetDescriptionIsCloseTo() {
    // These must be distinguishable as strings
    final Float[] values1 = {0.0f, 1.1f};
    final Integer[] values2 = {0, 2};
    for (final Number v1 : values1) {
      for (final Number v2 : values2) {
        testFloatsGetDescriptionIsCloseTo(v1, v2);
      }
    }
  }

  private static void testFloatsGetDescriptionIsCloseTo(Number relativeError,
      Number absoluteError) {

    final double inputRelError = relativeError.doubleValue();
    final float inputAbsError = absoluteError.floatValue();

    // The raw input
    final String result = DescriptionHelper.getDescriptionIsCloseTo(inputRelError, inputAbsError);

    // Handle special case
    if (inputRelError == 0) {
      relativeError = -1;
      if (inputAbsError < 0) {
        absoluteError = 0;
      }
    }
    if (absoluteError.doubleValue() == 0 && relativeError.doubleValue() > 0) {
      absoluteError = -1;
    }

    // Get the expected values in the message
    final double relError = relativeError.doubleValue();
    final float absError = absoluteError.floatValue();

    final boolean hasAbs = absError >= 0;
    final boolean hasRel = relError > 0;

    final String relString = String.valueOf(relError);
    final String absString = (absError == 0) ? "0" : String.valueOf(absError);

    assertMessageContains(hasRel, result, "|v1-v2|/|v1|", "Predicate description", relativeError,
        absoluteError);
    assertMessageContains(hasRel, result, relString, "Relative error", relativeError,
        absoluteError);
    assertMessageContains(hasAbs, result, "|v1-v2| ", "Predicate description", relativeError,
        absoluteError);
    assertMessageContains(hasAbs, result, absString, "Absolute error", relativeError,
        absoluteError);
    assertMessageContains(hasRel && hasAbs, result, "||", "Combination string", relativeError,
        absoluteError);
  }

  @Test
  public void testDoublesGetDescriptionIsCloseTo() {
    // These must be distinguishable as strings
    final Double[] values1 = {0.0, 1.1};
    final Integer[] values2 = {0, 2};
    for (final Number v1 : values1) {
      for (final Number v2 : values2) {
        testDoublesGetDescriptionIsCloseTo(v1, v2);
      }
    }
  }

  private static void testDoublesGetDescriptionIsCloseTo(Number relativeError,
      Number absoluteError) {

    final double inputRelError = relativeError.doubleValue();
    final double inputAbsError = absoluteError.doubleValue();

    // The raw input
    final String result =
        DescriptionHelper.getDescriptionIsCloseTo(inputRelError, inputAbsError);

    // Handle special case
    if (inputRelError == 0) {
      relativeError = -1;
      if (inputAbsError < 0) {
        absoluteError = 0;
      }
    }
    if (absoluteError.doubleValue() == 0 && relativeError.doubleValue() > 0) {
      absoluteError = -1;
    }

    // Get the expected values in the message
    final double relError = relativeError.doubleValue();
    final double absError = absoluteError.doubleValue();

    final boolean hasAbs = absError >= 0;
    final boolean hasRel = relError > 0;

    final String relString = String.valueOf(relError);
    final String absString = (absError == 0) ? "0" : String.valueOf(absError);

    assertMessageContains(hasRel, result, "|v1-v2|/|v1|", "Predicate description", relativeError,
        absoluteError);
    assertMessageContains(hasRel, result, relString, "Relative error", relativeError,
        absoluteError);
    assertMessageContains(hasAbs, result, "|v1-v2| ", "Predicate description", relativeError,
        absoluteError);
    assertMessageContains(hasAbs, result, absString, "Absolute error", relativeError,
        absoluteError);
    assertMessageContains(hasRel && hasAbs, result, "||", "Combination string", relativeError,
        absoluteError);
  }

  @Test
  public void testLongsGetDescriptionIsCloseTo() {
    // These must be distinguishable as strings
    final Double[] values1 = {0.0, 1.1};
    final Long[] values2 = {0L, 2L};
    for (final Number v1 : values1) {
      for (final Number v2 : values2) {
        testLongsGetDescriptionIsCloseTo(v1, v2);
      }
    }
  }

  private static void testLongsGetDescriptionIsCloseTo(Number relativeError, Number absoluteError) {

    final double inputRelError = relativeError.doubleValue();
    final long inputAbsError = absoluteError.longValue();

    // The raw input
    final String result = DescriptionHelper.getDescriptionIsCloseTo(inputRelError, inputAbsError);

    // Handle special case
    if (inputRelError == 0) {
      relativeError = -1;
      if (inputAbsError < 0) {
        absoluteError = 0;
      }
    }
    if (absoluteError.longValue() == 0 && relativeError.doubleValue() > 0) {
      absoluteError = -1;
    }

    // Get the expected values in the message
    final double relError = relativeError.doubleValue();
    final long absError = absoluteError.longValue();

    final boolean hasAbs = absError >= 0;
    final boolean hasRel = relError > 0;

    final String relString = String.valueOf(relError);
    final String absString = String.valueOf(absError);

    assertMessageContains(hasRel, result, "|v1-v2|/|v1|", "Predicate description", relativeError,
        absoluteError);
    assertMessageContains(hasRel, result, relString, "Relative error", relativeError,
        absoluteError);
    assertMessageContains(hasAbs, result, "|v1-v2| ", "Predicate description", relativeError,
        absoluteError);
    assertMessageContains(hasAbs, result, absString, "Absolute error", relativeError,
        absoluteError);
    assertMessageContains(hasRel && hasAbs, result, "||", "Combination string", relativeError,
        absoluteError);
  }

  @Test
  public void testIntsGetDescriptionIsCloseTo() {
    // These must be distinguishable as strings
    final Double[] values1 = {0.0, 1.1};
    final Integer[] values2 = {0, 2};
    for (final Number v1 : values1) {
      for (final Number v2 : values2) {
        testIntsGetDescriptionIsCloseTo(v1, v2);
      }
    }
  }

  private static void testIntsGetDescriptionIsCloseTo(Number relativeError, Number absoluteError) {

    final double inputRelError = relativeError.doubleValue();
    final int inputAbsError = absoluteError.intValue();

    // The raw input
    final String result = DescriptionHelper.getDescriptionIsCloseTo(inputRelError, inputAbsError);

    // Handle special case
    if (inputRelError == 0) {
      relativeError = -1;
      if (inputAbsError < 0) {
        absoluteError = 0;
      }
    }
    if (absoluteError.intValue() == 0 && relativeError.doubleValue() > 0) {
      absoluteError = -1;
    }

    // Get the expected values in the message
    final double relError = relativeError.doubleValue();
    final int absError = absoluteError.intValue();

    final boolean hasAbs = absError >= 0;
    final boolean hasRel = relError > 0;

    final String relString = String.valueOf(relError);
    final String absString = String.valueOf(absError);

    assertMessageContains(hasRel, result, "|v1-v2|/|v1|", "Predicate description", relativeError,
        absoluteError);
    assertMessageContains(hasRel, result, relString, "Relative error", relativeError,
        absoluteError);
    assertMessageContains(hasAbs, result, "|v1-v2| ", "Predicate description", relativeError,
        absoluteError);
    assertMessageContains(hasAbs, result, absString, "Absolute error", relativeError,
        absoluteError);
    assertMessageContains(hasRel && hasAbs, result, "||", "Combination string", relativeError,
        absoluteError);
  }
}
