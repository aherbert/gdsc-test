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
