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
package uk.ac.sussex.gdsc.test.utils;

import java.util.function.Supplier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import uk.ac.sussex.gdsc.test.utils.TestUtils;

@SuppressWarnings("javadoc")
public class TestUtilsTest {
    /** The supplied message. */
    private final Supplier<String> messageSupplier = () -> {
        return "Lambda message";
    };

    @Test
    public void testWrapAssertionFailedErrorAppend() {
        try {
            TestUtils.wrapAssertionFailedErrorAppend(new AssertionError(), messageSupplier);
        } catch (AssertionError e) {
            // Null is ignored
            Assertions.assertEquals(e.getMessage(), messageSupplier.get());
        }
        try {
            TestUtils.wrapAssertionFailedErrorAppend(new AssertionError(""), messageSupplier);
        } catch (AssertionError e) {
            // Empty string is ignored
            Assertions.assertEquals(e.getMessage(), messageSupplier.get());
        }
        try {
            TestUtils.wrapAssertionFailedErrorAppend(new AssertionError("Something bad"), messageSupplier);
        } catch (AssertionError e) {
            // Empty string is ignored
            Assertions.assertEquals(e.getMessage(), "Something bad " + messageSupplier.get());
        }
    }

    @Test
    public void testWrapAssertionFailedError() {
        try {
            TestUtils.wrapAssertionFailedError(new AssertionError(), messageSupplier);
        } catch (AssertionError e) {
            // Null is ignored
            Assertions.assertEquals(e.getMessage(), messageSupplier.get());
        }
        try {
            TestUtils.wrapAssertionFailedError(new AssertionError(""), messageSupplier);
        } catch (AssertionError e) {
            // Empty string is ignored
            Assertions.assertEquals(e.getMessage(), messageSupplier.get());
        }
        try {
            TestUtils.wrapAssertionFailedError(new AssertionError("Something bad"), messageSupplier);
        } catch (AssertionError e) {
            // Empty string is ignored
            Assertions.assertEquals(e.getMessage(), messageSupplier.get() + " Something bad");
        }
    }
}
