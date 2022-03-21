/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
 *
 * Generates Java classes for the GDSC Test project.
 * %%
 * Copyright (C) 2018 - 2022 Alex Herbert
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package uk.ac.sussex.gdsc.test.generator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.ac.sussex.gdsc.test.generator.CodeGenerator.ExitCode;

@SuppressWarnings("javadoc")
class CodeGeneratorTest {

  @Test
  void testGetExitCodeForException() {
    Assertions.assertEquals(ExitCode.IO_ERROR,
        CodeGenerator.getExitCodeForException(new IOException()));
    Assertions.assertEquals(ExitCode.INVALID_MODEL,
        CodeGenerator.getExitCodeForException(new InvalidModelException("Test")));
    Assertions.assertEquals(ExitCode.FAILED,
        CodeGenerator.getExitCodeForException(new Exception()));
  }

  @Test
  void testHelpOption() throws UnsupportedEncodingException {
    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (PrintStream ps = new PrintStream(baos, true, "UTF-8")) {
      CodeGenerator.setOut(ps);
      final ExitCode result = CodeGenerator.run(getArgs("-h"));
      Assertions.assertEquals(ExitCode.HELP, result, "Exit code");
      final String message = new String(baos.toByteArray(), StandardCharsets.UTF_8);
      assertMessageContains(message, "Usage: codegenerator", "Usage description");
    } finally {
      CodeGenerator.setErr(System.err);
    }
  }

  @Test
  void testMissingSourceDirectoryParameter() throws UnsupportedEncodingException {
    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (PrintStream ps = new PrintStream(baos, true, "UTF-8")) {
      CodeGenerator.setErr(ps);
      final ExitCode result = CodeGenerator.run(getArgs("-l", "WARNING"));
      Assertions.assertEquals(ExitCode.BAD_PARAMETERS, result, "Exit code");
      final String message = new String(baos.toByteArray(), StandardCharsets.UTF_8);
      assertMessageContains(message, "Missing required parameters", "Reason for error");
    } finally {
      CodeGenerator.setErr(System.err);
    }
  }

  @Test
  void testBadParameter() throws IOException {
    final Path root = Files.createTempDirectory(CodeGeneratorTest.class.getSimpleName());
    final File rootFile = root.toFile();
    FileUtils.forceDeleteOnExit(rootFile);

    final String pathForSource = new File(rootFile, "source").getPath();
    final String pathForTarget = new File(rootFile, "target").getPath();
    FileUtils.forceMkdir(new File(pathForSource));

    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (PrintStream ps = new PrintStream(baos, true, "UTF-8")) {
      CodeGenerator.setErr(ps);
      final String badParameter = "--some-flag";
      final ExitCode result =
          CodeGenerator.run(getArgs(pathForSource, pathForTarget, badParameter, "-l", "WARNING"));
      Assertions.assertEquals(ExitCode.BAD_PARAMETERS, result, "Exit code");
      final String message = new String(baos.toByteArray(), StandardCharsets.UTF_8);
      assertMessageContains(message, "Unknown option", "Reason for error");
      assertMessageContains(message, badParameter, "The bad parameter");
    } finally {
      CodeGenerator.setErr(System.err);
    }
  }

