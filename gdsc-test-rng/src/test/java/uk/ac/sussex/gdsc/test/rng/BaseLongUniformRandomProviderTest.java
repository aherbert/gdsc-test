/*-
 * #%L
 * Genome Damage and Stability Centre Test RNG
 *
 * Contains utilities for use with Commons RNG for random tests.
 * %%
 * Copyright (C) 2018 - 2022 Alex Herbert
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package uk.ac.sussex.gdsc.test.rng;

import java.util.Arrays;
import java.util.stream.Stream;
import org.apache.commons.rng.RandomProviderState;
import org.apache.commons.rng.RestorableUniformRandomProvider;
import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.statistics.inference.ChiSquareTest;
import org.apache.commons.statistics.inference.SignificanceResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@TestInstance(Lifecycle.PER_CLASS)
@SuppressWarnings("javadoc")
abstract class BaseLongUniformRandomProviderTest {
  /**
   * Creates the rng to test.
   *
   * @param seed the seed
   * @return the uniform random provider
   */
  protected abstract RestorableUniformRandomProvider createRng(long seed);

  /**
   * Creates the rng to test using the default seeding.
   *
   * @return the uniform random provider
   */
  protected abstract RestorableUniformRandomProvider createRng();

  // Method to stream arguments to the parameterized tests.
  // A fixed seed is used to avoid flaky tests.
  // Can be overridden if the fixed seed fails.

  protected Stream<Arguments> testNextLong() {
    return Stream.of(Arguments.of(createRng(0)), Arguments.of(createRng(0xc46ffeffe1903eeaL)));
  }

  protected Stream<Arguments> testNextInt() {
    return Stream.of(Arguments.of(createRng(0xf8c5aaaf1eb43c7aL)));
  }

  protected Stream<Arguments> testNextDouble() {
    return Stream.of(Arguments.of(createRng(0x720995f7d30ee23eL)));
  }

  protected Stream<Arguments> testNextBoolean() {
    return Stream.of(Arguments.of(createRng(0xfae390d00da70326L)));
  }

  protected Stream<Arguments> testNextFloat() {
    return Stream.of(Arguments.of(createRng(0xe1c8d7fbd551c59aL)));
  }

  protected Stream<Arguments> testNextBytes() {
    return Stream.of(Arguments.of(createRng(0xb5970846df53ba38L)));
  }

  protected Stream<Arguments> testNextIntInRange() {
    // A power of 2 and the worst case scenario for the rejection algorithm.
    // Rejection should occur almost 50% of the time so the test should hit all paths.
    return Stream.of(Arguments.of(createRng(0xe1c8d7fbd551c59aL), 16 * 16, 16),
        Arguments.of(createRng(0x5fc8a8132a0a5c35L), 17 * 17, 17),
        Arguments.of(createRng(0xc3551694899ff615L), (1 << 30) + 16, 16));
  }

  protected Stream<Arguments> testNextLongInRange() {
    // A power of 2 and the worst case scenario for the rejection algorithm.
    // Rejection should occur almost 50% of the time so the test should hit all paths.
    return Stream.of(Arguments.of(createRng(0xcaf3aad35dbaf3f3L), 16 * 16, 16),
        Arguments.of(createRng(0xb4efb1858f509812L), 17 * 17, 17),
        Arguments.of(createRng(0x20bd4363056da00eL), (1L << 62) + 16, 16));
  }

  protected Stream<Arguments> testSaveAndRestoreState() {
    return Stream.of(Arguments.of(createRng(0x40c52c1bc1042d40L)));
  }

  protected Stream<Arguments> testRestoreUsingBadStateThrows() {
    return Stream.of(Arguments.of(createRng(0x6cc8c6fed300c8d6L)));
  }

  // All basic RNG methods based on the monobit test.

  @ParameterizedTest
  @MethodSource
  void testNextLong(UniformRandomProvider rng) {
    int bitCount = 0;
    final int n = 100;
    for (int i = 0; i < n; i++) {
      bitCount += Long.bitCount(rng.nextLong());
    }
    final int numberOfBits = n * Long.SIZE;
    assertMonobit(bitCount, numberOfBits);
  }

  @ParameterizedTest
  @MethodSource
  void testNextInt(UniformRandomProvider rng) {
    int bitCount = 0;
    final int n = 100;
    for (int i = 0; i < n; i++) {
      bitCount += Integer.bitCount(rng.nextInt());
    }
    final int numberOfBits = n * Integer.SIZE;
    assertMonobit(bitCount, numberOfBits);
  }

  @ParameterizedTest
  @MethodSource
  void testNextDouble(UniformRandomProvider rng) {
    int bitCount = 0;
    final int n = 100;
    for (int i = 0; i < n; i++) {
      bitCount += Long.bitCount((long) (rng.nextDouble() * (1L << 53)));
    }
    final int numberOfBits = n * 53;
    assertMonobit(bitCount, numberOfBits);
  }

  @ParameterizedTest
  @MethodSource
  void testNextBoolean(UniformRandomProvider rng) {
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

  @ParameterizedTest
  @MethodSource
  void testNextFloat(UniformRandomProvider rng) {
    int bitCount = 0;
    final int n = 100;
    for (int i = 0; i < n; i++) {
      bitCount += Integer.bitCount((int) (rng.nextFloat() * (1 << 24)));
    }
    final int numberOfBits = n * 24;
    assertMonobit(bitCount, numberOfBits);
  }

  @ParameterizedTest
  @MethodSource
  void testNextBytes(UniformRandomProvider rng) {
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
    final double max = Math.sqrt(numberOfBits) * 2.5758293035489004;
    Assertions.assertTrue(absSum <= max, () -> "Walked too far astray: " + absSum + " > " + max
        + " (test will fail randomly about 1 in 100 times)");
  }

  // Range methods uniformity tested using Chi-squared

  @ParameterizedTest
  @MethodSource
  void testNextIntInRange(UniformRandomProvider rng, int limit, int bins) {
    Assertions.assertEquals(0, limit % bins, "Invalid test: limit/bins must be a whole number");

    final long[] observed = new long[bins];
    final int divisor = limit / bins;
    final int samples = 10000;
    for (int i = 0; i < 10000; i++) {
      observed[rng.nextInt(limit) / divisor]++;
    }
    final double[] expected = new double[bins];
    Arrays.fill(expected, (double) samples / bins);
    final SignificanceResult r = ChiSquareTest.withDefaults().test(expected, observed);
    Assertions.assertFalse(r.reject(0.01), () -> "p-value = " + r.getPValue());
  }

  @ParameterizedTest
  @MethodSource
  void testNextLongInRange(UniformRandomProvider rng, long limit, int bins) {
    Assertions.assertEquals(0, limit % bins, "Invalid test: limit/bins must be a whole number");

    final long[] observed = new long[bins];
    final long divisor = limit / bins;
    final int samples = 10000;
    for (int i = 0; i < 10000; i++) {
      observed[(int) (rng.nextLong(limit) / divisor)]++;
    }
    final double[] expected = new double[bins];
    Arrays.fill(expected, (double) samples / bins);
    final SignificanceResult r = ChiSquareTest.withDefaults().test(expected, observed);
    Assertions.assertFalse(r.reject(0.01), () -> "p-value = " + r.getPValue());
  }

  @ParameterizedTest
  @MethodSource
  void testSaveAndRestoreState(RestorableUniformRandomProvider rng) {
    final RandomProviderState state = rng.saveState();
    final int[] seq1 = new int[10];
    fill(rng, seq1);
    rng.restoreState(state);
    final int[] seq2 = new int[seq1.length];
    fill(rng, seq2);
    Assertions.assertArrayEquals(seq1, seq2);
  }

  private static void fill(UniformRandomProvider rng, int[] sequence) {
    for (int i = 0; i < sequence.length; i++) {
      sequence[i] = rng.nextInt();
    }
  }

  @ParameterizedTest
  @MethodSource
  void testRestoreUsingBadStateThrows(RestorableUniformRandomProvider rng) {
    final RandomProviderState state = null;
    Assertions.assertThrows(IllegalArgumentException.class, () -> rng.restoreState(state));
  }

  @RepeatedTest(value = 3)
  void testNoArgumentConstructor() {
    final UniformRandomProvider rng1 = createRng();
    final UniformRandomProvider rng2 = createRng();
    final int n = 10;
    // Chance of matched output = 1 in (2^64)^n
    // n = 10: 1 in 4.56e192
    for (int i = n; i-- != 0;) {
      if (rng1.nextLong() != rng2.nextLong()) {
        return;
      }
    }
    Assertions.fail("2 instances output the same sequence");
  }
}
