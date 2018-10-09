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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Properties;

@SuppressWarnings("javadoc")
public class StringTemplateModelTest {

  @Test
  public void testConstructorUsingClassnameScope() throws InvalidModelException {
    final Properties properties = new Properties();
    final String packageName = "org.something";
    final String templateClassName = "MyTemplate";
    final String template = "<Other> = 1;";

    // OK
    properties.put("classname.My", "Your");
    properties.put("class.Anything", "value1");
    properties.put("template.Other", "value2");

    final StringTemplateModel model =
        new StringTemplateModel(properties, packageName, templateClassName, template);

    Assertions.assertEquals(packageName, model.getPackageName());
    Assertions.assertEquals(templateClassName, model.getTemplateClassName());
    Assertions.assertEquals(template, model.getTemplate());

    Assertions.assertTrue(model.isRenamedClass(), "Is a renamed class");

    final List<Pair<String, List<String>>> classNameScope = model.getClassNameScope();
    final List<Pair<String, List<Object>>> classScope = model.getClassScope();
    final List<Pair<String, List<Object>>> templateScope = model.getTemplateScope();

    Assertions.assertFalse(classNameScope.isEmpty());
    Assertions.assertFalse(classScope.isEmpty());
    Assertions.assertFalse(templateScope.isEmpty());

    // Check
    for (final Pair<String, List<String>> pair : classNameScope) {
      Assertions.assertEquals("My", pair.first, "Missing classname scope substitution");
      Assertions.assertTrue(pair.second.contains("Your"), "Missing classname scope value");
    }

    for (final Pair<String, List<Object>> pair : classScope) {
      Assertions.assertEquals("Anything", pair.first, "Missing class scope substitution");
      Assertions.assertTrue(pair.second.contains("value1"), "Missing class scope value");
    }

    for (final Pair<String, List<Object>> pair : templateScope) {
      Assertions.assertEquals("Other", pair.first, "Missing template scope substitution");
      Assertions.assertTrue(pair.second.contains("value2"), "Missing template scope value");
    }
  }

  @Test
  public void testConstructorUsingTemplateScope() throws InvalidModelException {
    final Properties properties = new Properties();
    final String packageName = "org.something";
    final String templateClassName = "MyTemplate";
    final String template = "<Other> = 1;";

    // OK
    properties.put("template.Other", "value2");

    final StringTemplateModel model =
        new StringTemplateModel(properties, packageName, templateClassName, template);

    Assertions.assertEquals(packageName, model.getPackageName());
    Assertions.assertEquals(templateClassName, model.getTemplateClassName());
    Assertions.assertEquals(template, model.getTemplate());

    Assertions.assertFalse(model.isRenamedClass(), "Not a renamed class");

    final List<Pair<String, List<String>>> classNameScope = model.getClassNameScope();
    final List<Pair<String, List<Object>>> classScope = model.getClassScope();
    final List<Pair<String, List<Object>>> templateScope = model.getTemplateScope();

    Assertions.assertTrue(classNameScope.isEmpty());
    Assertions.assertTrue(classScope.isEmpty());
    Assertions.assertFalse(templateScope.isEmpty());

    // Check
    for (final Pair<String, List<Object>> pair : templateScope) {
      Assertions.assertEquals("Other", pair.first, "Missing template scope substitution");
      Assertions.assertTrue(pair.second.contains("value2"), "Missing template scope value");
    }
  }

  @SuppressWarnings("unused")
  @Test
  public void testConstructorThrowsWithNullPointers() {
    final Properties properties = new Properties();
    final String packageName = "org.something";
    final String templateClassName = "MyTemplate";
    final String template = "";

    // Check these arguments are OK on their own
    Assertions.assertDoesNotThrow(() -> {
      new StringTemplateModel(properties, packageName, templateClassName, template);
    });

    Assertions.assertThrows(NullPointerException.class, () -> {
      new StringTemplateModel(null, packageName, templateClassName, template);
    });
    Assertions.assertThrows(NullPointerException.class, () -> {
      new StringTemplateModel(properties, null, templateClassName, template);
    });
    Assertions.assertThrows(NullPointerException.class, () -> {
      new StringTemplateModel(properties, packageName, null, template);
    });
    Assertions.assertThrows(NullPointerException.class, () -> {
      new StringTemplateModel(properties, packageName, templateClassName, null);
    });
  }

  @Test
  public void testConstructorThrowsWithBadClassName() {
    final Properties properties = new Properties();
    final String packageName = "org.something";
    final String templateClassName = "My.Template"; // Bad character
    final String template = "";

    @SuppressWarnings("unused")
    final String message = Assertions.assertThrows(InvalidModelException.class, () -> {
      new StringTemplateModel(properties, packageName, templateClassName, template);
    }).getMessage();
    assertMessageContains(message, "class name", "class name contains illegal characters");
  }

