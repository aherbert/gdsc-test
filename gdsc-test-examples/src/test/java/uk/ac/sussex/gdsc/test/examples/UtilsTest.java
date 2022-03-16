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
