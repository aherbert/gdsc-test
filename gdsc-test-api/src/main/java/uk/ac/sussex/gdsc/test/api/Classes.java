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