  @Test
  void testGeneration() throws IOException {
    // Create a source and target directory structure
    final Path root = Files.createTempDirectory(CodeGeneratorTest.class.getSimpleName());
    final File rootFile = root.toFile();

    final String pathForSource = new File(rootFile, "source").getPath();
    final String pathForTarget = new File(rootFile, "target").getPath();
    FileUtils.forceMkdir(new File(pathForSource));

    // Create a package structure
    final String packageName = "org";
    final File sourceDir = new File(pathForSource, packageName);
    FileUtils.forceMkdir(sourceDir);

    // Write a template file
    final String templateClassName = "MyTemplate";
    final String template = "<gender>";
    FileUtils.write(new File(sourceDir, templateClassName + ".st"), template,
        StandardCharsets.UTF_8);

    // Write a properties file
    final Properties properties = new Properties();
    properties.put("classname.My", "His Her");
    properties.put("class.gender", "Male Female");
    try (FileOutputStream outStream =
        new FileOutputStream(new File(sourceDir, templateClassName + ".properties"))) {
      properties.store(outStream, "Test template");
    }

    // Run the generator
    final ExitCode result = CodeGenerator.run(getArgs(pathForSource, pathForTarget, "-l", "INFO"));
    Assertions.assertEquals(ExitCode.OK, result, "Exit code");

    // Check the expected target templates
    final File targetDir = new File(pathForTarget, packageName);
    final String template1 =
        FileUtils.readFileToString(new File(targetDir, "HisTemplate.java"), StandardCharsets.UTF_8);
    Assertions.assertEquals("Male", template1, "Template 1");
    final String template2 =
        FileUtils.readFileToString(new File(targetDir, "HerTemplate.java"), StandardCharsets.UTF_8);
    Assertions.assertEquals("Female", template2, "Template 2");

    // Clean up only on success
    FileUtils.forceDeleteOnExit(rootFile);
  }

  @Test
  void testFileHeader() throws IOException {
    // Create a source and target directory structure
    final Path root = Files.createTempDirectory(CodeGeneratorTest.class.getSimpleName());
    final File rootFile = root.toFile();

    final String pathForSource = new File(rootFile, "source").getPath();
    final String pathForTarget = new File(rootFile, "target").getPath();
    FileUtils.forceMkdir(new File(pathForSource));

    // Create a package structure
    final String packageName = "org";
    final File sourceDir = new File(pathForSource, packageName);
    FileUtils.forceMkdir(sourceDir);

    // Write a template file
    final String templateClassName = "MyTemplate";
    final String template = "template text";
    FileUtils.write(new File(sourceDir, templateClassName + ".st"), template,
        StandardCharsets.UTF_8);

    // Write a properties file. This can be empty.
    final Properties properties = new Properties();
    try (FileOutputStream outStream =
        new FileOutputStream(new File(sourceDir, templateClassName + ".properties"))) {
      properties.store(outStream, "Test template");
    }

    // Write a header file
    final File headerFile = new File(rootFile, "header.txt");
    final String header = "// Header text ";
    FileUtils.write(headerFile, header, StandardCharsets.UTF_8);

    // Run the generator
    final ExitCode result = CodeGenerator.run(
        getArgs(pathForSource, pathForTarget, "-l", "WARNING", "--header", headerFile.getPath()));
    Assertions.assertEquals(ExitCode.OK, result, "Exit code");

    // Check the expected target templates
    final File targetDir = new File(pathForTarget, packageName);
    final String template1 =
        FileUtils.readFileToString(new File(targetDir, "MyTemplate.java"), StandardCharsets.UTF_8);
    Assertions.assertEquals(header + template, template1, "Missing header + template");

    // Clean up only on success
    FileUtils.forceDeleteOnExit(rootFile);
  }

  @Test
  void testMissingSourceDirectory() throws IOException {
    // Create a source and target directory structure
    final Path root = Files.createTempDirectory(CodeGeneratorTest.class.getSimpleName());
    final File rootFile = root.toFile();

    final String pathForSource = new File(rootFile, "source").getPath();
    final String pathForTarget = new File(rootFile, "target").getPath();

    // Run the generator
    final ExitCode result =
        CodeGenerator.run(getArgs(pathForSource, pathForTarget, "-l", "WARNING"));
    Assertions.assertEquals(ExitCode.MISSING_SOURCE, result, "Exit code");

    // Clean up only on success
    FileUtils.forceDeleteOnExit(rootFile);
  }

  @Test
  void testMissingTemplates() throws IOException {
    // Create a source and target directory structure
    final Path root = Files.createTempDirectory(CodeGeneratorTest.class.getSimpleName());
    final File rootFile = root.toFile();

    final String pathForSource = new File(rootFile, "source").getPath();
    final String pathForTarget = new File(rootFile, "target").getPath();
    FileUtils.forceMkdir(new File(pathForSource));

    // Run the generator
    final ExitCode result =
        CodeGenerator.run(getArgs(pathForSource, pathForTarget, "-l", "WARNING"));
    Assertions.assertEquals(ExitCode.MISSING_TEMPLATES, result, "Exit code");

    // Clean up only on success
    FileUtils.forceDeleteOnExit(rootFile);
  }

