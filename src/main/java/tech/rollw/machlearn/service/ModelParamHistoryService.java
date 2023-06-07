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

import tech.rollw.machlearn.data.database.MachSystemDatabase;
import tech.rollw.machlearn.data.database.ModelParamHistoryDao;
import tech.rollw.machlearn.data.entity.ModelParamHistory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author RollW
 */
@Service
public class ModelParamHistoryService {
    private final ModelParamHistoryDao modelParamHistoryDao;

    public ModelParamHistoryService(MachSystemDatabase database) {
        modelParamHistoryDao = database.getModelParamHistoryDao();
    }

    public void saveEvaluateModelParamHistory(String[] params,
                                              String modelName) {
        ModelParamHistory history = new ModelParamHistory(null, modelName, -1, params,
                System.currentTimeMillis(), ModelParamHistory.Type.EVALUATE);
        modelParamHistoryDao.insert(history);
        removeRedundantHistory(modelName, ModelParamHistory.Type.EVALUATE);
    }

    public void savePredictModelParamHistory(String[] params,
                                             String modelName) {
        ModelParamHistory history = new ModelParamHistory(null, modelName, -1, params,
                System.currentTimeMillis(), ModelParamHistory.Type.PREDICT);
        modelParamHistoryDao.insert(history);
        removeRedundantHistory(modelName, ModelParamHistory.Type.PREDICT);
    }

    // TODO: needs user id also, but for now it's not necessary
    public List<ModelParamHistory> getEvaluateModelParamHistory(String modelName) {
        return modelParamHistoryDao.getByNameAndType(modelName,
                ModelParamHistory.Type.EVALUATE);
    }

    public List<ModelParamHistory> getPredictModelParamHistory(String modelName) {
        return modelParamHistoryDao.getByNameAndType(modelName,
                ModelParamHistory.Type.PREDICT);
    }

    public void deleteHistory(long id) {
        modelParamHistoryDao.deleteById(id);
    }

    @Async
    void removeRedundantHistory(String modelName, ModelParamHistory.Type type) {
        modelParamHistoryDao.getByNameAndType(modelName, type)
                .stream()
                .skip(10)
                .forEach(modelParamHistoryDao::delete);
    }

    public void clearHistory(String name, ModelParamHistory.Type type) {
        modelParamHistoryDao.deleteByNameAndType(name, type);
    }
}
