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
    <div ref="echart"/>
</template>

<script setup>
import {getCurrentInstance, ref, onBeforeUnmount, watch, onMounted} from "vue";

const {proxy} = getCurrentInstance()
const echart = ref(null)

const props = defineProps({
    predict: {
        type: Array,
        default: () => []
    },
    actual: {
        type: Array,
        default: () => []
    },
    classes: {
        type: Array,
        default: () => []
    },
})

let myChart = null

watch(() => props.actual, async () => {
    resetEchartOption()
})

const resizeListener = () => {
    if (myChart !== null) {
        myChart.resize()
    }
}

const resetEchartOption = () => {
    if (myChart !== null) {
        window.removeEventListener('resize', resizeListener)
        myChart.dispose()
    }
    myChart = proxy.$echarts.init(echart.value)
    let option = {
        title: {
            text: '预测值对照',
            color: 'black',
            left: 'left',
        },
        tooltip: {},
        legend: {
            data: ['预测数量', '真实数量'],
            top: 'bottom',
        },
        yAxis: {
            data: props.classes
        },
        xAxis: {},
        series: [
            {
                name: '预测数量',
                type: 'bar',
                color: '#00325a',
                data: props.predict
            },
            {
                name: '真实数量',
                type: 'bar',
                color: '#396fd3',
                data: props.actual
            }
        ],
    };

    myChart.setOption(option);

    window.addEventListener('resize', resizeListener)
}
onMounted(() => {
    resetEchartOption()
})

onBeforeUnmount(() => {
    window.onresize = null
})
</script>
