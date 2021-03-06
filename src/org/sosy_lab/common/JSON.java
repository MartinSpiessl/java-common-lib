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
package org.sosy_lab.common;

import com.google.common.base.Ascii;
import com.google.common.base.Strings;
import com.google.errorprone.annotations.Var;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.sosy_lab.common.io.IO;

/**
 * This class is based on code from the library JSON.simple in version 1.1
 * (https://code.google.com/p/json-simple/) by Fang Yidong {@code fangyidong@yahoo.com.cn}. The
 * license is the Apache 2.0 License (http://www.apache.org/licenses/LICENSE-2.0.txt).
 *
 * <p>Significant performance improvements were made compared to the library.
 */
public final class JSON {

  private JSON() {}

  /** Encode an object into JSON text and write it to a file. */
  public static void writeJSONString(@Nullable Object value, Path file) throws IOException {
    // We escape everything, so pure ASCII remains
    try (Writer out = IO.openOutputFile(file, StandardCharsets.US_ASCII)) {
      writeJSONString(value, out);
    }
  }

  /** Encode an object into JSON text and write it to out. */
  public static void writeJSONString(@Nullable Object value, Appendable out) throws IOException {
    if (value == null) {
      out.append("null");

    } else if (value instanceof CharSequence) {
      out.append('\"');
      escape((CharSequence) value, out);
      out.append('\"');

    } else if (value instanceof Double) {
      if (((Double) value).isInfinite() || ((Double) value).isNaN()) {
        out.append("null");
      } else {
        out.append(value.toString());
      }

    } else if (value instanceof Float) {
      if (((Float) value).isInfinite() || ((Float) value).isNaN()) {
        out.append("null");
      } else {
        out.append(value.toString());
      }

    } else if (value instanceof Number) {
      out.append(value.toString());

    } else if (value instanceof Boolean) {
      out.append(value.toString());

    } else if (value instanceof Map<?, ?>) {
      writeJSONString((Map<?, ?>) value, out);

    } else if (value instanceof Iterable<?>) {
      writeJSONString((Iterable<?>) value, out);

    } else {
      throw new NotSerializableException(
          "Object of class " + value.getClass().getName() + " cannot be written as JSON");
    }
  }

  /** Encode an list into JSON text and write it to out. */
  private static void writeJSONString(Iterable<?> list, Appendable out) throws IOException {
    @Var boolean first = true;

    out.append('[');
    for (Object value : list) {
      if (first) {
        first = false;
      } else {
        out.append(',');
      }

      JSON.writeJSONString(value, out);
    }
    out.append(']');
  }

  /** Encode a map into JSON text and write it to out. */
  private static void writeJSONString(Map<?, ?> map, Appendable out) throws IOException {
    @Var boolean first = true;

    out.append('{');
    for (Map.Entry<?, ?> entry : map.entrySet()) {
      if (first) {
        first = false;
      } else {
        out.append(',');
      }
      out.append('\"');
      escape(String.valueOf(entry.getKey()), out);
      out.append('\"');
      out.append(':');
      writeJSONString(entry.getValue(), out);
    }
    out.append('}');
  }

  /**
   * Escape quotes, \, /, \r, \n, \b, \f, \t and other control characters (U+0000 through U+001F).
   */
  private static void escape(CharSequence s, Appendable out) throws IOException {
    for (int i = 0; i < s.length(); i++) {
      char ch = s.charAt(i);
      switch (ch) {
        case '"':
          out.append("\\\"");
          break;
        case '\\':
          out.append("\\\\");
          break;
        case '\b':
          out.append("\\b");
          break;
        case '\f':
          out.append("\\f");
          break;
        case '\n':
          out.append("\\n");
          break;
        case '\r':
          out.append("\\r");
          break;
        case '\t':
          out.append("\\t");
          break;
        case '/':
          out.append("\\/");
          break;
        default:
          // Reference: http://www.unicode.org/versions/Unicode5.1.0/
          if ((ch >= '\u0000' && ch <= '\u001F')
              || (ch >= '\u007F' && ch <= '\u009F')
              || (ch >= '\u2000' && ch <= '\u20FF')) {
            String ss = Ascii.toUpperCase(Integer.toHexString(ch));
            out.append("\\u");
            out.append(Strings.padStart(ss, 4, '0'));
          } else {
            out.append(ch);
          }
      }
    }
  }
}
