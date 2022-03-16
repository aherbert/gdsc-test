/*-
 * #%L
 * Genome Damage and Stability Centre Test Examples
 *
 * Contains examples of the GDSC Test libraries.
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

package uk.ac.sussex.gdsc.test.examples;

import java.util.stream.IntStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.ac.sussex.gdsc.test.api.TestAssertions;
import uk.ac.sussex.gdsc.test.api.Predicates;
import uk.ac.sussex.gdsc.test.api.function.DoubleDoubleBiPredicate;
import uk.ac.sussex.gdsc.test.api.function.IntIntBiPredicate;

/**
 * Contains demonstration code for the API package.
 *
 * <p>Code snippets from this class are used in the documentation, thus this class exists to ensure
 * the snippets are valid. Documentation should be updated appropriately when this class is updated.
 */
class ApiTest {

  /**
   * Test the Close predicate.
   */
  @Test
  void testClosePredicate() {
    double relativeError = 0.01;
    DoubleDoubleBiPredicate areClose = Predicates.doublesAreClose(relativeError);

    // The AreClose relative equality is symmetric
    assert areClose.test(100, 99) : "Difference 1 should be <= 0.01 of 100";
    assert areClose.test(99, 100) : "Difference 1 should be <= 0.01 of 100";

    // The test identifies large relative error
    assert !areClose.test(10, 9) : "Difference 1 should not be <= 0.01 of 10";
    assert !areClose.test(9, 10) : "Difference 1 should not be <= 0.01 of 10";
  }

  /**
   * Test the IscloseTo predicate.
   */
  @Test
  void testIsCloseToPredicate() {
    double relativeError = 0.01;
    DoubleDoubleBiPredicate isCloseTo = Predicates.doublesIsCloseTo(relativeError);

    // The IsCloseTo relative equality is asymmetric
    assert isCloseTo.test(100, 99) : "Difference 1 should be <= 0.01 of 100";
    assert !isCloseTo.test(99, 100) : "Difference 1 should not be <= 0.01 of 99";

    // The test identifies large relative error
    assert !isCloseTo.test(10, 9) : "Difference 1 should not be <= 0.01 of 10";
    assert !isCloseTo.test(9, 10) : "Difference 1 should not be <= 0.01 of 9";
  }

  /**
   * Test the Close predicate using absolute error.
   */
  @Test
  void testClosePredicateUsingAbsError() {
    double relativeError = 0.01;
    double absoluteError = 1;
    DoubleDoubleBiPredicate areClose = Predicates.doublesAreClose(relativeError, absoluteError);

    // This would fail using relative error.
    // The test passes using absolute error.
    assert areClose.test(10, 9) : "Difference 1 should be <= 1";
    assert areClose.test(9, 10) : "Difference 1 should be <= 1";
  }

  /**
   * Test the IsCloseTo predicate within a test framework.
   */
  @Test
  void testIsCloseToWithinFramework() {
    double relativeError = 0.01;
    double expected = 100;
    double actual = 99;

    // equal within relative error of expected
    Assertions.assertEquals(expected, actual, Math.abs(expected) * relativeError);

    // replace with predicate
    DoubleDoubleBiPredicate isCloseTo = Predicates.doublesIsCloseTo(relativeError);
    Assertions.assertTrue(isCloseTo.test(expected, actual));
  }

  /**
   * Test the IsCloseTo predicate with TestAssertions.
   */
  @Test
  void testIsCloseToWithTestAssertions() {
    double relativeError = 0.01;
    double expected = 100;
    double actual = 99;

    DoubleDoubleBiPredicate isCloseTo = Predicates.doublesIsCloseTo(relativeError);

    TestAssertions.assertTest(expected, actual, isCloseTo);

    // primitive arrays
    double[] expectedArray = new double[] {expected};
    double[] actualArray = new double[] {actual};
    TestAssertions.assertArrayTest(expectedArray, actualArray, isCloseTo);

    // nested primitive arrays of matched dimension
    Object[] expectedNestedArray = new double[][][] {{{expected}}};
    Object[] actualNestedArray = new double[][][] {{{actual}}};
    TestAssertions.assertArrayTest(expectedNestedArray, actualNestedArray, isCloseTo);
  }

  /**
   * Test the AreWithinUlp predicate with TestAssertions.
   */
  @Test
  void testAreWithinUlpWithTestAssertions() {
    int ulpError = 1;
    double expected = 100;
    double actual = Math.nextUp(expected);

    DoubleDoubleBiPredicate areWithinUlp = Predicates.doublesAreWithinUlp(ulpError);

    TestAssertions.assertTest(expected, actual, areWithinUlp);
    TestAssertions.assertTest(expected, Math.nextUp(actual), areWithinUlp.negate());
  }

  /**
   * Test matrix recursion.
   */
  @Test
  void testMatrixRecursion() {
    IntIntBiPredicate equal = Predicates.intsEqual();
    Object[] expected = new int[4][5][6];
    Object[] actual = new int[4][5][6];
    TestAssertions.assertArrayTest(expected, actual, equal);
  }

  /**
   * Test nested arrays.
   */
  @Test
  void testNestedArrays() {
    DoubleDoubleBiPredicate equal = Predicates.doublesAreClose(1e-3);
    double[][] expected = {
        {1, 2, 30},
        {4, 5, 6},
    };
    double[][] actual = {
        {1, 2, 30.01},
        {4.0001, 5, 6},
    };
    TestAssertions.assertArrayTest(expected, actual, equal);
  }

  /**
   * Test predicate conversion.
   */
  @Test
  void testPredicateConversion() {
    final int answer = 42;
    uk.ac.sussex.gdsc.test.api.function.IntPredicate isAnswer1 = v -> v == answer;
    java.util.function.IntPredicate isAnswer2 = isAnswer1::test;
    uk.ac.sussex.gdsc.test.api.function.IntPredicate isAnswer3 = isAnswer2::test;
    for (final int value : new int[] {2, answer}) {
      Assertions.assertEquals(isAnswer1.test(value), isAnswer2.test(value));
      Assertions.assertEquals(isAnswer1.test(value), isAnswer3.test(value));
    }

    uk.ac.sussex.gdsc.test.api.function.IntPredicate isEven = v -> (v & 1) == 0;
    long even = IntStream.of(1, 2, 3).filter(isEven::test).count();
    Assertions.assertEquals(1, even);
  }
}
