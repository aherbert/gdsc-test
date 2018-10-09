<package>

<! Explicit imports (not templated) so the sort order is correct for Checkstyle !>
import uk.ac.sussex.gdsc.test.api.equality.EqualityUtils;
import uk.ac.sussex.gdsc.test.api.function.BooleanBooleanBiPredicate;
import uk.ac.sussex.gdsc.test.api.function.BooleanPredicate;
import uk.ac.sussex.gdsc.test.api.function.ByteByteBiPredicate;
import uk.ac.sussex.gdsc.test.api.function.BytePredicate;
import uk.ac.sussex.gdsc.test.api.function.CharCharBiPredicate;
import uk.ac.sussex.gdsc.test.api.function.CharPredicate;
import uk.ac.sussex.gdsc.test.api.function.DoubleDoubleBiPredicate;
import uk.ac.sussex.gdsc.test.api.function.DoublePredicate;
import uk.ac.sussex.gdsc.test.api.function.FloatFloatBiPredicate;
import uk.ac.sussex.gdsc.test.api.function.FloatPredicate;
import uk.ac.sussex.gdsc.test.api.function.IntIntBiPredicate;
import uk.ac.sussex.gdsc.test.api.function.IntPredicate;
import uk.ac.sussex.gdsc.test.api.function.LongLongBiPredicate;
import uk.ac.sussex.gdsc.test.api.function.LongPredicate;
import uk.ac.sussex.gdsc.test.api.function.ShortPredicate;
import uk.ac.sussex.gdsc.test.api.function.ShortShortBiPredicate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link TestHelper}.
 */
public class TestHelperTest {
<types,Types,TypeValues:{type,Type,values |
<\n>
  /**
   * Test creates a predicate to compare a {@code <type>\} value to the reference value using the
   * {@code ==\} operator.
   */
  @Test
  public void testEquals<Type>() {
    final <type>[] values = { <values> \};
    for (<type> value1 : values) {
      final <Type>Predicate test = TestHelper.equals(value1);
      for (<type> value2 : values) {
        Assertions.assertEquals(value1 == value2, test.test(value2), 
            "Predicate does not compute ==");
      \}
    \}
  \}}><types,Types,TypeValues:{type,Type,values |
<\n>
  /**
   * Test creates a predicate to compare a {@code <type>\} values using the
   * {@code ==\} operator.
   */
  @Test
  public void testEqual<Type>s() {
    final <Type><Type>BiPredicate test = TestHelper.equal<Type>s();
    final <type>[] values = { <values> \};
    for (<type> value1 : values) {
      for (<type> value2 : values) {
        Assertions.assertEquals(value1 == value2, test.test(value1, value2), 
            "Predicate does not compute ==");
      \}
    \}
  \}}><floats,Floats,FloatValues:{float,Float,values |
<\n>
  /**
   * Test creates a predicate to compare a {@code <float>\} value to the reference value using
   * binary comparison.
   */
  @Test
  public void testCmp<Float>() {
    final <float>[] values = { <values> \};
    for (<float> value1 : values) {
      final <Float>Predicate test = TestHelper.cmp(value1);
      for (<float> value2 : values) {
        Assertions.assertEquals(<Float>.compare(value1, value2) == 0, test.test(value2), 
            "Predicate does not compute <Float>.compare");
      \}
    \}
  \}}><floats,Floats,FloatValues:{float,Float,values |
<\n>
  /**
   * Test creates a predicate to compare {@code <float>\} values using binary comparison.
   */
  @Test
  public void testCmp<Float>s() {
    final <Float><Float>BiPredicate test = TestHelper.cmp<Float>s();
    final <float>[] values = { <values> \};
    for (<float> value1 : values) {
      for (<float> value2 : values) {
        Assertions.assertEquals(<Float>.compare(value1, value2) == 0, test.test(value1, value2), 
            "Predicate does not compute <Float>.compare");
      \}
    \}
  \}}><floats,Floats,FloatValues:{float,Float,values |
<\n>
  /**
   * Test creates a predicate to compare {@code <float>\} values are almost equal.
   */
  @Test
  public void testAlmostEqual<Float>s() {
    final double relativeError = 0.5;
    final <float> absoluteError = 0;
    final <Float><Float>BiPredicate test = TestHelper.almostEqual<Float>s(relativeError, 
        absoluteError);
    final <float>[] values = { <values> \};
    for (<float> value1 : values) {
      for (<float> value2 : values) {
        Assertions.assertEquals(EqualityUtils.<float>sAreAlmostEqual(value1, value2,
            relativeError, absoluteError), test.test(value1, value2), 
            "Predicate does not compute almost equal");
      \}
    \}
  \}}>
}
