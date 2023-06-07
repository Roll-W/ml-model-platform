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

import tech.rollw.machlearn.model.ModelData;
import org.nd4j.linalg.api.ndarray.INDArray;

/**
 * @author RollW
 */
public class ModelPredictResult {
    private final int predictClass;
    private final double[] probabilities;

    public ModelPredictResult(int predictClass, double[] probabilities) {
        this.predictClass = predictClass;
        this.probabilities = probabilities;
    }

    public int getPredictClass() {
        return predictClass;
    }

    public double[] getProbabilities() {
        return probabilities;
    }

    public static ModelPredictResult from(ModelData modelData) {
        if (modelData == null) {
            return null;
        }
        INDArray indArray = modelData.toINDArray();
        double[][] matrix = indArray.toDoubleMatrix();
        if (matrix.length < 1) {
            throw new IllegalArgumentException("Matrix rank 0, expect rank >= 1.");
        }
        int maxIndex = 0;
        double[] res = matrix[0];
        for (int i = 1; i < res.length; i++) {
            if (res[i] > res[maxIndex]) {
                maxIndex = i;
            }
        }
        return new ModelPredictResult(maxIndex, indArray.toDoubleMatrix()[0]);
    }
}
