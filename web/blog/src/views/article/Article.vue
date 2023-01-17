<template>
  <div class="article">
    <!--封面 -->
    <header-cover>
      <div class="article-info">
        <h1 class="article-title">
          {{ state.articleDetail.articleTitle }}
        </h1>
        <div class="article-meta-data-wrap">
          <span class="article-meta-data">
            <calendar-filled />&nbsp;发表于
            {{ $formatDate(state.articleDetail.createTime) }}
          </span>
          <span class="article-meta-data-divider">|</span>
          <span class="article-meta-data">
            <field-time-outlined />&nbsp;更新于
            {{ $formatDate(state.articleDetail.updateTime) }}
          </span>
          <span class="article-meta-data-divider">|</span>
          <span class="article-meta-data">
            <container-filled />&nbsp;分类
            {{ state.articleDetail.categoryName }}
          </span>
        </div>
        <div class="article-meta-data-wrap">
          <span class="article-meta-data">
            <like-filled />&nbsp;点赞量
            {{ state.articleDetail.likeCount }}
          </span>
          <span class="article-meta-data-divider">|</span>
          <span class="article-meta-data">
            <message-filled />&nbsp;评论量
            {{ state.commentCount }}
          </span>
          <span class="article-meta-data-divider">|</span>
          <span class="article-meta-data">
            <eye-filled />&nbsp;阅读量
            {{ state.articleDetail.viewsCount }}
          </span>
        </div>
      </div>
    </header-cover>
    <div class="container">
      <div class="main">
        <a-card class="article-card">
          <!--文章内容-->
          <div class="article-content">
            <!--@ts-ignore-->
            <md-editor v-model="state.articleDetail.articleContent" :theme="pinia.themeName" preview-only
              :preview-theme="state.themes[state.themeIndex].name" />
          </div>

          <!--分割线-->
          <hr />

          <div class="article-like">
            觉得文章写得不错的话可以点赞鼓励一下&nbsp;
            <a-button @click="ArticleLike(state.articleDetail)" :class="isLike(state.articleDetail.id)">
              <like-filled />
              <span v-show="state.articleDetail.likeCount > 0">{{
                  state.articleDetail.likeCount
              }}</span>
            </a-button>
          </div>

          <!-- 版权声明 -->
          <div class="article-signature">
            <img :src="pinia.blogInfo.websiteConfig.websiteAvatar" alt="头像" />
            <div class="copyright">
              <div class="copyright-item">
                <span class="copyright-title">文章作者：</span>
                <span class="copyright-content">
                  <router-link to="/">
                    {{
                        pinia.blogInfo.websiteConfig.websiteAuthor
                    }}</router-link>
                </span>
              </div>
              <div class="copyright-item">
                <span class="copyright-title">文章链接：</span>
                <span class="copyright-content">
                  <a :href="state.articleHref">{{
                      state.articleHref
                  }}</a>
                </span>
              </div>
              <div class="copyright-item">
                <span class="copyright-title">版权声明：</span>
                <span class="copyright-content">
                  本博客所有文章除特别声明外，均采用
                  <a href="https://creativecommons.org/licenses/by-nc-nd/4.0/">BY-NC-SA</a>
                  许可协议。转载请注明出处！
                </span>
              </div>
            </div>
          </div>

          <!-- 标签 -->
          <div class="article-tags">
            <tags-filled style="font-size: 20px" />&nbsp;标签：
            <router-link v-for="item of state.articleDetail.tagDTOList" :key="item.id" :to="'/tag/' + item.id"
              class="tag-link">
              {{ item.tagName }}
            </router-link>
          </div>
          <!-- 上一篇和下一篇 -->
          <div class="previous-next-article" v-if="state.articleDetail.nextArticle">
            <div class="previous-article" :style="{
              width: state.articleDetail.nextArticle.id
                ? '50%'
                : '100%',
            }">
              <router-link :to="`/articles/${state.articleDetail.lastArticle.id}`">
                <img :src="state.articleDetail.lastArticle.articleCover" alt="缩略图" />
                <div class="previous-article-info">
                  <div class="label">« 上一篇</div>
                  <div class="title">
                    {{ state.articleDetail.lastArticle.articleTitle }}
                  </div>
                </div>
              </router-link>
            </div>

            <div class="next-article" :style="{
              width: state.articleDetail.lastArticle.id
                ? '50%'
                : '100%',
            }">
              <router-link :to="`/articles/${state.articleDetail.nextArticle.id}`">
                <img :src="state.articleDetail.nextArticle.articleCover" alt="缩略图" />
                <div class="next-article-info">
                  <div class="label">下一篇 »</div>
                  <div class="title">
                    {{ state.articleDetail.nextArticle.articleTitle }}
                  </div>
                </div>
              </router-link>
            </div>
          </div>
          <!--分割线-->
          <hr />
        </a-card>

        <a-card class="comment-card">
          <!--评论-->
          <comment @get-count="getCount" />
        </a-card>
      </div>

      <div class="side">
        <!--主题切换-->
        <div class="theme-card">
          <blog-card title="主题切换" style="margin: 0">
            <template #icon>
              <skin-filled style="color: #1296db" />&nbsp;
            </template>
            <template #slot-body>
              <div class="theme-body">
                <div @click="themeSwitch(1)" style="display: inline">
                  <icon-font type="icon-zuojiantou" class="left" />
                </div>
                <div v-for="(item, index) in state.themes" :key="index" style="display: inline-block">
                  <p :style="item.show ? 'display:block;' : 'display:none;'" :class="state.animateType">
                    {{ item.name }}
                  </p>
                </div>
                <div @click="themeSwitch(2)" style="display: inline">
                  <icon-font type="icon-youjiantou" class="right" />
                </div>
              </div>
            </template>
          </blog-card>
        </div>
        <!--固定位置-->
        <div class="sticky-layout">
          <!--文章目录-->
          <article-catalog />
          <!--热门文章-->
          <blog-card title="热门文章">
            <template #icon>
              <fire-filled style="color: rgb(240, 17, 17)" />&nbsp;
            </template>
            <template #slot-body>
              <div v-for="article in state.articleDetail.recommendArticleList" :key="article.id"
                class="hot-article-item">
                <router-link :to="`/articles/${article.id}`" class="hot-article-thumbail-link">
                  <img :src="article.articleCover" alt="缩略图" class="hot-article-thumbnail" />
                </router-link>
                <div class="hot-article-info">
                  <router-link :to="`/articles/${article.id}`" class="hot-article-title">{{ article.articleTitle }}
                  </router-link>
                </div>
              </div>
            </template>
          </blog-card>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import "animate.css";
