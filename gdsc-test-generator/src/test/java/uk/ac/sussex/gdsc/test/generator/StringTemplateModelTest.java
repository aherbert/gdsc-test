/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
 *
 * Generates Java classes for the GDSC Test project.
 * %%
 * Copyright (C) 2018 - 2020 Alex Herbert
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

package uk.ac.sussex.gdsc.test.generator;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("javadoc")
class StringTemplateModelTest {

  @Test
  void testCreateUsingClassnameScope() throws InvalidModelException {
    final Properties properties = new Properties();
    final String packageName = "org.something";
    final String templateClassName = "MyTemplate";
    final String template = "This does not matter";

    // OK
    properties.put("classname.My", "Your");
    properties.put("class.Anything", "value1");
    properties.put("template.Other", "value2");

    final StringTemplateModel model =
        StringTemplateModel.create(properties, packageName, templateClassName, template);

    Assertions.assertEquals(packageName, model.getPackageName());
    Assertions.assertEquals(templateClassName, model.getTemplateClassName());
    Assertions.assertEquals(template, model.getTemplate());

    Assertions.assertTrue(model.isRenamedClass(), "Is a renamed class");

    final List<Pair<String, List<String>>> classNameScope = model.getClassNameScope();
    final List<Pair<String, List<Object>>> classScope = model.getClassScope();
    final List<Pair<String, List<Object>>> templateScope = model.getTemplateScope();

    Assertions.assertEquals(1, classNameScope.size(), "Classname scope size");
    Assertions.assertEquals(1, classScope.size(), "Class scope size");
    Assertions.assertEquals(1, templateScope.size(), "Template scope size");

    // Check
    for (final Pair<String, List<String>> pair : classNameScope) {
      Assertions.assertEquals("My", pair.getKey(), "Missing classname scope substitution");
      Assertions.assertTrue(pair.getValue().contains("Your"), "Missing classname scope value");
    }

    for (final Pair<String, List<Object>> pair : classScope) {
      Assertions.assertEquals("Anything", pair.getKey(), "Missing class scope substitution");
      Assertions.assertTrue(pair.getValue().contains("value1"), "Missing class scope value");
    }

    for (final Pair<String, List<Object>> pair : templateScope) {
      Assertions.assertEquals("Other", pair.getKey(), "Missing template scope substitution");
      Assertions.assertTrue(pair.getValue().contains("value2"), "Missing template scope value");
    }
  }

  @Test
  void testCreateUsingTemplateScope() throws InvalidModelException {
    final Properties properties = new Properties();
    final String packageName = "org.something";
    final String templateClassName = "MyTemplate";
    final String template = "This does not matter";

    // OK
    properties.put("template.Other", "value2");

    final StringTemplateModel model =
        StringTemplateModel.create(properties, packageName, templateClassName, template);

    Assertions.assertEquals(packageName, model.getPackageName());
    Assertions.assertEquals(templateClassName, model.getTemplateClassName());
    Assertions.assertEquals(template, model.getTemplate());

    Assertions.assertFalse(model.isRenamedClass(), "Not a renamed class");

    final List<Pair<String, List<String>>> classNameScope = model.getClassNameScope();
    final List<Pair<String, List<Object>>> classScope = model.getClassScope();
    final List<Pair<String, List<Object>>> templateScope = model.getTemplateScope();

    Assertions.assertEquals(0, classNameScope.size(), "Classname scope size");
    Assertions.assertEquals(0, classScope.size(), "Class scope size");
    Assertions.assertEquals(1, templateScope.size(), "Template scope size");

    // Check
    for (final Pair<String, List<Object>> pair : templateScope) {
      Assertions.assertEquals("Other", pair.getKey(), "Missing template scope substitution");
      Assertions.assertTrue(pair.getValue().contains("value2"), "Missing template scope value");
    }
  }

