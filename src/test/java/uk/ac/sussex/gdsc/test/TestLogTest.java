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
package uk.ac.sussex.gdsc.test;

import java.util.IllegalFormatConversionException;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import uk.ac.sussex.gdsc.test.TestLog.TestLevel;

@SuppressWarnings("javadoc")
public class TestLogTest
{
    private static Logger logger;

    @BeforeAll
    public static void beforeAll()
    {
        logger = Logger.getLogger(TestLogTest.class.getName());
    }

    @AfterAll
    public static void afterAll()
    {
        logger = null;
    }

    @Test
    public void canSupplyVarArgs()
    {
        TestLog.getSupplier("log varargs = %d %f", 1, 2.3).get();
    }

    @Test
    public void canSupplyObjectArray()
    {
        final Object[] args = new Object[] { 1, 2.3 };
        TestLog.getSupplier("log Object[] = %d %f", args).get();
    }

    @Test
    public void cannotSupplyObjectArrayAndVarargs()
    {
        // Use severe to always run
        Assumptions.assumeTrue(logger.isLoggable(Level.SEVERE));
        final Object[] args = new Object[] { 1, 2.3 };
        Assertions.assertThrows(IllegalFormatConversionException.class, () -> {
            TestLog.getSupplier("%d %f %d", args, 3).get();
        });
    }

    @Test
    public void canGetStaceTraceElement()
    {
        final StackTraceElement[] e = new Throwable().getStackTrace();
        final StackTraceElement o = TestLog.getStackTraceElement();
        final StackTraceElement o2 = TestLog.getStackTraceElement(1);
        Assertions.assertNotNull(e);
        Assertions.assertNotNull(o);
        Assertions.assertEquals(e[0].getClassName(), o.getClassName());
        Assertions.assertEquals(e[0].getMethodName(), o.getMethodName());
        Assertions.assertEquals(e[0].getLineNumber() + 1, o.getLineNumber());
        logger.info(() -> String.format("%s:%s:%d", o.getClassName(), o.getMethodName(), o.getLineNumber()));

        Assertions.assertNotNull(o2);
        Assertions.assertEquals(e[1].getClassName(), o2.getClassName());
        Assertions.assertEquals(e[1].getMethodName(), o2.getMethodName());
        Assertions.assertEquals(e[1].getLineNumber(), o2.getLineNumber());
    }

    @Test
    public void getStaceTraceElementIsNullWhenNotLocated()
    {
        final int size = new Throwable().getStackTrace().length;
        final StackTraceElement o = TestLog.getStackTraceElement(size);
        Assertions.assertNull(o);
    }

