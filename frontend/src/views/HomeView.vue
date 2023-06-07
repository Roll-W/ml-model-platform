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
  <main>
    <NavBar/>
    <div class="d-flex flex-column p-4 flex-grow-1 allow-rolling bg-light">
      <router-view></router-view>
      <Footer/>
    </div>
  </main>
</template>
<script>
import NavBar from "@/components/NavBar.vue";
import Footer from "@/components/Footer.vue";

export default {
  name: "HomeView",
  components:{
    Footer,
    NavBar,
  }
}
</script>

<style scoped>
body {
  min-height: 100vh;
}

th,
td {
  text-align: center;
}

main {
  display: flex;
  flex-wrap: nowrap;
  width: auto;
  height: 100vh;
  max-height: 100vh;
  overflow-x: auto;
  overflow-y: hidden
}

.allow-rolling {
  overflow: auto;
}
</style>

