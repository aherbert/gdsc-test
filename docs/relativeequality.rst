.. index:: relative equality
.. _relativeequality:

Relative Equality
=================

Definition
----------

The GDSC Test library contains assertion functionality for relative equality between numbers.

The implementation of relative equality expresses the difference :math:`\delta`
between :math:`a` and :math:`b`:

.. math::

    \delta = |a-b|

relative to the magnitude of :math:`a` and/or :math:`b`.

A **symmetric** relative error is:

.. math::

    e = \frac { |a-b| } { \max(|a|, |b|) }

This term is identical if :math:`a` and :math:`b` are switched.

The equivalent **asymmetric** relative error :math:`e` is: 

.. math::

    e_a = \frac { |a-b| } { |a| }

This is the distance that :math:`b` is from :math:`a`, relative to :math:`a`.

Testing Relative Equality
-------------------------

A simple test for relative equality may check that the equality is below a maximum threshold
:math:`e_{max}`. For example a **symmetric** test of relative equality can be:

.. math::

    \frac { |a-b| } { \max(|a|, |b|) } \leq e_{max}

Note that this will fail if both values are zero due to division by zero. It can be rearranged
to avoid this:

.. math::

    |a-b| \leq \max(|a|, |b|) \times e_{max}

The expression is a test that the distance between two values is less than an error, relative to
the largest magnitude. This is a test for convergence of two values.

The equivalent **asymmetric** test:

.. math::

    |a-b| \leq |a| \times e_{max}

is a test that the distance between two values is less than an error, relative to :math:`a`. Since
the test is asymmetric it can be used when :math:`a` is the known (expected) value and :math:`b`
is an actual value to be tested, i.e. test if the actual value is close to the expected value using
a relative error.

Value Around Zero
-----------------

The relative equality uses the magnitude of the values to define the scale.

.. math::

    e &= \frac { |a-b| } { max(|a|,|b|) }

    e_a &= \frac { |a-b| } { |a| }

In the limit :math:`|a| = 0` then :math:`e_a = \infty` but :math:`e = 1`.

The **asymmetric** relative error is thus unbounded.

The limit for the **symmetric** relative error is `2` when :math:`a` and :math:`b` are
of equal magnitudes and opposite signs, e.g.

.. math::

    a &= 1

    b &= -1

    e &= \frac { |a-b| } { \max(|a|, |b|) }

    e &= \frac { 2 } { 1 }

This allows the relative error argument to be checked that it falls within the
allowed range :math:`0 \leq e \leq 2`.

Testing Close to Zero
---------------------

The relative error increases as one of the test values approaches zero. The following demonstrates
this for the **symmetric** relative error:

.. math::

    a &= 0.01

    b &= 0.00001

    e &= \frac { |a-b| } { \max(|a|, |b|) }

    e &= \frac { 0.00999 } { 0.01 }

    e &= 0.999

The relative error :math:`e` will be large (:math:`0.999`) although
the absolute difference :math:`\delta` will be small (:math:`0.00999`).

This can be handled by allowing the :math:`\delta` to be tested against a relative error
threshold :math:`rel_{max}` **or** an absolute error threshold :math:`abs_{max}` :

.. math::

    |a-b| &\leq \max(|a|, |b|) \times rel_{max}

    |a-b| &\leq abs_{max}

Test Predicates
---------------

The GDSC Test library contains predicates that test relative equality between
two values.

Support is provided for **symmetric** relative equality using the name **AreClose**
which does not imply a direction. Support is provided for **asymmetric** relative equality
using the name **IsCloseTo** which implies a direction.

These can be constructed using a helper class::

    double relativeError = 0.01;

    DoubleDoubleBiPredicate areClose = Predicates.doublesAreClose(relativeError);

    // The AreClose relative equality is symmetric
    assert areClose.test(100, 99) : "Difference 1 should be <= 0.01 of 100";
    assert areClose.test(99, 100) : "Difference 1 should be <= 0.01 of 100";

    // The test identifies large relative error
    assert !areClose.test(10, 9) : "Difference 1 should not be <= 0.01 of 10";
    assert !areClose.test(9, 10) : "Difference 1 should not be <= 0.01 of 10";


    DoubleDoubleBiPredicate isCloseTo = Predicates.doublesIsCloseTo(relativeError);

    // The IsCloseTo relative equality is asymmetric
    assert isCloseTo.test(100, 99) : "Difference 1 should be <= 0.01 of 100";
    assert !isCloseTo.test(99, 100) : "Difference 1 should not be <= 0.01 of 99";

    // The test identifies large relative error
    assert !isCloseTo.test(10, 9) : "Difference 1 should not be <= 0.01 of 10";
    assert !isCloseTo.test(9, 10) : "Difference 1 should not be <= 0.01 of 9";

Note that the predicates can be constructed using an absolute error
tolerance which is combined with the relative equality test using an **Or** operator::

    double relativeError = 0.01;
    double absoluteError = 1;
    DoubleDoubleBiPredicate areClose = Predicates.doublesAreClose(relativeError, absoluteError);

    // This would fail using relative error.
    // The test passes using absolute error.
    assert areClose.test(10, 9) : "Difference 1 should be <= 1";
    assert areClose.test(9, 10) : "Difference 1 should be <= 1";

Test Framework Support
----------------------

Testing relative equality within a test framework is simplied using predicates. For example a
test for floating-point relative equality in ``JUnit 5`` must adapt the test for
absolute difference::

    double relativeError = 0.01;
    double expected = 100;
    double actual = 99;

    // equal within relative error of expected
    Assertions.assertEquals(expected, actual, Math.abs(expected) * relativeError);

This can be replaced with::

    double relativeError = 0.01;
    double expected = 100;
    double actual = 99;

    // equal within relative error of expected
    DoubleDoubleBiPredicate isCloseTo = Predicates.doublesIsCloseTo(relativeError);
    Assertions.assertTrue(isCloseTo.test(expected, actual));

This will identify errors but the error message is not helpful.

In order to provide useful error messages for a ``true/false`` predicate the
GDSC Test library contains a helper class for performing assertions that will raise
an ``AssertionError`` if the test is ``false``. The ``TestAssertions`` class is based on the
``Assertions`` design ideas of ``JUnit 5``. It provides static assertion methods for
pairs of all primitive types using any bi-valued test predicate to compare the two matched values.
Arrays and nested arrays are supported using recursion.

This allows the test for equality to be extended to arrays and nested arrays::

    double relativeError = 0.01;
    double expected = 100;
    double actual = 99;

    DoubleDoubleBiPredicate isCloseTo = Predicates.doublesIsCloseTo(relativeError);

    TestAssertions.assertTest(expected, actual, isCloseTo);

    // primitive arrays
    double[] expectedArray = new double[] { expected };
    double[] actualArray = new double[] { actual };
    TestAssertions.assertArrayTest(expectedArray, actualArray, isCloseTo);

    // nested primitive arrays of matched dimension
    Object[] expectedNestedArray = new double[][][] {{{ expected }}};
    Object[] actualNestedArray = new double[][][] {{{ actual }}};
    TestAssertions.assertArrayTest(expectedNestedArray, actualNestedArray, isCloseTo);

If the predicate test fails then the ``TestAssertions`` class will construct a message containing
the values that failed. Additionally all the predicates provided by the GDSC Test library
support a description that will be added to the ``AssertionError`` message.
