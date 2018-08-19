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
package uk.ac.sussex.gdsc.test.utils;

import java.util.function.Supplier;

/**
 * Contains test utility functions.
 */
public class TestUtils {
    /**
     * Do not allow public construction.
     */
    private TestUtils() {
    }

    /**
     * Wraps an assertion error with a new error that has a formatted message
     * appended to the input error's message.
     *
     * @param error           The error.
     * @param messageSupplier The message supplier.
     * @throws AssertionError The wrapped assertion error.
     */
    public static void wrapAssertionFailedErrorAppend(AssertionError error, Supplier<String> messageSupplier)
            throws AssertionError {
        final String msg = error.getMessage();
        if (msg == null || msg.length() == 0)
            throw new AssertionError(messageSupplier.get(), error);
        throw new AssertionError(msg + " " + messageSupplier.get(), error);
    }

    /**
     * Wraps an assertion error with a new error that has a formatted message
     * prepended to the input error's message.
     *
     * @param error           The error.
     * @param messageSupplier The message supplier.
     * @throws AssertionError The wrapped assertion error.
     */
    public static void wrapAssertionFailedError(AssertionError error, Supplier<String> messageSupplier)
            throws AssertionError {
        final String msg = error.getMessage();
        if (msg == null || msg.length() == 0)
            throw new AssertionError(messageSupplier.get(), error);
        throw new AssertionError(messageSupplier.get() + " " + msg, error);
    }
}
