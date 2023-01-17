<template>
  <!--添加弹窗-->
  <a-modal v-model:visible="state.visible" title="友链申请" :footer="null" width="400px" centered>
    <a-form ref="formRef" :model="state.addForm" name="basic" autocomplete="off" @finish="addLink" ok hideRequiredMark>
      <a-form-item label="博客名称" name="linkName" :colon="false" :rules="[{ required: true, message: '不能为空' }]">
        <a-input v-model:value="state.addForm.linkName" />
      </a-form-item>

      <a-form-item label="头像地址" name="linkAvatar" :colon="false" :rules="[{ required: true, message: '不能为空' }]">
        <a-input v-model:value="state.addForm.linkAvatar" />
      </a-form-item>

      <a-form-item label="博客地址" name="linkAddress" :colon="false" :rules="[{ required: true, message: '不能为空' }]">
        <a-input v-model:value="state.addForm.linkAddress">
          <template #addonBefore>
            <a-select v-model:value="state.selectStr" style="width: 90px">
              <a-select-option value="Http://">Http://</a-select-option>
              <a-select-option value="Https://">Https://</a-select-option>
            </a-select>
          </template>
        </a-input>
      </a-form-item>

      <a-form-item label="博客简介" name="linkIntro" :colon="false" :rules="[{ required: true, message: '不能为空' }]">
        <a-input v-model:value="state.addForm.linkIntro" />
      </a-form-item>

      <a-form-item :wrapper-col="{ offset: 8, span: 16 }">
        <a-button type="primary" html-type="submit">申请添加</a-button>
      </a-form-item>
    </a-form>
  </a-modal>
  <div class="link">
    <!-- 二次元封面 -->
    <header-cover>
      <div class="article-info">
        <h1 class="article-title">友链</h1>
      </div>
    </header-cover>
    <!-- 标签列表 -->
    <div class="container">
      <a-card class="link-card">
        <div class="link-title">友链墙</div>
        <div v-if="state.friendLinkList.length != 0">
          <a-row class="link-container">
            <a-col class="link-wrapper" v-for="item of state.friendLinkList" :span="8" :key="item.id">
              <a :href="item.linkAddress">
                <div class="link-avatar">
                  <img :src="item.linkAvatar" />
                </div>
                <div style="width:100%;z-index:10;">
                  <div class="link-name">{{ item.linkName }}</div>
                  <div class="link-intro">{{ item.linkIntro }}</div>
                </div>
              </a>
            </a-col>
          </a-row>
        </div>
        <div v-else>
          <div class="empty">
            <img src="https://img-static.mihoyo.com/communityweb/upload/41724f7913741265f489592102214b0f.png" />
            <span>还没有没有一个朋友......</span>
          </div>
        </div>
        <!-- 说明 -->
        <div class="link-title">添加友链</div>
        <blockquote>
          <div>名称：{{ pinia.blogInfo.websiteConfig.websiteName }}</div>
          <div>简介：{{ pinia.blogInfo.websiteConfig.websiteIntro }}</div>
          <div>头像：{{ pinia.blogInfo.websiteConfig.websiteAvatar }}</div>
        </blockquote>
        <div class="add-info">
          <span @click="showModal">需要交换友链的点击这里💖</span>
        </div>
        <blockquote>
          友链信息展示需要，你的信息格式要包含：名称、介绍、链接、头像
        </blockquote>
      </a-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { link } from "@/types/api/blog"
import { message } from "ant-design-vue";
import { listFriendLink, addFriendLink } from "@/api/blog";
import { onMounted, reactive, ref } from "vue";
import HeaderCover from "@/components/component/HeaderCover.vue";
import { useStore } from "@/store/index";
const pinia = useStore();
const state = reactive({
  addForm: {
    linkName: "",
    linkAvatar: "",
    linkIntro: "",
    linkAddress: "",
  },
  selectStr: "",
  friendLinkList: [] as link[],
  visible: false,
})
const formRef = ref();
const getFirendLinkList = async () => {
  const data = await listFriendLink()
  state.friendLinkList.push(...data);
}

const showModal = () => {
  state.visible = true;
};

const addLink = async () => {
  state.addForm.linkAddress = state.selectStr + state.addForm.linkAddress;
  await addFriendLink(state.addForm)
  message.success("申请成功,审核后即可添加");
  state.visible = false;
  formRef.value.resetFields();
};

onMounted(() => {
  getFirendLinkList()
});
</script>

<style scoped lang="less">
.link {
  width: 100%;
  height: 100%;
  background-color: var(--theme-background);

  .container {
    padding: 5% 10%;
  }

  .link-card {
    background: var(--theme-card-color);
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.05);
    border: 1px solid var(--theme-card-color);
  }

  .empty {
    text-align: center;

    img {
      width: 56px;
      height: 56px;
    }
  }

  .add-info {
    margin: 10px;
    color: var(--theme-color);

    span:hover {
      color: #49b1f5;
    }
  }

  blockquote {
    color: var(--theme-color);
    line-height: 2;
    margin: 0;
    font-size: 15px;
    border-left: 0.2rem solid #49b1f5;
    padding: 10px 1rem !important;
    background-color: var(--theme-background);
    border-radius: 4px;
    margin: 10px;
    word-break: break-all
  }

  .link-title {
    color: var(--theme-color);
    font-size: 21px;
    font-weight: bold;
    line-height: 2;
    margin: 10px;
  }

  .link-container {
    margin: 10px 10px 0;
  }

  .link-wrapper {
    position: relative;
    transition: all 0.3s;
    border-radius: 8px;
  }

  .link-avatar {
    display: inline-block;
    margin-top: 10px;
    margin-left: 10px;
    transition: all 0.5s;

    img {
      width: 65px;
      height: 65px;
      border-radius: 50%;
    }
  }

  @media (max-width: 759px) {
    .link-avatar {
      margin-left: 30px;
    }
  }

  .link-name {
    text-align: center;
    font-size: 1.25rem;
    font-weight: bold;
    z-index: 9000;
    color: var(--theme-color);
  }

  .link-intro {
    text-align: center;
    padding: 16px 10px;
    height: 50px;
    font-size: 13px;
    color: var(--theme-color);
    width: 100%;
    z-index: 9000;
  }

  .link-wrapper:hover a {
    color: #fff;
  }

  .link-wrapper:hover .link-intro {
    color: #fff;
  }

  .link-wrapper:hover .link-avatar {
    transform: rotate(360deg);
  }

  .link-wrapper a {
    color: #333;
    text-decoration: none;
    display: flex;
    height: 100%;
    width: 100%;
  }

  .link-wrapper:hover {
    box-shadow: 0 2px 20px #49b1f5;
  }

  .link-wrapper:hover:before {
    transform: scale(1);
  }

  .link-wrapper:before {
    position: absolute;
    border-radius: 8px;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    background: #49b1f5 !important;
    content: "";
    transition-timing-function: ease-out;
    transition-duration: 0.3s;
    transition-property: transform;
    transform: scale(0);
  }
}
</style>
