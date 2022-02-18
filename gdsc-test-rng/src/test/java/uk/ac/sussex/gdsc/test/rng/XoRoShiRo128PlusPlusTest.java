/*-
 * #%L
 * Genome Damage and Stability Centre Test RNG
 *
 * Contains utilities for use with Commons RNG for random tests.
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

package uk.ac.sussex.gdsc.test.rng;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.commons.math3.stat.inference.ChiSquareTest;
import org.apache.commons.rng.RandomProviderState;
import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("javadoc")
class XoRoShiRo128PlusPlusTest {
  @Test
  void testNextLong() {
    // Tested with respect to commons-rng implementation.
    final long seed0 = ThreadLocalRandom.current().nextLong();
    final long seed1 = seed0 + 627384217384238449L;
    final UniformRandomProvider rng1 =
        RandomSource.XO_RO_SHI_RO_128_PP.create(new long[] {seed0, seed1});
    final XoRoShiRo128PlusPlus rng2 = new XoRoShiRo128PlusPlus(seed0, seed1);
    for (int i = 0; i < 200; i++) {
      Assertions.assertEquals(rng1.nextLong(), rng2.nextLong());
    }
  }

  @Test
  void testZeroSeed() {
    final XoRoShiRo128PlusPlus rng1 = new XoRoShiRo128PlusPlus(0);
    final XoRoShiRo128PlusPlus rng2 = new XoRoShiRo128PlusPlus(0, 0);
    boolean zeroOutput = true;
    for (int i = 0; i < 200; i++) {
      final long value = rng1.nextLong();
      Assertions.assertEquals(value, rng2.nextLong());
      if (value != 0) {
        zeroOutput = false;
      }
    }
    Assertions.assertFalse(zeroOutput, "Zero seed should not create all zero output");
  }

  /**
   * Hit the edge case where the one seed state is zero but the second is not (i.e. a partial zero
   * seed).
   */
  @Test
  void testPartialZeroSeed() {
    final XoRoShiRo128PlusPlus rng1 = new XoRoShiRo128PlusPlus(0);
    final XoRoShiRo128PlusPlus rng2 = new XoRoShiRo128PlusPlus(0, 1);
    Assertions.assertNotEquals(rng1.nextLong(), rng2.nextLong());
    final XoRoShiRo128PlusPlus rng3 = new XoRoShiRo128PlusPlus(0);
    final XoRoShiRo128PlusPlus rng4 = new XoRoShiRo128PlusPlus(1, 0);
    Assertions.assertNotEquals(rng3.nextLong(), rng4.nextLong());
  }

  /**
   * Check the 32-bit int is the upper bits from the long.
   */
  @Test
  void testNextIntIsUpper64Bits() {
    final long seed = ThreadLocalRandom.current().nextLong();
    final XoRoShiRo128PlusPlus rng1 = new XoRoShiRo128PlusPlus(seed);
    final XoRoShiRo128PlusPlus rng2 = new XoRoShiRo128PlusPlus(seed);
    for (int i = 0; i < 200; i++) {
      final int expected = (int) (rng1.nextLong() >>> 32);
      Assertions.assertEquals(expected, rng2.nextInt());
    }
  }

  /**
   * Check the boolean is a sign test on the long value.
   */
  @Test
  void testNextBooleanIsSignTest() {
    final long seed = ThreadLocalRandom.current().nextLong();
    final XoRoShiRo128PlusPlus rng1 = new XoRoShiRo128PlusPlus(seed);
    final XoRoShiRo128PlusPlus rng2 = new XoRoShiRo128PlusPlus(seed);
    for (int i = 0; i < 200; i++) {
      Assertions.assertEquals(rng1.nextLong() < 0, rng2.nextBoolean());
    }
  }

  /**
   * Check the float is the upper 24-bits from the int value multiplied by a constant.
   */
  @Test
  void testNextFloatIs24BitProduct() {
    final long seed = ThreadLocalRandom.current().nextLong();
    final XoRoShiRo128PlusPlus rng1 = new XoRoShiRo128PlusPlus(seed);
    final XoRoShiRo128PlusPlus rng2 = new XoRoShiRo128PlusPlus(seed);
    for (int i = 0; i < 200; i++) {
      Assertions.assertEquals((rng1.nextInt() >>> 8) * 0x1.0p-24f, rng2.nextFloat());
    }
  }

  /**
   * Check the double is the upper 53-bits from the long value multiplied by a constant.
   */
  @Test
  void testNextDoubleIs53BitProduct() {
    final long seed = ThreadLocalRandom.current().nextLong();
    final XoRoShiRo128PlusPlus rng1 = new XoRoShiRo128PlusPlus(seed);
    final XoRoShiRo128PlusPlus rng2 = new XoRoShiRo128PlusPlus(seed);
    for (int i = 0; i < 200; i++) {
      Assertions.assertEquals((rng1.nextLong() >>> 11) * 0x1.0p-53, rng2.nextDouble());
    }
  }

  // All basic RNG methods based on the monobit test.
  // A fixed seed is used to avoid flaky tests.

  @Test
  void testNextInt() {
    final long seed = 45649872123325L;
    final XoRoShiRo128PlusPlus rng = new XoRoShiRo128PlusPlus(seed);
    int bitCount = 0;
    final int n = 100;
    for (int i = 0; i < n; i++) {
      bitCount += Integer.bitCount(rng.nextInt());
    }
    final int numberOfBits = n * Integer.SIZE;
    assertMonobit(bitCount, numberOfBits);
  }

  @Test
  void testNextDouble() {
    final long seed = -4567987432145468744L;
    final XoRoShiRo128PlusPlus rng = new XoRoShiRo128PlusPlus(seed);
    int bitCount = 0;
    final int n = 100;
    for (int i = 0; i < n; i++) {
      bitCount += Long.bitCount((long) (rng.nextDouble() * (1L << 53)));
    }
    final int numberOfBits = n * 53;
    assertMonobit(bitCount, numberOfBits);
  }

  @Test
  void testNextBoolean() {
    final long seed = 45679872136479L;
    final XoRoShiRo128PlusPlus rng = new XoRoShiRo128PlusPlus(seed);
    int bitCount = 0;
    final int n = 1000;
    for (int i = 0; i < n; i++) {
      if (rng.nextBoolean()) {
        bitCount++;
      }
    }
    final int numberOfBits = n;
    assertMonobit(bitCount, numberOfBits);
  }

  @Test
  void testNextFloat() {
    final long seed = 4564342345446344544L;
    final XoRoShiRo128PlusPlus rng = new XoRoShiRo128PlusPlus(seed);
    int bitCount = 0;
    final int n = 100;
    for (int i = 0; i < n; i++) {
      bitCount += Integer.bitCount((int) (rng.nextFloat() * (1 << 24)));
    }
    final int numberOfBits = n * 24;
    assertMonobit(bitCount, numberOfBits);
  }

  @Test
  void testNextBytes() {
    final long seed = -789153135486941564L;
    final XoRoShiRo128PlusPlus rng = new XoRoShiRo128PlusPlus(seed);
    for (final int range : new int[] {16, 18}) {
      final byte[] bytes = new byte[range];
      int bitCount = 0;
      final int n = 100;
      for (int i = 0; i < n; i++) {
        rng.nextBytes(bytes);
        for (final byte b1 : bytes) {
          bitCount += Integer.bitCount(b1 & 0xff);
        }
      }
      final int numberOfBits = n * Byte.SIZE * range;
      assertMonobit(bitCount, numberOfBits);
    }
  }

  /**
   * Assert that the number of 1 bits is approximately 50%. This is based upon a fixed-step "random
   * walk" of +1/-1 from zero.
   *
   * <p>The test is equivalent to the NIST Monobit test with a fixed p-value of 0.01. The number of
   * bits is recommended to be above 100.</p>
   *
   * @see <A href="https://csrc.nist.gov/publications/detail/sp/800-22/rev-1a/final">Bassham, et al
   *      (2010) NIST SP 800-22: A Statistical Test Suite for Random and Pseudorandom Number
   *      Generators for Cryptographic Applications. Section 2.1.</a>
   *
   * @param bitCount The bit count.
   * @param numberOfBits Number of bits.
   */
  private static void assertMonobit(int bitCount, int numberOfBits) {
    // Convert the bit count into a number of +1/-1 steps.
    final double sum = 2.0 * bitCount - numberOfBits;
    // The reference distribution is Normal with a standard deviation of sqrt(n).
    // Check the absolute position is not too far from the mean of 0 with a fixed
    // p-value of 0.01 taken from a 2-tailed Normal distribution. Computation of
    // the p-value requires the complimentary error function.
    final double absSum = Math.abs(sum);
    final double max = Math.sqrt(numberOfBits) * 2.576;
    Assertions.assertTrue(absSum <= max, () -> "Walked too far astray: " + absSum + " > " + max
        + " (test will fail randomly about 1 in 100 times)");
  }

  // Range methods uniformity tested using Chi-squared

  @Test
  void testNextIntInRange() {
    final long seed = -89656413479899763L;
    final XoRoShiRo128PlusPlus rng = new XoRoShiRo128PlusPlus(seed);
    // A power of 2 and the worst case scenario for the rejection algorithm.
    // Rejection should occur almost 50% of the time so the test should hit all paths.
    assertNextIntInRange(rng, 16 * 16, 16);
    assertNextIntInRange(rng, 17 * 17, 17);
    assertNextIntInRange(rng, (1 << 30) + 16, 16);
  }

  /**
   * Assert the nextInt(int) method is uniform. The bins must exactly divide into the limit.
   *
   * @param rng the rng
   * @param limit the limit
   * @param bins the bins
   */
  private static void assertNextIntInRange(XoRoShiRo128PlusPlus rng, int limit, int bins) {
    Assertions.assertEquals(0, limit % bins, "Invalid test: limit/bins must be a whole number");

    final long[] observed = new long[bins];
    final int divisor = limit / bins;
    final int samples = 10000;
    for (int i = 0; i < 10000; i++) {
      observed[rng.nextInt(limit) / divisor]++;
    }
    final double[] expected = new double[bins];
    Arrays.fill(expected, (double) samples / bins);
    final ChiSquareTest test = new ChiSquareTest();
    final double pvalue = test.chiSquareTest(expected, observed);
    Assertions.assertFalse(pvalue < 0.01, "P-value = " + pvalue);
  }

  @Test
  void testNextLongInRange() {
    final long seed = 789542313489478946L;
    final XoRoShiRo128PlusPlus rng = new XoRoShiRo128PlusPlus(seed);
    // A power of 2 and the worst case scenario for the rejection algorithm.
    // Rejection should occur almost 50% of the time so the test should hit all paths.
    assertNextLongInRange(rng, 16 * 16, 16);
    assertNextLongInRange(rng, 17 * 17, 17);
    assertNextLongInRange(rng, (1L << 62) + 16, 16);
  }

  /**
   * Assert the nextLong(long) method is uniform. The bins must exactly divide into the limit.
   *
   * @param rng the rng
   * @param limit the limit
   * @param bins the bins
   */
  private static void assertNextLongInRange(XoRoShiRo128PlusPlus rng, long limit, int bins) {
    Assertions.assertEquals(0, limit % bins, "Invalid test: limit/bins must be a whole number");

    final long[] observed = new long[bins];
    final long divisor = limit / bins;
    final int samples = 10000;
    for (int i = 0; i < 10000; i++) {
      observed[(int) (rng.nextLong(limit) / divisor)]++;
    }
    final double[] expected = new double[bins];
    Arrays.fill(expected, (double) samples / bins);
    final ChiSquareTest test = new ChiSquareTest();
    final double pvalue = test.chiSquareTest(expected, observed);
    Assertions.assertFalse(pvalue < 0.01, "P-value = " + pvalue);
  }

  private static void fill(XoRoShiRo128PlusPlus rng, int[] sequence) {
    for (int i = 0; i < sequence.length; i++) {
      sequence[i] = rng.nextInt();
    }
  }

  @Test
  void testNextIntUsingZeroThrows() {
    final XoRoShiRo128PlusPlus rng = new XoRoShiRo128PlusPlus(0);
    Assertions.assertThrows(IllegalArgumentException.class, () -> rng.nextInt(0));
  }

  @Test
  void testNextLongUsingZeroThrows() {
    final XoRoShiRo128PlusPlus rng = new XoRoShiRo128PlusPlus(0);
    Assertions.assertThrows(IllegalArgumentException.class, () -> rng.nextLong(0));
  }

  @Test
  void testSaveAndRestoreState() {
    final long seed = ThreadLocalRandom.current().nextLong();
    final XoRoShiRo128PlusPlus rng = new XoRoShiRo128PlusPlus(seed);
    final RandomProviderState state = rng.saveState();
    final int[] seq1 = new int[10];
    fill(rng, seq1);
    rng.restoreState(state);
    final int[] seq2 = new int[seq1.length];
    fill(rng, seq2);
    Assertions.assertArrayEquals(seq1, seq2);
  }

  @Test
  void testRestoreUsingBadStateThrows() {
    final XoRoShiRo128PlusPlus rng = new XoRoShiRo128PlusPlus(0);
    final RandomProviderState state = null;
    Assertions.assertThrows(IllegalArgumentException.class, () -> rng.restoreState(state));
  }
}
