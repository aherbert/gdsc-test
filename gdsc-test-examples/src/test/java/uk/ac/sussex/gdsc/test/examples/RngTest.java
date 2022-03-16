/*-
 * #%L
 * Genome Damage and Stability Centre Test Examples
 *
 * Contains examples of the GDSC Test libraries.
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

package uk.ac.sussex.gdsc.test.examples;

import org.apache.commons.rng.UniformRandomProvider;
import uk.ac.sussex.gdsc.test.junit5.SeededTest;
import uk.ac.sussex.gdsc.test.rng.RngFactory;
import uk.ac.sussex.gdsc.test.utils.RandomSeed;

/**
 * Contains demonstration code for the RNG package.
 *
 * <p>Code snippets from this class are used in the documentation, thus this class exists to ensure
 * the snippets are valid. Documentation should be updated appropriately when this class is updated.
 */
class RngTest {

  /**
   * Test the seeded test annotation.
   *
   * @param seed the seed
   */
  @SeededTest
  void testSomethingRandom(RandomSeed seed) {
    long value = seed.getAsLong();
    // Do the test ...
  }

  /**
   * Test the seeded test annotation.
   *
   * @param seed the seed
   */
  @SeededTest
  void testSomethingRandom2(RandomSeed seed) {
    UniformRandomProvider rng = RngFactory.create(seed.get());
    // Do the test ...
  }
}
