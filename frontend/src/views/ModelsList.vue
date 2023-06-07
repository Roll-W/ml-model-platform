<!--
  - Copyright (C) 2022 RollW
  -
  - Licensed under the Apache License, Version 2.0 (the "License");
  - you may not use this file except in compliance with the License.
  - You may obtain a copy of the License at
  -
  -     http://www.apache.org/licenses/LICENSE-2.0
  -
  - Unless required by applicable law or agreed to in writing, software
  - distributed under the License is distributed on an "AS IS" BASIS,
  - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  - See the License for the specific language governing permissions and
  - limitations under the License.
  -->

<template>
    <CardComponent>
        <div class="mb-4 ms-2 d-flex flex-grow-1 flex-fill">
            <h2>模型列表</h2>
        </div>
        <hr>
        <table class="table table-hover text-center">
            <thead class="table-light">
            <tr>
                <th scope="col">模型名称</th>
                <th scope="col">模型从属ID</th>
                <th scope="col">模型操作</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="model in models" :key="model.name + model.id">
                <th scope="row">{{ model.name }}</th>
                <th scope="row">{{ model.id }}</th>
                <th scope="row">
                    <div class="justify-content-center">
                        <button class="btn btn-link fs-5 text-decoration-none" type="button"
                                @click="onClick(model)">
                            查看
                        </button>
                    </div>
                </th>
            </tr>
            </tbody>
        </table>
    </CardComponent>
</template>

<script setup>
import CardComponent from "@/components/CardCompenent.vue";
import axios from "axios";
import api from '@/api/url'
import {ref} from "vue";
import router from "@/router";

const models = ref([]);
axios.get(api.getModels(0)).then(res => {
    if (res.data.errorCode !== "00000") {
        return
    }
    models.value = res.data.data;
}).catch(err => {
    console.error(err)
});
const onClick = (model) => {
    router.push({
        name: "model_info",
        params: {
            id: model.id,
            name: model.name
        }
    })
}

</script>
