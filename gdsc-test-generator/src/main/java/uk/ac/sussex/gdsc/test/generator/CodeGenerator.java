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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.lang3.tuple.Pair;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.DefaultExceptionHandler;
import picocli.CommandLine.ITypeConverter;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ParseResult;

/**
 * Provides functionality to scan a source directory tree and generate Java code into a target
 * directory.
 */
@Command(description = "Generates code using string-template files from a source directory.",
    name = "codegenerator", mixinStandardHelpOptions = true, version = "codegenerator 1.0")
public class CodeGenerator {

  /** The logger. */
  private static final Logger logger = Logger.getLogger(CodeGenerator.class.getName());

  /** The out print stream for Picocli. */
  private static PrintStream out = System.out;

  /** The error print stream for Picocli. */
  private static PrintStream err = System.err;

  /** The source directory. */
  @Parameters(index = "0", description = "The source directory.")
  private File sourceDirectory;

  /** The target directory. */
  @Parameters(index = "1", description = "The target directory.")
  private File targetDirectory;

  /** The logging level. */
  @Option(names = {"-l", "--logging"}, description = "INFO, FINE, FINER, ...")
  private Level level = Level.INFO; // NOPMD Cannot be final to allow update by PicoCLI

  /** The debug flag. */
  @Option(names = {"-d", "--debug"}, description = "Extra debug information.")
  private boolean debug;

  /** The overwrite flag. */
  @Option(names = {"-o", "--overwrite"}, description = "Overwrite existing files.")
  private boolean overwrite;

  /** The header file. */
  @Option(names = {"--header"}, description = "The licence header.")
  private File headerFile;

  /**
   * The possible exit codes from the generator.
   *
   * <p>A non-zero exit code indicates error.
   */
  public enum ExitCode {
    /** The return code for normal completion. */
    OK,
    /** The return code for printing help. */
    HELP,
    /** The return code for bad parameters. */
    BAD_PARAMETERS,
    /** The return code for an invalid model. */
    INVALID_MODEL,
    /** The return code for a missing source directory. */
    MISSING_SOURCE,
    /** The return code for a missing template files. */
    MISSING_TEMPLATES,
    /** The return code for a file input/output error. */
    IO_ERROR,
    /** The return code for any other error. */
    FAILED;

    /**
     * Gets the exit code.
     *
     * <p>A non-zero exit code indicates error.
     *
     * @return the exit code
     */
    public int getExitCode() {
      return ordinal();
    }
  }

  /**
   * A DirectoryWalker that searches the source directory for String Template (.st) files.
   */
  private final class SourceDirectoryWalker extends DirectoryWalker<File> {

    /** The files. */
    final List<File> files = new ArrayList<>();

    /**
     * Creates a new source directory walker.
     */
    SourceDirectoryWalker() {
      super(HiddenFileFilter.VISIBLE, FileFilterUtils.suffixFileFilter(".st"), -1);
    }

    @Override
    protected void handleFile(File file, int depth, Collection<File> results) {
      files.add(file);
    }

    /**
     * Gets the files.
     *
     * <p>Searches the source directory for String Template (.st) files.
     *
     * @return the files
     * @throws IOException Signals that an I/O exception has occurred.
     */
    List<File> getFiles() throws IOException {
      walk(getSourceDirectory(), files);
      return files;
    }
  }

  /**
   * Scan a source directory tree and generate Java code into a target directory.
   *
   * <p>All parameters are parsed from the the input arguments.
   *
   * <p>Calls {@link System#exit(int)} with the exit code value from
   * {@link CodeGenerator#run(String[])}.
   *
   * @param args the arguments
   */
  public static void main(String[] args) {
    System.exit(run(args).getExitCode());
  }

