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
    <div class="d-flex ">
        <h4>{{ props.title }}</h4>
        <div class="d-flex flex-fill justify-content-end">
            <button :disabled="histories.length === 0" class="btn btn-outline-info" @click="clearHistory">清空历史
            </button>
        </div>
    </div>

    <div class="mt-3">

        <table v-if="histories.length !== 0" class="table table-hover text-center">
            <thead class="table-light">
            <tr>
                <th>时间</th>
                <th>参数值列表</th>
                <th class="w-20">操作</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="his in histories" :key="his">
                <th>{{ convertTimestamp(his.timestamp) }}</th>
                <th>{{ convertArray(his.params) }}</th>
                <th>
                    <button class="btn btn-link text-decoration-none" @click="callback(his)">填充</button>
                    <button class="btn btn-link text-decoration-none" @click="deleteHistory(his)">删除</button>
                </th>
            </tr>
            </tbody>
        </table>
        <div v-else class="fs-5">暂无历史数据</div>
    </div>

</template>

<script setup>
import api from '@/api/url'
import axios from "axios";
import {ref} from "vue";
import {convertTimestamp} from "@/util/time";

const props = defineProps({
    type: {
        type: String,
        required: true,
    },
    id: {
        type: Number,
        required: true,
    },
    name: {
        type: String,
        required: true,
    },
    title: {
        type: String,
        required: true,
    },
    callback: {
        type: Function,
        required: true,
    }
});

const histories = ref([]);

const getFromType = (type) => {
    if (type === 'evaluate') return api.evaluateModelHistory(props.id, props.name)
    else return api.predictModelHistory(props.id, props.name)
}

const loadTable = () => {
    axios.get(getFromType(props.type)).then((res) => {
        if (res.data.errorCode !== "00000") {
            return
        }
        console.log(res.data)
        histories.value = res.data.data
    }).catch((err) => {
        console.log(err)
    });
}

const clearHistory = () => {
    axios.delete(api.clearHistory(props.id, props.name, props.type)).then((res) => {
        if (res.data.errorCode !== "00000") {
            return
        }
        loadTable()
    }).catch((err) => {
        console.log(err)
    });
}

const deleteHistory = (history) => {
    axios.delete(api.deleteHistory(history.id)).then((res) => {
        if (res.data.errorCode !== "00000") {
            return
        }
        loadTable()
    }).catch((err) => {
        console.log(err)
    });
}

const convertArray = (array) => {
    let result = ''
    for (let i = 0; i < array.length; i++) {
        result += array[i] + ', '
    }
    return result.substring(0, result.length - 2)
}

defineExpose({
    loadTable
})
loadTable()
</script>