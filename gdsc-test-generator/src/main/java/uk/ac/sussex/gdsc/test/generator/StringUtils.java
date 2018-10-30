/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
 *
 * Generates API classes for the GDSC Test project.
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

package uk.ac.sussex.gdsc.test.generator;

/**
 * Contains string utility functions.
 */
public final class StringUtils {

  /** The space ' ' character. */
  private static final char SPACE_CHAR = ' ';

  /**
   * Do not allow public construction.
   */
  private StringUtils() {}

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

  /**
   * Trim brackets '[' and ']' from the first and last position in the string.
   *
   * @param string the string
   * @return the string
   */
  public static String trimBrackets(String string) {
    int len = string.length();
    final int start = (len > 0 && string.charAt(0) == '[') ? 1 : 0;
    if (start < len && (string.charAt(len - 1) == ']')) {
      len--;
    }
    return ((start > 0) || (len < string.length())) ? string.substring(start, len) : string;
  }
}
