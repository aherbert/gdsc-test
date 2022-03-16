/*-
 * #%L
 * Genome Damage and Stability Centre Test API
 *
 * Contains a generic framework for test predicates.
 * %%
 * Copyright (C) 2018 - 2020 Alex Herbert
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
class EqualityTest {

  private static final BigInteger MAX_LONG_DELTA =
      BigInteger.valueOf(Long.MAX_VALUE).subtract(BigInteger.valueOf(Long.MIN_VALUE));

  private static long nextUp(long value) {
    return ++value;
  }

  private static int nextUp(int value) {
    return ++value;
  }

  private static short nextUp(short value) {
    return ++value;
  }

  private static byte nextUp(byte value) {
    return ++value;
  }

  private static long nextDown(long value) {
    return --value;
  }

  private static int nextDown(int value) {
    return --value;
  }

  private static short nextDown(short value) {
    return --value;
  }

  private static byte nextDown(byte value) {
    return --value;
  }

  @Test
  void testFloatAreEqual() {
    final float[] values = {0, 1, 1.5f, (float) Math.PI, Float.NaN, Float.POSITIVE_INFINITY,
        Float.NEGATIVE_INFINITY, Float.MAX_VALUE, Float.MIN_VALUE};
    for (final float v1 : values) {
      for (final float v2 : values) {
        Assertions.assertEquals(Float.compare(v1, v2) == 0, FloatEquality.areEqual(v1, v2));
      }
    }
  }

  @Test
  void testDoubleAreEqual() {
    final double[] values = {0, 1, 1.5, Math.PI, Double.NaN, Double.POSITIVE_INFINITY,
        Double.NEGATIVE_INFINITY, Double.MAX_VALUE, Double.MIN_VALUE};
    for (final double v1 : values) {
      for (final double v2 : values) {
        Assertions.assertEquals(Double.compare(v1, v2) == 0, DoubleEquality.areEqual(v1, v2));
      }
    }
  }

  @Test
  void testLongAreEqual() {
    final long[] values = {0, 1, Long.MAX_VALUE, Long.MIN_VALUE};
    for (final long v1 : values) {
      for (final long v2 : values) {
        Assertions.assertEquals(Long.compare(v1, v2) == 0, LongEquality.areEqual(v1, v2));
      }
    }
  }

  @Test
  void testIntegerAreEqual() {
    final int[] values = {0, 1, Integer.MAX_VALUE, Integer.MIN_VALUE};
    for (final int v1 : values) {
      for (final int v2 : values) {
        Assertions.assertEquals(Integer.compare(v1, v2) == 0, IntEquality.areEqual(v1, v2));
      }
    }
  }

  @Test
  void testShortAreEqual() {
    final short[] values = {0, 1, Short.MAX_VALUE, Short.MIN_VALUE};
    for (final short v1 : values) {
      for (final short v2 : values) {
        Assertions.assertEquals(Short.compare(v1, v2) == 0, ShortEquality.areEqual(v1, v2));
      }
    }
  }

  @Test
  void testByteAreEqual() {
    final byte[] values = {0, 1, Byte.MAX_VALUE, Byte.MIN_VALUE};
    for (final byte v1 : values) {
      for (final byte v2 : values) {
        Assertions.assertEquals(Byte.compare(v1, v2) == 0, ByteEquality.areEqual(v1, v2));
      }
    }
  }

  // float WithinUlp

  @Test
  void testFloatsAreWithinUlpThrows() {
    final float expected = 0;
    final float actual = 0;
    // OK
    for (short ulpError : new short[] {0, 1, 2, Short.MAX_VALUE}) {
      FloatEquality.areWithinUlp(expected, actual, ulpError);
    }
    // Bad
    for (short ulpError : new short[] {-1, -2, Short.MIN_VALUE}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        FloatEquality.areWithinUlp(expected, actual, ulpError);
      });
    }
  }

  @Test
  void testFloatsAreWithinUlpUsingNoError() {
    // Test exact
    short ulpError = 0;

    // Use a range of values
    for (float value : new float[] {1e-10f, 1, (float) Math.PI, Float.MAX_VALUE, Float.MIN_VALUE}) {
      for (int i = 0; i < 2; i++) {
        value *= -1;
        Assertions.assertTrue(FloatEquality.areWithinUlp(value, value, ulpError));
        assertFloatsAreWithin(value, Math.nextUp(value), ulpError, false);
        assertFloatsAreWithin(value, Math.nextDown(value), ulpError, false);
        assertFloatsAreWithin(-value, Math.nextUp(-value), ulpError, false);
        assertFloatsAreWithin(-value, Math.nextDown(-value), ulpError, false);
      }
    }
    // Special case for zero
    assertFloatsAreWithin(-0.0f, 0.0f, ulpError, true);
  }

  @Test
  void testFloatsAreWithinUlpUsingUlpError() {
    final short ulpError = 3;
    final float expected = 2;
    float actual1 = expected;
    float actual2 = expected;
    for (int i = 0; i < ulpError; i++) {
      actual1 = Math.nextDown(actual1);
      actual2 = Math.nextUp(actual2);
      assertFloatsAreWithin(expected, actual1, ulpError, true);
      assertFloatsAreWithin(expected, actual2, ulpError, true);
    }

    // Gap too big
    for (int i = 0; i < ulpError; i++) {
      actual1 = Math.nextDown(actual1);
      actual2 = Math.nextUp(actual2);
      assertFloatsAreWithin(expected, actual1, ulpError, false);
      assertFloatsAreWithin(expected, actual2, ulpError, false);
    }
  }

  @Test
  void testFloatsAreWithinUlpInfiniteCases() {
    final short zero = 0;
    final short one = 1;
    assertFloatsAreWithin(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, zero, true);
    assertFloatsAreWithin(Float.POSITIVE_INFINITY, Float.MAX_VALUE, zero, false);
    assertFloatsAreWithin(Float.POSITIVE_INFINITY, Float.MAX_VALUE, one, true);
    assertFloatsAreWithin(-Float.POSITIVE_INFINITY, -Float.POSITIVE_INFINITY, zero, true);
    assertFloatsAreWithin(-Float.POSITIVE_INFINITY, -Float.MAX_VALUE, zero, false);
    assertFloatsAreWithin(-Float.POSITIVE_INFINITY, -Float.MAX_VALUE, one, true);
    assertFloatsAreWithin(-Float.POSITIVE_INFINITY, Float.MAX_VALUE, Short.MAX_VALUE, false);
    assertFloatsAreWithin(-Float.MAX_VALUE, Float.MAX_VALUE, Short.MAX_VALUE, false);
    assertFloatsAreWithin(-Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Short.MAX_VALUE,
        false);
  }

  @Test
  void testFloatsAreWithinUlpNaNCases() {
    // Attempt to put a sign bit into a NaN to hit edge cases.
    // This may not be supported depending on the JDK and the result of
    // intBitsToFloat may or may not preserve NaN bit patterns.
    final float signedNaN =
        Float.intBitsToFloat(Integer.MIN_VALUE | Float.floatToRawIntBits(Float.NaN));
    for (short ulpError : new short[] {0, 1, Short.MAX_VALUE}) {
      for (final float value : new float[] {Float.NaN, Float.POSITIVE_INFINITY, Float.MAX_VALUE, 42,
          0}) {
        assertFloatsAreWithin(value, Float.NaN, ulpError, false);
        assertFloatsAreWithin(-value, Float.NaN, ulpError, false);
        assertFloatsAreWithin(-value, signedNaN, ulpError, false);
        assertFloatsAreWithin(value, signedNaN, ulpError, false);
      }
    }
  }

  @Test
  void testFloatsAreWithinUlpZeroCases() {
    final short zero = 0;
    final short one = 1;
    final short two = 2;
    final short three = 3;
    assertFloatsAreWithin(-0.0f, 0.0f, zero, true);
    assertFloatsAreWithin(-0.0f, Float.MIN_VALUE, zero, false);
    assertFloatsAreWithin(-0.0f, Float.MIN_VALUE, one, true);
    assertFloatsAreWithin(-0.0f, 2 * Float.MIN_VALUE, one, false);
    assertFloatsAreWithin(-0.0f, 2 * Float.MIN_VALUE, two, true);
    assertFloatsAreWithin(-Float.MIN_VALUE, 2 * Float.MIN_VALUE, two, false);
    assertFloatsAreWithin(-Float.MIN_VALUE, 2 * Float.MIN_VALUE, three, true);
    // Reverse sign
    assertFloatsAreWithin(0.0f, -0.0f, zero, true);
    assertFloatsAreWithin(0.0f, -Float.MIN_VALUE, zero, false);
    assertFloatsAreWithin(0.0f, -Float.MIN_VALUE, one, true);
    assertFloatsAreWithin(0.0f, -2 * Float.MIN_VALUE, one, false);
    assertFloatsAreWithin(0.0f, -2 * Float.MIN_VALUE, two, true);
    assertFloatsAreWithin(Float.MIN_VALUE, -2 * Float.MIN_VALUE, two, false);
    assertFloatsAreWithin(Float.MIN_VALUE, -2 * Float.MIN_VALUE, three, true);
  }

  private static void assertFloatsAreWithin(float v1, float v2, short ulps, boolean expected) {
    Assertions.assertEquals(expected, FloatEquality.areWithinUlp(v1, v2, ulps));
    Assertions.assertEquals(expected, FloatEquality.areWithinUlp(v2, v1, ulps));
  }

  // double WithinUlp

  @Test
  void testDoublesAreWithinUlpThrows() {
    final double expected = 0;
    final double actual = 0;
    // OK
    for (int ulpError : new int[] {0, 1, 2, Integer.MAX_VALUE}) {
      DoubleEquality.areWithinUlp(expected, actual, ulpError);
    }
    // Bad
    for (int ulpError : new int[] {-1, -2, Integer.MIN_VALUE}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        DoubleEquality.areWithinUlp(expected, actual, ulpError);
      });
    }
  }

  @Test
  void testDoublesAreWithinUlpUsingNoError() {
    // Test exact
    int ulpError = 0;

    // Use a range of values
    for (double value : new double[] {1e-10, 1, Math.PI, Double.MAX_VALUE, Double.MIN_VALUE}) {
      for (int i = 0; i < 2; i++) {
        value *= -1;
        Assertions.assertTrue(DoubleEquality.areWithinUlp(value, value, ulpError));
        assertDoublesAreWithin(value, Math.nextUp(value), ulpError, false);
        assertDoublesAreWithin(value, Math.nextDown(value), ulpError, false);
        assertDoublesAreWithin(-value, Math.nextUp(-value), ulpError, false);
        assertDoublesAreWithin(-value, Math.nextDown(-value), ulpError, false);
      }
    }
    // Special case for zero
    assertDoublesAreWithin(-0.0, 0.0, ulpError, true);
  }

  @Test
  void testDoublesAreWithinUlpUsingUlpError() {
    final int ulpError = 3;
    final double expected = 2;
    double actual1 = expected;
    double actual2 = expected;
    for (int i = 0; i < ulpError; i++) {
      actual1 = Math.nextDown(actual1);
      actual2 = Math.nextUp(actual2);
      assertDoublesAreWithin(expected, actual1, ulpError, true);
      assertDoublesAreWithin(expected, actual2, ulpError, true);
    }

    // Gap too big
    for (int i = 0; i < ulpError; i++) {
      actual1 = Math.nextDown(actual1);
      actual2 = Math.nextUp(actual2);
      assertDoublesAreWithin(expected, actual1, ulpError, false);
      assertDoublesAreWithin(expected, actual2, ulpError, false);
    }
  }

  @Test
  void testDoublesAreWithinUlpInfiniteCases() {
    assertDoublesAreWithin(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, 0, true);
    assertDoublesAreWithin(Double.POSITIVE_INFINITY, Double.MAX_VALUE, 0, false);
    assertDoublesAreWithin(Double.POSITIVE_INFINITY, Double.MAX_VALUE, 1, true);
    assertDoublesAreWithin(-Double.POSITIVE_INFINITY, -Double.POSITIVE_INFINITY, 0, true);
    assertDoublesAreWithin(-Double.POSITIVE_INFINITY, -Double.MAX_VALUE, 0, false);
    assertDoublesAreWithin(-Double.POSITIVE_INFINITY, -Double.MAX_VALUE, 1, true);
    assertDoublesAreWithin(-Double.POSITIVE_INFINITY, Double.MAX_VALUE, Integer.MAX_VALUE, false);
    assertDoublesAreWithin(-Double.MAX_VALUE, Double.MAX_VALUE, Integer.MAX_VALUE, false);
    assertDoublesAreWithin(-Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Integer.MAX_VALUE,
        false);
  }

  @Test
  void testDoublesAreWithinUlpNaNCases() {
    // Attempt to put a sign bit into a NaN to hit edge cases.
    // This may not be supported depending on the JDK and the result of
    // intBitsToFloat may or may not preserve NaN bit patterns.
    final double signedNaN =
        Double.longBitsToDouble(Integer.MIN_VALUE | Double.doubleToRawLongBits(Double.NaN));
    for (int ulpError : new int[] {0, 1, Integer.MAX_VALUE}) {
      for (final double value : new double[] {Double.NaN, Double.POSITIVE_INFINITY,
          Double.MAX_VALUE, 42, 0}) {
        assertDoublesAreWithin(value, Double.NaN, ulpError, false);
        assertDoublesAreWithin(-value, Double.NaN, ulpError, false);
        assertDoublesAreWithin(-value, signedNaN, ulpError, false);
        assertDoublesAreWithin(value, signedNaN, ulpError, false);
      }
    }
  }

  @Test
  void testDoublesAreWithinUlpZeroCases() {
    assertDoublesAreWithin(-0.0, 0.0, 0, true);
    assertDoublesAreWithin(-0.0, Double.MIN_VALUE, 0, false);
    assertDoublesAreWithin(-0.0, Double.MIN_VALUE, 1, true);
    assertDoublesAreWithin(-0.0, 2 * Double.MIN_VALUE, 1, false);
    assertDoublesAreWithin(-0.0, 2 * Double.MIN_VALUE, 2, true);
    assertDoublesAreWithin(-Double.MIN_VALUE, 2 * Double.MIN_VALUE, 2, false);
    assertDoublesAreWithin(-Double.MIN_VALUE, 2 * Double.MIN_VALUE, 3, true);
    // Reverse sign
    assertDoublesAreWithin(0.0, -0.0, 0, true);
    assertDoublesAreWithin(0.0, -Double.MIN_VALUE, 0, false);
    assertDoublesAreWithin(0.0, -Double.MIN_VALUE, 1, true);
    assertDoublesAreWithin(0.0, -2 * Double.MIN_VALUE, 1, false);
    assertDoublesAreWithin(0.0, -2 * Double.MIN_VALUE, 2, true);
    assertDoublesAreWithin(Double.MIN_VALUE, -2 * Double.MIN_VALUE, 2, false);
    assertDoublesAreWithin(Double.MIN_VALUE, -2 * Double.MIN_VALUE, 3, true);
  }

  private static void assertDoublesAreWithin(double v1, double v2, int ulps, boolean expected) {
    Assertions.assertEquals(expected, DoubleEquality.areWithinUlp(v1, v2, ulps));
    Assertions.assertEquals(expected, DoubleEquality.areWithinUlp(v2, v1, ulps));
  }

  // float Within

  @Test
  void testFloatsAreWithinThrows() {
    final float expected = 0;
    final float actual = 0;
    // OK
    for (float absError : new float[] {-0f, 0, 1, (float) Math.PI, Float.MAX_VALUE}) {
      FloatEquality.areWithin(expected, actual, absError);
    }
    // Bad
    for (float absError : new float[] {-1, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY,
        Float.NaN}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        FloatEquality.areWithin(expected, actual, absError);
      });
    }
  }

  @Test
  void testFloatsAreWithinUsingNoError() {
    // Test exact
    float absoluteError = 0;

    // Use a range of values
    for (float value : new float[] {0, 1, (float) Math.PI, Float.MAX_VALUE, Float.MIN_VALUE}) {
      for (int i = 0; i < 2; i++) {
        value *= -1;
        Assertions.assertTrue(FloatEquality.areWithin(value, value, absoluteError));
        Assertions.assertFalse(FloatEquality.areWithin(value, Math.nextUp(value), absoluteError));
        Assertions.assertFalse(FloatEquality.areWithin(value, Math.nextDown(value), absoluteError));
        Assertions.assertFalse(FloatEquality.areWithin(Math.nextUp(value), value, absoluteError));
        Assertions.assertFalse(FloatEquality.areWithin(Math.nextDown(value), value, absoluteError));
      }
    }
    // Special floats are not equal as they are not within a real delta.
    for (final float value : new float[] {Float.NaN, Float.POSITIVE_INFINITY,
        Float.NEGATIVE_INFINITY}) {
      Assertions.assertFalse(FloatEquality.areWithin(value, value, absoluteError));
    }
  }

  @Test
  void testFloatsAreWithinUsingAbsoluteError() {
    final float expected = 2;
    final float actual = 1;
    final float absoluteError = expected - actual;

    // Order insensitive
    Assertions.assertTrue(FloatEquality.areWithin(expected, actual, absoluteError));
    Assertions.assertTrue(FloatEquality.areWithin(actual, expected, absoluteError));

    // Make the delta smaller
    final float delta = Math.nextDown(absoluteError);
    Assertions.assertFalse(FloatEquality.areWithin(expected, actual, delta));
    Assertions.assertFalse(FloatEquality.areWithin(actual, expected, delta));

    // Make the gap bigger
    // Calling Math.nextDown(expected) creates a value that still is within 1 of 2 due to float
    // error.
    // So instead get the next delta above and subtract from actual.
    final float expected2 = actual - Math.nextUp(absoluteError);
    Assertions.assertFalse(FloatEquality.areWithin(expected2, actual, absoluteError));
    Assertions.assertFalse(FloatEquality.areWithin(actual, expected2, absoluteError));
  }

  // double Within

  @Test
  void testDoublesAreWithinThrows() {
    final double expected = 0;
    final double actual = 0;
    // OK
    for (double absError : new double[] {-0.0, 0, 1, Math.PI, Double.MAX_VALUE}) {
      DoubleEquality.areWithin(expected, actual, absError);
    }
    // Bad
    for (double absError : new double[] {-1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,
        Double.NaN}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        DoubleEquality.areWithin(expected, actual, absError);
      });
    }
  }

  @Test
  void testDoublesAreWithinUsingNoError() {
    // Test exact
    double absoluteError = 0;

    // Use a range of values
    for (double value : new double[] {0, 1, Math.PI, Double.MAX_VALUE, Double.MIN_VALUE}) {
      for (int i = 0; i < 2; i++) {
        value *= -1;
        Assertions.assertTrue(DoubleEquality.areWithin(value, value, absoluteError));
        Assertions.assertFalse(DoubleEquality.areWithin(value, Math.nextUp(value), absoluteError));
        Assertions
            .assertFalse(DoubleEquality.areWithin(value, Math.nextDown(value), absoluteError));
        Assertions.assertFalse(DoubleEquality.areWithin(Math.nextUp(value), value, absoluteError));
        Assertions
            .assertFalse(DoubleEquality.areWithin(Math.nextDown(value), value, absoluteError));
      }
    }
    // Special doubles are not equal as they are not within a real delta.
    for (final double value : new double[] {Double.NaN, Double.POSITIVE_INFINITY,
        Double.NEGATIVE_INFINITY}) {
      Assertions.assertFalse(DoubleEquality.areWithin(value, value, absoluteError));
    }
  }

  @Test
  void testDoublesAreWithinUsingAbsoluteError() {
    final double expected = 2;
    final double actual = 1;
    final double absoluteError = expected - actual;

    // Order insensitive
    Assertions.assertTrue(DoubleEquality.areWithin(expected, actual, absoluteError));
    Assertions.assertTrue(DoubleEquality.areWithin(actual, expected, absoluteError));

    // Make the delta smaller
    final double delta = Math.nextDown(absoluteError);
    Assertions.assertFalse(DoubleEquality.areWithin(expected, actual, delta));
    Assertions.assertFalse(DoubleEquality.areWithin(actual, expected, delta));

    // Make the gap bigger
    // Calling Math.nextDown(expected) creates a value that still is within 1 of 2 due to float
    // error.
    // So instead get the next delta above and subtract from actual.
    final double expected2 = actual - Math.nextUp(absoluteError);
    Assertions.assertFalse(DoubleEquality.areWithin(expected2, actual, absoluteError));
    Assertions.assertFalse(DoubleEquality.areWithin(actual, expected2, absoluteError));
  }

  // long Within using BigInteger

  @Test
  void testLongsUsingBigIntegerAreWithinThrows() {
    final long expected = 0;
    final long actual = 0;

    // OK
    for (BigInteger absError : new BigInteger[] {BigInteger.ZERO, BigInteger.ONE,
        BigInteger.valueOf(Long.MAX_VALUE),
        // Bigger than a long
        BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE),
        // Biggest allowed difference between longs
        MAX_LONG_DELTA.subtract(BigInteger.ONE)}) {
      LongEquality.areWithin(expected, actual, absError);
    }
    // Bad
    for (BigInteger absError : new BigInteger[] {BigInteger.ONE.negate(),
        BigInteger.valueOf(Long.MIN_VALUE), MAX_LONG_DELTA}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        LongEquality.areWithin(expected, actual, absError);
      });
    }
  }

  @Test
  void testLongsUsingBigIntegerAreWithinUsingNoError() {
    // Test exact
    BigInteger absoluteError = BigInteger.ZERO;

    // Use a range of values
    for (long value : new long[] {0, 1, Long.MAX_VALUE, Long.MIN_VALUE}) {
      for (int i = 0; i < 2; i++) {
        value *= -1;
        Assertions.assertTrue(LongEquality.areWithin(value, value, absoluteError));
        Assertions.assertFalse(LongEquality.areWithin(value, nextUp(value), absoluteError));
        Assertions.assertFalse(LongEquality.areWithin(value, nextDown(value), absoluteError));
        Assertions.assertFalse(LongEquality.areWithin(nextUp(value), value, absoluteError));
        Assertions.assertFalse(LongEquality.areWithin(nextDown(value), value, absoluteError));
      }
    }
    // Test overflow of longs
    Assertions.assertFalse(LongEquality.areWithin(Long.MAX_VALUE, -1, absoluteError));
    Assertions.assertFalse(LongEquality.areWithin(Long.MIN_VALUE, 0, absoluteError));
    Assertions.assertFalse(LongEquality.areWithin(Long.MAX_VALUE, Long.MIN_VALUE, absoluteError));
  }

  @Test
  void testLongsUsingBigIntegerAreWithinUsingLargestError() {
    BigInteger absoluteError = BigInteger.valueOf(Long.MAX_VALUE)
        .subtract(BigInteger.valueOf(Long.MIN_VALUE)).subtract(BigInteger.ONE);
    Assertions.assertTrue(LongEquality.areWithin(Long.MAX_VALUE, 0, absoluteError));
    // Check overflow
    Assertions.assertTrue(LongEquality.areWithin(Long.MAX_VALUE, -1, absoluteError));
    Assertions.assertTrue(LongEquality.areWithin(Long.MIN_VALUE, 0, absoluteError));
    // This is the biggest delta possible
    Assertions.assertFalse(LongEquality.areWithin(Long.MAX_VALUE, Long.MIN_VALUE, absoluteError));
  }

  @Test
  void testLongsUsingBigIntegerAreWithinUsingAbsoluteError() {
    final long expected = Long.MAX_VALUE - 1;
    final long actual = -20;
    BigInteger absoluteError = BigInteger.valueOf(expected).subtract(BigInteger.valueOf(actual));

    // Order insensitive
    Assertions.assertTrue(LongEquality.areWithin(expected, actual, absoluteError));
    Assertions.assertTrue(LongEquality.areWithin(actual, expected, absoluteError));

    // Make the delta smaller
    final BigInteger delta = absoluteError.subtract(BigInteger.ONE);
    Assertions.assertFalse(LongEquality.areWithin(expected, actual, delta));
    Assertions.assertFalse(LongEquality.areWithin(actual, expected, delta));

    // Make the gap bigger
    final long expected2 = expected + 1;
    Assertions.assertFalse(LongEquality.areWithin(expected2, actual, absoluteError));
    Assertions.assertFalse(LongEquality.areWithin(actual, expected2, absoluteError));
  }

  // long Within

  @Test
  void testLongsAreWithinThrows() {
    final long expected = 0;
    final long actual = 0;
    // OK
    for (long absError : new long[] {0, 1, Long.MAX_VALUE}) {
      LongEquality.areWithin(expected, actual, absError);
    }
    // Bad
    for (long absError : new long[] {-1, Long.MIN_VALUE}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        LongEquality.areWithin(expected, actual, absError);
      });
    }
  }

  @Test
  void testLongsAreWithinUsingNoError() {
    // Test exact
    long absoluteError = 0;

    // Use a range of values
    for (long value : new long[] {0, 1, Long.MAX_VALUE, Long.MIN_VALUE}) {
      for (int i = 0; i < 2; i++) {
        value *= -1;
        Assertions.assertTrue(LongEquality.areWithin(value, value, absoluteError));
        Assertions.assertFalse(LongEquality.areWithin(value, nextUp(value), absoluteError));
        Assertions.assertFalse(LongEquality.areWithin(value, nextDown(value), absoluteError));
        Assertions.assertFalse(LongEquality.areWithin(nextUp(value), value, absoluteError));
        Assertions.assertFalse(LongEquality.areWithin(nextDown(value), value, absoluteError));
      }
    }
    // Test overflow of longs
    Assertions.assertFalse(LongEquality.areWithin(Long.MAX_VALUE, -1, absoluteError));
    Assertions.assertFalse(LongEquality.areWithin(Long.MIN_VALUE, 0, absoluteError));
    Assertions.assertFalse(LongEquality.areWithin(Long.MAX_VALUE, Long.MIN_VALUE, absoluteError));
  }

  @Test
  void testLongsAreWithinUsingLargestError() {
    long absoluteError = Long.MAX_VALUE;
    Assertions.assertTrue(LongEquality.areWithin(Long.MAX_VALUE, 0, absoluteError));
    // Check overflow
    Assertions.assertFalse(LongEquality.areWithin(Long.MAX_VALUE, -1, absoluteError));
    Assertions.assertFalse(LongEquality.areWithin(Long.MIN_VALUE, 0, absoluteError));
    Assertions.assertFalse(LongEquality.areWithin(Long.MAX_VALUE, Long.MIN_VALUE, absoluteError));
  }

  @Test
  void testLongsAreWithinUsingAbsoluteError() {
    final long expected = 20;
    final long actual = 10;
    final long absoluteError = expected - actual;

    // Order insensitive
    Assertions.assertTrue(LongEquality.areWithin(expected, actual, absoluteError));
    Assertions.assertTrue(LongEquality.areWithin(actual, expected, absoluteError));

    // Make the delta smaller
    final long delta = absoluteError - 1;
    Assertions.assertFalse(LongEquality.areWithin(expected, actual, delta));
    Assertions.assertFalse(LongEquality.areWithin(actual, expected, delta));

    // Make the gap bigger
    final long expected2 = expected + 1;
    Assertions.assertFalse(LongEquality.areWithin(expected2, actual, absoluteError));
    Assertions.assertFalse(LongEquality.areWithin(actual, expected2, absoluteError));
  }

  // int Within

  @Test
  void testIntsAreWithinThrows() {
    final int expected = 0;
    final int actual = 0;
    // OK
    for (long absError : new long[] {0, 1, Integer.MAX_VALUE, IntEquality.MAX_ABS_ERROR - 1}) {
      IntEquality.areWithin(expected, actual, absError);
    }
    // Bad
    for (long absError : new long[] {-1, Integer.MIN_VALUE, IntEquality.MAX_ABS_ERROR}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        IntEquality.areWithin(expected, actual, absError);
      });
    }
  }

  @Test
  void testIntsAreWithinUsingNoError() {
    // Test exact
    long absoluteError = 0;

    // Use a range of values
    for (int value : new int[] {0, 1, Integer.MAX_VALUE, Integer.MIN_VALUE}) {
      for (int i = 0; i < 2; i++) {
        value *= -1;
        Assertions.assertTrue(IntEquality.areWithin(value, value, absoluteError));
        Assertions.assertFalse(IntEquality.areWithin(value, nextUp(value), absoluteError));
        Assertions.assertFalse(IntEquality.areWithin(value, nextDown(value), absoluteError));
        Assertions.assertFalse(IntEquality.areWithin(nextUp(value), value, absoluteError));
        Assertions.assertFalse(IntEquality.areWithin(nextDown(value), value, absoluteError));
      }
    }
    // Test int overflow
    Assertions.assertFalse(IntEquality.areWithin(Integer.MAX_VALUE, -1, absoluteError));
    Assertions.assertFalse(IntEquality.areWithin(Integer.MIN_VALUE, 0, absoluteError));
    Assertions
        .assertFalse(IntEquality.areWithin(Integer.MAX_VALUE, Integer.MIN_VALUE, absoluteError));
  }

  @Test
  void testIntsAreWithinUsingLargestError() {
    long absoluteError = IntEquality.MAX_ABS_ERROR - 1;
    // Check largest range
    Assertions
        .assertTrue(IntEquality.areWithin(Integer.MAX_VALUE - 1, Integer.MIN_VALUE, absoluteError));
    Assertions
        .assertTrue(IntEquality.areWithin(Integer.MAX_VALUE, Integer.MIN_VALUE + 1, absoluteError));
    Assertions
        .assertFalse(IntEquality.areWithin(Integer.MAX_VALUE, Integer.MIN_VALUE, absoluteError));
  }

  @Test
  void testIntsAreWithinUsingAbsoluteError() {
    final int expected = 20;
    final int actual = 10;
    final long absoluteError = expected - actual;

    // Order insensitive
    Assertions.assertTrue(IntEquality.areWithin(expected, actual, absoluteError));
    Assertions.assertTrue(IntEquality.areWithin(actual, expected, absoluteError));

    // Make the delta smaller
    final long delta = absoluteError - 1;
    Assertions.assertFalse(IntEquality.areWithin(expected, actual, delta));
    Assertions.assertFalse(IntEquality.areWithin(actual, expected, delta));

    // Make the gap bigger
    final int expected2 = expected + 1;
    Assertions.assertFalse(IntEquality.areWithin(expected2, actual, absoluteError));
    Assertions.assertFalse(IntEquality.areWithin(actual, expected2, absoluteError));
  }

  // short Within

  @Test
  void testShortsAreWithinThrows() {
    final short expected = 0;
    final short actual = 0;
    // OK
    for (int absError : new int[] {0, 1, Short.MAX_VALUE, ShortEquality.MAX_ABS_ERROR - 1}) {
      ShortEquality.areWithin(expected, actual, absError);
    }
    // Bad
    for (int absError : new int[] {-1, Short.MIN_VALUE, ShortEquality.MAX_ABS_ERROR}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        ShortEquality.areWithin(expected, actual, absError);
      });
    }
  }

  @Test
  void testShortsAreWithinUsingNoError() {
    // Test exact
    int absoluteError = 0;

    // Use a range of values
    for (short value : new short[] {0, 1, Short.MAX_VALUE, Short.MIN_VALUE}) {
      for (short i = 0; i < 2; i++) {
        value *= -1;
        Assertions.assertTrue(ShortEquality.areWithin(value, value, absoluteError));
        Assertions.assertFalse(ShortEquality.areWithin(value, nextUp(value), absoluteError));
        Assertions.assertFalse(ShortEquality.areWithin(value, nextDown(value), absoluteError));
        Assertions.assertFalse(ShortEquality.areWithin(nextUp(value), value, absoluteError));
        Assertions.assertFalse(ShortEquality.areWithin(nextDown(value), value, absoluteError));
      }
    }
    // Test short overflow
    Assertions.assertFalse(ShortEquality.areWithin(Short.MAX_VALUE, (short) -1, absoluteError));
    Assertions.assertFalse(ShortEquality.areWithin(Short.MIN_VALUE, (short) 0, absoluteError));
    Assertions
        .assertFalse(ShortEquality.areWithin(Short.MAX_VALUE, Short.MIN_VALUE, absoluteError));
  }

  @Test
  void testShortsAreWithinUsingLargestError() {
    int absoluteError = ShortEquality.MAX_ABS_ERROR - 1;
    // Check largest range
    Assertions.assertTrue(
        ShortEquality.areWithin((short) (Short.MAX_VALUE - 1), Short.MIN_VALUE, absoluteError));
    Assertions.assertTrue(
        ShortEquality.areWithin(Short.MAX_VALUE, (short) (Short.MIN_VALUE + 1), absoluteError));
    Assertions
        .assertFalse(ShortEquality.areWithin(Short.MAX_VALUE, Short.MIN_VALUE, absoluteError));
  }

  @Test
  void testShortsAreWithinUsingAbsoluteError() {
    final short expected = 20;
    final short actual = 10;
    final int absoluteError = expected - actual;

    // Order insensitive
    Assertions.assertTrue(ShortEquality.areWithin(expected, actual, absoluteError));
    Assertions.assertTrue(ShortEquality.areWithin(actual, expected, absoluteError));

    // Make the delta smaller
    final int delta = absoluteError - 1;
    Assertions.assertFalse(ShortEquality.areWithin(expected, actual, delta));
    Assertions.assertFalse(ShortEquality.areWithin(actual, expected, delta));

    // Make the gap bigger
    final short expected2 = expected + 1;
    Assertions.assertFalse(ShortEquality.areWithin(expected2, actual, absoluteError));
    Assertions.assertFalse(ShortEquality.areWithin(actual, expected2, absoluteError));
  }

  // byte Within

  @Test
  void testBytesAreWithinThrows() {
    final byte expected = 0;
    final byte actual = 0;
    // OK
    for (int absError : new int[] {0, 1, Byte.MAX_VALUE, ByteEquality.MAX_ABS_ERROR - 1}) {
      ByteEquality.areWithin(expected, actual, absError);
    }
    // Bad
    for (int absError : new int[] {-1, Byte.MIN_VALUE, ByteEquality.MAX_ABS_ERROR}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        ByteEquality.areWithin(expected, actual, absError);
      });
    }
  }

  @Test
  void testBytesAreWithinUsingNoError() {
    // Test exact
    int absoluteError = 0;

    // Use a range of values
    for (byte value : new byte[] {0, 1, Byte.MAX_VALUE, Byte.MIN_VALUE}) {
      for (byte i = 0; i < 2; i++) {
        value *= -1;
        Assertions.assertTrue(ByteEquality.areWithin(value, value, absoluteError));
        Assertions.assertFalse(ByteEquality.areWithin(value, nextUp(value), absoluteError));
        Assertions.assertFalse(ByteEquality.areWithin(value, nextDown(value), absoluteError));
        Assertions.assertFalse(ByteEquality.areWithin(nextUp(value), value, absoluteError));
        Assertions.assertFalse(ByteEquality.areWithin(nextDown(value), value, absoluteError));
      }
    }
    // Test byte overflow
    Assertions.assertFalse(ByteEquality.areWithin(Byte.MAX_VALUE, (byte) -1, absoluteError));
    Assertions.assertFalse(ByteEquality.areWithin(Byte.MIN_VALUE, (byte) 0, absoluteError));
    Assertions.assertFalse(ByteEquality.areWithin(Byte.MAX_VALUE, Byte.MIN_VALUE, absoluteError));
  }

  @Test
  void testBytesAreWithinUsingLargestError() {
    int absoluteError = ByteEquality.MAX_ABS_ERROR - 1;
    // Check largest range
    Assertions.assertTrue(
        ByteEquality.areWithin((byte) (Byte.MAX_VALUE - 1), Byte.MIN_VALUE, absoluteError));
    Assertions.assertTrue(
        ByteEquality.areWithin(Byte.MAX_VALUE, (byte) (Byte.MIN_VALUE + 1), absoluteError));
    Assertions.assertFalse(ByteEquality.areWithin(Byte.MAX_VALUE, Byte.MIN_VALUE, absoluteError));
  }

  @Test
  void testBytesAreWithinUsingAbsoluteError() {
    final byte expected = 20;
    final byte actual = 10;
    final int absoluteError = expected - actual;

    // Order insensitive
    Assertions.assertTrue(ByteEquality.areWithin(expected, actual, absoluteError));
    Assertions.assertTrue(ByteEquality.areWithin(actual, expected, absoluteError));

    // Make the delta smaller
    final int delta = absoluteError - 1;
    Assertions.assertFalse(ByteEquality.areWithin(expected, actual, delta));
    Assertions.assertFalse(ByteEquality.areWithin(actual, expected, delta));

    // Make the gap bigger
    final byte expected2 = expected + 1;
    Assertions.assertFalse(ByteEquality.areWithin(expected2, actual, absoluteError));
    Assertions.assertFalse(ByteEquality.areWithin(actual, expected2, absoluteError));
  }

  // float Close

  @Test
  void testFloatsAreCloseThrows() {
    final float expected = 0;
    final float actual = 0;
    final double relativeError = 0;
    final float absoluteError = 0;

    for (float badRelativeError : new float[] {Float.NaN, Float.POSITIVE_INFINITY, -1, 2,
        Float.MAX_VALUE}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        FloatEquality.areClose(expected, actual, badRelativeError, absoluteError);
      });
    }
    for (float badAbsoluteError : new float[] {Float.NaN, Float.POSITIVE_INFINITY, -1}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        FloatEquality.areClose(expected, actual, relativeError, badAbsoluteError);
      });
    }
  }

  @Test
  void testFloatsAreCloseUsingNoError() {
    final float relativeError = 0;
    final float absoluteError = 0;

    // Use a range of values
    for (float value : new float[] {0, 1, (float) Math.PI, Float.MAX_VALUE, Float.MIN_VALUE}) {
      for (int i = 0; i < 2; i++) {
        value *= -1;
        Assertions.assertTrue(FloatEquality.areClose(value, value, relativeError, absoluteError));
        Assertions.assertFalse(
            FloatEquality.areClose(value, Math.nextUp(value), relativeError, absoluteError));
        Assertions.assertFalse(
            FloatEquality.areClose(value, Math.nextDown(value), relativeError, absoluteError));
        Assertions.assertFalse(
            FloatEquality.areClose(Math.nextUp(value), value, relativeError, absoluteError));
        Assertions.assertFalse(
            FloatEquality.areClose(Math.nextDown(value), value, relativeError, absoluteError));
      }
    }
    // Special floats are not equal as they are not within a real delta.
    for (final float value : new float[] {Float.NaN, Float.POSITIVE_INFINITY,
        Float.NEGATIVE_INFINITY}) {
      Assertions.assertFalse(FloatEquality.areClose(value, value, relativeError, absoluteError));
    }
  }

  @Test
  void testFloatsAreCloseUsingAbsoluteError() {
    final float expected = 2;
    final float actual = 1;
    final float relativeError = 0;
    final float absoluteError = expected - actual;

    // Order insensitive
    Assertions.assertTrue(FloatEquality.areClose(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(FloatEquality.areClose(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final float delta = Math.nextDown(absoluteError);
    Assertions.assertFalse(FloatEquality.areClose(expected, actual, relativeError, delta));
    Assertions.assertFalse(FloatEquality.areClose(actual, expected, relativeError, delta));

    // Make the gap bigger
    // Calling Math.nextDown(expected) creates a value that still is within 1 of 2 due to float
    // error.
    // So instead get the next delta above and subtract from actual.
    final float expected2 = actual - Math.nextUp(absoluteError);
    Assertions.assertFalse(FloatEquality.areClose(expected2, actual, relativeError, absoluteError));
    Assertions.assertFalse(FloatEquality.areClose(actual, expected2, relativeError, absoluteError));
  }

  @Test
  void testFloatsAreCloseUsingRelativeError() {
    final float expected = 2;
    final float actual = 1;
    final double relativeError = 0.5;
    final float absoluteError = 0;

    // Order insensitive
    Assertions.assertTrue(FloatEquality.areClose(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(FloatEquality.areClose(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final double delta = Math.nextDown(relativeError);
    Assertions.assertFalse(FloatEquality.areClose(expected, actual, delta, absoluteError));
    Assertions.assertFalse(FloatEquality.areClose(actual, expected, delta, absoluteError));

    // Make the gap bigger
    // Calling Math.nextDown(expected) creates a value that still is within 1 of 2 due to float
    // error.
    // So instead get the next delta above and subtract from actual.
    final float expected2 = actual - Math.nextUp(expected - actual);
    Assertions.assertFalse(FloatEquality.areClose(expected2, actual, relativeError, absoluteError));
    Assertions.assertFalse(FloatEquality.areClose(actual, expected2, relativeError, absoluteError));
  }

  // double Close

  @Test
  void testDoublesAreCloseThrows() {
    final double expected = 0;
    final double actual = 0;
    final double relativeError = 0;
    final double absoluteError = 0;

    for (double badRelativeError : new double[] {Double.NaN, Double.POSITIVE_INFINITY, -1, 2,
        Double.MAX_VALUE}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        DoubleEquality.areClose(expected, actual, badRelativeError, absoluteError);
      });
    }
    for (double badAbsoluteError : new double[] {Double.NaN, Double.POSITIVE_INFINITY, -1}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        DoubleEquality.areClose(expected, actual, relativeError, badAbsoluteError);
      });
    }
  }

  @Test
  void testDoublesAreCloseUsingNoError() {
    final double relativeError = 0;
    final double absoluteError = 0;

    // Use a range of values
    for (double value : new double[] {0, 1, Math.PI, Double.MAX_VALUE, Double.MIN_VALUE}) {
      for (int i = 0; i < 2; i++) {
        value *= -1;
        Assertions.assertTrue(DoubleEquality.areClose(value, value, relativeError, absoluteError));
        Assertions.assertFalse(
            DoubleEquality.areClose(value, Math.nextUp(value), relativeError, absoluteError));
        Assertions.assertFalse(
            DoubleEquality.areClose(value, Math.nextDown(value), relativeError, absoluteError));
        Assertions.assertFalse(
            DoubleEquality.areClose(Math.nextUp(value), value, relativeError, absoluteError));
        Assertions.assertFalse(
            DoubleEquality.areClose(Math.nextDown(value), value, relativeError, absoluteError));
      }
    }
    // Special doubles are not equal as they are not within a real delta.
    for (final double value : new double[] {Double.NaN, Double.POSITIVE_INFINITY,
        Double.NEGATIVE_INFINITY}) {
      Assertions.assertFalse(DoubleEquality.areClose(value, value, relativeError, absoluteError));
    }
  }

  @Test
  void testDoublesAreCloseUsingAbsoluteError() {
    final double expected = 2;
    final double actual = 1;
    final double relativeError = 0;
    final double absoluteError = expected - actual;

    // Order insensitive
    Assertions.assertTrue(DoubleEquality.areClose(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(DoubleEquality.areClose(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final double delta = Math.nextDown(absoluteError);
    Assertions.assertFalse(DoubleEquality.areClose(expected, actual, relativeError, delta));
    Assertions.assertFalse(DoubleEquality.areClose(actual, expected, relativeError, delta));

    // Make the gap bigger
    // Calling Math.nextDown(expected) creates a value that still is within 1 of 2 due to float
    // error.
    // So instead get the next delta above and subtract from actual.
    final double expected2 = actual - Math.nextUp(absoluteError);
    Assertions
        .assertFalse(DoubleEquality.areClose(expected2, actual, relativeError, absoluteError));
    Assertions
        .assertFalse(DoubleEquality.areClose(actual, expected2, relativeError, absoluteError));
  }

  @Test
  void testDoublesAreCloseUsingRelativeError() {
    final double expected = 2;
    final double actual = 1;
    final double relativeError = 0.5;
    final double absoluteError = 0;

    // Order insensitive
    Assertions.assertTrue(DoubleEquality.areClose(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(DoubleEquality.areClose(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final double delta = Math.nextDown(relativeError);
    Assertions.assertFalse(DoubleEquality.areClose(expected, actual, delta, absoluteError));
    Assertions.assertFalse(DoubleEquality.areClose(actual, expected, delta, absoluteError));

    // Make the gap bigger
    // Calling Math.nextDown(expected) creates a value that still is within 1 of 2 due to float
    // error.
    // So instead get the next delta above and subtract from actual.
    final double expected2 = actual - Math.nextUp(expected - actual);
    Assertions
        .assertFalse(DoubleEquality.areClose(expected2, actual, relativeError, absoluteError));
    Assertions
        .assertFalse(DoubleEquality.areClose(actual, expected2, relativeError, absoluteError));
  }

  // long Close

  @Test
  void testLongsAreCloseThrows() {
    final long expected = 0;
    final long actual = 0;
    final double relativeError = 0;
    final long absoluteError = 0;

    for (double badRelativeError : new double[] {Double.NaN, Double.POSITIVE_INFINITY, -1, 2,
        Double.MAX_VALUE}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        LongEquality.areClose(expected, actual, badRelativeError, absoluteError);
      });
    }
    for (long badAbsoluteError : new long[] {Long.MIN_VALUE, -1}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        LongEquality.areClose(expected, actual, relativeError, badAbsoluteError);
      });
    }
  }

  @Test
  void testLongsAreCloseUsingNoError() {
    final double relativeError = 0;
    final long absoluteError = 0;

    // Use a range of values
    for (long value : new long[] {Long.MIN_VALUE, Long.MIN_VALUE + 1, -1, 0, 1, Long.MAX_VALUE - 1,
        Long.MAX_VALUE}) {
      Assertions.assertTrue(LongEquality.areClose(value, value, relativeError, absoluteError));
      Assertions
          .assertFalse(LongEquality.areClose(value, nextUp(value), relativeError, absoluteError));
      Assertions
          .assertFalse(LongEquality.areClose(value, nextDown(value), relativeError, absoluteError));
      Assertions
          .assertFalse(LongEquality.areClose(nextUp(value), value, relativeError, absoluteError));
      Assertions
          .assertFalse(LongEquality.areClose(nextDown(value), value, relativeError, absoluteError));
    }
    // Test overflow of longs
    Assertions.assertFalse(LongEquality.areClose(Long.MAX_VALUE, -1, relativeError, absoluteError));
    Assertions.assertFalse(LongEquality.areClose(Long.MIN_VALUE, 0, relativeError, absoluteError));
    Assertions.assertFalse(
        LongEquality.areClose(Long.MAX_VALUE, Long.MIN_VALUE, relativeError, absoluteError));
  }

  @Test
  void testLongsAreCloseUsingAbsoluteError() {
    final long expected = 2;
    final long actual = 1;
    final double relativeError = 0;
    final long absoluteError = expected - actual;

    // Order insensitive
    Assertions.assertTrue(LongEquality.areClose(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(LongEquality.areClose(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final long delta = nextDown(absoluteError);
    Assertions.assertFalse(LongEquality.areClose(expected, actual, relativeError, delta));
    Assertions.assertFalse(LongEquality.areClose(actual, expected, relativeError, delta));

    // Make the gap bigger
    final long expected2 = nextUp(expected);
    Assertions.assertFalse(LongEquality.areClose(expected2, actual, relativeError, absoluteError));
    Assertions.assertFalse(LongEquality.areClose(actual, expected2, relativeError, absoluteError));
  }

  @Test
  void testLongsAreCloseUsingRelativeError() {
    final long expected = 2;
    final long actual = 1;
    final double relativeError = 0.5;
    final long absoluteError = 0;

    // Order insensitive
    Assertions.assertTrue(LongEquality.areClose(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(LongEquality.areClose(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final double delta = Math.nextDown(relativeError);
    Assertions.assertFalse(LongEquality.areClose(expected, actual, delta, absoluteError));
    Assertions.assertFalse(LongEquality.areClose(actual, expected, delta, absoluteError));

    // Make the gap bigger
    final long expected2 = nextUp(expected);
    Assertions.assertFalse(LongEquality.areClose(expected2, actual, relativeError, absoluteError));
    Assertions.assertFalse(LongEquality.areClose(actual, expected2, relativeError, absoluteError));
  }

  @Test
  void testLongsAreCloseUsingRelativeErrorAtMinValue() {
    final long expected = Long.MIN_VALUE;
    final long actual = expected + 1;
    final double relativeError = 0.5 - 1e-3;
    final long absoluteError = 0;

    // Order insensitive
    Assertions.assertTrue(LongEquality.areClose(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(LongEquality.areClose(actual, expected, relativeError, absoluteError));

    // Make the gap bigger (so relative error is 0.5)
    final long expected2 = expected / 2;
    Assertions.assertFalse(LongEquality.areClose(expected2, actual, relativeError, absoluteError));
    Assertions.assertFalse(LongEquality.areClose(actual, expected2, relativeError, absoluteError));
  }

  // int Close

  @Test
  void testIntsAreCloseThrows() {
    final int expected = 0;
    final int actual = 0;
    final double relativeError = 0;
    final long absoluteError = 0;

    for (double badRelativeError : new double[] {Double.NaN, Double.POSITIVE_INFINITY, -1, 2,
        Double.MAX_VALUE}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        IntEquality.areClose(expected, actual, badRelativeError, absoluteError);
      });
    }
    for (long badAbsoluteError : new long[] {Integer.MIN_VALUE, -1, IntEquality.MAX_ABS_ERROR}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        IntEquality.areClose(expected, actual, relativeError, badAbsoluteError);
      });
    }
  }

  @Test
  void testIntsAreCloseUsingNoError() {
    final double relativeError = 0;
    final long absoluteError = 0;

    // Use a range of values
    for (int value : new int[] {Integer.MIN_VALUE, Integer.MIN_VALUE + 1, -1, 0, 1,
        Integer.MAX_VALUE - 1, Integer.MAX_VALUE}) {
      Assertions.assertTrue(IntEquality.areClose(value, value, relativeError, absoluteError));
      Assertions
          .assertFalse(IntEquality.areClose(value, nextUp(value), relativeError, absoluteError));
      Assertions
          .assertFalse(IntEquality.areClose(value, nextDown(value), relativeError, absoluteError));
      Assertions
          .assertFalse(IntEquality.areClose(nextUp(value), value, relativeError, absoluteError));
      Assertions
          .assertFalse(IntEquality.areClose(nextDown(value), value, relativeError, absoluteError));
    }
    // Test overflow of ints
    Assertions
        .assertFalse(IntEquality.areClose(Integer.MAX_VALUE, -1, relativeError, absoluteError));
    Assertions
        .assertFalse(IntEquality.areClose(Integer.MIN_VALUE, 0, relativeError, absoluteError));
    Assertions.assertFalse(
        IntEquality.areClose(Integer.MAX_VALUE, Integer.MIN_VALUE, relativeError, absoluteError));
  }

  @Test
  void testIntsAreCloseUsingAbsoluteError() {
    final int expected = 2;
    final int actual = 1;
    final double relativeError = 0;
    final long absoluteError = expected - actual;

    // Order insensitive
    Assertions.assertTrue(IntEquality.areClose(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(IntEquality.areClose(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final long delta = nextDown(absoluteError);
    Assertions.assertFalse(IntEquality.areClose(expected, actual, relativeError, delta));
    Assertions.assertFalse(IntEquality.areClose(actual, expected, relativeError, delta));

    // Make the gap bigger
    final int expected2 = nextUp(expected);
    Assertions.assertFalse(IntEquality.areClose(expected2, actual, relativeError, absoluteError));
    Assertions.assertFalse(IntEquality.areClose(actual, expected2, relativeError, absoluteError));
  }

  @Test
  void testIntsAreCloseUsingRelativeError() {
    final int expected = 2;
    final int actual = 1;
    final double relativeError = 0.5;
    final long absoluteError = 0;

    // Order insensitive
    Assertions.assertTrue(IntEquality.areClose(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(IntEquality.areClose(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final double delta = Math.nextDown(relativeError);
    Assertions.assertFalse(IntEquality.areClose(expected, actual, delta, absoluteError));
    Assertions.assertFalse(IntEquality.areClose(actual, expected, delta, absoluteError));

    // Make the gap bigger
    final int expected2 = nextUp(expected);
    Assertions.assertFalse(IntEquality.areClose(expected2, actual, relativeError, absoluteError));
    Assertions.assertFalse(IntEquality.areClose(actual, expected2, relativeError, absoluteError));
  }

  @Test
  void testIntsAreCloseUsingRelativeErrorAtMinValue() {
    final int expected = Integer.MIN_VALUE;
    final int actual = expected + 1;
    final double relativeError = 0.5 - 1e-3;
    final long absoluteError = 0;

    // Order insensitive
    Assertions.assertTrue(IntEquality.areClose(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(IntEquality.areClose(actual, expected, relativeError, absoluteError));

    // Make the gap bigger (so relative error is 0.5)
    final int expected2 = expected / 2;
    Assertions.assertFalse(IntEquality.areClose(expected2, actual, relativeError, absoluteError));
    Assertions.assertFalse(IntEquality.areClose(actual, expected2, relativeError, absoluteError));
  }

  // short Close

  @Test
  void testShortsAreCloseThrows() {
    final short expected = 0;
    final short actual = 0;
    final double relativeError = 0;
    final int absoluteError = 0;

    for (double badRelativeError : new double[] {Double.NaN, Double.POSITIVE_INFINITY, -1, 2,
        Double.MAX_VALUE}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        ShortEquality.areClose(expected, actual, badRelativeError, absoluteError);
      });
    }
    for (int badAbsoluteError : new int[] {Short.MIN_VALUE, -1, ShortEquality.MAX_ABS_ERROR}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        ShortEquality.areClose(expected, actual, relativeError, badAbsoluteError);
      });
    }
  }

  @Test
  void testShortsAreCloseUsingNoError() {
    final double relativeError = 0;
    final int absoluteError = 0;

    // Use a range of values
    for (short value : new short[] {Short.MIN_VALUE, Short.MIN_VALUE + 1, -1, 0, 1,
        Short.MAX_VALUE - 1, Short.MAX_VALUE}) {
      Assertions.assertTrue(ShortEquality.areClose(value, value, relativeError, absoluteError));
      Assertions
          .assertFalse(ShortEquality.areClose(value, nextUp(value), relativeError, absoluteError));
      Assertions.assertFalse(
          ShortEquality.areClose(value, nextDown(value), relativeError, absoluteError));
      Assertions
          .assertFalse(ShortEquality.areClose(nextUp(value), value, relativeError, absoluteError));
      Assertions.assertFalse(
          ShortEquality.areClose(nextDown(value), value, relativeError, absoluteError));
    }
    // Test overflow of shorts
    Assertions.assertFalse(
        ShortEquality.areClose(Short.MAX_VALUE, (short) -1, relativeError, absoluteError));
    Assertions.assertFalse(
        ShortEquality.areClose(Short.MIN_VALUE, (short) 0, relativeError, absoluteError));
    Assertions.assertFalse(
        ShortEquality.areClose(Short.MAX_VALUE, Short.MIN_VALUE, relativeError, absoluteError));
  }

  @Test
  void testShortsAreCloseUsingAbsoluteError() {
    final short expected = 2;
    final short actual = 1;
    final double relativeError = 0;
    final int absoluteError = expected - actual;

    // Order insensitive
    Assertions.assertTrue(ShortEquality.areClose(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(ShortEquality.areClose(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final int delta = nextDown(absoluteError);
    Assertions.assertFalse(ShortEquality.areClose(expected, actual, relativeError, delta));
    Assertions.assertFalse(ShortEquality.areClose(actual, expected, relativeError, delta));

    // Make the gap bigger
    final short expected2 = nextUp(expected);
    Assertions.assertFalse(ShortEquality.areClose(expected2, actual, relativeError, absoluteError));
    Assertions.assertFalse(ShortEquality.areClose(actual, expected2, relativeError, absoluteError));
  }

  @Test
  void testShortsAreCloseUsingRelativeError() {
    final short expected = 2;
    final short actual = 1;
    final double relativeError = 0.5;
    final int absoluteError = 0;

    // Order insensitive
    Assertions.assertTrue(ShortEquality.areClose(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(ShortEquality.areClose(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final double delta = Math.nextDown(relativeError);
    Assertions.assertFalse(ShortEquality.areClose(expected, actual, delta, absoluteError));
    Assertions.assertFalse(ShortEquality.areClose(actual, expected, delta, absoluteError));

    // Make the gap bigger
    final short expected2 = nextUp(expected);
    Assertions.assertFalse(ShortEquality.areClose(expected2, actual, relativeError, absoluteError));
    Assertions.assertFalse(ShortEquality.areClose(actual, expected2, relativeError, absoluteError));
  }

  @Test
  void testShortsAreCloseUsingRelativeErrorAtMinValue() {
    final short expected = Short.MIN_VALUE;
    final short actual = expected + 1;
    final double relativeError = 0.5 - 1e-3;
    final int absoluteError = 0;

    // Order insensitive
    Assertions.assertTrue(ShortEquality.areClose(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(ShortEquality.areClose(actual, expected, relativeError, absoluteError));

    // Make the gap bigger (so relative error is 0.5)
    final short expected2 = expected / 2;
    Assertions.assertFalse(ShortEquality.areClose(expected2, actual, relativeError, absoluteError));
    Assertions.assertFalse(ShortEquality.areClose(actual, expected2, relativeError, absoluteError));
  }

  // byte Close

  @Test
  void testBytesAreCloseThrows() {
    final byte expected = 0;
    final byte actual = 0;
    final double relativeError = 0;
    final int absoluteError = 0;

    for (double badRelativeError : new double[] {Double.NaN, Double.POSITIVE_INFINITY, -1, 2,
        Double.MAX_VALUE}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        ByteEquality.areClose(expected, actual, badRelativeError, absoluteError);
      });
    }
    for (int badAbsoluteError : new int[] {Byte.MIN_VALUE, -1, ByteEquality.MAX_ABS_ERROR}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        ByteEquality.areClose(expected, actual, relativeError, badAbsoluteError);
      });
    }
  }

  @Test
  void testBytesAreCloseUsingNoError() {
    final double relativeError = 0;
    final int absoluteError = 0;

    // Use a range of values
    for (byte value : new byte[] {Byte.MIN_VALUE, Byte.MIN_VALUE + 1, -1, 0, 1, Byte.MAX_VALUE - 1,
        Byte.MAX_VALUE}) {
      Assertions.assertTrue(ByteEquality.areClose(value, value, relativeError, absoluteError));
      Assertions
          .assertFalse(ByteEquality.areClose(value, nextUp(value), relativeError, absoluteError));
      Assertions
          .assertFalse(ByteEquality.areClose(value, nextDown(value), relativeError, absoluteError));
      Assertions
          .assertFalse(ByteEquality.areClose(nextUp(value), value, relativeError, absoluteError));
      Assertions
          .assertFalse(ByteEquality.areClose(nextDown(value), value, relativeError, absoluteError));
    }
    // Test overflow of bytes
    Assertions.assertFalse(
        ByteEquality.areClose(Byte.MAX_VALUE, (byte) -1, relativeError, absoluteError));
    Assertions
        .assertFalse(ByteEquality.areClose(Byte.MIN_VALUE, (byte) 0, relativeError, absoluteError));
    Assertions.assertFalse(
        ByteEquality.areClose(Byte.MAX_VALUE, Byte.MIN_VALUE, relativeError, absoluteError));
  }

  @Test
  void testBytesAreCloseUsingAbsoluteError() {
    final byte expected = 2;
    final byte actual = 1;
    final double relativeError = 0;
    final int absoluteError = expected - actual;

    // Order insensitive
    Assertions.assertTrue(ByteEquality.areClose(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(ByteEquality.areClose(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final int delta = nextDown(absoluteError);
    Assertions.assertFalse(ByteEquality.areClose(expected, actual, relativeError, delta));
    Assertions.assertFalse(ByteEquality.areClose(actual, expected, relativeError, delta));

    // Make the gap bigger
    final byte expected2 = nextUp(expected);
    Assertions.assertFalse(ByteEquality.areClose(expected2, actual, relativeError, absoluteError));
    Assertions.assertFalse(ByteEquality.areClose(actual, expected2, relativeError, absoluteError));
  }

  @Test
  void testBytesAreCloseUsingRelativeError() {
    final byte expected = 2;
    final byte actual = 1;
    final double relativeError = 0.5;
    final int absoluteError = 0;

    // Order insensitive
    Assertions.assertTrue(ByteEquality.areClose(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(ByteEquality.areClose(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final double delta = Math.nextDown(relativeError);
    Assertions.assertFalse(ByteEquality.areClose(expected, actual, delta, absoluteError));
    Assertions.assertFalse(ByteEquality.areClose(actual, expected, delta, absoluteError));

    // Make the gap bigger
    final byte expected2 = nextUp(expected);
    Assertions.assertFalse(ByteEquality.areClose(expected2, actual, relativeError, absoluteError));
    Assertions.assertFalse(ByteEquality.areClose(actual, expected2, relativeError, absoluteError));
  }

  @Test
  void testBytesAreCloseUsingRelativeErrorAtMinValue() {
    final byte expected = Byte.MIN_VALUE;
    final byte actual = expected + 1;
    final double relativeError = 0.49;
    final int absoluteError = 0;

    // Order insensitive
    Assertions.assertTrue(ByteEquality.areClose(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(ByteEquality.areClose(actual, expected, relativeError, absoluteError));

    // Make the gap bigger (so relative error is 0.5)
    final byte expected2 = expected / 2;
    Assertions.assertFalse(ByteEquality.areClose(expected2, actual, relativeError, absoluteError));
    Assertions.assertFalse(ByteEquality.areClose(actual, expected2, relativeError, absoluteError));
  }

  // float IsCloseTo

  @Test
  void testFloatsIsCloseToThrows() {
    final float expected = 0;
    final float actual = 0;
    final double relativeError = 0;
    final float absoluteError = 0;

    for (double goodRelativeError : new double[] {2, Double.MAX_VALUE}) {
      FloatEquality.isCloseTo(expected, actual, goodRelativeError, absoluteError);
    }
    for (double badRelativeError : new double[] {Double.NaN, Double.POSITIVE_INFINITY, -1}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        FloatEquality.isCloseTo(expected, actual, badRelativeError, absoluteError);
      });
    }
    for (float badAbsoluteError : new float[] {Float.NaN, Float.POSITIVE_INFINITY, -1}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        FloatEquality.isCloseTo(expected, actual, relativeError, badAbsoluteError);
      });
    }
  }

  @Test
  void testFloatsIsCloseToUsingNoError() {
    final float relativeError = 0;
    final float absoluteError = 0;

    // Use a range of values
    for (float value : new float[] {0, 1, (float) Math.PI, Float.MAX_VALUE, Float.MIN_VALUE}) {
      for (int i = 0; i < 2; i++) {
        value *= -1;
        Assertions.assertTrue(FloatEquality.isCloseTo(value, value, relativeError, absoluteError));
        Assertions.assertFalse(
            FloatEquality.isCloseTo(value, Math.nextUp(value), relativeError, absoluteError));
        Assertions.assertFalse(
            FloatEquality.isCloseTo(value, Math.nextDown(value), relativeError, absoluteError));
        Assertions.assertFalse(
            FloatEquality.isCloseTo(Math.nextUp(value), value, relativeError, absoluteError));
        Assertions.assertFalse(
            FloatEquality.isCloseTo(Math.nextDown(value), value, relativeError, absoluteError));
      }
    }
    // Special floats are not equal as they are not within a real delta.
    for (final float value : new float[] {Float.NaN, Float.POSITIVE_INFINITY,
        Float.NEGATIVE_INFINITY}) {
      Assertions.assertFalse(FloatEquality.isCloseTo(value, value, relativeError, absoluteError));
    }
  }

  @Test
  void testFloatsIsCloseToUsingAbsoluteError() {
    final float expected = 2;
    final float actual = 1;
    final float relativeError = 0;
    final float absoluteError = expected - actual;

    // Order insensitive
    Assertions.assertTrue(FloatEquality.isCloseTo(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(FloatEquality.isCloseTo(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final float delta = Math.nextDown(absoluteError);
    Assertions.assertFalse(FloatEquality.isCloseTo(expected, actual, relativeError, delta));
    Assertions.assertFalse(FloatEquality.isCloseTo(actual, expected, relativeError, delta));

    // Make the gap bigger
    // Calling Math.nextDown(expected) creates a value that still is within 1 of 2 due to float
    // error.
    // So instead get the next delta above and subtract from actual.
    final float expected2 = actual - Math.nextUp(absoluteError);
    Assertions
        .assertFalse(FloatEquality.isCloseTo(expected2, actual, relativeError, absoluteError));
    Assertions
        .assertFalse(FloatEquality.isCloseTo(actual, expected2, relativeError, absoluteError));
  }

  @Test
  void testFloatsIsCloseToUsingRelativeError() {
    final float expected = 2;
    final float actual = 1;
    final double relativeError = 0.5;
    final float absoluteError = 0;

    // Order sensitive
    Assertions.assertTrue(FloatEquality.isCloseTo(expected, actual, relativeError, absoluteError));
    Assertions.assertFalse(FloatEquality.isCloseTo(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final double delta = Math.nextDown(relativeError);
    Assertions.assertFalse(FloatEquality.isCloseTo(expected, actual, delta, absoluteError));

    // Make the gap bigger
    // Calling Math.nextDown(expected) creates a value that still is within 1 of 2 due to float
    // error.
    // So instead get the next delta above and subtract from actual.
    final float expected2 = actual - Math.nextUp(expected - actual);
    Assertions
        .assertFalse(FloatEquality.isCloseTo(expected2, actual, relativeError, absoluteError));
  }

  // double Close

  @Test
  void testDoublesIsCloseToThrows() {
    final double expected = 0;
    final double actual = 0;
    final double relativeError = 0;
    final double absoluteError = 0;

    for (double goodRelativeError : new double[] {2, Double.MAX_VALUE}) {
      DoubleEquality.isCloseTo(expected, actual, goodRelativeError, absoluteError);
    }
    for (double badRelativeError : new double[] {Double.NaN, Double.POSITIVE_INFINITY, -1}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        DoubleEquality.isCloseTo(expected, actual, badRelativeError, absoluteError);
      });
    }
    for (double badAbsoluteError : new double[] {Double.NaN, Double.POSITIVE_INFINITY, -1}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        DoubleEquality.isCloseTo(expected, actual, relativeError, badAbsoluteError);
      });
    }
  }

  @Test
  void testDoublesIsCloseToUsingNoError() {
    final double relativeError = 0;
    final double absoluteError = 0;

    // Use a range of values
    for (double value : new double[] {0, 1, Math.PI, Double.MAX_VALUE, Double.MIN_VALUE}) {
      for (int i = 0; i < 2; i++) {
        value *= -1;
        Assertions.assertTrue(DoubleEquality.isCloseTo(value, value, relativeError, absoluteError));
        Assertions.assertFalse(
            DoubleEquality.isCloseTo(value, Math.nextUp(value), relativeError, absoluteError));
        Assertions.assertFalse(
            DoubleEquality.isCloseTo(value, Math.nextDown(value), relativeError, absoluteError));
        Assertions.assertFalse(
            DoubleEquality.isCloseTo(Math.nextUp(value), value, relativeError, absoluteError));
        Assertions.assertFalse(
            DoubleEquality.isCloseTo(Math.nextDown(value), value, relativeError, absoluteError));
      }
    }
    // Special doubles are not equal as they are not within a real delta.
    for (final double value : new double[] {Double.NaN, Double.POSITIVE_INFINITY,
        Double.NEGATIVE_INFINITY}) {
      Assertions.assertFalse(DoubleEquality.isCloseTo(value, value, relativeError, absoluteError));
    }
  }

  @Test
  void testDoublesIsCloseToUsingAbsoluteError() {
    final double expected = 2;
    final double actual = 1;
    final double relativeError = 0;
    final double absoluteError = expected - actual;

    // Order insensitive
    Assertions.assertTrue(DoubleEquality.isCloseTo(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(DoubleEquality.isCloseTo(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final double delta = Math.nextDown(absoluteError);
    Assertions.assertFalse(DoubleEquality.isCloseTo(expected, actual, relativeError, delta));
    Assertions.assertFalse(DoubleEquality.isCloseTo(actual, expected, relativeError, delta));

    // Make the gap bigger
    // Calling Math.nextDown(expected) creates a value that still is within 1 of 2 due to float
    // error.
    // So instead get the next delta above and subtract from actual.
    final double expected2 = actual - Math.nextUp(absoluteError);
    Assertions
        .assertFalse(DoubleEquality.isCloseTo(expected2, actual, relativeError, absoluteError));
    Assertions
        .assertFalse(DoubleEquality.isCloseTo(actual, expected2, relativeError, absoluteError));
  }

  @Test
  void testDoublesIsCloseToUsingRelativeError() {
    final double expected = 2;
    final double actual = 1;
    final double relativeError = 0.5;
    final double absoluteError = 0;

    // Order sensitive
    Assertions.assertTrue(DoubleEquality.isCloseTo(expected, actual, relativeError, absoluteError));
    Assertions
        .assertFalse(DoubleEquality.isCloseTo(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final double delta = Math.nextDown(relativeError);
    Assertions.assertFalse(DoubleEquality.isCloseTo(expected, actual, delta, absoluteError));

    // Make the gap bigger
    // Calling Math.nextDown(expected) creates a value that still is within 1 of 2 due to float
    // error.
    // So instead get the next delta above and subtract from actual.
    final double expected2 = actual - Math.nextUp(expected - actual);
    Assertions
        .assertFalse(DoubleEquality.isCloseTo(expected2, actual, relativeError, absoluteError));
  }

  // long IsCloseTo

  @Test
  void testLongsIsCloseToThrows() {
    final long expected = 0;
    final long actual = 0;
    final double relativeError = 0;
    final long absoluteError = 0;

    for (double goodRelativeError : new double[] {2, Double.MAX_VALUE}) {
      LongEquality.isCloseTo(expected, actual, goodRelativeError, absoluteError);
    }
    for (double badRelativeError : new double[] {Double.NaN, Double.POSITIVE_INFINITY, -1}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        LongEquality.isCloseTo(expected, actual, badRelativeError, absoluteError);
      });
    }
    for (long badAbsoluteError : new long[] {Long.MIN_VALUE, -1}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        LongEquality.isCloseTo(expected, actual, relativeError, badAbsoluteError);
      });
    }
  }

  @Test
  void testLongsIsCloseToUsingNoError() {
    final double relativeError = 0;
    final long absoluteError = 0;

    // Use a range of values
    for (long value : new long[] {Long.MIN_VALUE, Long.MIN_VALUE + 1, -1, 0, 1, Long.MAX_VALUE - 1,
        Long.MAX_VALUE}) {
      Assertions.assertTrue(LongEquality.isCloseTo(value, value, relativeError, absoluteError));
      Assertions
          .assertFalse(LongEquality.isCloseTo(value, nextUp(value), relativeError, absoluteError));
      Assertions.assertFalse(
          LongEquality.isCloseTo(value, nextDown(value), relativeError, absoluteError));
      Assertions
          .assertFalse(LongEquality.isCloseTo(nextUp(value), value, relativeError, absoluteError));
      Assertions.assertFalse(
          LongEquality.isCloseTo(nextDown(value), value, relativeError, absoluteError));
    }
    // Test overflow of longs
    Assertions
        .assertFalse(LongEquality.isCloseTo(Long.MAX_VALUE, -1, relativeError, absoluteError));
    Assertions.assertFalse(LongEquality.isCloseTo(Long.MIN_VALUE, 0, relativeError, absoluteError));
    Assertions.assertFalse(
        LongEquality.isCloseTo(Long.MAX_VALUE, Long.MIN_VALUE, relativeError, absoluteError));
  }

  @Test
  void testLongsIsCloseToUsingAbsoluteError() {
    final long expected = 2;
    final long actual = 1;
    final double relativeError = 0;
    final long absoluteError = expected - actual;

    // Order insensitive
    Assertions.assertTrue(LongEquality.isCloseTo(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(LongEquality.isCloseTo(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final long delta = nextDown(absoluteError);
    Assertions.assertFalse(LongEquality.isCloseTo(expected, actual, relativeError, delta));
    Assertions.assertFalse(LongEquality.isCloseTo(actual, expected, relativeError, delta));

    // Make the gap bigger
    final long expected2 = nextUp(expected);
    Assertions.assertFalse(LongEquality.isCloseTo(expected2, actual, relativeError, absoluteError));
    Assertions.assertFalse(LongEquality.isCloseTo(actual, expected2, relativeError, absoluteError));
  }

  @Test
  void testLongsIsCloseToUsingRelativeError() {
    final long expected = 2;
    final long actual = 1;
    final double relativeError = 0.5;
    final long absoluteError = 0;

    // Order sensitive
    Assertions.assertTrue(LongEquality.isCloseTo(expected, actual, relativeError, absoluteError));
    Assertions.assertFalse(LongEquality.isCloseTo(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final double delta = Math.nextDown(relativeError);
    Assertions.assertFalse(LongEquality.isCloseTo(expected, actual, delta, absoluteError));

    // Make the gap bigger
    final long expected2 = nextUp(expected);
    Assertions.assertFalse(LongEquality.isCloseTo(expected2, actual, relativeError, absoluteError));
  }

  @Test
  void testLongsIsCloseToUsingRelativeErrorAtMinValue() {
    final long expected = Long.MIN_VALUE;
    final long actual = expected + 1;
    final double relativeError = 0.5;
    final long absoluteError = 0;

    // Both within the relative error of 0.5
    Assertions.assertTrue(LongEquality.isCloseTo(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(LongEquality.isCloseTo(actual, expected, relativeError, absoluteError));

    // Make the gap bigger (so relative error is 1 or 0.5)
    final long actual2 = expected / 2;
    // Actual is within 0.5 of expected
    Assertions.assertTrue(LongEquality.isCloseTo(expected, actual2, relativeError, absoluteError));
    // Expected is within 1.0 of actual
    Assertions.assertFalse(LongEquality.isCloseTo(actual2, expected, relativeError, absoluteError));
  }

  // int IsCloseTo

  @Test
  void testIntsIsCloseToThrows() {
    final int expected = 0;
    final int actual = 0;
    final double relativeError = 0;
    final long absoluteError = 0;

    for (double goodRelativeError : new double[] {2, Double.MAX_VALUE}) {
      IntEquality.isCloseTo(expected, actual, goodRelativeError, absoluteError);
    }
    for (double badRelativeError : new double[] {Double.NaN, Double.POSITIVE_INFINITY, -1}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        IntEquality.isCloseTo(expected, actual, badRelativeError, absoluteError);
      });
    }
    for (long badAbsoluteError : new long[] {Integer.MIN_VALUE, -1, IntEquality.MAX_ABS_ERROR}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        IntEquality.isCloseTo(expected, actual, relativeError, badAbsoluteError);
      });
    }
  }

  @Test
  void testIntsIsCloseToUsingNoError() {
    final double relativeError = 0;
    final long absoluteError = 0;

    // Use a range of values
    for (int value : new int[] {Integer.MIN_VALUE, Integer.MIN_VALUE + 1, -1, 0, 1,
        Integer.MAX_VALUE - 1, Integer.MAX_VALUE}) {
      Assertions.assertTrue(IntEquality.isCloseTo(value, value, relativeError, absoluteError));
      Assertions
          .assertFalse(IntEquality.isCloseTo(value, nextUp(value), relativeError, absoluteError));
      Assertions
          .assertFalse(IntEquality.isCloseTo(value, nextDown(value), relativeError, absoluteError));
      Assertions
          .assertFalse(IntEquality.isCloseTo(nextUp(value), value, relativeError, absoluteError));
      Assertions
          .assertFalse(IntEquality.isCloseTo(nextDown(value), value, relativeError, absoluteError));
    }
    // Test overflow of ints
    Assertions
        .assertFalse(IntEquality.isCloseTo(Integer.MAX_VALUE, -1, relativeError, absoluteError));
    Assertions
        .assertFalse(IntEquality.isCloseTo(Integer.MIN_VALUE, 0, relativeError, absoluteError));
    Assertions.assertFalse(
        IntEquality.isCloseTo(Integer.MAX_VALUE, Integer.MIN_VALUE, relativeError, absoluteError));
  }

  @Test
  void testIntsIsCloseToUsingAbsoluteError() {
    final int expected = 2;
    final int actual = 1;
    final double relativeError = 0;
    final long absoluteError = expected - actual;

    // Order insensitive
    Assertions.assertTrue(IntEquality.isCloseTo(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(IntEquality.isCloseTo(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final long delta = nextDown(absoluteError);
    Assertions.assertFalse(IntEquality.isCloseTo(expected, actual, relativeError, delta));
    Assertions.assertFalse(IntEquality.isCloseTo(actual, expected, relativeError, delta));

    // Make the gap bigger
    final int expected2 = nextUp(expected);
    Assertions.assertFalse(IntEquality.isCloseTo(expected2, actual, relativeError, absoluteError));
    Assertions.assertFalse(IntEquality.isCloseTo(actual, expected2, relativeError, absoluteError));
  }

  @Test
  void testIntsIsCloseToUsingRelativeError() {
    final int expected = 2;
    final int actual = 1;
    final double relativeError = 0.5;
    final long absoluteError = 0;

    // Order sensitive
    Assertions.assertTrue(IntEquality.isCloseTo(expected, actual, relativeError, absoluteError));
    Assertions.assertFalse(IntEquality.isCloseTo(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final double delta = Math.nextDown(relativeError);
    Assertions.assertFalse(IntEquality.isCloseTo(expected, actual, delta, absoluteError));

    // Make the gap bigger
    final int expected2 = nextUp(expected);
    Assertions.assertFalse(IntEquality.isCloseTo(expected2, actual, relativeError, absoluteError));
  }

  @Test
  void testIntsIsCloseToUsingRelativeErrorAtMinValue() {
    final int expected = Integer.MIN_VALUE;
    final int actual = expected + 1;
    final double relativeError = 0.5;
    final long absoluteError = 0;

    // Both within the relative error of 0.5
    Assertions.assertTrue(IntEquality.isCloseTo(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(IntEquality.isCloseTo(actual, expected, relativeError, absoluteError));

    // Make the gap bigger (so relative error is 1 or 0.5)
    final int actual2 = expected / 2;
    // Actual is within 0.5 of expected
    Assertions.assertTrue(IntEquality.isCloseTo(expected, actual2, relativeError, absoluteError));
    // Expected is within 1.0 of actual
    Assertions.assertFalse(IntEquality.isCloseTo(actual2, expected, relativeError, absoluteError));
  }

  // short IsCloseTo

  @Test
  void testShortsIsCloseToThrows() {
    final short expected = 0;
    final short actual = 0;
    final double relativeError = 0;
    final int absoluteError = 0;

    for (double goodRelativeError : new double[] {2, Double.MAX_VALUE}) {
      ShortEquality.isCloseTo(expected, actual, goodRelativeError, absoluteError);
    }
    for (double badRelativeError : new double[] {Double.NaN, Double.POSITIVE_INFINITY, -1}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        ShortEquality.isCloseTo(expected, actual, badRelativeError, absoluteError);
      });
    }
    for (int badAbsoluteError : new int[] {Short.MIN_VALUE, -1, ShortEquality.MAX_ABS_ERROR}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        ShortEquality.isCloseTo(expected, actual, relativeError, badAbsoluteError);
      });
    }
  }

  @Test
  void testShortsIsCloseToUsingNoError() {
    final double relativeError = 0;
    final int absoluteError = 0;

    // Use a range of values
    for (short value : new short[] {Short.MIN_VALUE, Short.MIN_VALUE + 1, -1, 0, 1,
        Short.MAX_VALUE - 1, Short.MAX_VALUE}) {
      Assertions.assertTrue(ShortEquality.isCloseTo(value, value, relativeError, absoluteError));
      Assertions
          .assertFalse(ShortEquality.isCloseTo(value, nextUp(value), relativeError, absoluteError));
      Assertions.assertFalse(
          ShortEquality.isCloseTo(value, nextDown(value), relativeError, absoluteError));
      Assertions
          .assertFalse(ShortEquality.isCloseTo(nextUp(value), value, relativeError, absoluteError));
      Assertions.assertFalse(
          ShortEquality.isCloseTo(nextDown(value), value, relativeError, absoluteError));
    }
    // Test overflow of shorts
    Assertions.assertFalse(
        ShortEquality.isCloseTo(Short.MAX_VALUE, (short) -1, relativeError, absoluteError));
    Assertions.assertFalse(
        ShortEquality.isCloseTo(Short.MIN_VALUE, (short) 0, relativeError, absoluteError));
    Assertions.assertFalse(
        ShortEquality.isCloseTo(Short.MAX_VALUE, Short.MIN_VALUE, relativeError, absoluteError));
  }

  @Test
  void testShortsIsCloseToUsingAbsoluteError() {
    final short expected = 2;
    final short actual = 1;
    final double relativeError = 0;
    final int absoluteError = expected - actual;

    // Order insensitive
    Assertions.assertTrue(ShortEquality.isCloseTo(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(ShortEquality.isCloseTo(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final int delta = nextDown(absoluteError);
    Assertions.assertFalse(ShortEquality.isCloseTo(expected, actual, relativeError, delta));
    Assertions.assertFalse(ShortEquality.isCloseTo(actual, expected, relativeError, delta));

    // Make the gap bigger
    final short expected2 = nextUp(expected);
    Assertions
        .assertFalse(ShortEquality.isCloseTo(expected2, actual, relativeError, absoluteError));
    Assertions
        .assertFalse(ShortEquality.isCloseTo(actual, expected2, relativeError, absoluteError));
  }

  @Test
  void testShortsIsCloseToUsingRelativeError() {
    final short expected = 2;
    final short actual = 1;
    final double relativeError = 0.5;
    final int absoluteError = 0;

    // Order sensitive
    Assertions.assertTrue(ShortEquality.isCloseTo(expected, actual, relativeError, absoluteError));
    Assertions.assertFalse(ShortEquality.isCloseTo(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final double delta = Math.nextDown(relativeError);
    Assertions.assertFalse(ShortEquality.isCloseTo(expected, actual, delta, absoluteError));

    // Make the gap bigger
    final short expected2 = nextUp(expected);
    Assertions
        .assertFalse(ShortEquality.isCloseTo(expected2, actual, relativeError, absoluteError));
  }

  @Test
  void testShortsIsCloseToUsingRelativeErrorAtMinValue() {
    final short expected = Short.MIN_VALUE;
    final short actual = expected + 1;
    final double relativeError = 0.5;
    final int absoluteError = 0;

    // Both within the relative error of 0.5
    Assertions.assertTrue(ShortEquality.isCloseTo(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(ShortEquality.isCloseTo(actual, expected, relativeError, absoluteError));

    // Make the gap bigger (so relative error is 1 or 0.5)
    final short actual2 = expected / 2;
    // Actual is within 0.5 of expected
    Assertions.assertTrue(ShortEquality.isCloseTo(expected, actual2, relativeError, absoluteError));
    // Expected is within 1.0 of actual
    Assertions
        .assertFalse(ShortEquality.isCloseTo(actual2, expected, relativeError, absoluteError));
  }

  // byte IsCloseTo

  @Test
  void testBytesIsCloseToThrows() {
    final byte expected = 0;
    final byte actual = 0;
    final double relativeError = 0;
    final int absoluteError = 0;

    for (double goodRelativeError : new double[] {2, Double.MAX_VALUE}) {
      ByteEquality.isCloseTo(expected, actual, goodRelativeError, absoluteError);
    }
    for (double badRelativeError : new double[] {Double.NaN, Double.POSITIVE_INFINITY, -1}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        ByteEquality.isCloseTo(expected, actual, badRelativeError, absoluteError);
      });
    }
    for (int badAbsoluteError : new int[] {Byte.MIN_VALUE, -1, ByteEquality.MAX_ABS_ERROR}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        ByteEquality.isCloseTo(expected, actual, relativeError, badAbsoluteError);
      });
    }
  }

  @Test
  void testBytesIsCloseToUsingNoError() {
    final double relativeError = 0;
    final int absoluteError = 0;

    // Use a range of values
    for (byte value : new byte[] {Byte.MIN_VALUE, Byte.MIN_VALUE + 1, -1, 0, 1, Byte.MAX_VALUE - 1,
        Byte.MAX_VALUE}) {
      Assertions.assertTrue(ByteEquality.isCloseTo(value, value, relativeError, absoluteError));
      Assertions
          .assertFalse(ByteEquality.isCloseTo(value, nextUp(value), relativeError, absoluteError));
      Assertions.assertFalse(
          ByteEquality.isCloseTo(value, nextDown(value), relativeError, absoluteError));
      Assertions
          .assertFalse(ByteEquality.isCloseTo(nextUp(value), value, relativeError, absoluteError));
      Assertions.assertFalse(
          ByteEquality.isCloseTo(nextDown(value), value, relativeError, absoluteError));
    }
    // Test overflow of bytes
    Assertions.assertFalse(
        ByteEquality.isCloseTo(Byte.MAX_VALUE, (byte) -1, relativeError, absoluteError));
    Assertions.assertFalse(
        ByteEquality.isCloseTo(Byte.MIN_VALUE, (byte) 0, relativeError, absoluteError));
    Assertions.assertFalse(
        ByteEquality.isCloseTo(Byte.MAX_VALUE, Byte.MIN_VALUE, relativeError, absoluteError));
  }

  @Test
  void testBytesIsCloseToUsingAbsoluteError() {
    final byte expected = 2;
    final byte actual = 1;
    final double relativeError = 0;
    final int absoluteError = expected - actual;

    // Order insensitive
    Assertions.assertTrue(ByteEquality.isCloseTo(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(ByteEquality.isCloseTo(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final int delta = nextDown(absoluteError);
    Assertions.assertFalse(ByteEquality.isCloseTo(expected, actual, relativeError, delta));
    Assertions.assertFalse(ByteEquality.isCloseTo(actual, expected, relativeError, delta));

    // Make the gap bigger
    final byte expected2 = nextUp(expected);
    Assertions.assertFalse(ByteEquality.isCloseTo(expected2, actual, relativeError, absoluteError));
    Assertions.assertFalse(ByteEquality.isCloseTo(actual, expected2, relativeError, absoluteError));
  }

  @Test
  void testBytesIsCloseToUsingRelativeError() {
    final byte expected = 2;
    final byte actual = 1;
    final double relativeError = 0.5;
    final int absoluteError = 0;

    // Order sensitive
    Assertions.assertTrue(ByteEquality.isCloseTo(expected, actual, relativeError, absoluteError));
    Assertions.assertFalse(ByteEquality.isCloseTo(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final double delta = Math.nextDown(relativeError);
    Assertions.assertFalse(ByteEquality.isCloseTo(expected, actual, delta, absoluteError));

    // Make the gap bigger
    final byte expected2 = nextUp(expected);
    Assertions.assertFalse(ByteEquality.isCloseTo(expected2, actual, relativeError, absoluteError));
  }

  @Test
  void testBytesIsCloseToUsingRelativeErrorAtMinValue() {
    final byte expected = Byte.MIN_VALUE;
    final byte actual = expected + 1;
    final double relativeError = 0.5;
    final int absoluteError = 0;

    // Both within the relative error of 0.5
    Assertions.assertTrue(ByteEquality.isCloseTo(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(ByteEquality.isCloseTo(actual, expected, relativeError, absoluteError));

    // Make the gap bigger (so relative error is 1 or 0.5)
    final byte actual2 = expected / 2;
    // Actual is within 0.5 of expected
    Assertions.assertTrue(ByteEquality.isCloseTo(expected, actual2, relativeError, absoluteError));
    // Expected is within 1.0 of actual
    Assertions.assertFalse(ByteEquality.isCloseTo(actual2, expected, relativeError, absoluteError));
  }
}
