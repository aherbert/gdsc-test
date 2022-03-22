.. index:: why all the primitives, predicate, library
.. _why:

Why all the primitives?
=======================

Short answer: To provide **bi-valued predicates for primitives** for use in testing, in particular
testing relative equality of nested arrays::

    @Test
    void testNestedArrays() {
        DoubleDoubleBiPredicate equal = Predicates.doublesAreRelativelyClose(1e-3);
        double[][] expected = {
            {1, 2, 30},
            {4, 5, 6},
        };
        double[][] actual = {
            {1, 2, 30.01},
            {4.0001, 5, 6},
        };
        TestAssertions.assertArrayTest(expected, actual, equal);
    }


Java Predicates
---------------

Java supports testing of primitives from ``java.util.function`` using::

    @FunctionalInterface
    public interface DoublePredicate {

        /**
         * Evaluates this predicate on the given argument.
         *
         * @param value the input argument
         * @return {@code true} if the input argument matches the predicate,
         * otherwise {@code false}
         */
        boolean test(double value);

        // Default methods
    }

for the ``double`` primitive type and a similar ``IntPredicate`` and ``LongPredicate`` for the
``int`` and ``long`` primitive types.

The Java language can natively convert ``float`` to ``double`` and ``byte``, ``char``, and ``short``
to ``int``. So the standard Java predicates can be used
to test all primitive types except ``boolean``. This can be supported with a simple cast to 1 or 0.
Thus *Java predicates already support testing single-valued primitives*.

In the case of bi-valued predicates Java only provides ``BiPredicate<T, U>``::

    @FunctionalInterface
    public interface BiPredicate<T, U> {

        /**
         * Evaluates this predicate on the given arguments.
         *
         * @param t the first input argument
         * @param u the second input argument
         * @return {@code true} if the input arguments match the predicate,
         * otherwise {@code false}
         */
        boolean test(T t, U u);

        // Default methods
    }

Primitives must be tested via the auto-boxing of primitives in their respective class.

A Predicate Library
-------------------

The motivation for the library was to provide **bi-valued predicates for primitives** for use in
testing. By providing predicates for all primitives it ensures:

- The test is explicit
- The test can assume a range for the value and natural behaviour
- ``boolean`` is supported

An example is that the maximum difference between ``int`` values requires using ``long`` to prevent overflow and
``long`` differences require using ``BigInteger``.

The predicate library provides the following generic interfaces::

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
``long`` and ``short``.

The predicates copy the functionality of Java's ``java.util.function.Predicate`` by supporting
default methods for ``negate`` and the logical combination using ``or`` and ``and``
but also add ``xor``.
However in contrast to the Java version these default interface methods return concrete classes
and not lambda expressions. This is to support a feature useful for testing where classes
implementing the interface also implement ``Supplier<String>`` to provide a description.
Thus logical combination predicates can provide a logical combination of the description of
their composed predicates.

Note that the library duplicates ``DoublePredicate``, ``IntPredicate`` and ``LongPredicate``.
The GDSC Test versions do not extend their respective Java versions. This avoids a confusing API
where predicates do not function identically due to argument types to default methods
(``or`` and ``and``) either accepting ``java.util.function`` predicates or GDSC Test predicates.

Since these are functional interfaces it is easy to convert between the two using a method
reference to the ``test`` method::

    final int answer = 42;
    uk.ac.sussex.gdsc.test.api.function.IntPredicate isAnswer1 = v -> v == answer;
    java.util.function.IntPredicate isAnswer2 = isAnswer1::test;
    uk.ac.sussex.gdsc.test.api.function.IntPredicate isAnswer3 = isAnswer2::test;

This allows using the GDSC test predicates within the standard Java framework::

    uk.ac.sussex.gdsc.test.api.function.IntPredicate isEven = v -> (v & 1) == 0;
    long even = IntStream.of(1, 2, 3).filter(isEven::test).count();


Code Generation
---------------

The simple and repetitive code for most predicates in the library is auto-generated from
templates.

Generation uses the `String Template <http://www.stringtemplate.org/>`_ library.
