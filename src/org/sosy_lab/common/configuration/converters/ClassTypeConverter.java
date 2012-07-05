/*
 *  SoSy-Lab Common is a library of useful utilities.
 *  This file is part of SoSy-Lab Common.
 *
 *  Copyright (C) 2007-2012  Dirk Beyer
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
 */
package org.sosy_lab.common.configuration.converters;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import org.sosy_lab.common.Classes;
import org.sosy_lab.common.configuration.ClassOption;
import org.sosy_lab.common.configuration.InvalidConfigurationException;


public class ClassTypeConverter implements TypeConverter {

  @Override
  public Object convert(String optionName, String value, Class<?> type, Type genericType,
      Annotation secondaryOption) throws InvalidConfigurationException {

    // get optional package prefix
    String packagePrefix = "";
    if (secondaryOption != null) {
      if (!(secondaryOption instanceof ClassOption)) {
        throw new UnsupportedOperationException("Options of type Class may not be annotated with " + secondaryOption);
      }
      packagePrefix = ((ClassOption) secondaryOption).packagePrefix();
    }

    // get value of type parameter
    Class<?> targetType = Classes.getComponentType(genericType).getFirst();

    // get class object
    Class<?> cls;
    try {
      cls = Classes.forName(value, packagePrefix);
    } catch (ClassNotFoundException e) {
      throw new InvalidConfigurationException("Class " + value + " specified in option " + optionName + " not found");
    }

    // check type
    if (!targetType.isAssignableFrom(cls)) {
      throw new InvalidConfigurationException("Class " + value + " specified in option " + optionName + " is not an instance of " + targetType.getCanonicalName());
    }

    return cls;
  }

  @Override
  public <T> T convertDefaultValue(String pOptionName, T pValue, Class<T> pType, Type pGenericType,
      Annotation pSecondaryOption) {

    return pValue;
  }

}