  @Test
  void testCreateUsingSpecialValues() throws InvalidModelException {
    final Properties properties = new Properties();
    final String packageName = "org.something";
    final String templateClassName = "MyTemplate";
    final String template = "<Other> = 1;";

    // OK
    properties.put("classname.My", "His Her Your");
    properties.put("class.Anything", "value1 \\N value3");
    properties.put("template.Other", "\"\" value2 \\N \" \"");

    final StringTemplateModel model =
        StringTemplateModel.create(properties, packageName, templateClassName, template);

    Assertions.assertEquals(packageName, model.getPackageName());
    Assertions.assertEquals(templateClassName, model.getTemplateClassName());
    Assertions.assertEquals(template, model.getTemplate());

    Assertions.assertTrue(model.isRenamedClass(), "Is a renamed class");

    final List<Pair<String, List<String>>> classNameScope = model.getClassNameScope();
    final List<Pair<String, List<Object>>> classScope = model.getClassScope();
    final List<Pair<String, List<Object>>> templateScope = model.getTemplateScope();

    Assertions.assertEquals(1, classNameScope.size(), "Classname scope size");
    Assertions.assertEquals(1, classScope.size(), "Class scope size");
    Assertions.assertEquals(1, templateScope.size(), "Template scope size");

    // Check
    final Pair<String, List<String>> classNamePair = classNameScope.get(0);
    Assertions.assertEquals("My", classNamePair.getKey(), "Missing classname scope substitution");
    Assertions.assertTrue(classNamePair.getValue().contains("Your"),
        "Missing classname scope value");

    final Pair<String, List<Object>> classPair = classScope.get(0);
    Assertions.assertEquals("Anything", classPair.getKey(), "Missing class scope substitution");
    Assertions.assertEquals(Arrays.asList("value1", null, "value3"), classPair.getValue(),
        "Missing class scope values");

    final Pair<String, List<Object>> templatePair = templateScope.get(0);
    Assertions.assertEquals("Other", templatePair.getKey(), "Missing template scope substitution");
    Assertions.assertEquals(Arrays.asList("", "value2", null, " "), templatePair.getValue(),
        "Missing template scope values");
  }

  @Test
  void testCreateThrowsWithNullPointers() {
    final Properties properties = new Properties();
    final String packageName = "org.something";
    final String templateClassName = "MyTemplate";
    final String template = "";

    // Check these arguments are OK on their own
    Assertions.assertDoesNotThrow(() -> {
      StringTemplateModel.create(properties, packageName, templateClassName, template);
    });

    Assertions.assertThrows(NullPointerException.class, () -> {
      StringTemplateModel.create(null, packageName, templateClassName, template);
    });
    Assertions.assertThrows(NullPointerException.class, () -> {
      StringTemplateModel.create(properties, null, templateClassName, template);
    });
    Assertions.assertThrows(NullPointerException.class, () -> {
      StringTemplateModel.create(properties, packageName, null, template);
    });
    Assertions.assertThrows(NullPointerException.class, () -> {
      StringTemplateModel.create(properties, packageName, templateClassName, null);
    });
  }

  @Test
  void testCreateThrowsWithBadClassName() {
    final Properties properties = new Properties();
    final String packageName = "org.something";
    final String templateClassName = "My.Template"; // Bad character
    final String template = "";

    final String message = Assertions.assertThrows(InvalidModelException.class, () -> {
      StringTemplateModel.create(properties, packageName, templateClassName, template);
    }).getMessage();
    assertMessageContains(message, "class name", "class name contains illegal characters");
  }

  @Test
  void testCreateThrowsWithBadPackageName() {
    final Properties properties = new Properties();
    final String packageName = "org)something"; // Bad character
    final String templateClassName = "MyTemplate";
    final String template = "";

    // Check these arguments are OK
    Assertions.assertDoesNotThrow(() -> {
      StringTemplateModel.create(properties, "", templateClassName, template);
    }, "Throws with empty package name");

    final String message = Assertions.assertThrows(InvalidModelException.class, () -> {
      StringTemplateModel.create(properties, packageName, templateClassName, template);
    }).getMessage();
    assertMessageContains(message, "package name", "package name contains illegal characters");
  }


  @Test
  void testCreateThrowsWithInvalidScope() {
    final Properties properties = new Properties();
    final String packageName = "";
    final String templateClassName = "MyTemplate";
    final String template = "";

    // Not a valid scope
    properties.clear();
    properties.put("test.Other", "value");

    final String message = Assertions.assertThrows(InvalidModelException.class, () -> {
      StringTemplateModel.create(properties, packageName, templateClassName, template);
    }).getMessage();
    assertMessageContains(message, "Invalid scope", "scope is invalid");
    assertMessageContains(message, "test", "the scope");
  }

  @Test
  void testCreateThrowsWithNoKey() {
    final Properties properties = new Properties();
    final String packageName = "";
    final String templateClassName = "MyTemplate";
    final String template = "";

    // No key
    final String key = "";
    properties.put(key, "value");

    final String message = Assertions.assertThrows(InvalidModelException.class, () -> {
      StringTemplateModel.create(properties, packageName, templateClassName, template);
    }).getMessage();
    assertMessageContains(message, "key", "the fail reason was a bad key");
  }

