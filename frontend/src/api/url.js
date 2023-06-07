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

const server = "http://localhost:4500";
const getModels = (id) => server + `/api/models/${id}`;
const getModelBy = (id, name) => server + `/api/models/${id}/${name}`;
const evaluateModel = (id, name) => server + `/api/models/${id}/${name}/evaluate`;
const predictModel = (id, name) => server + `/api/models/${id}/${name}/predict`;
const predictModelHistory = (id, name) => server + `/api/models/${id}/${name}/histories/predict`;
const evaluateModelHistory = (id, name) => server + `/api/models/${id}/${name}/histories/evaluate`;
const deleteHistory = (id) => server + `/api/models/histories/${id}`;
const clearHistory = (id, name, type) => server + `/api/models/${id}/${name}/histories/${type}`;

export default {
    getModels,
    getModelBy,
    evaluateModel,
    predictModel,
    predictModelHistory,
    evaluateModelHistory,
    deleteHistory,
    clearHistory
}
