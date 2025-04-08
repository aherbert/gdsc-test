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

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("javadoc")
class ObjectArrayFormatSupplierTest {

  private final String nullString = null;

  @Test
  void testConstructer() {
    final String format = "not-empty";
    final int size = 1;

    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      @SuppressWarnings("unused")
      final ObjectArrayFormatSupplier s = new ObjectArrayFormatSupplier(null, size);
    });
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      @SuppressWarnings("unused")
      final ObjectArrayFormatSupplier s = new ObjectArrayFormatSupplier("", size);
    });

    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      @SuppressWarnings("unused")
      final ObjectArrayFormatSupplier s = new ObjectArrayFormatSupplier(format, 0);
    });
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      @SuppressWarnings("unused")
      final ObjectArrayFormatSupplier s = new ObjectArrayFormatSupplier(format, -1);
    });

    final ObjectArrayFormatSupplier s = new ObjectArrayFormatSupplier(format, size);
    Assertions.assertNotNull(s);
    Assertions.assertEquals(size, s.getSize());
  }

  @Test
  void test1DMessage() {
    final String format = "[%s]";
    final int size = 1;

    final ObjectArrayFormatSupplier s = new ObjectArrayFormatSupplier(format, size);
    Assertions.assertEquals(String.format(format, nullString), s.get());
    final UniformRandomProvider rng = RandomSource.SPLIT_MIX_64.create();

    for (int i = 0; i < 5; i++) {
      final String next = next(rng);
      s.set(0, next);
      Assertions.assertEquals(next, s.get(0));
      Assertions.assertEquals(String.format(format, next), s.get());
    }

    Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
      s.set(1, 0);
    });
  }

  @Test
  void test2DMessage() {
    final String format = "[%s][%s]";
    final int size = 2;
    final ObjectArrayFormatSupplier s = new ObjectArrayFormatSupplier(format, size);
    Assertions.assertEquals(String.format(format, nullString, nullString), s.get());
    final UniformRandomProvider rng = RandomSource.SPLIT_MIX_64.create();
    for (int i = 0; i < 3; i++) {
      final String nexti = next(rng);
      s.set(0, nexti);
      Assertions.assertEquals(nexti, s.get(0));
      for (int j = 0; j < 3; j++) {
        final String nextj = next(rng);
        s.set(1, nextj);
        Assertions.assertEquals(nextj, s.get(1));
        Assertions.assertEquals(String.format(format, nexti, nextj), s.get());
      }
    }

    Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
      s.set(2, 0);
    });
  }

  private static String next(UniformRandomProvider rng) {
    final byte[] bytes = new byte[20];
    rng.nextBytes(bytes);
    return new String(bytes);
  }
}
