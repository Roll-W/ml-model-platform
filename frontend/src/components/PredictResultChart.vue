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
import {getCurrentInstance, onBeforeUnmount, onMounted, ref, watch} from "vue";

const echart = ref(null);
const props = defineProps({
    percentages: {
        type: Array,
        default: () => []
    },
    classes: {
        type: Array,
        default: () => []
    }
})
const {proxy} = getCurrentInstance()
let myChart = null

watch(() => props.percentages, async () => {
    resetEchartOption()
})

const resizeListener = () => {
    if (myChart !== null) {
        myChart.resize()
    }
}

const resetEchartOption = () => {
    if (myChart !== null) {
        myChart.dispose()
        window.removeEventListener('resize', resizeListener)
    }
    let color = ['#00325a', '#0077b6',
        '#005ae2', '#0022ff',
        '#7300ff', '#314ba2']
    myChart = proxy.$echarts.init(echart.value)
    let data = props.percentages.map((value, index) => {
        return ({
            name: props.classes[index],
            value: value,
        })
    })
    let option = {
        title: {
            text: 'Predict Result',
            color: 'black',
        },
        tooltip: {},
        legend: {
            data: props.classes,
            top: 'bottom',
        },
        series: [
            {
                type: "pie",
                color: color,
                data: data,
                radius: "75%",
            }
        ]
    }
    myChart.setOption(option)
    window.addEventListener('resize', resizeListener)
}

onMounted(() => {
    resetEchartOption()
})

onBeforeUnmount(() => {
    window.onresize = null
})
</script>
