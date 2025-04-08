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
class IndexSupplierTest {
  private static final String PREFIX = "prefix ";
  private static final String SUFFIX = " suffix";

  @Test
  void testConstructer() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      @SuppressWarnings("unused")
      final IndexSupplier s = new IndexSupplier(0);
    });
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      @SuppressWarnings("unused")
      final IndexSupplier s = new IndexSupplier(-1);
    });
    for (int dim = 1; dim <= 3; dim++) {
      IndexSupplier is = new IndexSupplier(dim);
      Assertions.assertNotNull(is);
      Assertions.assertEquals(dim, is.getDimensions());

      is = new IndexSupplier(dim, PREFIX, SUFFIX);
      Assertions.assertNotNull(is);
      Assertions.assertEquals(dim, is.getDimensions());
      Assertions.assertEquals(PREFIX, is.getMessagePrefix());
      Assertions.assertEquals(SUFFIX, is.getMessageSuffix());
    }
  }

  @Test
  void testSetters() {
    final IndexSupplier is = new IndexSupplier(1);
    Assertions.assertEquals("[", is.getPrefix());
    Assertions.assertEquals("]", is.getSuffix());
    Assertions.assertEquals("][", is.getDelimiter());
    Assertions.assertNull(is.getMessagePrefix());
    Assertions.assertNull(is.getMessageSuffix());

    is.setFormat("<", ">");
    Assertions.assertEquals("<", is.getPrefix());
    Assertions.assertEquals(">", is.getSuffix());
    Assertions.assertEquals("><", is.getDelimiter());

    is.setMessagePrefix(null);
    Assertions.assertNull(is.getMessagePrefix());
    is.setMessagePrefix("");
    Assertions.assertEquals("", is.getMessagePrefix());
    is.setMessagePrefix(PREFIX);
    Assertions.assertEquals(PREFIX, is.getMessagePrefix());

    is.setMessageSuffix(null);
    Assertions.assertNull(is.getMessageSuffix());
    is.setMessageSuffix("");
    Assertions.assertEquals("", is.getMessageSuffix());
    is.setMessageSuffix(SUFFIX);
    Assertions.assertEquals(SUFFIX, is.getMessageSuffix());
  }

  @Test
  void test1DMessage() {
    final IndexSupplier is = new IndexSupplier(1);
    Assertions.assertEquals("[0]", is.get());
    final UniformRandomProvider rng = RandomSource.SPLIT_MIX_64.create();
    for (int i = 0; i < 5; i++) {
      final int next = rng.nextInt(10);
      is.set(0, next);
      Assertions.assertEquals(next, is.get(0));
      Assertions.assertEquals("[" + next + "]", is.get());
    }
    is.setFormat("<", ">");
    // Try with empty message pre/suffix
    is.setMessagePrefix("");
    is.setMessageSuffix("");
    for (int i = 0; i < 5; i++) {
      final int next = rng.nextInt(10);
      is.set(0, next);
      Assertions.assertEquals("<" + next + ">", is.get());
    }
    is.setFormat("(", ")");
    // Try with empty message pre/suffix
    is.setMessagePrefix(PREFIX);
    is.setMessageSuffix(SUFFIX);
    for (int i = 0; i < 5; i++) {
      final int next = rng.nextInt(10);
      is.set(0, next);
      Assertions.assertEquals(PREFIX + "(" + next + ")" + SUFFIX, is.get());
    }

    Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
      is.set(1, 0);
    });
  }

  @Test
  void test2DMessage() {
    final IndexSupplier is = new IndexSupplier(2);
    Assertions.assertEquals("[0][0]", is.get());
    final UniformRandomProvider rng = RandomSource.SPLIT_MIX_64.create();
    for (int i = 0; i < 3; i++) {
      final int nexti = rng.nextInt(10);
      is.set(0, nexti);
      Assertions.assertEquals(nexti, is.get(0));
      for (int j = 0; j < 3; j++) {
        final int nextj = rng.nextInt(10);
        is.set(1, nextj);
        Assertions.assertEquals(nextj, is.get(1));
        Assertions.assertEquals("[" + nexti + "][" + nextj + "]", is.get());
      }
    }

    Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
      is.set(2, 0);
    });
  }

  @Test
  void demoIndexSupplier() {
    final int dimensions = 2;
    final IndexSupplier is = new IndexSupplier(dimensions);
    is.setMessagePrefix("Index count: ");
    is.setFormat("(", ")");
    try {
      int count = 0;
      for (int i = 0; i < 3; i++) {
        is.set(0, i);
        for (int j = 0; j < 3; j++) {
          // Fails at message "Index count: (2)(1)"
          Assertions.assertTrue(count++ < 7, is.set(1, j));
        }
      }
      Assertions.fail("Should not reach here!");
    } catch (final AssertionError ex) {
      Assertions.assertTrue(ex.getMessage().startsWith("Index count: (2)(1)"));
    }
  }
}
