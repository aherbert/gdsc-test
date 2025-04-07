GDSC Test
=========

GDSC Test provides utility functionality for writing Java tests.

[![License: Apache 2.0](https://img.shields.io/badge/License-Apache%20v2-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Build Status](https://github.com/aherbert/gdsc-test/actions/workflows/build.yml/badge.svg)](https://github.com/aherbert/gdsc-test/actions/workflows/build.yml)
[![Coverage Status](https://codecov.io/gh/aherbert/gdsc-test/branch/master/graph/badge.svg)](https://app.codecov.io/gh/aherbert/gdsc-test)
[![Documentation Status](https://readthedocs.org/projects/gdsc-test/badge/?version=latest)](https://gdsc-test.readthedocs.io/en/latest/?badge=latest)
[![Maven Central](https://img.shields.io/maven-central/v/uk.ac.sussex.gdsc/gdsc-test-api)](https://search.maven.org/artifact/uk.ac.sussex.gdsc/gdsc-test-api/)
[![Javadocs](https://javadoc.io/badge2/uk.ac.sussex.gdsc/gdsc-test-api/javadoc.svg)](https://javadoc.io/doc/uk.ac.sussex.gdsc/gdsc-test-api)

Functionality includes:

- Predicate library for single- or bi-valued test predicates using Java primitives
- Assertion utilities for asserting predicates
- Dynamic messages implementing `Supplier<String>`
- Configurable random seed utilities

Documentation
-------------

See the latest documentation on [ReadTheDocs](https://gdsc-test.readthedocs.io).

Predicate Library
-----------------

The GDSC predicate library is an extension of the ``java.util.function``
primitive predicates ``DoublePredicate`` and ``LongPredicate`` to all java
primitives. These are functional interfaces for single or bi-valued predicates,
for example for ``int`` primitives:

```Java
@FunctionalInterface
public interface IntPredicate {
    boolean test(int value);
    // default methods
}

@FunctionalInterface
public interface IntIntBiPredicate {
    boolean test(int value1, int value2);
    // default methods
}
```
Standard predicates are provided to test equality and closeness within an
`absolute` or `relative` error for use in a testing framework.

Assertion Utilities
-------------------

Support for testing using a test framework is provided with a utility class that
will test primitive value(s) using a single or bi-valued predicate. An
``AssertionError`` is generated when a test fails. For example a test for
relative equality:

```Java
import uk.ac.sussex.gdsc.test.api.TestAssertions;
import uk.ac.sussex.gdsc.test.api.Predicates;

@Test
public void testRelativeEquality() {
    double relativeError = 0.01;
    double expected = 100;
    double actual = 99;

    DoubleDoubleBiPredicate areClose = Predicates.doublesAreRelativelyClose(relativeError);

    // This will pass as 99 is within (0.01*100) of 100
    TestAssertions.assertTest(expected, actual, areClose);
}
```

All provided implementations of the ``TypePredicate`` or ``TypeTypeBiPredicate``
interface implement ``Supplier<String>`` to provide a text description of the predicate. This is used to format an error message for the failed test.

Nested arrays are supported using recursion allowing testing of matrices:

```Java
IntIntBiPredicate equal = Predicates.intsAreEqual();
Object[] expected = new int[4][5][6];
Object[] actual = new int[4][5][6];
TestAssertions.assertArrayTest(expected, actual, equal);
```

Installation
------------

This package is a library to be used used by other Java programs.
The latest version is on Maven central:

[![Maven Central](https://img.shields.io/maven-central/v/uk.ac.sussex.gdsc/gdsc-test-api)](https://search.maven.org/artifact/uk.ac.sussex.gdsc/gdsc-test-api/)

The package can be installed locally from the source using Maven.

        git clone https://github.com/aherbert/gdsc-test.git
        cd gdsc-test
        mvn install

Background
----------

Developed at the
[Genome Damage and Stability Centre, University of Sussex](http://www.sussex.ac.uk/gdsc/).
