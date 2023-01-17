<template>
  <div :class="cardClass">
    <!-- 缩略图 -->
    <router-link :to="`/articles/${article.id}`" :class="thumbailLinkClass"><img :src="article.articleCover" alt="缩略图"
        class="post-article-thumbnail" />
    </router-link>

    <!-- 文章信息 -->
    <div class="post-article-info">
      <!-- 文章标题 -->
      <router-link :to="`/articles/${article.id}`" class="post-article-title">
        {{ article.articleTitle }}
      </router-link>

      <!-- 其它数据 -->
      <div class="post-article-meta-data-wrap">
        <span class="post-article-meta-data" v-if="article.isTop === 1">
          <vertical-align-top-outlined style="color: red;" /> 置顶
          <span class="post-article-meta-data-divider">|</span>
        </span>
        <span class="post-article-meta-data">
          <calendar-filled /> 发表于 {{ $formatDate(article.createTime) }}
        </span>
        <span class="post-article-meta-data-divider">|</span>
        <span class="post-article-meta-data">
          <container-filled /> 分类 {{ article.categoryName }}
        </span>
        <span class="post-article-meta-data-divider">|</span>
        <span class="post-article-meta-data" v-if="article.tagDTOList.length === 1">
          <tags-filled /> {{ article.tagDTOList[0].tagName }}
        </span>
        <span class="post-article-meta-data" v-else>
          <tags-filled /> {{ article.tagDTOList[0].tagName }} ...
        </span>
      </div>

      <!-- 文章摘要 -->
      <div class="post-article-summary">
        {{ deleteMDTag(article.articleContent) }}
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive } from "vue";
import {
  TagsFilled,
  CalendarFilled,
  ContainerFilled,
  VerticalAlignTopOutlined,
} from "@ant-design/icons-vue";
import MarkdownIt from "markdown-it";
import { articles } from "@/types/api/article"
let md = new MarkdownIt();
let cardClass = reactive(["post-article-card"]);
let thumbailLinkClass = reactive(["post-article-thumbail-link"]);
const props = defineProps<{
  article: articles,
  reverse: boolean
}>()
const deleteMDTag = (articleContent: string) => {
  // 去除markdown标签
  return articleContent = md
    .render(articleContent)
    .replace(/<\/?[^>]*>/g, "")
    .replace(/[|]*\n/, "")
    .replace(/&npsp;/gi, "");
}
if (props.reverse) {
  cardClass.push("post-article-card-reversed");
  thumbailLinkClass.push("post-article-thumbail-link-reversed");
}
</script>

<style scoped lang="less">
.post-article-card {
  display: flex;
  justify-content: center;
  align-items: center;
  color: var(--theme-color);
  background-color: var(--theme-card-color);
  border-radius: 8px;
  box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.05);

  @media (min-width: 760px) {
    height: 240px;
    flex-direction: row;
  }

  @media (max-width: 759px) {
    flex-direction: column;
    height: 450px;
  }
}

.post-article-card-reversed {
  @media (min-width: 760px) {
    flex-direction: row-reverse;

    .post-article-thumbnail {
      clip-path: polygon(0 100%, 100% 100%, 100% 0, 20% 0);
    }

    .post-article-thumbnail:hover {
      //transform: scale(1.1);
      box-shadow: 0 16px 32px 0 rgba(48, 55, 66, 0.15);
      /* 鼠标悬浮时盒子出现的阴影 */
      transform: translate(10px, 0);
      /* 鼠标悬浮时盒子上移10px */
    }
  }

  @media (max-width: 759px) {
    flex-direction: column;

    .post-article-thumbnail {
      clip-path: polygon(0 100%, 100% 100%, 100% 0, 0 0);
    }
  }
}

.post-article-thumbail-link {
  @media (min-width: 760px) {
    border-radius: 8px 0 0 8px;
    width: 44%;
  }

  @media (max-width: 759px) {
    border-radius: 8px 8px 0 0;
    width: 100%;
  }

  height: 100%;
  overflow: hidden;
  box-sizing: border-box;
}

.post-article-thumbail-link-reversed {
  @media (min-width: 760px) {
    border-radius: 0 8px 8px 0;
  }

  @media (max-width: 759px) {
    border-radius: 8px 8px 0 0;
  }
}

.post-article-thumbnail {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: all 0.5s ease;
  box-sizing: border-box;
  overflow: hidden;

  @media (min-width: 760px) {
    clip-path: polygon(0 100%, 100% 100%, 80% 0, 0 0);
  }

  @media (max-width: 759px) {
    clip-path: polygon(0 100%, 100% 100%, 100% 0, 0 0);
  }

}

.post-article-thumbnail:hover {
  //transform: scale(1.1);
  box-shadow: 0 16px 32px 0 rgba(48, 55, 66, 0.15);
  /* 鼠标悬浮时盒子出现的阴影 */
  transform: translate(-10px, 0);
  /* 鼠标悬浮时盒子上移10px */
}

.post-article-info {
  @media (min-width: 760px) {
    width: 57%;
    padding: 0 40px;
  }

  @media (max-width: 759px) {
    width: 100%;
    padding: 10px;
  }
}

.post-article-title {
  color: var(--theme-color);
  font-size: 24px;
  text-decoration: none;
  transition: color 0.4s;
  overflow: hidden;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  line-height: 1.5;
  -webkit-line-clamp: 2;
}

.post-article-title:hover {
  color: #49b1f5;
}

.post-article-meta-data-wrap {
  display: flex;
  margin: 9px 0;
}

.post-article-meta-data {
  font-size: 12px;
  color: var(--theme-color);
  box-sizing: border-box;
  line-height: 24px;
  overflow: hidden;
  -webkit-line-clamp: 1;
  display: -webkit-box;
  -webkit-box-orient: vertical;
}

.post-article-meta-data-divider {
  color: rgb(133, 133, 133);
  margin: 3px 8px;
  font-size: 12px;
}

.post-article-summary {
  color: var(--text-color);
  font-size: 14px;
  overflow: hidden;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  line-height: 2;
  -webkit-line-clamp: 2;
}
</style>
