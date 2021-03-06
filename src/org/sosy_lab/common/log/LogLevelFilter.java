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

import com.google.common.collect.ImmutableSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/** {@link Filter} implementation for blacklisting log levels. */
class LogLevelFilter implements Filter {

  private final Set<Level> excludeLevels;

  LogLevelFilter(List<Level> excludeLevels) {
    this.excludeLevels = ImmutableSet.copyOf(excludeLevels);
  }

  @Override
  public boolean isLoggable(LogRecord pRecord) {
    return !excludeLevels.contains(pRecord.getLevel());
  }
}
