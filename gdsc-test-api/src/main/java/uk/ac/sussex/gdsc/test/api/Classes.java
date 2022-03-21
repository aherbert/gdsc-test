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

/**
 * Defines {@link Class} utilities.
 */
final class Classes {

  /** No instances. */
  private Classes() {}

  /**
   * Gets the class name of the object.
   *
   * @param obj the object
   * @return the class name
   */
  static String getClassName(Object obj) {
    if (obj == null) {
      return "null";
    }
    if (obj instanceof Class) {
      final Class<?> clazz = (Class<?>) obj;
      final String name = clazz.getCanonicalName();
      return name != null ? name : clazz.getName();
    }
    return obj.getClass().getName();
  }

  /**
   * Gets the class name of the object. This resolves array names to the human readable format:
   *
   * <pre>
   * null                    "null"
   * String.class            "java.lang.String"
   * byte.class              "byte"
   * new Object[3]           "java.lang.Object[]"
   * new int[1][2][3]        "int[][][]"
   * </pre>
   *
   * @param obj the object
   * @return the class name
   */
  static String getArrayClassName(Object obj) {
    if (obj == null) {
      return "null";
    }
    final String name = getClassName(obj);

    // Convert array objects to their canonical name:
    // [I => int[]
    if (name.startsWith("[")) {
      // Assume the classname is correctly formatted:
      // [I
      // [[[Z
      // [Ljava.lang.String;
      int dimension = 1;
      while (name.charAt(dimension) == '[') {
        dimension++;
      }
      final int len = name.length();
      final StringBuilder sb = new StringBuilder(dimension * 2 + len);
      if (name.charAt(dimension) == 'L') {
        // Not a primitive array. It should end with ';'
        sb.append(name, dimension + 1, name.charAt(len - 1) == ';' ? len - 1 : len);
      } else {
        // Primitive array
        sb.append(getPrimitiveType(name.charAt(dimension)));
      }
      for (int i = 0; i < dimension; i++) {
        sb.append("[]");
      }
      return sb.toString();
    }

    return name;
  }

  /**
   * Gets the primitive type name.
   *
   * @param c the primitive classname abbreviation
   * @return the primitive type name
   */
  private static String getPrimitiveType(char c) {
    switch (c) {
      case 'Z':
        return "boolean";
      case 'B':
        return "byte";
      case 'C':
        return "char";
      case 'D':
        return "double";
      case 'F':
        return "float";
      case 'I':
        return "int";
      case 'J':
        return "long";
      case 'S':
        return "short";
      // This should not happen
      default:
        return "object";
    }
  }
}