  @Test
  void testCreateThrowsWithNoScope() {
    final Properties properties = new Properties();
    final String packageName = "";
    final String templateClassName = "MyTemplate";
    final String template = "";

    // No scope
    final String key = ".Other";
    properties.put(key, "value");

    final String message = Assertions.assertThrows(InvalidModelException.class, () -> {
      StringTemplateModel.create(properties, packageName, templateClassName, template);
    }).getMessage();
    assertMessageContains(message, "key", "the fail reason was a bad key");
    assertMessageContains(message, key, "the key");
  }

  @Test
  void testCreateThrowsWithNoSubstritution() {
    final Properties properties = new Properties();
    final String packageName = "";
    final String templateClassName = "MyTemplate";
    final String template = "";

    // No substitution
    final String key = "class.";
    properties.put(key, "value");

    final String message = Assertions.assertThrows(InvalidModelException.class, () -> {
      StringTemplateModel.create(properties, packageName, templateClassName, template);
    }).getMessage();
    assertMessageContains(message, "key", "the fail reason was a bad key");
    assertMessageContains(message, key, "the key");
  }

  @Test
  void testCreateThrowsWithReservedWordForSubstitution() {
    final Properties properties = new Properties();
    final String packageName = "";
    final String templateClassName = "MyTemplate";
    final String template = "";

    // Reserved word
    final String key = "class.true";
    properties.put(key, "value");

    final String message = Assertions.assertThrows(InvalidModelException.class, () -> {
      StringTemplateModel.create(properties, packageName, templateClassName, template);
    }).getMessage();
    assertMessageContains(message, "Reserved substitution", "the fail reason was reserved word");
    assertMessageContains(message, "true", "the substitution");
  }

  @Test
  void testCreateThrowsWithDuplicateSubstitution() {
    final Properties properties = new Properties();
    final String packageName = "";
    final String templateClassName = "MyTemplate";
    final String template = "";

    // Duplicate substitution
    final String key = "class.Anything";
    final String key2 = "template.Anything";
    properties.put(key, "value");
    properties.put(key2, "value");

    final String message = Assertions.assertThrows(InvalidModelException.class, () -> {
      StringTemplateModel.create(properties, packageName, templateClassName, template);
    }).getMessage();
    assertMessageContains(message, "Duplicate substitution",
        "the fail reason was a duplicate substitution");
    assertMessageContains(message, "Anything", "the substitution");
  }

  @Test
  void testCreateThrowsWithDuplicateLowercaseClassnameSubstitution() {
    final Properties properties = new Properties();
    final String packageName = "";
    final String templateClassName = "MyTemplate";
    final String template = "";

    // Duplicate of lower-case classname substitution
    final String key = "classname.Anything";
    final String key2 = "template.anything";
    properties.put(key, "value");
    properties.put(key2, "value");

    final String message = Assertions.assertThrows(InvalidModelException.class, () -> {
      StringTemplateModel.create(properties, packageName, templateClassName, template);
    }).getMessage();
    assertMessageContains(message, "Duplicate substitution",
        "the fail reason was a duplicate substitution");
    assertMessageContains(message, "anything", "the substitution");
  }

  @Test
  void testCreateThrowsWithDuplicateClassnameSubstitution() throws InvalidModelException {
    final Properties properties = new Properties();
    final String packageName = "";
    final String templateClassName = "MyTemplate";
    final String template = "";

    // This test the edge case where the classname substitution is already lower-case.
    final String key = "classname.plate";
    properties.put(key, "value");

    StringTemplateModel.create(properties, packageName, templateClassName, template);

    // Duplicate of classname substitution.
    final String key2 = "template.plate";
    properties.put(key2, "value");

    final String message = Assertions.assertThrows(InvalidModelException.class, () -> {
      StringTemplateModel.create(properties, packageName, templateClassName, template);
    }).getMessage();
    assertMessageContains(message, "Duplicate substitution",
        "the fail reason was a duplicate substitution");
    assertMessageContains(message, "plate", "the substitution");
  }

  @Test
  void testCreateThrowsWithEmptyClassnameScopeValue() {
    final Properties properties = new Properties();
    final String packageName = "";
    final String templateClassName = "MyTemplate";
    final String template = "";

    // No value in classname scope
    final String key = "classname.My";
    final String value = "";
    properties.clear();
    properties.put(key, value);

    final String message = Assertions.assertThrows(InvalidModelException.class, () -> {
      StringTemplateModel.create(properties, packageName, templateClassName, template);
    }).getMessage();
    assertMessageContains(message, "value", "the fail reason was a bad value");
  }

