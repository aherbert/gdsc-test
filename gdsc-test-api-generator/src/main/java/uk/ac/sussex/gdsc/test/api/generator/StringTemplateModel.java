/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
 *
 * Generates API classes for the GDSC Test project.
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

package uk.ac.sussex.gdsc.test.api.generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Define the model for template substitutions using the StringTemplate library.
 */
public class StringTemplateModel {

  /**
   * The scope of the template substitution.
   */
  public enum SubstitutionScope {
  /**
   * The substitution occurs at the class name. A new class is created for each defined
   * substitution. If absent then only template level substitutions are valid.
   */
  CLASSNAME,
  /**
   * The substitution occurs at the class. The number of substitutions must match the number defined
   * by {@link #CLASSNAME}, i.e. one per class.
   */
  CLASS,
  /** The substitution occurs at the template. */
  TEMPLATE
  }

  /** The regular expression to split the key into scope and pattern. */
  private static final String SPLIT_REGEX = "\\.";

  /** The regular expression for a class name. */
  private static final String CLASS_NAME_REGEX = "^[A-Z][A-Za-z0-9_]*$";

  /** The regular expression for a package name. */
  private static final String PACKAGE_REGEX = "^[a-z][a-z0-9_]*(\\.[a-z0-9_]+)*$";

  /** The regular expression for whitespace. */
  private static final String WHITESPACE_REGEX = "\\p{javaSpaceChar}{1,}";

  /** The compiled pattern to split the key into scope and pattern. */
  private static final Pattern SPLIT_PATTERN = Pattern.compile(SPLIT_REGEX);

  /** The compiled pattern for a class name. */
  private static final Pattern CLASS_NAME_PATTERN = Pattern.compile(CLASS_NAME_REGEX);

  /** The compiled pattern for a package name. */
  private static final Pattern PACKAGE_PATTERN = Pattern.compile(PACKAGE_REGEX);

  /** The compiled pattern for whitespace. */
  private static final Pattern WHITESPACE_PATTERN = Pattern.compile(WHITESPACE_REGEX);

  /** The list of reserved substitution words. */
  private static final List<String> RESERVED = Arrays.asList("true", "false", "import", "default",
      "key", "group", "implements", "first", "last", "rest", "trunc", "strip", "trim", "length",
      "strlen", "reverse", "if", "else", "elseif", "endif", "delimiters", "package");

  /** The properties. */
  private final Properties properties;

  /** The package name. */
  private final String packageName;

  /** The template class name. */
  private final String templateClassName;

  /** The template. */
  private final String template;

  /**
   * Substitutions for the class name. The key must match part of the template class name.
   */
  private List<Pair<String, List<String>>> classNameScope;
  /**
   * Substitutions for each class. Each named substitution must match the length of the class name
   * substitutions. Not valid is no class name scope substitutions.
   */
  private List<Pair<String, List<Object>>> classScope;
  /**
   * Substitutions for the template.
   */
  private List<Pair<String, List<Object>>> templateScope;

  /**
   * Instantiates a new string template model.
   *
   * @param properties the properties
   * @param packageName the package name
   * @param templateClassName the template class name
   * @param template the template
   * @throws InvalidModelException If the model cannot be created
   */
  public StringTemplateModel(Properties properties, String packageName, String templateClassName,
      String template) throws InvalidModelException {
    this.properties = Objects.requireNonNull(properties, "Properties is null");
    this.packageName = Objects.requireNonNull(packageName, "Package name is null");
    this.templateClassName = Objects.requireNonNull(templateClassName, "Class name is null");
    this.template = Objects.requireNonNull(template, "Template is null");
    validate();
  }

  private void validate() throws InvalidModelException {

    validateClassName();

    validatePackageName();

    // Store classname scope
    classNameScope = new ArrayList<>();

    // Store other scopes to be validated after the classname scope
    final List<Triple<SubstitutionScope, String, String>> scopes = new ArrayList<>();

    // Any class/classname scope must match the first classname scope list size
    int classnameListSize = 0;

    // The current set of substitutions. No duplicates are allowed.
    final Set<String> subs = new HashSet<>();

    // Get the scope for all String=String key:value pairs
    for (final String key : properties.stringPropertyNames()) {
      final Triple<SubstitutionScope, String, String> scope = splitKey(key, subs);

      if (scope.first == SubstitutionScope.CLASSNAME) {
        classnameListSize = addClassnameScope(classnameListSize, scope);
      } else {
        // Store for validation
        scopes.add(scope);
      }
    }

    // Split the remaining scopes
    classScope = new ArrayList<>();
    templateScope = new ArrayList<>();
    for (final Triple<SubstitutionScope, String, String> scope : scopes) {
      switch (scope.first) {
        case CLASS:
          addClassScope(classnameListSize, scope);
          break;
        case TEMPLATE:
          addTemplateScope(scope);
          break;

        // Note: This is not possible as the scope is generated using
        // SubstitutionScope.valueOf(String) which throws.
        // Leave in to catch future errors.
        default:
          throw new InvalidModelException("Unknown scope: " + scope.first);
      }
    }
  }

