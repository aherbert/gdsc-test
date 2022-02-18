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

import java.util.concurrent.ThreadLocalRandom;
import org.apache.commons.rng.RestorableUniformRandomProvider;
import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("javadoc")
class XoRoShiRo128PlusPlusTest extends BaseLongUniformRandomProviderTest {
  @Override
  protected RestorableUniformRandomProvider createRng(long seed) {
    return new XoRoShiRo128PlusPlus(seed);
  }

  @Test
  void testNextLongReference() {
    // Tested with respect to commons-rng implementation.
    final long seed0 = ThreadLocalRandom.current().nextLong();
    final long seed1 = seed0 + 627384217384238449L;
    final UniformRandomProvider rng1 =
        RandomSource.XO_RO_SHI_RO_128_PP.create(new long[] {seed0, seed1});
    final XoRoShiRo128PlusPlus rng2 = new XoRoShiRo128PlusPlus(seed0, seed1);
    for (int i = 0; i < 1000; i++) {
      Assertions.assertEquals(rng1.nextLong(), rng2.nextLong());
    }
  }

  @Test
  void testZeroSeed() {
    final XoRoShiRo128PlusPlus rng1 = new XoRoShiRo128PlusPlus(0);
    final XoRoShiRo128PlusPlus rng2 = new XoRoShiRo128PlusPlus(0, 0);
    long zeroOutput = 0;
    for (int i = 0; i < 10; i++) {
      final long value = rng1.nextLong();
      Assertions.assertEquals(value, rng2.nextLong());
      zeroOutput |= value;
    }
    Assertions.assertNotEquals(0, zeroOutput, "Zero seed should not create all zero output");
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
}
