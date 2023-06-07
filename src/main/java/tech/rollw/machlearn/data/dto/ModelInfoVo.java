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

package tech.rollw.machlearn.data.dto;

import tech.rollw.machlearn.model.Model;

import java.util.Map;

/**
 * @author RollW
 */
public record ModelInfoVo(
        long id,
        String name,
        boolean supportsDataSetDivide,
        int classIndex,
        Map<Integer, String> classMapping,
        Map<Integer, String> labelMapping,
        String[] classes,
        String[] labels
) {

    public static ModelInfoVo fromModel(Model model) {
        if (model == null) {
            return null;
        }

        return new ModelInfoVo(
                model.getId(),
                model.getName(),
                model.supportsDataSetDivide(),
                model.classIndex(),
                model.getClassMapping(),
                model.getLabelMapping(),
                convert(model.getClassMapping()),
                convert(model.getLabelMapping())
        );
    }

    private static String[] convert(Map<Integer, String> map) {
        String[] result = new String[map.size()];
        map.forEach((k, v) -> result[k] = v);
        return result;
    }
}
