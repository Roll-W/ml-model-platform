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
        <div class="mb-4 d-flex flex-grow-1 flex-fill">
            <h2>{{ modelName }}</h2>
            <div class="d-flex flex-fill justify-content-end">
                <button class="btn btn-outline-info ms-2 fs-5" type="button" @click="back">返回模型列表</button>
            </div>
        </div>
        <hr class="p-1">
        <h3>模型信息</h3>
        <div class="d-flex flex-row justify-content-evenly align-items-stretch align-self-stretch">
            <div class="card m-2 align-self-stretch flex-fill">
                <div class="card-body">
                    <h4>模型数据集标签</h4>
                    <table class="table table-hover text-center">
                        <thead class="table-light">
                        <tr>
                            <th scope="col">数据列</th>
                            <th scope="col">标签</th>
                        </tr>
                        </thead>

                        <tbody>
                        <tr v-for="(val, key) in modelLabels" :key="key">
                            <th scope="row">{{ key }}</th>
                            <th scope="row">{{ val }}</th>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="card m-2 flex-fill">
                <div class="card-body">
                    <h4>模型类标签</h4>
                    <table class="table table-hover text-center">
                        <thead class="table-light">
                        <tr>
                            <th scope="col">类别序号</th>
                            <th scope="col">标签</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr v-for="(val, key) in modelClasses" :key="key">
                            <th scope="row">{{ key }}</th>
                            <th scope="row">{{ val }}</th>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <hr class="p-1">
        <h3>模型评估</h3>
        <div class="alert alert-primary">
            <p>训练集分割参数（训练集使用的比例）在部分模型上不可用。</p>
            <p>训练集分割参数值<code>p</code>需要满足<code>0 &lt;= p &lt;= 1</code>。</p>
        </div>
        <div class="d-flex flex-row justify-content-evenly align-items-stretch align-self-stretch">
            <div class="d-flex flex-column w-40 h-100">
                <Transition>
                    <div v-if="evaErr" class="alert alert-danger">
                        <div class="alert-heading fs-5">模型评估时发生错误：</div>
                        {{ evaErr }}
                    </div>
                </Transition>
                <div class="pb-3 form-floating">
                    <input id="trainMultipleInput" v-model="trainMultiple" class="form-control" min="0"
                           placeholder="请输入训练集倍数"
                           type="number">
                    <label class="form-label" for="trainMultipleInput">模型训练Epoch（>0）</label>
                </div>
                <div class="pb-3 form-floating">
                    <input id="trainSplitInput" v-model="trainSplit" :disabled="!allowSplit" class="form-control"
                           max="1" min="0"
                           placeholder="请输入训练分割率"
                           step="0.01" type="number">
                    <label class="form-label" for="trainSplitInput">训练集分割率</label>
                </div>
                <div class="pb-3 form-floating">
                    <input id="learningRateInput" class="form-control" placeholder="请输入学习率" type="number">
                    <label class="form-label" for="learningRateInput">学习率</label>
                </div>
                <div class="pb-3 form-floating">
                    <input id="l2Input" class="form-control" placeholder="请输入l2" type="number">
                    <label class="form-label" for="l2Input">l2</label>
                </div>
                <div class="pb-3 form-floating">
                    <input id="seedInput" class="form-control" placeholder="请输入seed" type="number">
                    <label class="form-label" for="seedInput">Seed</label>
                </div>
                <div class="btn-group align-items-end">
                    <button class="btn btn-outline-secondary" type="button" @click="clickEvalReset">
                        重置输入
                    </button>
                    <button class="btn btn-outline-info" type="submit" @click="clickEvaluate">
                        确认
                    </button>
                </div>
                <hr>
                <div class="pt-3 pb-3">
                    <HistoryTable ref="evaluateHistoryTable" :callback="fillInEvaluate"
                                  :id="Number.parseInt(modelId)"
                                  :name="modelName" title="评估参数历史记录" type="evaluate"/>
                </div>
            </div>
            <div class="vr m-2"></div>
            <div class="d-flex flex-column flex-grow-1 justify-content-evenly align-items-stretch align-self-stretch h-100">
                <div class="card">
                    <div class="card-body">
                        <h4>混淆矩阵</h4>
                        <div class="alert alert-primary">
                            类别请参照模型信息中的类标签。
                        </div>
                        <Transition>
                            <div :hidden="!evaluateWaiting" class="alert alert-success">
                                <div
                                        class="d-flex flex-fill flex-grow-1 w-100 flex-row justify-content-start align-items-stretch align-self-stretch">
                                    <div class="spinner-border"></div>
                                    <div class="m-1 ms-3">等待数据更新</div>
                                </div>
                            </div>
                        </Transition>
                        <table v-if="modelResultMatrix !== null" class="table text-center rounded rounded-circle">
                            <thead class="table-dark ">
                            <tr>
                                <th class="w-25">类别索引</th>
                                <th v-for="(val, index) in modelResultMatrix" :key="val" scope="col">
                                    {{ index }}
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr v-for="(val, index) in modelResultMatrix">
                                <th class="table-dark">{{ index }}</th>
                                <th v-for="(val2, index2) in val" :key="val"
                                    :class="index2 === index ? 'table-info' : ''">
                                    {{ val2 }}
                                </th>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="card mt-2 flex-fill">
                    <div class="card-body">
                        <h4>相关结果</h4>
                        <Transition>
                            <div :hidden="!evaluateWaiting" class="alert alert-success">
                                <div class="d-flex flex-fill flex-grow-1 w-100 flex-row
                                    justify-content-start align-items-stretch align-self-stretch">
                                    <div class="spinner-border"></div>
                                    <div class="m-1 ms-3">等待数据更新</div>
                                </div>
                            </div>
                        </Transition>
                        <div v-if="modelResult !== null" class="mb-2">
                            <div class="row">
                                <div class="col">Accuracy:</div>
                                <div class="col">{{ modelResult.accuracy.toFixed(3) }}</div>
                            </div>
                            <div class="row">
                                <div class="col">TopN Accuracy:</div>
                                <div class="col">{{ modelResult.topNAccuracy.toFixed(3) }}</div>
                            </div>
                            <div class="row">
                                <div class="col">Precision</div>
                                <div class="col">{{ modelResult.precision.toFixed(3) }}</div>
                            </div>
                            <div class="row">
                                <div class="col">Recall:</div>
                                <div class="col">{{ modelResult.recall.toFixed(3) }}</div>
                            </div>
                            <div class="row">
                                <div class="col">F1:</div>
                                <div class="col">{{ modelResult.f1.toFixed(3) }}</div>
                            </div>
                        </div>
                        <div>
                            <EvaluatePredictResultChart v-if="matrixActualArray.length !== 0"
                                                        :actual="matrixActualArray"
                                                        :classes="modelClassesArray"
                                                        :predict="matrixPredictArray" class="mt-2 w-100 vh-50"/>
                            <div v-else class="fs-5">暂无数据信息</div>
                        </div>
                    </div>
                </div>
            </div>

        </div>
        <hr class="p-1">
        <h3>模型预测</h3>
        <div class="alert alert-primary">
            预测使用的模型为上方评估后的模型。若未评估将按照模型内部默认参数进行训练后预测。
        </div>
        <div class="d-flex flex-row justify-content-evenly align-items-stretch align-self-stretch">

            <div class="d-flex flex-column w-40 h-100">
                <div class="alert alert-warning ">
                    数据项不填默认为0；数据项填写错误默认为0。
                </div>
                <Transition>
                    <div v-if="predictErr" class="alert alert-danger">
                        <div class="alert-heading fs-5">模型预测时发生错误：</div>
                        {{ predictErr }}
                    </div>
                </Transition>
                <div v-for="label in predictLabels" :key="label.index" class="mb-2">
                    <div>
                        <label class="form-label" for="predictInput">{{ label.index }}: {{ label.label }}</label>
                        <input id="predictInput" v-model="label.input"
                               :placeholder="'请输入数据列 ' + label.index + ' 数据'"
                               class="form-control" type="number">
                    </div>
                </div>
                <div class="btn-group align-items-end">
                    <button class="btn btn-outline-secondary" type="button" @click="clickPredictReset">
                        重置输入
                    </button>
                    <button class="btn btn-outline-info" type="submit" @click="clickPredict">
                        确认
                    </button>
                </div>
                <hr>
                <div class="pt-3 pb-3">
                    <HistoryTable
                            :id="Number.parseInt(modelId)" ref="predictHistoryTable"
                            :callback="fillInPredict" :name="modelName"
                            title="预测参数历史记录" type="predict"/>
                </div>
            </div>
            <div class="vr m-2"></div>
            <div class="d-flex flex-column flex-grow-1 justify-content-evenly align-items-stretch align-self-stretch">
                <div class="card align-self-stretch h-100">
                    <div class="card-body">
                        <h4>预测结果</h4>
                        <Transition>
                            <div :hidden="!predictWaiting" class="alert alert-success">
                                <div
                                        class="d-flex flex-fill flex-grow-1 w-100 flex-row justify-content-start align-items-stretch align-self-stretch">
                                    <div class="spinner-border"></div>
                                    <div class="m-1 ms-3">等待数据更新</div>
                                </div>
                            </div>
                        </Transition>
                        <Transition>
                            <div v-if="predictResult !== null">
                                <h4>类别为：{{
                                        predictResult.predictClass
                                    }}，标签：{{ mappingBack(predictResult.predictClass) }}</h4>
                                <hr>
                                <h4>类别预测</h4>
                                <div v-for="(percentage, index) in predictResult.probabilities" :key="percentage">
                                    <div class="row">
                                        <div class="col">{{ index }}</div>
                                        <div class="col">{{ (percentage * 100).toFixed(5) }}%</div>
                                    </div>
                                </div>

                            </div>
                        </Transition>
                        <div>
                            <PredictResultChart v-if="convertPredictPercentage.length !== 0"
                                                :classes="modelClassesArray"
                                                :percentages="convertPredictPercentage"
                                                class="mt-2 w-100 vh-50"/>
                            <div v-else class="fs-5">暂无数据信息</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </CardComponent>
