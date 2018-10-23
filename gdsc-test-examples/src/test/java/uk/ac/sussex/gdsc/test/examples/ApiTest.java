/*-
 * #%L
 * Genome Damage and Stability Centre Test Examples
 * 
 * Contains examples of the GDSC Test libraries.
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

package uk.ac.sussex.gdsc.test.examples;

import uk.ac.sussex.gdsc.test.api.TestHelper;
import uk.ac.sussex.gdsc.test.api.function.DoubleDoubleBiPredicate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Contains demonstration code for the API.
 *
 * <p>Code snippets from this class are used in the documentation, thus this class exists to ensure
 * the snippets are valid. Documentation should be updated appropriately when this class is updated.
 */
public class ApiTest {

  /**
   * Test the relative equality predicate.
   */
  @Test
  public void testRelativeEqualityPredicate() {
    double relativeError = 1e-4;
    double absoluteError = 0;
    DoubleDoubleBiPredicate predicate = TestHelper.almostEqualDoubles(relativeError, absoluteError);

    predicate.test(99999.0, 100000.0); // true
    predicate.test(9999.0, 10000.0); // false

    Assertions.assertTrue(predicate.test(9999.0, 10000.0));
    Assertions.assertFalse(predicate.test(999.0, 1000.0));
  }
}
