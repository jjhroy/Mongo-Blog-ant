<template>
  <div class="archive">
    <!-- 二次元封面 -->
    <header-cover>
      <div class="article-info">
        <h1 class="article-title">归档</h1>
      </div>
    </header-cover>
    <div class="container">
      <a-card class="archive-card">
        <a-timeline>
          <a-timeline-item style="color: var(--theme-color);">
            共发布了{{ state.count }}篇文章了
          </a-timeline-item>

          <a-timeline-item v-for="item in state.archiveList" :key="item.id">
            <!-- 日期 -->
            <span class="time">{{ $formatDate(item.createTime) }}</span>
            <!-- 文章标题 -->
            <router-link :to="`/articles/${item.id}`">
              {{ item.articleTitle }}
            </router-link>
          </a-timeline-item>
        </a-timeline>
        <div class="pagination">
          <a-pagination v-model:current="state.current" :total="state.count" :pageSize="10" @change="pageChange" />
        </div>
      </a-card>
    </div>
  </div>
</template>
<script setup lang="ts">
import { onMounted, reactive } from "vue";
import HeaderCover from "@/components/component/HeaderCover.vue";
import { getArchiveData } from "@/api/blog";
import { archive } from "@/types/api/blog"
const state = reactive({
  archiveList: [] as archive[],
  count: 0,
  current: 1,
});
onMounted(() => {
  getArchive();
});
const pageChange = (page: number, pageSize: number) => {
  state.current = page;
  console.log(state.current)
  getArchive();
};
const getArchive = async () => {
  const data = await getArchiveData(state.current)
  state.archiveList = data.recordList;
  state.count = data.count;
};
</script>
<style scoped lang="less">
.archive {
  width: 100%;
  height: 100%;
  background-color: var(--theme-background);
  color: var(--theme-color);

  .pagination {
    text-align: center;
  }

  .time {
    font-size: 0.75rem;
    color: var(--theme-color);
    margin-right: 1rem;
  }

  .container {
    padding: 5% 10%;
  }

  .archive-card {
    background: var(--theme-card-color);
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.05);
    border: 1px solid var(--theme-card-color);
  }

  a {
    color: var(--theme-color);
    text-decoration: none;
    font-size: 1.2rem;
  }

  a:hover {
    font-size: 1.2rem;
    color: #03a9f4 !important;
    transform: scale(1.1);
  }
}
</style>
