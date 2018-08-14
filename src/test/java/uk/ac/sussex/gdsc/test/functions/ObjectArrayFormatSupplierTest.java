/*-
 * #%L
 * Genome Damage and Stability Centre Test Package
 *
 * The GDSC Test package contains code for use with the JUnit test framework.
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
package uk.ac.sussex.gdsc.test.functions;

import org.apache.commons.rng.UniformRandomProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import uk.ac.sussex.gdsc.test.TestSettings;
import uk.ac.sussex.gdsc.test.junit5.RandomSeed;
import uk.ac.sussex.gdsc.test.junit5.SeededTest;

@SuppressWarnings("javadoc")
public class ObjectArrayFormatSupplierTest {

    private final String nullString = null;

    @Test
    public void testConstructer() {
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

    @SeededTest
    public void test1DMessage(RandomSeed seed) {
        final String format = "[%s]";
        final int size = 1;

        final ObjectArrayFormatSupplier s = new ObjectArrayFormatSupplier(format, size);
        Assertions.assertEquals(String.format(format, nullString), s.get());
        final UniformRandomProvider rng = TestSettings.getRandomGenerator(seed.getSeed());

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

    @SeededTest
    public void test2DMessage(RandomSeed seed) {
        final String format = "[%s][%s]";
        final int size = 2;
        final ObjectArrayFormatSupplier s = new ObjectArrayFormatSupplier(format, size);
        Assertions.assertEquals(String.format(format, nullString, nullString), s.get());
        final UniformRandomProvider rng = TestSettings.getRandomGenerator(seed.getSeed());
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
