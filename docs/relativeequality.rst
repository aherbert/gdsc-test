.. index:: relative, equality
.. _relativeequality:

Relative Equality
=================

Definition
----------

The GDSC Test library contains assertion functionality for relative equality between
floating point numbers.

The implementation of relative equality is based on convergence criteria for floating point
numbers, i.e. express the difference :math:`\delta` between :math:`a` and :math:`b`:

.. math::

    \delta = |a-b|

as a relative error :math:`e`:

.. math::

    e = \frac { |a-b| } { \max(|a|, |b|) }

Rearranging this allows an assertion of relative equality using a tolerance :math:`e`:

.. math::

    |a-b| \leq \max(|a|, |b|) \times e

It should be noted that the relative error and equality assertion are symmetric,
i.e. they are identical if :math:`a` and :math:`b` are switched.

Relative error can be expressed in Java as::

    boolean doublesAreEqualRelative(double a,
                                    double b,
                                    double relativeError) {
        double delta = Math.abs(a - b);
        double max = Math.max(Math.abs(a), Math.abs(b));
        return delta <= max * relativeError;
    }

Value around zero
-----------------

Note that relative equality uses the maximum of :math:`a` or :math:`b` to define the scale.
This produces different results from:

.. math::

    e_a &= \frac { |a-b| } { |a| }

    e_b &= \frac { |a-b| } { |b| }

as either :math:`a` or :math:`b` approach zero. In the limit :math:`|a| = 0` then
:math:`e_a = \infty` but :math:`e = 1`.

The limit for the convergence relative error is `2` when :math:`a` and :math:`b` are
of equal magnitudes and opposite signs, e.g.

.. math::

    a &= 1

    b &= -1

    e &= \frac { |a-b| } { \max(|a|, |b|) }

    e &= \frac { 2 } { 1 }

This allows the relative error argument to be checked that it falls within the
allowed range :math:`0 \leq e \leq 2`.

Despite this smaller maximum range the relative error can still increase
dramatically above an assertion level (e.g. :math:`10^{-3}`) as one
of the test values approaches zero:

.. math::

    a &= 0.01

    b &= 0.00001

    e &= \frac { |a-b| } { \max(|a|, |b|) }

    e &= \frac { 0.00999 } { 0.01 }

    e &= 0.999

The relative error :math:`e` will be large (:math:`0.999`) although
the absolute difference :math:`\delta` will be small (:math:`0.00999`).

This can be handled by adding a fixed delta below which the two values are
considered equal::

    boolean doublesAreEqualRelativeOrAbsolute(double a,
                                              double b,
                                              double relativeError,
                                              double absoluteError) {
        double delta = Math.abs(a - b);
        if (delta <= absoluteError)
            return true;
        double max = Math.max(Math.abs(a), Math.abs(b));
        return delta <= max * relativeError;
    }

Test Support
------------

The GDSC Test library contains predicates that test relative equality between
two ``float`` or ``double`` values. These can be constructed using a helper
class::

    double relativeError = 1e-4;
    double absoluteError = 0;
    DoubleDoubleBiPredicate predicate = TestHelper.almostEqualDoubles(
        relativeError, absoluteError);

    predicate.test(9999.0, 10000.0); // true since 1/10000 <= 1e-4
    predicate.test(999.0,  1000.0);  // false since 1/1000  > 1e-4

This removes the need to use code such as::

    double expected, actual;
    double relativeError = 1e-3;

    // equal within relative delta of expected
    Assertions.assertEquals(expected, actual, expected * relativeError);

Replacing it with::

    double expected, actual;
    double relativeError = 1e-3;
    DoubleDoubleBiPredicate predicate = TestHelper.almostEqualDoubles(
        relativeError, 0);

    // equal within relative error
    Assertions.assertTrue(predicate.test(expected, actual));

In order to provide useful error messages for a ``true/false`` predicate the
GDSC Test library contains a helper class for performing assertions. This can
test on input primitives and primitive arrays using any predicate.

This allows the test for equality to be extended to arrays and nested arrays::

    DoubleDoubleBiPredicate predicate = TestHelper.almostEqualDoubles(
        relativeError, 0);

    // primitives
    double expected = ...;
    double actual = ...;
    TestAssertions.assertTest(expected, actual, predicate);

    // primitive arrays
    double[] expected = ...;
    double[] actual = ...;
    TestAssertions.assertArrayTest(expected, actual, predicate);

    // nested primitive arrays of matched dimension (e.g. x,y,z)
    Object[] expected = new double[x][y][z];
    Object[] actual = new double[x][y][z];
    TestAssertions.assertArrayTest(expected, actual, predicate);

Note that a relative delta for arrays is not natively supported in JUnit
without loop constructs::

    double[] expected, actual;
    for (int i=0; i < expected.length; i++) {
      Assertions.assertEquals(expected[i], actual[i],
                              expected[i] * 1e-3);
    }
