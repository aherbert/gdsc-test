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

import java.util.Arrays;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("javadoc")
class IndicesTest {
  @Test
  void testPushPop() {
    final int[] values = {123, 1, 2, 4325, 99};
    final Indices i = new Indices();
    Assertions.assertEquals(0, i.size());
    Assertions.assertTrue(i.isEmpty());
    Assertions.assertEquals("", i.toString());
    StringBuilder sb = new StringBuilder();
    i.append(sb);
    Assertions.assertEquals("", sb.toString());
    for (int j = 0; j < values.length; j++) {
      i.push(values[j]);
      Assertions.assertEquals(j + 1, i.size());
      Assertions.assertFalse(i.isEmpty());
      String expected = Arrays.stream(values).limit(j + 1).mapToObj(Integer::toString)
          .collect(Collectors.joining("][", "[", "]"));
      Assertions.assertEquals(expected, i.toString());
      sb.setLength(0);
      i.append(sb);
      Assertions.assertEquals(expected, sb.toString());
    }
    for (int j = values.length; j-- != 0;) {
      Assertions.assertFalse(i.isEmpty());
      Assertions.assertEquals(values[j], i.pop());
      Assertions.assertEquals(j, i.size());
      String expected;
      if (j == 0) {
        expected = "";
      } else {
        expected = Arrays.stream(values).limit(j).mapToObj(Integer::toString)
            .collect(Collectors.joining("][", "[", "]"));
      }
      Assertions.assertEquals(expected, i.toString());
      sb.setLength(0);
      i.append(sb);
      Assertions.assertEquals(expected, sb.toString());
    }
    Assertions.assertTrue(i.isEmpty());
    Assertions.assertEquals(-1, i.pop());
    Assertions.assertEquals(-1, i.pop());
    Assertions.assertEquals(-1, i.pop());
    Assertions.assertEquals(0, i.size());
    Assertions.assertTrue(i.isEmpty());
  }

  @Test
  void testReplace() {
    final Indices i = new Indices();
    i.toString(); // ""
    Assertions.assertEquals("", i.toString());
    i.push(1);
    i.push(3);
    Assertions.assertEquals("[1][3]", i.toString());
    i.pop();
    Assertions.assertEquals("[1]", i.toString());
    i.replace(43);
    Assertions.assertEquals("[43]", i.toString());
  }
}
