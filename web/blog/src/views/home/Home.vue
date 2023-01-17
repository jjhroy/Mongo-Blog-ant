<template>
  <div class="home">
    <!-- 网站封面 -->
    <home-cover>
    </home-cover>
    <!--首页主体-->
    <div class="container">
      <div class="main-card">
        <home-tool-bar :text="pinia.blogInfo.websiteConfig.websiteNotice" @sortArticle="sortArticle">
        </home-tool-bar>
        <div class="post-article-list">
          <article-list-card v-for="(article, index) in state.articleList" :key="article.id" :article="article"
            :reverse="index % 2 == 1" />
          <div class="loading-text">
            <span style="font-size: 15px; font-weight: bold;color: var(--theme-color);"
              @click="getHomeArticleList()">点击加载更多</span>
            <div class="dotting"></div>
          </div>
        </div>
      </div>
      <div class="sider-card">
        <!--图床弹窗-->
        <div>
          <a-modal v-model:visible="state.visible" title="芒果图床🍋" centered>
            <a-input v-model:value="state.uploadCode" placeholder="输入上传码">
              <template #prefix>
                <gift-filled />
              </template>
            </a-input>
            <a-upload-dragger v-model:fileList="state.fileList" name="file" :multiple="true"
              action="/api/admin/cloud/images" @change="handleChange" @drop="handleDrop" :before-upload="beforeUpload"
              :max-count="10">
              <p class="ant-upload-drag-icon">
                <inbox-outlined></inbox-outlined>
              </p>
              <p class="ant-upload-text">点击或拖动文件上传</p>
              <template #itemRender="{ file, actions }">
                <div style="display: flex;margin: 4px;" v-if="file.status">
                  <div style="align-self: center;">
                    <icon-font :type="file.status === 'error' ? 'icon-jingshi' : 'icon-chenggong'" />
                    <span :style="file.status === 'error' ? 'color: red;' : ''">{{
                        file.name
                    }}</span>
                  </div>
                  <div style="margin-left: auto;" v-if="file.status == 'done'">
                    <copy-filled @click="copyUrl(file)" />
                  </div>
                  <div style="margin-left: auto;" v-else>
                    <delete-filled @click="actions.remove" />
                  </div>
                </div>
              </template>
            </a-upload-dragger>
          </a-modal>
        </div>

        <div class="img__upload">
          <a-button shape="circle" size="large" @click="showModal">
            <template #icon>
              <icon-font type="icon-ic_image_upload" />
            </template>
          </a-button>

        </div>
        <!--博客信息-->
        <a-card class="card-style">
          <template #cover>
            <div class="blog-avatar">
              <a-avatar :src="pinia.blogInfo.websiteConfig.websiteAvatar" :size="110" />
            </div>
            <div class="author-info">
              {{ pinia.blogInfo.websiteConfig.websiteAuthor }}
            </div>
          </template>
          <a-card-meta>
            <template #description>
              <div class="blog-info">
                <a-row>
                  <a-col :span="8"><span>文章</span></a-col>
                  <a-col :span="8"><span>标签</span></a-col>
                  <a-col :span="8"><span>分类</span></a-col>
                </a-row>
                <a-row>
                  <a-col :span="8"><span>{{ pinia.blogInfo.articleCount }}</span></a-col>
                  <a-col :span="8"><span>{{ pinia.blogInfo.categoryCount }}</span></a-col>
                  <a-col :span="8"><span>{{ pinia.blogInfo.tagCount }}</span></a-col>
                </a-row>
              </div>
            </template>
          </a-card-meta>
        </a-card>
        <!--博客公告-->
        <!-- <a-card class="card-style">
          <a-card-meta>
            <template #title>
              <bell-filled style="color: rgb(240, 17, 17)" />&nbsp;<span class="card-title">公告</span>
            </template>
            <template #description>
              <div class="blog-notice">
                {{ pinia.blogInfo.websiteConfig.websiteNotice }}
              </div>
            </template>
          </a-card-meta>
        </a-card> -->
        <!--网站信息-->
        <a-card class="card-style">
          <a-card-meta>
            <template #title>
              <line-chart-outlined style="color: rgb(240, 17, 17)" />
              &nbsp;<span class="card-title">网站信息</span>
            </template>
            <template #description>
              <div class="web-info">
                <div style="padding: 4px 0 0">
                  已运行:<span style="float: right !important">{{
                      state.time
                  }}</span>
                </div>
                <div style="padding: 4px 0 0">
                  访问量:<span style="float: right !important">
                    {{ pinia.blogInfo.viewsCount }}
                  </span>
                </div>
              </div>
            </template>
          </a-card-meta>
        </a-card>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import "animate.css";
