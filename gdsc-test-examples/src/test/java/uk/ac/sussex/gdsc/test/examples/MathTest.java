/*-
 * #%L
 * Genome Damage and Stability Centre Test Examples
 *
 * Contains examples of the GDSC Test libraries.
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

package uk.ac.sussex.gdsc.test.examples;

import java.util.Objects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Tests Math operations that form the base for predicates.
 */
class MathTest {

  /**
   * Test the extremes of math operations used in relative and absolute error.
   *
   * <p>This test demonstrates that differences involving at least one Infinity term result in an
   * infinite (or NaN) difference. This is not a valid difference and so Infinity is never close to
   * anything.
   */
  @Test
  void testDoubleError() {

    // No test for operations involving NaN: they are always NaN
    Assertions.assertTrue(Objects.equals(Double.NaN, Double.NaN), "NaN does not equal NaN");

    // -Infinity difference:
    // Always -Infinity or NaN
    assertDelta(Double.NaN, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
    assertDelta(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, -Double.MAX_VALUE);
    assertDelta(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, -1);
    assertDelta(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, -Double.MIN_VALUE);
    assertDelta(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, -0);
    assertDelta(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 0);
    assertDelta(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.MIN_VALUE);
    assertDelta(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 1);
    assertDelta(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.MAX_VALUE);
    assertDelta(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

    // , -Infinity difference
    // Always , -Infinity or NaN
    assertDelta(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
    assertDelta(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, -Double.MAX_VALUE);
    assertDelta(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, -1);
    assertDelta(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, -Double.MIN_VALUE);
    assertDelta(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, -0);
    assertDelta(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, 0);
    assertDelta(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.MIN_VALUE);
    assertDelta(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, 1);
    assertDelta(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.MAX_VALUE);
    assertDelta(Double.NaN, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);

    double[] values = new double[] {Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,
        -Double.MAX_VALUE, -1, -Double.MIN_VALUE, -0, 0, Double.MIN_VALUE, 1, Double.MAX_VALUE};

    // Any divide by infinity is signed 0 or NaN (if infinite)
    for (double v1 : values) {
      double expected = Double.isInfinite(v1) ? Double.NaN : Math.signum(v1) * 0.0;
      Assertions.assertEquals(expected, v1 / Double.POSITIVE_INFINITY, () -> v1 + " / +Infinity");
      Assertions.assertEquals(-expected, v1 / Double.NEGATIVE_INFINITY, () -> v1 + " / -Infinity");
    }
  }

  private static void assertDelta(double delta, double v1, double v2) {
    Assertions.assertEquals(delta, v1 - v2, () -> v1 + " - " + v2);
  }

  /**
   * Print a table of absolute and relative error between double values.
   */
  @Disabled("This is here for documentation")
  @Test
  void printDeltaTable() {

    double[] values = new double[] {Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,
        -Double.MAX_VALUE, -1, -Double.MIN_VALUE, -0, 0, Double.MIN_VALUE, 1, Double.MAX_VALUE};

    // Print delta table
    System.out.println("v1, v2, v1-v2, |v1-v2|, |v1-v2|/v1");
    for (double v1 : values) {
      for (double v2 : values) {
        double delta = v1 - v2;
        double absDelta = Math.abs(delta);
        double relativeError = absDelta / Math.abs(v1);
        System.out.printf("%s, %s, %s, %s, %s%n", v1, v2, delta, absDelta, relativeError);
      }
    }
  }
}
