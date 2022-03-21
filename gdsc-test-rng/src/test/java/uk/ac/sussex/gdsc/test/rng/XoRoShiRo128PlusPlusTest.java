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
