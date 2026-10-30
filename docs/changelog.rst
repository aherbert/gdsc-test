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

   * - Update
     - ``L64X128M`` saves the full generator state to support restoring the state to a different
       instance, as allowed by ``RestorableUniformRandomProvider``.

   * - Update
     - Document the ULP comparison of infinite values, the use of the seed bytes by
       ``RngFactory.create(byte[])``, and exceptions thrown for null or invalid arguments.

   * - Fix
     - Correct the documentation. The random source example uses ``RngFactory``.

   * - Fix
     - Correct the documentation of the absolute error validation for numeric comparisons and
       predicates. The absolute error must be positive and finite for ``double`` and ``float``;
       positive for ``long``; and positive and below the maximum difference between values for
       ``int``, ``short``, ``byte`` and ``long`` using a ``BigInteger`` error.

   * - Fix
     - Correct documentation errors in examples, method descriptions and the ``CodeGenerator``
       exit codes.

   * - Fix
     - Relative error comparisons of ``double`` and ``float`` values return false if either
       value is infinite. The difference between the values and the relative limit are computed
       without overflow.

   * - Fix
     - ``LongEquality`` relative error comparisons support a difference between the values
       that overflows a signed ``long``.

   * - Fix
     - ``TestAssertions.assertArrayTest`` applies the predicate when the expected and actual
       arrays are the same object. Null expected and actual arrays fail the assertion.

   * - Fix
     - ``AssertionErrorCounter`` counts failures atomically to support tests run in parallel.
       Previously failures could be lost if the same test index was used by multiple threads
       concurrently; use of a dedicated index per thread was not affected.

   * - Fix
     - ``TestLogging`` records created with a message supplier or object can be serialised
       when the message contains a format specifier, e.g. ``%``. Previously the message was
       used as the format string of the deserialised record. Records created with a format and
       arguments can be serialised when the arguments are not serialisable.

   * - Fix
     - ``TestSettings.getSeed`` generates a single seed when called concurrently and no seed
       is set. Previously concurrent calls could generate and return different seeds.

   * - Fix
     - Restoring the state of a generator created by ``RngFactory`` throws an
       ``IllegalArgumentException`` if the state was saved by a different type of generator.

   * - Fix
     - ``CodeGenerator`` supports templates in the source root directory (default package).
       A null value (``\N``) for a class name substitution is reported as an invalid model.

   * - Fix
     - ``CodeGenerator`` reads the string-template files and the header file using UTF-8 to
       match the encoding of the generated code. Previously the platform default encoding was
       used.

   * - Fix
     - ``CodeGenerator`` reports a malformed substitution value, e.g. an unclosed quote, as an
       invalid model. Any other unexpected error returns the ``FAILED`` exit code; previously the
       exception was not caught and the exit code matched the ``HELP`` exit code.

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
