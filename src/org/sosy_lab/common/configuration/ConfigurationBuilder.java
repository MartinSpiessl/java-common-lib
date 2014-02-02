/*
 *  CPAchecker is a tool for configurable software verification.
 *  This file is part of CPAchecker.
 *
 *  Copyright (C) 2007-2014  Dirk Beyer
 *  All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 *  CPAchecker web page:
 *    http://cpachecker.sosy-lab.org
 */
package org.sosy_lab.common.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.sosy_lab.common.configuration.converters.TypeConverter;
import org.sosy_lab.common.io.Path;



public interface ConfigurationBuilder {

  /**
   * Set a single option.
   */
  public ConfigurationBuilder setOption(String name, String value);

  /**
   * Reset a single option to its default value.
   */
  public ConfigurationBuilder clearOption(String name);

  /**
   * Add all options from a map.
   */
  public ConfigurationBuilder setOptions(Map<String, String> options);

  /**
   * Set the optional prefix for new configuration.
   */
  public ConfigurationBuilder setPrefix(String prefix);

  /**
   * Copy everything from an existing Configuration instance. This also means
   * that the new configuration object created by this builder will share the
   * set of unused properties with the configuration instance passed to this
   * class.
   *
   * If this method is called, it has to be the first method call on this
   * builder instance.
   */
  public ConfigurationBuilder copyFrom(Configuration oldConfig);

  /**
   * Load options from an InputStream with a "key = value" format.
   *
   * The stream remains open after this method returns.
   *
   * @param stream The stream to read from.
   * @param basePath The directory where relative #include directives should be based on.
   * @param source A string to use as source of the file in error messages (this should usually be a filename or something similar).
   * @throws IOException If the stream cannot be read.
   * @throws InvalidConfigurationException If the stream contains an invalid format.
   */
  public ConfigurationBuilder loadFromStream(InputStream stream, String basePath, String source) throws IOException,
      InvalidConfigurationException;

  /**
   * Load options from an InputStream with a "key = value" format.
   *
   * The stream remains open after this method returns.
   *
   * @deprecated Use {@link #loadFromStream(InputStream, String, String)} instead.
   * @param stream The stream to read from.
   * @throws IOException If the stream cannot be read.
   * @throws InvalidConfigurationException If the stream contains an invalid format.
   */
  public ConfigurationBuilder loadFromStream(InputStream stream) throws IOException, InvalidConfigurationException;

  /**
   * Load options from a file with a "key = value" format.
   *
   * @throws IOException If the file cannot be read.
   * @throws InvalidConfigurationException If the file contains an invalid format.
   */
  public ConfigurationBuilder loadFromFile(String filename) throws IOException, InvalidConfigurationException;

  /**
   * Load options from a file with a "key = value" format.
   *
   * @throws IOException If the file cannot be read.
   * @throws InvalidConfigurationException If the file contains an invalid format.
   */
  public ConfigurationBuilder loadFromFile(Path file) throws IOException, InvalidConfigurationException;

  /**
   * Add a type converter for options with a certain type.
   * This will enable the Configuration instance to parse strings into values
   * of the given type and inject them just as the base option types.
   *
   * As an alternative, the type of an option detail annotation
   * ({@link OptionDetailAnnotation}) can be given. In this case, the type
   * converter will be called for options annotated with this type.
   *
   * Previous type converters for the same type will be overwritten
   * (this also works for types usually handled by the Configuration class,
   * however not for collection and array types).
   *
   * The same converter may be used for several types.
   *
   * @param cls The type the type converter handles.
   * @param converter A converter instance.
   * @return this
   */
  public ConfigurationBuilder addConverter(Class<?> cls, TypeConverter converter);

  /**
   * Create a Configuration instance with the settings specified by method
   * calls on this builder instance.
   *
   * This method resets the builder instance, so that after this method has
   * returned it is exactly in the same state as directly after instantiation.
   *
   * @throws InvalidConfigurationException if the settings contained invalid values for the configuration options of the Configuration class
   */
  public Configuration build() throws InvalidConfigurationException;

}