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

import java.util.ArrayList;
import java.util.List;

/**
 * @author RollW
 */
public abstract class ModelFinderChain implements ModelFinder {
    private final ModelFinderChain next;

    public ModelFinderChain(ModelFinderChain next) {
        this.next = next;
    }

    public ModelFinderChain() {
        this(null);
    }

    @Override
    public Model findModel(String name, long id) {
        Model model = findModelInternal(name, id);
        if (model != null) {
            return model;
        }
        if (next != null) {
            model = next.findModel(name, id);
        }
        return model;
    }

    protected abstract Model findModelInternal(String name, long id);

    protected abstract List<Model> getModelsInternal(long id);

    @Override
    public List<? extends Model> getModels(long id) {
        List<Model> models = new ArrayList<>(getModelsInternal(id));
        ModelFinderChain next = this.next;
        while (next != null) {
            models.addAll(next.getModelsInternal(id));
            next = next.next;
        }
        return models;
    }

    public static ModelFinderChain start(ModelFinderChain next) {
        return BuiltinModelFactory.getFinderChain(next);
    }
}
