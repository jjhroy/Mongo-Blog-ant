<template>
  <div class="settings">
    <!-- 二次元封面 -->
    <header-cover>
      <div class="article-info">
        <h1 class="article-title">个人中心</h1>
      </div>
    </header-cover>
    <!-- 个人信息 -->
    <div class="container">
      <a-card class="settings-card">
        <!-- 用户封面 -->
        <div class="user-cover">
          <img src="http://www.dmoe.cc/random.php" alt="封面" />
        </div>
        <!-- 用户头像 -->
        <div class="user-avatar">
          <img :src="pinia.loginUserInfo.avatar" alt="头像" id="avatar" />
        </div>
        <!--用户信息-->
        <a-form name="setting" autocomplete="off" ok :label-col="{ span: 3 }" :wrapper-col="{ span: 21 }">
          <a-form-item label="昵称" name="nickname" :colon="false">
            <a-input :bordered="false" v-model:value="state.userInfoForm.nickname" />
          </a-form-item>
          <a-form-item label="网站" name="webSite" :colon="false">
            <a-input :bordered="false" v-model:value="state.userInfoForm.webSite" />
          </a-form-item>
          <a-form-item label="邮箱" :colon="false">
            <a-input :bordered="false" v-model:value="state.email" />
          </a-form-item>
          <a-form-item label="简介" name="intro" :colon="false">
            <a-input :bordered="false" v-model:value="state.userInfoForm.intro" />
          </a-form-item>
        </a-form>
        <div class="card-footer">
          <a-button type="priamry" @click="update">保存</a-button>
        </div>
      </a-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive } from "vue";
import { message } from "ant-design-vue";
import { updateUserInfo } from "@/api/user"
import HeaderCover from "@/components/component/HeaderCover.vue";
import { useStore } from "@/store/index";
const pinia = useStore();
const state = reactive({
  userInfoForm: {
    nickname: pinia.loginUserInfo.nickname,
    webSite: pinia.loginUserInfo.webSite,
    intro: pinia.loginUserInfo.intro
  },
  email:pinia.loginUserInfo.email,
  avatar: pinia.loginUserInfo.avatar
});
const update = async () => {
  await updateUserInfo(state.userInfoForm)
  message.success("修改成功")
}
onMounted(() => {

})
</script>

<style lang="less" scoped>
.settings {
  width: 100%;
  height: 100%;
  background-color: var(--theme-background);

  .container {
    padding: 5% 10%;
  }

  .settings-card {
    background: var(--theme-card-color);
    border-radius: 8px;
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.05);
    border: 1px solid var(--theme-card-color);

    :deep(.ant-form-item-label label) {
      color: var(--theme-color);
    }

    :deep(.ant-input) {
      color: var(--theme-color);
    }

    .user-cover {
      height: 240px;
      overflow: hidden;

      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
        border-top-left-radius: 8px;
        border-top-right-radius: 8px;
      }
    }

    .user-avatar {
      height: 130px;
      width: 130px;
      margin-left: 35px;
      padding: 5px;
      transform: translateY(-50%);
      background: white;
      border: transparent;
      border-radius: 50%;

      img {
        width: 100%;
        height: 100%;
        border-radius: 50%;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
        transition: 0.5s ease-out;

        &:hover {
          transform: rotate(360deg);
        }
      }
    }

    .card-footer {
      padding: 1rem 2rem;
      background-color: var(--theme-background);
      border-top: 1px solid rgba(0, 0, 0, 0);
      border-bottom-left-radius: 8px;
      border-bottom-right-radius: 8px;
      text-align: end;
    }
  }
}
</style>