  /**
   * Validate the class name.
   *
   * @throws InvalidModelException If the class name is invalid
   */
  private void validateClassName() throws InvalidModelException {
    // Validate the class name
    if (!CLASS_NAME_PATTERN.matcher(templateClassName).matches()) {
      throw new InvalidModelException("Bad template class name: " + templateClassName);
    }
  }

  /**
   * Validate the package name.
   *
   * @throws InvalidModelException If the package name is invalid
   */
  private void validatePackageName() throws InvalidModelException {
    // Validate the package name if not empty
    if (packageName.length() > 0 && !PACKAGE_PATTERN.matcher(packageName).matches()) {
      throw new InvalidModelException("Bad package name: " + packageName);
    }
  }

  /**
   * Gets the scope and substitution by splitting the key into scope and substitution using the
   * {@link #SPLIT_REGEX} character.
   *
   * @param key the key
   * @param subs the set of current substitutions
   * @return the triple (scope,substitution,key)
   * @throws InvalidModelException If the {@link #SPLIT_REGEX} character does not split the string,
   *         or the scope is invalid, or the substitution is invalid.
   */
  private static Triple<SubstitutionScope, String, String> splitKey(String key, Set<String> subs)
      throws InvalidModelException {
    // Note: Trailing empty strings are not included in the resulting array
    // so the second item must not be empty.
    final String[] pair = SPLIT_PATTERN.split(key);
    if (pair.length != 2 || StringUtils.isNullOrEmpty(pair[0])) {
      throw new InvalidModelException(
          String.format("Failed to split key using [%s] character. Key=%s", SPLIT_REGEX, key));
    }
    final String scope = pair[0];
    final SubstitutionScope s = getSubstitutionScope(scope);
    final String sub = pair[1];
    validateSubstitution(s, sub, subs);
    return new Triple<>(s, sub, key);
  }

  /**
   * Gets the substitution scope.
   *
   * @param scope the scope
   * @return the substitution scope
   * @throws InvalidModelException If the scope is not valid
   */
  private static SubstitutionScope getSubstitutionScope(final String scope)
      throws InvalidModelException {
    try {
      return SubstitutionScope.valueOf(scope.toUpperCase(Locale.getDefault()));
    } catch (final IllegalArgumentException ex) {
      // This is because there is no enum for the scope
      throw new InvalidModelException("Invalid scope = " + scope, ex);
    }
  }

  /**
   * Validate substitution is not a reserved word or a duplicate.
   *
   * @param scope the scope
   * @param sub the substitution
   * @param subs the existing substitutions
   * @throws InvalidModelException If the substitution is a reserved word or duplicate
   */
  private static void validateSubstitution(SubstitutionScope scope, String sub, Set<String> subs)
      throws InvalidModelException {
    // Do not use the reserved attribute/template names:
    if (RESERVED.contains(sub)) {
      throw new InvalidModelException("Reserved substitution word: " + sub);
    }
    checkSubstitution(sub, subs);
    if (scope == SubstitutionScope.CLASSNAME) {
      // Special substitution repeated in lowercase
      final String lc = sub.toLowerCase(Locale.getDefault());
      if (!sub.equals(lc)) {
        checkSubstitution(lc, subs);
      }
    }
  }

  /**
   * Check the substitution has not already been used.
   *
   * @param sub the sub
   * @param subs the subs
   * @throws InvalidModelException If the substitution is a duplicate
   */
  private static void checkSubstitution(String sub, Set<String> subs) throws InvalidModelException {
    if (!subs.add(sub)) {
      throw new InvalidModelException("Duplicate substitution word: " + sub);
    }
  }

