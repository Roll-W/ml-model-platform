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

import {createRouter, createWebHistory} from 'vue-router'
import HomeView from '@/views/HomeView.vue'
import NotFound from "@/views/NotFound.vue";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'home',
            redirect: '/models',
            component: HomeView,
            meta: {
                title: 'Home | Machine Learning Model Platform',
            },
            children: [
                {
                    path: '/models',
                    name: 'models_list',
                    component: () => import('@/views/ModelsList.vue'),
                    meta: {
                        title: '模型列表 | Machine Learning Model Platform',
                    }
                },
                {
                    path: '/models/:id/:name',
                    name: 'model_info',
                    component: () => import('@/views/ModelView.vue'),
                    meta: {
                        title: '模型详情 | Machine Learning Model Platform',
                    }
                },
                {
                    path: '/404',
                    name: '404',
                    component: NotFound,// 404,
                    meta: {
                        title: "404 | Machine Learning Model Platform"
                    }
                },
                {
                    path: '/:catchAll(.*)',
                    redirect: '/404'
                },
            ]
        },

    ]
})

const defaultTitle = "Machine Learning Model Platform";

router.beforeEach((to, from, next) => {
    document.title = to.meta.title ? to.meta.title : defaultTitle
    next()
})

export default router
