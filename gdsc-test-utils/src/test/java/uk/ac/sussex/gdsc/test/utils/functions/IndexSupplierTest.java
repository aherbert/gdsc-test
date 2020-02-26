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

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("javadoc")
public class IndexSupplierTest {
  private final String messagePrefix = "prefix ";
  private final String messageSuffix = " suffix";

  @Test
  public void testConstructer() {
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

      is = new IndexSupplier(dim, messagePrefix, messageSuffix);
      Assertions.assertNotNull(is);
      Assertions.assertEquals(dim, is.getDimensions());
      Assertions.assertEquals(messagePrefix, is.getMessagePrefix());
      Assertions.assertEquals(messageSuffix, is.getMessageSuffix());
    }
  }

  @Test
  public void testSetters() {
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
    is.setMessagePrefix(messagePrefix);
    Assertions.assertEquals(messagePrefix, is.getMessagePrefix());

    is.setMessageSuffix(null);
    Assertions.assertNull(is.getMessageSuffix());
    is.setMessageSuffix("");
    Assertions.assertEquals("", is.getMessageSuffix());
    is.setMessageSuffix(messageSuffix);
    Assertions.assertEquals(messageSuffix, is.getMessageSuffix());
  }

  @Test
  public void test1DMessage() {
    final IndexSupplier is = new IndexSupplier(1);
    Assertions.assertEquals("[0]", is.get());
    final UniformRandomProvider rng = RandomSource.create(RandomSource.SPLIT_MIX_64);
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
    is.setMessagePrefix(messagePrefix);
    is.setMessageSuffix(messageSuffix);
    for (int i = 0; i < 5; i++) {
      final int next = rng.nextInt(10);
      is.set(0, next);
      Assertions.assertEquals(messagePrefix + "(" + next + ")" + messageSuffix, is.get());
    }

    Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
      is.set(1, 0);
    });
  }

  @Test
  public void test2DMessage() {
    final IndexSupplier is = new IndexSupplier(2);
    Assertions.assertEquals("[0][0]", is.get());
    final UniformRandomProvider rng = RandomSource.create(RandomSource.SPLIT_MIX_64);
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
  public void demoIndexSupplier() {
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
