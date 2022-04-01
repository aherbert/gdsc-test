Change Log
==========

.. contents::

Version 1.0.2
-------------

Patch release of GDSC Test.

.. list-table::
   :widths: 30 70
   :header-rows: 1

   * - Change
     - Description

   * - Fix
     - Avoid using a method reference to SplittableRandom.
       The method changes to a default implementation in JDK 17.

Version 1.0.1
-------------

Patch release of GDSC Test.

.. list-table::
   :widths: 30 70
   :header-rows: 1

   * - Change
     - Description

   * - Update
     - Update build tools to support JDK 17.

   * - Update
     - Remove redundant files from the gdsc-test-api jar.


Version 1.0
-----------

First working version of GDSC Test.

GDSC Test 1.0 contains the following modules:

- ``gdsc-test-build-tools`` (required only for building the source)
- ``gdsc-test-generator``
- ``gdsc-test-api``
- ``gdsc-test-utils``
- ``gdsc-test-junit5`` (requires JUnit 5)
- ``gdsc-test-rng`` (requires Commons RNG)
- ``gdsc-test-docs`` (documentation aggregator)
- ``gdsc-test-examples``

Requires Java 8.
