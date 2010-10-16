/*
 *  CPAchecker is a tool for configurable software verification.
 *  This file is part of CPAchecker.
 *
 *  Copyright (C) 2007-2010  Dirk Beyer
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
package org.sosy_lab.common;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Helper class for various methods related to handling Java classes and types.
 */
public final class Classes {

  private Classes() { }

  /**
   * Exception thrown by {@link Classes#createInstance(String, String, Class[], Object[], Class)}.
   */
  public static class ClassInstantiationException extends Exception {

    private static final long serialVersionUID = 7862065219560550275L;
    
    public ClassInstantiationException(String className, String msg) {
      super("Cannot instantiate class " + className + ":" + msg);
    }
  }
  
  /**
   * Creates an instance of class className, passing the objects from argumentList
   * to the constructor and casting the object to class type.
   *
   * @param className The class name.
   * @param prefix An optional package name that is prefixed to the className if the class is not found.
   * @param argumentTypes Array with the types of the parameters of the desired constructor.
   * @param argumentValues Array with the values that will be passed to the constructor.
   * @param type The return type (has to be a super type of the class, of course).
   * @throws ClassInstantiationException If something goes wrong (like class cannot be found or has no constructor).
   * @throws InvocationTargetException If the constructor throws an exception.  
   */
  public static <T> T createInstance(String className, String prefix,
      Class<?>[] argumentTypes, Object[] argumentValues, Class<T> type)
      throws ClassInstantiationException, InvocationTargetException {
    try {
      Class<?> cls = forName(className, prefix);
      Constructor<?> ct = cls.getConstructor(argumentTypes);
      Object obj = ct.newInstance(argumentValues);
      return type.cast(obj);

    } catch (ClassNotFoundException e) {
      throw new ClassInstantiationException(className, "Class not found!");
    } catch (SecurityException e) {
      throw new ClassInstantiationException(className, e.getMessage());
    } catch (NoSuchMethodException e) {
      throw new ClassInstantiationException(className, "Matching constructor not found!");
    } catch (InstantiationException e) {
      throw new ClassInstantiationException(className, e.getMessage());
    } catch (IllegalAccessException e) {
      throw new ClassInstantiationException(className, e.getMessage());
    } catch (ClassCastException e) {
      throw new ClassInstantiationException(className, "Not an instance of " + type.getCanonicalName());
    }
  }
  
  /**
   * Similar to {@link Class#forName(String)}, but if the class is not found this
   * method re-tries with a package name prefixed.
   * 
   * @param name The class name.
   * @param prefix An optional package name as prefix.
   * @return The class object for  name  or  prefix + "." + name  
   * @throws ClassNotFoundException If none of the two classes can be found.
   */
  public static Class<?> forName(String name, String prefix) throws ClassNotFoundException {
    if (prefix == null || prefix.isEmpty()) {
      return Class.forName(name);
    }
    
    try {
      return Class.forName(name);
      
    } catch (ClassNotFoundException e1) {
      try {
        return Class.forName(prefix + "." + name); // try with prefix added
      } catch (ClassNotFoundException e2) {
        throw e1; // re-throw original exception to get correct error message
      }
    }
  }
}