/*-
 * #%L
 * Genome Damage and Stability Centre Test Utilities
 *
 * Contains utilities for use with test frameworks.
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
  public IndexSupplier(int dimensions) {
    if (dimensions <= 0) {
      throw new IllegalArgumentException(dimensions + " <= 0");
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
  public IndexSupplier(int dimensions, String messagePrefix, String messageSuffix) {
    this(dimensions);
    this.messagePrefix = messagePrefix;
    this.messageSuffix = messageSuffix;
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
    final StringBuilder message = new StringBuilder();
    if (messagePrefix != null && messagePrefix.length() > 0) {
      message.append(messagePrefix);
    }
    message.append(Arrays.stream(indices).mapToObj(Integer::toString)
        .collect(Collectors.joining(delimiter, prefix, suffix)));
    if (messageSuffix != null && messageSuffix.length() > 0) {
      message.append(messageSuffix);
    }
    return message.toString();
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
  public IndexSupplier set(int index, int value) {
    indices[index] = value;
    return this;
  }

  /**
   * Sets the format to use around each index.
   *
   * @param prefix the prefix for each index value
   * @param suffix the suffix for each index value
   * @return the supplier
   */
  public IndexSupplier setFormat(String prefix, String suffix) {
    this.prefix = prefix;
    this.suffix = suffix;
    this.delimiter = suffix + prefix;
    return this;
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
   * @return the supplier
   */
  public IndexSupplier setMessagePrefix(String messagePrefix) {
    this.messagePrefix = messagePrefix;
    return this;
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
   * @return the supplier
   */
  public IndexSupplier setMessageSuffix(String messageSuffix) {
    this.messageSuffix = messageSuffix;
    return this;
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