  /**
   * Scan a source directory tree and generate Java code into a target directory.
   *
   * <p>All parameters are parsed from the the input arguments.
   *
   * <p>Errors parsing the parameters are written to the configured output error stream
   * ({@link System#err}).
   *
   * <p>If the help parameter was provided then help is written to the configured output stream
   * ({@link System#out}).
   *
   * <p>Any errors generating the Java code are logged using {@link java.util.logging.Logger} and
   * the method returns a code indicating the result.
   *
   * <p>Runtime errors will not be caught and logged.
   *
   * @param args the arguments
   * @return the exit code
   */
  public static ExitCode run(String[] args) {
    final CodeGenerator apiGenerator = new CodeGenerator();
    try {
      // Set-up command line parsing
      final CommandLine cmd = new CommandLine(apiGenerator);
      // Convert logging level
      cmd.registerConverter(Level.class, new ITypeConverter<Level>() {
        @Override
        public Level convert(String value) throws Exception {
          return Level.parse(value);
        }
      });
      // Parse
      final ParseResult parseResult = cmd.parseArgs(args);
      if (CommandLine.printHelpIfRequested(parseResult.asCommandLineList(), getOut(), getErr(),
          CommandLine.Help.Ansi.AUTO)) {
        return ExitCode.HELP;
      }
      // Configure the log level
      logger.setLevel(apiGenerator.level);
      return apiGenerator.generate();

    } catch (final ParameterException ex) {
      new DefaultExceptionHandler<List<Object>>().useErr(getErr())
          .useAnsi(CommandLine.Help.Ansi.AUTO).handleParseException(ex, args);
      return ExitCode.BAD_PARAMETERS;
    } catch (final InvalidModelException | IOException ex) {
      final LogRecord lr =
          new LogRecord(Level.SEVERE, "Failed to generate code: " + ex.getMessage());
      if (apiGenerator.debug) {
        lr.setThrown(ex);
      }
      logger.log(lr);
      return getExitCodeForException(ex);
    }
  }

  /**
   * Gets the exit code for the exception.
   *
   * @param exception the exception
   * @return the exit code
   */
  static ExitCode getExitCodeForException(Exception exception) {
    if (exception instanceof InvalidModelException) {
      return ExitCode.INVALID_MODEL;
    }
    if (exception instanceof IOException) {
      return ExitCode.IO_ERROR;
    }
    return ExitCode.FAILED;
  }