  /**
   * Adds the classname scope.
   *
   * @param classnameListSize the size of the list for the first encountered classname scope
   * @param scope the scope
   * @return the size of the list for the classname scope
   * @throws InvalidModelException If the scope is not valid
   */
  private int addClassnameScope(int classnameListSize,
      final Triple<SubstitutionScope, String, String> scope) throws InvalidModelException {
    // The substitution must match the template name
    if (!templateClassName.contains(scope.second)) {
      throw new InvalidModelException(
          String.format("Template name <%s> does not contain the substitution key <%s> ",
              templateClassName, scope.second));
    }
    // The class scope is a list of single strings
    final List<String> list = splitValue(scope.third, properties.getProperty(scope.third));
    if (classnameListSize != 0 && list.size() != classnameListSize) {
      throw new InvalidModelException(String.format(
          "Subsequent entry for scope <%s> has different size", SubstitutionScope.CLASSNAME));
    }
    classNameScope.add(new Pair<>(scope.second, list));
    return list.size();
  }


  /**
   * Adds the class scope.
   *
   * @param classnameListSize the classname scope list size
   * @param scope the scope
   * @throws InvalidModelException If the scope is not valid
   */
  private void addClassScope(int classnameListSize,
      final Triple<SubstitutionScope, String, String> scope) throws InvalidModelException {
    if (classnameListSize == 0) {
      // Not allowed when no classname scope element
      throw new InvalidModelException("Invalid scope : " + SubstitutionScope.CLASS);
    }
    final List<Object> list = splitValueToObject(scope.third, properties.getProperty(scope.third));
    // Any class scope must match the classname scope list size
    if (list.size() != classnameListSize) {
      throw new InvalidModelException(
          String.format("Entry for scope <%s> has different size than <%s>",
              SubstitutionScope.CLASS, SubstitutionScope.CLASSNAME));
    }
    classScope.add(new Pair<>(scope.second, list));
  }


  /**
   * Adds the template scope.
   *
   * @param scope the scope
   * @throws InvalidModelException If the scope is not valid
   */
  private void addTemplateScope(final Triple<SubstitutionScope, String, String> scope)
      throws InvalidModelException {
    templateScope.add(new Pair<>(scope.second,
        splitValueToObject(scope.third, properties.getProperty(scope.third))));
  }

  /**
   * Split the string value into a list using whitespace.
   *
   * @param key the key
   * @param value the value
   * @return the list
   * @throws InvalidModelException If the value is empty
   */
  private static List<String> splitValue(String key, String value) throws InvalidModelException {
    if (StringUtils.isNullOrEmpty(value)) {
      throw new InvalidModelException("Substitution value is empty for key: " + key);
    }
    final String[] values = WHITESPACE_PATTERN.split(value);
    if (values.length == 0) {
      throw new InvalidModelException("Substitution value is only whitepsace for key: " + key);
    }
    return Arrays.asList(values);
  }

  /**
   * Split the string value into a list using whitespace.
   *
   * @param key the key
   * @param value the value
   * @return the list
   * @throws InvalidModelException If the value is empty
   */
  private static List<Object> splitValueToObject(String key, String value)
      throws InvalidModelException {
    if (StringUtils.isNullOrEmpty(value)) {
      throw new InvalidModelException("Substitution value is empty for key: " + key);
    }
    // Currently advanced splitting to mutliple value is not supported
    final String[] values = WHITESPACE_PATTERN.split(value);
    if (values.length == 0) {
      throw new InvalidModelException("Substitution value is only whitepsace for key: " + key);
    }
    return Arrays.asList((Object[]) values);
  }

  /**
   * Gets the class name to use for the template. This may require substitutions.
   *
   * @return the name
   */
  public String getTemplateClassName() {
    return templateClassName;
  }

  /**
   * Gets the template. This is a valid template for the StringTemplate library.
   *
   * @return the template
   */
  public String getTemplate() {
    return template;
  }

  /**
   * Gets the package name.
   *
   * @return the package name
   */
  public String getPackageName() {
    return packageName;
  }

  /**
   * Checks if the model specifies renamed output classes. This is true if there are substitutions
   * for the class name.
   *
   * @return true, if the model specifies renamed output classes
   * @see #getTemplateClassName()
   * @see #getClassNameScope()
   */
  public boolean isRenamedClass() {
    return !classNameScope.isEmpty();
  }

  /**
   * Gets the class name scope substitutions.
   *
   * @return the class name scope substitution.
   */
  public List<Pair<String, List<String>>> getClassNameScope() {
    return classNameScope;
  }

  /**
   * Gets the class scope substitutions.
   *
   * @return the class scope substitutions.
   */
  public List<Pair<String, List<Object>>> getClassScope() {
    return classScope;
  }

  /**
   * Gets the template scope substitutions.
   *
   * @return the template scope substitutions.
   */
  public List<Pair<String, List<Object>>> getTemplateScope() {
    return templateScope;
  }
}
