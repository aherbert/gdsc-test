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

import java.util.function.Supplier;

/**
 * Defines {@link String} utilities.
 */
public final class Strings {

  /** The space ' ' character. */
  private static final char SPACE_CHAR = ' ';

  /** The empty string. */
  public static final String EMPTY = "";

  /** The wrapped unknown string: "(NA)". */
  public static final String NA = "(NA)";

  /**
   * Do not allow public construction.
   */
  private Strings() {}

  /**
   * Checks if the string is not null or empty whitespace.
   *
   * @param string the string
   * @return true, if is not empty
   */
  public static boolean isNotEmpty(String string) {
    return string != null && hasNonWhiteSpace(string);
  }

  /**
   * Checks if the string is null or empty whitespace.
   *
   * @param string the string
   * @return true, if null or empty
   */
  public static boolean isNullOrEmpty(String string) {
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
  public static String toString(Object object) {
    if (object instanceof String) {
      return (String) object;
    }
    if (object instanceof Supplier) {
      final Object supplied = ((Supplier<?>) object).get();
      if (supplied != null) {
        return supplied.toString();
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
  public static String negateToString(Object object) {
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
  public static String orToString(Object object1, Object object2) {
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
   * </ul> {@link String} or {@link Supplier} of {@link String}.
   *
   * @param object1 the first object
   * @param object2 the second object
   * @return the AND string representation
   * @see #toString(Object)
   */
  public static String andToString(Object object1, Object object2) {
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
  public static String xorToString(Object object1, Object object2) {
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
  public static String wrap(String string) {
    return isNotEmpty(string) ? "(" + string + ")" : NA;
  }
}