  @SuppressWarnings("unused")
  @Test
  public void testConstructorThrowsWithBadPackageName() {
    final Properties properties = new Properties();
    final String packageName = "org)something"; // Bad character
    final String templateClassName = "MyTemplate";
    final String template = "";

    // Check these arguments are OK
    Assertions.assertDoesNotThrow(() -> {
      new StringTemplateModel(properties, "", templateClassName, template);
    }, "Throws with empty package name");

    final String message = Assertions.assertThrows(InvalidModelException.class, () -> {
      new StringTemplateModel(properties, packageName, templateClassName, template);
    }).getMessage();
    assertMessageContains(message, "package name", "package name contains illegal characters");
  }


  @SuppressWarnings("unused")
  @Test
  public void testConstructorThrowsWithInvalidScope() {
    final Properties properties = new Properties();
    final String packageName = "";
    final String templateClassName = "MyTemplate";
    final String template = "";

    // Not a valid scope
    properties.clear();
    properties.put("test.Other", "value");

    final String message = Assertions.assertThrows(InvalidModelException.class, () -> {
      new StringTemplateModel(properties, packageName, templateClassName, template);
    }).getMessage();
    assertMessageContains(message, "Invalid scope", "scope is invalid");
    assertMessageContains(message, "test", "the scope");
  }

  @SuppressWarnings("unused")
  @Test
  public void testConstructorThrowsWithNoKey() {
    final Properties properties = new Properties();
    final String packageName = "";
    final String templateClassName = "MyTemplate";
    final String template = "";

    // No key
    final String key = "";
    properties.put(key, "value");

    final String message = Assertions.assertThrows(InvalidModelException.class, () -> {
      new StringTemplateModel(properties, packageName, templateClassName, template);
    }).getMessage();
    assertMessageContains(message, "key", "the fail reason was a bad key");
  }

  @SuppressWarnings("unused")
  @Test
  public void testConstructorThrowsWithNoScope() {
    final Properties properties = new Properties();
    final String packageName = "";
    final String templateClassName = "MyTemplate";
    final String template = "";

    // No scope
    final String key = ".Other";
    properties.put(key, "value");

    final String message = Assertions.assertThrows(InvalidModelException.class, () -> {
      new StringTemplateModel(properties, packageName, templateClassName, template);
    }).getMessage();
    assertMessageContains(message, "key", "the fail reason was a bad key");
    assertMessageContains(message, key, "the key");
  }

  @SuppressWarnings("unused")
  @Test
  public void testConstructorThrowsWithNoSubstritution() {
    final Properties properties = new Properties();
    final String packageName = "";
    final String templateClassName = "MyTemplate";
    final String template = "";

    // No substitution
    final String key = "class.";
    properties.put(key, "value");

    final String message = Assertions.assertThrows(InvalidModelException.class, () -> {
      new StringTemplateModel(properties, packageName, templateClassName, template);
    }).getMessage();
    assertMessageContains(message, "key", "the fail reason was a bad key");
    assertMessageContains(message, key, "the key");
  }

  @SuppressWarnings("unused")
  @Test
  public void testConstructorThrowsWithReservedWordForSubstitution() {
    final Properties properties = new Properties();
    final String packageName = "";
    final String templateClassName = "MyTemplate";
    final String template = "";

    // Reserved word
    final String key = "class.true";
    properties.put(key, "value");

    final String message = Assertions.assertThrows(InvalidModelException.class, () -> {
      new StringTemplateModel(properties, packageName, templateClassName, template);
    }).getMessage();
    assertMessageContains(message, "Reserved substitution", "the fail reason was reserved word");
    assertMessageContains(message, "true", "the substitution");
  }

