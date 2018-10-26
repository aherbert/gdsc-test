.. index:: why all the primitives, predicate, library
.. _why:

Why all the primitives?
=======================

Short answer: To provide **bi-valued predicates for primitives** for use in testing.

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

and ``LongPredicate`` for ``long`` primitive type.

The Java language can natively convert ``float`` to ``double`` and ``byte``, ``char``, ``int``,
and ``short`` to ``long``. So the standard ``DoublePredicate`` and ``LongPredicate`` can be used
to test all primitive types except ``boolean``. This can be supported with a simple cast to 1 or 0.
Thus *Java predicates already supports testing single-valued primitives*.

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

An example is that differences for ``int`` require using ``long`` to prevent overflow and
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

They copy the functionality of Java's ``Predicate`` by supporting default methods for
``negate`` and the logical combination using ``or`` and ``and`` but also add ``xor``.
However in contrast to the Java version these default interface methods return concrete classes
and not lambda expressions. This is to support a feature required for testing where classes
implementing the interface also implement ``Supplier<String>`` to provide a description.
Thus logical combination predicates can provide a logical combination of the description of
their composed predicates.

Note that the library duplicates ``DoublePredicate`` and ``LongPredicate``. The GDSC Test
versions do not extend their respective Java versions. This is due to the GDSC Test predicates
all extending a marker interface that is used to support certain methods of the test API. Java
prevents multiple inheritance in interfaces.

Code Generation
---------------

Given that most predicates are very simple the GDSC predicate library is auto-generated from
templates. This makes supporting all the primitives easier.

Generation uses the `String Template <http://www.stringtemplate.org/>`_ library.
