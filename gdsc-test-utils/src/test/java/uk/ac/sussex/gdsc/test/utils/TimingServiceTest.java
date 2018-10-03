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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@SuppressWarnings("javadoc")
public class TimingServiceTest {
  private static Logger logger;

  @BeforeAll
  public static void beforeAll() {
    logger = Logger.getLogger(TimingServiceTest.class.getName());
  }

  @AfterAll
  public static void afterAll() {
    logger = null;
  }

  @Test
  public void canConstruct() {
    TimingService ts = new TimingService();
    Assertions.assertNotNull(ts);
    Assertions.assertFalse(0 == ts.getRuns());
    Assertions.assertEquals(0, ts.getSize());
    ts = new TimingService(10);
    Assertions.assertNotNull(ts);
    Assertions.assertEquals(10, ts.getRuns());
    Assertions.assertEquals(0, ts.getSize());
  }

  private static class LoggingTimingTask implements TimingTask {
    Object[] in = new Object[] {new Object(), new Object()};
    Object[] out = new Object[] {new Object(), new Object()};

    int runCounter = 0;
    int checkCounter = 0;
    String name;

    LoggingTimingTask(String name) {
      this.name = name;
    }

    @Override
    public int getSize() {
      return in.length;
    }

    @Override
    public Object getData(int i) {
      return in[i];
    }

    @Override
    public Object run(Object data) {
      final int index = runCounter++ % in.length;
      return out[index];
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public void check(int i, Object result) {
      final int index = checkCounter++ % in.length;
      Assertions.assertEquals(out[index], result);
    }
  }

