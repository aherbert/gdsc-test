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
package uk.ac.sussex.gdsc.test.functions;

import java.util.function.Supplier;

/**
 * Supply a formatted message using the {@link String#format(String, Object...)}
 * function with an array of object arguments.
 */
public class ObjectArrayFormatSupplier extends ArrayFormatSupplier {

    private final Object[] values;

    /**
     * Constructs a new instance of this class.
     *
     * @param format the format (assumed to support the class {@link #getArgs()}
     *               function)
     * @param size   the size
     * @throws IllegalArgumentException If the format is null or empty, or size if
     *                                  not strictly positive
     */
    public ObjectArrayFormatSupplier(String format, int size) throws IllegalArgumentException {
        super(format, size);
        values = new Object[size];
    }

    /**
     * Sets the value.
     *
     * @param i     the index
     * @param value the value
     * @return the supplier
     */
    public Supplier<String> set(int i, Object value) {
        values[i] = value;
        return this;
    }

    /**
     * Gets the value.
     *
     * @param i the index
     * @return the value
     */
    public Object get(int i) {
        return values[i];
    }

    @Override
    public Object[] getArgs() {
        return values;
    }
}
