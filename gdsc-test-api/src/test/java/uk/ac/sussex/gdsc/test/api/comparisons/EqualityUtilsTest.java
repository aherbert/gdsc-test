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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

@SuppressWarnings("javadoc")
public class EqualityUtilsTest {

  private static final BigInteger MAX_LONG_DELTA =
      BigInteger.valueOf(Long.MAX_VALUE).subtract(BigInteger.valueOf(Long.MIN_VALUE));

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
  public void testFloatAreEqual() {
    final float[] values = {0, 1, 1.5f, (float) Math.PI, Float.NaN, Float.POSITIVE_INFINITY,
        Float.NEGATIVE_INFINITY, Float.MAX_VALUE, Float.MIN_VALUE};
    for (final float v1 : values) {
      for (final float v2 : values) {
        Assertions.assertEquals(Float.compare(v1, v2) == 0, EqualityUtils.areEqual(v1, v2));
      }
    }
  }

  @Test
  public void testDoubleAreEqual() {
    final double[] values = {0, 1, 1.5, Math.PI, Double.NaN, Double.POSITIVE_INFINITY,
        Double.NEGATIVE_INFINITY, Double.MAX_VALUE, Double.MIN_VALUE};
    for (final double v1 : values) {
      for (final double v2 : values) {
        Assertions.assertEquals(Double.compare(v1, v2) == 0, EqualityUtils.areEqual(v1, v2));
      }
    }
  }

  // float Within

