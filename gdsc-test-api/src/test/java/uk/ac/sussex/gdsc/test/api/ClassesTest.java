/*-
 * #%L
 * Genome Damage and Stability Centre Test API
 *
 * Contains a generic framework for test predicates.
 * %%
 * Copyright (C) 2018 - 2022 Alex Herbert
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
