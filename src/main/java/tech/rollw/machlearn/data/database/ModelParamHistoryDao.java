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

package tech.rollw.machlearn.data.database;

import tech.rollw.machlearn.data.entity.ModelParamHistory;
import space.lingu.light.Dao;
import space.lingu.light.Delete;
import space.lingu.light.Insert;
import space.lingu.light.OnConflictStrategy;
import space.lingu.light.Query;

import java.util.List;

/**
 * @author RollW
 */
@Dao
public abstract class ModelParamHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(ModelParamHistory... modelParamHistories);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(List<ModelParamHistory> modelParamHistories);

    @Delete
    public abstract void delete(ModelParamHistory modelParamHistory);

    @Delete("DELETE FROM model_param_history WHERE id = {id}")
    public abstract void deleteById(long id);

    @Delete("DELETE FROM model_param_history")
    public abstract void deleteAll();

    @Delete("DELETE FROM model_param_history WHERE model_name = {name} AND `type` = {type}")
    public abstract void deleteByNameAndType(String name, ModelParamHistory.Type type);

    @Query("SELECT * FROM model_param_history ORDER BY `timestamp` DESC ")
    public abstract List<ModelParamHistory> getAll();

    @Query("SELECT * FROM model_param_history WHERE model_name = {name} ORDER BY `timestamp` DESC ")
    public abstract List<ModelParamHistory> getByName(String name);

    @Query("SELECT * FROM model_param_history WHERE model_name = {name} AND `type` = {type} ORDER BY `timestamp` DESC ")
    public abstract List<ModelParamHistory> getByNameAndType(String name, ModelParamHistory.Type type);

    @Query("SELECT * FROM model_param_history WHERE model_user_id = {id} AND model_name = {name} AND `type` = {type} ORDER BY timestamp DESC ")
    public abstract List<ModelParamHistory> getByUserIdAndNameAndType(long id, String name,
                                                                      ModelParamHistory.Type type);


}
