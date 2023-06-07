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

import org.nd4j.evaluation.classification.ConfusionMatrix;

import java.util.HashMap;
import java.util.Map;

/**
 * @author RollW
 */
public class ModelHelper {
    public static Map<Integer, Map<Integer, Integer>> convertToMatrix(ConfusionMatrix<Integer> matrix) {
        Map<Integer,  Map<Integer, Integer>> map = new HashMap<>();
        matrix.getMatrix().forEach((i, set) -> {
            Map<Integer, Integer> integers = map.computeIfAbsent(i, k -> new HashMap<>());
            set.forEach(j -> {
                if (integers.containsKey(j)) {
                    integers.put(j, integers.get(j) + 1);
                } else {
                    integers.put(j, 1);
                }
            });
        });
        return map;
    }

    public static int[][] convertToMatrixInt(ConfusionMatrix<Integer> matrix) {
        int[][] ints = new int[matrix.getMatrix().size()][matrix.getMatrix().size()];
        matrix.getMatrix().forEach((i, set) ->
                set.forEach(j -> ints[i][j] += 1));
        return ints;
    }


    private ModelHelper() {
    }
}
