<template>
  <!-- 搜索框 -->
  <a-modal v-model:visible="pinia.showMessageModal" :footer="null" centered>
    <div style="margin-bottom: 10px">
      <span class="message-title">消息中心</span>
    </div>
    <!-- 信息列表 -->
    <div class="message-result-wrapper">
      <hr class="divider" />
      <p style="margin-bottom: 8px" v-if="pinia.loginUserInfo.newCommentsList.length === 0">
        暂时还没有收到新的回复！
      </p>
      <p style="margin-bottom: 8px" v-else>你有新的未读回复</p>
      <ul>
        <li class="message-reslut" v-for="item of pinia.loginUserInfo.newCommentsList" :key="item.id">
          <a-badge :count="item.newCommentsCount" :overflow-count="10">
            <!-- 文章标题 -->
            <a @click="toArticleDetail(item.id)">
              <span>{{ item.articleTitle }}</span>
            </a>
          </a-badge>

          <span @click="clearMessage(item.id)">已阅</span>
        </li>
      </ul>
    </div>
  </a-modal>
</template>

<script setup lang="ts">
import { deleteNewComments } from "@/api/user";
import config from "@/config/config";
import { createFromIconfontCN } from "@ant-design/icons-vue";
import { useRouter } from "vue-router";
import { useStore } from "@/store/index";
const pinia = useStore();
const IconFont = createFromIconfontCN({
  scriptUrl: config.ICON_FONT_URL,
});
const router = useRouter();
const toArticleDetail = (id:number) => {
  router.push(`/articles/${id}`);
  pinia.showMessageModal = false;
};
const clearMessage = async (id:number) => {
  await deleteNewComments(id);
  pinia.clearMessage(id);
};
</script>

<style scoped lang="less">
.message-title {
  color: #49b1f5;
  font-size: 1.25rem;
  line-height: 1;
}

@media (min-width: 960px) {
  .message-result-wrapper {
    padding-right: 5px;
    height: 450px;
    overflow: auto;

    li {
      height: 30px;
      margin-bottom: 15px;
    }

    a {
      color: #555;
      font-weight: bold;
      text-decoration: none;
      font-size: 1.2rem;
    }

    a:hover {
      color: #49b1f5;
    }

    .message-reslut {
      display: flex;
      justify-content: space-between;
    }

    span:hover {
      color: #49b1f5;
    }
  }
}

@media (max-width: 959px) {
  .message-result-wrapper {
    height: calc(100vh - 110px);
    overflow: auto;
  }
}

.divider {
  margin: 20px 0;
  border: 2px dashed #d2ebfd;
}
</style>
