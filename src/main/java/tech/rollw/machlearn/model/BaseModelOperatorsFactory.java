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

/**
 * @author RollW
 */
public class BaseModelOperatorsFactory implements ModelOperatorsFactory {
    @Override
    public ModelEvaluator buildEvaluator(Model model) {
        if (model == null) {
            throw new NullPointerException("Model cannot be null.");
        }
        if (BuiltinModel.isBuiltinModel(model)) {
            return BuiltinModelFactory.getInstance().buildEvaluator(model);
        }
        return CustomModelOperatorsFactory.getInstance().buildEvaluator(model);
    }

    @Override
    public ModelPredictor buildPredictor(Model model) {
        if (model == null) {
            throw new NullPointerException("Model cannot be null.");
        }
        if (BuiltinModel.isBuiltinModel(model)) {
            return BuiltinModelFactory.getInstance().buildPredictor(model);
        }
        return CustomModelOperatorsFactory.getInstance().buildPredictor(model);
    }

    public static BaseModelOperatorsFactory getInstance() {
      return SingletonHolder.INSTANCE;
    }

    private static final class SingletonHolder {
        static final BaseModelOperatorsFactory INSTANCE = new BaseModelOperatorsFactory();
    }
}