import { getArticleDetail, likeArticle } from "@/api/article";
import MdEditor from "md-editor-v3";
import "md-editor-v3/lib/style.css";
import BlogCard from "@/components/component/BlogCard.vue";
import HeaderCover from "@/components/component/HeaderCover.vue";
import ArticleCatalog from "@/components/component/ArticleCatalog.vue";
import { createFromIconfontCN } from "@ant-design/icons-vue";
import Comment from "@/components/component/Comment.vue";
import { onMounted, reactive } from "vue";
import {
  TagsFilled,
  CalendarFilled,
  ContainerFilled,
  EyeFilled,
  FireFilled,
  FieldTimeOutlined,
  MessageFilled,
  SkinFilled,
  LikeFilled,
} from "@ant-design/icons-vue";
import { useRoute } from "vue-router";
import config from "@/config/config";
import { useStore } from "@/store/index";
import { message } from "ant-design-vue";
import { articleDetails } from "@/types/api/article"
const pinia = useStore();
const IconFont = createFromIconfontCN({
  scriptUrl: config.ICON_FONT_URL,
});
const state = reactive({
  commentCount: 0,
  animateType: "",
  articleDetail: {} as articleDetails,
  articleHref: window.location.href,
  catalogList: [],
  themeIndex: 0,
  themes: [
    { name: "default", show: true },
    { name: "github", show: false },
    { name: "vuepress", show: false },
    { name: "mk-cute", show: false },
    { name: "smart-blue", show: false },
    { name: "cyanosis", show: false },
  ],
})
const getCount = (count: number) => {
  state.commentCount = count;
};
const route = useRoute();
//判断是否点过赞
const isLike = (articleId: number) => {
  //判断是否登录
  if (pinia.isLogin) {
    var articleLikeSet = pinia.loginUserInfo.articleLikeSet;
    //@ts-ignore
    return articleLikeSet.indexOf(articleId) != -1 ? "like-active" : "like";
  } else {
    return "like";
  }
};
//切换主题
const themeSwitch = (type: number) => {
  if (type === 1) {
    state.animateType = "animate__animated animate__fadeInRight";
    if (state.themeIndex === 0) {
      state.themes[0].show = false;
      state.themes[5].show = true;
      state.themeIndex = 5;
    } else {
      state.themes[state.themeIndex].show = false;
      state.themes[state.themeIndex - 1].show = true;
      state.themeIndex--;
    }
  } else {
    state.animateType = "animate__animated animate__fadeInLeft";
    if (state.themeIndex === 5) {
      state.themes[5].show = false;
      state.themes[0].show = true;
      state.themeIndex = 0;
    } else {
      state.themes[state.themeIndex].show = false;
      state.themes[state.themeIndex + 1].show = true;
      state.themeIndex++;
    }
  }
};
const ArticleLike = (item: any) => {
  //判断是否登录
  if (!pinia.isLogin) {
    message.error("登录？");
    return false;
  }
  likeArticle(item.id).then(data => {
    if (data.flag) {
      //判断是否点赞
      //@ts-ignore
      if (pinia.loginUserInfo.articleLikeSet.indexOf(item.id) != -1) {
        item.likeCount -= 1;
        message.error("痛,太痛了!");
      } else {
        item.likeCount += 1;
        message.success("谢谢支持");
      }
      pinia.articleLike(item.id);
    }
  });
};
const getDetail = async () => {
  const data = await getArticleDetail(route.fullPath)
  state.articleDetail = data;
}
onMounted(() => {
  getDetail()
});
</script>

