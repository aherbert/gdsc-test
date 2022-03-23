<package>

/**
 * Represents a predicate (boolean-valued function) of one {@code <type>}-valued
 * argument. This is a {@code <type>}-consuming primitive type specialisation of
 * {@link java.util.function.Predicate Predicate}.
 *
 * \<p>This is a {@link FunctionalInterface} whose functional method is
 * {@link #test(<type>)}.
 *
 * \<p>Note: This is a copy implementation of {@link java.util.function.LongPredicate}
 * to consume <type> and add functionality including {@code xor} and inheritance of
 * the marker interface {@link PrimitivePredicate}.
 *
 * @see java.util.function.Predicate
 * @since 1.0
 */
@FunctionalInterface
public interface <Type>Predicate extends PrimitivePredicate {

  /**
   * Evaluates this predicate on the given argument.
   *
   * @param value the input argument
   * @return {@code true} if the input argument matches the predicate, otherwise {@code false}
   */
  boolean test(<type> value);

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
  default <Type>Predicate and(<Type>Predicate other) {
    return new <Type>AndPredicate(this, other);
  }

  /**
   * Returns a predicate that represents the logical negation of this predicate.
   *
   * @return a predicate that represents the logical negation of this predicate
   */
  default <Type>Predicate negate() {
    return new <Type>NegatePredicate(this);
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
  default <Type>Predicate or(<Type>Predicate other) {
    return new <Type>OrPredicate(this, other);
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
  default <Type>Predicate xor(<Type>Predicate other) {
    return new <Type>XorPredicate(this, other);
  }
}
