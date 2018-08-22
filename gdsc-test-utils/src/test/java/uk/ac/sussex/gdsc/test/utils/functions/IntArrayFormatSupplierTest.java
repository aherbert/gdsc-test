/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
 *
 * Contains utilities for use with test frameworks.
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
package uk.ac.sussex.gdsc.test.utils.functions;

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import uk.ac.sussex.gdsc.test.utils.functions.IntArrayFormatSupplier;

@SuppressWarnings("javadoc")
public class IntArrayFormatSupplierTest {

    @Test
    public void testConstructer() {
        final String format = "not-empty";
        final int size = 1;

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            @SuppressWarnings("unused")
            final
            IntArrayFormatSupplier s = new IntArrayFormatSupplier(null, size);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            @SuppressWarnings("unused")
            final
            IntArrayFormatSupplier s = new IntArrayFormatSupplier("", size);
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            @SuppressWarnings("unused")
            final
            IntArrayFormatSupplier s = new IntArrayFormatSupplier(format, 0);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            @SuppressWarnings("unused")
            final
            IntArrayFormatSupplier s = new IntArrayFormatSupplier(format, -1);
        });

        final IntArrayFormatSupplier s = new IntArrayFormatSupplier(format, size);
        Assertions.assertNotNull(s);
        Assertions.assertEquals(size, s.getSize());
    }

    @Test
    public void test1DMessage() {
        final String format = "[%d]";
        final int size = 1;

        final IntArrayFormatSupplier s = new IntArrayFormatSupplier(format, size);
        Assertions.assertEquals(String.format(format, 0), s.get());
        final UniformRandomProvider rng = RandomSource.create(RandomSource.SPLIT_MIX_64);
        for (int i = 0; i < 5; i++) {
            final int next = rng.nextInt(10);
            s.set(0, next);
            Assertions.assertEquals(next, s.get(0));
            Assertions.assertEquals(String.format(format, next), s.get());
        }

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            s.set(1, 0);
        });
    }

    @Test
    public void test2DMessage() {
        final String format = "[%d][%d]";
        final int size = 2;
        final IntArrayFormatSupplier s = new IntArrayFormatSupplier(format, size);
        Assertions.assertEquals(String.format(format, 0, 0), s.get());
        final UniformRandomProvider rng = RandomSource.create(RandomSource.SPLIT_MIX_64);
        for (int i = 0; i < 3; i++) {
            final int nexti = rng.nextInt(10);
            s.set(0, nexti);
            Assertions.assertEquals(nexti, s.get(0));
            for (int j = 0; j < 3; j++) {
                final int nextj = rng.nextInt(10);
                s.set(1, nextj);
                Assertions.assertEquals(nextj, s.get(1));
                Assertions.assertEquals(String.format(format, nexti, nextj), s.get());
            }
        }

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            s.set(2, 0);
        });
    }
}
