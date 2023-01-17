<template>
  <div class="cover" :style="coverStyle">
    <div class="cover-word-container">
      <h1 class="cover-title">{{ pinia.blogInfo.websiteConfig.websiteName }}</h1>
      <div class="cover-content">
        <typewriter />
      </div>
    </div>
    <down-outlined style="font-size: 30px" class="arrow-down" @click="scrollToContent" />
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from "vue";
import Typewriter from "../tool/Typewriter.vue";
import { DownOutlined } from "@ant-design/icons-vue";
import { useStore } from "@/store";
const pinia = useStore()
const defaultUrl = ref("https://upload.linkstarted.top/config/690cd550b6f59b2613974a3804a01d42.jpg")
let coverStyle = reactive({
  background: `url(${pinia.blogInfo.websiteConfig.websiteCover || defaultUrl.value})`,
  backgroundSize: "cover",
});

const scrollToContent = () => {
  let h = document.getElementsByClassName("cover")[0].scrollHeight;
  window.scrollTo({ top: h, behavior: "smooth" });
};

</script>

<style scoped>
.cover {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 100vh;
  background-attachment: fixed;
  text-align: center;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 10px;
}

.cover-word-container {
  margin: 0 auto;
  text-align: center;
}

.cover-title {
  font-family: "Kanit";
  color: white;
  font-size: 2.5rem;
  margin: 0px;
  text-shadow: 0 3px 6px rgba(0, 0, 0, 0.3);
  animation: fadeUpIn 1.5s;
}

.cover-content {
  color: white;
  font-size: 1.5rem;
  font-family: "Long Cang", sans-serif;
  text-shadow: 0 3px 6px rgba(0, 0, 0, 0.3);
  animation: fadeUpInSlow 1.5s;
}

.arrow-down {
  position: absolute;
  bottom: 6%;
  cursor: pointer;
  color: rgba(255, 255, 255, 0.6);
  transition: color 0.2s;
  animation: bounce 4s infinite;
}

.arrow-down:hover {
  color: rgba(255, 255, 255, 0.8);
  transition: color 0.2s;
}

@-webkit-keyframes bounce {

  0%,
  10%,
  25%,
  40%,
  50% {
    -webkit-transform: translateY(0);
    transform: translateY(0);
  }

  20% {
    -webkit-transform: translateY(-10px);
    transform: translateY(-10px);
  }

  30% {
    -webkit-transform: translateY(-5px);
    transform: translateY(-5px);
  }
}

@keyframes bounce {

  0%,
  10%,
  25%,
  40%,
  50% {
    -webkit-transform: translateY(0);
    transform: translateY(0);
  }

  20% {
    -webkit-transform: translateY(-10px);
    transform: translateY(-10px);
  }

  30% {
    -webkit-transform: translateY(-5px);
    transform: translateY(-5px);
  }
}

@keyframes fadeUpInCover {
  0% {
    -webkit-transform: translateY(-70px);
    transform: translateY(-70px);
    opacity: 0%;
  }

  100% {
    -webkit-transform: translateY(0);
    transform: translateY(0);
    opacity: 100%;
  }
}

@keyframes fadeUpIn {
  0% {
    -webkit-transform: translateY(-50px);
    transform: translateY(-50px);
    opacity: 0%;
  }

  100% {
    -webkit-transform: translateY(0);
    transform: translateY(0);
    opacity: 100%;
  }
}

@keyframes fadeInUp {
  from {
    margin-top: 50px;
    opacity: 0;
  }

  to {
    margin-top: 0;
    opacity: 1;
  }
}

@keyframes fadeUpInSlow {
  0% {
    -webkit-transform: translateY(-100px);
    transform: translateY(-100px);
    opacity: 0%;
  }

  100% {
    -webkit-transform: translateY(0);
    transform: translateY(0);
    opacity: 100%;
  }
}
</style>
