package uk.ac.sussex.gdsc.test.rng;

import org.apache.commons.math3.stat.inference.ChiSquareTest;
import org.apache.commons.rng.RandomProviderState;
import org.apache.commons.rng.core.util.NumberFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("javadoc")
public class PcgXshRs32Test {
  @Test
  public void testNextIntXshRr() {
    /*
     * Tested with respect to pcg_engines::setseq_xsh_rs_64_32 from the C++ implementation. See :
     * http://www.pcg-random.org/download.html#cpp-implementation
     */
    final int[] expectedSequence = {0xba4138b8, 0xd329a393, 0x75d68d3f, 0xbb7572ca, 0x7a48d2f2,
        0xcb3c1e37, 0xc1374a97, 0x7c2c5bfa, 0x8a1c8695, 0x30db4fea, 0x95f9a901, 0x72ebfa48,
        0x6a284dbf, 0x0ef11286, 0x37330e11, 0xfeb53893, 0x77e3adda, 0x64dc86bd, 0xc8d762d7,
        0xbf3fb80c, 0x732dfd12, 0x6088e86d, 0xbc4e79e5, 0x56ece5b1, 0xe706ac72, 0xee798018,
        0xef73de74, 0x3de1f966, 0x7a36db53, 0x1e921eb2, 0x55e35484, 0x2577c6f2, 0x0a006e21,
        0x8cb811b7, 0x5f26c916, 0x3990837f, 0x15f2983d, 0x546ccb4a, 0x4eda8716, 0xb8666a25,};
    final PcgXshRs32 rng = new PcgXshRs32(0x012de1babb3c4104L, 0xc8161b4202294965L);
    assertNextInt(expectedSequence, rng);
  }

  @Test
  public void testNextIntXshRrDefaultInc() {
    /*
     * Tested with respect to pcg_engines::setseq_xsh_rs_64_32 from the C++ implementation. See :
     * http://www.pcg-random.org/download.html#cpp-implementation
     */
    final int[] expectedSequence = {0x5ab2ddd9, 0x215c476c, 0x83c34b11, 0xe2c5e213, 0x37979624,
        0x303cf5b5, 0xbf2a146e, 0xb0692351, 0x49b00de3, 0xd9ded67c, 0x298e2bb9, 0xa20d2287,
        0xa067cd33, 0x5c10d395, 0x1f8d8bd5, 0x4306b6bc, 0x97a3e50b, 0x992e0604, 0x8a982b33,
        0x4baa6604, 0xefd995eb, 0x0f341c29, 0x080bce32, 0xb22b3de2, 0x5fbf47ff, 0x7fc928bf,
        0x075a5871, 0x174a0c48, 0x72458b67, 0xa869a8c1, 0x64857577, 0xed28377c, 0x3ce86b48,
        0xa855af8b, 0x6a051d88, 0x23b06c33, 0xb3e4afc1, 0xa848c3e4, 0x79f969a6, 0x670e2acb,};
    final PcgXshRs32 rng = new PcgXshRs32(0x012de1babb3c4104L);
    assertNextInt(expectedSequence, rng);
  }

  /**
   * Check the expected sequence is output by the generator.
   *
   * @param expectedSequence the expected sequence
   * @param rng the generator
   */
  private static void assertNextInt(int[] expectedSequence, PcgXshRs32 rng) {
    for (int i = 0; i < expectedSequence.length; i++) {
      Assertions.assertEquals(expectedSequence[i], rng.nextInt());
    }
  }

  /**
   * Check the 64-bit long is two concatenated 32-bit int values.
   */
  @Test
  public void testNextLong() {
    final long seed = ThreadLocalRandom.current().nextLong();
    final PcgXshRs32 rng1 = new PcgXshRs32(seed);
    final PcgXshRs32 rng2 = new PcgXshRs32(seed);
    for (int i = 0; i < 200; i++) {
      final long expected = NumberFactory.makeLong(rng1.nextInt(), rng1.nextInt());
      Assertions.assertEquals(expected, rng2.nextLong());
    }
  }

  /**
   * Check the boolean is a sign test on the int value.
   */
  @Test
  public void testNextBooleanIsSignTest() {
    final long seed = ThreadLocalRandom.current().nextLong();
    final PcgXshRs32 rng1 = new PcgXshRs32(seed);
    final PcgXshRs32 rng2 = new PcgXshRs32(seed);
    for (int i = 0; i < 200; i++) {
      Assertions.assertEquals(rng1.nextInt() < 0, rng2.nextBoolean());
    }
  }

