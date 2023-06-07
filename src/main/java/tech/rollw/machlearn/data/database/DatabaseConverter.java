/*
 * Copyright (C) 2022 RollW
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

package tech.rollw.machlearn.data.database;

import space.lingu.light.DataConverter;

import java.util.StringJoiner;

/**
 * @author RollW
 */
public final class DatabaseConverter {
    @DataConverter
    public static String[] convert(String s) {
        return s.split(",");
    }

    @DataConverter
    public static String convert(String[] s) {
        StringJoiner joiner = new StringJoiner(",");
        for (String value : s) {
            joiner.add(value);
        }
        return joiner.toString();
    }

    private DatabaseConverter() {
    }
}
