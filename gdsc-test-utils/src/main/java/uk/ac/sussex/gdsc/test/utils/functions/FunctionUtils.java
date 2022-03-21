/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
 *
 * Contains utilities for use with test frameworks.
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

package uk.ac.sussex.gdsc.test.utils.functions;

import java.util.Formatter;
import java.util.Locale;
import java.util.function.Supplier;

/**
 * Contains function utilities.
 */
public final class FunctionUtils {

  /** The default locale. */
  private static final Locale DEFAULT_LOCALE = Locale.getDefault(Locale.Category.FORMAT);

  /**
   * Do not allow construction.
   */
  private FunctionUtils() {
    // Do nothing
  }

  /**
   * Get a supplier for the string using the format and arguments.
   *
   * <p>This can be used where it is not convenient to create a lambda function directly because the
   * arguments are not effectively final.
   *
   * <p>Returns the equivalent of:
   *
   * <pre>
   * <code>
   * () -&gt; String.format(format, args);
   * </code>
   * </pre>
   *
   * <p>Uses the default locale. This is set once so any changes to the default locale will be
   * ignored.
   *
   * @param format the format
   * @param args the arguments
   * @return the supplier
   * @see Locale#getDefault(Locale.Category)
   */
  public static Supplier<String> getSupplier(String format, Object... args) {
    return () -> new Formatter(DEFAULT_LOCALE).format(format, args).toString();
  }
}
