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

import org.stringtemplate.v4.ST;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Generate java source files from a template using the StringTemplate library.
 */
public final class StringTemplateHelper {

  /**
   * No public construction.
   */
  private StringTemplateHelper() {}

  /**
   * Generate the templates.
   *
   * @param model the model
   * @return the list of templates
   */
  public static List<Pair<String, String>> generate(StringTemplateModel model) {

    final List<Pair<String, String>> list = new ArrayList<>();

    final String name = model.getTemplateClassName();
    if (model.isRenamedClass()) {
      final List<Pair<String, List<String>>> classNameSubstitutions = model.getClassNameScope();
      final List<Pair<String, List<Object>>> classScopeSubstitutions = model.getClassScope();
      // All the same size
      final int size = classNameSubstitutions.get(0).second.size();
      for (int i = 0; i < size; i++) {
        // Clone
        // ST st2 = new ST(st);
        final ST st = createStringTemplate(model);

        String newName = name;
        // Each classname scope key will have an entry for this class.
        for (final Pair<String, List<String>> sub : classNameSubstitutions) {
          final String classNamePattern = sub.first;
          final String classNameSub = sub.second.get(i);
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
          st.add(sub.first, sub.second.get(i));
        }

        // Build the output
        list.add(new Pair<>(newName, st.render()));
      }
    } else {
      // A single output file
      list.add(new Pair<>(name, createStringTemplate(model).render()));
    }

    return list;
  }

  private static ST createStringTemplate(StringTemplateModel model) {
    final ST st = new ST(model.getTemplate());

    // Do all template scope substitutions
    for (final Pair<String, List<Object>> sub : model.getTemplateScope()) {
      st.add(sub.first, sub.second);
    }
    // Inject the package
    st.add("package", getPackageName(model.getPackageName()));
    return st;
  }

  private static Object getPackageName(String packageName) {
    return StringUtils.isNotEmpty(packageName) ? "package " + packageName + ";" : "";
  }

  /**
   * List the template names that will be created using {@link #generate(StringTemplateModel)}.
   *
   * @param model the model
   * @return the list
   */
  public static List<String> listNames(StringTemplateModel model) {
    final List<String> list = new ArrayList<>();
    final String name = model.getTemplateClassName();
    if (model.isRenamedClass()) {
      final List<Pair<String, List<String>>> classNameSubstitutions = model.getClassNameScope();
      // All the same size
      final int size = classNameSubstitutions.get(0).second.size();
      for (int i = 0; i < size; i++) {

        String newName = name;
        // Each classname scope key will have an entry for this class.
        for (final Pair<String, List<String>> sub : classNameSubstitutions) {
          final String classNamePattern = sub.first;
          final String classNameSub = sub.second.get(i);
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
