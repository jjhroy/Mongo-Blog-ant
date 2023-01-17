<template>
  <div class="tag">
    <!-- 二次元封面 -->
    <header-cover>
      <div class="article-info">
        <h1 class="article-title">标签</h1>
      </div>
    </header-cover>
    <!-- 标签列表 -->
    <div class="container">
      <a-card class="tag-card">
        <div class="tag-cloud-title">标签 - {{ state.count }}</div>
        <div class="tag-cloud">
          <router-link :style="{ 'font-size': Math.floor(Math.random() * 10) + 18 + 'px' }"
            v-for="item of state.tagList" :key="item.id" :to="`/tag/${item.id}`">
            {{ item.tagName }}
          </router-link>
        </div>
      </a-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import 'animate.css'
import {tag} from "@/types/api/blog"
import { getTagsData } from "@/api/blog";
import { onMounted, reactive } from "vue";
import HeaderCover from "@/components/component/HeaderCover.vue";
const state = reactive({
  tagList: [] as tag[],
  count: 0
})
const getTagList = async () => {
  const data = await getTagsData()
  state.tagList.push(...data.recordList);
  state.count = data.count;
}
onMounted(() => {
  getTagList()
});

</script>

<style scoped lang="less">
.tag {
  width: 100%;
  height: 100%;
  background-color: var(--theme-background);
  .container {
    padding: 5% 10%;
  }

  .tag-card {
    background: var(--theme-card-color);
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.05);
    border: 1px solid var(--theme-card-color);
  }


  .tag-cloud-title {
    line-height: 2;
    font-size: 36px;
    text-align: center;
    color: var(--theme-color);
  }


  @media (max-width: 759px) {
    .tag-cloud-title {
      font-size: 25px;
    }
  }

  .tag-cloud {
    text-align: center;
  }

  .tag-cloud a {
    color: var(--theme-color);
    display: inline-block;
    text-decoration: none;
    padding: 0 8px;
    line-height: 2;
    transition: all 0.3s;
  }

  .tag-cloud a:hover {
    color: #03a9f4 !important;
    transform: scale(1.1);
  }
}
</style>
