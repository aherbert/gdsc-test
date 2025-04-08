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
  }
}