  @SuppressWarnings("unused")
  @Test
  public void testConstructorThrowsWithDuplicateSubstitution() {
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
      new StringTemplateModel(properties, packageName, templateClassName, template);
    }).getMessage();
    assertMessageContains(message, "Duplicate substitution",
        "the fail reason was a duplicate substitution");
    assertMessageContains(message, "Anything", "the substitution");
  }

  @SuppressWarnings("unused")
  @Test
  public void testConstructorThrowsWithDuplicateLowercaseClassnameSubstitution() {
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
      new StringTemplateModel(properties, packageName, templateClassName, template);
    }).getMessage();
    assertMessageContains(message, "Duplicate substitution",
        "the fail reason was a duplicate substitution");
    assertMessageContains(message, "anything", "the substitution");
  }

  @SuppressWarnings("unused")
  @Test
  public void testConstructorThrowsWithDuplicateClassnameSubstitution()
      throws InvalidModelException {
    final Properties properties = new Properties();
    final String packageName = "";
    final String templateClassName = "MyTemplate";
    final String template = "";

    // This test the edge case where the classname substitution is already lower-case.
    final String key = "classname.plate";
    properties.put(key, "value");

    new StringTemplateModel(properties, packageName, templateClassName, template);

    // Duplicate of classname substitution.
    final String key2 = "template.plate";
    properties.put(key2, "value");

    final String message = Assertions.assertThrows(InvalidModelException.class, () -> {
      new StringTemplateModel(properties, packageName, templateClassName, template);
    }).getMessage();
    assertMessageContains(message, "Duplicate substitution",
        "the fail reason was a duplicate substitution");
    assertMessageContains(message, "plate", "the substitution");
  }

  @SuppressWarnings("unused")
  @Test
  public void testConstructorThrowsWithEmptyClassnameScopeValue() {
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
      new StringTemplateModel(properties, packageName, templateClassName, template);
    }).getMessage();
    assertMessageContains(message, "value", "the fail reason was a bad value");
  }

  @SuppressWarnings("unused")
  @Test
  public void testConstructorThrowsWithOnlyWhitespaceInClassnameScopeValue() {
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
      new StringTemplateModel(properties, packageName, templateClassName, template);
    }).getMessage();
    assertMessageContains(message, "value", "the fail reason was a bad value");
  }

  @SuppressWarnings("unused")
  @Test
  public void testConstructorThrowsWithEmptyTemplateScopeValue() {
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
      new StringTemplateModel(properties, packageName, templateClassName, template);
    }).getMessage();
    assertMessageContains(message, "value", "the fail reason was a bad value");
  }

  @SuppressWarnings("unused")
  @Test
  public void testConstructorThrowsWithOnlyWhitespaceInTemplateScopeValue() {
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
      new StringTemplateModel(properties, packageName, templateClassName, template);
    }).getMessage();
    assertMessageContains(message, "value", "the fail reason was a bad value");
  }

  @SuppressWarnings("unused")
  @Test
  public void testConstructorThrowsWithClassScopeAndNoClassnameScope() {
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
      new StringTemplateModel(properties, packageName, templateClassName, template);
    }).getMessage();
    assertMessageContains(message, "scope", "the fail reason was a bad scope");
  }

  @SuppressWarnings("unused")
  @Test
  public void testConstructorThrowsWithClassScopeDifferentValueSizeToClassnameScope() {
    final Properties properties = new Properties();
    final String packageName = "";
    final String templateClassName = "MyTemplate";
    final String template = "";

    properties.clear();
    // Different value sizes
    properties.put("classname.My", "value");
    properties.put("class.Anything", "value1 value2");

    final String message = Assertions.assertThrows(InvalidModelException.class, () -> {
      new StringTemplateModel(properties, packageName, templateClassName, template);
    }).getMessage();
    assertMessageContains(message, "scope", "the fail reason was a different size scope");
    assertMessageContains(message, "size", "the fail reason was a different size scope");
  }

  @SuppressWarnings("unused")
  @Test
  public void testConstructorThrowsWithClassnameScopeDifferentValueSizeToClassnameScope() {
    final Properties properties = new Properties();
    final String packageName = "";
    final String templateClassName = "MyTemplate";
    final String template = "";

    properties.clear();
    properties.put("classname.My", "value");
    // Same value sizes
    properties.put("classname.Template", "value1");

    Assertions.assertDoesNotThrow(() -> {
      new StringTemplateModel(properties, "", templateClassName, template);
    }, "Throws with valid classname scope value size");

    // Different value sizes
    properties.put("classname.Template", "value1 value2");

    final String message = Assertions.assertThrows(InvalidModelException.class, () -> {
      new StringTemplateModel(properties, packageName, templateClassName, template);
    }).getMessage();
    assertMessageContains(message, "scope", "the fail reason was a different size scope");
    assertMessageContains(message, "size", "the fail reason was a different size scope");
    assertMessageContains(message, "Subsequent",
        "the fail reason was a different size subsequent classname scope");
  }

  @SuppressWarnings("unused")
  @Test
  public void testConstructorThrowsClassnameSubstitutionNotInClassName() {
    final Properties properties = new Properties();
    final String packageName = "";
    final String templateClassName = "MyTemplate";
    final String template = "";

    // OK
    properties.put("classname.My", "Your");

    // Check these arguments are OK
    Assertions.assertDoesNotThrow(() -> {
      new StringTemplateModel(properties, "", templateClassName, template);
    }, "Throws with valid classname scope");

    properties.put("classname.NotMy", "NotYour");

    final String message = Assertions.assertThrows(InvalidModelException.class, () -> {
      new StringTemplateModel(properties, packageName, templateClassName, template);
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
