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

package uk.ac.sussex.gdsc.test.api.generator;

/**
 * Contains string utility functions.
 */
public final class StringUtils {
  /**
   * Do not allow public construction.
   */
  private StringUtils() {}

  /**
   * Checks if the message is not empty.
   *
   * @param message the message
   * @return true, if is not empty
   */
  public static boolean isNotEmpty(String message) {
    return message != null && message.length() > 0;
  }

  /**
   * Checks if the message is null or empty.
   *
   * @param message the message
   * @return true, if null or empty
   */
  public static boolean isNullOrEmpty(String message) {
    return message == null || message.length() == 0;
  }
}
