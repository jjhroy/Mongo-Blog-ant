<template>
  <div class="footer-wrap">
    <div class="web-info">
      © 2021 -
      {{ new Date().getFullYear() }} By
      {{ pinia.blogInfo.websiteConfig.websiteAuthor }}
      <a href="https://beian.miit.gov.cn/" target="_blank">
        <icon-font type="icon-ICPbeian" style="font-size: 20px" />
        {{ pinia.blogInfo.websiteConfig.websiteRecordNo }}
      </a>
    </div>
  </div>
</template>
<script setup lang="ts">
import { useStore } from "../store/index";
import { createFromIconfontCN } from "@ant-design/icons-vue";
import config from "../config/config";
const IconFont = createFromIconfontCN({
  scriptUrl: config.ICON_FONT_URL,
});
const pinia = useStore();
</script>
<style scoped lang="less">
.footer-wrap {
  position: relative;
  display: flex;
  justify-content: space-between;
  left: 0;
  right: 0;
  bottom: 0;
  width: 100%;
  line-height: 2;
  padding: 24px 24px;
  color: #eee;
  font-size: 0.9rem;
  text-align: center;
  background: linear-gradient(-45deg,var(--theme-footer-left),var(--theme-footer-right));

  .web-info {
    display: flex;
    flex-direction: column;
    margin: auto;
    P:after {
      content: "二次元真别恶心我😅";
    }

    p:hover {
      color: #49b1f5;
    }

    p:hover:after {
      content: "神子,真🔥啊!🥵";
    }
  }

  img {
    width: 100px;
    height: 100px;
  }
}

.footer-wrap a {
  color: #eee !important;
}

@keyframes Gradient {
  0% {
    background-position: 0 50%;
  }

  50% {
    background-position: 100% 50%;
  }

  100% {
    background-position: 0 50%;
  }
}
</style>
