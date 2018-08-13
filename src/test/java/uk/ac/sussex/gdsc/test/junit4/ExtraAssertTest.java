/*-
 * #%L
 * Genome Damage and Stability Centre Test Package
 *
 * The GDSC Test package contains code for use with the JUnit test framework.
 * %%
 * Copyright (C) 2018 Alex Herbert
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package uk.ac.sussex.gdsc.test.junit4;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("javadoc")
public class ExtraAssertTest
{
    // Testing of relative error.

    // Observed and expected
    static double doubleL = 1;
    static double doubleH = 2;
    static double relativeError;
    // Create next up/down for each that changes the relative error
    static double doubleLU, doubleLD, doubleHU, doubleHD;

    // Since the relative error still uses a double the float values
    // are computed using the double relative error.
    static float floatL = (float) doubleL;
    static float floatH = (float) doubleH;
    // Create next up/down for each that changes the relative error
    static float floatLU, floatLD, floatHU, floatHD;

    static
    {
        // Note the relative error is diff / max(e, o).
        doubleLU = doubleLD = doubleL;
        doubleHU = doubleHD = doubleH;
        relativeError = relativeError(doubleL, doubleH);
        while (relativeError(doubleLU, doubleH) == relativeError)
            doubleLU = Math.nextUp(doubleLU);
        while (relativeError(doubleLD, doubleH) == relativeError)
            doubleLD = Math.nextDown(doubleLD);
        while (relativeError(doubleHU, doubleL) == relativeError)
            doubleHU = Math.nextUp(doubleHU);
        while (relativeError(doubleHD, doubleL) == relativeError)
            doubleHD = Math.nextDown(doubleHD);

        // Since the relative error still uses a double the float values
        // are computed using the double relative error.
        floatLU = floatLD = floatL;
        floatHU = floatHD = floatH;
        while (relativeError(floatLU, floatH) == relativeError)
            floatLU = Math.nextUp(floatLU);
        while (relativeError(floatLD, floatH) == relativeError)
            floatLD = Math.nextDown(floatLD);
        while (relativeError(floatHU, floatL) == relativeError)
            floatHU = Math.nextUp(floatHU);
        while (relativeError(floatHD, floatL) == relativeError)
            floatHD = Math.nextDown(floatHD);
    }

    static double relativeError(double e, double o)
    {
        // Assumes o and e are positive
        return Math.abs(o - e) / Math.max(o, e);
    }

    static double relativeError(float e, float o)
    {
        // This method is required to ensure float arithmetic in computing the error

        // Assumes o and e are positive
        return Math.abs(o - e) / Math.max(o, e);
    }

    /** The fixed message. */
    private final String message = "Fixed message";
    /** An empty message. */
    private final String emptyMessage = "";

    // XXX - Copy from here
    @Test
    public void testAssertEqualsRelativeDouble()
    {
        double e = doubleL;
        double o = doubleL;
        ExtraAssert.assertEqualsRelative(e, o, Double.MIN_VALUE);

        o = doubleH;
        ExtraAssert.assertEqualsRelative(e, o, relativeError);

        e = doubleLU;
        o = doubleH;
        ExtraAssert.assertEqualsRelative(e, o, relativeError);

        e = doubleL;
        o = doubleHD;
        ExtraAssert.assertEqualsRelative(e, o, relativeError);
    }

    @Test
    public void testAssertEqualsRelativeDoubleThrowsWithMessage()
    {
        final double e = 1;
        final double o = 2;
        try
        {
            ExtraAssert.assertEqualsRelative(message, e, o, Double.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(message));
        }
        try
        {
            ExtraAssert.assertEqualsRelative(message, o, e, Double.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(message));
        }
        try
        {
            ExtraAssert.assertEqualsRelative(null, e, o, Double.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
        try
        {
            ExtraAssert.assertEqualsRelative(null, o, e, Double.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
        try
        {
            ExtraAssert.assertEqualsRelative(emptyMessage, e, o, Double.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
        try
        {
            ExtraAssert.assertEqualsRelative(emptyMessage, o, e, Double.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
    }

    @Test
    public void testAssertEqualsRelativeDoubleErrorsUp()
    {
        // Move further apart they should be not equal
        final double e = doubleL;
        final double o = doubleHU;
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertEqualsRelative(e, o, relativeError);
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertEqualsRelative(message, e, o, relativeError);
        });
    }

    @Test
    public void testAssertEqualsRelativeDoubleErrorsDown()
    {
        // Move further apart they should be not equal
        final double e = doubleLD;
        final double o = doubleH;
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertEqualsRelative(e, o, relativeError);
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertEqualsRelative(message, e, o, relativeError);
        });
    }

    @Test
    public void testAssertNotEqualsRelativeDouble()
    {
        // Not equals should be OK
        final double e = doubleLD;
        final double o = doubleH;
        ExtraAssert.assertNotEqualsRelative(e, o, relativeError);
        ExtraAssert.assertNotEqualsRelative(message, e, o, relativeError);

        // If not equal this should throw
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertEqualsRelative(e, o, relativeError);
        });

        final double e2 = doubleL;
        final double o2 = doubleHU;
        ExtraAssert.assertNotEqualsRelative(e2, o2, relativeError);
        ExtraAssert.assertNotEqualsRelative(message, e2, o2, relativeError);
    }

    @Test
    public void testAssertNotEqualsRelativeDoubleThrowsWithMessage()
    {
        final double e = 1;
        final double o = 1;
        try
        {
            ExtraAssert.assertNotEqualsRelative(message, e, o, Double.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(message));
        }
        try
        {
            ExtraAssert.assertNotEqualsRelative(message, o, e, Double.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(message));
        }
        try
        {
            ExtraAssert.assertNotEqualsRelative(null, e, o, Double.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
        try
        {
            ExtraAssert.assertNotEqualsRelative(null, o, e, Double.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
        try
        {
            ExtraAssert.assertNotEqualsRelative(emptyMessage, e, o, Double.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
        try
        {
            ExtraAssert.assertNotEqualsRelative(emptyMessage, o, e, Double.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
    }

    @Test
    public void testAssertNotEqualsRelativeDoubleErrors()
    {
        // If equal this should throw
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertNotEqualsRelative(doubleL, doubleH, relativeError);
        });
        // Check it is equal
        ExtraAssert.assertEqualsRelative(doubleL, doubleH, relativeError);
    }

    @Test
    public void testAssertArrayEqualsRelativeDouble()
    {
        double[] e = new double[] { doubleL };
        // Check with self
        ExtraAssert.assertArrayEqualsRelative(e, e, Double.MIN_VALUE);
        ExtraAssert.assertArrayEqualsRelative(message, e, e, Double.MIN_VALUE);
        ExtraAssert.assertArrayEqualsRelative(emptyMessage, e, e, Double.MIN_VALUE);

        double[] o = new double[] { doubleL };
        ExtraAssert.assertArrayEqualsRelative(e, o, Double.MIN_VALUE);
        ExtraAssert.assertArrayEqualsRelative(message, o, e, Double.MIN_VALUE);
        ExtraAssert.assertArrayEqualsRelative(emptyMessage, o, e, Double.MIN_VALUE);

        o = new double[] { doubleH };
        ExtraAssert.assertArrayEqualsRelative(e, o, relativeError);
        ExtraAssert.assertArrayEqualsRelative(message, o, e, relativeError);
        ExtraAssert.assertArrayEqualsRelative(emptyMessage, o, e, relativeError);

        e = new double[] { doubleLU };
        o = new double[] { doubleH };
        ExtraAssert.assertArrayEqualsRelative(e, o, relativeError);
        ExtraAssert.assertArrayEqualsRelative(message, o, e, relativeError);
        ExtraAssert.assertArrayEqualsRelative(emptyMessage, o, e, relativeError);

        e = new double[] { doubleL };
        o = new double[] { doubleHD };
        ExtraAssert.assertArrayEqualsRelative(e, o, relativeError);
        ExtraAssert.assertArrayEqualsRelative(message, o, e, relativeError);
        ExtraAssert.assertArrayEqualsRelative(emptyMessage, o, e, relativeError);
    }

    @Test
    public void testAssertArrayEqualsRelativeDoubleThrowsWithFormattedMessage()
    {
        final double[] e = new double[] { 1 };
        final double[] o = new double[] { 2 };
        try
        {
            ExtraAssert.assertArrayEqualsRelative(message, e, o, Double.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(message));
        }
        try
        {
            ExtraAssert.assertArrayEqualsRelative(message, o, e, Double.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(message));
        }
        try
        {
            ExtraAssert.assertArrayEqualsRelative(null, e, o, Double.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
        try
        {
            ExtraAssert.assertArrayEqualsRelative(null, o, e, Double.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
        try
        {
            ExtraAssert.assertArrayEqualsRelative(emptyMessage, e, o, Double.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
        try
        {
            ExtraAssert.assertArrayEqualsRelative(emptyMessage, o, e, Double.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
    }

    @Test
    public void testAssertArrayEqualsRelativeDoubleErrorsUp()
    {
        // Move further apart they should be not equal
        final double[] e = new double[] { doubleL };
        final double[] o = new double[] { doubleHU };
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertArrayEqualsRelative(e, o, relativeError);
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertArrayEqualsRelative(message, e, o, relativeError);
        });
    }

    @Test
    public void testAssertArrayEqualsRelativeDoubleErrorsDown()
    {
        // Move further apart they should be not equal
        final double[] e = new double[] { doubleLD };
        final double[] o = new double[] { doubleH };
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertArrayEqualsRelative(e, o, relativeError);
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertArrayEqualsRelative(message, e, o, relativeError);
        });
    }

    @Test
    public void testAssertArrayEqualsRelativeDoubleThrowsWithNullArrays()
    {
        final double[] e = new double[] { 1 };
        final double[] o = null;
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertArrayEqualsRelative(e, o, relativeError);
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertArrayEqualsRelative(message, e, o, relativeError);
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertArrayEqualsRelative(o, e, relativeError);
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertArrayEqualsRelative(message, o, e, relativeError);
        });
    }

    @Test
    public void testAssertArrayEqualsRelativeDoubleThrowsWithMismatchedArrays()
    {
        final double[] e = new double[] { 1 };
        final double[] o = new double[] { 1, 2 };
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertArrayEqualsRelative(e, o, relativeError);
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertArrayEqualsRelative(message, e, o, relativeError);
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertArrayEqualsRelative(o, e, relativeError);
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertArrayEqualsRelative(message, o, e, relativeError);
        });
    }

    @Test
    public void testAssertObjectArrayEqualsRelativeDouble()
    {
        double[][][] e = new double[][][] { { { doubleL } } };
        // Check with self
        ExtraAssert.assertDoubleArrayEqualsRelative(e, e, Double.MIN_VALUE);
        ExtraAssert.assertDoubleArrayEqualsRelative(message, e, e, Double.MIN_VALUE);
        ExtraAssert.assertDoubleArrayEqualsRelative(emptyMessage, e, e, Double.MIN_VALUE);

        double[][][] o = new double[][][] { { { doubleL } } };
        ExtraAssert.assertDoubleArrayEqualsRelative(e, o, Double.MIN_VALUE);
        ExtraAssert.assertDoubleArrayEqualsRelative(message, e, o, Double.MIN_VALUE);
        ExtraAssert.assertDoubleArrayEqualsRelative(emptyMessage, e, o, Double.MIN_VALUE);

        o = new double[][][] { { { doubleH } } };
        ExtraAssert.assertDoubleArrayEqualsRelative(e, o, relativeError);
        ExtraAssert.assertDoubleArrayEqualsRelative(message, e, o, relativeError);
        ExtraAssert.assertDoubleArrayEqualsRelative(emptyMessage, e, o, relativeError);

        e = new double[][][] { { { doubleLU } } };
        o = new double[][][] { { { doubleH } } };
        ExtraAssert.assertDoubleArrayEqualsRelative(e, o, relativeError);
        ExtraAssert.assertDoubleArrayEqualsRelative(message, e, o, relativeError);
        ExtraAssert.assertDoubleArrayEqualsRelative(emptyMessage, e, o, relativeError);

        e = new double[][][] { { { doubleL } } };
        o = new double[][][] { { { doubleHD } } };
        ExtraAssert.assertDoubleArrayEqualsRelative(e, o, relativeError);
        ExtraAssert.assertDoubleArrayEqualsRelative(message, e, o, relativeError);
        ExtraAssert.assertDoubleArrayEqualsRelative(emptyMessage, e, o, relativeError);

        // At least one of the same element
        double[] element = new double[] { 1 };
        e = new double[][][] { { element, { 2 } } };
        o = new double[][][] { { element, { 2 } } };
        ExtraAssert.assertDoubleArrayEqualsRelative(e, o, relativeError);
        ExtraAssert.assertDoubleArrayEqualsRelative(message, e, o, relativeError);
        ExtraAssert.assertDoubleArrayEqualsRelative(emptyMessage, e, o, relativeError);
    }

    @Test
    public void testAssertObjectArrayEqualsRelativeDoubleThrowsWithMessage()
    {
        final double[][][] e = new double[][][] { { { 1 } } };
        final double[][][] o = new double[][][] { { { 2 } } };
        try
        {
            ExtraAssert.assertDoubleArrayEqualsRelative(message, e, o, Double.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(message));
        }
        try
        {
            ExtraAssert.assertDoubleArrayEqualsRelative(message, o, e, Double.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(message));
        }
        try
        {
            ExtraAssert.assertDoubleArrayEqualsRelative(null, e, o, Double.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
        try
        {
            ExtraAssert.assertDoubleArrayEqualsRelative(null, o, e, Double.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }        try
        {
            ExtraAssert.assertDoubleArrayEqualsRelative(emptyMessage, e, o, Double.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
        try
        {
            ExtraAssert.assertDoubleArrayEqualsRelative(emptyMessage, o, e, Double.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
    }

    @Test
    public void testAssertObjectArrayEqualsRelativeDoubleErrorsUp()
    {
        // Move further apart they should be not equal
        final double[][][] e = new double[][][] { { { doubleL } } };
        final double[][][] o = new double[][][] { { { doubleHU } } };
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertDoubleArrayEqualsRelative(e, o, relativeError);
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertDoubleArrayEqualsRelative(message, e, o, relativeError);
        });
    }

    @Test
    public void testAssertObjectArrayEqualsRelativeDoubleErrorsDown()
    {
        // Move further apart they should be not equal
        final double[][][] e = new double[][][] { { { doubleLD } } };
        final double[][][] o = new double[][][] { { { doubleH } } };
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertDoubleArrayEqualsRelative(e, o, relativeError);
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertDoubleArrayEqualsRelative(message, e, o, relativeError);
        });
    }

    @Test
    public void testAssertObjectArrayEqualsRelativeDoubleThrowsWithNullArrays()
    {
        final double[][][] e = new double[][][] { { { 1 } } };
        final double[][][] o = null;
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertDoubleArrayEqualsRelative(e, o, relativeError);
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertDoubleArrayEqualsRelative(message, e, o, relativeError);
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertDoubleArrayEqualsRelative(o, e, relativeError);
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertDoubleArrayEqualsRelative(message, o, e, relativeError);
        });
    }

    @Test
    public void testAssertObjectArrayEqualsRelativeDoubleThrowsWithMismatchedArrays()
    {
        final double[][][] e = new double[][][] { { { 1 } } };
        final double[][][] o = new double[][][] { { { 1, 2 } } };
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertDoubleArrayEqualsRelative(e, o, relativeError);
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertDoubleArrayEqualsRelative(message, e, o, relativeError);
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertDoubleArrayEqualsRelative(o, e, relativeError);
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertDoubleArrayEqualsRelative(message, o, e, relativeError);
        });
    }

    @Test
    public void testAssertObjectArrayEqualsRelativeDoubleThrowsWithNonArrayElements()
    {
        final Object object = new Object();
        final Object inner1 = new Object[] { object };
        final Object inner2 = new Object[] { object };
        final Object inner3 = new Object();

        Object[] e = new Object[][] { { inner1, inner2 } };
        Object[] o = new Object[][] { { inner1, inner2 } };
        ExtraAssert.assertDoubleArrayEqualsRelative(e, o, relativeError);
        ExtraAssert.assertDoubleArrayEqualsRelative(o, e, relativeError);

        e = new Object[][] { { object, object } };
        o = new Object[][] { { object, object } };
        ExtraAssert.assertDoubleArrayEqualsRelative(e, o, relativeError);
        ExtraAssert.assertDoubleArrayEqualsRelative(o, e, relativeError);

        final Object[] e2 = new Object[][] { { inner1, inner2 } };
        final Object[] o2 = new Object[][] { { inner1, inner3 } };
        Assertions.assertThrows(ClassCastException.class, () -> {
            ExtraAssert.assertDoubleArrayEqualsRelative(e2, o2, relativeError);
        });
        Assertions.assertThrows(ClassCastException.class, () -> {
            ExtraAssert.assertDoubleArrayEqualsRelative(message, e2, o2, relativeError);
        });
        Assertions.assertThrows(ClassCastException.class, () -> {
            ExtraAssert.assertDoubleArrayEqualsRelative(o2, e2, relativeError);
        });
        Assertions.assertThrows(ClassCastException.class, () -> {
            ExtraAssert.assertDoubleArrayEqualsRelative(message, o2, e2, relativeError);
        });
    }

    // XXX - Copy to here
    @Test
    public void testAssertEqualsRelativeFloat()
    {
        float e = floatL;
        float o = floatL;
        ExtraAssert.assertEqualsRelative(e, o, Float.MIN_VALUE);

        o = floatH;
        ExtraAssert.assertEqualsRelative(e, o, relativeError);

        e = floatLU;
        o = floatH;
        ExtraAssert.assertEqualsRelative(e, o, relativeError);

        e = floatL;
        o = floatHD;
        ExtraAssert.assertEqualsRelative(e, o, relativeError);
    }

    @Test
    public void testAssertEqualsRelativeFloatThrowsWithMessage()
    {
        final float e = 1;
        final float o = 2;
        try
        {
            ExtraAssert.assertEqualsRelative(message, e, o, Float.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(message));
        }
        try
        {
            ExtraAssert.assertEqualsRelative(message, o, e, Float.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(message));
        }
        try
        {
            ExtraAssert.assertEqualsRelative(null, e, o, Float.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
        try
        {
            ExtraAssert.assertEqualsRelative(null, o, e, Float.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
        try
        {
            ExtraAssert.assertEqualsRelative(emptyMessage, e, o, Float.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
        try
        {
            ExtraAssert.assertEqualsRelative(emptyMessage, o, e, Float.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
    }

    @Test
    public void testAssertEqualsRelativeFloatErrorsUp()
    {
        // Move further apart they should be not equal
        final float e = floatL;
        final float o = floatHU;
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertEqualsRelative(e, o, relativeError);
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertEqualsRelative(message, e, o, relativeError);
        });
    }

    @Test
    public void testAssertEqualsRelativeFloatErrorsDown()
    {
        // Move further apart they should be not equal
        final float e = floatLD;
        final float o = floatH;
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertEqualsRelative(e, o, relativeError);
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertEqualsRelative(message, e, o, relativeError);
        });
    }

    @Test
    public void testAssertNotEqualsRelativeFloat()
    {
        // Not equals should be OK
        final float e = floatLD;
        final float o = floatH;
        ExtraAssert.assertNotEqualsRelative(e, o, relativeError);
        ExtraAssert.assertNotEqualsRelative(message, e, o, relativeError);

        // If not equal this should throw
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertEqualsRelative(e, o, relativeError);
        });

        final float e2 = floatL;
        final float o2 = floatHU;
        ExtraAssert.assertNotEqualsRelative(e2, o2, relativeError);
        ExtraAssert.assertNotEqualsRelative(message, e2, o2, relativeError);
    }

    @Test
    public void testAssertNotEqualsRelativeFloatThrowsWithMessage()
    {
        final float e = 1;
        final float o = 1;
        try
        {
            ExtraAssert.assertNotEqualsRelative(message, e, o, Float.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(message));
        }
        try
        {
            ExtraAssert.assertNotEqualsRelative(message, o, e, Float.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(message));
        }
        try
        {
            ExtraAssert.assertNotEqualsRelative(null, e, o, Float.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
        try
        {
            ExtraAssert.assertNotEqualsRelative(null, o, e, Float.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
        try
        {
            ExtraAssert.assertNotEqualsRelative(emptyMessage, e, o, Float.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
        try
        {
            ExtraAssert.assertNotEqualsRelative(emptyMessage, o, e, Float.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
    }

    @Test
    public void testAssertNotEqualsRelativeFloatErrors()
    {
        // If equal this should throw
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertNotEqualsRelative(floatL, floatH, relativeError);
        });
        // Check it is equal
        ExtraAssert.assertEqualsRelative(floatL, floatH, relativeError);
    }

    @Test
    public void testAssertArrayEqualsRelativeFloat()
    {
        float[] e = new float[] { floatL };
        // Check with self
        ExtraAssert.assertArrayEqualsRelative(e, e, Float.MIN_VALUE);
        ExtraAssert.assertArrayEqualsRelative(message, e, e, Float.MIN_VALUE);
        ExtraAssert.assertArrayEqualsRelative(emptyMessage, e, e, Float.MIN_VALUE);

        float[] o = new float[] { floatL };
        ExtraAssert.assertArrayEqualsRelative(e, o, Float.MIN_VALUE);
        ExtraAssert.assertArrayEqualsRelative(message, o, e, Float.MIN_VALUE);
        ExtraAssert.assertArrayEqualsRelative(emptyMessage, o, e, Float.MIN_VALUE);

        o = new float[] { floatH };
        ExtraAssert.assertArrayEqualsRelative(e, o, relativeError);
        ExtraAssert.assertArrayEqualsRelative(message, o, e, relativeError);
        ExtraAssert.assertArrayEqualsRelative(emptyMessage, o, e, relativeError);

        e = new float[] { floatLU };
        o = new float[] { floatH };
        ExtraAssert.assertArrayEqualsRelative(e, o, relativeError);
        ExtraAssert.assertArrayEqualsRelative(message, o, e, relativeError);
        ExtraAssert.assertArrayEqualsRelative(emptyMessage, o, e, relativeError);

        e = new float[] { floatL };
        o = new float[] { floatHD };
        ExtraAssert.assertArrayEqualsRelative(e, o, relativeError);
        ExtraAssert.assertArrayEqualsRelative(message, o, e, relativeError);
        ExtraAssert.assertArrayEqualsRelative(emptyMessage, o, e, relativeError);
    }

    @Test
    public void testAssertArrayEqualsRelativeFloatThrowsWithFormattedMessage()
    {
        final float[] e = new float[] { 1 };
        final float[] o = new float[] { 2 };
        try
        {
            ExtraAssert.assertArrayEqualsRelative(message, e, o, Float.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(message));
        }
        try
        {
            ExtraAssert.assertArrayEqualsRelative(message, o, e, Float.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(message));
        }
        try
        {
            ExtraAssert.assertArrayEqualsRelative(null, e, o, Float.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
        try
        {
            ExtraAssert.assertArrayEqualsRelative(null, o, e, Float.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
        try
        {
            ExtraAssert.assertArrayEqualsRelative(emptyMessage, e, o, Float.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
        try
        {
            ExtraAssert.assertArrayEqualsRelative(emptyMessage, o, e, Float.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
    }

    @Test
    public void testAssertArrayEqualsRelativeFloatErrorsUp()
    {
        // Move further apart they should be not equal
        final float[] e = new float[] { floatL };
        final float[] o = new float[] { floatHU };
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertArrayEqualsRelative(e, o, relativeError);
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertArrayEqualsRelative(message, e, o, relativeError);
        });
    }

    @Test
    public void testAssertArrayEqualsRelativeFloatErrorsDown()
    {
        // Move further apart they should be not equal
        final float[] e = new float[] { floatLD };
        final float[] o = new float[] { floatH };
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertArrayEqualsRelative(e, o, relativeError);
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertArrayEqualsRelative(message, e, o, relativeError);
        });
    }

    @Test
    public void testAssertArrayEqualsRelativeFloatThrowsWithNullArrays()
    {
        final float[] e = new float[] { 1 };
        final float[] o = null;
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertArrayEqualsRelative(e, o, relativeError);
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertArrayEqualsRelative(message, e, o, relativeError);
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertArrayEqualsRelative(o, e, relativeError);
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertArrayEqualsRelative(message, o, e, relativeError);
        });
    }

    @Test
    public void testAssertArrayEqualsRelativeFloatThrowsWithMismatchedArrays()
    {
        final float[] e = new float[] { 1 };
        final float[] o = new float[] { 1, 2 };
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertArrayEqualsRelative(e, o, relativeError);
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertArrayEqualsRelative(message, e, o, relativeError);
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertArrayEqualsRelative(o, e, relativeError);
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertArrayEqualsRelative(message, o, e, relativeError);
        });
    }

    @Test
    public void testAssertObjectArrayEqualsRelativeFloat()
    {
        float[][][] e = new float[][][] { { { floatL } } };
        // Check with self
        ExtraAssert.assertFloatArrayEqualsRelative(e, e, Float.MIN_VALUE);
        ExtraAssert.assertFloatArrayEqualsRelative(message, e, e, Float.MIN_VALUE);
        ExtraAssert.assertFloatArrayEqualsRelative(emptyMessage, e, e, Float.MIN_VALUE);

        float[][][] o = new float[][][] { { { floatL } } };
        ExtraAssert.assertFloatArrayEqualsRelative(e, o, Float.MIN_VALUE);
        ExtraAssert.assertFloatArrayEqualsRelative(message, e, o, Float.MIN_VALUE);
        ExtraAssert.assertFloatArrayEqualsRelative(emptyMessage, e, o, Float.MIN_VALUE);

        o = new float[][][] { { { floatH } } };
        ExtraAssert.assertFloatArrayEqualsRelative(e, o, relativeError);
        ExtraAssert.assertFloatArrayEqualsRelative(message, e, o, relativeError);
        ExtraAssert.assertFloatArrayEqualsRelative(emptyMessage, e, o, relativeError);

        e = new float[][][] { { { floatLU } } };
        o = new float[][][] { { { floatH } } };
        ExtraAssert.assertFloatArrayEqualsRelative(e, o, relativeError);
        ExtraAssert.assertFloatArrayEqualsRelative(message, e, o, relativeError);
        ExtraAssert.assertFloatArrayEqualsRelative(emptyMessage, e, o, relativeError);

        e = new float[][][] { { { floatL } } };
        o = new float[][][] { { { floatHD } } };
        ExtraAssert.assertFloatArrayEqualsRelative(e, o, relativeError);
        ExtraAssert.assertFloatArrayEqualsRelative(message, e, o, relativeError);
        ExtraAssert.assertFloatArrayEqualsRelative(emptyMessage, e, o, relativeError);

        // At least one of the same element
        float[] element = new float[] { 1 };
        e = new float[][][] { { element, { 2 } } };
        o = new float[][][] { { element, { 2 } } };
        ExtraAssert.assertFloatArrayEqualsRelative(e, o, relativeError);
        ExtraAssert.assertFloatArrayEqualsRelative(message, e, o, relativeError);
        ExtraAssert.assertFloatArrayEqualsRelative(emptyMessage, e, o, relativeError);
    }

    @Test
    public void testAssertObjectArrayEqualsRelativeFloatThrowsWithMessage()
    {
        final float[][][] e = new float[][][] { { { 1 } } };
        final float[][][] o = new float[][][] { { { 2 } } };
        try
        {
            ExtraAssert.assertFloatArrayEqualsRelative(message, e, o, Float.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(message));
        }
        try
        {
            ExtraAssert.assertFloatArrayEqualsRelative(message, o, e, Float.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(message));
        }
        try
        {
            ExtraAssert.assertFloatArrayEqualsRelative(null, e, o, Float.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
        try
        {
            ExtraAssert.assertFloatArrayEqualsRelative(null, o, e, Float.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }        try
        {
            ExtraAssert.assertFloatArrayEqualsRelative(emptyMessage, e, o, Float.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
        try
        {
            ExtraAssert.assertFloatArrayEqualsRelative(emptyMessage, o, e, Float.MIN_VALUE);
        }
        catch (final AssertionError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
    }

    @Test
    public void testAssertObjectArrayEqualsRelativeFloatErrorsUp()
    {
        // Move further apart they should be not equal
        final float[][][] e = new float[][][] { { { floatL } } };
        final float[][][] o = new float[][][] { { { floatHU } } };
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertFloatArrayEqualsRelative(e, o, relativeError);
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertFloatArrayEqualsRelative(message, e, o, relativeError);
        });
    }

    @Test
    public void testAssertObjectArrayEqualsRelativeFloatErrorsDown()
    {
        // Move further apart they should be not equal
        final float[][][] e = new float[][][] { { { floatLD } } };
        final float[][][] o = new float[][][] { { { floatH } } };
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertFloatArrayEqualsRelative(e, o, relativeError);
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertFloatArrayEqualsRelative(message, e, o, relativeError);
        });
    }

    @Test
    public void testAssertObjectArrayEqualsRelativeFloatThrowsWithNullArrays()
    {
        final float[][][] e = new float[][][] { { { 1 } } };
        final float[][][] o = null;
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertFloatArrayEqualsRelative(e, o, relativeError);
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertFloatArrayEqualsRelative(message, e, o, relativeError);
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertFloatArrayEqualsRelative(o, e, relativeError);
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertFloatArrayEqualsRelative(message, o, e, relativeError);
        });
    }

    @Test
    public void testAssertObjectArrayEqualsRelativeFloatThrowsWithMismatchedArrays()
    {
        final float[][][] e = new float[][][] { { { 1 } } };
        final float[][][] o = new float[][][] { { { 1, 2 } } };
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertFloatArrayEqualsRelative(e, o, relativeError);
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertFloatArrayEqualsRelative(message, e, o, relativeError);
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertFloatArrayEqualsRelative(o, e, relativeError);
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            ExtraAssert.assertFloatArrayEqualsRelative(message, o, e, relativeError);
        });
    }

    @Test
    public void testAssertObjectArrayEqualsRelativeFloatThrowsWithNonArrayElements()
    {
        final Object object = new Object();
        final Object inner1 = new Object[] { object };
        final Object inner2 = new Object[] { object };
        final Object inner3 = new Object();

        Object[] e = new Object[][] { { inner1, inner2 } };
        Object[] o = new Object[][] { { inner1, inner2 } };
        ExtraAssert.assertFloatArrayEqualsRelative(e, o, relativeError);
        ExtraAssert.assertFloatArrayEqualsRelative(o, e, relativeError);

        e = new Object[][] { { object, object } };
        o = new Object[][] { { object, object } };
        ExtraAssert.assertFloatArrayEqualsRelative(e, o, relativeError);
        ExtraAssert.assertFloatArrayEqualsRelative(o, e, relativeError);

        final Object[] e2 = new Object[][] { { inner1, inner2 } };
        final Object[] o2 = new Object[][] { { inner1, inner3 } };
        Assertions.assertThrows(ClassCastException.class, () -> {
            ExtraAssert.assertFloatArrayEqualsRelative(e2, o2, relativeError);
        });
        Assertions.assertThrows(ClassCastException.class, () -> {
            ExtraAssert.assertFloatArrayEqualsRelative(message, e2, o2, relativeError);
        });
        Assertions.assertThrows(ClassCastException.class, () -> {
            ExtraAssert.assertFloatArrayEqualsRelative(o2, e2, relativeError);
        });
        Assertions.assertThrows(ClassCastException.class, () -> {
            ExtraAssert.assertFloatArrayEqualsRelative(message, o2, e2, relativeError);
        });
    }
}
