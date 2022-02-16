GDSC Test Documentation
=======================

.. image:: https://travis-ci.com/aherbert/gdsc-test.svg?branch=master
    :target: https://travis-ci.com/aherbert/gdsc-test
    :alt: Build Status

.. image:: https://coveralls.io/repos/github/aherbert/gdsc-test/badge.svg?branch=master
    :target: https://coveralls.io/github/aherbert/gdsc-test?branch=master
    :alt: Coverage Status

.. image:: https://readthedocs.org/projects/gdsc-test/badge/?version=latest
    :target: https://gdsc-test.readthedocs.io/en/latest/?badge=latest
    :alt: Documentation Status

.. image:: https://img.shields.io/badge/License-GPL%20v3-blue.svg
    :target: https://www.gnu.org/licenses/gpl-3.0
    :alt: License: GPL v3

GDSC Test provides utility functionality for writing Java tests including:

- Predicate library for single- or bi-valued test predicates using Java primitives
- Assertion utilities for asserting predicates
- Dynamic messages implementing ``Supplier<String>``
- Configurable random seed utilities

Note: The GDSC Test library works for Java 1.8+.

The code is hosted on `GitHub <https://github.com/aherbert/gdsc-test>`_.

Predicate Library
=================

The GDSC predicate library is an extension of the ``java.util.function`` primitive predicates
``DoublePredicate`` and ``LongPredicate`` to all java primitives. These are functional interfaces
for single or bi-valued predicates providing the following::

    @FunctionalInterface
    public interface TypePredicate {
        boolean test(type value);
        // default methods
    }

    @FunctionalInterface
    public interface TypeTypeBiPredicate {
        boolean test(type value1, type value2);
        // default methods
    }

where ``<type>`` is one of: ``boolean``, ``byte``, ``char``, ``double``, ``float``, ``int``,
``long`` and ``short`` (see `why all the primitives? <why.html>`_).

The predicate functional interfaces provide:

- The logical ``or``, ``and`` and ``xor`` operations with other predicates
- Negation to an opposite test

Standard predicates are provided to test equality and closeness within an absolute or relative
error (see `Relative Equality <relativeequality.html>`_). Floating-point numbers can be compared
with a units in the last place (`ULP <https://en.wikipedia.org/wiki/Unit_in_the_last_place>`_)
tolerance.

Combined predicates exists to combine two single-valued predicates into a bi-valued predicate that
tests each input value with a distinct predicate.

Assertion Utilities
===================

Support for testing using a test framework is provided with a utility class that will test
primitive value(s) using a single or bi-valued predicate. An ``AssertionError`` is generated when
a test fails. For example a test for relative equality::

    import uk.ac.sussex.gdsc.test.api.TestAssertions;
    import uk.ac.sussex.gdsc.test.api.TestHelper;

    @Test
    public void testRelativeEquality() {
        double relativeError = 0.01;
        double expected = 100;
        double actual = 99;

        DoubleDoubleBiPredicate isCloseTo = TestHelper.doublesIsCloseTo(relativeError);

        // This will pass as 99 is within (0.01*100) of 100
        TestAssertions.assertTest(expected, actual, isCloseTo);
    }

A test for equality with units in the last place (ULP)::

    import uk.ac.sussex.gdsc.test.api.TestAssertions;
    import uk.ac.sussex.gdsc.test.api.TestHelper;

    @Test
    public void testUlpEquality() {
        int ulpError = 1;
        double expected = 100;
        double actual = Math.nextUp(expected);

        DoubleDoubleBiPredicate areWithinUlp = TestHelper.doublesAreWithinUlp(ulpError);

        TestAssertions.assertTest(expected, actual, areWithinUlp);
        TestAssertions.assertTest(expected, Math.nextUp(actual), areWithinUlp.negate());
    }


All provided implementations of the ``TypePredicate`` or ``TypeTypeBiPredicate`` interface
implement ``Supplier<String>`` to provide a text description of the predicate. This is used to
format an error message for the failed test.

Nested arrays are supported using recursion allowing testing of matrices::

    IntIntBiPredicate equal = TestHelper.intsEqual();
    Object[] expected = new int[4][5][6];
    Object[] actual = new int[4][5][6];
    TestAssertions.assertArrayTest(expected, actual, equal);

Dynamic Messages
================

