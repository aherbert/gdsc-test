package uk.ac.sussex.gdsc.test.rng;

import org.apache.commons.rng.RandomProviderState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("javadoc")
public class PcgXshRr32Test {
  @Test
  public void testNextIntXshRr() {
    /*
     * Tested with respect to pcg_engines::mcg_xsh_rr_64_32 of the C++ implementation. See :
     * http://www.pcg-random.org/download.html#cpp-implementation
     */
    final int[] expectedSequence = {0xe860dd24, 0x15d339c0, 0xd9f75c46, 0x00efabb7, 0xa625e97f,
        0xcdeae599, 0x6304e667, 0xbc81be11, 0x2b8ea285, 0x8e186699, 0xac552be9, 0xd1ae72e5,
        0x5b953ad4, 0xa061dc1b, 0x526006e7, 0xf5a6c623, 0xfcefea93, 0x3a1964d2, 0xd6f03237,
        0xf3e493f7, 0x0c733750, 0x34a73582, 0xc4f8807b, 0x92b741ca, 0x0d38bf9c, 0xc39ee6ad,
        0xdc24857b, 0x7ba8f7d8, 0x377a2618, 0x92d83d3f, 0xd22a957a, 0xb6724af4, 0xe116141a,
        0xf465fe45, 0xa95f35bb, 0xf0398d4d, 0xe880af3e, 0xc2951dfd, 0x984ec575, 0x8679addb,};
    final PcgXshRr32 rng = new PcgXshRr32(0x012de1babb3c4104L, 0xc8161b4202294965L);
    assertNextInt(expectedSequence, rng);
  }

  @Test
  public void testNextIntXshRrDefaultInc() {
    /*
     * Tested with respect to pcg_engines::mcg_xsh_rr_64_32 of the C++ implementation. See :
     * http://www.pcg-random.org/download.html#cpp-implementation
     */
    final int[] expectedSequence = {0x0d2d5291, 0x45df90aa, 0xc60f3fb7, 0x06694f16, 0x29563e6f,
        0x42f46063, 0xf2be5583, 0x30360e91, 0x36385531, 0xddd36cd9, 0x5f4a6535, 0x644d10c0,
        0xaca075d7, 0x33781706, 0x4e1f9f34, 0x0676e286, 0xaca5eeb2, 0x7315cc93, 0xa6dfefe2,
        0xd480e065, 0xda9da26f, 0xda0f27b7, 0x045c0844, 0x22acfa0f, 0xcd7ecd75, 0xb97fd692,
        0xac96dd03, 0xf59c7174, 0x488947fe, 0x64a3d543, 0x90963884, 0x4adee0bb, 0x993cf7c0,
        0x8545b3f2, 0x409b542d, 0x6bf0a247, 0xfd59f9b4, 0x8f50b06e, 0x1bbcf6f5, 0xe1fdd29c,};
    final PcgXshRr32 rng = new PcgXshRr32(0x012de1babb3c4104L);
    assertNextInt(expectedSequence, rng);
  }

  /**
   * Check the expected sequence is output by the generator.
   *
   * @param expectedSequence the expected sequence
   * @param rng the generator
   */
  private static void assertNextInt(int[] expectedSequence, PcgXshRr32 rng) {
    for (int i = 0; i < expectedSequence.length; i++) {
      Assertions.assertEquals(expectedSequence[i], rng.nextInt());
    }
  }

  // All basic RNG methods based on the monobit test.
  // A fixed seed is used to avoid flaky tests.

  @Test
  public void testNextInt() {
    final long seed = 237849123L;
    final PcgXshRr32 rng = new PcgXshRr32(seed);
    int bitCount = 0;
    final int n = 100;
    for (int i = 0; i < n; i++) {
      bitCount += Integer.bitCount(rng.nextInt());
    }
    final int numberOfBits = n * Integer.SIZE;
    assertMonobit(bitCount, numberOfBits);
  }

  @Test
  public void testNextLong() {
    final long seed = -623427342092394L;
    final PcgXshRr32 rng = new PcgXshRr32(seed);
    int bitCount = 0;
    final int n = 100;
    for (int i = 0; i < n; i++) {
      bitCount += Long.bitCount(rng.nextLong());
    }
    final int numberOfBits = n * Long.SIZE;
    assertMonobit(bitCount, numberOfBits);
  }

  @Test
  public void testNextDouble() {
    final long seed = -4567987432145468744L;
    final PcgXshRr32 rng = new PcgXshRr32(seed);
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
    final PcgXshRr32 rng = new PcgXshRr32(seed);
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
    final PcgXshRr32 rng = new PcgXshRr32(seed);
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
    final PcgXshRr32 rng = new PcgXshRr32(seed);
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
   * <p>The test is equivalent to the NIST Monobit test with a fixed p-value of 0.01. The number
   * of bits is recommended to be above 100.</p>
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

  // No statistical tests for range methods

  @Test
  public void testNextIntInRange() {
    final long seed = ThreadLocalRandom.current().nextLong();
    final PcgXshRr32 rng = new PcgXshRr32(seed);
    // A power of 2 and the worst case scenario for the rejection algorithm.
    // Rejection should occur almost 50% of the time so the test should hit all paths.
    for (final int range : new int[] {256, (1 << 30) + 1}) {
      for (int i = 0; i < 10; i++) {
        final int value = rng.nextInt(range);
        Assertions.assertTrue(value >= 0 && value < range);
      }
    }
  }

  @Test
  public void testNextLongInRange() {
    final long seed = ThreadLocalRandom.current().nextLong();
    final PcgXshRr32 rng = new PcgXshRr32(seed);
    // A power of 2 and the worst case scenario for the rejection algorithm.
    // Rejection should occur almost 50% of the time so the test should hit all paths.
    for (final long range : new long[] {256, (1L << 62) + 1}) {
      for (int i = 0; i < 10; i++) {
        final long value = rng.nextLong(range);
        Assertions.assertTrue(value >= 0 && value < range);
      }
    }
  }

  private static void fill(PcgXshRr32 rng, int[] sequence) {
    for (int i = 0; i < sequence.length; i++) {
      sequence[i] = rng.nextInt();
    }
  }

  @Test
  public void testNextIntUsingZeroThrows() {
    final PcgXshRr32 rng = new PcgXshRr32(0);
    Assertions.assertThrows(IllegalArgumentException.class, () -> rng.nextInt(0));
  }

  @Test
  public void testNextLongUsingZeroThrows() {
    final PcgXshRr32 rng = new PcgXshRr32(0);
    Assertions.assertThrows(IllegalArgumentException.class, () -> rng.nextLong(0));
  }

  @Test
  public void testSaveAndRestoreState() {
    final long seed = ThreadLocalRandom.current().nextLong();
    final PcgXshRr32 rng = new PcgXshRr32(seed);
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
    final PcgXshRr32 rng = new PcgXshRr32(0);
    final RandomProviderState state = null;
    Assertions.assertThrows(IllegalArgumentException.class, () -> rng.restoreState(state));
  }
}