import useClipboard from 'vue-clipboard3'
import { getArticleList } from "@/api/article";
import { message } from 'ant-design-vue';
import { onMounted, reactive, onBeforeUnmount } from "vue";
import { createFromIconfontCN, InboxOutlined, CopyFilled, DeleteFilled, GiftFilled, LineChartOutlined } from "@ant-design/icons-vue";
import config from "@/config/config";
import ArticleListCard from "@/components/component/ArticleListCard.vue";
import HomeCover from "@/components/component/HomeCover.vue";
import HomeToolBar from "@/components/component/HomeToolBar.vue";
import { useStore } from "@/store/index";
import { articles } from "@/types/api/article";
const pinia = useStore();
const IconFont = createFromIconfontCN({
  scriptUrl: config.ICON_FONT_URL,
});
//复制组件
const { toClipboard } = useClipboard()
const state = reactive({
  visible: false,
  uploadCode: "",
  fileList: [],
  time: "",
  articleList: [] as articles[],
  noResult: false,
  message: "",
  isShowSortBtn: false,
  animateType: "btn animate__animated animate__fadeInDown",
  themeClass: true,
  timer: null as any,
  currentType: 1,
  currentPage: 1,
})
//运行时间
const runTime = () => {
  let timeold =
    new Date().getTime() -
    new Date(pinia.blogInfo.websiteConfig.websiteCreateTime).getTime();
  let msPerDay = 24 * 60 * 60 * 1000;
  let daysold = Math.floor(timeold / msPerDay);
  let str = "";
  str += daysold + "天";
  // str += day.getHours() + "时";
  // str += day.getMinutes() + "分";
  // str += day.getSeconds() + "秒";
  state.time = str;
};
const showModal = () => {
  state.visible = true;
};
const beforeUpload = (file: any) => {
  const isPNG = file.type.indexOf("image") != -1;
  if (!isPNG) {
    message.error("只允许图片文件");
  }
  if (state.uploadCode != "123789") {
    message.error("请输入上传码");
    return false
  }
  return isPNG;
};
const handleChange = (info: any) => {
  const status = info.file.status
  if (status === 'done') {
    message.success(`${info.file.name} 上传成功`);
  } else if (status === 'error') {
    message.error(`${info.file.name} 上传失败`);
  }
};
const copyUrl = async (file: any) => {
  //复制
  await toClipboard(file.response.data)
  try {
    //复制
    await toClipboard(file.response.data)
    message.success("复制成功")
  } catch (e) {
    //复制失败
    message.error("复制失败")
  }
};
const handleDrop = (e: any) => {
  console.log(e);
};

const sortArticle = (page: number, type: number) => {
  state.currentType = type
  state.currentPage = page
  getHomeArticleList(page, type)
}

const getHomeArticleList = async (page?: number, type?: number) => {
  const data = await getArticleList(page != undefined ? page : ++state.currentPage, type || state.currentType)
  // //第一页覆盖，其余继续添加到列表里
  if (page == 1) {
    console.log("=")
    state.articleList = data
  } else {
    console.log("push")
    state.articleList.push(...data)
  }
}
onMounted(() => {
  getHomeArticleList(1, 1);
  runTime();
  // state.timer = setInterval(runTime, 1000);
});
onBeforeUnmount(() => {
  //clearInterval(state.timer)
})

