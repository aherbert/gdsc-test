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

import java.util.SplittableRandom;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.commons.rng.UniformRandomProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.opentest4j.AssertionFailedError;
import uk.ac.sussex.gdsc.test.utils.TestSettings;

@SuppressWarnings("javadoc")
class RngFactoryTest {

  /** The long seed. */
  private static final long LONG_SEED = 5656787697789L;

  @Test
  void canGetSameRandomWithSameSeed() {
    UniformRandomProvider rng = RngFactory.create(LONG_SEED);
    final long[] e = {rng.nextLong(), rng.nextLong()};
    rng = RngFactory.create(LONG_SEED);
    final long[] o = {rng.nextLong(), rng.nextLong()};
    Assertions.assertArrayEquals(e, o);
  }

  @Test
  void canGetDifferentRandomWithDifferentSeed() {
    UniformRandomProvider rng = RngFactory.create(LONG_SEED);
    final long[] e = {rng.nextLong(), rng.nextLong()};
    rng = RngFactory.create(LONG_SEED * 2);
    final long[] o = {rng.nextLong(), rng.nextLong()};
    Assertions.assertThrows(AssertionFailedError.class, () -> {
      Assertions.assertArrayEquals(e, o);
    });
  }

  @Test
  void canGetSameRandomWithZeroSeed() {
    // Test zero is allowed as a random seed.
    UniformRandomProvider rng = RngFactory.create(0);
    final long[] e = {rng.nextLong(), rng.nextLong()};
    rng = RngFactory.create(0);
    final long[] o = {rng.nextLong(), rng.nextLong()};
    Assertions.assertArrayEquals(e, o);
  }

  @ParameterizedTest
  @ValueSource(ints = {16, 32})
  void canGetSameRandomWithSameByteSeed(int n) {
    final byte[] bytes = new byte[n];
    ThreadLocalRandom.current().nextBytes(bytes);
    UniformRandomProvider rng = RngFactory.create(bytes);
    final long[] e = {rng.nextLong(), rng.nextLong()};
    rng = RngFactory.create(bytes);
    final long[] o = {rng.nextLong(), rng.nextLong()};
    Assertions.assertArrayEquals(e, o);
  }

  @ParameterizedTest
  @ValueSource(ints = {16, 32})
  void canGetDifferentRandomWithDifferentByteSeed(int n) {
    final byte[] bytes = new byte[n];
    ThreadLocalRandom.current().nextBytes(bytes);
    UniformRandomProvider rng = RngFactory.create(bytes);
    final long[] e = {rng.nextLong(), rng.nextLong()};
    ThreadLocalRandom.current().nextBytes(bytes);
    rng = RngFactory.create(bytes);
    final long[] o = {rng.nextLong(), rng.nextLong()};
    Assertions.assertThrows(AssertionFailedError.class, () -> {
      Assertions.assertArrayEquals(e, o);
    });
  }

  @Test
  void canGetDifferentRandomWithNullByteSeed() {
    // The use of a null byte[] seed will create randomly seeded RNG
    UniformRandomProvider rng = RngFactory.create(null);
    final long[] e = {rng.nextLong(), rng.nextLong()};
    rng = RngFactory.create(null);
    final long[] o = {rng.nextLong(), rng.nextLong()};
    Assertions.assertThrows(AssertionFailedError.class, () -> {
      Assertions.assertArrayEquals(e, o);
    });
  }

  @Test
  void canGetDifferentRandomWithZeroLengthByteSeed() {
    // The use of a zero length byte[] seed will create randomly seeded RNG
    final byte[] seed = new byte[0];
    UniformRandomProvider rng = RngFactory.create(seed);
    final long[] e = {rng.nextLong(), rng.nextLong()};
    rng = RngFactory.create(seed);
    final long[] o = {rng.nextLong(), rng.nextLong()};
    Assertions.assertThrows(AssertionFailedError.class, () -> {
      Assertions.assertArrayEquals(e, o);
    });
  }

  @Test
  void canGetSameRandomWithFixedSeedMatchingConfiguredSeed() {
    final byte[] seed = TestSettings.getSeed();
    UniformRandomProvider rng = RngFactory.createWithFixedSeed();
    final long[] e = {rng.nextLong(), rng.nextLong()};
    rng = RngFactory.create(seed);
    final long[] o = {rng.nextLong(), rng.nextLong()};
    Assertions.assertArrayEquals(e, o);
  }

  @Test
  void testRxsmxsOutput() {
    // Code generated using the reference c code obtained from:
    // https://mostlymangling.blogspot.com/2018/07/on-mixing-functions-in-fast-splittable.html
    final long[] values =
        {0x4909bf4228b09f5dL, 0x62f1175aa2ac2becL, 0x0fff75f3a0f6eaa1L, 0x440055bc9b89eaf0L,
            0x8d3954796546094bL, 0x541dc47bccef0e39L, 0xbcf2cf7ed5e3db25L, 0x3902cbf791fbac96L,
            0x144101ff31d0bccdL, 0x5da6aec2faa5adceL, 0xbfff757b69be2784L, 0x6b3e67846edd0fd5L,
            0x68192de4e987bdc4L, 0xb3fe34cee77a79a8L, 0xdea80e3b85df836dL, 0xd6276bdcf3d6d342L,
            0x0b6cba29f4d2ad13L, 0xb6bfe1d2d013aa12L, 0xf08e347e079ba78cL, 0x0150ac7471e862fcL,
            0x16457cc24205be12L, 0x1b3c87d7fc416c26L, 0xa7d1a4e56bbf95a9L, 0x44d605b26c0bbdaaL,
            0xfe6167e96c66a310L, 0x09ecd862a1ef544bL, 0x8d83e2956e0da35dL, 0x268734a1a4d94cfdL,
            0x104b3ca0ce4b772bL, 0x488689436f671ba8L, 0x3ae5b9b467cc827aL, 0xac0e0ba50f83e589L,
            0xfd8d4041fb3350c6L, 0x2aebfa9bf4afe825L, 0xf7f840be98599b68L, 0x2c65c37d25d3f0ccL,
            0xbf49fcbcef6acb81L, 0x0552891572d190a4L, 0x8292c9877a7a73dbL, 0x6377a3a02614593eL,};
    long state = 0x012de1babb3c4104L;
    final long increment = 0xc8161b4202294965L;
    for (int i = 0; i < values.length; i++) {
      Assertions.assertEquals(values[i], RngFactory.rrmxmx(state += increment));
    }
  }

  @Test
  void testStafford13Output() {
    long state = 0x012de1babb3c4104L;
    final long increment = RngFactory.GOLDEN_RATIO;
    SplittableRandom rng = new SplittableRandom(state);
    for (int i = 0; i < 50; i++) {
      Assertions.assertEquals(rng.nextLong(), RngFactory.stafford13(state += increment));
    }
  }
}
