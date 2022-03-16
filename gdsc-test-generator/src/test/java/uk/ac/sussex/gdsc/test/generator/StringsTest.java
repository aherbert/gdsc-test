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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("javadoc")
class StringsTest {
  @Test
  void testIsNotEmpty() {
    Assertions.assertTrue(Strings.isNotEmpty("Not empty"));
    Assertions.assertFalse(Strings.isNotEmpty(""));
    Assertions.assertFalse(Strings.isNotEmpty(" "));
    Assertions.assertFalse(Strings.isNotEmpty("\t"));
    Assertions.assertFalse(Strings.isNotEmpty(null));
  }

  @Test
  void testNullOrEmpty() {
    Assertions.assertFalse(Strings.isNullOrEmpty("Not empty"));
    Assertions.assertTrue(Strings.isNullOrEmpty(""));
    Assertions.assertTrue(Strings.isNullOrEmpty(" "));
    Assertions.assertTrue(Strings.isNullOrEmpty("\t"));
    Assertions.assertTrue(Strings.isNullOrEmpty(null));
  }

  @Test
  void testTrimeBrackets() {
    Assertions.assertEquals("", Strings.trimBrackets(""));
    Assertions.assertEquals("", Strings.trimBrackets("["));
    Assertions.assertEquals("", Strings.trimBrackets("]"));
    Assertions.assertEquals("", Strings.trimBrackets("[]"));
    Assertions.assertEquals("a", Strings.trimBrackets("a]"));
    Assertions.assertEquals("a", Strings.trimBrackets("[a"));
    Assertions.assertEquals("a", Strings.trimBrackets("a"));
    Assertions.assertEquals("a", Strings.trimBrackets("[a]"));
  }
}
