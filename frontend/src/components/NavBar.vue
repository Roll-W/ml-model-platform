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
    <button aria-controls="navSidebar" aria-expanded="false" class="navbar-toggler float-start bg-dark"
            data-bs-target="#navSidebar" data-bs-toggle="collapse" type="button">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div id="navSidebar" class="collapse collapse-horizontal show w-15">
        <nav class="d-flex flex-grow-0 flex-column flex-shrink-0 p-4 bg-dark border-light border-end border-1 h-100"
             data-bs-backdrop="false" data-bs-scroll="true" tabindex="-1">
            <h2>
                <router-link :to="{ name: 'home' }"
                             class="d-flex align-items-center mb-3 mb-md-0 me-md-auto link-light text-decoration-none">机器学习<br>模型系统
                </router-link>
            </h2>
            <hr>
            <ul class="nav nav-pills flex-column mb-auto">
                <li>
                    <router-link :class="['nav-link ', route_name === 'models_list' ? 'active' : 'link-light']"
                                 :to="{ name: 'models_list' }">模型列表
                    </router-link>
                </li>
            </ul>
            <hr>
            <div class="dropdown">
                <a id="dropdownUser2" aria-expanded="false"
                   class="d-flex align-items-center link-light text-decoration-none dropdown-toggle"
                   data-bs-toggle="dropdown" href="#">
                    <strong>User</strong>
                </a>
                <ul aria-labelledby="dropdownUser2" class="dropdown-menu text-small shadow">
                    <li><a class="dropdown-item disabled">设置</a></li>
                    <li><a class="dropdown-item">用户功能暂不提供</a></li>
                    <li>
                        <hr class="dropdown-divider">
                    </li>
                    <li><a class="dropdown-item disabled">退出</a></li>
                </ul>
            </div>
        </nav>
    </div>
</template>

<script setup>
import {useRoute} from "vue-router";
import {computed} from "vue";

const route = useRoute();
let route_name = computed(() => {
    return route.name
});
</script>

<style scoped>

</style>
