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
package uk.ac.sussex.gdsc.test.junit5;

import java.util.function.Supplier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

@SuppressWarnings("javadoc")
public class ExtraAssertionsTest
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
    /** A null message. */
    private final String nullMessage = null;

    /** The supplied message. */
    private final Supplier<String> messageSupplier = () -> {
        return "Lambda message";
    };
    /** A null supplied message. */
    private final Supplier<String> nullMessageSupplier = null;

    @Test
    public void testAssertObjectArrayEqualsRelativeDoubleThrowsWithNonFloatArrays()
    {
        final Object[] i1 = new int[][] { { 1 } };
        final Object[] i2 = new int[][] { { 1 } };
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(i1, i2, relativeError);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(i1, i2, relativeError, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(i1, i2, relativeError, messageSupplier);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(i2, i1, relativeError);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(i2, i1, relativeError, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(i2, i1, relativeError, messageSupplier);
        });
        final Object[] f = new float[][] { { 1 } };
        final Object[] d = new double[][] { { 1 } };
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(f, d, relativeError);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(f, d, relativeError, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(f, d, relativeError, messageSupplier);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(d, f, relativeError);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(d, f, relativeError, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(d, f, relativeError, messageSupplier);
        });
    }

    @Test
    public void testAssertObjectArrayEqualsDoubleThrowsWithNonFloatArrays()
    {
        final Object[] i1 = new int[][] { { 1 } };
        final Object[] i2 = new int[][] { { 1 } };
        final Double delta = new Double(1);
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(i1, i2, delta);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(i1, i2, delta, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(i1, i2, delta, messageSupplier);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(i2, i1, delta);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(i2, i1, delta, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(i2, i1, delta, messageSupplier);
        });
        final Object[] f = new float[][] { { 1 } };
        final Object[] d = new double[][] { { 1 } };
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(f, d, delta);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(f, d, delta, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(f, d, delta, messageSupplier);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(d, f, delta);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(d, f, delta, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(d, f, delta, messageSupplier);
        });
    }

    // XXX - Copy from here
    @Test
    public void testAssertEqualsRelativeDouble()
    {
        double e = doubleL;
        double o = doubleL;
        ExtraAssertions.assertEqualsRelative(e, o, Double.MIN_VALUE);
        ExtraAssertions.assertEqualsRelative(e, o, Double.MIN_VALUE, message);
        ExtraAssertions.assertEqualsRelative(e, o, Double.MIN_VALUE, messageSupplier);
        ExtraAssertions.assertEqualsRelative(e, o, Double.MIN_VALUE, nullMessage);
        ExtraAssertions.assertEqualsRelative(e, o, Double.MIN_VALUE, nullMessageSupplier);

        o = doubleH;
        ExtraAssertions.assertEqualsRelative(e, o, relativeError);
        ExtraAssertions.assertEqualsRelative(e, o, relativeError, message);
        ExtraAssertions.assertEqualsRelative(e, o, relativeError, messageSupplier);
        ExtraAssertions.assertEqualsRelative(e, o, relativeError, nullMessage);
        ExtraAssertions.assertEqualsRelative(e, o, relativeError, nullMessageSupplier);

        e = doubleLU;
        o = doubleH;
        ExtraAssertions.assertEqualsRelative(e, o, relativeError);
        ExtraAssertions.assertEqualsRelative(e, o, relativeError, message);
        ExtraAssertions.assertEqualsRelative(e, o, relativeError, messageSupplier);
        ExtraAssertions.assertEqualsRelative(e, o, relativeError, nullMessage);
        ExtraAssertions.assertEqualsRelative(e, o, relativeError, nullMessageSupplier);

        e = doubleL;
        o = doubleHD;
        ExtraAssertions.assertEqualsRelative(e, o, relativeError);
        ExtraAssertions.assertEqualsRelative(e, o, relativeError, message);
        ExtraAssertions.assertEqualsRelative(e, o, relativeError, messageSupplier);
        ExtraAssertions.assertEqualsRelative(e, o, relativeError, nullMessage);
        ExtraAssertions.assertEqualsRelative(e, o, relativeError, nullMessageSupplier);
    }

    @Test
    public void testAssertEqualsRelativeDoubleThrowsWithFormattedMessage()
    {
        final double e = 1;
        final double o = 2;
        try
        {
            ExtraAssertions.assertEqualsRelative(e, o, Double.MIN_VALUE, message);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(message));
        }
        try
        {
            ExtraAssertions.assertEqualsRelative(e, o, Double.MIN_VALUE, messageSupplier);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(messageSupplier.get()));
        }
        try
        {
            ExtraAssertions.assertEqualsRelative(o, e, Double.MIN_VALUE, message);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(message));
        }
        try
        {
            ExtraAssertions.assertEqualsRelative(o, e, Double.MIN_VALUE, messageSupplier);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(messageSupplier.get()));
        }
        try
        {
            ExtraAssertions.assertEqualsRelative(e, o, Double.MIN_VALUE, nullMessage);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
        try
        {
            ExtraAssertions.assertEqualsRelative(e, o, Double.MIN_VALUE, nullMessageSupplier);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
        try
        {
            ExtraAssertions.assertEqualsRelative(o, e, Double.MIN_VALUE, nullMessage);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
        try
        {
            ExtraAssertions.assertEqualsRelative(o, e, Double.MIN_VALUE, nullMessageSupplier);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
    }

    @Test
    public void testAssertEqualsRelativeDoubleThrowsUsingBadThreshold()
    {
        final double e = 1;
        final double o = 1;
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertEqualsRelative(e, o, 0);
        }, "0");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertEqualsRelative(e, o, 2);
        }, "2");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertEqualsRelative(e, o, Double.POSITIVE_INFINITY);
        }, "+Inf");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertEqualsRelative(e, o, Double.NaN);
        }, "NaN");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertEqualsRelative(e, o, -1);
        }, "-1");

        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertEqualsRelative(e, o, 0, message);
        }, "0");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertEqualsRelative(e, o, 2, message);
        }, "2");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertEqualsRelative(e, o, Double.POSITIVE_INFINITY, message);
        }, "+Inf");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertEqualsRelative(e, o, Double.NaN, message);
        }, "NaN");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertEqualsRelative(e, o, -1, message);
        }, "-1");

        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertEqualsRelative(e, o, 0, messageSupplier);
        }, "0");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertEqualsRelative(e, o, 2, messageSupplier);
        }, "2");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertEqualsRelative(e, o, Double.POSITIVE_INFINITY, messageSupplier);
        }, "+Inf");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertEqualsRelative(e, o, Double.NaN, messageSupplier);
        }, "NaN");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertEqualsRelative(e, o, -1, messageSupplier);
        }, "-1");
    }

    @Test
    public void testAssertEqualsRelativeDoubleErrorsUp()
    {
        // Move further apart they should be not equal
        final double e = doubleL;
        final double o = doubleHU;
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertEqualsRelative(e, o, relativeError);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertEqualsRelative(e, o, relativeError, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertEqualsRelative(e, o, relativeError, messageSupplier);
        });
    }

    @Test
    public void testAssertEqualsRelativeDoubleErrorsDown()
    {
        // Move further apart they should be not equal
        final double e = doubleLD;
        final double o = doubleH;
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertEqualsRelative(e, o, relativeError);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertEqualsRelative(e, o, relativeError, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertEqualsRelative(e, o, relativeError, messageSupplier);
        });
    }

    @Test
    public void testAssertArrayEqualsRelativeDouble()
    {
        double[] e = new double[] { doubleL };
        // Check with self
        ExtraAssertions.assertArrayEqualsRelative(e, e, Double.MIN_VALUE);
        ExtraAssertions.assertArrayEqualsRelative(e, e, Double.MIN_VALUE, message);
        ExtraAssertions.assertArrayEqualsRelative(e, e, Double.MIN_VALUE, messageSupplier);
        ExtraAssertions.assertArrayEqualsRelative(e, e, Double.MIN_VALUE, nullMessage);
        ExtraAssertions.assertArrayEqualsRelative(e, e, Double.MIN_VALUE, nullMessageSupplier);

        double[] o = new double[] { doubleL };
        ExtraAssertions.assertArrayEqualsRelative(e, o, Double.MIN_VALUE);
        ExtraAssertions.assertArrayEqualsRelative(e, o, Double.MIN_VALUE, message);
        ExtraAssertions.assertArrayEqualsRelative(e, o, Double.MIN_VALUE, messageSupplier);
        ExtraAssertions.assertArrayEqualsRelative(e, o, Double.MIN_VALUE, nullMessage);
        ExtraAssertions.assertArrayEqualsRelative(e, o, Double.MIN_VALUE, nullMessageSupplier);

        o = new double[] { doubleH };
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, message);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, messageSupplier);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, nullMessage);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, nullMessageSupplier);

        e = new double[] { doubleLU };
        o = new double[] { doubleH };
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, message);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, messageSupplier);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, nullMessage);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, nullMessageSupplier);

        e = new double[] { doubleL };
        o = new double[] { doubleHD };
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, message);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, messageSupplier);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, nullMessage);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, nullMessageSupplier);
    }

    @Test
    public void testAssertArrayEqualsRelativeDoubleThrowsWithFormattedMessage()
    {
        final double[] e = new double[] { 1 };
        final double[] o = new double[] { 2 };
        try
        {
            ExtraAssertions.assertArrayEqualsRelative(e, o, Double.MIN_VALUE, message);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(message));
        }
        try
        {
            ExtraAssertions.assertArrayEqualsRelative(e, o, Double.MIN_VALUE, messageSupplier);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(messageSupplier.get()));
        }
        try
        {
            ExtraAssertions.assertArrayEqualsRelative(o, e, Double.MIN_VALUE, message);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(message));
        }
        try
        {
            ExtraAssertions.assertArrayEqualsRelative(o, e, Double.MIN_VALUE, messageSupplier);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(messageSupplier.get()));
        }
        try
        {
            ExtraAssertions.assertArrayEqualsRelative(e, o, Double.MIN_VALUE, nullMessage);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
        try
        {
            ExtraAssertions.assertArrayEqualsRelative(e, o, Double.MIN_VALUE, nullMessageSupplier);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
    }

    @Test
    public void testAssertArrayEqualsRelativeDoubleThrowsUsingBadThreshold()
    {
        final double[] e = new double[] { 1 };
        for (final double[] o : new double[][] { null, e })
        {
            Assertions.assertThrows(AssertionFailedError.class, () -> {
                ExtraAssertions.assertArrayEqualsRelative(e, o, 0);
            }, "0");
            Assertions.assertThrows(AssertionFailedError.class, () -> {
                ExtraAssertions.assertArrayEqualsRelative(e, o, 2);
            }, "2");
            Assertions.assertThrows(AssertionFailedError.class, () -> {
                ExtraAssertions.assertArrayEqualsRelative(e, o, Double.POSITIVE_INFINITY);
            }, "+Inf");
            Assertions.assertThrows(AssertionFailedError.class, () -> {
                ExtraAssertions.assertArrayEqualsRelative(e, o, Double.NaN);
            }, "NaN");
            Assertions.assertThrows(AssertionFailedError.class, () -> {
                ExtraAssertions.assertArrayEqualsRelative(e, o, -1);
            }, "-1");

            Assertions.assertThrows(AssertionFailedError.class, () -> {
                ExtraAssertions.assertArrayEqualsRelative(e, o, 0, message);
            }, "0");
            Assertions.assertThrows(AssertionFailedError.class, () -> {
                ExtraAssertions.assertArrayEqualsRelative(e, o, 2, message);
            }, "2");
            Assertions.assertThrows(AssertionFailedError.class, () -> {
                ExtraAssertions.assertArrayEqualsRelative(e, o, Double.POSITIVE_INFINITY, message);
            }, "+Inf");
            Assertions.assertThrows(AssertionFailedError.class, () -> {
                ExtraAssertions.assertArrayEqualsRelative(e, o, Double.NaN, message);
            }, "NaN");
            Assertions.assertThrows(AssertionFailedError.class, () -> {
                ExtraAssertions.assertArrayEqualsRelative(e, o, -1, message);
            }, "-1");

            Assertions.assertThrows(AssertionFailedError.class, () -> {
                ExtraAssertions.assertArrayEqualsRelative(e, o, 0, messageSupplier);
            }, "0");
            Assertions.assertThrows(AssertionFailedError.class, () -> {
                ExtraAssertions.assertArrayEqualsRelative(e, o, 2, messageSupplier);
            }, "2");
            Assertions.assertThrows(AssertionFailedError.class, () -> {
                ExtraAssertions.assertArrayEqualsRelative(e, o, Double.POSITIVE_INFINITY, messageSupplier);
            }, "+Inf");
            Assertions.assertThrows(AssertionFailedError.class, () -> {
                ExtraAssertions.assertArrayEqualsRelative(e, o, Double.NaN, messageSupplier);
            }, "NaN");
            Assertions.assertThrows(AssertionFailedError.class, () -> {
                ExtraAssertions.assertArrayEqualsRelative(e, o, -1, messageSupplier);
            }, "-1");
        }
    }

    @Test
    public void testAssertArrayEqualsRelativeDoubleErrorsUp()
    {
        // Move further apart they should be not equal
        final double[] e = new double[] { doubleL };
        final double[] o = new double[] { doubleHU };
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, messageSupplier);
        });
    }

    @Test
    public void testAssertArrayEqualsRelativeDoubleErrorsDown()
    {
        // Move further apart they should be not equal
        final double[] e = new double[] { doubleLD };
        final double[] o = new double[] { doubleH };
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, messageSupplier);
        });
    }

    @Test
    public void testAssertArrayEqualsRelativeDoubleThrowsWithNullArrays()
    {
        final double[] e = new double[] { 1 };
        final double[] o = null;
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, messageSupplier);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(o, e, relativeError);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(o, e, relativeError, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(o, e, relativeError, messageSupplier);
        });
    }

    @Test
    public void testAssertArrayEqualsRelativeDoubleThrowsWithMismatchedArrays()
    {
        final double[] e = new double[] { 1 };
        final double[] o = new double[] { 1, 2 };
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, messageSupplier);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(o, e, relativeError);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(o, e, relativeError, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(o, e, relativeError, messageSupplier);
        });
    }

    @Test
    public void testAssertObjectArrayEqualsRelativeDouble()
    {
        double[][][] e = new double[][][] { { { doubleL } } };
        // Check with self
        ExtraAssertions.assertArrayEqualsRelative(e, e, Double.MIN_VALUE);
        ExtraAssertions.assertArrayEqualsRelative(e, e, Double.MIN_VALUE, message);
        ExtraAssertions.assertArrayEqualsRelative(e, e, Double.MIN_VALUE, messageSupplier);
        ExtraAssertions.assertArrayEqualsRelative(e, e, Double.MIN_VALUE, nullMessage);
        ExtraAssertions.assertArrayEqualsRelative(e, e, Double.MIN_VALUE, nullMessageSupplier);

        double[][][] o = new double[][][] { { { doubleL } } };
        ExtraAssertions.assertArrayEqualsRelative(e, o, Double.MIN_VALUE);
        ExtraAssertions.assertArrayEqualsRelative(e, o, Double.MIN_VALUE, message);
        ExtraAssertions.assertArrayEqualsRelative(e, o, Double.MIN_VALUE, messageSupplier);
        ExtraAssertions.assertArrayEqualsRelative(e, o, Double.MIN_VALUE, nullMessage);
        ExtraAssertions.assertArrayEqualsRelative(e, o, Double.MIN_VALUE, nullMessageSupplier);

        o = new double[][][] { { { doubleH } } };
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, message);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, messageSupplier);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, nullMessage);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, nullMessageSupplier);

        e = new double[][][] { { { doubleLU } } };
        o = new double[][][] { { { doubleH } } };
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, message);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, messageSupplier);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, nullMessage);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, nullMessageSupplier);

        e = new double[][][] { { { doubleL } } };
        o = new double[][][] { { { doubleHD } } };
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, message);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, messageSupplier);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, nullMessage);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, nullMessageSupplier);

        // At least one of the same element
        double[] element = new double[] { 1 };
        e = new double[][][] { { element, { 2 } } };
        o = new double[][][] { { element, { 2 } } };
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, message);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, messageSupplier);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, nullMessage);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, nullMessageSupplier);
    }

    @Test
    public void testAssertObjectArrayEqualsRelativeDoubleThrowsWithFormattedMessage()
    {
        final double[][][] e = new double[][][] { { { 1 } } };
        final double[][][] o = new double[][][] { { { 2 } } };
        try
        {
            ExtraAssertions.assertArrayEqualsRelative(e, o, Double.MIN_VALUE, message);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(message));
        }
        try
        {
            ExtraAssertions.assertArrayEqualsRelative(e, o, Double.MIN_VALUE, messageSupplier);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(messageSupplier.get()));
        }
        try
        {
            ExtraAssertions.assertArrayEqualsRelative(o, e, Double.MIN_VALUE, message);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(message));
        }
        try
        {
            ExtraAssertions.assertArrayEqualsRelative(o, e, Double.MIN_VALUE, messageSupplier);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(messageSupplier.get()));
        }
        try
        {
            ExtraAssertions.assertArrayEqualsRelative(e, o, Double.MIN_VALUE, nullMessage);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
        try
        {
            ExtraAssertions.assertArrayEqualsRelative(e, o, Double.MIN_VALUE, nullMessageSupplier);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
        try
        {
            ExtraAssertions.assertArrayEqualsRelative(o, e, Double.MIN_VALUE, nullMessage);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
        try
        {
            ExtraAssertions.assertArrayEqualsRelative(o, e, Double.MIN_VALUE, nullMessageSupplier);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
    }

    @Test
    public void testAssertObjectArrayEqualsRelativeDoubleThrowsUsingBadThreshold()
    {
        // This does not throw on a bad delta if e==o so create a new object
        final double[][][] e = new double[][][] { { { 1 } } };
        final double[][][] o = new double[][][] { { { 1 } } };
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, 0);
        }, "0");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, 2);
        }, "2");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, Double.POSITIVE_INFINITY);
        }, "+Inf");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, Double.NaN);
        }, "NaN");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, -1);
        }, "-1");

        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, 0, message);
        }, "0");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, 2, message);
        }, "2");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, Double.POSITIVE_INFINITY, message);
        }, "+Inf");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, Double.NaN, message);
        }, "NaN");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, -1, message);
        }, "-1");

        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, 0, messageSupplier);
        }, "0");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, 2, messageSupplier);
        }, "2");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, Double.POSITIVE_INFINITY, messageSupplier);
        }, "+Inf");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, Double.NaN, messageSupplier);
        }, "NaN");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, -1, messageSupplier);
        }, "-1");
    }

    @Test
    public void testAssertObjectArrayEqualsRelativeDoubleErrorsUp()
    {
        // Move further apart they should be not equal
        final double[][][] e = new double[][][] { { { doubleL } } };
        final double[][][] o = new double[][][] { { { doubleHU } } };
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, messageSupplier);
        });
    }

    @Test
    public void testAssertObjectArrayEqualsRelativeDoubleErrorsDown()
    {
        // Move further apart they should be not equal
        final double[][][] e = new double[][][] { { { doubleLD } } };
        final double[][][] o = new double[][][] { { { doubleH } } };
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, messageSupplier);
        });
    }

    @Test
    public void testAssertObjectArrayEqualsRelativeDoubleThrowsWithNullArrays()
    {
        final double[][][] e = new double[][][] { { { 1 } } };
        final double[][][] o = null;
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, messageSupplier);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(o, e, relativeError);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(o, e, relativeError, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(o, e, relativeError, messageSupplier);
        });
    }

    @Test
    public void testAssertObjectArrayEqualsRelativeDoubleThrowsWithMismatchedArrays()
    {
        final double[][][] e = new double[][][] { { { 1 } } };
        final double[][][] o = new double[][][] { { { 1, 2 } } };
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, messageSupplier);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(o, e, relativeError);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(o, e, relativeError, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(o, e, relativeError, messageSupplier);
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
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError);
        ExtraAssertions.assertArrayEqualsRelative(o, e, relativeError);

        e = new Object[][] { { object, object } };
        o = new Object[][] { { object, object } };
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError);
        ExtraAssertions.assertArrayEqualsRelative(o, e, relativeError);

        final Object[] e2 = new Object[][] { { inner1, inner2 } };
        final Object[] o2 = new Object[][] { { inner1, inner3 } };
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e2, o2, relativeError);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e2, o2, relativeError, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e2, o2, relativeError, messageSupplier);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(o2, e2, relativeError);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(o2, e2, relativeError, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(o2, e2, relativeError, messageSupplier);
        });
    }
    
    // Non-relative array equals

    @Test
    public void testAssertObjectArrayEqualsDouble()
    {
        double[][][] e = new double[][][] { { { 1 } } };
        // Check with self
        ExtraAssertions.assertArrayEquals(e, e, Double.MIN_VALUE);
        ExtraAssertions.assertArrayEquals(e, e, Double.MIN_VALUE, message);
        ExtraAssertions.assertArrayEquals(e, e, Double.MIN_VALUE, messageSupplier);
        ExtraAssertions.assertArrayEquals(e, e, Double.MIN_VALUE, nullMessage);
        ExtraAssertions.assertArrayEquals(e, e, Double.MIN_VALUE, nullMessageSupplier);

        double[][][] o = new double[][][] { { { 1 } } };
        final Double delta = new Double(1);
        ExtraAssertions.assertArrayEquals(e, o, delta);
        ExtraAssertions.assertArrayEquals(e, o, delta, message);
        ExtraAssertions.assertArrayEquals(e, o, delta, messageSupplier);
        ExtraAssertions.assertArrayEquals(e, o, delta, nullMessage);
        ExtraAssertions.assertArrayEquals(e, o, delta, nullMessageSupplier);

        o = new double[][][] { { { 2 } } };
        ExtraAssertions.assertArrayEquals(e, o, delta);
        ExtraAssertions.assertArrayEquals(e, o, delta, message);
        ExtraAssertions.assertArrayEquals(e, o, delta, messageSupplier);
        ExtraAssertions.assertArrayEquals(e, o, delta, nullMessage);
        ExtraAssertions.assertArrayEquals(e, o, delta, nullMessageSupplier);

        o = new double[][][] { { { 0 } } };
        ExtraAssertions.assertArrayEquals(e, o, delta);
        ExtraAssertions.assertArrayEquals(e, o, delta, message);
        ExtraAssertions.assertArrayEquals(e, o, delta, messageSupplier);
        ExtraAssertions.assertArrayEquals(e, o, delta, nullMessage);
        ExtraAssertions.assertArrayEquals(e, o, delta, nullMessageSupplier);
        
        // At least one of the same element
        final double[] element = new double[] { 1 };
        e = new double[][][] { { element, { 2 } } };
        o = new double[][][] { { element, { 2 } } };
        ExtraAssertions.assertArrayEquals(e, o, delta);
        ExtraAssertions.assertArrayEquals(e, o, delta, message);
        ExtraAssertions.assertArrayEquals(e, o, delta, messageSupplier);
        ExtraAssertions.assertArrayEquals(e, o, delta, nullMessage);
        ExtraAssertions.assertArrayEquals(e, o, delta, nullMessageSupplier);
    }

    @Test
    public void testAssertObjectArrayEqualsDoubleThrowsWithFormattedMessage()
    {
        final double[][][] e = new double[][][] { { { 2 } } };
        final double[][][] o = new double[][][] { { { 4 } } };
        final Double delta = new Double(1);
        try
        {
            ExtraAssertions.assertArrayEquals(e, o, delta, message);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(message));
        }
        try
        {
            ExtraAssertions.assertArrayEquals(e, o, delta, messageSupplier);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(messageSupplier.get()));
        }
        try
        {
            ExtraAssertions.assertArrayEquals(o, e, delta, message);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(message));
        }
        try
        {
            ExtraAssertions.assertArrayEquals(o, e, delta, messageSupplier);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(messageSupplier.get()));
        }
        try
        {
            ExtraAssertions.assertArrayEquals(e, o, delta, nullMessage);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
        try
        {
            ExtraAssertions.assertArrayEquals(e, o, delta, nullMessageSupplier);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
    }

    @Test
    public void testAssertObjectArrayEqualsDoubleThrowsUsingBadThreshold()
    {
        // This does not throw on a bad delta if e==o so create a new object
        final double[][][] e = new double[][][] { { { 1 } } };
        final double[][][] o = new double[][][] { { { 1 } } };
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, 0);
        }, "0");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, Double.NaN);
        }, "NaN");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, -1);
        }, "-1");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, new Integer(1));
        }, "Integer(1)");

        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, 0, message);
        }, "0");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, Double.NaN, message);
        }, "NaN");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, -1, message);
        }, "-1");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, new Integer(1), message);
        }, "Integer(1)");

        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, 0, messageSupplier);
        }, "0");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, Double.NaN, messageSupplier);
        }, "NaN");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, -1, messageSupplier);
        }, "-1");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, new Integer(1), messageSupplier);
        }, "Integer(1)");
    }

    @Test
    public void testAssertObjectArrayEqualsDoubleErrorsUp()
    {
        // Move further apart they should be not equal
        final double[][][] e = new double[][][] { { { 2 } } };
        final double[][][] o = new double[][][] { { { 4 } } };
        final double delta = new Double(1);
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, delta);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, delta, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, delta, messageSupplier);
        });
    }

    @Test
    public void testAssertObjectArrayEqualsDoubleErrorsDown()
    {
        // Move further apart they should be not equal
        final double[][][] e = new double[][][] { { { 2 } } };
        final double[][][] o = new double[][][] { { { 0 } } };
        final double delta = new Double(1);
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, delta);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, delta, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, delta, messageSupplier);
        });
    }

    @Test
    public void testAssertObjectArrayEqualsDoubleThrowsWithNullArrays()
    {
        final double[][][] e = new double[][][] { { { 1 } } };
        final double[][][] o = null;
        final double delta = new Double(1);
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, delta);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, delta, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, delta, messageSupplier);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(o, e, delta);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(o, e, delta, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(o, e, delta, messageSupplier);
        });
    }

    @Test
    public void testAssertObjectArrayEqualsDoubleThrowsWithMismatchedArrays()
    {
        final double[][][] e = new double[][][] { { { 1 } } };
        final double[][][] o = new double[][][] { { { 1, 2 } } };
        final double delta = new Double(1);
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, delta);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, delta, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, delta, messageSupplier);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(o, e, delta);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(o, e, delta, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(o, e, delta, messageSupplier);
        });
    }
    
    @Test
    public void testAssertObjectArrayEqualsDoubleThrowsWithNonArrayElements()
    {
        final Object object = new Object();
        final Object inner1 = new Object[] { object }; 
        final Object inner2 = new Object[] { object }; 
        final Object inner3 = new Object();
        
        Object[] e = new Object[][] { { inner1, inner2 } };
        Object[] o = new Object[][] { { inner1, inner2 } };
        final double delta = new Double(1);
        ExtraAssertions.assertArrayEquals(e, o, delta);
        ExtraAssertions.assertArrayEquals(o, e, delta);

        e = new Object[][] { { object, object } };
        o = new Object[][] { { object, object } };
        ExtraAssertions.assertArrayEquals(e, o, delta);
        ExtraAssertions.assertArrayEquals(o, e, delta);

        final Object[] e2 = new Object[][] { { inner1, inner2 } };
        final Object[] o2 = new Object[][] { { inner1, inner3 } };
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e2, o2, delta);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e2, o2, delta, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e2, o2, delta, messageSupplier);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(o2, e2, delta);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(o2, e2, delta, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(o2, e2, delta, messageSupplier);
        });
    }

    // XXX - Copy to here
    @Test
    public void testAssertEqualsRelativeFloat()
    {
        float e = floatL;
        float o = floatL;
        ExtraAssertions.assertEqualsRelative(e, o, Float.MIN_VALUE);
        ExtraAssertions.assertEqualsRelative(e, o, Float.MIN_VALUE, message);
        ExtraAssertions.assertEqualsRelative(e, o, Float.MIN_VALUE, messageSupplier);
        ExtraAssertions.assertEqualsRelative(e, o, Float.MIN_VALUE, nullMessage);
        ExtraAssertions.assertEqualsRelative(e, o, Float.MIN_VALUE, nullMessageSupplier);

        o = floatH;
        ExtraAssertions.assertEqualsRelative(e, o, relativeError);
        ExtraAssertions.assertEqualsRelative(e, o, relativeError, message);
        ExtraAssertions.assertEqualsRelative(e, o, relativeError, messageSupplier);
        ExtraAssertions.assertEqualsRelative(e, o, relativeError, nullMessage);
        ExtraAssertions.assertEqualsRelative(e, o, relativeError, nullMessageSupplier);

        e = floatLU;
        o = floatH;
        ExtraAssertions.assertEqualsRelative(e, o, relativeError);
        ExtraAssertions.assertEqualsRelative(e, o, relativeError, message);
        ExtraAssertions.assertEqualsRelative(e, o, relativeError, messageSupplier);
        ExtraAssertions.assertEqualsRelative(e, o, relativeError, nullMessage);
        ExtraAssertions.assertEqualsRelative(e, o, relativeError, nullMessageSupplier);

        e = floatL;
        o = floatHD;
        ExtraAssertions.assertEqualsRelative(e, o, relativeError);
        ExtraAssertions.assertEqualsRelative(e, o, relativeError, message);
        ExtraAssertions.assertEqualsRelative(e, o, relativeError, messageSupplier);
        ExtraAssertions.assertEqualsRelative(e, o, relativeError, nullMessage);
        ExtraAssertions.assertEqualsRelative(e, o, relativeError, nullMessageSupplier);
    }

    @Test
    public void testAssertEqualsRelativeFloatThrowsWithFormattedMessage()
    {
        final float e = 1;
        final float o = 2;
        try
        {
            ExtraAssertions.assertEqualsRelative(e, o, Float.MIN_VALUE, message);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(message));
        }
        try
        {
            ExtraAssertions.assertEqualsRelative(e, o, Float.MIN_VALUE, messageSupplier);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(messageSupplier.get()));
        }
        try
        {
            ExtraAssertions.assertEqualsRelative(o, e, Float.MIN_VALUE, message);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(message));
        }
        try
        {
            ExtraAssertions.assertEqualsRelative(o, e, Float.MIN_VALUE, messageSupplier);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(messageSupplier.get()));
        }
        try
        {
            ExtraAssertions.assertEqualsRelative(e, o, Float.MIN_VALUE, nullMessage);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
        try
        {
            ExtraAssertions.assertEqualsRelative(e, o, Float.MIN_VALUE, nullMessageSupplier);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
    }

    @Test
    public void testAssertEqualsRelativeFloatThrowsUsingBadThreshold()
    {
        final float e = 1;
        final float o = 1;
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertEqualsRelative(e, o, 0);
        }, "0");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertEqualsRelative(e, o, 2);
        }, "2");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertEqualsRelative(e, o, Float.POSITIVE_INFINITY);
        }, "+Inf");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertEqualsRelative(e, o, Float.NaN);
        }, "NaN");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertEqualsRelative(e, o, -1);
        }, "-1");

        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertEqualsRelative(e, o, 0, message);
        }, "0");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertEqualsRelative(e, o, 2, message);
        }, "2");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertEqualsRelative(e, o, Float.POSITIVE_INFINITY, message);
        }, "+Inf");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertEqualsRelative(e, o, Float.NaN, message);
        }, "NaN");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertEqualsRelative(e, o, -1, message);
        }, "-1");

        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertEqualsRelative(e, o, 0, messageSupplier);
        }, "0");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertEqualsRelative(e, o, 2, messageSupplier);
        }, "2");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertEqualsRelative(e, o, Float.POSITIVE_INFINITY, messageSupplier);
        }, "+Inf");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertEqualsRelative(e, o, Float.NaN, messageSupplier);
        }, "NaN");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertEqualsRelative(e, o, -1, messageSupplier);
        }, "-1");
    }

    @Test
    public void testAssertEqualsRelativeFloatErrorsUp()
    {
        // Move further apart they should be not equal
        final float e = floatL;
        final float o = floatHU;
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertEqualsRelative(e, o, relativeError);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertEqualsRelative(e, o, relativeError, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertEqualsRelative(e, o, relativeError, messageSupplier);
        });
    }

    @Test
    public void testAssertEqualsRelativeFloatErrorsDown()
    {
        // Move further apart they should be not equal
        final float e = floatLD;
        final float o = floatH;
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertEqualsRelative(e, o, relativeError);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertEqualsRelative(e, o, relativeError, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertEqualsRelative(e, o, relativeError, messageSupplier);
        });
    }

    @Test
    public void testAssertArrayEqualsRelativeFloat()
    {
        float[] e = new float[] { floatL };
        // Check with self
        ExtraAssertions.assertArrayEqualsRelative(e, e, Float.MIN_VALUE);
        ExtraAssertions.assertArrayEqualsRelative(e, e, Float.MIN_VALUE, message);
        ExtraAssertions.assertArrayEqualsRelative(e, e, Float.MIN_VALUE, messageSupplier);
        ExtraAssertions.assertArrayEqualsRelative(e, e, Float.MIN_VALUE, nullMessage);
        ExtraAssertions.assertArrayEqualsRelative(e, e, Float.MIN_VALUE, nullMessageSupplier);

        float[] o = new float[] { floatL };
        ExtraAssertions.assertArrayEqualsRelative(e, o, Float.MIN_VALUE);
        ExtraAssertions.assertArrayEqualsRelative(e, o, Float.MIN_VALUE, message);
        ExtraAssertions.assertArrayEqualsRelative(e, o, Float.MIN_VALUE, messageSupplier);
        ExtraAssertions.assertArrayEqualsRelative(e, o, Float.MIN_VALUE, nullMessage);
        ExtraAssertions.assertArrayEqualsRelative(e, o, Float.MIN_VALUE, nullMessageSupplier);

        o = new float[] { floatH };
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, message);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, messageSupplier);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, nullMessage);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, nullMessageSupplier);

        e = new float[] { floatLU };
        o = new float[] { floatH };
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, message);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, messageSupplier);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, nullMessage);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, nullMessageSupplier);

        e = new float[] { floatL };
        o = new float[] { floatHD };
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, message);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, messageSupplier);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, nullMessage);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, nullMessageSupplier);
    }

    @Test
    public void testAssertArrayEqualsRelativeFloatThrowsWithFormattedMessage()
    {
        final float[] e = new float[] { 1 };
        final float[] o = new float[] { 2 };
        try
        {
            ExtraAssertions.assertArrayEqualsRelative(e, o, Float.MIN_VALUE, message);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(message));
        }
        try
        {
            ExtraAssertions.assertArrayEqualsRelative(e, o, Float.MIN_VALUE, messageSupplier);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(messageSupplier.get()));
        }
        try
        {
            ExtraAssertions.assertArrayEqualsRelative(o, e, Float.MIN_VALUE, message);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(message));
        }
        try
        {
            ExtraAssertions.assertArrayEqualsRelative(o, e, Float.MIN_VALUE, messageSupplier);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(messageSupplier.get()));
        }
        try
        {
            ExtraAssertions.assertArrayEqualsRelative(e, o, Float.MIN_VALUE, nullMessage);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
        try
        {
            ExtraAssertions.assertArrayEqualsRelative(e, o, Float.MIN_VALUE, nullMessageSupplier);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
        try
        {
            ExtraAssertions.assertArrayEqualsRelative(o, e, Float.MIN_VALUE, nullMessage);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
        try
        {
            ExtraAssertions.assertArrayEqualsRelative(o, e, Float.MIN_VALUE, nullMessageSupplier);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
    }

    @Test
    public void testAssertArrayEqualsRelativeFloatThrowsUsingBadThreshold()
    {
        final float[] e = new float[] { 1 };
        for (final float[] o : new float[][] { null, e })
        {
            Assertions.assertThrows(AssertionFailedError.class, () -> {
                ExtraAssertions.assertArrayEqualsRelative(e, o, 0);
            }, "0");
            Assertions.assertThrows(AssertionFailedError.class, () -> {
                ExtraAssertions.assertArrayEqualsRelative(e, o, 2);
            }, "2");
            Assertions.assertThrows(AssertionFailedError.class, () -> {
                ExtraAssertions.assertArrayEqualsRelative(e, o, Float.POSITIVE_INFINITY);
            }, "+Inf");
            Assertions.assertThrows(AssertionFailedError.class, () -> {
                ExtraAssertions.assertArrayEqualsRelative(e, o, Float.NaN);
            }, "NaN");
            Assertions.assertThrows(AssertionFailedError.class, () -> {
                ExtraAssertions.assertArrayEqualsRelative(e, o, -1);
            }, "-1");

            Assertions.assertThrows(AssertionFailedError.class, () -> {
                ExtraAssertions.assertArrayEqualsRelative(e, o, 0, message);
            }, "0");
            Assertions.assertThrows(AssertionFailedError.class, () -> {
                ExtraAssertions.assertArrayEqualsRelative(e, o, 2, message);
            }, "2");
            Assertions.assertThrows(AssertionFailedError.class, () -> {
                ExtraAssertions.assertArrayEqualsRelative(e, o, Float.POSITIVE_INFINITY, message);
            }, "+Inf");
            Assertions.assertThrows(AssertionFailedError.class, () -> {
                ExtraAssertions.assertArrayEqualsRelative(e, o, Float.NaN, message);
            }, "NaN");
            Assertions.assertThrows(AssertionFailedError.class, () -> {
                ExtraAssertions.assertArrayEqualsRelative(e, o, -1, message);
            }, "-1");

            Assertions.assertThrows(AssertionFailedError.class, () -> {
                ExtraAssertions.assertArrayEqualsRelative(e, o, 0, messageSupplier);
            }, "0");
            Assertions.assertThrows(AssertionFailedError.class, () -> {
                ExtraAssertions.assertArrayEqualsRelative(e, o, 2, messageSupplier);
            }, "2");
            Assertions.assertThrows(AssertionFailedError.class, () -> {
                ExtraAssertions.assertArrayEqualsRelative(e, o, Float.POSITIVE_INFINITY, messageSupplier);
            }, "+Inf");
            Assertions.assertThrows(AssertionFailedError.class, () -> {
                ExtraAssertions.assertArrayEqualsRelative(e, o, Float.NaN, messageSupplier);
            }, "NaN");
            Assertions.assertThrows(AssertionFailedError.class, () -> {
                ExtraAssertions.assertArrayEqualsRelative(e, o, -1, messageSupplier);
            }, "-1");
        }
    }

    @Test
    public void testAssertArrayEqualsRelativeFloatErrorsUp()
    {
        // Move further apart they should be not equal
        final float[] e = new float[] { floatL };
        final float[] o = new float[] { floatHU };
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, messageSupplier);
        });
    }

    @Test
    public void testAssertArrayEqualsRelativeFloatErrorsDown()
    {
        // Move further apart they should be not equal
        final float[] e = new float[] { floatLD };
        final float[] o = new float[] { floatH };
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, messageSupplier);
        });
    }

    @Test
    public void testAssertArrayEqualsRelativeFloatThrowsWithNullArrays()
    {
        final float[] e = new float[] { 1 };
        final float[] o = null;
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, messageSupplier);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(o, e, relativeError);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(o, e, relativeError, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(o, e, relativeError, messageSupplier);
        });
    }

    @Test
    public void testAssertArrayEqualsRelativeFloatThrowsWithMismatchedArrays()
    {
        final float[] e = new float[] { 1 };
        final float[] o = new float[] { 1, 2 };
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, messageSupplier);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(o, e, relativeError);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(o, e, relativeError, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(o, e, relativeError, messageSupplier);
        });
    }

    @Test
    public void testAssertObjectArrayEqualsRelativeFloat()
    {
        float[][][] e = new float[][][] { { { floatL } } };
        // Check with self
        ExtraAssertions.assertArrayEqualsRelative(e, e, Float.MIN_VALUE);
        ExtraAssertions.assertArrayEqualsRelative(e, e, Float.MIN_VALUE, message);
        ExtraAssertions.assertArrayEqualsRelative(e, e, Float.MIN_VALUE, messageSupplier);
        ExtraAssertions.assertArrayEqualsRelative(e, e, Float.MIN_VALUE, nullMessage);
        ExtraAssertions.assertArrayEqualsRelative(e, e, Float.MIN_VALUE, nullMessageSupplier);

        float[][][] o = new float[][][] { { { floatL } } };
        ExtraAssertions.assertArrayEqualsRelative(e, o, Float.MIN_VALUE);
        ExtraAssertions.assertArrayEqualsRelative(e, o, Float.MIN_VALUE, message);
        ExtraAssertions.assertArrayEqualsRelative(e, o, Float.MIN_VALUE, messageSupplier);
        ExtraAssertions.assertArrayEqualsRelative(e, o, Float.MIN_VALUE, nullMessage);
        ExtraAssertions.assertArrayEqualsRelative(e, o, Float.MIN_VALUE, nullMessageSupplier);

        o = new float[][][] { { { floatH } } };
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, message);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, messageSupplier);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, nullMessage);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, nullMessageSupplier);

        e = new float[][][] { { { floatLU } } };
        o = new float[][][] { { { floatH } } };
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, message);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, messageSupplier);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, nullMessage);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, nullMessageSupplier);

        e = new float[][][] { { { floatL } } };
        o = new float[][][] { { { floatHD } } };
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, message);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, messageSupplier);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, nullMessage);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, nullMessageSupplier);

        // At least one of the same element
        float[] element = new float[] { 1 };
        e = new float[][][] { { element, { 2 } } };
        o = new float[][][] { { element, { 2 } } };
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, message);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, messageSupplier);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, nullMessage);
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, nullMessageSupplier);
    }

    @Test
    public void testAssertObjectArrayEqualsRelativeFloatThrowsWithFormattedMessage()
    {
        final float[][][] e = new float[][][] { { { 1 } } };
        final float[][][] o = new float[][][] { { { 2 } } };
        try
        {
            ExtraAssertions.assertArrayEqualsRelative(e, o, Float.MIN_VALUE, message);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(message));
        }
        try
        {
            ExtraAssertions.assertArrayEqualsRelative(e, o, Float.MIN_VALUE, messageSupplier);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(messageSupplier.get()));
        }
        try
        {
            ExtraAssertions.assertArrayEqualsRelative(o, e, Float.MIN_VALUE, message);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(message));
        }
        try
        {
            ExtraAssertions.assertArrayEqualsRelative(o, e, Float.MIN_VALUE, messageSupplier);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(messageSupplier.get()));
        }
        try
        {
            ExtraAssertions.assertArrayEqualsRelative(e, o, Float.MIN_VALUE, nullMessage);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
        try
        {
            ExtraAssertions.assertArrayEqualsRelative(e, o, Float.MIN_VALUE, nullMessageSupplier);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
        try
        {
            ExtraAssertions.assertArrayEqualsRelative(o, e, Float.MIN_VALUE, nullMessage);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
        try
        {
            ExtraAssertions.assertArrayEqualsRelative(o, e, Float.MIN_VALUE, nullMessageSupplier);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains("expected"));
            Assertions.assertTrue(ex.getMessage().contains("but was"));
        }
    }

    @Test
    public void testAssertObjectArrayEqualsRelativeFloatThrowsUsingBadThreshold()
    {
        // This does not throw on a bad delta if e==o so create a new object
        final float[][][] e = new float[][][] { { { 1 } } };
        final float[][][] o = new float[][][] { { { 1 } } };
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, 0);
        }, "0");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, 2);
        }, "2");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, Float.POSITIVE_INFINITY);
        }, "+Inf");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, Float.NaN);
        }, "NaN");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, -1);
        }, "-1");

        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, 0, message);
        }, "0");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, 2, message);
        }, "2");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, Float.POSITIVE_INFINITY, message);
        }, "+Inf");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, Float.NaN, message);
        }, "NaN");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, -1, message);
        }, "-1");

        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, 0, messageSupplier);
        }, "0");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, 2, messageSupplier);
        }, "2");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, Float.POSITIVE_INFINITY, messageSupplier);
        }, "+Inf");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, Float.NaN, messageSupplier);
        }, "NaN");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, -1, messageSupplier);
        }, "-1");
    }

    @Test
    public void testAssertObjectArrayEqualsRelativeFloatErrorsUp()
    {
        // Move further apart they should be not equal
        final float[][][] e = new float[][][] { { { floatL } } };
        final float[][][] o = new float[][][] { { { floatHU } } };
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, messageSupplier);
        });
    }

    @Test
    public void testAssertObjectArrayEqualsRelativeFloatErrorsDown()
    {
        // Move further apart they should be not equal
        final float[][][] e = new float[][][] { { { floatLD } } };
        final float[][][] o = new float[][][] { { { floatH } } };
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, messageSupplier);
        });
    }

    @Test
    public void testAssertObjectArrayEqualsRelativeFloatThrowsWithNullArrays()
    {
        final float[][][] e = new float[][][] { { { 1 } } };
        final float[][][] o = null;
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, messageSupplier);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(o, e, relativeError);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(o, e, relativeError, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(o, e, relativeError, messageSupplier);
        });
    }

    @Test
    public void testAssertObjectArrayEqualsRelativeFloatThrowsWithMismatchedArrays()
    {
        final float[][][] e = new float[][][] { { { 1 } } };
        final float[][][] o = new float[][][] { { { 1, 2 } } };
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError, messageSupplier);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(o, e, relativeError);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(o, e, relativeError, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(o, e, relativeError, messageSupplier);
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
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError);
        ExtraAssertions.assertArrayEqualsRelative(o, e, relativeError);

        e = new Object[][] { { object, object } };
        o = new Object[][] { { object, object } };
        ExtraAssertions.assertArrayEqualsRelative(e, o, relativeError);
        ExtraAssertions.assertArrayEqualsRelative(o, e, relativeError);

        final Object[] e2 = new Object[][] { { inner1, inner2 } };
        final Object[] o2 = new Object[][] { { inner1, inner3 } };
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e2, o2, relativeError);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e2, o2, relativeError, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(e2, o2, relativeError, messageSupplier);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(o2, e2, relativeError);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(o2, e2, relativeError, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEqualsRelative(o2, e2, relativeError, messageSupplier);
        });
    }
    
    // Non-relative array equals

    @Test
    public void testAssertObjectArrayEqualsFloat()
    {
        float[][][] e = new float[][][] { { { 1 } } };
        // Check with self
        ExtraAssertions.assertArrayEquals(e, e, Float.MIN_VALUE);
        ExtraAssertions.assertArrayEquals(e, e, Float.MIN_VALUE, message);
        ExtraAssertions.assertArrayEquals(e, e, Float.MIN_VALUE, messageSupplier);
        ExtraAssertions.assertArrayEquals(e, e, Float.MIN_VALUE, nullMessage);
        ExtraAssertions.assertArrayEquals(e, e, Float.MIN_VALUE, nullMessageSupplier);

        float[][][] o = new float[][][] { { { 1 } } };
        final Float delta = new Float(1);
        ExtraAssertions.assertArrayEquals(e, o, delta);
        ExtraAssertions.assertArrayEquals(e, o, delta, message);
        ExtraAssertions.assertArrayEquals(e, o, delta, messageSupplier);
        ExtraAssertions.assertArrayEquals(e, o, delta, nullMessage);
        ExtraAssertions.assertArrayEquals(e, o, delta, nullMessageSupplier);

        o = new float[][][] { { { 2 } } };
        ExtraAssertions.assertArrayEquals(e, o, delta);
        ExtraAssertions.assertArrayEquals(e, o, delta, message);
        ExtraAssertions.assertArrayEquals(e, o, delta, messageSupplier);
        ExtraAssertions.assertArrayEquals(e, o, delta, nullMessage);
        ExtraAssertions.assertArrayEquals(e, o, delta, nullMessageSupplier);

        o = new float[][][] { { { 0 } } };
        ExtraAssertions.assertArrayEquals(e, o, delta);
        ExtraAssertions.assertArrayEquals(e, o, delta, message);
        ExtraAssertions.assertArrayEquals(e, o, delta, messageSupplier);
        ExtraAssertions.assertArrayEquals(e, o, delta, nullMessage);
        ExtraAssertions.assertArrayEquals(e, o, delta, nullMessageSupplier);
        
        // At least one of the same element
        final float[] element = new float[] { 1 };
        e = new float[][][] { { element, { 2 } } };
        o = new float[][][] { { element, { 2 } } };
        ExtraAssertions.assertArrayEquals(e, o, delta);
        ExtraAssertions.assertArrayEquals(e, o, delta, message);
        ExtraAssertions.assertArrayEquals(e, o, delta, messageSupplier);
        ExtraAssertions.assertArrayEquals(e, o, delta, nullMessage);
        ExtraAssertions.assertArrayEquals(e, o, delta, nullMessageSupplier);
    }

    @Test
    public void testAssertObjectArrayEqualsFloatThrowsWithFormattedMessage()
    {
        final float[][][] e = new float[][][] { { { 2 } } };
        final float[][][] o = new float[][][] { { { 4 } } };
        final Float delta = new Float(1);
        try
        {
            ExtraAssertions.assertArrayEquals(e, o, delta, message);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(message));
        }
        try
        {
            ExtraAssertions.assertArrayEquals(e, o, delta, messageSupplier);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(messageSupplier.get()));
        }
        try
        {
            ExtraAssertions.assertArrayEquals(o, e, delta, message);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(message));
        }
        try
        {
            ExtraAssertions.assertArrayEquals(o, e, delta, messageSupplier);
        }
        catch (final AssertionFailedError ex)
        {
            Assertions.assertTrue(ex.getMessage().contains(messageSupplier.get()));
        }
    }

    @Test
    public void testAssertObjectArrayEqualsFloatThrowsUsingBadThreshold()
    {
        // This does not throw on a bad delta if e==o so create a new object
        final float[][][] e = new float[][][] { { { 1 } } };
        final float[][][] o = new float[][][] { { { 1 } } };
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, 0);
        }, "0");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, Float.NaN);
        }, "NaN");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, -1);
        }, "-1");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, new Integer(1));
        }, "Integer(1)");

        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, 0, message);
        }, "0");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, Float.NaN, message);
        }, "NaN");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, -1, message);
        }, "-1");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, new Integer(1), message);
        }, "Integer(1)");

        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, 0, messageSupplier);
        }, "0");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, Float.NaN, messageSupplier);
        }, "NaN");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, -1, messageSupplier);
        }, "-1");
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, new Integer(1), messageSupplier);
        }, "Integer(1)");
    }

    @Test
    public void testAssertObjectArrayEqualsFloatErrorsUp()
    {
        // Move further apart they should be not equal
        final float[][][] e = new float[][][] { { { 2 } } };
        final float[][][] o = new float[][][] { { { 4 } } };
        final float delta = new Float(1);
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, delta);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, delta, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, delta, messageSupplier);
        });
    }

    @Test
    public void testAssertObjectArrayEqualsFloatErrorsDown()
    {
        // Move further apart they should be not equal
        final float[][][] e = new float[][][] { { { 2 } } };
        final float[][][] o = new float[][][] { { { 0 } } };
        final float delta = new Float(1);
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, delta);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, delta, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, delta, messageSupplier);
        });
    }

    @Test
    public void testAssertObjectArrayEqualsFloatThrowsWithNullArrays()
    {
        final float[][][] e = new float[][][] { { { 1 } } };
        final float[][][] o = null;
        final float delta = new Float(1);
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, delta);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, delta, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, delta, messageSupplier);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(o, e, delta);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(o, e, delta, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(o, e, delta, messageSupplier);
        });
    }

    @Test
    public void testAssertObjectArrayEqualsFloatThrowsWithMismatchedArrays()
    {
        final float[][][] e = new float[][][] { { { 1 } } };
        final float[][][] o = new float[][][] { { { 1, 2 } } };
        final float delta = new Float(1);
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, delta);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, delta, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e, o, delta, messageSupplier);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(o, e, delta);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(o, e, delta, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(o, e, delta, messageSupplier);
        });
    }
    
    @Test
    public void testAssertObjectArrayEqualsFloatThrowsWithNonArrayElements()
    {
        final Object object = new Object();
        final Object inner1 = new Object[] { object }; 
        final Object inner2 = new Object[] { object }; 
        final Object inner3 = new Object();
        
        Object[] e = new Object[][] { { inner1, inner2 } };
        Object[] o = new Object[][] { { inner1, inner2 } };
        final float delta = new Float(1);
        ExtraAssertions.assertArrayEquals(e, o, delta);
        ExtraAssertions.assertArrayEquals(o, e, delta);

        e = new Object[][] { { object, object } };
        o = new Object[][] { { object, object } };
        ExtraAssertions.assertArrayEquals(e, o, delta);
        ExtraAssertions.assertArrayEquals(o, e, delta);

        final Object[] e2 = new Object[][] { { inner1, inner2 } };
        final Object[] o2 = new Object[][] { { inner1, inner3 } };
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e2, o2, delta);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e2, o2, delta, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(e2, o2, delta, messageSupplier);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(o2, e2, delta);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(o2, e2, delta, message);
        });
        Assertions.assertThrows(AssertionFailedError.class, () -> {
            ExtraAssertions.assertArrayEquals(o2, e2, delta, messageSupplier);
        });
    }
}