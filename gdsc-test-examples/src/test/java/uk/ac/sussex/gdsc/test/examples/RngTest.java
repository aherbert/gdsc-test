/*-
 * #%L
 * Genome Damage and Stability Centre Test Examples
 *
 * Contains examples of the GDSC Test libraries.
 * %%
 * Copyright (C) 2018 - 2025 Alex Herbert
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
