<package>

/**
 * Represents a predicate (boolean-valued function) of two primitive-valued arguments. This is a
 * {@code <type1>},{@code <type2>}-consuming primitive type specialisation of
 * {@link java.util.function.BiPredicate BiPredicate}.
 *
 * \<p>This is a {@link FunctionalInterface} whose functional method is
 * {@link #test(<type1>, <type2>)}.
 *
 * \<p>Note: This is a re-implementation of {@code java.util.function} primitive bi-predicates
 * to consume <type1> and <type2>, and add functionality including {@code xor} and inheritance of
 * the marker interface {@link PrimitiveBiPredicate}.
 *
 * @see java.util.function.BiPredicate
 * @see PrimitiveBiPredicate
 * @since 1.0
 */
@FunctionalInterface
public interface <Type1><Type2>BiPredicate extends PrimitiveBiPredicate {

  /**
   * Evaluates this predicate on the given arguments.
   *
   * @param value1 the first input value
   * @param value2 the second input value
   * @return {@code true} if the input arguments match the predicate, otherwise {@code false}
   */
  boolean test(<type1> value1, <type2> value2);

  /**
   * Returns a composed predicate that represents a short-circuiting logical AND of this predicate
   * and another. When evaluating the composed predicate, if this predicate is {@code false}, then
   * the {@code other} predicate is not evaluated.
   *
   * \<p>Any exceptions thrown during evaluation of either predicate are relayed to the caller; if
   * evaluation of this predicate throws an exception, the {@code other} predicate will not be
   * evaluated.
   *
   * @param other a predicate that will be logically-ANDed with this predicate
   * @return a composed predicate that represents the short-circuiting logical AND of this predicate
   *         and the {@code other} predicate
   * @throws NullPointerException if other is null
   */
  default <Type1><Type2>BiPredicate and(<Type1><Type2>BiPredicate other) {
    return new <Type1><Type2>AndBiPredicate(this, other);
  }

  /**
   * Returns a predicate that represents the logical negation of this predicate.
   *
   * @return a predicate that represents the logical negation of this predicate
   */
  default <Type1><Type2>BiPredicate negate() {
    return new <Type1><Type2>NegateBiPredicate(this);
  }

  /**
   * Returns a composed predicate that represents a short-circuiting logical OR of this predicate
   * and another. When evaluating the composed predicate, if this predicate is {@code true}, then
   * the {@code other} predicate is not evaluated.
   *
   * \<p>Any exceptions thrown during evaluation of either predicate are relayed to the caller; if
   * evaluation of this predicate throws an exception, the {@code other} predicate will not be
   * evaluated.
   *
   * @param other a predicate that will be logically-ORed with this predicate
   * @return a composed predicate that represents the short-circuiting logical OR of this predicate
   *         and the {@code other} predicate
   * @throws NullPointerException if other is null
   */
  default <Type1><Type2>BiPredicate or(<Type1><Type2>BiPredicate other) {
    return new <Type1><Type2>OrBiPredicate(this, other);
  }

  /**
   * Returns a composed predicate that represents a logical XOR of this predicate and another. When
   * evaluating the composed predicate both predicates must be evaulated.
   *
   * \<p>Any exceptions thrown during evaluation of either predicate are relayed to the caller.
   *
   * @param other a predicate that will be logically-XORed with this predicate
   * @return a composed predicate that represents the logical XOR of this predicate and the
   *         {@code other} predicate
   * @throws NullPointerException if other is null
   */
  default <Type1><Type2>BiPredicate xor(<Type1><Type2>BiPredicate other) {
    return new <Type1><Type2>XorBiPredicate(this, other);
  }
}
