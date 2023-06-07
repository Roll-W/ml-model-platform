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

import tech.rollw.machlearn.data.entity.UserDefinedModel;

/**
 * @author RollW
 */
public class CustomModelOperatorsFactory implements ModelOperatorsFactory {
    @Override
    public ModelEvaluator buildEvaluator(Model model) {
        if (model instanceof UserDefinedModel userDefinedModel) {
            return buildUserModelEvaluator(userDefinedModel);
        }
        return null;
    }

    @Override
    public ModelPredictor buildPredictor(Model model) {
        if (model instanceof UserDefinedModel userDefinedModel) {
            return buildUserModelPredictor(userDefinedModel);
        }
        return null;
    }

    private ModelEvaluator buildUserModelEvaluator(UserDefinedModel model) {
        return null;
    }

    private ModelPredictor buildUserModelPredictor(UserDefinedModel model) {
        return null;
    }

    public static CustomModelOperatorsFactory getInstance() {
      return SingletonHolder.INSTANCE;
    }

    private static final class SingletonHolder {
        static final CustomModelOperatorsFactory INSTANCE = new CustomModelOperatorsFactory();
    }
}
