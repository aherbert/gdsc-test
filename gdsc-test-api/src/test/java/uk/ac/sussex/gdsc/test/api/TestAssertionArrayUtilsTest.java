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

/**
 * This class is used to hit coverage points not made by the {@link TestAssertionsTest}.
 */
@SuppressWarnings("javadoc")
class TestAssertionArrayUtilsTest {
  @Test
  void testGetClassName() {
    final Integer zero = Integer.valueOf(0);
    Assertions.assertEquals("null", TestAssertionArrayUtils.getClassName(null));
    Assertions.assertEquals("java.lang.Integer", TestAssertionArrayUtils.getClassName(zero));
    final Class<?> clazz = zero.getClass();
    Assertions.assertEquals("java.lang.Integer", TestAssertionArrayUtils.getClassName(clazz));
    @SuppressWarnings("serial")
    final Object anonymous = new java.io.Serializable() {
      // Do nothing
    };
    final String expected = this.getClass().getName() + "$1";
    Assertions.assertEquals(expected, TestAssertionArrayUtils.getClassName(anonymous));
    Assertions.assertEquals(expected, TestAssertionArrayUtils.getClassName(anonymous.getClass()));
  }
}
