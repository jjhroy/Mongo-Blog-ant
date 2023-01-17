<template>
  <div class="oauth-background">
    <div id="preloader_1">
      <span></span>
      <span></span>
      <span></span>
      <span></span>
      <span></span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { qqAuthLogin } from "@/api/auth";
import { useRoute, useRouter } from "vue-router";
import { useStore } from "@/store";
import { message } from "ant-design-vue";
import { onMounted } from "vue";
const pinia = useStore();
const route = useRoute();
const router = useRouter();
const qqLogin = () => {
  //关闭登录框
  pinia.showLoginModal = false;
  // 拿到openId，accessToken传入后台
  //@ts-ignore
  if (QC.Login.check()) {
    //@ts-ignore
    QC.Login.getMe(async function (openId: string, accessToken: string) {
      let params = {
        openId: openId,
        accessToken: accessToken,
      };
      const data = await qqAuthLogin(params)
      //保存登录状态
      pinia.saveLoginUserInfo(data.data);
      if (data.email == null) {
        message.warning("请及时绑定邮箱");
      } else {
        message.success(data.message);
      }
    });
  } else {
    message.error("获取token失败");
  }
  // 跳转回原页面
  const loginUrl = pinia.loginUrl;
  if (loginUrl != null && loginUrl != "") {
    router.push(loginUrl);
  } else {
    router.push("/");
  }
}
onMounted(() => {
  qqLogin()
});
</script>

<style scoped>
.oauth-background {
  position: fixed;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  background: #fff;
  z-index: 1000;
}

#preloader_1 {
  position: relative;
  top: 45vh;
  left: 45vw;
}

#preloader_1 span {
  display: block;
  bottom: 0px;
  width: 9px;
  height: 5px;
  background: #9b59b6;
  position: absolute;
  animation: preloader_1 1.5s infinite ease-in-out;
}

#preloader_1 span:nth-child(2) {
  left: 11px;
  animation-delay: 0.2s;
}

#preloader_1 span:nth-child(3) {
  left: 22px;
  animation-delay: 0.4s;
}

#preloader_1 span:nth-child(4) {
  left: 33px;
  animation-delay: 0.6s;
}

#preloader_1 span:nth-child(5) {
  left: 44px;
  animation-delay: 0.8s;
}

@keyframes preloader_1 {
  0% {
    height: 5px;
    transform: translateY(0px);
    background: #9b59b6;
  }

  25% {
    height: 30px;
    transform: translateY(15px);
    background: #3498db;
  }

  50% {
    height: 5px;
    transform: translateY(0px);
    background: #9b59b6;
  }

  100% {
    height: 5px;
    transform: translateY(0px);
    background: #9b59b6;
  }
}
</style>
