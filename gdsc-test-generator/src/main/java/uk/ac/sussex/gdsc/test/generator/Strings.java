/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
 *
 * Generates Java classes for the GDSC Test project.
 * %%
 * Copyright (C) 2018 - 2025 Alex Herbert
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

package uk.ac.sussex.gdsc.test.generator;

/**
 * Contains string utility functions.
 */
final class Strings {

  /** The space ' ' character. */
  private static final char SPACE_CHAR = ' ';

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

  /**
   * Trim brackets '[' and ']' from the first and last position in the string.
   *
   * @param string the string
   * @return the string
   */
  static String trimBrackets(String string) {
    int len = string.length();
    final int start = (len > 0 && string.charAt(0) == '[') ? 1 : 0;
    if (start < len && (string.charAt(len - 1) == ']')) {
      len--;
    }
    return ((start > 0) || (len < string.length())) ? string.substring(start, len) : string;
  }
}