</template>

<script setup>
import {useRoute} from "vue-router";
import {ref} from "vue";
import axios from "axios";
import api from '@/api/url'
import CardComponent from "@/components/CardCompenent.vue";
import router from "@/router";
import EvaluatePredictResultChart from "@/components/EvaluatePredictResultChart.vue";
import PredictResultChart from "@/components/PredictResultChart.vue";
import HistoryTable from "@/components/HistoryTable.vue";

const route = useRoute();
const modelId = route.params.id
const modelName = route.params.name

const evaluateHistoryTable = ref(null)
const predictHistoryTable = ref(null)

// 参数
const trainMultiple = ref(null)
const trainSplit = ref(null)

// 结果
const modelLabels = ref({})
const modelClasses = ref({})
const allowSplit = ref(null)

// 评估
const modelResultMatrix = ref(null)
const modelResult = ref(null)
const modelClassesArray = ref([])
const matrixActualArray = ref([])
const matrixPredictArray = ref([])

// 预测
const predictResult = ref(null)
const predictLabels = ref(null)
const classIndex = ref(null)
const convertPredictPercentage = ref([])

// 错误
const evaluateWaiting = ref(false)
const evaErr = ref(null)
const predictWaiting = ref(false)
const predictErr = ref(false)


const fillInEvaluate = (his) => {
    let params = his.params
    trainMultiple.value = params[0]
    trainSplit.value = params[1]
}