  @Test
  public void testFloatsAreWithinThrows() {
    final float expected = 0;
    final float actual = 0;
    // OK
    for (float absError : new float[] {-0f, 0, 1, (float) Math.PI, Float.MAX_VALUE}) {
      EqualityUtils.areWithin(expected, actual, absError);
    }
    // Bad
    for (float absError : new float[] {-1, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY,
        Float.NaN}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        EqualityUtils.areWithin(expected, actual, absError);
      });
    }
  }

  @Test
  public void testFloatsAreWithinUsingNoError() {
    // Test exact
    float absoluteError = 0;

    // Use a range of values
    for (float value : new float[] {0, 1, (float) Math.PI, Float.MAX_VALUE, Float.MIN_VALUE}) {
      for (int i = 0; i < 2; i++) {
        value *= -1;
        Assertions.assertTrue(EqualityUtils.areWithin(value, value, absoluteError));
        Assertions.assertFalse(EqualityUtils.areWithin(value, Math.nextUp(value), absoluteError));
        Assertions.assertFalse(EqualityUtils.areWithin(value, Math.nextDown(value), absoluteError));
        Assertions.assertFalse(EqualityUtils.areWithin(Math.nextUp(value), value, absoluteError));
        Assertions.assertFalse(EqualityUtils.areWithin(Math.nextDown(value), value, absoluteError));
      }
    }
    // Special floats are not equal as they are not within a real delta.
    for (final float value : new float[] {Float.NaN, Float.POSITIVE_INFINITY,
        Float.NEGATIVE_INFINITY}) {
      Assertions.assertFalse(EqualityUtils.areWithin(value, value, absoluteError));
    }
  }

  @Test
  public void testFloatsAreWithinUsingAbsoluteError() {
    final float expected = 2;
    final float actual = 1;
    final float absoluteError = expected - actual;

    // Order insensitive
    Assertions.assertTrue(EqualityUtils.areWithin(expected, actual, absoluteError));
    Assertions.assertTrue(EqualityUtils.areWithin(actual, expected, absoluteError));

    // Make the delta smaller
    final float delta = Math.nextDown(absoluteError);
    Assertions.assertFalse(EqualityUtils.areWithin(expected, actual, delta));
    Assertions.assertFalse(EqualityUtils.areWithin(actual, expected, delta));

    // Make the gap bigger
    // Calling Math.nextDown(expected) creates a value that still is within 1 of 2 due to float
    // error.
    // So instead get the next delta above and subtract from actual.
    final float expected2 = actual - Math.nextUp(absoluteError);
    Assertions.assertFalse(EqualityUtils.areWithin(expected2, actual, absoluteError));
    Assertions.assertFalse(EqualityUtils.areWithin(actual, expected2, absoluteError));
  }

  @Test
  public void testFloatsGetDescriptionWithin() {
    // These must be distinguishable as strings
    for (final float absError : new float[] {-0f, 0, 0.5f, 1, (float) Math.PI}) {
      final String result = EqualityUtils.floatsGetDescriptionWithin(absError);
      final String absString = (absError == 0) ? "0" : String.valueOf(absError);
      assertMessageContains(result, "|v1-v2|", "Predicate description");
      assertMessageContains(result, (absError == 0) ? " == " : " <= ", "Predicate description");
      assertMessageContains(result, absString, "Absolute error");
    }
  }

  // double Within

  @Test
  public void testDoublesAreWithinThrows() {
    final double expected = 0;
    final double actual = 0;
    // OK
    for (double absError : new double[] {-0.0, 0, 1, Math.PI, Double.MAX_VALUE}) {
      EqualityUtils.areWithin(expected, actual, absError);
    }
    // Bad
    for (double absError : new double[] {-1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,
        Double.NaN}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        EqualityUtils.areWithin(expected, actual, absError);
      });
    }
  }

  @Test
  public void testDoublesAreWithinUsingNoError() {
    // Test exact
    double absoluteError = 0;

    // Use a range of values
    for (double value : new double[] {0, 1, Math.PI, Double.MAX_VALUE, Double.MIN_VALUE}) {
      for (int i = 0; i < 2; i++) {
        value *= -1;
        Assertions.assertTrue(EqualityUtils.areWithin(value, value, absoluteError));
        Assertions.assertFalse(EqualityUtils.areWithin(value, Math.nextUp(value), absoluteError));
        Assertions.assertFalse(EqualityUtils.areWithin(value, Math.nextDown(value), absoluteError));
        Assertions.assertFalse(EqualityUtils.areWithin(Math.nextUp(value), value, absoluteError));
        Assertions.assertFalse(EqualityUtils.areWithin(Math.nextDown(value), value, absoluteError));
      }
    }
    // Special doubles are not equal as they are not within a real delta.
    for (final double value : new double[] {Double.NaN, Double.POSITIVE_INFINITY,
        Double.NEGATIVE_INFINITY}) {
      Assertions.assertFalse(EqualityUtils.areWithin(value, value, absoluteError));
    }
  }

  @Test
  public void testDoublesAreWithinUsingAbsoluteError() {
    final double expected = 2;
    final double actual = 1;
    final double absoluteError = expected - actual;

    // Order insensitive
    Assertions.assertTrue(EqualityUtils.areWithin(expected, actual, absoluteError));
    Assertions.assertTrue(EqualityUtils.areWithin(actual, expected, absoluteError));

    // Make the delta smaller
    final double delta = Math.nextDown(absoluteError);
    Assertions.assertFalse(EqualityUtils.areWithin(expected, actual, delta));
    Assertions.assertFalse(EqualityUtils.areWithin(actual, expected, delta));

    // Make the gap bigger
    // Calling Math.nextDown(expected) creates a value that still is within 1 of 2 due to float
    // error.
    // So instead get the next delta above and subtract from actual.
    final double expected2 = actual - Math.nextUp(absoluteError);
    Assertions.assertFalse(EqualityUtils.areWithin(expected2, actual, absoluteError));
    Assertions.assertFalse(EqualityUtils.areWithin(actual, expected2, absoluteError));
  }

  @Test
  public void testDoublesGetDescriptionWithin() {
    // These must be distinguishable as strings
    for (final double absError : new double[] {-0.0, 0, 0.5, 1, Math.PI}) {
      final String result = EqualityUtils.doublesGetDescriptionWithin(absError);
      final String absString = (absError == 0) ? "0" : String.valueOf(absError);
      assertMessageContains(result, "|v1-v2|", "Predicate description");
      assertMessageContains(result, (absError == 0) ? " == " : " <= ", "Predicate description");
      assertMessageContains(result, absString, "Absolute error");
    }
  }

  // long Within using BigInteger

  @Test
  public void testLongsUsingBigIntegerAreWithinThrows() {
    final long expected = 0;
    final long actual = 0;

    // OK
    for (BigInteger absError : new BigInteger[] {BigInteger.ZERO, BigInteger.ONE,
        BigInteger.valueOf(Long.MAX_VALUE),
        // Bigger than a long
        BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE),
        // Biggest allowed difference between longs
        MAX_LONG_DELTA.subtract(BigInteger.ONE)}) {
      EqualityUtils.areWithin(expected, actual, absError);
    }
    // Bad
    for (BigInteger absError : new BigInteger[] {BigInteger.ONE.negate(),
        BigInteger.valueOf(Long.MIN_VALUE), MAX_LONG_DELTA}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        EqualityUtils.areWithin(expected, actual, absError);
      });
    }
  }

  @Test
  public void testLongsUsingBigIntegerAreWithinUsingNoError() {
    // Test exact
    BigInteger absoluteError = BigInteger.ZERO;

    // Use a range of values
    for (long value : new long[] {0, 1, Long.MAX_VALUE, Long.MIN_VALUE}) {
      for (int i = 0; i < 2; i++) {
        value *= -1;
        Assertions.assertTrue(EqualityUtils.areWithin(value, value, absoluteError));
        Assertions.assertFalse(EqualityUtils.areWithin(value, value + 1, absoluteError));
        Assertions.assertFalse(EqualityUtils.areWithin(value, value - 1, absoluteError));
        Assertions.assertFalse(EqualityUtils.areWithin(value + 1, value, absoluteError));
        Assertions.assertFalse(EqualityUtils.areWithin(value - 1, value, absoluteError));
      }
    }
    // Special longs are not equal as they are not within a real delta.
    Assertions.assertFalse(EqualityUtils.areWithin(Long.MAX_VALUE, -1, absoluteError));
    Assertions.assertFalse(EqualityUtils.areWithin(Long.MIN_VALUE, 0, absoluteError));
    Assertions.assertFalse(EqualityUtils.areWithin(Long.MAX_VALUE, Long.MIN_VALUE, absoluteError));
  }

  @Test
  public void testLongsUsingBigIntegerAreWithinUsingLargestError() {
    BigInteger absoluteError = BigInteger.valueOf(Long.MAX_VALUE)
        .subtract(BigInteger.valueOf(Long.MIN_VALUE)).subtract(BigInteger.ONE);
    Assertions.assertTrue(EqualityUtils.areWithin(Long.MAX_VALUE, 0, absoluteError));
    // Check overflow
    Assertions.assertTrue(EqualityUtils.areWithin(Long.MAX_VALUE, -1, absoluteError));
    Assertions.assertTrue(EqualityUtils.areWithin(Long.MIN_VALUE, 0, absoluteError));
    // This is the biggest delta possible
    Assertions.assertFalse(EqualityUtils.areWithin(Long.MAX_VALUE, Long.MIN_VALUE, absoluteError));
  }

  @Test
  public void testLongsUsingBigIntegerAreWithinUsingAbsoluteError() {
    final long expected = Long.MAX_VALUE - 1;
    final long actual = -20;
    BigInteger absoluteError = BigInteger.valueOf(expected).subtract(BigInteger.valueOf(actual));

    // Order insensitive
    Assertions.assertTrue(EqualityUtils.areWithin(expected, actual, absoluteError));
    Assertions.assertTrue(EqualityUtils.areWithin(actual, expected, absoluteError));

    // Make the delta smaller
    final BigInteger delta = absoluteError.subtract(BigInteger.ONE);
    Assertions.assertFalse(EqualityUtils.areWithin(expected, actual, delta));
    Assertions.assertFalse(EqualityUtils.areWithin(actual, expected, delta));

    // Make the gap bigger
    final long expected2 = expected + 1;
    Assertions.assertFalse(EqualityUtils.areWithin(expected2, actual, absoluteError));
    Assertions.assertFalse(EqualityUtils.areWithin(actual, expected2, absoluteError));
  }

  @Test
  public void testLongsUsingBigIntegerGetDescriptionWithin() {
    // These must be distinguishable as strings
    for (BigInteger absError : new BigInteger[] {BigInteger.ZERO, BigInteger.ONE,
        BigInteger.valueOf(Long.MAX_VALUE),
        // Bigger than a long
        BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE),
        // Biggest allowed difference between longs
        MAX_LONG_DELTA.subtract(BigInteger.ONE)}) {
      final String result = EqualityUtils.longsGetDescriptionWithin(absError);
      final String absString = String.valueOf(absError);
      assertMessageContains(result, "|v1-v2|", "Predicate description");
      assertMessageContains(result, (absError.signum() == 0) ? " == " : " <= ",
          "Predicate description");
      assertMessageContains(result, absString, "Absolute error");
    }
  }

  // long Within

  @Test
  public void testLongsAreWithinThrows() {
    final long expected = 0;
    final long actual = 0;
    // OK
    for (long absError : new long[] {0, 1, Long.MAX_VALUE}) {
      EqualityUtils.areWithin(expected, actual, absError);
    }
    // Bad
    for (long absError : new long[] {-1, Long.MIN_VALUE}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        EqualityUtils.areWithin(expected, actual, absError);
      });
    }
  }

  @Test
  public void testLongsAreWithinUsingNoError() {
    // Test exact
    long absoluteError = 0;

    // Use a range of values
    for (long value : new long[] {0, 1, Long.MAX_VALUE, Long.MIN_VALUE}) {
      for (int i = 0; i < 2; i++) {
        value *= -1;
        Assertions.assertTrue(EqualityUtils.areWithin(value, value, absoluteError));
        Assertions.assertFalse(EqualityUtils.areWithin(value, value + 1, absoluteError));
        Assertions.assertFalse(EqualityUtils.areWithin(value, value - 1, absoluteError));
        Assertions.assertFalse(EqualityUtils.areWithin(value + 1, value, absoluteError));
        Assertions.assertFalse(EqualityUtils.areWithin(value - 1, value, absoluteError));
      }
    }
    // Special longs are not equal as they are not within a real delta.
    Assertions.assertFalse(EqualityUtils.areWithin(Long.MAX_VALUE, -1, absoluteError));
    Assertions.assertFalse(EqualityUtils.areWithin(Long.MIN_VALUE, 0, absoluteError));
    Assertions.assertFalse(EqualityUtils.areWithin(Long.MAX_VALUE, Long.MIN_VALUE, absoluteError));
  }

  @Test
  public void testLongsAreWithinUsingLargestError() {
    long absoluteError = Long.MAX_VALUE;
    Assertions.assertTrue(EqualityUtils.areWithin(Long.MAX_VALUE, 0, absoluteError));
    // Check overflow
    Assertions.assertFalse(EqualityUtils.areWithin(Long.MAX_VALUE, -1, absoluteError));
    Assertions.assertFalse(EqualityUtils.areWithin(Long.MIN_VALUE, 0, absoluteError));
    Assertions.assertFalse(EqualityUtils.areWithin(Long.MAX_VALUE, Long.MIN_VALUE, absoluteError));
  }

  @Test
  public void testLongsAreWithinUsingAbsoluteError() {
    final long expected = 20;
    final long actual = 10;
    final long absoluteError = expected - actual;

    // Order insensitive
    Assertions.assertTrue(EqualityUtils.areWithin(expected, actual, absoluteError));
    Assertions.assertTrue(EqualityUtils.areWithin(actual, expected, absoluteError));

    // Make the delta smaller
    final long delta = absoluteError - 1;
    Assertions.assertFalse(EqualityUtils.areWithin(expected, actual, delta));
    Assertions.assertFalse(EqualityUtils.areWithin(actual, expected, delta));

    // Make the gap bigger
    final long expected2 = expected + 1;
    Assertions.assertFalse(EqualityUtils.areWithin(expected2, actual, absoluteError));
    Assertions.assertFalse(EqualityUtils.areWithin(actual, expected2, absoluteError));
  }

  @Test
  public void testLongsGetDescriptionWithin() {
    // These must be distinguishable as strings
    for (final long absError : new long[] {0, 1, Long.MAX_VALUE}) {
      final String result = EqualityUtils.longsGetDescriptionWithin(absError);
      final String absString = String.valueOf(absError);
      assertMessageContains(result, "|v1-v2|", "Predicate description");
      assertMessageContains(result, (absError == 0) ? " == " : " <= ", "Predicate description");
      assertMessageContains(result, absString, "Absolute error");
    }
  }

  // int Within

  @Test
  public void testIntsAreWithinThrows() {
    final int expected = 0;
    final int actual = 0;
    // OK
    for (long absError : new long[] {0, 1, Integer.MAX_VALUE,
        EqualityUtils.MAX_INT_ABS_ERROR - 1}) {
      EqualityUtils.areWithin(expected, actual, absError);
    }
    // Bad
    for (long absError : new long[] {-1, Integer.MIN_VALUE, EqualityUtils.MAX_INT_ABS_ERROR}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        EqualityUtils.areWithin(expected, actual, absError);
      });
    }
  }

  @Test
  public void testIntsAreWithinUsingNoError() {
    // Test exact
    long absoluteError = 0;

    // Use a range of values
    for (int value : new int[] {0, 1, Integer.MAX_VALUE, Integer.MIN_VALUE}) {
      for (int i = 0; i < 2; i++) {
        value *= -1;
        Assertions.assertTrue(EqualityUtils.areWithin(value, value, absoluteError));
        Assertions.assertFalse(EqualityUtils.areWithin(value, value + 1, absoluteError));
        Assertions.assertFalse(EqualityUtils.areWithin(value, value - 1, absoluteError));
        Assertions.assertFalse(EqualityUtils.areWithin(value + 1, value, absoluteError));
        Assertions.assertFalse(EqualityUtils.areWithin(value - 1, value, absoluteError));
      }
    }
    // Test int overflow
    Assertions.assertFalse(EqualityUtils.areWithin(Integer.MAX_VALUE, -1, absoluteError));
    Assertions.assertFalse(EqualityUtils.areWithin(Integer.MIN_VALUE, 0, absoluteError));
    Assertions
        .assertFalse(EqualityUtils.areWithin(Integer.MAX_VALUE, Integer.MIN_VALUE, absoluteError));
  }

  @Test
  public void testIntsAreWithinUsingLargestError() {
    long absoluteError = EqualityUtils.MAX_INT_ABS_ERROR - 1;
    // Check largest range
    Assertions.assertTrue(
        EqualityUtils.areWithin(Integer.MAX_VALUE - 1, Integer.MIN_VALUE, absoluteError));
    Assertions.assertTrue(
        EqualityUtils.areWithin(Integer.MAX_VALUE, Integer.MIN_VALUE + 1, absoluteError));
    Assertions
        .assertFalse(EqualityUtils.areWithin(Integer.MAX_VALUE, Integer.MIN_VALUE, absoluteError));
  }

  @Test
  public void testIntsAreWithinUsingAbsoluteError() {
    final int expected = 20;
    final int actual = 10;
    final long absoluteError = expected - actual;

    // Order insensitive
    Assertions.assertTrue(EqualityUtils.areWithin(expected, actual, absoluteError));
    Assertions.assertTrue(EqualityUtils.areWithin(actual, expected, absoluteError));

    // Make the delta smaller
    final long delta = absoluteError - 1;
    Assertions.assertFalse(EqualityUtils.areWithin(expected, actual, delta));
    Assertions.assertFalse(EqualityUtils.areWithin(actual, expected, delta));

    // Make the gap bigger
    final int expected2 = expected + 1;
    Assertions.assertFalse(EqualityUtils.areWithin(expected2, actual, absoluteError));
    Assertions.assertFalse(EqualityUtils.areWithin(actual, expected2, absoluteError));
  }

  @Test
  public void testIntsGetDescriptionWithin() {
    // These must be distinguishable as strings
    for (long absError : new long[] {0, 1, Integer.MAX_VALUE,
        EqualityUtils.MAX_INT_ABS_ERROR - 1}) {
      final String result = EqualityUtils.intsGetDescriptionWithin(absError);
      final String absString = String.valueOf(absError);
      assertMessageContains(result, "|v1-v2|", "Predicate description");
      assertMessageContains(result, (absError == 0) ? " == " : " <= ", "Predicate description");
      assertMessageContains(result, absString, "Absolute error");
    }
  }

  // short Within

  @Test
  public void testShortsAreWithinThrows() {
    final short expected = 0;
    final short actual = 0;
    // OK
    for (int absError : new int[] {0, 1, Short.MAX_VALUE, EqualityUtils.MAX_SHORT_ABS_ERROR - 1}) {
      EqualityUtils.areWithin(expected, actual, absError);
    }
    // Bad
    for (int absError : new int[] {-1, Short.MIN_VALUE, EqualityUtils.MAX_SHORT_ABS_ERROR}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        EqualityUtils.areWithin(expected, actual, absError);
      });
    }
  }

  @Test
  public void testShortsAreWithinUsingNoError() {
    // Test exact
    int absoluteError = 0;

    // Use a range of values
    for (short value : new short[] {0, 1, Short.MAX_VALUE, Short.MIN_VALUE}) {
      for (short i = 0; i < 2; i++) {
        value *= -1;
        Assertions.assertTrue(EqualityUtils.areWithin(value, value, absoluteError));
        Assertions.assertFalse(EqualityUtils.areWithin(value, (short) (value + 1), absoluteError));
        Assertions.assertFalse(EqualityUtils.areWithin(value, (short) (value - 1), absoluteError));
        Assertions.assertFalse(EqualityUtils.areWithin((short) (value + 1), value, absoluteError));
        Assertions.assertFalse(EqualityUtils.areWithin((short) (value - 1), value, absoluteError));
      }
    }
    // Test short overflow
    Assertions.assertFalse(EqualityUtils.areWithin(Short.MAX_VALUE, (short) -1, absoluteError));
    Assertions.assertFalse(EqualityUtils.areWithin(Short.MIN_VALUE, (short) 0, absoluteError));
    Assertions
        .assertFalse(EqualityUtils.areWithin(Short.MAX_VALUE, Short.MIN_VALUE, absoluteError));
  }

  @Test
  public void testShortsAreWithinUsingLargestError() {
    int absoluteError = EqualityUtils.MAX_SHORT_ABS_ERROR - 1;
    // Check largest range
    Assertions.assertTrue(
        EqualityUtils.areWithin((short) (Short.MAX_VALUE - 1), Short.MIN_VALUE, absoluteError));
    Assertions.assertTrue(
        EqualityUtils.areWithin(Short.MAX_VALUE, (short) (Short.MIN_VALUE + 1), absoluteError));
    Assertions
        .assertFalse(EqualityUtils.areWithin(Short.MAX_VALUE, Short.MIN_VALUE, absoluteError));
  }

  @Test
  public void testShortsAreWithinUsingAbsoluteError() {
    final short expected = 20;
    final short actual = 10;
    final int absoluteError = expected - actual;

    // Order insensitive
    Assertions.assertTrue(EqualityUtils.areWithin(expected, actual, absoluteError));
    Assertions.assertTrue(EqualityUtils.areWithin(actual, expected, absoluteError));

    // Make the delta smaller
    final int delta = absoluteError - 1;
    Assertions.assertFalse(EqualityUtils.areWithin(expected, actual, delta));
    Assertions.assertFalse(EqualityUtils.areWithin(actual, expected, delta));

    // Make the gap bigger
    final short expected2 = expected + 1;
    Assertions.assertFalse(EqualityUtils.areWithin(expected2, actual, absoluteError));
    Assertions.assertFalse(EqualityUtils.areWithin(actual, expected2, absoluteError));
  }

  @Test
  public void testShortsGetDescriptionWithin() {
    // These must be distinguishable as strings
    for (int absError : new int[] {0, 1, Short.MAX_VALUE, EqualityUtils.MAX_SHORT_ABS_ERROR - 1}) {
      final String result = EqualityUtils.shortsGetDescriptionWithin(absError);
      final String absString = String.valueOf(absError);
      assertMessageContains(result, "|v1-v2|", "Predicate description");
      assertMessageContains(result, (absError == 0) ? " == " : " <= ", "Predicate description");
      assertMessageContains(result, absString, "Absolute error");
    }
  }

  // byte Within

  @Test
  public void testBytesAreWithinThrows() {
    final byte expected = 0;
    final byte actual = 0;
    // OK
    for (int absError : new int[] {0, 1, Byte.MAX_VALUE, EqualityUtils.MAX_BYTE_ABS_ERROR - 1}) {
      EqualityUtils.areWithin(expected, actual, absError);
    }
    // Bad
    for (int absError : new int[] {-1, Byte.MIN_VALUE, EqualityUtils.MAX_BYTE_ABS_ERROR}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        EqualityUtils.areWithin(expected, actual, absError);
      });
    }
  }

  @Test
  public void testBytesAreWithinUsingNoError() {
    // Test exact
    int absoluteError = 0;

    // Use a range of values
    for (byte value : new byte[] {0, 1, Byte.MAX_VALUE, Byte.MIN_VALUE}) {
      for (byte i = 0; i < 2; i++) {
        value *= -1;
        Assertions.assertTrue(EqualityUtils.areWithin(value, value, absoluteError));
        Assertions.assertFalse(EqualityUtils.areWithin(value, (byte) (value + 1), absoluteError));
        Assertions.assertFalse(EqualityUtils.areWithin(value, (byte) (value - 1), absoluteError));
        Assertions.assertFalse(EqualityUtils.areWithin((byte) (value + 1), value, absoluteError));
        Assertions.assertFalse(EqualityUtils.areWithin((byte) (value - 1), value, absoluteError));
      }
    }
    // Test byte overflow
    Assertions.assertFalse(EqualityUtils.areWithin(Byte.MAX_VALUE, (byte) -1, absoluteError));
    Assertions.assertFalse(EqualityUtils.areWithin(Byte.MIN_VALUE, (byte) 0, absoluteError));
    Assertions.assertFalse(EqualityUtils.areWithin(Byte.MAX_VALUE, Byte.MIN_VALUE, absoluteError));
  }

  @Test
  public void testBytesAreWithinUsingLargestError() {
    int absoluteError = EqualityUtils.MAX_BYTE_ABS_ERROR - 1;
    // Check largest range
    Assertions.assertTrue(
        EqualityUtils.areWithin((byte) (Byte.MAX_VALUE - 1), Byte.MIN_VALUE, absoluteError));
    Assertions.assertTrue(
        EqualityUtils.areWithin(Byte.MAX_VALUE, (byte) (Byte.MIN_VALUE + 1), absoluteError));
    Assertions.assertFalse(EqualityUtils.areWithin(Byte.MAX_VALUE, Byte.MIN_VALUE, absoluteError));
  }

  @Test
  public void testBytesAreWithinUsingAbsoluteError() {
    final byte expected = 20;
    final byte actual = 10;
    final int absoluteError = expected - actual;

    // Order insensitive
    Assertions.assertTrue(EqualityUtils.areWithin(expected, actual, absoluteError));
    Assertions.assertTrue(EqualityUtils.areWithin(actual, expected, absoluteError));

    // Make the delta smaller
    final int delta = absoluteError - 1;
    Assertions.assertFalse(EqualityUtils.areWithin(expected, actual, delta));
    Assertions.assertFalse(EqualityUtils.areWithin(actual, expected, delta));

    // Make the gap bigger
    final byte expected2 = expected + 1;
    Assertions.assertFalse(EqualityUtils.areWithin(expected2, actual, absoluteError));
    Assertions.assertFalse(EqualityUtils.areWithin(actual, expected2, absoluteError));
  }

  @Test
  public void testBytesGetDescriptionWithin() {
    // These must be distinguishable as strings
    for (int absError : new int[] {0, 1, Byte.MAX_VALUE, EqualityUtils.MAX_BYTE_ABS_ERROR - 1}) {
      final String result = EqualityUtils.bytesGetDescriptionWithin(absError);
      final String absString = String.valueOf(absError);
      assertMessageContains(result, "|v1-v2|", "Predicate description");
      assertMessageContains(result, (absError == 0) ? " == " : " <= ", "Predicate description");
      assertMessageContains(result, absString, "Absolute error");
    }
  }

  // float Close

  @Test
  public void testFloatsAreCloseThrows() {
    final float expected = 0;
    final float actual = 0;
    final double relativeError = 0;
    final double absoluteError = 0;

    for (float badRelativeError : new float[] {Float.NaN, Float.POSITIVE_INFINITY, -1, 2,
        Float.MAX_VALUE}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        EqualityUtils.areClose(expected, actual, badRelativeError, absoluteError);
      });
    }
    for (float badAbsoluteError : new float[] {Float.NaN, Float.POSITIVE_INFINITY, -1}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        EqualityUtils.areClose(expected, actual, relativeError, badAbsoluteError);
      });
    }
  }

  @Test
  public void testFloatsAreCloseUsingNoError() {
    final float relativeError = 0;
    final float absoluteError = 0;

    // Use a range of values
    for (float value : new float[] {0, 1, (float) Math.PI, Float.MAX_VALUE, Float.MIN_VALUE}) {
      for (int i = 0; i < 2; i++) {
        value *= -1;
        Assertions.assertTrue(EqualityUtils.areClose(value, value, relativeError, absoluteError));
        Assertions.assertFalse(
            EqualityUtils.areClose(value, Math.nextUp(value), relativeError, absoluteError));
        Assertions.assertFalse(
            EqualityUtils.areClose(value, Math.nextDown(value), relativeError, absoluteError));
        Assertions.assertFalse(
            EqualityUtils.areClose(Math.nextUp(value), value, relativeError, absoluteError));
        Assertions.assertFalse(
            EqualityUtils.areClose(Math.nextDown(value), value, relativeError, absoluteError));
      }
    }
    // Special floats are not equal as they are not within a real delta.
    for (final float value : new float[] {Float.NaN, Float.POSITIVE_INFINITY,
        Float.NEGATIVE_INFINITY}) {
      Assertions.assertFalse(EqualityUtils.areClose(value, value, relativeError, absoluteError));
    }
  }

  @Test
  public void testFloatsAreCloseUsingAbsoluteError() {
    final float expected = 2;
    final float actual = 1;
    final float relativeError = 0;
    final float absoluteError = expected - actual;

    // Order insensitive
    Assertions.assertTrue(EqualityUtils.areClose(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(EqualityUtils.areClose(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final float delta = Math.nextDown(absoluteError);
    Assertions.assertFalse(EqualityUtils.areClose(expected, actual, relativeError, delta));
    Assertions.assertFalse(EqualityUtils.areClose(actual, expected, relativeError, delta));

    // Make the gap bigger
    // Calling Math.nextDown(expected) creates a value that still is within 1 of 2 due to float
    // error.
    // So instead get the next delta above and subtract from actual.
    final float expected2 = actual - Math.nextUp(absoluteError);
    Assertions.assertFalse(EqualityUtils.areClose(expected2, actual, relativeError, absoluteError));
    Assertions.assertFalse(EqualityUtils.areClose(actual, expected2, relativeError, absoluteError));
  }

  @Test
  public void testFloatsAreCloseUsingRelativeError() {
    final float expected = 2;
    final float actual = 1;
    final double relativeError = 0.5;
    final float absoluteError = 0;

    // Order insensitive
    Assertions.assertTrue(EqualityUtils.areClose(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(EqualityUtils.areClose(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final double delta = Math.nextDown(relativeError);
    Assertions.assertFalse(EqualityUtils.areClose(expected, actual, delta, absoluteError));
    Assertions.assertFalse(EqualityUtils.areClose(actual, expected, delta, absoluteError));

    // Make the gap bigger
    // Calling Math.nextDown(expected) creates a value that still is within 1 of 2 due to float
    // error.
    // So instead get the next delta above and subtract from actual.
    final float expected2 = actual - Math.nextUp(expected - actual);
    Assertions.assertFalse(EqualityUtils.areClose(expected2, actual, relativeError, absoluteError));
    Assertions.assertFalse(EqualityUtils.areClose(actual, expected2, relativeError, absoluteError));
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

    // The method assumes the errors must pass the validation in checkErrors(...)
    try {
      EqualityUtils.floatsValidateClose(inputRelError, inputAbsError);
    } catch (final IllegalArgumentException ex) {
      return;
    }

    // The raw input
    final String result = EqualityUtils.floatsGetDescriptionClose(inputRelError, inputAbsError);

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

  // double Close

  @Test
  public void testDoublesAreCloseThrows() {
    final double expected = 0;
    final double actual = 0;
    final double relativeError = 0;
    final double absoluteError = 0;

    for (double badRelativeError : new double[] {Double.NaN, Double.POSITIVE_INFINITY, -1, 2,
        Double.MAX_VALUE}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        EqualityUtils.areClose(expected, actual, badRelativeError, absoluteError);
      });
    }
    for (double badAbsoluteError : new double[] {Double.NaN, Double.POSITIVE_INFINITY, -1}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        EqualityUtils.areClose(expected, actual, relativeError, badAbsoluteError);
      });
    }
  }

  @Test
  public void testDoublesAreCloseUsingNoError() {
    final double relativeError = 0;
    final double absoluteError = 0;

    // Use a range of values
    for (double value : new double[] {0, 1, Math.PI, Double.MAX_VALUE, Double.MIN_VALUE}) {
      for (int i = 0; i < 2; i++) {
        value *= -1;
        Assertions.assertTrue(EqualityUtils.areClose(value, value, relativeError, absoluteError));
        Assertions.assertFalse(
            EqualityUtils.areClose(value, Math.nextUp(value), relativeError, absoluteError));
        Assertions.assertFalse(
            EqualityUtils.areClose(value, Math.nextDown(value), relativeError, absoluteError));
        Assertions.assertFalse(
            EqualityUtils.areClose(Math.nextUp(value), value, relativeError, absoluteError));
        Assertions.assertFalse(
            EqualityUtils.areClose(Math.nextDown(value), value, relativeError, absoluteError));
      }
    }
    // Special doubles are not equal as they are not within a real delta.
    for (final double value : new double[] {Double.NaN, Double.POSITIVE_INFINITY,
        Double.NEGATIVE_INFINITY}) {
      Assertions.assertFalse(EqualityUtils.areClose(value, value, relativeError, absoluteError));
    }
  }

  @Test
  public void testDoublesAreCloseUsingAbsoluteError() {
    final double expected = 2;
    final double actual = 1;
    final double relativeError = 0;
    final double absoluteError = expected - actual;

    // Order insensitive
    Assertions.assertTrue(EqualityUtils.areClose(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(EqualityUtils.areClose(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final double delta = Math.nextDown(absoluteError);
    Assertions.assertFalse(EqualityUtils.areClose(expected, actual, relativeError, delta));
    Assertions.assertFalse(EqualityUtils.areClose(actual, expected, relativeError, delta));

    // Make the gap bigger
    // Calling Math.nextDown(expected) creates a value that still is within 1 of 2 due to float
    // error.
    // So instead get the next delta above and subtract from actual.
    final double expected2 = actual - Math.nextUp(absoluteError);
    Assertions.assertFalse(EqualityUtils.areClose(expected2, actual, relativeError, absoluteError));
    Assertions.assertFalse(EqualityUtils.areClose(actual, expected2, relativeError, absoluteError));
  }

  @Test
  public void testDoublesAreCloseUsingRelativeError() {
    final double expected = 2;
    final double actual = 1;
    final double relativeError = 0.5;
    final double absoluteError = 0;

    // Order insensitive
    Assertions.assertTrue(EqualityUtils.areClose(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(EqualityUtils.areClose(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final double delta = Math.nextDown(relativeError);
    Assertions.assertFalse(EqualityUtils.areClose(expected, actual, delta, absoluteError));
    Assertions.assertFalse(EqualityUtils.areClose(actual, expected, delta, absoluteError));

    // Make the gap bigger
    // Calling Math.nextDown(expected) creates a value that still is within 1 of 2 due to float
    // error.
    // So instead get the next delta above and subtract from actual.
    final double expected2 = actual - Math.nextUp(expected - actual);
    Assertions.assertFalse(EqualityUtils.areClose(expected2, actual, relativeError, absoluteError));
    Assertions.assertFalse(EqualityUtils.areClose(actual, expected2, relativeError, absoluteError));
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

    // The method assumes the errors must pass the validation in checkErrors(...)
    try {
      EqualityUtils.doublesValidateClose(inputRelError, inputAbsError);
    } catch (final IllegalArgumentException ex) {
      return;
    }

    // The raw input
    final String result = EqualityUtils.doublesGetDescriptionClose(inputRelError, inputAbsError);

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

  // float IsCloseTo

  @Test
  public void testFloatsIsCloseToThrows() {
    final float expected = 0;
    final float actual = 0;
    final double relativeError = 0;
    final double absoluteError = 0;

    for (float badRelativeError : new float[] {Float.NaN, Float.POSITIVE_INFINITY, -1}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        EqualityUtils.isCloseTo(expected, actual, badRelativeError, absoluteError);
      });
    }
    for (float badAbsoluteError : new float[] {Float.NaN, Float.POSITIVE_INFINITY, -1}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        EqualityUtils.isCloseTo(expected, actual, relativeError, badAbsoluteError);
      });
    }
  }

  @Test
  public void testFloatsIsCloseToUsingNoError() {
    final float relativeError = 0;
    final float absoluteError = 0;

    // Use a range of values
    for (float value : new float[] {0, 1, (float) Math.PI, Float.MAX_VALUE, Float.MIN_VALUE}) {
      for (int i = 0; i < 2; i++) {
        value *= -1;
        Assertions.assertTrue(EqualityUtils.isCloseTo(value, value, relativeError, absoluteError));
        Assertions.assertFalse(
            EqualityUtils.isCloseTo(value, Math.nextUp(value), relativeError, absoluteError));
        Assertions.assertFalse(
            EqualityUtils.isCloseTo(value, Math.nextDown(value), relativeError, absoluteError));
        Assertions.assertFalse(
            EqualityUtils.isCloseTo(Math.nextUp(value), value, relativeError, absoluteError));
        Assertions.assertFalse(
            EqualityUtils.isCloseTo(Math.nextDown(value), value, relativeError, absoluteError));
      }
    }
    // Special floats are not equal as they are not within a real delta.
    for (final float value : new float[] {Float.NaN, Float.POSITIVE_INFINITY,
        Float.NEGATIVE_INFINITY}) {
      Assertions.assertFalse(EqualityUtils.isCloseTo(value, value, relativeError, absoluteError));
    }
  }

  @Test
  public void testFloatsIsCloseToUsingAbsoluteError() {
    final float expected = 2;
    final float actual = 1;
    final float relativeError = 0;
    final float absoluteError = expected - actual;

    // Order insensitive
    Assertions.assertTrue(EqualityUtils.isCloseTo(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(EqualityUtils.isCloseTo(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final float delta = Math.nextDown(absoluteError);
    Assertions.assertFalse(EqualityUtils.isCloseTo(expected, actual, relativeError, delta));
    Assertions.assertFalse(EqualityUtils.isCloseTo(actual, expected, relativeError, delta));

    // Make the gap bigger
    // Calling Math.nextDown(expected) creates a value that still is within 1 of 2 due to float
    // error.
    // So instead get the next delta above and subtract from actual.
    final float expected2 = actual - Math.nextUp(absoluteError);
    Assertions
        .assertFalse(EqualityUtils.isCloseTo(expected2, actual, relativeError, absoluteError));
    Assertions
        .assertFalse(EqualityUtils.isCloseTo(actual, expected2, relativeError, absoluteError));
  }

  @Test
  public void testFloatsIsCloseToUsingRelativeError() {
    final float expected = 2;
    final float actual = 1;
    final double relativeError = 0.5;
    final float absoluteError = 0;

    // Order sensitive
    Assertions.assertTrue(EqualityUtils.isCloseTo(expected, actual, relativeError, absoluteError));
    Assertions.assertFalse(EqualityUtils.isCloseTo(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final double delta = Math.nextDown(relativeError);
    Assertions.assertFalse(EqualityUtils.isCloseTo(expected, actual, delta, absoluteError));

    // Make the gap bigger
    // Calling Math.nextDown(expected) creates a value that still is within 1 of 2 due to float
    // error.
    // So instead get the next delta above and subtract from actual.
    final float expected2 = actual - Math.nextUp(expected - actual);
    Assertions
        .assertFalse(EqualityUtils.isCloseTo(expected2, actual, relativeError, absoluteError));
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

    // The method assumes the errors must pass the validation in checkErrors(...)
    try {
      EqualityUtils.floatsValidateClose(inputRelError, inputAbsError);
    } catch (final IllegalArgumentException ex) {
      return;
    }

    // The raw input
    final String result = EqualityUtils.floatsGetDescriptionIsCloseTo(inputRelError, inputAbsError);

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

  // double Close

  @Test
  public void testDoublesIsCloseToThrows() {
    final double expected = 0;
    final double actual = 0;
    final double relativeError = 0;
    final double absoluteError = 0;

    for (double badRelativeError : new double[] {Double.NaN, Double.POSITIVE_INFINITY, -1}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        EqualityUtils.isCloseTo(expected, actual, badRelativeError, absoluteError);
      });
    }
    for (double badAbsoluteError : new double[] {Double.NaN, Double.POSITIVE_INFINITY, -1}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        EqualityUtils.isCloseTo(expected, actual, relativeError, badAbsoluteError);
      });
    }
  }

  @Test
  public void testDoublesIsCloseToUsingNoError() {
    final double relativeError = 0;
    final double absoluteError = 0;

    // Use a range of values
    for (double value : new double[] {0, 1, Math.PI, Double.MAX_VALUE, Double.MIN_VALUE}) {
      for (int i = 0; i < 2; i++) {
        value *= -1;
        Assertions.assertTrue(EqualityUtils.isCloseTo(value, value, relativeError, absoluteError));
        Assertions.assertFalse(
            EqualityUtils.isCloseTo(value, Math.nextUp(value), relativeError, absoluteError));
        Assertions.assertFalse(
            EqualityUtils.isCloseTo(value, Math.nextDown(value), relativeError, absoluteError));
        Assertions.assertFalse(
            EqualityUtils.isCloseTo(Math.nextUp(value), value, relativeError, absoluteError));
        Assertions.assertFalse(
            EqualityUtils.isCloseTo(Math.nextDown(value), value, relativeError, absoluteError));
      }
    }
    // Special doubles are not equal as they are not within a real delta.
    for (final double value : new double[] {Double.NaN, Double.POSITIVE_INFINITY,
        Double.NEGATIVE_INFINITY}) {
      Assertions.assertFalse(EqualityUtils.isCloseTo(value, value, relativeError, absoluteError));
    }
  }

  @Test
  public void testDoublesIsCloseToUsingAbsoluteError() {
    final double expected = 2;
    final double actual = 1;
    final double relativeError = 0;
    final double absoluteError = expected - actual;

    // Order insensitive
    Assertions.assertTrue(EqualityUtils.isCloseTo(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(EqualityUtils.isCloseTo(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final double delta = Math.nextDown(absoluteError);
    Assertions.assertFalse(EqualityUtils.isCloseTo(expected, actual, relativeError, delta));
    Assertions.assertFalse(EqualityUtils.isCloseTo(actual, expected, relativeError, delta));

    // Make the gap bigger
    // Calling Math.nextDown(expected) creates a value that still is within 1 of 2 due to float
    // error.
    // So instead get the next delta above and subtract from actual.
    final double expected2 = actual - Math.nextUp(absoluteError);
    Assertions
        .assertFalse(EqualityUtils.isCloseTo(expected2, actual, relativeError, absoluteError));
    Assertions
        .assertFalse(EqualityUtils.isCloseTo(actual, expected2, relativeError, absoluteError));
  }

  @Test
  public void testDoublesIsCloseToUsingRelativeError() {
    final double expected = 2;
    final double actual = 1;
    final double relativeError = 0.5;
    final double absoluteError = 0;

    // Order sensitive
    Assertions.assertTrue(EqualityUtils.isCloseTo(expected, actual, relativeError, absoluteError));
    Assertions.assertFalse(EqualityUtils.isCloseTo(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final double delta = Math.nextDown(relativeError);
    Assertions.assertFalse(EqualityUtils.isCloseTo(expected, actual, delta, absoluteError));

    // Make the gap bigger
    // Calling Math.nextDown(expected) creates a value that still is within 1 of 2 due to float
    // error.
    // So instead get the next delta above and subtract from actual.
    final double expected2 = actual - Math.nextUp(expected - actual);
    Assertions
        .assertFalse(EqualityUtils.isCloseTo(expected2, actual, relativeError, absoluteError));
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

    // The method assumes the errors must pass the validation in checkErrors(...)
    try {
      EqualityUtils.doublesValidateClose(inputRelError, inputAbsError);
    } catch (final IllegalArgumentException ex) {
      return;
    }

    // The raw input
    final String result =
        EqualityUtils.doublesGetDescriptionIsCloseTo(inputRelError, inputAbsError);

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
}
