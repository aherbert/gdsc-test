/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
 *
 * Contains utilities for use with test frameworks.
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
package uk.ac.sussex.gdsc.test.utils;

import static uk.ac.sussex.gdsc.test.utils.TestLog.TestLevel.TEST_INFO;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import uk.ac.sussex.gdsc.test.utils.TestLog.TestLevel;

@SuppressWarnings("javadoc")
public class TestLogTest {
    private static Logger logger;

    @BeforeAll
    public static void beforeAll() {
        logger = Logger.getLogger(TestLogTest.class.getName());
    }

    @AfterAll
    public static void afterAll() {
        logger = null;
    }

    @Test
    public void canGetStaceTraceElement() {
        final StackTraceElement[] e = new Throwable().getStackTrace();
        final StackTraceElement o = TestLog.getStackTraceElement();
        final StackTraceElement o2 = TestLog.getStackTraceElement(1);
        Assertions.assertNotNull(e);
        Assertions.assertNotNull(o);
        Assertions.assertEquals(e[0].getClassName(), o.getClassName());
        Assertions.assertEquals(e[0].getMethodName(), o.getMethodName());
        Assertions.assertEquals(e[0].getLineNumber() + 1, o.getLineNumber());
        logger.log(TEST_INFO, () -> String.format("%s:%s:%d", o.getClassName(), o.getMethodName(), o.getLineNumber()));

        Assertions.assertNotNull(o2);
        Assertions.assertEquals(e[1].getClassName(), o2.getClassName());
        Assertions.assertEquals(e[1].getMethodName(), o2.getMethodName());
        Assertions.assertEquals(e[1].getLineNumber(), o2.getLineNumber());
    }

    @Test
    public void getStaceTraceElementIsNullWhenNotLocated() {
        final int size = new Throwable().getStackTrace().length;
        final StackTraceElement o = TestLog.getStackTraceElement(size);
        Assertions.assertNull(o);
    }