const fillInPredict = (his) => {
    let params = his.params
    predictLabels.value.forEach((label, index) => {
        label.input = params[index]
    })
}

const getModeInfo = () => {
    axios.get(api.getModelBy(modelId, modelName)).then(res => {
        if (res.data.errorCode !== "00000") {
            return
        }
        modelLabels.value = res.data.data.labelMapping
        modelClasses.value = res.data.data.classMapping
        allowSplit.value = res.data.data.supportsDataSetDivide
        classIndex.value = res.data.data.classIndex
        modelClassesArray.value = Object.keys(modelClasses.value)

        predictLabels.value = res.data.data.labels.map((val, index) => {
            if (index === classIndex.value) {
                return null
            }
            return ({
                index: index,
                label: val,
                input: ref(null)
            })
        })
        predictLabels.value = predictLabels.value.filter(val => val !== null)
        console.log(res.data);
    }).catch(err => {
        console.error(err)
    });
}

getModeInfo()

const evaluateModel = (trainMultiple, fractionTrain) => {
    axios.post(api.evaluateModel(modelId, modelName), {
        trainMultiple: trainMultiple,
        fractionTrain: fractionTrain
    }).then(res => {
        evaluateWaiting.value = false
        if (res.data.errorCode !== "00000") {
            evaErr.value = res.data.message
            return
        }
        evaErr.value = null
        let matrix = res.data.data.confusionMatrix

        let tmpActual = []
        let tmpPredict = []

        for (let row of matrix) {
            tmpActual.push(row.reduce((a, b) => a + b))
            for (let i = 0; i < row.length; i++) {
                if (tmpPredict[i] === undefined) {
                    tmpPredict[i] = 0
                }
                tmpPredict[i] += row[i]
            }
        }
        evaluateHistoryTable.value.loadTable()
        matrixActualArray.value = tmpActual
        matrixPredictArray.value = tmpPredict
        modelResultMatrix.value = matrix
        modelResult.value = res.data.data
        console.log(res.data)
    }).catch(err => {
        console.error(err)
        evaluateWaiting.value = false
        evaErr.value = err.response.data.message
    });
}

