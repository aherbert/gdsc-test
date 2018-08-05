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

    //@formatter:off

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
    public void canStaceTraceElement()
    {
        final StackTraceElement e = new Throwable().getStackTrace()[0];
        final StackTraceElement o = TestLog.getStaceTraceElement();
        Assertions.assertNotNull(e);
        Assertions.assertNotNull(o);
        Assertions.assertEquals(e.getClassName(), o.getClassName());
        Assertions.assertEquals(e.getMethodName(), o.getMethodName());
        Assertions.assertEquals(e.getLineNumber() + 1, o.getLineNumber());
        logger.info(() -> String.format("%s:%s:%d", o.getClassName(), o.getMethodName(), o.getLineNumber()));
    }

    @Test
    public void canGetRecord()
    {
        for (final Level l : new Level[] { Level.INFO, Level.FINER}) {
            logger.log(check(l, TestLog.getRecord(l, "This is a record")));
            logger.log(check(l, TestLog.getRecord(l, "This is a formatted record: %d", 1)));
            logger.log(check(l, TestLog.getRecord(l, () -> String.format("This is a supplier record: %d", 1))));
        }
    }

    private static LogRecord check(Level level, LogRecord record)
    {
        Assertions.assertEquals(level, record.getLevel());
        return record;
    }

    @Test
    public void canGetFailureRecord()
    {
         logger.log(check(TestLevel.TEST_FAILURE, TestLog.getFailRecord("This is a failed record")));
         logger.log(check(TestLevel.TEST_FAILURE, TestLog.getFailRecord( "This is a failed formatted record: %d", 1)));
         logger.log(check(TestLevel.TEST_FAILURE, TestLog.getFailRecord( () -> String.format("This is a failed supplier record: %d", 1))));
    }

    @Test
    public void canGetTimingResultRecord()
    {
        final TimingTask fast = new NamedTimingTask("Fast");
        final TimingTask slow = new NamedTimingTask("Slow");
        final TimingResult fastR = new TimingResult(fast, new long[] { 100 });
        final TimingResult slowR = new TimingResult(slow, new long[] { 1000 });
        final TimingResult slowFastR = new TimingResult(fast, new long[] { 10000 });

         logger.log(check(Level.INFO, TestLog.getRecord(slowR, fastR)));
         logger.log(check(TestLevel.TEST_FAILURE, TestLog.getRecord(slowR, slowFastR)));
         logger.log(check(Level.INFO, TestLog.getStageRecord(slowR, fastR)));
         logger.log(check(TestLevel.STAGE_FAILURE, TestLog.getStageRecord(slowR, slowFastR)));
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
        logger.log(TestLog.getRecord(true, "This is a test passed record"));
        logger.log(TestLog.getRecord(true, "This is a test passed formatted record: %d", 1));
        logger.log(TestLog.getStageRecord(true, "This is a test stage passed record"));
        logger.log(TestLog.getStageRecord(true, "This is a test stage passed formatted record: %d", 1));
        logger.log(TestLog.getRecord(false, "This is a test failure record"));
        logger.log(TestLog.getRecord(false, "This is a test failure formatted record: %d", 1));
        logger.log(TestLog.getStageRecord(false, "This is a test stage failure record"));
        logger.log(TestLog.getStageRecord(false, "This is a test stage failure formatted record: %d", 1));

        MessageSupplier message = new MessageSupplier();
        Assertions.assertEquals(TestLevel.TEST_FAILURE.intValue(), TestLog.getRecord(false, "").getLevel().intValue());
        Assertions.assertEquals(TestLevel.TEST_FAILURE.intValue(), TestLog.getRecord(false, "%d", 1).getLevel().intValue());
        Assertions.assertEquals(TestLevel.TEST_FAILURE.intValue(), TestLog.getRecord(false, message).getLevel().intValue());
        Assertions.assertEquals(Level.INFO.intValue(), TestLog.getRecord(true, "").getLevel().intValue());
        Assertions.assertEquals(Level.INFO.intValue(), TestLog.getRecord(true, "%d", 1).getLevel().intValue());
        Assertions.assertEquals(Level.INFO.intValue(), TestLog.getRecord(true, message).getLevel().intValue());

        Assertions.assertEquals(TestLevel.STAGE_FAILURE.intValue(), TestLog.getStageRecord(false, "").getLevel().intValue());
        Assertions.assertEquals(TestLevel.STAGE_FAILURE.intValue(), TestLog.getStageRecord(false, "%d", 1).getLevel().intValue());
        Assertions.assertEquals(TestLevel.STAGE_FAILURE.intValue(), TestLog.getStageRecord(false, message).getLevel().intValue());
        Assertions.assertEquals(Level.INFO.intValue(), TestLog.getStageRecord(true, "").getLevel().intValue());
        Assertions.assertEquals(Level.INFO.intValue(), TestLog.getStageRecord(true, "%d", 1).getLevel().intValue());
        Assertions.assertEquals(Level.INFO.intValue(), TestLog.getStageRecord(true, message).getLevel().intValue());
    }

    @Test
    public void canLazyLoadRecordMessage()
    {
        MessageSupplier message = new MessageSupplier();

        if (logger.isLoggable(TestLevel.TEST_FAILURE))
        {
            message = new MessageSupplier();
            logger.log(TestLog.getRecord(false, message));
            Assertions.assertEquals(1, message.count);
        }

        if (!logger.isLoggable(Level.INFO))
        {
            message = new MessageSupplier();
            logger.log(TestLog.getRecord(true, message));
            // Should not try to create the message
            Assertions.assertEquals(0, message.count);
        }        
    }
}
