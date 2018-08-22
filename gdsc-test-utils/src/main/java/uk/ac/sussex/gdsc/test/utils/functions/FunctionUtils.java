/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
 *
 * Contains utilities for use with test frameworks.
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
package uk.ac.sussex.gdsc.test.utils.functions;

import java.util.Formatter;
import java.util.Locale;
import java.util.function.Supplier;

/**
 * Contains function utilities.
 */
public class FunctionUtils {

    private static final Locale locale = Locale.getDefault(Locale.Category.FORMAT);

    /**
     * Do not allow construction
     */
    private FunctionUtils() {
        // Do nothing
    }

    /**
     * Get a supplier for the string using the format and arguments.
     * <p>
     * This can be used where it is not convenient to create a lambda function
     * directly because the arguments are not effectively final.
     * <p>
     * Returns the equivalent of:
     *
     * <pre>
     * <code>
     * () -&gt; String.format(format, args);
     * </code>
     * </pre>
     *
     * @param format the format
     * @param args   the arguments
     * @return the supplier
     */
    public static final Supplier<String> getSupplier(String format, Object... args) {
        return () -> new Formatter(locale).format(format, args).toString();
    }
}
