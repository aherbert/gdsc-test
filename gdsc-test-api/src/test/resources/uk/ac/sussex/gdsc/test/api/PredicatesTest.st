<package>

<! Explicit imports (not templated) so the sort order is correct for Checkstyle !>
import uk.ac.sussex.gdsc.test.api.comparisons.BooleanEquals;
import uk.ac.sussex.gdsc.test.api.comparisons.ByteEquality;
import uk.ac.sussex.gdsc.test.api.comparisons.ByteEquals;
import uk.ac.sussex.gdsc.test.api.comparisons.CharEquals;
import uk.ac.sussex.gdsc.test.api.comparisons.DoubleEquality;
import uk.ac.sussex.gdsc.test.api.comparisons.DoubleEquals;
import uk.ac.sussex.gdsc.test.api.comparisons.FloatEquality;
import uk.ac.sussex.gdsc.test.api.comparisons.FloatEquals;
import uk.ac.sussex.gdsc.test.api.comparisons.IntEquality;
import uk.ac.sussex.gdsc.test.api.comparisons.IntEquals;
import uk.ac.sussex.gdsc.test.api.comparisons.LongEquality;
import uk.ac.sussex.gdsc.test.api.comparisons.LongEquals;
import uk.ac.sussex.gdsc.test.api.comparisons.ShortEquality;
import uk.ac.sussex.gdsc.test.api.comparisons.ShortEquals;
import uk.ac.sussex.gdsc.test.api.function.BooleanBooleanBiPredicate;
import uk.ac.sussex.gdsc.test.api.function.BooleanPredicate;
import uk.ac.sussex.gdsc.test.api.function.BooleanPredicateAndBooleanPredicate;
import uk.ac.sussex.gdsc.test.api.function.BooleanPredicateOrBooleanPredicate;
import uk.ac.sussex.gdsc.test.api.function.BooleanPredicateXorBooleanPredicate;
import uk.ac.sussex.gdsc.test.api.function.ByteByteBiPredicate;
import uk.ac.sussex.gdsc.test.api.function.BytePredicate;
import uk.ac.sussex.gdsc.test.api.function.BytePredicateAndBytePredicate;
import uk.ac.sussex.gdsc.test.api.function.BytePredicateOrBytePredicate;
import uk.ac.sussex.gdsc.test.api.function.BytePredicateXorBytePredicate;
import uk.ac.sussex.gdsc.test.api.function.CharCharBiPredicate;
import uk.ac.sussex.gdsc.test.api.function.CharPredicate;
import uk.ac.sussex.gdsc.test.api.function.CharPredicateAndCharPredicate;
import uk.ac.sussex.gdsc.test.api.function.CharPredicateOrCharPredicate;
import uk.ac.sussex.gdsc.test.api.function.CharPredicateXorCharPredicate;
import uk.ac.sussex.gdsc.test.api.function.DoubleDoubleBiPredicate;
import uk.ac.sussex.gdsc.test.api.function.DoublePredicate;
import uk.ac.sussex.gdsc.test.api.function.DoublePredicateAndDoublePredicate;
import uk.ac.sussex.gdsc.test.api.function.DoublePredicateOrDoublePredicate;
import uk.ac.sussex.gdsc.test.api.function.DoublePredicateXorDoublePredicate;
import uk.ac.sussex.gdsc.test.api.function.FloatFloatBiPredicate;
import uk.ac.sussex.gdsc.test.api.function.FloatPredicate;
import uk.ac.sussex.gdsc.test.api.function.FloatPredicateAndFloatPredicate;
import uk.ac.sussex.gdsc.test.api.function.FloatPredicateOrFloatPredicate;
import uk.ac.sussex.gdsc.test.api.function.FloatPredicateXorFloatPredicate;
import uk.ac.sussex.gdsc.test.api.function.IntIntBiPredicate;
import uk.ac.sussex.gdsc.test.api.function.IntPredicate;
import uk.ac.sussex.gdsc.test.api.function.IntPredicateAndIntPredicate;
import uk.ac.sussex.gdsc.test.api.function.IntPredicateOrIntPredicate;
import uk.ac.sussex.gdsc.test.api.function.IntPredicateXorIntPredicate;
import uk.ac.sussex.gdsc.test.api.function.LongLongBiPredicate;
import uk.ac.sussex.gdsc.test.api.function.LongPredicate;
import uk.ac.sussex.gdsc.test.api.function.LongPredicateAndLongPredicate;
import uk.ac.sussex.gdsc.test.api.function.LongPredicateOrLongPredicate;
import uk.ac.sussex.gdsc.test.api.function.LongPredicateXorLongPredicate;
import uk.ac.sussex.gdsc.test.api.function.ShortPredicate;
import uk.ac.sussex.gdsc.test.api.function.ShortPredicateAndShortPredicate;
import uk.ac.sussex.gdsc.test.api.function.ShortPredicateOrShortPredicate;
import uk.ac.sussex.gdsc.test.api.function.ShortPredicateXorShortPredicate;
import uk.ac.sussex.gdsc.test.api.function.ShortShortBiPredicate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link Predicates}.
 */
