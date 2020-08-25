/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
 *
 * Contains utilities for use with test frameworks.
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

package uk.ac.sussex.gdsc.test.utils.functions;

import java.util.IllegalFormatConversionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("javadoc")
class FunctionUtilsTest {
  @Test
  void testGetSupplierUsingArrayOfParameters() {
    final String message = FunctionUtils.getSupplier("%d %.1f", 1, 2.3).get();
    Assertions.assertEquals("1 2.3", message);
  }

  @Test
  void testGetSupplierUsingSingleObjectArray() {
    final Object[] args = new Object[] {1, 2.3};
    final String message = FunctionUtils.getSupplier("%d %.1f", args).get();
    Assertions.assertEquals("1 2.3", message);
  }

  @Test
  void testGetSupplierUsingSingleObjectArrayAndArrayOfParametersThrows() {
    final Object[] args = new Object[] {1, 2.3};
    Assertions.assertThrows(IllegalFormatConversionException.class, () -> {
      FunctionUtils.getSupplier("%d %f %d", args, 3).get();
    });
  }
}
