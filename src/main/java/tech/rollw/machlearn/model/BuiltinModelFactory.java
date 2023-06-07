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

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * @author RollW
 */
public class BuiltinModelFactory implements ModelOperatorsFactory {
    private static final List<Model> BUILTIN_MODELS =
            List.of(BuiltinModel.values());

    private final Map<BuiltinModel, ModelEvaluator> modelCache
            = new EnumMap<>(BuiltinModel.class);

    @Override
    public ModelEvaluator buildEvaluator(Model model) throws ModelException {
        if (model == null) {
            throw new NullPointerException();
        }

        if (!(model instanceof BuiltinModel)) {
            throw new ModelException("Not a builtin model.");
        }
        return getOrCreate((BuiltinModel) model);
    }

    @Override
    public ModelPredictor buildPredictor(Model model) {
        return (ModelPredictor) buildEvaluator(model);
    }

    private ModelEvaluator getOrCreate(BuiltinModel model) {
        if (!modelCache.containsKey(model)) {
            ModelEvaluator evaluator = createModel(model);
            modelCache.put(model, evaluator);
            return evaluator;
        }
        return modelCache.get(model);
    }

    private ModelEvaluator createModel(BuiltinModel model) {
        switch (model) {
            case IRIS -> {
                return new IrisModelOperator();
            }
            case MOON -> {
                return new MoonModelOperator();
            }
        }
        throw new ModelException("Not fount builtin model of " + model);
    }

    public static BuiltinModelFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public static ModelFinderChain getFinderChain(ModelFinderChain next) {
        return new ModelChain(next);
    }

    private static final class SingletonHolder {
        static final BuiltinModelFactory INSTANCE = new BuiltinModelFactory();
    }

    private static class ModelChain extends ModelFinderChain {
        public ModelChain(ModelFinderChain next) {
            super(next);
        }

        public ModelChain() {
            super();
        }

        @Override
        protected Model findModelInternal(String name, long id) {
            return BuiltinModel.findByName(name);
        }

        @Override
        protected List<Model> getModelsInternal(long id) {
            return BUILTIN_MODELS;
        }
    }
}
