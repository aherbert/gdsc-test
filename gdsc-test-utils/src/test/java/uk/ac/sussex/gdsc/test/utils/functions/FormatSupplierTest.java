/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
 *
 * Contains utilities for use with test frameworks.
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

package uk.ac.sussex.gdsc.test.utils.functions;

import java.util.IllegalFormatConversionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("javadoc")
class FormatSupplierTest {
  @Test
  void testGetSupplierUsingArrayOfParameters() {
    final String message = FormatSupplier.getSupplier("%d %.1f", 1, 2.3).get();
    Assertions.assertEquals("1 2.3", message);
  }

  @Test
  void testGetSupplierUsingSingleObjectArray() {
    final Object[] args = new Object[] {1, 2.3};
    final String message = FormatSupplier.getSupplier("%d %.1f", args).get();
    Assertions.assertEquals("1 2.3", message);
  }

  @Test
  void testGetSupplierUsingSingleObjectArrayAndArrayOfParametersThrows() {
    final Object[] args = new Object[] {1, 2.3};
    Assertions.assertThrows(IllegalFormatConversionException.class, () -> {
      FormatSupplier.getSupplier("%d %f %d", args, 3).get();
    });
  }
}