    @Test
    public void getStaceTraceElementThrowsWhenCountIsNegative() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            TestLog.getStackTraceElement(-1);
        });
    }

    @Test
    public void canGetCodePoint() {
        final StackTraceElement e = new Throwable().getStackTrace()[0];
        final String codePoint = TestLog.getCodePoint();
        Assertions.assertNotNull(e);
        Assertions.assertNotNull(codePoint);
        final String[] o = codePoint.split(":");
        Assertions.assertEquals(e.getClassName(), o[0]);
        Assertions.assertEquals(e.getMethodName(), o[1]);
        Assertions.assertEquals(e.getLineNumber() + 1, Integer.parseInt(o[2]));
        logger.log(TEST_INFO, codePoint);
        Assertions.assertEquals("", TestLog.getCodePoint(null));
    }

    @Test
    public void canGetRecord() {
        final StringBuilder sb = new StringBuilder("A StringBuilder passed as an object");
        for (final Level l : new Level[] { TestLevel.TEST_INFO, TestLevel.TEST_DEBUG }) {
            //@formatter:off
            logger.log(check(l, TestLog.getRecord(l, "This is a record"), "This is a record"));
            logger.log(check(l, TestLog.getRecord(l, "This is a formatted record: %d", 1), String.format("This is a formatted record: %d", 1)));
            logger.log(check(l, TestLog.getRecord(l, () -> String.format("This is a supplier record: %d", 1)), String.format("This is a supplier record: %d", 1)));
            logger.log(check(l, TestLog.getRecord(l, sb), sb.toString()));
            //@formatter:on
        }
    }

    private static LogRecord check(Level level, LogRecord record, String expected) {
        Assertions.assertEquals(level, record.getLevel());
        Assertions.assertEquals(expected, record.getMessage());
        return record;
    }

    private static LogRecord check(Level level, LogRecord record, String... contains) {
        Assertions.assertEquals(level, record.getLevel());
        String msg = record.getMessage();
        for (String expected : contains)
            Assertions.assertTrue(msg.contains(expected), expected);
        return record;
    }

    @Test
    public void canGetFailRecord() {
        //@formatter:off
        logger.log(check(TestLevel.TEST_FAILURE, TestLog.getFailRecord("This is a failed record"), "This is a failed record"));
        logger.log(check(TestLevel.TEST_FAILURE, TestLog.getFailRecord("This is a failed formatted record: %d", 1), String.format("This is a failed formatted record: %d", 1)));
        logger.log(check(TestLevel.TEST_FAILURE, TestLog.getFailRecord(() -> String.format("This is a failed supplier record: %d", 1)), String.format("This is a failed supplier record: %d", 1)));
        //@formatter:on

        // Since the stack trace looks bad in the output create a dummy one
        Throwable t = new Throwable("This is a failed record with thrown");
        StackTraceElement[] trace = t.getStackTrace();
        trace = Arrays.copyOf(trace, 2);
        final String className = this.getClass().getSimpleName();
        trace[1] = new StackTraceElement("stack.trace.truncated.for." + className, "itWouldBeLonger", "DummyClass.java",
                10);
        t.setStackTrace(trace);
        logger.log(check(TestLevel.TEST_FAILURE, TestLog.getFailRecord(t), "This is a failed record with thrown"));
    }

    @Test
    public void canGetTimingRecord() {
        //@formatter:off
        // Default levels
        logger.log(check(TestLevel.TEST_INFO, TestLog.getTimingRecord("slow", 123, "fast", 21), "slow", "fast", "123", "21"));
        logger.log(check(TestLevel.TEST_FAILURE, TestLog.getTimingRecord("slow", 7, "fast", 21), "slow", "fast", "7", "21"));
        logger.log(check(TestLevel.TEST_INFO, TestLog.getStageTimingRecord("slow", 123, "fast", 21), "slow", "fast", "123", "21"));
        logger.log(check(TestLevel.TEST_WARNING, TestLog.getStageTimingRecord("slow", 7, "fast", 21), "slow", "fast", "7", "21"));

        // Explicit levels
        // long version
        logger.log(check(Level.FINER, TestLog.getTimingRecord("slow", 123, "fast", 21, Level.FINER, Level.WARNING), "slow", "fast", "123", "21"));
        logger.log(check(Level.WARNING, TestLog.getTimingRecord("slow", 7, "fast", 21, Level.FINER, Level.WARNING), "slow", "fast", "7", "21"));
        // double version
        logger.log(check(Level.FINER, TestLog.getTimingRecord("slow", 123., "fast", 21., Level.FINER, Level.WARNING), "slow", "fast", "123.", "21."));
        logger.log(check(Level.WARNING, TestLog.getTimingRecord("slow", 7., "fast", 21., Level.FINER, Level.WARNING), "slow", "fast", "7.", "21."));

        // Using the TimingResult
        final TimingResult fast = new TimingResult("Fast", new long[] { 100 });
        final TimingResult slow = new TimingResult("Slow", new long[] { 1000 });
        final TimingResult slowFast = new TimingResult("SlowFast", new long[] { 100, 10000 });

        logger.log(check(TestLevel.TEST_INFO, TestLog.getTimingRecord(slow, fast)));
        logger.log(check(TestLevel.TEST_INFO, TestLog.getTimingRecord(slow, slowFast, true)));
        logger.log(check(TestLevel.TEST_FAILURE, TestLog.getTimingRecord(slow, slowFast, false)));
        logger.log(check(TestLevel.TEST_INFO, TestLog.getStageTimingRecord(slow, fast)));
        logger.log(check(TestLevel.TEST_INFO, TestLog.getStageTimingRecord(slow, slowFast, true)));
        logger.log(check(TestLevel.TEST_WARNING, TestLog.getStageTimingRecord(slow, slowFast, false)));
        //@formatter:on
    }

    private static class MessageSupplier implements Supplier<String> {
        int count = 0;

        @Override
        public String get() {
            count++;
            return "Lazy message";
        }
    }

    @Test
    public void canGetResultRecord() {
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
        Assertions.assertEquals(TestLevel.TEST_INFO.intValue(), TestLog.getResultRecord(true, "").getLevel().intValue());
        Assertions.assertEquals(TestLevel.TEST_INFO.intValue(), TestLog.getResultRecord(true, "%d", 1).getLevel().intValue());
        Assertions.assertEquals(TestLevel.TEST_INFO.intValue(), TestLog.getResultRecord(true, message).getLevel().intValue());

        Assertions.assertEquals(TestLevel.TEST_WARNING.intValue(), TestLog.getStageResultRecord(false, "").getLevel().intValue());
        Assertions.assertEquals(TestLevel.TEST_WARNING.intValue(), TestLog.getStageResultRecord(false, "%d", 1).getLevel().intValue());
        Assertions.assertEquals(TestLevel.TEST_WARNING.intValue(), TestLog.getStageResultRecord(false, message).getLevel().intValue());
        Assertions.assertEquals(TestLevel.TEST_INFO.intValue(), TestLog.getStageResultRecord(true, "").getLevel().intValue());
        Assertions.assertEquals(TestLevel.TEST_INFO.intValue(), TestLog.getStageResultRecord(true, "%d", 1).getLevel().intValue());
        Assertions.assertEquals(TestLevel.TEST_INFO.intValue(), TestLog.getStageResultRecord(true, message).getLevel().intValue());
        //@formatter:on
    }

    @Test
    public void canLazyLoadRecordMessage() {
        MessageSupplier message = new MessageSupplier();

        if (logger.isLoggable(TestLevel.TEST_FAILURE)) {
            message = new MessageSupplier();
            logger.log(TestLog.getResultRecord(false, message));
            Assertions.assertEquals(1, message.count);
        }

        if (!logger.isLoggable(TestLevel.TEST_INFO)) {
            message = new MessageSupplier();
            logger.log(TestLog.getResultRecord(true, message));
            // Should not try to create the message
            Assertions.assertEquals(0, message.count);
        }
    }

    @Test
    public void canParseTestLevel() {
        Assertions.assertEquals(TestLevel.TEST_FAILURE, Level.parse("TEST FAILURE"));
        Assertions.assertEquals(TestLevel.TEST_WARNING, Level.parse("TEST WARNING"));
        Assertions.assertEquals(TestLevel.TEST_INFO, Level.parse("TEST INFO"));
        Assertions.assertEquals(TestLevel.TEST_DEBUG, Level.parse("TEST DEBUG"));
    }
}
