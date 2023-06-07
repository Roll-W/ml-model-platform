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

package tech.rollw.machlearn.controller;

import org.springframework.web.bind.annotation.*;
import tech.rollw.machlearn.common.ErrorCode;
import tech.rollw.machlearn.data.dto.HttpResponseEntity;
import tech.rollw.machlearn.data.dto.ModelEvaluationRequest;
import tech.rollw.machlearn.data.dto.ModelEvaluationResult;
import tech.rollw.machlearn.data.dto.ModelInfoVo;
import tech.rollw.machlearn.data.dto.ModelPredictRequest;
import tech.rollw.machlearn.data.dto.ModelPredictResult;
import tech.rollw.machlearn.model.Model;
import tech.rollw.machlearn.service.ModelService;

import java.util.List;

/**
 * @author RollW
 */
@RestController
@ModelApi
public class ModelController {
    private final ModelService modelService;

    public ModelController(ModelService modelService) {
        this.modelService = modelService;
    }


    @GetMapping("/{id}")
    public HttpResponseEntity<List<ModelInfoVo>> getModels(@PathVariable("id") long id) {
        return HttpResponseEntity.success(
                modelService.getModels(id)
                        .stream().map(ModelInfoVo::fromModel)
                        .toList()
        );
    }

    @GetMapping("/{id}/{name}")
    public HttpResponseEntity<ModelInfoVo> getModelInfo(@PathVariable("id") long id,
                                                        @PathVariable("name") String name) {
        return HttpResponseEntity.success(
                ModelInfoVo.fromModel(modelService.findModel(name, id))
        );
    }

    @PostMapping("/{id}/{name}/evaluate")
    public HttpResponseEntity<ModelEvaluationResult> evaluateModel(
            @PathVariable("id") long id,
            @PathVariable("name") String name,
            @RequestBody ModelEvaluationRequest request) {
        Model model = modelService.findModel(name, id);
        if (model == null) {
            return HttpResponseEntity.failure("Cannot find model: " + name,
                    ErrorCode.ERROR_DATA_NOT_EXIST);
        }
        return HttpResponseEntity.success(
                modelService.evaluateModel(model, request.getTrainMultiple(),
                        request.getFractionTrain())
        );
    }

    @PostMapping("/{id}/{name}/predict")
    public HttpResponseEntity<ModelPredictResult> predictModel(
            @PathVariable("id") long id,
            @PathVariable("name") String name,
            @RequestBody ModelPredictRequest request) {
        Model model = modelService.findModel(name, id);
        if (model == null) {
            return HttpResponseEntity.failure("Cannot find model: " + name,
                    ErrorCode.ERROR_DATA_NOT_EXIST);
        }
        return HttpResponseEntity.success(
                ModelPredictResult.from(
                        modelService.predictModel(model, request.data())
                )
        );
    }
}
