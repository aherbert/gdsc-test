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

/**
 * Supply a formatted message using the {@link String#format(String, Object...)}
 * function with an array of arguments.
 * <p>
 * This is a base class to allow dynamic update of a message supplier for use in
 * messages.
 */
public abstract class ArrayFormatSupplier extends FormatSupplier {

    /** The size. */
    private final int size;

    /**
     * Constructs a new instance of this class.
     *
     * @param format the format
     * @param size   the size
     * @throws IllegalArgumentException If the format is null or empty, or size if
     *                                  not strictly positive
     */
    public ArrayFormatSupplier(String format, int size) throws IllegalArgumentException {
        super(format);
        if (size < 1)
            throw new IllegalArgumentException(size + " < 1");
        this.size = size;
    }

    /**
     * Gets the size of the array.
     *
     * @return the size
     */
    public int getSize() {
        return size;
    }
}
