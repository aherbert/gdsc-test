GDSC Test
=========

GDSC Test provides utility functionality for writing Java tests.

[![Build Status](https://travis-ci.com/aherbert/gdsc-test.svg?branch=master)](https://travis-ci.com/aherbert/gdsc-test)
[![Coverage Status](https://coveralls.io/repos/github/aherbert/gdsc-test/badge.svg?branch=master)](https://coveralls.io/github/aherbert/gdsc-test?branch=master)
[![Documentation Status](https://readthedocs.org/projects/gdsc-test/badge/?version=latest)](https://gdsc-test.readthedocs.io/en/latest/?badge=latest)
[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

Functionality includes:

- Relative equality assertions
- Dynamic messages implementing `Supplier<String>`
- Configurable random seed

[JUnit 5](https://junit.org/junit5/) relative equality example:

```java
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
```

Dynamic messages example:

```java
import uk.ac.sussex.gdsc.test.utils.functions.IndexSupplier;

final int dimensions = 2;
final IndexSupplier s = new IndexSupplier(dimensions);
System.out.println(s.get());
s.setMessagePrefix("Index: ");
s.set(0, 23); // Set index 0
s.set(1, 14); // Set index 1
System.out.println(s.get());
```

[JUnit 5](https://junit.org/junit5/) seeded test example:

```java
import uk.ac.sussex.gdsc.test.junit5.SeededTest;

// A repeated parameterised test with run-time configurable seed and repeats
@SeededTest
public void testSomethingRandom(RandomSeed seed) {
    long seed = seed.getSeed();
    // Do the test with a seeded random source ...
}
```

Configure the seed and repeats, e.g. for Maven:

        mvn test -Dgdsc.test.seed=12345 -Dgdsc.test.repeats=5

Documentation
-------------

See the latest documentation on [ReadTheDocs](https://gdsc-test.readthedocs.io).

Maven Installation
------------------

This package is a library to be used used by other Java programs. It is only
necessary to perform an install if you are building other packages that depend
on it.

1. Clone the repository

        git clone https://github.com/aherbert/gdsc-test.git

2. Build the code and install using Maven

        cd gdsc-test
        mvn install

    This will produce a gdsc-test-[package]-[VERSION].jar file in the local Maven
    repository.


# About #

###### Owner ######
Alex Herbert

###### Institution ######
[Genome Damage and Stability Centre, University of Sussex](http://www.sussex.ac.uk/gdsc/)