const predictModelPost = (predictData) => {
    axios.post(api.predictModel(modelId, modelName), {
        data: predictData
    }).then(res => {
        predictWaiting.value = true
        convertPredictPercentage.value = []
        if (res.data.errorCode !== "00000") {
            predictErr.value = res.data.message
            return
        }
        predictResult.value = res.data.data
        convertPredictPercentage.value = predictResult.value.probabilities.map((val) => {
            return (val * 100).toFixed(3)
        })
        predictWaiting.value = false
        predictHistoryTable.value.loadTable()
        console.log(res.data)
    }).catch(err => {
        convertPredictPercentage.value = []
        predictErr.value = err.response.data.message
        predictWaiting.value = false
    });
}

const clickEvaluate = () => {
    if (evaluateWaiting.value) {
        evaErr.value = "请等待上一次请求完成"
        return
    }

    evaluateWaiting.value = true
    evaluateModel(trainMultiple.value, trainSplit.value)
}

const clickEvalReset = () => {
    trainMultiple.value = null
    trainSplit.value = null
    evaErr.value = null
}

const clickPredict = () => {
    if (evaluateWaiting.value || predictWaiting.value) {
        predictErr.value = "请等待上一次请求完成"
        return
    }

    predictWaiting.value = true
    const predictData = predictLabels.value.map(val => {
        let input = val.input
        if (input === '' || input === null) {
            input = 0
        }
        return input
    })
    predictModelPost(predictData)
}

const clickPredictReset = () => {
    for (let valueElement of predictLabels.value) {
        if (valueElement === null || valueElement.input === null) {
            continue
        }
        valueElement.input = null
    }
    predictErr.value = null
}

const mappingBack = (index) => {
    return modelClasses.value[index]
}

const back = () => {
    router.push({name: "models_list"})
}
</script>