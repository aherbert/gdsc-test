/*-
 * #%L
 * Genome Damage and Stability Centre Test API
 *
 * Contains a generic framework for test predicates.
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

package uk.ac.sussex.gdsc.test.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("javadoc")
class ClassesTest {
  @Test
  void testGetClassName() {
    Assertions.assertEquals("null", Classes.getClassName(null));

    // Objects
    final Integer zero = Integer.valueOf(0);
    Assertions.assertEquals("java.lang.Integer", Classes.getClassName(zero));
    final Class<?> clazz = zero.getClass();
    Assertions.assertEquals("java.lang.Integer", Classes.getClassName(clazz));

    Assertions.assertEquals("java.lang.String", Classes.getClassName(String.class));

    @SuppressWarnings("serial")
    final Object anonymous = new java.io.Serializable() {
      // Do nothing
    };
    final String expected = this.getClass().getName() + "$1";
    Assertions.assertEquals(expected, Classes.getClassName(anonymous));
    Assertions.assertEquals(expected, Classes.getClassName(anonymous.getClass()));
  }

  @Test
  void testGetArrayClassName() {
    Assertions.assertEquals("boolean[]", Classes.getArrayClassName(new boolean[0]));
    Assertions.assertEquals("byte[]", Classes.getArrayClassName(new byte[0]));
    Assertions.assertEquals("char[]", Classes.getArrayClassName(new char[0]));
    Assertions.assertEquals("double[]", Classes.getArrayClassName(new double[0]));
    Assertions.assertEquals("float[]", Classes.getArrayClassName(new float[0]));
    Assertions.assertEquals("int[]", Classes.getArrayClassName(new int[0]));
    Assertions.assertEquals("long[]", Classes.getArrayClassName(new long[0]));
    Assertions.assertEquals("short[]", Classes.getArrayClassName(new short[0]));

    Assertions.assertEquals("null", Classes.getArrayClassName(null));
    Assertions.assertEquals("java.lang.String", Classes.getArrayClassName(String.class));
    Assertions.assertEquals("byte", Classes.getArrayClassName(byte.class));
    Assertions.assertEquals("java.lang.Object[]", Classes.getArrayClassName(new Object[0]));
    Assertions.assertEquals("int[][]", Classes.getArrayClassName(new int[0][0]));
    Assertions.assertEquals("int[][][]", Classes.getArrayClassName(new int[0][0][0]));
    Assertions.assertEquals("java.lang.Integer[][]", Classes.getArrayClassName(new Integer[0][0]));
  }
}
