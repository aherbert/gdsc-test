/*-
 * #%L
 * Genome Damage and Stability Centre Test Examples
 *
 * Contains examples of the GDSC Test libraries.
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

package uk.ac.sussex.gdsc.test.examples;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.ac.sussex.gdsc.test.utils.RandomSeeds;
import uk.ac.sussex.gdsc.test.utils.TestSettings;
import uk.ac.sussex.gdsc.test.utils.functions.IndexSupplier;

/**
 * Contains demonstration code for the Utils package.
 *
 * <p>Code snippets from this class are used in the documentation, thus this class exists to ensure
 * the snippets are valid. Documentation should be updated appropriately when this class is updated.
 */
class UtilsTest {

  /**
   * Test the dynamic message.
   */
  @Test
  void testDynamicMessage() {
    int dimensions = 2;
    IndexSupplier message = new IndexSupplier(dimensions);
    message.setMessagePrefix("Index ");
    // The message will supply "Index [i][j]" for the assertion, e.g.
    message.set(0, 42);
    Assertions.assertEquals("Index [42][3]", message.set(1, 3).get());

    int size = 5;
    int[][] matrix = new int[size][size];
    for (int i = 0; i < size; i++) {
      message.set(0, i);
      for (int j = 0; j < size; j++) {
        // The message will supply "Index [i][j]" for the assertion
        Assertions.assertEquals(0, matrix[i][j], message.set(1, j));
      }
    }
  }

  @Test
  void testGetSeed() {
    byte[] seed = TestSettings.getSeed();
    long longSeed = RandomSeeds.makeLong(seed);
  }
}
