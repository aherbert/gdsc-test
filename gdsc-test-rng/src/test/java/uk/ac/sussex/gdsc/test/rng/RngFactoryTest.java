/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
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

import org.apache.commons.rng.UniformRandomProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import uk.ac.sussex.gdsc.test.rng.RngFactory;
import uk.ac.sussex.gdsc.test.utils.TestSettings;

@SuppressWarnings("javadoc")
public class RngFactoryTest {

  /** The seed. */
  private final long SEED = 5656787697789L;

  @Test
  public void canGetSameRandomWithSameSeed() {
    UniformRandomProvider r = RngFactory.create(SEED);
    final double[] e = {r.nextDouble(), r.nextDouble()};
    r = RngFactory.create(SEED);
    final double[] o = {r.nextDouble(), r.nextDouble()};
    Assertions.assertArrayEquals(e, o);
  }

  @Test
  public void canGetDifferentRandomWithDifferentSeed() {
    UniformRandomProvider r = RngFactory.create(SEED);
    final double[] e = {r.nextDouble(), r.nextDouble()};
    r = RngFactory.create(SEED * 2);
    final double[] o = {r.nextDouble(), r.nextDouble()};
    Assertions.assertThrows(AssertionFailedError.class, () -> {
      Assertions.assertArrayEquals(e, o);
    });
  }

  @Test
  public void canGetSameRandomWithFixedSeedMatchingConfiguredSeed() {
    final long seed = TestSettings.getSeed();
    UniformRandomProvider r = RngFactory.createWithFixedSeed();
    final double[] e = {r.nextDouble(), r.nextDouble()};
    r = RngFactory.create(seed);
    final double[] o = {r.nextDouble(), r.nextDouble()};
    Assertions.assertArrayEquals(e, o);
  }

  @Test
  public void canGetDifferentRandomWithZeroSeed() {
    UniformRandomProvider r = RngFactory.create(0);
    final double[] e = {r.nextDouble(), r.nextDouble()};
    r = RngFactory.create(0);
    final double[] o = {r.nextDouble(), r.nextDouble()};
    Assertions.assertThrows(AssertionFailedError.class, () -> {
      Assertions.assertArrayEquals(e, o);
    });
  }

  @Test
  public void canGetSameRandomWithSameSeedAndNoCache() {
    final long seed = this.SEED + 1;
    UniformRandomProvider r = RngFactory.create(seed, false);
    final double[] e = {r.nextDouble(), r.nextDouble()};
    r = RngFactory.create(seed, false);
    final double[] o = {r.nextDouble(), r.nextDouble()};
    Assertions.assertArrayEquals(e, o);

    // Check verses cached version
    r = RngFactory.create(seed);
    final double[] o2 = {r.nextDouble(), r.nextDouble()};
    Assertions.assertArrayEquals(e, o2);
  }
}
