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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.apache.commons.lang3.tuple.Pair;
import org.stringtemplate.v4.ST;

/**
 * Generate java source files from a template using the StringTemplate library.
 */
final class StringTemplates {

  /**
   * No construction.
   */
  private StringTemplates() {}

  /**
   * Generate the templates.
   *
   * <p>This deliberately omits class scope substitutions from an output class if the substitution
   * is the empty string.
   *
   * @param model the model
   * @return the list of templates
   */
  static List<Pair<String, String>> generate(StringTemplateModel model) {

    final List<Pair<String, String>> list = new ArrayList<>();

    final String name = model.getTemplateClassName();
    if (model.isRenamedClass()) {
      final List<Pair<String, List<String>>> classNameSubstitutions = model.getClassNameScope();
      final List<Pair<String, List<Object>>> classScopeSubstitutions = model.getClassScope();
      // All the same size
      final int size = classNameSubstitutions.get(0).getValue().size();
      for (int i = 0; i < size; i++) {
        // This has to be a new version, not a copy as that maintains some references, i.e.
        // Using a single ST and copying via 'ST st2 = new ST(st)' does not work.
        final ST st = createStringTemplate(model);

        String newName = name;
        // Each classname scope key will have an entry for this class.
        for (final Pair<String, List<String>> sub : classNameSubstitutions) {
          final String classNamePattern = sub.getKey();
          final String classNameSub = sub.getValue().get(i);
          // Perform the class name substitution
          newName = newName.replaceAll(classNamePattern, classNameSub);
          // Perform the class name substitution in the template
          st.add(classNamePattern, classNameSub);
          // Do it for lowercase as well, e.g. Double and double
          final String lc = classNamePattern.toLowerCase(Locale.getDefault());
          if (!lc.equals(classNamePattern)) {
            st.add(lc, classNameSub.toLowerCase(Locale.getDefault()));
          }
        }

        // Do the class substitutions.
        // Each class scope key will have an entry for this class.
        for (final Pair<String, List<Object>> sub : classScopeSubstitutions) {
          addClassSubstitution(st, sub.getKey(), sub.getValue().get(i));
        }

        // Build the output
        list.add(Pair.of(newName, st.render()));
      }
    } else {
      // A single output file
      list.add(Pair.of(name, createStringTemplate(model).render()));
    }

    return list;
  }

  /**
   * Adds the class substitution. This skips empty strings allowing class substitutions to be
   * skipped.
   *
   * @param st the string template
   * @param name the name
   * @param value the value
   */
  private static void addClassSubstitution(ST st, String name, Object value) {
    if (!skipSubstitution(value)) {
      st.add(name, value);
    }
  }

  /**
   * Test if the value of the substitution is the empty string.
   *
   * @param value the value
   * @return true, if successful
   */
  static boolean skipSubstitution(Object value) {
    return value instanceof String && ((String) value).length() == 0;
  }

  private static ST createStringTemplate(StringTemplateModel model) {
    final ST st = new ST(model.getTemplate());

    // Do all template scope substitutions
    for (final Pair<String, List<Object>> sub : model.getTemplateScope()) {
      st.add(sub.getKey(), sub.getValue());
    }
    // Inject the package
    st.add("package", getPackageName(model.getPackageName()));
    return st;
  }

  private static Object getPackageName(String packageName) {
    return Strings.isNotEmpty(packageName) ? "package " + packageName + ";" : "";
  }

  /**
   * List the template names that will be created using {@link #generate(StringTemplateModel)}.
   *
   * @param model the model
   * @return the list
   */
  static List<String> listNames(StringTemplateModel model) {
    final List<String> list = new ArrayList<>();
    final String name = model.getTemplateClassName();
    if (model.isRenamedClass()) {
      final List<Pair<String, List<String>>> classNameSubstitutions = model.getClassNameScope();
      // All the same size
      final int size = classNameSubstitutions.get(0).getValue().size();
      for (int i = 0; i < size; i++) {

        String newName = name;
        // Each classname scope key will have an entry for this class.
        for (final Pair<String, List<String>> sub : classNameSubstitutions) {
          final String classNamePattern = sub.getKey();
          final String classNameSub = sub.getValue().get(i);
          // Perform the class name substitution
          newName = newName.replaceAll(classNamePattern, classNameSub);
        }

        // Build the output
        list.add(newName);
      }
    } else {
      // A single output file
      list.add(name);
    }
    return list;
  }
}
