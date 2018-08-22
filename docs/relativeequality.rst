.. index:: relative, equality
.. _relativeequality:

Relative Equality
=====

Definition
-----

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

Relative error can be expressed in Java as::

    boolean doublesAreEqualRelative(double a,
                                    double b,
                                    double relativeError) {
        double delta = Math.abs(a - b);
        double max = Math.max(Math.abs(a), Math.abs(b));
        return delta <= max * relativeError;
    }

Value around zero
-----

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

JUnit Support
-----

The GDSC Test library contains ``assertEqualsRelative`` and ``assertEqualsRelativeOrAbsolute``
to complement the standard JUnit ``assertEquals`` function.

This removes the need to use code such as::

    double expected, actual;
    Assertions.assertEquals(expected, actual, expected * 1e-3);

Replacing it with::

    ExtraAssertions.assertEqualsRelative(expected, actual, 1e-3);

The support extends to using arrays::

    double[] expected, actual;
    ExtraAssertions.assertArrayEqualsRelative(expected, actual, 1e-3);

and object arrays that are nested ``double[]`` or ``float[]`` primitive arrays::

    Object[] expected = new double[x][y][z];
    Object[] actual = new double[x][y][z];
    ExtraAssertions.assertArrayEqualsRelative(expected, actual, 1e-3);

This would not be supported in JUnit without loop constructs::

    double[] expected, actual;
    for (int i=0; i < expected.length; i++)
        Assertions.assertEquals(expected[i], actual[i],
                                expected[i] * 1e-3);

JUnit 5
-----

`JUnit 5 <https://junit.org/junit5/>`_ support is within the module ``gdsc-test-junit5``
that contains the package ``uk.ac.sussex.gdsc.test.junit5``.

JUnit 4
-----

`JUnit 4 <https://junit.org/junit4/>`_ support is within the module ``gdsc-test-junit4``
that contains the package ``uk.ac.sussex.gdsc.test.junit4``.
