/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
 *
 * Contains utilities for use with test frameworks.
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

package uk.ac.sussex.gdsc.test.utils;

/**
 * Generates information on the call stack. This is done by generating a stack trace to obtain
 * information on the caller. This will be inefficient and should be used for debugging only, for
 * example calls to obtain the code point can be inserted into any method and logged to determine
 * the parts of code that is being executed:
 *
 * <pre>
 * void testMethod() {
 *   System.out.println(CallStack.getCodePoint());
 *   // Do something
 *   System.out.println(CallStack.getCodePoint());
 *   // Do something
 *   System.out.println(CallStack.getCodePoint());
 * }
 * </pre>
 */
public final class CallStack {

  /**
   * Do not allow public construction.
   */
  private CallStack() {}

  /**
   * Gets the code point (ClassName:MethodName:LineNumber) marking the position where this method
   * was called.
   *
   * <p>Note: This method will generate a stack trace to extract the required information.
   *
   * @return the code point
   */
  public static String getCodePoint() {
    final StackTraceElement element = getStackTraceElement_499ad503_0184_4099_bf36_65c73b4932d3(1);
    return getCodePoint(element);
  }

  /**
   * Gets the code point (ClassName:MethodName:LineNumber) from the stack trace element.
   *
   * <p>Returns an empty string if the element is null avoiding a NullPointerException if the
   * element is not available.
   *
   * @param element the stack trace element
   * @return the code point (or empty string if the element is null)
   */
  public static String getCodePoint(StackTraceElement element) {
    if (element == null) {
      return "";
    }
    return String.format("%s:%s:%d:", element.getClassName(), element.getMethodName(),
        element.getLineNumber());
  }

  /**
   * Gets the stack trace element marking the position where this method was called.
   *
   * <p>Note: This method will generate a stack trace to extract the required information.
   *
   * @return the stack trace element
   */
  public static StackTraceElement getStackTraceElement() {
    return getStackTraceElement_499ad503_0184_4099_bf36_65c73b4932d3(1);
  }

  /**
   * Gets the stack trace element marking the position a set count of elements before where this
   * method was called.
   *
   * <p>Note: This method will generate a stack trace to extract the required information.
   *
   * @param count the count
   * @return the stack trace element
   * @throws IllegalArgumentException if {@code count < 0}
   */
  public static StackTraceElement getStackTraceElement(int count) {
    if (count < 0) {
      throw new IllegalArgumentException("Count must be positive: " + count);
    }
    return getStackTraceElement_499ad503_0184_4099_bf36_65c73b4932d3(count + 1);
  }

  /**
   * Gets the stack trace element marking the call to this method in the stack trace. Optionally
   * offset this by a count of elements to locate a position before this method in the stack trace.
   *
   * @param count the count ({@code >=0})
   * @return the stack trace element
   */
  private static StackTraceElement
      getStackTraceElement_499ad503_0184_4099_bf36_65c73b4932d3(int count) {
    // Based on
    // https://stackoverflow.com/questions/17473148/dynamically-get-the-current-line-number/17473358
    int remaining = count;
    boolean thisMethod = false;
    for (final StackTraceElement element : Thread.currentThread().getStackTrace()) {
      if (thisMethod) {
        if (remaining == 0) {
          return element;
        }
        remaining--;
      } else if ("getStackTraceElement_499ad503_0184_4099_bf36_65c73b4932d3"
          .equals(element.getMethodName())) {
        thisMethod = true;
      }
    }
    return null;
  }
}
