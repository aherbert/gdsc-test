/*-
 * #%L
 * Genome Damage and Stability Centre Test Package
 *
 * The GDSC Test package contains code for use with the JUnit test framework.
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

import java.util.function.Supplier;

/**
 * Supply a formatted message using the {@link String#format(String, Object...)}
 * function.
 * <p>
 * This is a base class to allow dynamic update of a message supplier for use in
 * messages.
 */
public abstract class FormatSupplier implements Supplier<String> {

    /** The message format. */
    private final String format;

    /**
     * Constructs a new instance of this class.
     *
     * @param format the format
     * @throws IllegalArgumentException If the format is null or empty
     */
    public FormatSupplier(String format) throws IllegalArgumentException {
        if (format == null || format.length() == 0)
            throw new IllegalArgumentException("Format is null or empty");
        this.format = format;
    }

    /**
     * Gets a message consisting of:
     *
     * <pre>
     * String.format(format, getArgs());
     * </pre>
     *
     * @see java.util.function.Supplier#get()
     * @see #getArgs()
     */
    @Override
    public String get() {
        return String.format(format, getArgs());
    }

    /**
     * Gets the arguments for the {@link String#format(String, Object...)} function.
     *
     * @return the arguments
     */
    public abstract Object[] getArgs();
}