  @Test
  void testInvalidModel() throws IOException {
    // Create a source and target directory structure
    final Path root = Files.createTempDirectory(CodeGeneratorTest.class.getSimpleName());
    final File rootFile = root.toFile();

    final String pathForSource = new File(rootFile, "source").getPath();
    final String pathForTarget = new File(rootFile, "target").getPath();
    FileUtils.forceMkdir(new File(pathForSource));

    // Create a package structure
    final String packageName = "org";
    final File sourceDir = new File(pathForSource, packageName);
    FileUtils.forceMkdir(sourceDir);

    // Write a template file
    final String templateClassName = "MyTemplate";
    final String template = "<gender>";
    FileUtils.write(new File(sourceDir, templateClassName + ".st"), template,
        StandardCharsets.UTF_8);

    // Write a properties file
    final Properties properties = new Properties();
    properties.put("classname.Missing", "His Her");
    properties.put("class.gender", "Male Female");
    try (FileOutputStream outStream =
        new FileOutputStream(new File(sourceDir, templateClassName + ".properties"))) {
      properties.store(outStream, "Test template");
    }

    // Run the generator
    ExitCode result = CodeGenerator.run(getArgs(pathForSource, pathForTarget, "-l", "OFF"));
    Assertions.assertEquals(ExitCode.INVALID_MODEL, result, "Exit code");

    // Test again with debug logging
    result = CodeGenerator.run(getArgs(pathForSource, pathForTarget, "-l", "OFF", "-d"));
    Assertions.assertEquals(ExitCode.INVALID_MODEL, result, "Exit code");

    // Clean up only on success
    FileUtils.forceDeleteOnExit(rootFile);
  }

  @Test
  void testMissingProperties() throws IOException {
    // Create a source and target directory structure
    final Path root = Files.createTempDirectory(CodeGeneratorTest.class.getSimpleName());
    final File rootFile = root.toFile();

    final String pathForSource = new File(rootFile, "source").getPath();
    final String pathForTarget = new File(rootFile, "target").getPath();
    FileUtils.forceMkdir(new File(pathForSource));

    // Create a package structure
    final String packageName = "org";
    final File sourceDir = new File(pathForSource, packageName);
    FileUtils.forceMkdir(sourceDir);

    // Write a template file
    final String templateClassName = "MyTemplate";
    final String template = "<gender>";
    FileUtils.write(new File(sourceDir, templateClassName + ".st"), template,
        StandardCharsets.UTF_8);

    // Currently this is OK

    // Run the generator
    final ExitCode result =
        CodeGenerator.run(getArgs(pathForSource, pathForTarget, "-l", "WARNING"));
    Assertions.assertEquals(ExitCode.OK, result, "Exit code");

    // Check the expected target templates do not exists
    final File targetDir = new File(pathForTarget, packageName);
    final File template1 = new File(targetDir, "HisTemplate.java");
    Assertions.assertFalse(template1.exists(), () -> "Template exists: " + template1.getPath());
    final File template2 = new File(targetDir, "HerTemplate.java");
    Assertions.assertFalse(template2.exists(), () -> "Template exists: " + template2.getPath());

    // Clean up only on success
    FileUtils.forceDeleteOnExit(rootFile);
  }

