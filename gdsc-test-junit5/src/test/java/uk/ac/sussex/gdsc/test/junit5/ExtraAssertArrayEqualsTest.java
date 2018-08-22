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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * This class is used to hit coverage points not made by the {@link ExtraAssertionsTest}.
 */
@SuppressWarnings("javadoc")
public class ExtraAssertArrayEqualsTest
{
    @Test
    public void testAssertArrayEqualsDouble()
    {
        final double[] e = new double[] { 1 };
        final Deque<Integer> indexes = new ArrayDeque<>();
        ExtraAssertArrayEquals.assertArrayEquals(e, e, 1, indexes, "no message");
    }

    @Test
    public void testAssertArrayEqualsFloat()
    {
        final float[] e = new float[] { 1 };
        final Deque<Integer> indexes = new ArrayDeque<>();
        ExtraAssertArrayEquals.assertArrayEquals(e, e, 1, indexes, "no message");
    }

    @Test
    public void testGetClassName()
    {
        Assertions.assertEquals("null", ExtraAssertArrayEquals.getClassName(null));
        Assertions.assertEquals("java.lang.Integer", ExtraAssertArrayEquals.getClassName(new java.lang.Integer(0)));
        Class<?> clazz = new java.lang.Integer(0).getClass();
        Assertions.assertEquals("java.lang.Integer", ExtraAssertArrayEquals.getClassName(clazz));
        @SuppressWarnings("serial")
        Object anonymous = new java.io.Serializable()
        {
            // Do nothing
        };
        String expected = this.getClass().getName() + "$1";
        Assertions.assertEquals(expected, ExtraAssertArrayEquals.getClassName(anonymous));
        Assertions.assertEquals(expected, ExtraAssertArrayEquals.getClassName(anonymous.getClass()));
    }
}
