GDSC Test Documentation
=====

.. image:: https://travis-ci.com/aherbert/gdsc-test.svg?branch=master
    :target: https://travis-ci.com/aherbert/gdsc-test
    :alt: Build Status

.. image:: https://coveralls.io/repos/github/aherbert/gdsc-test/badge.svg?branch=master
    :target: https://coveralls.io/github/aherbert/gdsc-test?branch=master
    :alt: Coverage Status

.. image:: https://img.shields.io/badge/License-GPL%20v3-blue.svg
    :target: https://www.gnu.org/licenses/gpl-3.0
    :alt: License: GPL v3

GDSC Test provides utility functionality for writing Java tests including:

- Relative equality assertions
- Dynamic messages implementing ``Supplier<String>``
- Configurable random seed

:Note: The GDSC Test library works for Java 1.8+
:Documentation: `On ReadTheDocs <http://gdsc-test.readthedocs.io/en/master>`_
:GitHub: `On github <https://github.com/aherbert/gdsc-test>`_

Relative Equality
====

Support for relative equality is provided for `JUnit 4 <https://junit.org/junit4/>`_
and `JUnit 5 <https://junit.org/junit5/>`_, for example::

    import org.junit.jupiter.api.Test;
    import uk.ac.sussex.gdsc.test.junit5.ExtraAssertions;

    @Test
    public void testRelativeEquality() {
        // Pass
        ExtraAssertions.assertEqualsRelative(1000, 1001, 1e-3);
        ExtraAssertions.assertEqualsRelativeOrAbsolute(1000, 1001,
                                                       1e-6, 1);
        // Fail
        ExtraAssertions.assertEqualsRelative(1000, 1001, 1e-6);
    }

Dynamic Messages
=====

Support for dynamic messages using ``Supplier<String>`` suppliers.
These store updatable arguments to pass to ``String.format(String, Object...)``
for an error message, for example::

    import uk.ac.sussex.gdsc.test.utils.functions.IndexSupplier;

    final int dimensions = 2;
    final IndexSupplier s = new IndexSupplier(dimensions);
    System.out.println(s.get());
    s.setMessagePrefix("Index: ");
    s.set(0, 23); // Set index 0
    s.set(1, 14); // Set index 1
    System.out.println(s.get());

Reports:

.. code-block:: console

    [0][0]
    Index: [23][14]

Configurable Random Seed
=====

Support for test randomness is provided using a single ``long`` seed::

    import uk.ac.sussex.gdsc.test.utils.TestSettings;

    long seed = TestSettings.getSeed();

If not set then the seed will be a randomly generated ``long``. This is logged
using ``java.util.logging.Logger`` at the ``Level.INFO`` level so failed tests
can be repeated by setting the seed using a Java property:

.. code-block:: console

    -Dgdsc.test.seed=12345

Support for seeded tests is provided using `JUnit 5 <https://junit.org/junit5/>`_::

    import uk.ac.sussex.gdsc.test.junit5.SeededTest;

    // A repeated parameterised test with run-time configurable seed
    @SeededTest
    public void testSomethingRandom(RandomSeed seed) {
        long seed = seed.getSeed();
        // Do the test with a seeded random source ...
    }

The ``@SeededTest`` is a ``@RepeatedTest`` and the number of repeats can be
set using a Java property:

.. code-block:: console

    -Dgdsc.test.repeats=5

An example implementation for test randomness is provided using
`Commons RNG <https://commons.apache.org/rng/>`_, for example::

    import org.apache.commons.rng.UniformRandomProvider;
    import uk.ac.sussex.gdsc.test.junit5.SeededTest;
    import uk.ac.sussex.gdsc.test.junit5.RandomSeed;
    import uk.ac.sussex.gdsc.test.rng.RNGFactory;

    // A repeated parameterised test with run-time configurable seed
    @SeededTest
    public void testSomethingRandom(RandomSeed seed) {
        UniformRandomProvider rng = RNGFactory.create(seed.getSeed());
        // Do the test ...
    }

Modular Design
=====

GDSC Test is separated into different packages so that only the desired
functionality can be included as a project dependency.

================================= ===========
Package                           Description
================================= ===========
``uk.ac.sussex.gdsc.test.utils``  Utilities
``uk.ac.sussex.gdsc.test.junit5`` JUnit5 assertions and assumptions
``uk.ac.sussex.gdsc.test.junit4`` JUnit4 assertions and assumptions
``uk.ac.sussex.gdsc.test.rng``    Commons RNG
================================= ===========

Contents
=====

.. toctree::
    :numbered:
    :maxdepth: 1

    installation.rst
    relativeequality.rst

Issues
=====

Please fill bug report in https://github.com/aherbert/gdsc-test/issues.

.. toctree::
    :hidden:

    changelog.rst

Indices and tables
=====

* :ref:`genindex`
* :ref:`search`