  /**
   * Check the float is the upper 24-bits from the int value multiplied by a constant.
   */
  @Test
  public void testNextFloatIs24BitProduct() {
    final long seed = ThreadLocalRandom.current().nextLong();
    final PcgXshRs32 rng1 = new PcgXshRs32(seed);
    final PcgXshRs32 rng2 = new PcgXshRs32(seed);
    for (int i = 0; i < 200; i++) {
      Assertions.assertEquals((rng1.nextInt() >>> 8) * 0x1.0p-24f, rng2.nextFloat());
    }
  }

  /**
   * Check the double is the upper 53-bits from the long value multiplied by a constant.
   */
  @Test
  public void testNextDoubleIs53BitProduct() {
    final long seed = 67868L; // ThreadLocalRandom.current().nextLong();
    final PcgXshRs32 rng1 = new PcgXshRs32(seed);
    final PcgXshRs32 rng2 = new PcgXshRs32(seed);
    for (int i = 0; i < 200; i++) {
      Assertions.assertEquals((rng1.nextLong() >>> 11) * 0x1.0p-53, rng2.nextDouble());
    }
  }

  // All basic RNG methods based on the monobit test.
  // A fixed seed is used to avoid flaky tests.

  @Test
  public void testNextInt() {
    final long seed = 45649872123325L;
    final PcgXshRs32 rng = new PcgXshRs32(seed);
    int bitCount = 0;
    final int n = 100;
    for (int i = 0; i < n; i++) {
      bitCount += Integer.bitCount(rng.nextInt());
    }
    final int numberOfBits = n * Integer.SIZE;
    assertMonobit(bitCount, numberOfBits);
  }

  @Test
  public void testNextDouble() {
    final long seed = -4567987432145468744L;
    final PcgXshRs32 rng = new PcgXshRs32(seed);
    int bitCount = 0;
    final int n = 100;
    for (int i = 0; i < n; i++) {
      bitCount += Long.bitCount((long) (rng.nextDouble() * (1L << 53)));
    }
    final int numberOfBits = n * 53;
    assertMonobit(bitCount, numberOfBits);
  }

  @Test
  public void testNextBoolean() {
    final long seed = 45679872136479L;
    final PcgXshRs32 rng = new PcgXshRs32(seed);
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
  public void testNextFloat() {
    final long seed = 4564342345446344544L;
    final PcgXshRs32 rng = new PcgXshRs32(seed);
    int bitCount = 0;
    final int n = 100;
    for (int i = 0; i < n; i++) {
      bitCount += Integer.bitCount((int) (rng.nextFloat() * (1 << 24)));
    }
    final int numberOfBits = n * 24;
    assertMonobit(bitCount, numberOfBits);
  }

  @Test
  public void testNextBytes() {
    final long seed = -789153135486941564L;
    final PcgXshRs32 rng = new PcgXshRs32(seed);
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
  public void testNextIntInRange() {
    final long seed = -89656413479899763L;
    final PcgXshRs32 rng = new PcgXshRs32(seed);
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
  private static void assertNextIntInRange(PcgXshRs32 rng, int limit, int bins) {
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
  public void testNextLongInRange() {
    final long seed = 789542313489478946L;
    final PcgXshRs32 rng = new PcgXshRs32(seed);
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
  private static void assertNextLongInRange(PcgXshRs32 rng, long limit, int bins) {
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

  private static void fill(PcgXshRs32 rng, int[] sequence) {
    for (int i = 0; i < sequence.length; i++) {
      sequence[i] = rng.nextInt();
    }
  }

  @Test
  public void testNextIntUsingZeroThrows() {
    final PcgXshRs32 rng = new PcgXshRs32(0);
    Assertions.assertThrows(IllegalArgumentException.class, () -> rng.nextInt(0));
  }

  @Test
  public void testNextLongUsingZeroThrows() {
    final PcgXshRs32 rng = new PcgXshRs32(0);
    Assertions.assertThrows(IllegalArgumentException.class, () -> rng.nextLong(0));
  }

  @Test
  public void testSaveAndRestoreState() {
    final long seed = ThreadLocalRandom.current().nextLong();
    final PcgXshRs32 rng = new PcgXshRs32(seed);
    final RandomProviderState state = rng.saveState();
    final int[] seq1 = new int[10];
    fill(rng, seq1);
    rng.restoreState(state);
    final int[] seq2 = new int[seq1.length];
    fill(rng, seq2);
    Assertions.assertArrayEquals(seq1, seq2);
  }

  @Test
  public void testRestoreUsingBadStateThrows() {
    final PcgXshRs32 rng = new PcgXshRs32(0);
    final RandomProviderState state = null;
    Assertions.assertThrows(IllegalArgumentException.class, () -> rng.restoreState(state));
  }
}