Support is provided for dynamic messages using ``Supplier<String>`` suppliers.
These store updatable arguments to pass to ``String.format(String, Object...)``
for an error message, for example::

    import uk.ac.sussex.gdsc.test.utils.functions.IndexSupplier;

    final int dimensions = 2;
    final IndexSupplier msg = new IndexSupplier(dimensions);
    System.out.println(msg.get());
    msg.setMessagePrefix("Index: ");
    msg.set(0, 23); // Set index 0
    msg.set(1, 14); // Set index 1
    System.out.println(msg.get());

Reports:

.. code-block:: console

    [0][0]
    Index: [23][14]

All setters return the message as a ``Supplier<String>`` for testing within loops that require
a message. For example using JUnit 5::

    import uk.ac.sussex.gdsc.test.utils.functions.IndexSupplier;

    int dimensions = 2;
    IndexSupplier message = new IndexSupplier(dimensions);
    message.setMessagePrefix("Index ");
    // The message will supply "Index [i][j]" for the assertion, e.g.
    message.set(0, 42);
    Assertions.assertEquals("Index [42][3]", message.set(1, 3).get());

    int size = 5;
    int[][] matrix = new int[size][size];
    for (int i = 0; i < size; i++) {
        message.set(0, i);
        for (int j = 0; j < size; j++) {
            // The message will supply "Index [i][j]" for the assertion
            Assertions.assertEquals(0, matrix[i][j], message.set(1, j));
        }
    }

Configurable Random Seed
========================

Support for test randomness is provided using a single ``byte[]`` seed::

    import uk.ac.sussex.gdsc.test.utils.TestSettings;

    byte[] seed = TestSettings.getSeed();

A ``SeedUtils`` class is provided to convert the ``byte[]`` to ``int[]``, ``long[]`` or ``long``.

The seed can be set using a Hex-encoded string property:

.. code-block:: console

    -Dgdsc.test.seed=123456789abcdf

If not set then the seed will be randomly generated using ``java.security.SecureRandom`` and logged
using the configurable ``java.util.logging.Logger``. This allows failed tests to be repeated by
re-using the generated seed that triggered a test failure.

Extra support for seeded tests is provided for `JUnit 5 <https://junit.org/junit5/>`_ using a
custom ``@SeededTest`` annotation::

    import uk.ac.sussex.gdsc.test.junit5.SeededTest;
    import uk.ac.sussex.gdsc.test.utils.RandomSeed;

    // A repeated parameterised test with run-time configurable seed
    // and repeats
    @SeededTest
    public void testSomethingRandom(RandomSeed seed) {
        long value = seed.getAsLong();
        // Do the test with a seeded random source ...
    }

The ``@SeededTest`` is a ``@RepeatedTest``. Each repeat will have a unique random seed. The number
of repeats can be set using a Java property:

.. code-block:: console

    -Dgdsc.test.repeats=5

An example implementation for test randomness is provided using
`Apache Commons RNG <https://commons.apache.org/rng/>`_ client API ``UniformRandomProvider``,
for example::

    import uk.ac.sussex.gdsc.test.junit5.SeededTest;
    import uk.ac.sussex.gdsc.test.rng.RngUtils;
    import uk.ac.sussex.gdsc.test.utils.RandomSeed;

    import org.apache.commons.rng.UniformRandomProvider;

    // A repeated parameterised test with run-time configurable seed
    // and repeats
    @SeededTest
    public void testSomethingRandom(RandomSeed seed) {
        UniformRandomProvider rng = RngUtils.create(seed.get());
        // Do the test ...
    }


Modular Design
==============

GDSC Test is separated into different packages so that the desired
functionality can be included as a project dependency.

================================= ===========
Package                           Description
================================= ===========
``uk.ac.sussex.gdsc.test.api``    Predicates and assertions
``uk.ac.sussex.gdsc.test.utils``  Utilities for error messages, logging and timing
``uk.ac.sussex.gdsc.test.junit5`` JUnit5 annotations
``uk.ac.sussex.gdsc.test.rng``    Provides a configurable random source
================================= ===========

Contents
========

.. toctree::
    :numbered:
    :maxdepth: 1

    installation.rst
    why.rst
    relativeequality.rst

Issues
======

Please fill bug report in https://github.com/aherbert/gdsc-test/issues.

.. toctree::
    :hidden:

    changelog.rst

Indices and tables
==================

* :ref:`genindex`
* :ref:`search`
