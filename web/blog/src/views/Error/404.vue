<template>
  <section class="page_404">
    <div class="container">
      <div id="lottie_box" class="lottie-container"></div>
      <div class="contant_box_404">
        <h3 class="h2">啊哦，出错啦</h3>
        <p>你寻找的页面走丢啦!</p>
        <router-link to="/" class="link_404">返回主页</router-link>
      </div>
    </div>
  </section>
</template>
<script setup>
import { onMounted } from "vue";
import lottie from "lottie-web";
import * as anData from "@/assets/lottie/404.json";
let animation = {};
onMounted(() => {
  animation = lottie.loadAnimation({
    container: document.getElementById("lottie_box"), //当前需要渲染的DOM
    renderer: "svg", //渲染方式，默认为svg,还可以渲染为html和canvas
    loop: true, //循环播放
    autoplay: true, //自动播放
    animationData: anData.default, //本地动画
  });
});
</script>

<style scoped>
.lottie-container {
  position: sticky;
  width: 40%;
  left: 30%;
  right: 30%;
  height: 400px;
}
.page_404 {
  background: #fff;
  height: 100%;
}

.container {
  padding: 40px 0;
  text-align: center;
}

.page_404 img {
  width: 100%;
}

.link_404 {
  color: #fff;
  background: #1892ff;
  text-decoration: none;
  padding: 8px 20px;
  border-radius: 5px;
  margin: 20px 0;
  display: inline-block;
}

.contant_box_404 {
  margin-bottom: 9%;
}
</style>
