/*-
 * #%L
 * Genome Damage and Stability Centre Test Examples
 *
 * Contains examples of the GDSC Test libraries.
 * %%
 * Copyright (C) 2018 Alex Herbert
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

package uk.ac.sussex.gdsc.test.examples;

import java.util.Objects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Tests Math operations that form the base for predicates.
 */
public class MathTest {

  /**
   * Test the extremes of math operations used in relative and absolute error.
   *
   * <p>This test demonstrates that differences involving at least one Infinity term result in an
   * infinite (or NaN) difference. This is not a valid difference and so Infinity is never close to
   * anything.
   */
  @Test
  public void testDoubleError() {

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
  public void printDeltaTable() {

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
