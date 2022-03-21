/*-
 * #%L
 * Genome Damage and Stability Centre Test API
 *
 * Contains a generic framework for test predicates.
 * %%
 * Copyright (C) 2018 - 2022 Alex Herbert
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

/**
 * Defines a LIFO stack of {@code int} indices. The indices can be compiled into a string
 * representation of the access of a ND array.
 *
 * <pre>
 * Indices i = new Indices();
 * i.toString(); // ""
 * i.push(1);
 * i.push(3);
 * i.toString(); // "[1][3]"
 * i.pop();
 * i.toString(); // "[1]"
 * i.replace(43);
 * i.toString(); // "[43]"
 * </pre>
 *
 * <p>The integers are not validated to be positive indices. Popping a value from an empty stack has
 * no effect.
 */
final class Indices {
  /** The size. */
  private int size;
  /** The elements. */
  private int[] elements;

  /**
   * Create an instance.
   */
  Indices() {
    // Handle up to 4D arrays by default
    elements = new int[4];
  }

  /**
   * Get the current number of elements.
   *
   * @return the size
   */
  int size() {
    return size;
  }

  /**
   * Returns true if the size is zero.
   *
   * @return true if empty
   */
  boolean isEmpty() {
    return size == 0;
  }

  /**
   * Push an element onto the stack.
   *
   * @param v the value
   */
  void push(int v) {
    int s;
    int[] e;
    if ((s = size) == (e = elements).length) {
      // Simple double in size.
      elements = e = Arrays.copyOf(e, size << 1);
    }
    e[s] = v;
    size = s + 1;
  }

  /**
   * Remove an element from the stack. Returns -1 if empty.
   *
   * @return the value
   */
  int pop() {
    final int s = size;
    return s == 0 ? -1 : elements[--size];
  }

  /**
   * Replace the top element in the stack.
   *
   * @param v the value
   */
  void replace(int v) {
    final int s = size - 1;
    if (s >= 0) {
      elements[s] = v;
    }
  }

  /**
   * Append the string representation to the given StringBuilder.
   *
   * @param sb the StringBuilder
   * @return the StringBuilder
   */
  StringBuilder append(StringBuilder sb) {
    final int s = size;
    if (s != 0) {
      // 10 chars for a positive integer, plus 2 for the square brackets
      sb.ensureCapacity(12 * s);
      final int[] e = elements;
      sb.append('[');
      for (int i = 0; i < s; i++) {
        if (i != 0) {
          // This is used in place of adding a length 2 string "][" as it may
          // be more performant.
          sb.append(']').append('[');
        }
        sb.append(e[i]);
      }
      sb.append(']');
    }
    return sb;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    if (size == 0) {
      return "";
    }
    return append(new StringBuilder()).toString();
  }
}