</script>
<style scoped lang="less">
.home {
  height: 100%;
  width: 100%;
  background-color: var(--theme-background);

  .theme-switch {
    position: fixed;
    right: 24px;
    bottom: 124px;
  }

  .loading-text {
    margin: 10px;
    text-align: center;

    span:hover {
      color: #49b1f5;
    }

    .dotting {
      color: var(--theme-color);
      /*点点的颜色*/
      display: inline-block;
      width: 3px;
      height: 3px;
      /*点点的高度和宽度*/
      box-shadow: 7px 0 currentColor, 21px 0 currentColor, 35px 0 currentColor,
        49px 0 currentColor, 63px 0 currentColor, 77px 0 currentColor,
        91px 0 currentColor, 105px 0 currentColor, 119px 0 currentColor,
        133px 0 currentColor;
      /* for IE9+, ..., 3个点 */
      animation: dot 4s infinite step-start both;
      /* for IE10+, ... */
    }

    .dotting::before {
      content: "";
    }

    /* for IE9+ 覆盖 IE8 */

    :root .dotting {
      margin-right: 132px;
    }

    /* for IE9+,FF,CH,OP,SF 占据空间*/
    @keyframes dot {
      0% {
        box-shadow: none;
      }

      /* 0个点 */
      33% {
        box-shadow: 7px 0 currentColor;
      }

      /* 1个点 */
      66% {
        box-shadow: 7px 0 currentColor, 21px 0 currentColor;
        /* 2个点 */
      }

      100% {
        box-shadow: 7px 0 currentColor, 21px 0 currentColor, 35px 0 currentColor;
        /* 3个点 */
      }
    }

  }

  .container {
    display: flex;
    margin: calc(100vh - 10px) auto auto 0px;
    animation: fadeInUp 1s;

    @media (min-width: 760px) {
      padding: 40px 15% 0px 15%;
    }

    @media (max-width: 759px) {
      padding: 40px 8px 0px 8px;

      .sider-card {
        display: none;
      }
    }

    .main-card {
      flex: 0.8;

      .post-article-list {
        width: 100%;
        position: relative;

        .post-article-card {
          margin-top: 20px;
        }

        .post-article-card:nth-child(1) {
          margin-top: 0;
        }
      }

    }

    .sider-card {
      flex: 0.2;
      padding: 0 16px;
      height: 100vh;
      position: sticky;
      top: 24px;
      margin-bottom: 20px;

      .web-info {
        padding: 0.25rem;
        font-size: 0.875rem;
        color: var(--theme-color);
      }

      .card-style {
        border-radius: 8px;
        margin-bottom: 16px;
        box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.05);
        border: 1px solid var(--theme-card-color);
        background: var(--theme-card-color);
      }

      .card-title {
        color: var(--theme-color);
      }

      .blog-avatar {
        padding: 20px;
        text-align: center;
        transition: all 0.5s;
      }

      .author-info {
        font-size: 1.375rem;
        font-weight: bold;
        margin-top: 0.625rem;
        text-align: center;
        color: var(--theme-color);
      }

      .blog-avatar:hover {
        transform: rotate(360deg);
      }

      .blog-notice {
        font-size: 0.875rem;
        color: var(--theme-color);
      }

      .blog-info {
        text-align: center;
        color: var(--theme-color);
        font-weight: bold;
      }

      .blog-intro {
        font-size: 0.875rem;
        color: var(--theme-color);
        text-align: center;
      }

    }

    .img__upload {
      position: fixed;
      right: 24px;
      bottom: 24px;
      z-index: 999999;
    }


  }

}


@keyframes header-effect {
  0% {
    opacity: 0;
    filter: alpha(opacity=0);
    transform: translateY(-50px);
  }

  100% {
    opacity: 1;
    filter: none;
    transform: translateY(0);
  }
}

@keyframes scroll-down-effect {
  0% {
    top: 0;
    opacity: 0.4;
    filter: alpha(opacity=40);
  }

  50% {
    top: -5px;
    opacity: 1;
    filter: none;
  }

  100% {
    top: 0;
    opacity: 0.4;
    filter: alpha(opacity=40);
  }
}
</style>
