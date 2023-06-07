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

package tech.rollw.machlearn.data.entity;

import space.lingu.light.DataColumn;
import space.lingu.light.DataTable;
import space.lingu.light.PrimaryKey;

/**
 * 参数历史记录
 *
 * @author RollW
 */
@DataTable(tableName = "model_param_history")
public class ModelParamHistory {
    @PrimaryKey(autoGenerate = true)
    @DataColumn(name = "id")
    private final Long id;

    @DataColumn(name = "model_name")
    private final String modelName;

    @DataColumn(name = "model_user_id")
    private final long modelUserId;

    @DataColumn(name = "params")
    private final String[] params;

    @DataColumn(name = "timestamp")
    private final long timestamp;

    @DataColumn(name = "type")
    private final Type type;

    public ModelParamHistory(Long id, String modelName,
                             long modelUserId, String[] params,
                             long timestamp, Type type) {
        this.id = id;
        this.modelName = modelName;
        this.modelUserId = modelUserId;
        this.params = params;
        this.timestamp = timestamp;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public String getModelName() {
        return modelName;
    }

    public long getModelUserId() {
        return modelUserId;
    }

    public String[] getParams() {
        return params;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        EVALUATE,
        PREDICT;

        public static Type from(String s) {
           for (Type type : values()) {
               if (type.name().equalsIgnoreCase(s)) {
                   return type;
               }
           }
           throw new IllegalArgumentException("No enum constant " + Type.class.getName() + "." + s);
        }
    }
}
