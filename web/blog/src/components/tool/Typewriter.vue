<template>
  {{ typerOption.output }}<span class="typed-cursor">|</span>
</template>

<script setup>
import { onMounted, reactive } from "vue";
import EasyTyper from "easy-typer-js";
onMounted(() => {
  init();
})
const typerOption = reactive({
  output: "请输入需要显示的内容",
  isEnd: false,
  speed: 500,
  singleBack: false,
  sleep: 20,
  type: "rollback",
  backSpeed: 500,
  sentencePause: true,
});
// 初始化
const init = () => {
  document.title = "mongo-blog";
  // 一言Api进行打字机循环输出效果
  fetch("https://v1.hitokoto.cn?c=i")
    .then((res) => {
      return res.json();
    })
    .then(({ hitokoto }) => {
      initTyped(hitokoto);
    });
};
const initTyped = (input) => {
  // eslint-disable-next-line no-unused-vars
  const typed = new EasyTyper(typerOption, input);
};
</script>
<style lang="stylus" scoped>
.typed-cursor
  opacity: 1
  animation: blink 0.7s infinite
@keyframes blink
  0%
    opacity: 1
  50%
    opacity: 0
  100%
    opacity: 1
</style>