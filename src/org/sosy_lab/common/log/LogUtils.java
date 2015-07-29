/*
 *  SoSy-Lab Common is a library of useful utilities.
 *  This file is part of SoSy-Lab Common.
 *
 *  Copyright (C) 2007-2015  Dirk Beyer
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
package org.sosy_lab.common.log;

import java.util.logging.LogRecord;

class LogUtils {

  private LogUtils() {}

  /**
   * Get the simple name of the source class of a log record.
   */
  static String extractSimpleClassName(LogRecord lr) {
    String fullClassName = lr.getSourceClassName();
    int dotIndex = fullClassName.lastIndexOf('.');
    assert dotIndex < fullClassName.length() - 1 : "Last character in a class name cannot be a dot";

    // if no dot is contained, dotIndex is -1 so we get the substring from 0,
    // i.e., the whole string (which is what we want)

    String className = fullClassName.substring(dotIndex + 1);
    return className;
  }

}
