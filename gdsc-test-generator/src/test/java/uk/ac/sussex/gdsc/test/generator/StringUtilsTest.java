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
public class StringUtilsTest {
  @Test
  public void testIsNotEmpty() {
    Assertions.assertTrue(StringUtils.isNotEmpty("Not empty"));
    Assertions.assertFalse(StringUtils.isNotEmpty(""));
    Assertions.assertFalse(StringUtils.isNotEmpty(" "));
    Assertions.assertFalse(StringUtils.isNotEmpty("\t"));
    Assertions.assertFalse(StringUtils.isNotEmpty(null));
  }

  @Test
  public void testNullOrEmpty() {
    Assertions.assertFalse(StringUtils.isNullOrEmpty("Not empty"));
    Assertions.assertTrue(StringUtils.isNullOrEmpty(""));
    Assertions.assertTrue(StringUtils.isNullOrEmpty(" "));
    Assertions.assertTrue(StringUtils.isNullOrEmpty("\t"));
    Assertions.assertTrue(StringUtils.isNullOrEmpty(null));
  }

  @Test
  public void testTrimeBrackets() {
    Assertions.assertEquals("", StringUtils.trimBrackets(""));
    Assertions.assertEquals("", StringUtils.trimBrackets("["));
    Assertions.assertEquals("", StringUtils.trimBrackets("]"));
    Assertions.assertEquals("", StringUtils.trimBrackets("[]"));
    Assertions.assertEquals("a", StringUtils.trimBrackets("a]"));
    Assertions.assertEquals("a", StringUtils.trimBrackets("[a"));
    Assertions.assertEquals("a", StringUtils.trimBrackets("a"));
    Assertions.assertEquals("a", StringUtils.trimBrackets("[a]"));
  }
}