  @Test
  public void canRunTasks() {
    final TimingService ts = new TimingService();
    final LoggingTimingTask tt1 = new LoggingTimingTask("task1");
    final LoggingTimingTask tt2 = new LoggingTimingTask("task2");
    ts.execute(tt1, false);
    ts.execute(tt2, true);
    Assertions.assertEquals(2, ts.getSize());
    final int expectedRuns = ts.getRuns() * tt1.getSize();
    Assertions.assertEquals(expectedRuns, tt1.runCounter);
    Assertions.assertEquals(0, tt1.checkCounter);
    Assertions.assertEquals(expectedRuns, tt2.runCounter);
    Assertions.assertEquals(tt2.getSize(), tt2.checkCounter);
    ts.repeat();
    Assertions.assertEquals(4, ts.getSize());
    Assertions.assertEquals(expectedRuns * 2, tt1.runCounter);
    Assertions.assertEquals(0, tt1.checkCounter);
    Assertions.assertEquals(expectedRuns * 2, tt2.runCounter);
    Assertions.assertEquals(tt2.getSize(), tt2.checkCounter);
    ts.repeat(1);
    Assertions.assertEquals(5, ts.getSize());
    Assertions.assertEquals(expectedRuns * 3, tt1.runCounter);
    Assertions.assertEquals(0, tt1.checkCounter);
    Assertions.assertEquals(expectedRuns * 2, tt2.runCounter);
    Assertions.assertEquals(tt2.getSize(), tt2.checkCounter);
    // Check get() can use negative index
    final int size = ts.getSize();
    for (int i = 0; i < size; i++) {
      final TimingResult r1 = ts.get(i);
      final TimingResult r2 = ts.get(-(size - i));
      Assertions.assertSame(r1, r2);
    }
    Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
      ts.get(size);
    });
    Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
      ts.get(-(size + 1));
    });
    ts.clearResults();
  }

  private static class SleepTimingTask extends BaseTimingTask {
    Object in = new Object();
    Object out = new Object();

    long millis;

    SleepTimingTask(String name, long millis) {
      super(name);
      this.millis = millis;
    }

    @Override
    public int getSize() {
      return 1;
    }

    @Override
    public Object getData(int i) {
      return in;
    }

    @Override
    public Object run(Object data) {
      try {
        Thread.sleep(millis);
      } catch (final InterruptedException e) {
        Assertions.fail("Cannot sleep in timing task: " + e.getMessage());
      }
      return out;
    }
  }

  @Test
  public void canCheckTasks() {
    final TimingService ts = new TimingService();
    final LoggingTimingTask tt1 = new LoggingTimingTask("task1");
    final LoggingTimingTask tt2 = new LoggingTimingTask("task2");
    ts.execute(tt1, false);
    ts.execute(tt2, false);
    Assertions.assertEquals(2, ts.getSize());
    final int expectedRuns = ts.getRuns() * tt1.getSize();
    Assertions.assertEquals(expectedRuns, tt1.runCounter);
    Assertions.assertEquals(0, tt1.checkCounter);
    Assertions.assertEquals(expectedRuns, tt2.runCounter);
    Assertions.assertEquals(0, tt2.checkCounter);

    // Check will only run the task once
    ts.check();
    Assertions.assertEquals(2, ts.getSize());
    Assertions.assertEquals(expectedRuns + tt1.getSize(), tt1.runCounter);
    Assertions.assertEquals(tt1.getSize(), tt1.checkCounter);
    Assertions.assertEquals(expectedRuns + tt2.getSize(), tt2.runCounter);
    Assertions.assertEquals(tt2.getSize(), tt2.checkCounter);

    // Static method
    TimingService.check(tt1);
    Assertions.assertEquals(expectedRuns + 2 * tt1.getSize(), tt1.runCounter);
    Assertions.assertEquals(tt1.getSize() * 2, tt1.checkCounter);
  }

  @Test
  public void canGetReport() {
    final TimingService ts = new TimingService(2);
    Assertions.assertEquals("", ts.getReport());
    Assertions.assertEquals("", ts.getReport(false));
    Assertions.assertEquals("", ts.getReport(true));
    Assertions.assertEquals("", ts.getReport(1));
    Assertions.assertEquals("", ts.getReport(1, false));
    Assertions.assertEquals("", ts.getReport(1, true));

    final SleepTimingTask tt1 = new SleepTimingTask("slow", 50);
    final SleepTimingTask tt2 = new SleepTimingTask("fast", 10);
    final SleepTimingTask tt3 = new SleepTimingTask("medium", 25);
    ts.execute(tt1, false);
    ts.execute(tt2, false);
    ts.execute(tt3, false);

    String report = ts.getReport();
    logger.log(TEST_INFO, report);

    Assertions.assertTrue(report.contains("slow"));
    Assertions.assertTrue(report.contains("fast"));
    Assertions.assertTrue(report.contains("medium"));

    // The fast task should be marked with a '*' character
    final String[] lines = report.split(TimingService.newLine);
    for (final String line : lines) {
      if (line.contains("*")) {
        Assertions.assertTrue(line.contains("fast"));
      }
    }

    // Test variants of the default report
    String report2 = ts.getReport(false);
    Assertions.assertEquals(report, TimingService.newLine + report2);
    report2 = ts.getReport(true);
    Assertions.assertEquals(report, report2);

    report = ts.getReport(2);
    logger.log(TEST_INFO, report);

    Assertions.assertFalse(report.contains("slow"));
    Assertions.assertTrue(report.contains("fast"));
    Assertions.assertTrue(report.contains("medium"));

    report2 = ts.getReport(2, false);
    Assertions.assertEquals(report, TimingService.newLine + report2);
    report2 = ts.getReport(2, true);
    Assertions.assertEquals(report, report2);

    Assertions.assertEquals("", ts.getReport(0));
    Assertions.assertEquals("", ts.getReport(0, false));
    Assertions.assertEquals("", ts.getReport(0, true));
  }

  @Test
  public void canReport() {
    final TimingService ts = new TimingService(2);
    Assertions.assertEquals("", getReport(ts));
    Assertions.assertEquals("", getReport(ts, 1));

    final SleepTimingTask tt1 = new SleepTimingTask("slow", 50);
    final SleepTimingTask tt2 = new SleepTimingTask("fast", 10);
    final SleepTimingTask tt3 = new SleepTimingTask("medium", 25);
    ts.execute(tt1, false);
    ts.execute(tt2, false);
    ts.execute(tt3, false);

    String report = getReport(ts);
    logger.log(TEST_INFO, report);

    Assertions.assertTrue(report.contains("slow"));
    Assertions.assertTrue(report.contains("fast"));
    Assertions.assertTrue(report.contains("medium"));

    // The fast task should be marked with a '*' character
    final String[] lines = report.split(TimingService.newLine);
    for (final String line : lines) {
      if (line.contains("*")) {
        Assertions.assertTrue(line.contains("fast"));
      }
    }

    // Test variants of the default report
    report = getReport(ts, 2);
    logger.log(TEST_INFO, report);

    Assertions.assertFalse(report.contains("slow"));
    Assertions.assertTrue(report.contains("fast"));
    Assertions.assertTrue(report.contains("medium"));

    Assertions.assertEquals("", getReport(ts, 0));
  }

  private static String getReport(TimingService ts) {
    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (PrintStream ps = new PrintStream(baos, true)) {
      ts.report(ps);
      ps.close();
      return new String(baos.toByteArray());
    }
  }

  private static String getReport(TimingService ts, int n) {
    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (PrintStream ps = new PrintStream(baos, true)) {
      ts.report(ps, n);
      ps.close();
      return new String(baos.toByteArray());
    }
  }

  @Test
  public void canReportUsingEmptyResults() {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (PrintStream ps = new PrintStream(baos, true)) {
      TimingService.report(ps, (TimingResult[]) null);
      ps.close();
      Assertions.assertEquals("", new String(baos.toByteArray()));
    }
    baos = new ByteArrayOutputStream();
    try (PrintStream ps = new PrintStream(baos, true)) {
      TimingService.report(ps, new TimingResult[0]);
      ps.close();
      Assertions.assertEquals("", new String(baos.toByteArray()));
    }
  }
}
