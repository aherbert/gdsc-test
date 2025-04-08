/*-
 * #%L
 * Genome Damage and Stability Centre Test API
 *
 * Contains a generic framework for test predicates.
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

package uk.ac.sussex.gdsc.test.api.function;

import java.util.function.BiFunction;
import java.util.function.Supplier;
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
  void testToString() {
    Assertions.assertNull(Strings.toString(null));

    final String string1 = "asdhjkfkas";
    final String string2 = "ahsfhkasdj";
    Assertions.assertEquals(string1, Strings.toString(string1));
    Assertions.assertNotEquals(string1, Strings.toString(string2));

    final Supplier<String> supp1 = () -> string1;
    final Supplier<String> supp2 = () -> null;
    Assertions.assertEquals(string1, Strings.toString(supp1));
    Assertions.assertNotEquals(string1, Strings.toString(supp2));

    Assertions.assertNull(Strings.toString(supp2));
  }

  @Test
  void testNegateToString() {
    Assertions.assertNull(Strings.negateToString(null));

    final String string1 = "15277512";
    final String result = Strings.negateToString(string1);
    Assertions.assertTrue(result.contains(string1));
    Assertions.assertTrue(result.contains("!"));
  }

  @Test
  void testOrToString() {
    testCombinedToString(Strings::orToString, "||");
  }

  @Test
  void testAndToString() {
    testCombinedToString(Strings::andToString, "&&");
  }

  @Test
  void testXorToString() {
    testCombinedToString(Strings::xorToString, "^");
  }

  private static void testCombinedToString(BiFunction<Object, Object, String> fun,
      String operator) {
    final String[] strings = {null, "aeydfbuhs", "njkads9767867"};
    for (final String string1 : strings) {
      for (final String string2 : strings) {
        final String result = fun.apply(string1, string2);
        if (string1 == null && string2 == null) {
          Assertions.assertNull(result);
        } else {
          boolean na = false;
          if (string1 != null) {
            Assertions.assertTrue(result.contains(string1));
          } else {
            na = true;
          }
          if (string2 != null) {
            Assertions.assertTrue(result.contains(string2));
          } else {
            na = true;
          }
          Assertions.assertEquals(na, result.contains(Strings.NA));
          Assertions.assertTrue(result.contains(operator));
        }
      }
    }
  }
}