  @Test
  void testOverwrite() throws IOException {
    // Create a source and target directory structure
    final Path root = Files.createTempDirectory(CodeGeneratorTest.class.getSimpleName());
    final File rootFile = root.toFile();

    final String pathForSource = new File(rootFile, "source").getPath();
    final String pathForTarget = new File(rootFile, "target").getPath();
    FileUtils.forceMkdir(new File(pathForSource));

    // Create a package structure
    final String packageName = "org";
    final File sourceDir = new File(pathForSource, packageName);
    FileUtils.forceMkdir(sourceDir);

    // Write a template file
    final String templateClassName = "MyTemplate";
    final String template = "<gender>";
    final File templateFile = new File(sourceDir, templateClassName + ".st");
    FileUtils.write(templateFile, template, StandardCharsets.UTF_8);

    // Write a properties file
    final Properties properties = new Properties();
    properties.put("classname.My", "His Her");
    properties.put("class.gender", "Male Female");
    final File propsFile = new File(sourceDir, templateClassName + ".properties");
    try (FileOutputStream outStream = new FileOutputStream(propsFile)) {
      properties.store(outStream, "Test template");
    }

    // Run the generator
    ExitCode result = CodeGenerator.run(getArgs(pathForSource, pathForTarget, "-l", "WARNING"));
    Assertions.assertEquals(ExitCode.OK, result, "Exit code");

    // Check the expected target templates
    final File targetDir = new File(pathForTarget, packageName);
    final File templateFile1 = new File(targetDir, "HisTemplate.java");
    String template1 = FileUtils.readFileToString(templateFile1, StandardCharsets.UTF_8);
    Assertions.assertEquals("Male", template1, "Template 1");
    final File templateFile2 = new File(targetDir, "HerTemplate.java");
    final String template2 = FileUtils.readFileToString(templateFile2, StandardCharsets.UTF_8);
    Assertions.assertEquals("Female", template2, "Template 2");

    // Change contents of the output Java file
    final String badContents = "nothing";
    FileUtils.write(templateFile1, badContents, StandardCharsets.UTF_8);
    // Ensure the template file is modified before it
    templateFile.setLastModified(templateFile1.lastModified() - 2000);
    propsFile.setLastModified(templateFile1.lastModified() - 2000);

    // Rerun - This will skip existing newer output Java files
    result = CodeGenerator.run(getArgs(pathForSource, pathForTarget, "-l", "WARNING"));
    Assertions.assertEquals(ExitCode.OK, result, "Exit code");

    // Should be unchanged
    template1 = FileUtils.readFileToString(templateFile1, StandardCharsets.UTF_8);
    Assertions.assertEquals(badContents, template1, "Template 1");

    // Ensure the Java file is older than the template file
    templateFile1.setLastModified(templateFile.lastModified() - 2000);

    // Rerun - This will generate only missing templates
    result = CodeGenerator.run(getArgs(pathForSource, pathForTarget, "-l", "WARNING"));
    Assertions.assertEquals(ExitCode.OK, result, "Exit code");

    // Should be fixed
    template1 = FileUtils.readFileToString(templateFile1, StandardCharsets.UTF_8);
    Assertions.assertEquals("Male", template1, "Template 1");

    // Delete bad file
    templateFile1.delete();

    // Rerun - This will generate only missing templates
    result = CodeGenerator.run(getArgs(pathForSource, pathForTarget, "-l", "WARNING"));
    Assertions.assertEquals(ExitCode.OK, result, "Exit code");

    // Should be fixed
    template1 = FileUtils.readFileToString(templateFile1, StandardCharsets.UTF_8);
    Assertions.assertEquals("Male", template1, "Template 1");

    // Change contents
    FileUtils.write(templateFile1, badContents, StandardCharsets.UTF_8);

    // Rerun with overwrite
    result = CodeGenerator.run(getArgs(pathForSource, pathForTarget, "-l", "WARNING", "-o"));
    Assertions.assertEquals(ExitCode.OK, result, "Exit code");

    // Should be fixed
    template1 = FileUtils.readFileToString(templateFile1, StandardCharsets.UTF_8);
    Assertions.assertEquals("Male", template1, "Template 1");

    // Clean up only on success
    FileUtils.forceDeleteOnExit(rootFile);
  }

  /**
   * Gets the arguments array.
   *
   * @param args the arguments
   * @return the arguments
   */
  private static String[] getArgs(String... args) {
    return args;
  }

  /**
   * Assert the message contains the sub-string.
   *
   * <p>If missing fails with a description of the sub-string.
   *
   * @param message the message
   * @param subString the sub-string
   * @param description the description
   */
  private static void assertMessageContains(String message, String subString, String description) {
    Assertions.assertTrue(message.contains(subString),
        () -> String.format("Message '%s' is missing '%s' (%s)", message, subString, description));
  }
}
