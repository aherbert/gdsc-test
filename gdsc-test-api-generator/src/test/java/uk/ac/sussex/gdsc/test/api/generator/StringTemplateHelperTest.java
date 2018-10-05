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

import java.util.List;
import java.util.Properties;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("javadoc")
public class StringTemplateHelperTest {

  @Test
  public void testGeneratorWithTemplateSubstitution() throws InvalidModelException {
    final Properties properties = new Properties();
    final String packageName = "";
    final String templateClassName = "MyTemplate";
    final String template = "<Other> = 1;";

    properties.put("template.Other", "value");

    final StringTemplateModel model =
        new StringTemplateModel(properties, packageName, templateClassName, template);

    final List<String> names = StringTemplateHelper.listNames(model);

    Assertions.assertEquals(1, names.size(), "Names size");
    Assertions.assertEquals(templateClassName, names.get(0), "Template name");

    final List<Pair<String, String>> list = StringTemplateHelper.generate(model);

    Assertions.assertEquals(names.size(), list.size(), "List size mismatch to names size");

    Assertions.assertEquals(names.get(0), list.get(0).first, "Name is wrong");
    Assertions.assertEquals("value = 1;", list.get(0).second, "Output is wrong");
  }

  @Test
  public void testGeneratorWithEmptyPackageSubstitution() throws InvalidModelException {
    final Properties properties = new Properties();
    final String packageName = "";
    final String templateClassName = "MyTemplate";
    final String template = "<package>";

    final StringTemplateModel model =
        new StringTemplateModel(properties, packageName, templateClassName, template);

    final List<String> names = StringTemplateHelper.listNames(model);

    Assertions.assertEquals(1, names.size(), "Names size");
    Assertions.assertEquals(templateClassName, names.get(0), "Template name");

    final List<Pair<String, String>> list = StringTemplateHelper.generate(model);

    Assertions.assertEquals(names.size(), list.size(), "List size mismatch to names size");

    Assertions.assertEquals(names.get(0), list.get(0).first, "Name is wrong");
    Assertions.assertEquals("", list.get(0).second, "Output is wrong");
  }

  @Test
  public void testGeneratorWithPackageSubstitution() throws InvalidModelException {
    final Properties properties = new Properties();
    final String packageName = "org.something";
    final String templateClassName = "MyTemplate";
    final String template = "<package>";

    final StringTemplateModel model =
        new StringTemplateModel(properties, packageName, templateClassName, template);

    final List<String> names = StringTemplateHelper.listNames(model);

    Assertions.assertEquals(1, names.size(), "Names size");
    Assertions.assertEquals(templateClassName, names.get(0), "Template name");

    final List<Pair<String, String>> list = StringTemplateHelper.generate(model);

    Assertions.assertEquals(names.size(), list.size(), "List size mismatch to names size");

    Assertions.assertEquals(names.get(0), list.get(0).first, "Name is wrong");
    Assertions.assertEquals(String.format("package %s;", packageName), list.get(0).second,
        "Output is wrong");
  }

  @Test
  public void testGeneratorWithClassnameSubstitution() throws InvalidModelException {
    final Properties properties = new Properties();
    final String packageName = "";
    final String templateClassName = "MyTemplate";
    final String template = "Stuff";

    properties.put("classname.My", "His Her");

    final StringTemplateModel model =
        new StringTemplateModel(properties, packageName, templateClassName, template);

    final List<String> names = StringTemplateHelper.listNames(model);

    Assertions.assertEquals(2, names.size(), "Names size");
    Assertions.assertEquals("HisTemplate", names.get(0), "name 1 is wrong");
    Assertions.assertEquals("HerTemplate", names.get(1), "name 2 is wrong");

    final List<Pair<String, String>> list = StringTemplateHelper.generate(model);

    Assertions.assertEquals(names.size(), list.size());

    for (int i = 0; i < names.size(); i++) {
      final int nameIndex = i + 1;
      Assertions.assertEquals(names.get(0), list.get(0).first,
          () -> String.format("Name %d is wrong", nameIndex));

      Assertions.assertEquals(template, list.get(0).second,
          () -> String.format("Output %d is wrong", nameIndex));
    }
  }

  @Test
  public void testGeneratorWithLowercaseClassnameSubstitution() throws InvalidModelException {
    final Properties properties = new Properties();
    final String packageName = "";
    final String templateClassName = "Myfoo";
    final String template = "<foo>";

    properties.put("classname.foo", "bar");

    final StringTemplateModel model =
        new StringTemplateModel(properties, packageName, templateClassName, template);

    final List<String> names = StringTemplateHelper.listNames(model);

    Assertions.assertEquals(1, names.size(), "Names size");
    Assertions.assertEquals("Mybar", names.get(0), "Template name");

    final List<Pair<String, String>> list = StringTemplateHelper.generate(model);

    Assertions.assertEquals(names.size(), list.size(), "List size mismatch to names size");

    Assertions.assertEquals(names.get(0), list.get(0).first, "Name is wrong");
    Assertions.assertEquals("bar", list.get(0).second, "Output is wrong");
  }

  @Test
  public void testGeneratorWithClassSubstitution() throws InvalidModelException {
    final Properties properties = new Properties();
    final String packageName = "";
    final String templateClassName = "MyTemplate";
    final String template = "<gender>";

    properties.put("classname.My", "His Her");
    properties.put("class.gender", "Male Female");

    final StringTemplateModel model =
        new StringTemplateModel(properties, packageName, templateClassName, template);

    final List<String> names = StringTemplateHelper.listNames(model);

    Assertions.assertEquals(2, names.size(), "Names size");
    Assertions.assertEquals("HisTemplate", names.get(0), "name 1 is wrong");
    Assertions.assertEquals("HerTemplate", names.get(1), "name 2 is wrong");

    final List<Pair<String, String>> list = StringTemplateHelper.generate(model);

    Assertions.assertEquals(names.size(), list.size());

    for (int i = 0; i < names.size(); i++) {
      final int nameIndex = i + 1;
      Assertions.assertEquals(names.get(0), list.get(0).first,
          () -> String.format("Name %d is wrong", nameIndex));
    }

    Assertions.assertEquals("Male", list.get(0).second, "Output 1 is wrong");
    Assertions.assertEquals("Female", list.get(1).second, "Output 2 is wrong");
  }

  @Test
  public void testGeneratorWithListTemplateSubstitution() throws InvalidModelException {
    final Properties properties = new Properties();
    final String packageName = "org.test";
    final String templateClassName = "MyTemplate";
    final String template = "<package>\n<names,values:{name,value|<name>=<value>;\n}>";

    properties.put("template.names", "bill ben");
    properties.put("template.values", "flower pot");

    final StringTemplateModel model =
        new StringTemplateModel(properties, packageName, templateClassName, template);

    final List<String> names = StringTemplateHelper.listNames(model);

    Assertions.assertEquals(1, names.size(), "Names size");
    Assertions.assertEquals(templateClassName, names.get(0), "Template name");

    final List<Pair<String, String>> list = StringTemplateHelper.generate(model);

    Assertions.assertEquals(names.size(), list.size(), "List size mismatch to names size");

    Assertions.assertEquals(names.get(0), list.get(0).first, "Name is wrong");
    // Use the platform new line format string
    final String expected = String.format("package org.test;%nbill=flower;%nben=pot;%n");
    Assertions.assertEquals(expected, list.get(0).second, "Output is wrong");
  }
}