    @Test
    public void getStaceTraceElementThrowsWhenCountIsNegative()
    {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            TestLog.getStackTraceElement(-1);
        });
    }

    @Test
    public void canGetCodePoint()
    {
        final StackTraceElement e = new Throwable().getStackTrace()[0];
        final String codePoint = TestLog.getCodePoint();
        Assertions.assertNotNull(e);
        Assertions.assertNotNull(codePoint);
        final String[] o = codePoint.split(":");
        Assertions.assertEquals(e.getClassName(), o[0]);
        Assertions.assertEquals(e.getMethodName(), o[1]);
        Assertions.assertEquals(e.getLineNumber() + 1, Integer.parseInt(o[2]));
        logger.info(codePoint);
        Assertions.assertEquals("", TestLog.getCodePoint(null));
    }

    @Test
    public void canGetRecord()
    {
        for (final Level l : new Level[] { Level.INFO, Level.FINER })
        {
            //@formatter:off
            logger.log(check(l, TestLog.getRecord(l, "This is a record")));
            logger.log(check(l, TestLog.getRecord(l, "This is a formatted record: %d", 1)));
            logger.log(check(l, TestLog.getRecord(l, () -> String.format("This is a supplier record: %d", 1))));
            //@formatter:on
        }
    }

    private static LogRecord check(Level level, LogRecord record)
    {
        Assertions.assertEquals(level, record.getLevel());
        return record;
    }

    @Test
    public void canGetFailRecord()
    {
        //@formatter:off
        logger.log(check(TestLevel.TEST_FAILURE, TestLog.getFailRecord("This is a failed record")));
        logger.log(check(TestLevel.TEST_FAILURE, TestLog.getFailRecord( "This is a failed formatted record: %d", 1)));
        logger.log(check(TestLevel.TEST_FAILURE, TestLog.getFailRecord( () -> String.format("This is a failed supplier record: %d", 1))));
        logger.log(check(TestLevel.TEST_FAILURE, TestLog.getFailRecord(new Error("This is a failed record with thrown"))));
        //@formatter:on
    }

    @Test
    public void canGetTimingRecord()
    {
        //@formatter:off
        // Default levels
        logger.log(check(Level.INFO, TestLog.getTimingRecord("slow", 100, "fast", 10)));
        logger.log(check(TestLevel.TEST_FAILURE, TestLog.getTimingRecord("slow", 1, "fast", 10)));
        logger.log(check(Level.INFO, TestLog.getStageTimingRecord("slow", 100, "fast", 10)));
        logger.log(check(TestLevel.STAGE_FAILURE, TestLog.getStageTimingRecord("slow", 1, "fast", 10)));

        // Explicit levels
        // long version
        logger.log(check(Level.FINE, TestLog.getTimingRecord("slow", 100, "fast", 10, Level.FINE, Level.WARNING)));
        logger.log(check(Level.WARNING, TestLog.getTimingRecord("slow", 1, "fast", 10, Level.FINE, Level.WARNING)));
        // double version
        logger.log(check(Level.FINE, TestLog.getTimingRecord("slow", 100., "fast", 10., Level.FINE, Level.WARNING)));
        logger.log(check(Level.WARNING, TestLog.getTimingRecord("slow", 1., "fast", 10., Level.FINE, Level.WARNING)));

        // Using the TimingResult
        final TimingResult fast = new TimingResult("Fast", new long[] { 100 });
        final TimingResult slow = new TimingResult("Slow", new long[] { 1000 });
        final TimingResult slowFast = new TimingResult("SlowFast", new long[] { 100, 10000 });

        logger.log(check(Level.INFO, TestLog.getTimingRecord(slow, fast)));
        logger.log(check(Level.INFO, TestLog.getTimingRecord(slow, slowFast, true)));
        logger.log(check(TestLevel.TEST_FAILURE, TestLog.getTimingRecord(slow, slowFast, false)));
        logger.log(check(Level.INFO, TestLog.getStageTimingRecord(slow, fast)));
        logger.log(check(Level.INFO, TestLog.getStageTimingRecord(slow, slowFast, true)));
        logger.log(check(TestLevel.STAGE_FAILURE, TestLog.getStageTimingRecord(slow, slowFast, false)));
        //@formatter:on
    }

    private static class MessageSupplier implements Supplier<String>
    {
        int count = 0;

        @Override
        public String get()
        {
            count++;
            return "Lazy message";
        }
    }

    @Test
    public void canGetResultRecord()
    {
        //@formatter:off
        logger.log(TestLog.getResultRecord(true, "This is a test passed record"));
        logger.log(TestLog.getResultRecord(true, "This is a test passed formatted record: %d", 1));
        logger.log(TestLog.getStageResultRecord(true, "This is a test stage passed record"));
        logger.log(TestLog.getStageResultRecord(true, "This is a test stage passed formatted record: %d", 1));
        logger.log(TestLog.getResultRecord(false, "This is a test failure record"));
        logger.log(TestLog.getResultRecord(false, "This is a test failure formatted record: %d", 1));
        logger.log(TestLog.getStageResultRecord(false, "This is a test stage failure record"));
        logger.log(TestLog.getStageResultRecord(false, "This is a test stage failure formatted record: %d", 1));

        final MessageSupplier message = new MessageSupplier();
        Assertions.assertEquals(TestLevel.TEST_FAILURE.intValue(), TestLog.getResultRecord(false, "").getLevel().intValue());
        Assertions.assertEquals(TestLevel.TEST_FAILURE.intValue(), TestLog.getResultRecord(false, "%d", 1).getLevel().intValue());
        Assertions.assertEquals(TestLevel.TEST_FAILURE.intValue(), TestLog.getResultRecord(false, message).getLevel().intValue());
        Assertions.assertEquals(Level.INFO.intValue(), TestLog.getResultRecord(true, "").getLevel().intValue());
        Assertions.assertEquals(Level.INFO.intValue(), TestLog.getResultRecord(true, "%d", 1).getLevel().intValue());
        Assertions.assertEquals(Level.INFO.intValue(), TestLog.getResultRecord(true, message).getLevel().intValue());

        Assertions.assertEquals(TestLevel.STAGE_FAILURE.intValue(), TestLog.getStageResultRecord(false, "").getLevel().intValue());
        Assertions.assertEquals(TestLevel.STAGE_FAILURE.intValue(), TestLog.getStageResultRecord(false, "%d", 1).getLevel().intValue());
        Assertions.assertEquals(TestLevel.STAGE_FAILURE.intValue(), TestLog.getStageResultRecord(false, message).getLevel().intValue());
        Assertions.assertEquals(Level.INFO.intValue(), TestLog.getStageResultRecord(true, "").getLevel().intValue());
        Assertions.assertEquals(Level.INFO.intValue(), TestLog.getStageResultRecord(true, "%d", 1).getLevel().intValue());
        Assertions.assertEquals(Level.INFO.intValue(), TestLog.getStageResultRecord(true, message).getLevel().intValue());
        //@formatter:on
    }

    @Test
    public void canLazyLoadRecordMessage()
    {
        MessageSupplier message = new MessageSupplier();

        if (logger.isLoggable(TestLevel.TEST_FAILURE))
        {
            message = new MessageSupplier();
            logger.log(TestLog.getResultRecord(false, message));
            Assertions.assertEquals(1, message.count);
        }

        if (!logger.isLoggable(Level.INFO))
        {
            message = new MessageSupplier();
            logger.log(TestLog.getResultRecord(true, message));
            // Should not try to create the message
            Assertions.assertEquals(0, message.count);
        }
    }
}
