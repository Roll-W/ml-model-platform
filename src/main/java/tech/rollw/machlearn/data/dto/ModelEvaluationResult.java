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

/**
 * @author RollW
 */
public class ModelEvaluationResult {
    private final double accuracy;
    private final double topNAccuracy;
    private final double precision;
    private final double recall;
    private final double f1;
    private final int[][] confusionMatrix;

    public ModelEvaluationResult(double accuracy, double topNAccuracy,
                                 double precision, double recall, double f1,
                                 int[][] confusionMatrix) {
        this.accuracy = accuracy;
        this.topNAccuracy = topNAccuracy;
        this.precision = precision;
        this.recall = recall;
        this.f1 = f1;
        this.confusionMatrix = confusionMatrix;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public double getTopNAccuracy() {
        return topNAccuracy;
    }

    public double getPrecision() {
        return precision;
    }

    public double getRecall() {
        return recall;
    }

    public double getF1() {
        return f1;
    }

    public int[][] getConfusionMatrix() {
        return confusionMatrix;
    }

}
