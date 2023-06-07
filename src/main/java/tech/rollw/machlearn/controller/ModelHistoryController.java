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
import tech.rollw.machlearn.data.dto.HttpResponseEntity;
import tech.rollw.machlearn.data.entity.ModelParamHistory;
import tech.rollw.machlearn.service.ModelParamHistoryService;

import java.util.List;

/**
 * @author RollW
 */
@RestController
@ModelApi
public class ModelHistoryController {
    private final ModelParamHistoryService historyService;

    public ModelHistoryController(ModelParamHistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/{id}/{name}/histories/evaluate")
    public HttpResponseEntity<List<ModelParamHistory>> getEvaluateHistory(
            @PathVariable Long id,
            @PathVariable String name) {
        return HttpResponseEntity.success(
                historyService.getEvaluateModelParamHistory(name)
        );
    }

    @GetMapping("/{id}/{name}/histories/predict")
    public HttpResponseEntity<List<ModelParamHistory>> getPredictHistories(
            @PathVariable Long id,
            @PathVariable String name) {
        return HttpResponseEntity.success(
                historyService.getPredictModelParamHistory(name)
        );
    }

    @DeleteMapping("/histories/{id}")
    public HttpResponseEntity<Void> deleteHistory(
            @PathVariable Long id) {
        historyService.deleteHistory(id);
        return HttpResponseEntity.success();
    }

    @DeleteMapping("/{id}/{name}/histories/{type}")
    public HttpResponseEntity<Void> clearHistory(
            @PathVariable Long id,
            @PathVariable String name,
            @PathVariable String type) {
        historyService.clearHistory(name, ModelParamHistory.Type.from(type));
        return HttpResponseEntity.success();
    }
}