<style lang="less" scoped>
.article {
  height: 100%;
  width: 100%;
  background-color: var(--theme-background);

  .article-meta-data-wrap {
    display: flex;
    justify-content: center;
  }

  .article-meta-data {
    font-size: 14px;
    color: white;
    box-sizing: border-box;
    line-height: 24px;
    overflow: hidden;
    -webkit-line-clamp: 1;
    display: -webkit-box;
    -webkit-box-orient: vertical;
  }

  .article-meta-data-divider {
    color: white;
    margin: 3px 8px;
    font-size: 14px;
  }

  .container {
    display: flex;

    .comment-card {
      margin-top: 24px;
      background: var(--theme-card-color);
      border: 1px solid var(--theme-card-color);
      border-radius: 8px;
      box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.05);
    }

    .article-card {
      background: var(--theme-card-color);
      border: 1px solid var(--theme-card-color);
      border-radius: 8px;
      box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.05);
    }

    @media (min-width: 760px) {
      padding: 16px 20% 16px 20%;

      .main {
        width: 75%;
      }

      .side {
        width: 25%;
      }
    }

    @media (max-width: 759px) {
      padding: 16px 8px 16px 8px;

      .main {
        width: 100%;
      }

      .side {
        display: none;
      }
    }

    .article-like {
      text-align: center;
      color: var(--theme-color);

      .like-active {
        background-color: #ff4d4f;
        color: white;
      }

      .like {
        color: white;
        background-color: #1890ff;
      }
    }

    hr {
      position: relative;
      margin: 40px auto;
      border: 2px dashed #d2ebfd;
      width: calc(100% - 4px);
    }

    .theme-card {
      margin-left: 20px;

      .theme-body {
        position: relative;
        text-align: center;

        p {
          font-size: 1.5rem;
          font-weight: bold;
          color: #19b1f5;
        }

        .left {
          position: absolute;
          left: 0;
          font-size: 35px;
        }

        .right {
          position: absolute;
          right: 0;
          font-size: 35px;
        }
      }
    }

    .sticky-layout {
      position: sticky;
      top: 20px;
      margin-left: 20px;
    }

    .article-signature {
      border: 1px solid #ddd;
      position: relative;
      overflow: hidden;
      margin: 30px 5px 10px 5px;
      border-radius: 6px;
      display: flex;
      align-items: center;
      padding: 12px;
      transition: all 0.4s;

      &:hover {
        box-shadow: 0 3px 15px rgba(0, 0, 0, 0.1);
        transform: translateY(-1px);
      }

      img {
        margin-left: 10px;
        width: 90px;
        height: 90px;
        border-radius: 50%;
        transition: all 0.6s;

        &:hover {
          transform: rotate(-360deg);
        }
      }

      .copyright {
        padding-left: 20px;

        .copyright-item {
          display: -webkit-box;
          overflow: hidden;
          text-overflow: ellipsis;
          -webkit-line-clamp: 1;
          -webkit-box-orient: vertical;
          line-height: 28px;
          font-size: 15px;
          color: var(--theme-color);

          a {
            color: #99a9bf;
            transition: all 0.4s;
            cursor: pointer;

            &:hover {
              color: #19b1f5;
            }
          }

          .copyright-title {
            font-weight: bold;
            color: #19b1f5;
          }
        }
      }
    }

    .article-tags {
      padding-left: 3px;
      margin-top: 20px;
      color: var(--theme-color);
      font-size: 15px;

      a {
        border-radius: 4px;
        font-size: 13px;
        padding: 3px 12px;
        text-decoration: none;
        transition: all 0.4s;
        background: #49b1f5;
        margin-right: 8px;
        color: white;
        display: inline-block;
      }
    }

    .previous-next-article {
      width: 100%;
      margin-top: 50px;
      overflow: hidden;
      background: black;
      display: flex;
      border-radius: 9px;

      .previous-article,
      .next-article {
        width: 50%;

        a {
          height: 150px;
          overflow: hidden;
          display: block;
          position: relative;

          img {
            height: 100%;
            width: 100%;
            position: absolute;
            object-fit: cover;
            opacity: 0.5;
            transition: all 0.6s ease-in-out;

            &:hover {
              transform: scale(1.1);
              opacity: 0.8;
            }
          }

          .previous-article-info,
          .next-article-info {
            pointer-events: none;
            position: absolute;
            top: 50%;
            width: 100%;
            padding: 20px 40px;
            transform: translate(0, -50%);
            color: white;
            line-height: 28px;
            box-sizing: border-box;

            .label {
              font-size: 13px;
            }

            .title {
              font-weight: 500;
              font-size: 14px;
              display: -webkit-box;
              overflow: hidden;
              text-overflow: ellipsis;
              -webkit-line-clamp: 1;
              -webkit-box-orient: vertical;
            }
          }

          .next-article-info {
            text-align: right;
          }
        }
      }
    }

    .copyright {
      padding-left: 20px;

      .copyright-item {
        display: -webkit-box;
        overflow: hidden;
        text-overflow: ellipsis;
        -webkit-line-clamp: 1;
        -webkit-box-orient: vertical;
        line-height: 28px;
        font-size: 15px;
        color: var(--text-color);

        a {
          color: #99a9bf;
          transition: all 0.4s;
          cursor: pointer;

          &:hover {
            color: #19b1f5;
          }
        }

        .copyright-title {
          font-weight: bold;
          color: #19b1f5;
        }
      }
    }

    .hot-article-item {
      display: flex;
      justify-content: center;
      align-content: center;
      padding: 8px 0;
    }

    .hot-article-thumbail-link {
      height: 58px;
      width: 58px;
      overflow: hidden;
    }

    .hot-article-thumbnail {
      width: 100%;
      height: 100%;
      object-fit: cover;
      transition: all 0.4s ease;
    }

    .hot-article-thumbnail:hover {
      transform: scale(1.1);
    }

    .hot-article-info {
      flex: 1;
      padding-left: 10px;
      word-break: break-all;
      display: inline-block;
      align-self: center;
      color: var(--theme-color);
    }

    .hot-article-title {
      color: var(--text-color);
      font-size: 13.5px;
      text-decoration: none;
      transition: color 0.4s;
      overflow: hidden;
      display: -webkit-box;
      -webkit-box-orient: vertical;
      line-height: 1.5;
      -webkit-line-clamp: 2;
    }

    .hot-article-title:hover {
      color: var(--theme-color);
    }

    .hot-article-meta-data {
      font-size: 12px;
      color: rgb(133, 133, 133);
      box-sizing: border-box;
      line-height: 24px;
      overflow: hidden;
      -webkit-line-clamp: 1;
      display: -webkit-box;
      -webkit-box-orient: vertical;
    }

    .hot-article-meta-data span {
      margin-left: 4%;
    }
  }

}
</style>
