/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
 *
 * Contains utilities for use with JUnit 5.
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
package uk.ac.sussex.gdsc.test.junit5;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Supplier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

/**
 * This class is used to hit coverage points not made by the {@link ExtraAssertionsTest}.
 */
@SuppressWarnings("javadoc")
public class ExtraAssertionUtilsTest
{
    @Test
    public void testAssertValidDeltaDouble()
    {
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertionUtils.assertValidDelta(0d);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertionUtils.assertValidDelta(Double.NaN);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertionUtils.assertValidDelta(-1d);
        });
    }

    @Test
    public void testAssertValidDeltaFloat()
    {
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertionUtils.assertValidDelta(0f);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertionUtils.assertValidDelta(Float.NaN);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertionUtils.assertValidDelta(-1f);
        });
    }

    @Test
    public void testNullSafeGet()
    {
        final String message = "Fixed message";
        Assertions.assertEquals(message, ExtraAssertionUtils.nullSafeGet(message));
        final String nullMessage = null;
        Assertions.assertEquals(null, ExtraAssertionUtils.nullSafeGet(nullMessage));
        final Supplier<String> messageSupplier = () -> {
            return "Lambda message";
        };
        Assertions.assertEquals(messageSupplier.get(), ExtraAssertionUtils.nullSafeGet(messageSupplier));
        final Supplier<String> nullMessageSupplier = null;
        Assertions.assertEquals(null, ExtraAssertionUtils.nullSafeGet(nullMessageSupplier));
        final Supplier<String> badMessageSupplier = () -> {
            return null;
        };
        Assertions.assertEquals(null, ExtraAssertionUtils.nullSafeGet(badMessageSupplier));
    }

    @Test
    public void testFormatIndexes()
    {
        Deque<Integer> indexes = null;
        Assertions.assertEquals("", ExtraAssertionUtils.formatIndexes(indexes));
        indexes = new ArrayDeque<>();
        Assertions.assertEquals("", ExtraAssertionUtils.formatIndexes(indexes));
        indexes.add(1);
        Assertions.assertEquals(" at index [1]", ExtraAssertionUtils.formatIndexes(indexes));
        indexes.add(45);
        Assertions.assertEquals(" at index [1][45]", ExtraAssertionUtils.formatIndexes(indexes));
    }
}
