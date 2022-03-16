/*-
 * #%L
 * Genome Damage and Stability Centre Test API
 *
 * Contains a generic framework for test predicates.
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

package uk.ac.sussex.gdsc.test.api;

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
