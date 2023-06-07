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

package tech.rollw.machlearn.service;

import tech.rollw.machlearn.data.dto.ModelEvaluationResult;
import tech.rollw.machlearn.model.D4jModelData;
import tech.rollw.machlearn.model.Model;
import tech.rollw.machlearn.model.ModelData;
import tech.rollw.machlearn.model.ModelEvaluator;
import tech.rollw.machlearn.model.ModelException;
import tech.rollw.machlearn.model.ModelFinderChain;
import tech.rollw.machlearn.model.ModelOperatorsFactory;
import tech.rollw.machlearn.model.ModelPredictor;
import tech.rollw.machlearn.model.SingleDataSetModelEvaluator;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author RollW
 */
@Service
public class ModelService {
    private static final Logger logger = LoggerFactory.getLogger(ModelService.class);

    private final ModelParamHistoryService historyService;
    private final ModelFinderChain modelFinderChain;

    public ModelService(ModelParamHistoryService historyService) {
        this.historyService = historyService;
        modelFinderChain = ModelFinderChain.start(null);
    }

    public ModelEvaluationResult evaluateModel(Model model, int trainMultiple) {
        ModelEvaluator evaluator =
                ModelOperatorsFactory.getDefaultFactory().buildEvaluator(model);
        if (evaluator == null) {
            throw new ModelException("Cannot find evaluator for model: " + model);
        }
        historyService.saveEvaluateModelParamHistory(
                new String[]{String.valueOf(trainMultiple)},
                model.getName());
        return evaluator.evalModel(trainMultiple);
    }

    public ModelEvaluationResult evaluateModel(Model model,
                                               int trainMultiple,
                                               double fractionTrain) {
        ModelEvaluator evaluator =
                ModelOperatorsFactory.getDefaultFactory().buildEvaluator(model);
        if (evaluator == null) {
            throw new ModelException("Cannot find evaluator for model: " + model);
        }
        if (evaluator instanceof SingleDataSetModelEvaluator eva) {
            historyService.saveEvaluateModelParamHistory(
                    new String[]{String.valueOf(trainMultiple), String.valueOf(fractionTrain)},
                    model.getName());
            return eva.evalModel(trainMultiple, fractionTrain);
        }
        historyService.saveEvaluateModelParamHistory(
                new String[]{String.valueOf(trainMultiple)},
                model.getName());
        return evaluator.evalModel(trainMultiple);
    }

    public ModelData predictModel(Model model,
                                  double[] data) {
        ModelPredictor predictor =
                ModelOperatorsFactory.getDefaultFactory().buildPredictor(model);
        if (predictor == null) {
            throw new ModelException("Cannot find predictor for model: " + model);
        }
        historyService.savePredictModelParamHistory(
                convert(data),
                model.getName());
        INDArray array = Nd4j.create(new double[][]{data});
        ModelData modelData = new D4jModelData(array);
        return predictor.predict(modelData);
    }


    private static String[] convert(double[] data) {
        String[] result = new String[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = String.valueOf(data[i]);
        }
        return result;
    }

    public Model findModel(String name, long id) {
        return modelFinderChain.findModel(name, id);
    }

    public List<? extends Model> getModels(long id) {
        return modelFinderChain.getModels(id);
    }
}
