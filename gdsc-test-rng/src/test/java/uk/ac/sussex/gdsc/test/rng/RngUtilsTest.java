/*-
 * #%L
 * Genome Damage and Stability Centre Test RNG
 *
 * Contains utilities for use with Commons RNG for random tests.
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

package uk.ac.sussex.gdsc.test.rng;

import uk.ac.sussex.gdsc.test.utils.TestSettings;

import org.apache.commons.rng.UniformRandomProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

@SuppressWarnings("javadoc")
public class RngUtilsTest {

  /** The long seed. */
  private static final long LONG_SEED = 5656787697789L;

  /** The long seed. */
  private static final byte[] BYTE_SEED = {1, 2, 3, 4, 5, 6, 7, 8};

  @Test
  public void canGetSameRandomWithSameSeed() {
    UniformRandomProvider rng = RngUtils.create(LONG_SEED);
    final double[] e = {rng.nextDouble(), rng.nextDouble()};
    rng = RngUtils.create(LONG_SEED);
    final double[] o = {rng.nextDouble(), rng.nextDouble()};
    Assertions.assertArrayEquals(e, o);
  }

  @Test
  public void canGetDifferentRandomWithDifferentSeed() {
    UniformRandomProvider rng = RngUtils.create(LONG_SEED);
    final double[] e = {rng.nextDouble(), rng.nextDouble()};
    rng = RngUtils.create(LONG_SEED * 2);
    final double[] o = {rng.nextDouble(), rng.nextDouble()};
    Assertions.assertThrows(AssertionFailedError.class, () -> {
      Assertions.assertArrayEquals(e, o);
    });
  }

  @Test
  public void canGetSameRandomWithZeroSeed() {
    // Test zero is allowed as a random seed.
    UniformRandomProvider rng = RngUtils.create(0);
    final double[] e = {rng.nextDouble(), rng.nextDouble()};
    rng = RngUtils.create(0);
    final double[] o = {rng.nextDouble(), rng.nextDouble()};
    Assertions.assertArrayEquals(e, o);
  }

  @Test
  public void canGetSameRandomWithSameSeedAndNoCache() {
    final long seed = LONG_SEED + 1;
    UniformRandomProvider rng = RngUtils.create(seed, false);
    final double[] e = {rng.nextDouble(), rng.nextDouble()};
    rng = RngUtils.create(seed, false);
    final double[] o = {rng.nextDouble(), rng.nextDouble()};
    Assertions.assertArrayEquals(e, o);

    // Check verses cached version
    rng = RngUtils.create(seed);
    final double[] o2 = {rng.nextDouble(), rng.nextDouble()};
    Assertions.assertArrayEquals(e, o2);
  }

  @Test
  public void canGetSameRandomWithSameByteSeed() {
    UniformRandomProvider rng = RngUtils.create(BYTE_SEED);
    final double[] e = {rng.nextDouble(), rng.nextDouble()};
    rng = RngUtils.create(BYTE_SEED);
    final double[] o = {rng.nextDouble(), rng.nextDouble()};
    Assertions.assertArrayEquals(e, o);
  }

  @Test
  public void canGetDifferentRandomWithDifferentByteSeed() {
    UniformRandomProvider rng = RngUtils.create(BYTE_SEED);
    final double[] e = {rng.nextDouble(), rng.nextDouble()};
    final byte[] seed = BYTE_SEED.clone();
    seed[0]++;
    rng = RngUtils.create(seed);
    final double[] o = {rng.nextDouble(), rng.nextDouble()};
    Assertions.assertThrows(AssertionFailedError.class, () -> {
      Assertions.assertArrayEquals(e, o);
    });
  }

  @Test
  public void canGetDifferentRandomWithNullByteSeed() {
    // The use of a null byte[] seed will create randomly seeded RNG
    UniformRandomProvider rng = RngUtils.create(null);
    final double[] e = {rng.nextDouble(), rng.nextDouble()};
    rng = RngUtils.create(null);
    final double[] o = {rng.nextDouble(), rng.nextDouble()};
    Assertions.assertThrows(AssertionFailedError.class, () -> {
      Assertions.assertArrayEquals(e, o);
    });
  }

  @Test
  public void canGetDifferentRandomWithZeroLengthByteSeed() {
    // The use of a null byte[] seed will create randomly seeded RNG
    final byte[] seed = new byte[0];
    UniformRandomProvider rng = RngUtils.create(seed);
    final double[] e = {rng.nextDouble(), rng.nextDouble()};
    rng = RngUtils.create(seed);
    final double[] o = {rng.nextDouble(), rng.nextDouble()};
    Assertions.assertThrows(AssertionFailedError.class, () -> {
      Assertions.assertArrayEquals(e, o);
    });
  }

  @Test
  public void canGetSameRandomWithSameByteSeedAndNoCache() {
    final byte[] seed = BYTE_SEED.clone();
    seed[0]++;
    UniformRandomProvider rng = RngUtils.create(seed, false);
    final double[] e = {rng.nextDouble(), rng.nextDouble()};
    rng = RngUtils.create(seed, false);
    final double[] o = {rng.nextDouble(), rng.nextDouble()};
    Assertions.assertArrayEquals(e, o);

    // Check verses cached version
    rng = RngUtils.create(seed);
    final double[] o2 = {rng.nextDouble(), rng.nextDouble()};
    Assertions.assertArrayEquals(e, o2);
  }

  @Test
  public void canGetSameRandomWithFixedSeedMatchingConfiguredSeed() {
    final byte[] seed = TestSettings.getSeed();
    UniformRandomProvider rng = RngUtils.createWithFixedSeed();
    final double[] e = {rng.nextDouble(), rng.nextDouble()};
    rng = RngUtils.create(seed, true);
    final double[] o = {rng.nextDouble(), rng.nextDouble()};
    Assertions.assertArrayEquals(e, o);
  }
}
