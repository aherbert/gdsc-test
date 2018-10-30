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
        Assertions.assertFalse(EqualityUtils.areWithin(value, nextUp(value), absoluteError));
        Assertions.assertFalse(EqualityUtils.areWithin(value, nextDown(value), absoluteError));
        Assertions.assertFalse(EqualityUtils.areWithin(nextUp(value), value, absoluteError));
        Assertions.assertFalse(EqualityUtils.areWithin(nextDown(value), value, absoluteError));
      }
    }
    // Test overflow of longs
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
        Assertions.assertFalse(EqualityUtils.areWithin(value, nextUp(value), absoluteError));
        Assertions.assertFalse(EqualityUtils.areWithin(value, nextDown(value), absoluteError));
        Assertions.assertFalse(EqualityUtils.areWithin(nextUp(value), value, absoluteError));
        Assertions.assertFalse(EqualityUtils.areWithin(nextDown(value), value, absoluteError));
      }
    }
    // Test overflow of longs
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
        Assertions.assertFalse(EqualityUtils.areWithin(value, nextUp(value), absoluteError));
        Assertions.assertFalse(EqualityUtils.areWithin(value, nextDown(value), absoluteError));
        Assertions.assertFalse(EqualityUtils.areWithin(nextUp(value), value, absoluteError));
        Assertions.assertFalse(EqualityUtils.areWithin(nextDown(value), value, absoluteError));
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
        Assertions.assertFalse(EqualityUtils.areWithin(value, nextUp(value), absoluteError));
        Assertions.assertFalse(EqualityUtils.areWithin(value, nextDown(value), absoluteError));
        Assertions.assertFalse(EqualityUtils.areWithin(nextUp(value), value, absoluteError));
        Assertions.assertFalse(EqualityUtils.areWithin(nextDown(value), value, absoluteError));
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
        Assertions.assertFalse(EqualityUtils.areWithin(value, nextUp(value), absoluteError));
        Assertions.assertFalse(EqualityUtils.areWithin(value, nextDown(value), absoluteError));
        Assertions.assertFalse(EqualityUtils.areWithin(nextUp(value), value, absoluteError));
        Assertions.assertFalse(EqualityUtils.areWithin(nextDown(value), value, absoluteError));
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

  // long Close

  @Test
  public void testLongsAreCloseThrows() {
    final long expected = 0;
    final long actual = 0;
    final double relativeError = 0;
    final long absoluteError = 0;

    for (double badRelativeError : new double[] {Double.NaN, Double.POSITIVE_INFINITY, -1, 2,
        Double.MAX_VALUE}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        EqualityUtils.areClose(expected, actual, badRelativeError, absoluteError);
      });
    }
    for (long badAbsoluteError : new long[] {Long.MIN_VALUE, -1}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        EqualityUtils.areClose(expected, actual, relativeError, badAbsoluteError);
      });
    }
  }

  @Test
  public void testLongsAreCloseUsingNoError() {
    final double relativeError = 0;
    final long absoluteError = 0;

    // Use a range of values
    for (long value : new long[] {Long.MIN_VALUE, Long.MIN_VALUE + 1, -1, 0, 1, Long.MAX_VALUE - 1,
        Long.MAX_VALUE}) {
      Assertions.assertTrue(EqualityUtils.areClose(value, value, relativeError, absoluteError));
      Assertions
          .assertFalse(EqualityUtils.areClose(value, nextUp(value), relativeError, absoluteError));
      Assertions.assertFalse(
          EqualityUtils.areClose(value, nextDown(value), relativeError, absoluteError));
      Assertions
          .assertFalse(EqualityUtils.areClose(nextUp(value), value, relativeError, absoluteError));
      Assertions.assertFalse(
          EqualityUtils.areClose(nextDown(value), value, relativeError, absoluteError));
    }
    // Test overflow of longs
    Assertions
        .assertFalse(EqualityUtils.areClose(Long.MAX_VALUE, -1, relativeError, absoluteError));
    Assertions.assertFalse(EqualityUtils.areClose(Long.MIN_VALUE, 0, relativeError, absoluteError));
    Assertions.assertFalse(
        EqualityUtils.areClose(Long.MAX_VALUE, Long.MIN_VALUE, relativeError, absoluteError));
  }

  @Test
  public void testLongsAreCloseUsingAbsoluteError() {
    final long expected = 2;
    final long actual = 1;
    final double relativeError = 0;
    final long absoluteError = expected - actual;

    // Order insensitive
    Assertions.assertTrue(EqualityUtils.areClose(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(EqualityUtils.areClose(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final long delta = nextDown(absoluteError);
    Assertions.assertFalse(EqualityUtils.areClose(expected, actual, relativeError, delta));
    Assertions.assertFalse(EqualityUtils.areClose(actual, expected, relativeError, delta));

    // Make the gap bigger
    final long expected2 = nextUp(expected);
    Assertions.assertFalse(EqualityUtils.areClose(expected2, actual, relativeError, absoluteError));
    Assertions.assertFalse(EqualityUtils.areClose(actual, expected2, relativeError, absoluteError));
  }

  @Test
  public void testLongsAreCloseUsingRelativeError() {
    final long expected = 2;
    final long actual = 1;
    final double relativeError = 0.5;
    final long absoluteError = 0;

    // Order insensitive
    Assertions.assertTrue(EqualityUtils.areClose(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(EqualityUtils.areClose(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final double delta = Math.nextDown(relativeError);
    Assertions.assertFalse(EqualityUtils.areClose(expected, actual, delta, absoluteError));
    Assertions.assertFalse(EqualityUtils.areClose(actual, expected, delta, absoluteError));

    // Make the gap bigger
    final long expected2 = nextUp(expected);
    Assertions.assertFalse(EqualityUtils.areClose(expected2, actual, relativeError, absoluteError));
    Assertions.assertFalse(EqualityUtils.areClose(actual, expected2, relativeError, absoluteError));
  }

  @Test
  public void testLongsAreCloseUsingRelativeErrorAtMinValue() {
    final long expected = Long.MIN_VALUE;
    final long actual = expected + 1;
    final double relativeError = 0.5 - 1e-3;
    final long absoluteError = 0;

    // Order insensitive
    Assertions.assertTrue(EqualityUtils.areClose(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(EqualityUtils.areClose(actual, expected, relativeError, absoluteError));

    // Make the gap bigger (so relative error is 0.5)
    final long expected2 = expected / 2;
    Assertions.assertFalse(EqualityUtils.areClose(expected2, actual, relativeError, absoluteError));
    Assertions.assertFalse(EqualityUtils.areClose(actual, expected2, relativeError, absoluteError));
  }

  // int Close

  @Test
  public void testIntsAreCloseThrows() {
    final int expected = 0;
    final int actual = 0;
    final double relativeError = 0;
    final long absoluteError = 0;

    for (double badRelativeError : new double[] {Double.NaN, Double.POSITIVE_INFINITY, -1, 2,
        Double.MAX_VALUE}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        EqualityUtils.areClose(expected, actual, badRelativeError, absoluteError);
      });
    }
    for (long badAbsoluteError : new long[] {Integer.MIN_VALUE, -1,
        EqualityUtils.MAX_INT_ABS_ERROR}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        EqualityUtils.areClose(expected, actual, relativeError, badAbsoluteError);
      });
    }
  }

  @Test
  public void testIntsAreCloseUsingNoError() {
    final double relativeError = 0;
    final long absoluteError = 0;

    // Use a range of values
    for (int value : new int[] {Integer.MIN_VALUE, Integer.MIN_VALUE + 1, -1, 0, 1,
        Integer.MAX_VALUE - 1, Integer.MAX_VALUE}) {
      Assertions.assertTrue(EqualityUtils.areClose(value, value, relativeError, absoluteError));
      Assertions
          .assertFalse(EqualityUtils.areClose(value, nextUp(value), relativeError, absoluteError));
      Assertions.assertFalse(
          EqualityUtils.areClose(value, nextDown(value), relativeError, absoluteError));
      Assertions
          .assertFalse(EqualityUtils.areClose(nextUp(value), value, relativeError, absoluteError));
      Assertions.assertFalse(
          EqualityUtils.areClose(nextDown(value), value, relativeError, absoluteError));
    }
    // Test overflow of ints
    Assertions
        .assertFalse(EqualityUtils.areClose(Integer.MAX_VALUE, -1, relativeError, absoluteError));
    Assertions
        .assertFalse(EqualityUtils.areClose(Integer.MIN_VALUE, 0, relativeError, absoluteError));
    Assertions.assertFalse(
        EqualityUtils.areClose(Integer.MAX_VALUE, Integer.MIN_VALUE, relativeError, absoluteError));
  }

  @Test
  public void testIntsAreCloseUsingAbsoluteError() {
    final int expected = 2;
    final int actual = 1;
    final double relativeError = 0;
    final long absoluteError = expected - actual;

    // Order insensitive
    Assertions.assertTrue(EqualityUtils.areClose(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(EqualityUtils.areClose(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final long delta = nextDown(absoluteError);
    Assertions.assertFalse(EqualityUtils.areClose(expected, actual, relativeError, delta));
    Assertions.assertFalse(EqualityUtils.areClose(actual, expected, relativeError, delta));

    // Make the gap bigger
    final int expected2 = nextUp(expected);
    Assertions.assertFalse(EqualityUtils.areClose(expected2, actual, relativeError, absoluteError));
    Assertions.assertFalse(EqualityUtils.areClose(actual, expected2, relativeError, absoluteError));
  }

  @Test
  public void testIntsAreCloseUsingRelativeError() {
    final int expected = 2;
    final int actual = 1;
    final double relativeError = 0.5;
    final long absoluteError = 0;

    // Order insensitive
    Assertions.assertTrue(EqualityUtils.areClose(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(EqualityUtils.areClose(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final double delta = Math.nextDown(relativeError);
    Assertions.assertFalse(EqualityUtils.areClose(expected, actual, delta, absoluteError));
    Assertions.assertFalse(EqualityUtils.areClose(actual, expected, delta, absoluteError));

    // Make the gap bigger
    final int expected2 = nextUp(expected);
    Assertions.assertFalse(EqualityUtils.areClose(expected2, actual, relativeError, absoluteError));
    Assertions.assertFalse(EqualityUtils.areClose(actual, expected2, relativeError, absoluteError));
  }

  @Test
  public void testIntsAreCloseUsingRelativeErrorAtMinValue() {
    final int expected = Integer.MIN_VALUE;
    final int actual = expected + 1;
    final double relativeError = 0.5 - 1e-3;
    final long absoluteError = 0;

    // Order insensitive
    Assertions.assertTrue(EqualityUtils.areClose(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(EqualityUtils.areClose(actual, expected, relativeError, absoluteError));

    // Make the gap bigger (so relative error is 0.5)
    final int expected2 = expected / 2;
    Assertions.assertFalse(EqualityUtils.areClose(expected2, actual, relativeError, absoluteError));
    Assertions.assertFalse(EqualityUtils.areClose(actual, expected2, relativeError, absoluteError));
  }

  // short Close

  @Test
  public void testShortsAreCloseThrows() {
    final short expected = 0;
    final short actual = 0;
    final double relativeError = 0;
    final int absoluteError = 0;

    for (double badRelativeError : new double[] {Double.NaN, Double.POSITIVE_INFINITY, -1, 2,
        Double.MAX_VALUE}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        EqualityUtils.areClose(expected, actual, badRelativeError, absoluteError);
      });
    }
    for (int badAbsoluteError : new int[] {Short.MIN_VALUE, -1,
        EqualityUtils.MAX_SHORT_ABS_ERROR}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        EqualityUtils.areClose(expected, actual, relativeError, badAbsoluteError);
      });
    }
  }

  @Test
  public void testShortsAreCloseUsingNoError() {
    final double relativeError = 0;
    final int absoluteError = 0;

    // Use a range of values
    for (short value : new short[] {Short.MIN_VALUE, Short.MIN_VALUE + 1, -1, 0, 1,
        Short.MAX_VALUE - 1, Short.MAX_VALUE}) {
      Assertions.assertTrue(EqualityUtils.areClose(value, value, relativeError, absoluteError));
      Assertions
          .assertFalse(EqualityUtils.areClose(value, nextUp(value), relativeError, absoluteError));
      Assertions.assertFalse(
          EqualityUtils.areClose(value, nextDown(value), relativeError, absoluteError));
      Assertions
          .assertFalse(EqualityUtils.areClose(nextUp(value), value, relativeError, absoluteError));
      Assertions.assertFalse(
          EqualityUtils.areClose(nextDown(value), value, relativeError, absoluteError));
    }
    // Test overflow of shorts
    Assertions
        .assertFalse(EqualityUtils.areClose(Short.MAX_VALUE, -1, relativeError, absoluteError));
    Assertions
        .assertFalse(EqualityUtils.areClose(Short.MIN_VALUE, 0, relativeError, absoluteError));
    Assertions.assertFalse(
        EqualityUtils.areClose(Short.MAX_VALUE, Short.MIN_VALUE, relativeError, absoluteError));
  }

  @Test
  public void testShortsAreCloseUsingAbsoluteError() {
    final short expected = 2;
    final short actual = 1;
    final double relativeError = 0;
    final int absoluteError = expected - actual;

    // Order insensitive
    Assertions.assertTrue(EqualityUtils.areClose(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(EqualityUtils.areClose(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final int delta = nextDown(absoluteError);
    Assertions.assertFalse(EqualityUtils.areClose(expected, actual, relativeError, delta));
    Assertions.assertFalse(EqualityUtils.areClose(actual, expected, relativeError, delta));

    // Make the gap bigger
    final short expected2 = nextUp(expected);
    Assertions.assertFalse(EqualityUtils.areClose(expected2, actual, relativeError, absoluteError));
    Assertions.assertFalse(EqualityUtils.areClose(actual, expected2, relativeError, absoluteError));
  }

  @Test
  public void testShortsAreCloseUsingRelativeError() {
    final short expected = 2;
    final short actual = 1;
    final double relativeError = 0.5;
    final int absoluteError = 0;

    // Order insensitive
    Assertions.assertTrue(EqualityUtils.areClose(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(EqualityUtils.areClose(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final double delta = Math.nextDown(relativeError);
    Assertions.assertFalse(EqualityUtils.areClose(expected, actual, delta, absoluteError));
    Assertions.assertFalse(EqualityUtils.areClose(actual, expected, delta, absoluteError));

    // Make the gap bigger
    final short expected2 = nextUp(expected);
    Assertions.assertFalse(EqualityUtils.areClose(expected2, actual, relativeError, absoluteError));
    Assertions.assertFalse(EqualityUtils.areClose(actual, expected2, relativeError, absoluteError));
  }

  @Test
  public void testShortsAreCloseUsingRelativeErrorAtMinValue() {
    final short expected = Short.MIN_VALUE;
    final short actual = expected + 1;
    final double relativeError = 0.5 - 1e-3;
    final int absoluteError = 0;

    // Order insensitive
    Assertions.assertTrue(EqualityUtils.areClose(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(EqualityUtils.areClose(actual, expected, relativeError, absoluteError));

    // Make the gap bigger (so relative error is 0.5)
    final short expected2 = expected / 2;
    Assertions.assertFalse(EqualityUtils.areClose(expected2, actual, relativeError, absoluteError));
    Assertions.assertFalse(EqualityUtils.areClose(actual, expected2, relativeError, absoluteError));
  }

  // byte Close

  @Test
  public void testBytesAreCloseThrows() {
    final byte expected = 0;
    final byte actual = 0;
    final double relativeError = 0;
    final int absoluteError = 0;

    for (double badRelativeError : new double[] {Double.NaN, Double.POSITIVE_INFINITY, -1, 2,
        Double.MAX_VALUE}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        EqualityUtils.areClose(expected, actual, badRelativeError, absoluteError);
      });
    }
    for (int badAbsoluteError : new int[] {Byte.MIN_VALUE, -1, EqualityUtils.MAX_BYTE_ABS_ERROR}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        EqualityUtils.areClose(expected, actual, relativeError, badAbsoluteError);
      });
    }
  }

  @Test
  public void testBytesAreCloseUsingNoError() {
    final double relativeError = 0;
    final int absoluteError = 0;

    // Use a range of values
    for (byte value : new byte[] {Byte.MIN_VALUE, Byte.MIN_VALUE + 1, -1, 0, 1, Byte.MAX_VALUE - 1,
        Byte.MAX_VALUE}) {
      Assertions.assertTrue(EqualityUtils.areClose(value, value, relativeError, absoluteError));
      Assertions
          .assertFalse(EqualityUtils.areClose(value, nextUp(value), relativeError, absoluteError));
      Assertions.assertFalse(
          EqualityUtils.areClose(value, nextDown(value), relativeError, absoluteError));
      Assertions
          .assertFalse(EqualityUtils.areClose(nextUp(value), value, relativeError, absoluteError));
      Assertions.assertFalse(
          EqualityUtils.areClose(nextDown(value), value, relativeError, absoluteError));
    }
    // Test overflow of bytes
    Assertions
        .assertFalse(EqualityUtils.areClose(Byte.MAX_VALUE, -1, relativeError, absoluteError));
    Assertions.assertFalse(EqualityUtils.areClose(Byte.MIN_VALUE, 0, relativeError, absoluteError));
    Assertions.assertFalse(
        EqualityUtils.areClose(Byte.MAX_VALUE, Byte.MIN_VALUE, relativeError, absoluteError));
  }

  @Test
  public void testBytesAreCloseUsingAbsoluteError() {
    final byte expected = 2;
    final byte actual = 1;
    final double relativeError = 0;
    final int absoluteError = expected - actual;

    // Order insensitive
    Assertions.assertTrue(EqualityUtils.areClose(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(EqualityUtils.areClose(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final int delta = nextDown(absoluteError);
    Assertions.assertFalse(EqualityUtils.areClose(expected, actual, relativeError, delta));
    Assertions.assertFalse(EqualityUtils.areClose(actual, expected, relativeError, delta));

    // Make the gap bigger
    final byte expected2 = nextUp(expected);
    Assertions.assertFalse(EqualityUtils.areClose(expected2, actual, relativeError, absoluteError));
    Assertions.assertFalse(EqualityUtils.areClose(actual, expected2, relativeError, absoluteError));
  }

  @Test
  public void testBytesAreCloseUsingRelativeError() {
    final byte expected = 2;
    final byte actual = 1;
    final double relativeError = 0.5;
    final int absoluteError = 0;

    // Order insensitive
    Assertions.assertTrue(EqualityUtils.areClose(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(EqualityUtils.areClose(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final double delta = Math.nextDown(relativeError);
    Assertions.assertFalse(EqualityUtils.areClose(expected, actual, delta, absoluteError));
    Assertions.assertFalse(EqualityUtils.areClose(actual, expected, delta, absoluteError));

    // Make the gap bigger
    final byte expected2 = nextUp(expected);
    Assertions.assertFalse(EqualityUtils.areClose(expected2, actual, relativeError, absoluteError));
    Assertions.assertFalse(EqualityUtils.areClose(actual, expected2, relativeError, absoluteError));
  }

  @Test
  public void testBytesAreCloseUsingRelativeErrorAtMinValue() {
    final byte expected = Byte.MIN_VALUE;
    final byte actual = expected + 1;
    final double relativeError = 0.49;
    final int absoluteError = 0;

    // Order insensitive
    Assertions.assertTrue(EqualityUtils.areClose(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(EqualityUtils.areClose(actual, expected, relativeError, absoluteError));

    // Make the gap bigger (so relative error is 0.5)
    final byte expected2 = expected / 2;
    Assertions.assertFalse(EqualityUtils.areClose(expected2, actual, relativeError, absoluteError));
    Assertions.assertFalse(EqualityUtils.areClose(actual, expected2, relativeError, absoluteError));
  }

  // float IsCloseTo

  @Test
  public void testFloatsIsCloseToThrows() {
    final float expected = 0;
    final float actual = 0;
    final double relativeError = 0;
    final double absoluteError = 0;

    for (double goodRelativeError : new double[] {2, Double.MAX_VALUE}) {
      EqualityUtils.isCloseTo(expected, actual, goodRelativeError, absoluteError);
    }
    for (double badRelativeError : new double[] {Double.NaN, Double.POSITIVE_INFINITY, -1}) {
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

  // double Close

  @Test
  public void testDoublesIsCloseToThrows() {
    final double expected = 0;
    final double actual = 0;
    final double relativeError = 0;
    final double absoluteError = 0;

    for (double goodRelativeError : new double[] {2, Double.MAX_VALUE}) {
      EqualityUtils.isCloseTo(expected, actual, goodRelativeError, absoluteError);
    }
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

  // long IsCloseTo

  @Test
  public void testLongsIsCloseToThrows() {
    final long expected = 0;
    final long actual = 0;
    final double relativeError = 0;
    final long absoluteError = 0;

    for (double goodRelativeError : new double[] {2, Double.MAX_VALUE}) {
      EqualityUtils.isCloseTo(expected, actual, goodRelativeError, absoluteError);
    }
    for (double badRelativeError : new double[] {Double.NaN, Double.POSITIVE_INFINITY, -1}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        EqualityUtils.isCloseTo(expected, actual, badRelativeError, absoluteError);
      });
    }
    for (long badAbsoluteError : new long[] {Long.MIN_VALUE, -1}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        EqualityUtils.isCloseTo(expected, actual, relativeError, badAbsoluteError);
      });
    }
  }

  @Test
  public void testLongsIsCloseToUsingNoError() {
    final double relativeError = 0;
    final long absoluteError = 0;

    // Use a range of values
    for (long value : new long[] {Long.MIN_VALUE, Long.MIN_VALUE + 1, -1, 0, 1, Long.MAX_VALUE - 1,
        Long.MAX_VALUE}) {
      Assertions.assertTrue(EqualityUtils.isCloseTo(value, value, relativeError, absoluteError));
      Assertions
          .assertFalse(EqualityUtils.isCloseTo(value, nextUp(value), relativeError, absoluteError));
      Assertions.assertFalse(
          EqualityUtils.isCloseTo(value, nextDown(value), relativeError, absoluteError));
      Assertions
          .assertFalse(EqualityUtils.isCloseTo(nextUp(value), value, relativeError, absoluteError));
      Assertions.assertFalse(
          EqualityUtils.isCloseTo(nextDown(value), value, relativeError, absoluteError));
    }
    // Test overflow of longs
    Assertions
        .assertFalse(EqualityUtils.isCloseTo(Long.MAX_VALUE, -1, relativeError, absoluteError));
    Assertions
        .assertFalse(EqualityUtils.isCloseTo(Long.MIN_VALUE, 0, relativeError, absoluteError));
    Assertions.assertFalse(
        EqualityUtils.isCloseTo(Long.MAX_VALUE, Long.MIN_VALUE, relativeError, absoluteError));
  }

  @Test
  public void testLongsIsCloseToUsingAbsoluteError() {
    final long expected = 2;
    final long actual = 1;
    final double relativeError = 0;
    final long absoluteError = expected - actual;

    // Order insensitive
    Assertions.assertTrue(EqualityUtils.isCloseTo(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(EqualityUtils.isCloseTo(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final long delta = nextDown(absoluteError);
    Assertions.assertFalse(EqualityUtils.isCloseTo(expected, actual, relativeError, delta));
    Assertions.assertFalse(EqualityUtils.isCloseTo(actual, expected, relativeError, delta));

    // Make the gap bigger
    final long expected2 = nextUp(expected);
    Assertions
        .assertFalse(EqualityUtils.isCloseTo(expected2, actual, relativeError, absoluteError));
    Assertions
        .assertFalse(EqualityUtils.isCloseTo(actual, expected2, relativeError, absoluteError));
  }

  @Test
  public void testLongsIsCloseToUsingRelativeError() {
    final long expected = 2;
    final long actual = 1;
    final double relativeError = 0.5;
    final long absoluteError = 0;

    // Order sensitive
    Assertions.assertTrue(EqualityUtils.isCloseTo(expected, actual, relativeError, absoluteError));
    Assertions.assertFalse(EqualityUtils.isCloseTo(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final double delta = Math.nextDown(relativeError);
    Assertions.assertFalse(EqualityUtils.isCloseTo(expected, actual, delta, absoluteError));

    // Make the gap bigger
    final long expected2 = nextUp(expected);
    Assertions
        .assertFalse(EqualityUtils.isCloseTo(expected2, actual, relativeError, absoluteError));
  }

  @Test
  public void testLongsIsCloseToUsingRelativeErrorAtMinValue() {
    final long expected = Long.MIN_VALUE;
    final long actual = expected + 1;
    final double relativeError = 0.5;
    final long absoluteError = 0;

    // Both within the relative error of 0.5
    Assertions.assertTrue(EqualityUtils.isCloseTo(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(EqualityUtils.isCloseTo(actual, expected, relativeError, absoluteError));

    // Make the gap bigger (so relative error is 1 or 0.5)
    final long actual2 = expected / 2;
    // Actual is within 0.5 of expected
    Assertions.assertTrue(EqualityUtils.isCloseTo(expected, actual2, relativeError, absoluteError));
    // Expected is within 1.0 of actual
    Assertions
        .assertFalse(EqualityUtils.isCloseTo(actual2, expected, relativeError, absoluteError));
  }

  // int IsCloseTo

  @Test
  public void testIntsIsCloseToThrows() {
    final int expected = 0;
    final int actual = 0;
    final double relativeError = 0;
    final long absoluteError = 0;

    for (double goodRelativeError : new double[] {2, Double.MAX_VALUE}) {
      EqualityUtils.isCloseTo(expected, actual, goodRelativeError, absoluteError);
    }
    for (double badRelativeError : new double[] {Double.NaN, Double.POSITIVE_INFINITY, -1}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        EqualityUtils.isCloseTo(expected, actual, badRelativeError, absoluteError);
      });
    }
    for (long badAbsoluteError : new long[] {Integer.MIN_VALUE, -1,
        EqualityUtils.MAX_INT_ABS_ERROR}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        EqualityUtils.isCloseTo(expected, actual, relativeError, badAbsoluteError);
      });
    }
  }

  @Test
  public void testIntsIsCloseToUsingNoError() {
    final double relativeError = 0;
    final long absoluteError = 0;

    // Use a range of values
    for (int value : new int[] {Integer.MIN_VALUE, Integer.MIN_VALUE + 1, -1, 0, 1,
        Integer.MAX_VALUE - 1, Integer.MAX_VALUE}) {
      Assertions.assertTrue(EqualityUtils.isCloseTo(value, value, relativeError, absoluteError));
      Assertions
          .assertFalse(EqualityUtils.isCloseTo(value, nextUp(value), relativeError, absoluteError));
      Assertions.assertFalse(
          EqualityUtils.isCloseTo(value, nextDown(value), relativeError, absoluteError));
      Assertions
          .assertFalse(EqualityUtils.isCloseTo(nextUp(value), value, relativeError, absoluteError));
      Assertions.assertFalse(
          EqualityUtils.isCloseTo(nextDown(value), value, relativeError, absoluteError));
    }
    // Test overflow of ints
    Assertions
        .assertFalse(EqualityUtils.isCloseTo(Integer.MAX_VALUE, -1, relativeError, absoluteError));
    Assertions
        .assertFalse(EqualityUtils.isCloseTo(Integer.MIN_VALUE, 0, relativeError, absoluteError));
    Assertions.assertFalse(EqualityUtils.isCloseTo(Integer.MAX_VALUE, Integer.MIN_VALUE,
        relativeError, absoluteError));
  }

  @Test
  public void testIntsIsCloseToUsingAbsoluteError() {
    final int expected = 2;
    final int actual = 1;
    final double relativeError = 0;
    final long absoluteError = expected - actual;

    // Order insensitive
    Assertions.assertTrue(EqualityUtils.isCloseTo(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(EqualityUtils.isCloseTo(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final long delta = nextDown(absoluteError);
    Assertions.assertFalse(EqualityUtils.isCloseTo(expected, actual, relativeError, delta));
    Assertions.assertFalse(EqualityUtils.isCloseTo(actual, expected, relativeError, delta));

    // Make the gap bigger
    final int expected2 = nextUp(expected);
    Assertions
        .assertFalse(EqualityUtils.isCloseTo(expected2, actual, relativeError, absoluteError));
    Assertions
        .assertFalse(EqualityUtils.isCloseTo(actual, expected2, relativeError, absoluteError));
  }

  @Test
  public void testIntsIsCloseToUsingRelativeError() {
    final int expected = 2;
    final int actual = 1;
    final double relativeError = 0.5;
    final long absoluteError = 0;

    // Order sensitive
    Assertions.assertTrue(EqualityUtils.isCloseTo(expected, actual, relativeError, absoluteError));
    Assertions.assertFalse(EqualityUtils.isCloseTo(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final double delta = Math.nextDown(relativeError);
    Assertions.assertFalse(EqualityUtils.isCloseTo(expected, actual, delta, absoluteError));

    // Make the gap bigger
    final int expected2 = nextUp(expected);
    Assertions
        .assertFalse(EqualityUtils.isCloseTo(expected2, actual, relativeError, absoluteError));
  }

  @Test
  public void testIntsIsCloseToUsingRelativeErrorAtMinValue() {
    final int expected = Integer.MIN_VALUE;
    final int actual = expected + 1;
    final double relativeError = 0.5;
    final long absoluteError = 0;

    // Both within the relative error of 0.5
    Assertions.assertTrue(EqualityUtils.isCloseTo(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(EqualityUtils.isCloseTo(actual, expected, relativeError, absoluteError));

    // Make the gap bigger (so relative error is 1 or 0.5)
    final int actual2 = expected / 2;
    // Actual is within 0.5 of expected
    Assertions.assertTrue(EqualityUtils.isCloseTo(expected, actual2, relativeError, absoluteError));
    // Expected is within 1.0 of actual
    Assertions
        .assertFalse(EqualityUtils.isCloseTo(actual2, expected, relativeError, absoluteError));
  }

  // short IsCloseTo

  @Test
  public void testShortsIsCloseToThrows() {
    final short expected = 0;
    final short actual = 0;
    final double relativeError = 0;
    final int absoluteError = 0;

    for (double goodRelativeError : new double[] {2, Double.MAX_VALUE}) {
      EqualityUtils.isCloseTo(expected, actual, goodRelativeError, absoluteError);
    }
    for (double badRelativeError : new double[] {Double.NaN, Double.POSITIVE_INFINITY, -1}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        EqualityUtils.isCloseTo(expected, actual, badRelativeError, absoluteError);
      });
    }
    for (int badAbsoluteError : new int[] {Short.MIN_VALUE, -1,
        EqualityUtils.MAX_SHORT_ABS_ERROR}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        EqualityUtils.isCloseTo(expected, actual, relativeError, badAbsoluteError);
      });
    }
  }

  @Test
  public void testShortsIsCloseToUsingNoError() {
    final double relativeError = 0;
    final int absoluteError = 0;

    // Use a range of values
    for (short value : new short[] {Short.MIN_VALUE, Short.MIN_VALUE + 1, -1, 0, 1,
        Short.MAX_VALUE - 1, Short.MAX_VALUE}) {
      Assertions.assertTrue(EqualityUtils.isCloseTo(value, value, relativeError, absoluteError));
      Assertions
          .assertFalse(EqualityUtils.isCloseTo(value, nextUp(value), relativeError, absoluteError));
      Assertions.assertFalse(
          EqualityUtils.isCloseTo(value, nextDown(value), relativeError, absoluteError));
      Assertions
          .assertFalse(EqualityUtils.isCloseTo(nextUp(value), value, relativeError, absoluteError));
      Assertions.assertFalse(
          EqualityUtils.isCloseTo(nextDown(value), value, relativeError, absoluteError));
    }
    // Test overflow of shorts
    Assertions
        .assertFalse(EqualityUtils.isCloseTo(Short.MAX_VALUE, -1, relativeError, absoluteError));
    Assertions
        .assertFalse(EqualityUtils.isCloseTo(Short.MIN_VALUE, 0, relativeError, absoluteError));
    Assertions.assertFalse(
        EqualityUtils.isCloseTo(Short.MAX_VALUE, Short.MIN_VALUE, relativeError, absoluteError));
  }

  @Test
  public void testShortsIsCloseToUsingAbsoluteError() {
    final short expected = 2;
    final short actual = 1;
    final double relativeError = 0;
    final int absoluteError = expected - actual;

    // Order insensitive
    Assertions.assertTrue(EqualityUtils.isCloseTo(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(EqualityUtils.isCloseTo(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final int delta = nextDown(absoluteError);
    Assertions.assertFalse(EqualityUtils.isCloseTo(expected, actual, relativeError, delta));
    Assertions.assertFalse(EqualityUtils.isCloseTo(actual, expected, relativeError, delta));

    // Make the gap bigger
    final short expected2 = nextUp(expected);
    Assertions
        .assertFalse(EqualityUtils.isCloseTo(expected2, actual, relativeError, absoluteError));
    Assertions
        .assertFalse(EqualityUtils.isCloseTo(actual, expected2, relativeError, absoluteError));
  }

  @Test
  public void testShortsIsCloseToUsingRelativeError() {
    final short expected = 2;
    final short actual = 1;
    final double relativeError = 0.5;
    final int absoluteError = 0;

    // Order sensitive
    Assertions.assertTrue(EqualityUtils.isCloseTo(expected, actual, relativeError, absoluteError));
    Assertions.assertFalse(EqualityUtils.isCloseTo(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final double delta = Math.nextDown(relativeError);
    Assertions.assertFalse(EqualityUtils.isCloseTo(expected, actual, delta, absoluteError));

    // Make the gap bigger
    final short expected2 = nextUp(expected);
    Assertions
        .assertFalse(EqualityUtils.isCloseTo(expected2, actual, relativeError, absoluteError));
  }

  @Test
  public void testShortsIsCloseToUsingRelativeErrorAtMinValue() {
    final short expected = Short.MIN_VALUE;
    final short actual = expected + 1;
    final double relativeError = 0.5;
    final int absoluteError = 0;

    // Both within the relative error of 0.5
    Assertions.assertTrue(EqualityUtils.isCloseTo(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(EqualityUtils.isCloseTo(actual, expected, relativeError, absoluteError));

    // Make the gap bigger (so relative error is 1 or 0.5)
    final short actual2 = expected / 2;
    // Actual is within 0.5 of expected
    Assertions.assertTrue(EqualityUtils.isCloseTo(expected, actual2, relativeError, absoluteError));
    // Expected is within 1.0 of actual
    Assertions
        .assertFalse(EqualityUtils.isCloseTo(actual2, expected, relativeError, absoluteError));
  }

  // byte IsCloseTo

  @Test
  public void testBytesIsCloseToThrows() {
    final byte expected = 0;
    final byte actual = 0;
    final double relativeError = 0;
    final int absoluteError = 0;

    for (double goodRelativeError : new double[] {2, Double.MAX_VALUE}) {
      EqualityUtils.isCloseTo(expected, actual, goodRelativeError, absoluteError);
    }
    for (double badRelativeError : new double[] {Double.NaN, Double.POSITIVE_INFINITY, -1}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        EqualityUtils.isCloseTo(expected, actual, badRelativeError, absoluteError);
      });
    }
    for (int badAbsoluteError : new int[] {Byte.MIN_VALUE, -1, EqualityUtils.MAX_BYTE_ABS_ERROR}) {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        EqualityUtils.isCloseTo(expected, actual, relativeError, badAbsoluteError);
      });
    }
  }

  @Test
  public void testBytesIsCloseToUsingNoError() {
    final double relativeError = 0;
    final int absoluteError = 0;

    // Use a range of values
    for (byte value : new byte[] {Byte.MIN_VALUE, Byte.MIN_VALUE + 1, -1, 0, 1, Byte.MAX_VALUE - 1,
        Byte.MAX_VALUE}) {
      Assertions.assertTrue(EqualityUtils.isCloseTo(value, value, relativeError, absoluteError));
      Assertions
          .assertFalse(EqualityUtils.isCloseTo(value, nextUp(value), relativeError, absoluteError));
      Assertions.assertFalse(
          EqualityUtils.isCloseTo(value, nextDown(value), relativeError, absoluteError));
      Assertions
          .assertFalse(EqualityUtils.isCloseTo(nextUp(value), value, relativeError, absoluteError));
      Assertions.assertFalse(
          EqualityUtils.isCloseTo(nextDown(value), value, relativeError, absoluteError));
    }
    // Test overflow of bytes
    Assertions
        .assertFalse(EqualityUtils.isCloseTo(Byte.MAX_VALUE, -1, relativeError, absoluteError));
    Assertions
        .assertFalse(EqualityUtils.isCloseTo(Byte.MIN_VALUE, 0, relativeError, absoluteError));
    Assertions.assertFalse(
        EqualityUtils.isCloseTo(Byte.MAX_VALUE, Byte.MIN_VALUE, relativeError, absoluteError));
  }

  @Test
  public void testBytesIsCloseToUsingAbsoluteError() {
    final byte expected = 2;
    final byte actual = 1;
    final double relativeError = 0;
    final int absoluteError = expected - actual;

    // Order insensitive
    Assertions.assertTrue(EqualityUtils.isCloseTo(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(EqualityUtils.isCloseTo(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final int delta = nextDown(absoluteError);
    Assertions.assertFalse(EqualityUtils.isCloseTo(expected, actual, relativeError, delta));
    Assertions.assertFalse(EqualityUtils.isCloseTo(actual, expected, relativeError, delta));

    // Make the gap bigger
    final byte expected2 = nextUp(expected);
    Assertions
        .assertFalse(EqualityUtils.isCloseTo(expected2, actual, relativeError, absoluteError));
    Assertions
        .assertFalse(EqualityUtils.isCloseTo(actual, expected2, relativeError, absoluteError));
  }

  @Test
  public void testBytesIsCloseToUsingRelativeError() {
    final byte expected = 2;
    final byte actual = 1;
    final double relativeError = 0.5;
    final int absoluteError = 0;

    // Order sensitive
    Assertions.assertTrue(EqualityUtils.isCloseTo(expected, actual, relativeError, absoluteError));
    Assertions.assertFalse(EqualityUtils.isCloseTo(actual, expected, relativeError, absoluteError));

    // Make the delta smaller
    final double delta = Math.nextDown(relativeError);
    Assertions.assertFalse(EqualityUtils.isCloseTo(expected, actual, delta, absoluteError));

    // Make the gap bigger
    final byte expected2 = nextUp(expected);
    Assertions
        .assertFalse(EqualityUtils.isCloseTo(expected2, actual, relativeError, absoluteError));
  }

  @Test
  public void testBytesIsCloseToUsingRelativeErrorAtMinValue() {
    final byte expected = Byte.MIN_VALUE;
    final byte actual = expected + 1;
    final double relativeError = 0.5;
    final int absoluteError = 0;

    // Both within the relative error of 0.5
    Assertions.assertTrue(EqualityUtils.isCloseTo(expected, actual, relativeError, absoluteError));
    Assertions.assertTrue(EqualityUtils.isCloseTo(actual, expected, relativeError, absoluteError));

    // Make the gap bigger (so relative error is 1 or 0.5)
    final byte actual2 = expected / 2;
    // Actual is within 0.5 of expected
    Assertions.assertTrue(EqualityUtils.isCloseTo(expected, actual2, relativeError, absoluteError));
    // Expected is within 1.0 of actual
    Assertions
        .assertFalse(EqualityUtils.isCloseTo(actual2, expected, relativeError, absoluteError));
  }
}