  /**
   * Do the work to generate the Java code.
   *
   * <p>The configuration is set using parsing of command-line options by the Pico CLI library.
   *
   * @return the result code
   * @throws InvalidModelException If any string template model was invalid
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private ExitCode generate() throws InvalidModelException, IOException {

    if (!sourceDirectory.isDirectory()) {
      logger.log(Level.SEVERE, () -> "Missing source directory " + sourceDirectory.getPath());
      return ExitCode.MISSING_SOURCE;
    }

    logger.log(Level.INFO, () -> "Source: " + sourceDirectory.getAbsolutePath());
    logger.log(Level.INFO, () -> "Target: " + targetDirectory.getAbsolutePath());

    // Find the string-template files
    final List<File> results = new SourceDirectoryWalker().getFiles();
    if (results.isEmpty()) {
      logger.log(Level.SEVERE, "No string-template (.st) files");
      return ExitCode.MISSING_TEMPLATES;
    }

    // Create target
    FileUtils.forceMkdir(targetDirectory);

    final String sourcePath = sourceDirectory.getPath();
    final String header = loadHeader();

    // Note: Do not use the STGroupDir object to load sourceDirectory.
    // Load each template as raw text.

    // For each template create the primitive versions required
    for (final File stFile : results) {
      processTemplate(sourcePath, header, stFile);
    }

    return ExitCode.OK;
  }

  /**
   * Load the file header.
   *
   * <p>Returns the empty string if the file doesn't exists or is empty.
   *
   * @return the file header
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private String loadHeader() throws IOException {
    return (headerFile == null) ? "" : FileUtils.readFileToString(headerFile, (String) null);
  }

  /**
   * Process a single template file.
   *
   * @param sourcePath the source path as a string
   * @param header the header (this should not be null)
   * @param stFile the string template file
   * @throws InvalidModelException If any string template model was invalid
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private void processTemplate(String sourcePath, String header, File stFile)
      throws InvalidModelException, IOException {
    logger.log(Level.INFO, () -> stFile.getPath());
    // Check for .properties file
    final String propsPath = FilenameUtils.removeExtension(stFile.getPath()) + ".properties";
    final File propsFile = new File(propsPath);
    if (!propsFile.exists()) {
      // Does not cause the code to fail
      logger.log(Level.WARNING, () -> "No properties file: " + propsPath);
      return;
    }

    final Properties props = new Properties();
    try (InputStream inStream = Files.newInputStream(propsFile.toPath())) {
      props.load(inStream);
    }

    // Create path for output
    final String pathFromSource = stFile.getParent().substring(sourcePath.length());
    final String packageName = getPackageName(pathFromSource);
    final String templateClassName = FilenameUtils.getBaseName(stFile.getPath());
    final File pathForTarget = new File(targetDirectory, pathFromSource);
    FileUtils.forceMkdir(pathForTarget);

    // Load template
    final String template = FileUtils.readFileToString(stFile, (String) null);

    // Create model
    final StringTemplateModel model =
        StringTemplateModel.create(props, packageName, templateClassName, template);

    if (canSkipModel(stFile, propsFile, pathForTarget, model)) {
      return;
    }

    // Build sources
    final List<Pair<String, String>> files = StringTemplates.generate(model);

    for (final Pair<String, String> file : files) {
      final File targetFile = getTargetFile(pathForTarget, file.getKey());
      logger.log(Level.INFO, () -> targetFile.getPath());
      FileUtils.write(targetFile, header + file.getValue(), StandardCharsets.UTF_8);
    }
  }

  /**
   * Gets the package name.
   *
   * <p>The input path is assumed to start with the {@link File#separatorChar}.
   *
   * <p>E.g. "/some/dir/structure" returns "some.dir.structure".
   *
   * @param pathFromSource the path from source
   * @return the package name
   */
  private static String getPackageName(String pathFromSource) {
    final String packageName = pathFromSource.replace(File.separatorChar, '.');
    return packageName.substring(1);
  }

  /**
   * Determine if all the model output files already exist, are newer than the template, and no
   * overwrite option is enabled. This allows the template to be skipped.
   *
   * @param stFile the string template file
   * @param propsFile the properties file
   * @param pathForTarget the path for target
   * @param model the model
   * @return true, if the model can be skipped
   */
  private boolean canSkipModel(File stFile, File propsFile, File pathForTarget,
      StringTemplateModel model) {
    if (overwrite) {
      // Do not skip
      return false;
    }
    // Check all template output files exist and are newer than the source files.
    final long timestamp = Math.max(stFile.lastModified(), propsFile.lastModified());
    for (final String name : StringTemplates.listNames(model)) {
      final File targetFile = getTargetFile(pathForTarget, name);
      if (!FileUtils.isFileNewer(targetFile, timestamp)) {
        // Target file missing or not newer than the source files
        return false;
      }
    }
    return true;
  }

  /**
   * Gets the target file.
   *
   * @param pathForTarget the path for target
   * @param name the name of the template
   * @return the target file
   */
  private static File getTargetFile(File pathForTarget, String name) {
    return new File(pathForTarget, name + ".java");
  }

  /**
   * Gets the out print stream for PicoCLI.
   *
   * @return the out
   */
  static PrintStream getOut() {
    return out;
  }

  /**
   * Sets the out print stream for PicoCLI.
   *
   * @param out the new out
   */
  static void setOut(PrintStream out) {
    CodeGenerator.out = out;
  }

  /**
   * Gets the err print stream for PicoCLI.
   *
   * @return the err
   */
  static PrintStream getErr() {
    return err;
  }

  /**
   * Sets the err print stream for PicoCLI.
   *
   * @param err the new err
   */
  static void setErr(PrintStream err) {
    CodeGenerator.err = err;
  }

  /**
   * Gets the source directory.
   *
   * @return the source directory
   */
  File getSourceDirectory() {
    return sourceDirectory;
  }
}
