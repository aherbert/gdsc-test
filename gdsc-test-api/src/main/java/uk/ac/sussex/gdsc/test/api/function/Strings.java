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

package uk.ac.sussex.gdsc.test.api.function;

import java.util.function.Supplier;

/**
 * Defines {@link String} utilities.
 */
final class Strings {

  /** The space ' ' character. */
  private static final char SPACE_CHAR = ' ';

  /** The empty string. */
  static final String EMPTY = "";

  /** The wrapped unknown string: "(NA)". */
  static final String NA = "(NA)";

  /**
   * Do not allow construction.
   */
  private Strings() {}

  /**
   * Checks if the string is not null or empty whitespace.
   *
   * @param string the string
   * @return true, if is not empty
   */
  static boolean isNotEmpty(String string) {
    return string != null && hasNonWhiteSpace(string);
  }

  /**
   * Checks if the string is null or empty whitespace.
   *
   * @param string the string
   * @return true, if null or empty
   */
  static boolean isNullOrEmpty(String string) {
    return string == null || !hasNonWhiteSpace(string);
  }

  /**
   * Checks for non white space.
   *
   * @param string the string
   * @return true, if successful
   */
  private static boolean hasNonWhiteSpace(String string) {
    for (int i = 0; i < string.length(); i++) {
      if (string.charAt(i) > SPACE_CHAR) {
        return true;
      }
    }
    return false;
  }

  //@formatter:off

  /**
   * Gets the string representation. The object may be a {@link String} or {@link Supplier}.
   *
   * <ul>
   *  <li>If the object is a {@link String} then the string is returned.
   *  <li>If the object is a {@link Supplier} then the supplied object is obtained and,
   *      if not null, {@link Object#toString()} is returned.
   *  <li>Otherwise returns {@code null}.
   * </ul>
   *
   * @param object the object
   * @return the string representation
   */
  static String toString(Object object) {
    if (object instanceof String) {
      return (String) object;
    }
    if (object instanceof Supplier) {
      final Object result = ((Supplier<?>) object).get();
      if (result != null) {
        return result.toString();
      }
    }
    return null;
  }

  /**
   * Gets the negation of the string representation, obtained from {@link #toString(Object)}.
   *
   * <ul>
   *  <li>If the object has a string representation the result is {@code !(string)}.
   *  <li>Otherwise returns {@code null}.
   * </ul>
   *
   * @param object the object
   * @return the string representation
   */
  static String negateToString(Object object) {
    final String s = toString(object);
    return (isNotEmpty(s)) ? "!(" + s + ")" : null;
  }

  /**
   * Gets the logical OR combination of the string representation of two objects, each obtained from
   * {@link #toString(Object)}.
   *
   * <ul>
   *  <li>If both strings are valid the result is {@code (string1) || (string2)}.
   *  <li>If either string is {@code null} or empty it will be replaced with {@code NA}.
   *  <li>Otherwise returns {@code null}.
   * </ul>
   *
   * @param object1 the first object
   * @param object2 the second object
   * @return the OR string representation
   * @see #toString(Object)
   */
  static String orToString(Object object1, Object object2) {
    final String s1 = toString(object1);
    final String s2 = toString(object2);
    if (isNotEmpty(s1) || isNotEmpty(s2)) {
      return wrap(s1) + " || " + wrap(s2);
    }
    return null;
  }

  /**
   * Gets the logical AND combination of the string representation of two objects, each obtained
   * from {@link #toString(Object)}.
   *
   * <ul>
   *  <li>If both strings are valid the result is {@code (string1) && (string2)}.
   *  <li>If either string is {@code null} or empty it will be replaced with {@code NA}.
   *  <li>Otherwise returns {@code null}.
   * </ul>
   *
   * @param object1 the first object
   * @param object2 the second object
   * @return the AND string representation
   * @see #toString(Object)
   */
  static String andToString(Object object1, Object object2) {
    final String s1 = toString(object1);
    final String s2 = toString(object2);
    if (isNotEmpty(s1) || isNotEmpty(s2)) {
      return wrap(s1) + " && " + wrap(s2);
    }
    return null;
  }

  /**
   * Gets the logical XOR combination of the string representation of two objects, each obtained
   * from {@link #toString(Object)}.
   *
   * <ul>
   *  <li>If both strings are valid the result is {@code (string1) ^ (string2)}.
   *  <li>If either string is {@code null} or empty it will be replaced with {@code NA}.
   *  <li>Otherwise returns {@code null}.
   * </ul>
   *
   * @param object1 the first object
   * @param object2 the second object
   * @return the XOR string representation
   * @see #toString(Object)
   */
  static String xorToString(Object object1, Object object2) {
    final String s1 = toString(object1);
    final String s2 = toString(object2);
    if (isNotEmpty(s1) || isNotEmpty(s2)) {
      return wrap(s1) + " ^ " + wrap(s2);
    }
    return null;
  }

  //@formatter:on

  /**
   * Wrap the string with braces, or if empty return "(NA)".
   *
   * @param string the string
   * @return the wrapped string
   */
  static String wrap(String string) {
    return isNotEmpty(string) ? "(" + string + ")" : NA;
  }
}