  @Test
  void testCreateThrowsWithOnlyWhitespaceInClassnameScopeValue() {
    final Properties properties = new Properties();
    final String packageName = "";
    final String templateClassName = "MyTemplate";
    final String template = "";

    // Whitespace value in classname scope
    final String key = "classname.My";
    final String value = "  ";
    properties.clear();
    properties.put(key, value);

    final String message = Assertions.assertThrows(InvalidModelException.class, () -> {
      StringTemplateModel.create(properties, packageName, templateClassName, template);
    }).getMessage();
    assertMessageContains(message, "value", "the fail reason was a bad value");
  }

  @Test
  void testCreateThrowsWithEmptyTemplateScopeValue() {
    final Properties properties = new Properties();
    final String packageName = "";
    final String templateClassName = "MyTemplate";
    final String template = "";

    // No value in classname scope
    final String key = "template.My";
    final String value = "";
    properties.clear();
    properties.put(key, value);

    final String message = Assertions.assertThrows(InvalidModelException.class, () -> {
      StringTemplateModel.create(properties, packageName, templateClassName, template);
    }).getMessage();
    assertMessageContains(message, "value", "the fail reason was a bad value");
  }

  @Test
  void testCreateThrowsWithOnlyWhitespaceInTemplateScopeValue() {
    final Properties properties = new Properties();
    final String packageName = "";
    final String templateClassName = "MyTemplate";
    final String template = "";

    // Whitespace value in classname scope
    final String key = "template.My";
    final String value = "  ";
    properties.clear();
    properties.put(key, value);

    final String message = Assertions.assertThrows(InvalidModelException.class, () -> {
      StringTemplateModel.create(properties, packageName, templateClassName, template);
    }).getMessage();
    assertMessageContains(message, "value", "the fail reason was a bad value");
  }

  @Test
  void testCreateThrowsWithClassScopeAndNoClassnameScope() {
    final Properties properties = new Properties();
    final String packageName = "";
    final String templateClassName = "MyTemplate";
    final String template = "";

    // No corresponding classname scope
    final String key = "class.My";
    final String value = "value";
    properties.clear();
    properties.put(key, value);

    final String message = Assertions.assertThrows(InvalidModelException.class, () -> {
      StringTemplateModel.create(properties, packageName, templateClassName, template);
    }).getMessage();
    assertMessageContains(message, "scope", "the fail reason was a bad scope");
  }

  @Test
  void testCreateThrowsWithClassScopeDifferentValueSizeToClassnameScope() {
    final Properties properties = new Properties();
    final String packageName = "";
    final String templateClassName = "MyTemplate";
    final String template = "";

    properties.clear();
    // Different value sizes
    properties.put("classname.My", "value");
    properties.put("class.Anything", "value1 value2");

    final String message = Assertions.assertThrows(InvalidModelException.class, () -> {
      StringTemplateModel.create(properties, packageName, templateClassName, template);
    }).getMessage();
    assertMessageContains(message, "scope", "the fail reason was a different size scope");
    assertMessageContains(message, "size", "the fail reason was a different size scope");
  }

  @Test
  void testCreateThrowsWithClassnameScopeDifferentValueSizeToClassnameScope() {
    final Properties properties = new Properties();
    final String packageName = "";
    final String templateClassName = "MyTemplate";
    final String template = "";

    properties.clear();
    properties.put("classname.My", "value");
    // Same value sizes
    properties.put("classname.Template", "value1");

    Assertions.assertDoesNotThrow(() -> {
      StringTemplateModel.create(properties, "", templateClassName, template);
    }, "Throws with valid classname scope value size");

    // Different value sizes
    properties.put("classname.Template", "value1 value2");

    final String message = Assertions.assertThrows(InvalidModelException.class, () -> {
      StringTemplateModel.create(properties, packageName, templateClassName, template);
    }).getMessage();
    assertMessageContains(message, "scope", "the fail reason was a different size scope");
    assertMessageContains(message, "size", "the fail reason was a different size scope");
    assertMessageContains(message, "Subsequent",
        "the fail reason was a different size subsequent classname scope");
  }

  @Test
  void testCreateThrowsClassnameSubstitutionNotInClassName() {
    final Properties properties = new Properties();
    final String packageName = "";
    final String templateClassName = "MyTemplate";
    final String template = "";

    // OK
    properties.put("classname.My", "Your");

    // Check these arguments are OK
    Assertions.assertDoesNotThrow(() -> {
      StringTemplateModel.create(properties, "", templateClassName, template);
    }, "Throws with valid classname scope");

    properties.put("classname.NotMy", "NotYour");

    final String message = Assertions.assertThrows(InvalidModelException.class, () -> {
      StringTemplateModel.create(properties, packageName, templateClassName, template);
    }).getMessage();
    assertMessageContains(message, "substitution", "the fail reason was bad substitution");
    assertMessageContains(message, "NotMy", "the bad substitution");
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
