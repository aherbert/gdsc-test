Change Log
==========

.. contents::

Version 1.2
-----------

Minor release of GDSC Test.

.. list-table::
   :widths: 30 70
   :header-rows: 1

   * - Change
     - Description

   * - Update
     - Add documented default constructors to public classes without an explicit constructor.

   * - Update
     - Publish to Maven Central using the Central Publisher Portal.

   * - Update
     - Update the build plugins and the code generator dependencies (Commons CSV 1.14.1,
       Commons Lang 3.20.0, picocli 4.7.7).

   * - Update
     - Log a warning when a ``gdsc.test`` system property has an invalid value and is ignored.

   * - Fix
     - Correct the documentation. The random source example uses ``RngFactory``.

   * - Fix
     - Absolute and relative error comparisons evaluate the difference between the values
       exactly. The relative limit is computed using double precision. This is consistent for
       all primitive types. Infinite ``double`` and ``float`` values are not close.

   * - Fix
     - ``LongEquality`` relative error comparisons support a difference between the values
       that overflows a signed ``long``.

Version 1.1
-------------

Minor release of GDSC Test.

.. list-table::
   :widths: 30 70
   :header-rows: 1

   * - Change
     - Description

   * - Fix
     - Correct decode of hex char sequence containing a lower-case 'g' character.

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
