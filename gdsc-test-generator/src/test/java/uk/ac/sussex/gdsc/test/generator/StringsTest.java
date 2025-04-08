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
