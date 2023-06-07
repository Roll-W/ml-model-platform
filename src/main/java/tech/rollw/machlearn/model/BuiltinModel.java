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

package tech.rollw.machlearn.model;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;

/**
 * @author RollW
 */
public enum BuiltinModel implements Model {
    IRIS("iris_classifier", 1, true, 4,
            Map.of(
                    0, "Setosa",
                    1, "Versicolor",
                    2, "Virginica"),
            Map.of(
                    0, "sepal.length",
                    1, "sepal.width",
                    2, "petal.length",
                    3, "petal.width",
                    4, "variety")),
    MOON("moon_classifier", 2, false, 0,
            Map.of(0, "No", 1, "Yes"),
            Map.of(0, "moon",
                    1, "data_1",
                    2, "data_2"));

    private final String name;
    private final long id;
    private final boolean supportsDataSetDivide;
    private final int classIndex;
    private final Map<Integer, String> classMapping;
    private final Map<Integer, String> labelMapping;

    BuiltinModel(String name, long id,
                 boolean supportsDataSetDivide,
                 int classIndex,
                 Map<Integer, String> classMapping,
                 Map<Integer, String> labelMapping) {
        this.name = name;
        this.id = id;
        this.supportsDataSetDivide = supportsDataSetDivide;
        this.classIndex = classIndex;
        this.classMapping = classMapping;
        this.labelMapping = labelMapping;
    }

    public static boolean isBuiltinModel(Model model) {
        return model instanceof BuiltinModel;
    }

    @Override
    @NonNull
    public String getName() {
        return name;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public boolean supportsDataSetDivide() {
        return supportsDataSetDivide;
    }

    @Override
    public int classIndex() {
        return classIndex;
    }

    @Override
    @NonNull
    public Map<Integer, String> getClassMapping() {
        return classMapping;
    }

    @Override
    @NonNull
    public Map<Integer, String> getLabelMapping() {
        return labelMapping;
    }

    public static BuiltinModel findByName(String name) {
        for (BuiltinModel model : values()) {
            if (model.getName().equals(name)) {
                return model;
            }
        }
        return null;
    }
}