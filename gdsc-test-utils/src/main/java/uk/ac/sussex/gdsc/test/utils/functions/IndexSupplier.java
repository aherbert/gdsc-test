/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
 *
 * <p>Contains utilities for use with test frameworks.
 * %%
 * Copyright (C) 2018 Alex Herbert
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * <p>This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * <p>You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

package uk.ac.sussex.gdsc.test.utils.functions;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Supply a message for an n-dimensional index.
 *
 * <p>The default message will output the current index value {@code [x]} for each of the {@code n}
 * dimensions:
 *
 * <pre>
 * messagePrefix + "[1][2]...[n-1][n]" + messageSuffix
 * </pre>
 *
 * <p>The message prefix, suffix and index value delimiters can be configured.
 */
public class IndexSupplier implements Supplier<String> {

  /** The indices. */
  private final int[] indices;

  /** The message prefix. */
  private String messagePrefix;

  /** The message suffix. */
  private String messageSuffix;

  /** The prefix for each index. */
  private String prefix = "[";

  /** The suffix for each index. */
  private String suffix = "]";

  /** The delimiter used when joining each index. */
  private String delimiter = suffix + prefix;

  /**
   * Constructs a new instance of this class.
   *
   * @param dimensions the dimensions
   * @throws IllegalArgumentException If the dimensions are not strictly positive
   */
  public IndexSupplier(int dimensions) throws IllegalArgumentException {
    if (dimensions < 1) {
      throw new IllegalArgumentException(dimensions + " < 1");
    }
    this.indices = new int[dimensions];
  }

  /**
   * Constructs a new instance of this class.
   *
   * @param dimensions the dimensions
   * @param messagePrefix the message prefix
   * @param messageSuffix the message suffix
   * @throws IllegalArgumentException If the dimensions are not strictly positive
   */
  public IndexSupplier(int dimensions, String messagePrefix, String messageSuffix)
      throws IllegalArgumentException {
    this(dimensions);
    setMessagePrefix(messagePrefix);
    setMessageSuffix(messageSuffix);
  }

  /**
   * Gets a message consisting of: <ul> <li>The message prefix <li>The formatted indices for each of
   * the {@code n} dimensions <li>The message suffix </ul> E.g.
   *
   * <pre>
   * messagePrefix + "[1][2]...[n-1][n]" + messageSuffix
   * </pre>
   *
   * <p>The message prefix, suffix and index delimiters can be configured.
   *
   * @see java.util.function.Supplier#get()
   */
  @Override
  public String get() {
    String message = Arrays.stream(indices).boxed().map(Object::toString)
        .collect(Collectors.joining(delimiter, prefix, suffix));
    if (messagePrefix != null && messagePrefix.length() > 0) {
      message = messagePrefix + message;
    }
    if (messageSuffix != null && messageSuffix.length() > 0) {
      message += messageSuffix;
    }
    return message;
  }

  /**
   * Gets the index value.
   *
   * @param index the index
   * @return the index value
   */
  public int get(int index) {
    return indices[index];
  }

  /**
   * Sets the index value.
   *
   * @param index the index
   * @param value the index value
   * @return the supplier
   */
  public Supplier<String> set(int index, int value) {
    indices[index] = value;
    return this;
  }

  /**
   * Sets the format to use around each index.
   *
   * @param prefix the prefix for each index value
   * @param suffix the suffix for each index value
   */
  public void setFormat(String prefix, String suffix) {
    this.prefix = prefix;
    this.suffix = suffix;
    this.delimiter = suffix + prefix;
  }

  /**
   * Gets the message prefix.
   *
   * @return the message prefix
   */
  public String getMessagePrefix() {
    return messagePrefix;
  }

  /**
   * Sets the message prefix.
   *
   * @param messagePrefix the new message prefix
   */
  public void setMessagePrefix(String messagePrefix) {
    this.messagePrefix = messagePrefix;
  }

  /**
   * Gets the message suffix.
   *
   * @return the message suffix
   */
  public String getMessageSuffix() {
    return messageSuffix;
  }

  /**
   * Sets the message suffix.
   *
   * @param messageSuffix the new message suffix
   */
  public void setMessageSuffix(String messageSuffix) {
    this.messageSuffix = messageSuffix;
  }

  /**
   * Gets the prefix for each index value.
   *
   * @return the prefix
   */
  public String getPrefix() {
    return prefix;
  }

  /**
   * Gets the suffix for each index value.
   *
   * @return the suffix
   */
  public String getSuffix() {
    return suffix;
  }

  /**
   * Gets the delimiter used when joining each index value.
   *
   * <p>This is composed of {@link #getSuffix()} + {@link #getPrefix()}.
   *
   * @return the delimiter
   */
  public String getDelimiter() {
    return delimiter;
  }

  /**
   * Gets the dimensions.
   *
   * @return the dimensions
   */
  public int getDimensions() {
    return indices.length;
  }
}
