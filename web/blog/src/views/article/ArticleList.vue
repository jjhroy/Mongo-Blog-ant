<template>
  <!-- 二次元封面 -->
  <header-cover>
    <div class="article-info">
      <h1 class="article-title">{{ state.title }}-{{ state.name }}</h1>
      <h3 style="color: white;">-共计{{ state.count }}篇</h3>
    </div>
  </header-cover>
  <div class="article-list-wrapper">
    <a-row :gutter="[0, 16]">
      <a-col :span="8" v-for="item in state.articleList" :key="item.id">
        <a-card class="article-list-card" hoverable>
          <template #cover>
            <router-link :to="`/articles/${item.id}`">
              <img alt="cover" :src="item.articleCover" class="article-cover" />
            </router-link>
          </template>
          <a-card-meta>
            <template #title>
              <div class="title">
                <router-link :to="`/articles/${item.id}`">
                  {{ item.articleTitle }}
                </router-link>
              </div>
            </template>
            <template #description>
              <div>
                <div class="time-category">
                  <div>
                    <clock-circle-filled />
                    {{ item.createTime }}
                  </div>
                  <div>
                    <profile-filled />
                    <router-link :to="`/category/${item.categoryId}`">
                      &nbsp;{{ item.categoryName }}
                    </router-link>
                  </div>
                </div>
                <a-divider style="margin: 10px 0;"></a-divider>
                <div class="tags">
                  <router-link :to="'/tag/' + tag.id" v-for="tag of item.tagDTOList" :key="tag.id" class="tag-btn">
                    <tag-filled /> {{ tag.tagName }}
                  </router-link>
                </div>
              </div>
            </template>
          </a-card-meta>
        </a-card>
      </a-col>
    </a-row>

    <div>
      <a-pagination size="small" :total="state.count" class="pagination" :pageSize="10" hideOnSinglePage />
    </div>
  </div>
</template>

<script setup lang="ts">
import HeaderCover from "@/components/component/HeaderCover.vue";
import { reactive, onMounted } from "vue";
import {
  TagFilled,
  ClockCircleFilled,
  ProfileFilled
} from "@ant-design/icons-vue";
import { getArticleListBy } from "@/api/article"
import { useRoute } from "vue-router";
import { articlePreviewList } from "@/types/api/article"
const route = useRoute();
onMounted(() => {
  getData();
  const path = route.fullPath;
  if (path.indexOf("/categories") != -1) {
    state.title = "分类";
  } else {
    state.title = "标签";
  }
});
const state = reactive({
  current: 1,
  size: 10,
  articleList: [] as articlePreviewList[],
  name: "",
  title: "",
  count: 0
});
const getData = async () => {
  let params = {
    categoryId: route.params.categoryId,
    tagId: route.params.tagId,
    current: state.current,
  }
  const data = await getArticleListBy(params)
  state.name = data.name;
  state.count = data.articlePreviewDTOList.length;
  state.current++;
  state.articleList.push(...data.articlePreviewDTOList);
};
</script>

<style scoped lang="less">
.article-list-wrapper {
  margin: 20px 100px;

  .pagination {
    text-align: center;
    margin: 20px 0;
  }
}

.article-list-card {
  margin: auto;
  width: 360px;
  height: 340px;
  border-radius: 8px;
  box-shadow: 0 4px 8px 6px rgba(7, 17, 27, 0.06);



  a {
    color: #666;
    text-decoration: none
  }

  .article-cover {
    width: 100%;
    height: 200px;
  }

  .time-category {
    display: flex;
    justify-content: space-between;
  }

  .tags {
    a {
      color: #fff !important;
    }

    .tag-btn {
      display: inline-block;
      font-size: 0.725rem;
      line-height: 22px;
      height: 22px;
      border-radius: 10px;
      padding: 0 12px !important;
      background: linear-gradient(to right, #bf4643 0%, #6c9d8f 100%);
      opacity: 0.6;
      margin-right: 0.5rem;
    }
  }
}
</style>
