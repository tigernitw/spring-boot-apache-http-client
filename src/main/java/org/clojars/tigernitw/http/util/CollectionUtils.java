/*
 * Copyright (c) 2023 Shiva Samadhiya <shiva94.nitw@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.clojars.tigernitw.http.util;

import java.util.Collection;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CollectionUtils {

  public static boolean isNullOrEmpty(final Collection<?> c) {
    return c == null || c.isEmpty();
  }

  public static boolean isNullOrEmpty(byte[] bytes) {
    return bytes == null || bytes.length == 0;
  }

  public static String convertByteArrayToString(byte[] bytes) {
    return isNullOrEmpty(bytes) ? null : new String(bytes);
  }
}
