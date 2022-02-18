/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
 *
 * Generates Java classes for the GDSC Test project.
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

package uk.ac.sussex.gdsc.test.generator;

/**
 * Used to signal that a model is not valid.
 */
public class InvalidModelException extends Exception {

  /** The serial version UID. */
  private static final long serialVersionUID = 1L; // NOPMD

  /**
   * Creates a new illegal model exception.
   *
   * @param message the message
   */
  public InvalidModelException(String message) {
    super(message);
  }

  /**
   * Creates a new illegal model exception.
   *
   * @param message the message
   * @param cause the cause
   */
  public InvalidModelException(String message, Throwable cause) {
    super(message, cause);
  }
}