public class PredicatesTest {<types,Types:{type,Type |
<\n>
  /**
   * Test creates a predicate to compare a {@code <type>\} value to the reference value using the
   * {@code ==\} operator.
   */
  @Test
  public void testEquals<Type>() {
    final <type>[] values = ApiTests.get<Type>Values();
    for (<type> value1 : values) {
      final <Type>Predicate test = Predicates.<type>Equals(value1);
      for (<type> value2 : values) {
        Assertions.assertEquals(value1 == value2, test.test(value2),
            "Predicate does not compute ==");
      \}
    \}
  \}}><types,Types:{type,Type |
<\n>
  /**
   * Test creates a predicate to compare a {@code <type>\} values using the
   * {@code ==\} operator.
   */
  @Test
  public void testEqual<Type>s() {
    final <Type><Type>BiPredicate test = Predicates.<type>sEqual();
    final <type>[] values = ApiTests.get<Type>Values();
    for (<type> value1 : values) {
      for (<type> value2 : values) {
        Assertions.assertEquals(value1 == value2, test.test(value1, value2),
            "Predicate does not compute ==");
      \}
    \}
  \}}><floats,Floats:{float,Float |
<\n>
  /**
   * Test creates a predicate to compare a {@code <float>\} value to the reference value using
   * binary comparison.
   */
  @Test
  public void testBinaryEqual<Float>() {
    final <float>[] values = ApiTests.get<Float>Values();
    for (<float> value1 : values) {
      final <Float>Predicate test = Predicates.<float>BinaryEqual(value1);
      for (<float> value2 : values) {
        Assertions.assertEquals(<Float>.compare(value1, value2) == 0, test.test(value2),
            "Predicate does not compute <Float>.compare");
      \}
    \}
  \}}><floats,Floats:{float,Float |
<\n>
  /**
   * Test creates a predicate to compare {@code <float>\} values using binary comparison.
   */
  @Test
  public void testBinaryEqual<Float>s() {
    final <Float><Float>BiPredicate test = Predicates.<float>sBinaryEqual();
    final <float>[] values = ApiTests.get<Float>Values();
    for (<float> value1 : values) {
      for (<float> value2 : values) {
        Assertions.assertEquals(<Float>.compare(value1, value2) == 0, test.test(value1, value2),
            "Predicate does not compute <Float>.compare");
      \}
    \}
  \}}><floats,Floats,ulpTypes:{float,Float,ulpType |
<\n>
  /**
   * Test creates a predicate to compare {@code <float>\} values are within a ULP error.
   */
  @Test
  public void testAreWithinUlpUsing<Float>s() {
    final <ulpType> ulpError = 1;
    final <Float><Float>BiPredicate test = Predicates.<float>sAreWithinUlp(ulpError);
    final <float>[] values = ApiTests.get<Float>Values();
    for (<float> value1 : values) {
      for (<float> value2 : values) {
        Assertions.assertEquals(<Float>Equality.areWithinUlp(value1, value2, ulpError),
            test.test(value1, value2),
            "Predicate does not compute withinUlp");
      \}
    \}
  \}}><numbers,Numbers,NumberAbsTypes,numberAbsTypes:{number,Number,NumberAbsType,numberAbsType |
<\n>
  /**
   * Test creates a predicate to compare {@code <number>\} values are within a <numberAbsType>
   * absolute error.
   */
  @Test
  public void testAreWithin<Number>Using<NumberAbsType>s() {
    final <numberAbsType> absoluteError = 1;
    final <Number><Number>BiPredicate test = Predicates.<number>sAreWithin(absoluteError);
    final <number>[] values = ApiTests.get<Number>Values();
    for (<number> value1 : values) {
      for (<number> value2 : values) {
        Assertions.assertEquals(<Number>Equality.areWithin(value1, value2, absoluteError),
            test.test(value1, value2),
            "Predicate does not compute within");
      \}
    \}
  \}}><numbers,Numbers,NumberAbsTypes,numberAbsTypes:{number,Number,NumberAbsType,numberAbsType |
<\n>
  /**
   * Test creates a predicate to compare {@code <number>\} values are close.
   */
  @Test
  public void testAreClose<Number>s() {
    final double relativeError = 0.5;
    final <numberAbsType> absoluteError = 1;
    final <Number><Number>BiPredicate test1 = Predicates.<number>sAreClose(relativeError,
        absoluteError);
    final <Number><Number>BiPredicate test2 = Predicates.<number>sAreClose(relativeError);
    final <number>[] values = ApiTests.get<Number>Values();
    for (<number> value1 : values) {
      for (<number> value2 : values) {
        Assertions.assertEquals(<Number>Equality.areClose(value1, value2,
            relativeError, absoluteError), test1.test(value1, value2),
            "Predicate does not compute Close with relative and absolute error");
        Assertions.assertEquals(<Number>Equality.areClose(value1, value2,
            relativeError, 0), test2.test(value1, value2),
            "Predicate does not compute Close with relative error");
      \}
    \}
  \}}><numbers,Numbers,NumberAbsTypes,numberAbsTypes:{number,Number,NumberAbsType,numberAbsType |
<\n>
  /**
   * Test creates a predicate to compare a {@code <number>\} value is close to an expected value.
   */
  @Test
  public void testIsCloseTo<Number>s() {
    final double relativeError = 0.5;
    final <numberAbsType> absoluteError = 1;
    final <Number><Number>BiPredicate test1 = Predicates.<number>sIsCloseTo(relativeError,
        absoluteError);
    final <Number><Number>BiPredicate test2 = Predicates.<number>sIsCloseTo(relativeError);
    final <number>[] values = ApiTests.get<Number>Values();
    for (<number> value1 : values) {
      for (<number> value2 : values) {
        Assertions.assertEquals(<Number>Equality.isCloseTo(value1, value2,
            relativeError, absoluteError), test1.test(value1, value2),
            "Predicate does not compute IsCloseTo with relative and absolute error");
        Assertions.assertEquals(<Number>Equality.isCloseTo(value1, value2,
            relativeError, 0), test2.test(value1, value2),
            "Predicate does not compute IsCloseTo with relative error");
      \}
    \}
  \}}><Ops,ops,operators:{Op,op,operator |<types,Types:{type,Type |
<\n>
  /**
   * Test creates a predicate to test {@code <type>\} values using a logical {@code <Op>\} of two
   * single-value <type> predicates.
   */
  @Test
  public void test<Op><Type>Predicate<Type>Predicate() {
    final <type>[] values = ApiTests.get<Type>Values();
    final <Type>Predicate predicate1 = new <Type>Equals(values[0]);
    final <Type>Predicate predicate2 = new <Type>Equals(values[1]);
    final <Type><Type>BiPredicate predicate = Predicates.<op>(predicate1, predicate2);
    Assertions.assertEquals(<Type>Predicate<Op><Type>Predicate.class, predicate.getClass());
    for (<type> value1 : values) {
      for (<type> value2 : values) {
        // Test the two predicates are passed through to <Type>Predicate<Op><Type>Predicate
        final boolean expected = predicate1.test(value1) <operator> predicate2.test(value2);
        final boolean actual = predicate.test(value1, value2);
        Assertions.assertEquals(expected, actual, "Combined predicate does not compute <op>");
        // Do not test the description using get() as that is tested in the test
        // for <Type>Predicate<Op><Type>Predicate.
      \}
    \}
  \}}>}><Floats,floats:{Float,float |<Specials:{Special |
<\n>
  /**
   * Test creates a predicate to test a {@code <float>\} value is <Special>.
   */
  @Test
  public void test<Float>Is<Special>Predicate() {
    final <float>[] values = ApiTests.get<Float>Values();
    final <Float>Predicate predicate = Predicates.<float>Is<Special>();
    for (<float> value : values) {
      final boolean expected = <Float>.is<Special>(value);
      final boolean actual = predicate.test(value);
      Assertions.assertEquals(expected, actual, "Predicate does not compute <Float>.is<Special>");
    \}
  \}}>}>
}
