<template>
  <div>
    <div class="comment-title">
      <message-filled />&nbsp;评论
    </div>
    <!-- 评论框 -->
    <div class="comment-input-wrapper">
      <div style="display: flex">
        <div class="comment-input">
          <div>
            <textarea class="comment-textarea" v-model="state.commentContent" placeholder="留下点什么吧..." auto-grow dense />
            <div id="lottie_box" class="lottie-container"></div>
          </div>
          <!-- 操作按钮 -->
          <div class="emoji-container">
            <button @click="insertComment" class="submit-btn v-comment-btn">
              提交
            </button>
            <div class="emoji">
              <!-- 表情框 -->
              <V3Emoji @click-emoji="addEmoji" />
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- 评论详情 -->
    <div>
      <!-- 评论数量 -->
      <div class="count" v-if="state.count > 0">共{{ state.count }}评论</div>
      <div class="count" v-else style="text-align: center">
        该篇文章暂时没有评论
      </div>
      <!-- 评论列表 -->
      <div class="comment-item" v-for="(item, index) of state.commentList" :key="item.id">
        <!-- 头像 -->
        <div class="comment-avatar">
          <a-avatar :size="48" :src="item.avatar" class="avatar"></a-avatar>
        </div>
        <div class="comment-meta">
          <!-- 用户名 -->
          <div class="comment-user">
            <span v-if="!item.webSite">{{ item.nickname }}</span>
            <a v-else :href="item.webSite" target="_blank">
              {{ item.nickname }}
            </a>
            <a-tag color="#f50" style="margin-left: 8px;" v-if="item.userId == 1">博主</a-tag>
            <span class="ip">ip属地：{{ item.ipSource }}</span>
          </div>
          <!-- 信息 -->
          <div class="comment-info">
            <!-- 楼层 -->
            <span style="margin-right: 10px">{{ state.count - index }}楼</span>
            <!-- 发表时间 -->
            <span style="margin-right: 10px">{{ $formatDateFullTime(item.createTime) }}</span>
          </div>
          <!-- 评论内容 -->
          <p v-html="item.commentContent" class="comment-content"></p>
          <!--点赞评论-->
          <div class="comment-action">
            <!-- 点赞 -->
            <!-- <like-filled @click="like(item)" :class="isLike(item.id)" /> -->
            <like-outlined @click="like(item)" :class="isLike(item.id)" />
            <span v-show="item.likeCount > 0">&nbsp;{{ item.likeCount }}</span>
            <dislike-outlined style="margin:0 8px;font-size: 16px;color: var(--theme-color);" />
            <!-- 回复按钮 -->
            <a @click="replyComment(index, item)">
              <comment-outlined style="font-size: 16px;" />&nbsp;回复
            </a>
          </div>
          <!-- <a-divider></a-divider> -->
          <!-- 回复列表 -->
          <div style="display: flex" v-for="reply of item.replyDTOList" :key="reply.id">
            <!-- 头像 -->
            <div class="comment-avatar">
              <a-avatar :size="36" :src="reply.avatar">
              </a-avatar>
            </div>
            <div class="reply-meta">
              <!-- 用户名 -->
              <div class="comment-user">
                <span v-if="!reply.webSite">{{ reply.nickname }}</span>
                <a v-else :href="reply.webSite" target="_blank">
                  {{ reply.nickname }}
                </a>
                <a-tag color="#f50" style="margin-left: 8px;" v-if="reply.userId == 1">博主</a-tag>
                <span class="ip">ip属地：{{ item.ipSource }}</span>
              </div>
              <!-- 信息 -->
              <div class="comment-info">
                <!-- 发表时间 -->
                <span style="margin-right: 10px">
                  {{ $formatDateFullTime(reply.createTime) }}
                </span>
              </div>
              <!-- 回复内容 -->
              <p class="comment-content">
                <!-- 回复用户名 -->
                <template v-if="reply.replyUserId != item.userId">
                  <span v-if="!reply.replyWebSite">
                    @{{ reply.replyNickname }}1
                  </span>
                  <a v-else :href="reply.replyWebSite" target="_blank" class="comment-nickname">
                    @{{ reply.replyNickname }}2
                  </a>
                  ，
                </template>
                <span v-html="reply.commentContent" />
              </p>
              <!--点赞评论-->
              <div style="margin: 8px 0;">
                <!-- 点赞 -->
                <like-outlined @click="like(reply)" :class="isLike(reply.id)" />
                <span v-show="reply.likeCount > 0">&nbsp;{{ reply.likeCount }}</span>
                <dislike-outlined style="margin:0 8px;font-size: 16px;color: var(--theme-color);" />
                <!-- 回复按钮 -->
                <a @click="replyComment(index, reply)">
                  <comment-outlined style="font-size: 16px;" />&nbsp;回复
                </a>
              </div>
            </div>
          </div>
          <!-- 回复数量 -->
          <div style="font-size: 0.75rem; color: #6d757a; margin: 10px" ref="checkRef" v-show="item.replyCount > 3">
            共
            <b>{{ item.replyCount }}</b>
            条回复，
            <span style="color: #00a1d6; cursor: pointer" @click="checkReplies(index, item)">
              点击查看
            </span>
          </div>
          <!-- 回复分页 -->
          <div class="my-pagination" v-show="item.showReplayPaging">
            <a-pagination size="small" :total="item.replyCount" ref="pageRef" v-model:current="item.replayCurrent"
              @change="nextOrLastReplay(index, item)" :page-size="3" />
          </div>
        </div>
      </div>
      <!-- 加载按钮 -->
      <div style="text-align: center">
        <a-pagination size="small" :total="state.count" hideOnSinglePage @change="listComments"
          v-model:current="state.current" />
      </div>
    </div>
    <!--回复弹窗-->
    <replay-modal :showModal="state.isShowReplayModal" :replayState="state.replayState" @closeReplay="closeReplay" />
  </div>
</template>
<script setup lang="ts">
import {
  addComment,
  commentsList,
  repliesList,
  likeComment,
} from "@/api/comment";
import { MessageFilled, LikeOutlined, DislikeOutlined, CommentOutlined } from "@ant-design/icons-vue";
import { message } from "ant-design-vue";
//@ts-ignore
import V3Emoji from "vue3-emoji";
import "@/assets/css/emoji.css";
import { onMounted, reactive, ref } from "vue";
import { useRoute } from "vue-router";
import ReplayModal from "../modal/ReplayModal.vue";
import { useStore } from "@/store/index";
import lottie from "lottie-web";
import * as anData from "@/assets/lottie/plane.json";
import { comment } from "@/types/api/comment"
import { formDateFullTime } from "@/utils/dateFormat"
let animation = {};
const loadAnimation = () => {
  animation = lottie.loadAnimation({
    //@ts-ignore
    container: document.getElementById("lottie_box"), //当前需要渲染的DOM
    renderer: "svg", //渲染方式，默认为svg,还可以渲染为html和canvas
    loop: true, //循环播放
    autoplay: true, //自动播放
    //@ts-ignore
    animationData: anData.default, //本地动画
  });
};
const route = useRoute();
const pinia = useStore();
const pageRef = ref();
const checkRef = ref();
const state = reactive({
  commentContent: "",
  commentList: [] as comment[],
  count: 0,
  current: 1,
  isShowReplayModal: false,
  replayState: {
    type: 1,
    replyUserId: 1,
    parentId: 1,
    topicId: route.params.id,
    nickname: "",
    index: 1,
  }
})

const emit = defineEmits(["getCount"]);
//判断是否点过赞
const isLike = (commentId: number) => {
  //判断是否登录
  if (pinia.isLogin) {
    var commentLikeSet = pinia.loginUserInfo.commentLikeSet;
    //@ts-ignore
    return commentLikeSet.indexOf(commentId) != -1 ? "like-active" : "like";
  } else {
    return "like";
  }
};

//添加emoji表情
const addEmoji = (key: string) => {
  state.commentContent += key;
};
//控制回复模态框
const closeReplay = (index: number) => {
  state.isShowReplayModal = false;
  //index用于定位是哪条评论下的回复
  reloadRelay(index);
};
//查看更多回复
const checkReplies = async (index: number, item: any) => {
  let params = {
    current: 1,
    size: 3,
  };
  const data = await repliesList(params, item.id)
  //隐藏查看按钮
  checkRef.value[index].hidden = true;
  state.commentList[index].replyDTOList = data;
  item.showReplayPaging = true;
};

//回复成功后刷新回复列表
const reloadRelay = async (index: number) => {
  let params = {
    current: state.commentList[index].replayCurrent,
    size: 3,
  };
  const data = await repliesList(params, state.commentList[index].id)
  state.commentList[index].replyCount++;
  //回复大于3条展示分页
  if (state.commentList[index].replyCount > 3) {
    state.commentList[index].showReplayPaging = true;
  }
  state.commentList[index].replyDTOList = data;
};
//查看下一页回复
const nextOrLastReplay = async (index: number, item: any) => {
  let params = {
    current: item.replayCurrent,
    size: 3,
  };
  const data = await repliesList(params, item.id)
  state.commentList[index].replyDTOList = data;
};

//新增评论
const insertComment = async () => {
  //判断登录
  if (!pinia.isLogin) {
    message.error("请登录");
    pinia.openLoginModal()
    return false;
  }
  //判断评论是否为空
  if (state.commentContent === "") {
    message.error("说点啥吧!");
    return false;
  }

  //解析表情
  let reg = /\[.+?\]/g;
  state.commentContent = state.commentContent.replace(reg, function (str) {
    return (
      "<img src= '" +
      //@ts-ignore
      EmojiList[str] +
      "' width='24'height='24' style='margin: 0 1px;vertical-align: text-bottom'/>"
    );
  });
  //接口参数
  const params = {
    commentContent: state.commentContent,
    type: 1,
    topicId: route.params.id,
  };
  state.commentContent = "";
  await addComment(params)
  message.success("评论成功");
  listComments(1, 10);
};
//回复评论
const replyComment = (index: number, item: any) => {
  console.log(item)
  state.replayState.nickname = item.nickname;
  state.replayState.parentId = item.parentId || item.id;
  state.replayState.replyUserId = item.userId;
  state.replayState.index = index;
  state.isShowReplayModal = !state.isShowReplayModal;
};
//点赞
const like = async (item: any) => {
  //判断是否登录
  if (!pinia.isLogin) {
    message.error("没登录还想点赞？");
    return false;
  }
  // 发送请求
  await likeComment(item.id)
  //@ts-ignore
  if (pinia.loginUserInfo.commentLikeSet.indexOf(item.id) != -1) {
    item.likeCount -= 1;
  } else {
    item.likeCount += 1;
  }
  pinia.commentLike(item.id);
};
//获取评论列表
const listComments = async (page: number, pageSize: number) => {
  const params = {
    current: page,
    type: 1,
    topicId: route.params.id,
  };
  const data = await commentsList(params)
  state.count = data.count;
  emit("getCount", state.count);
  if (state.count === null) {
    return false;
  } else {
    data.recordList.map((item) => {
      item.replayCurrent = 1;
      item.showReplayPaging = false;
    });
    state.commentList.length = 0;
    state.commentList.push(...data.recordList);
  }
};
onMounted(() => {
  listComments(1, 10);
  loadAnimation();
});
</script>
<style scoped lang="less">
.emoji-container {
  position: absolute;
  bottom: 0;
  left: 0;



  .submit-btn {
    color: #fff;
    background-color: #49b1f5;
  }

  .submit-btn:hover {
    background-color: #ff7242;
  }

  .v-comment-btn {
    border: 1px solid #ededed;
    border-color: #fff;
    border-radius: 4px;
    text-align: center;
    font-size: 14px;
    transition: all 0.3s;
    outline: none;
  }

  .emoji {
    margin-left: 5px;
  }
}

.lottie-container {
  /*position: sticky;
  width: 40%;
  left: 30%;
  right: 30%;
  height: 400px;*/
  width: 100px;
  position: absolute;
  top: 0;
  right: 10px;
}

.comment-item {
  display: flex;
  width: 100%;
  margin: 16px 0;
}

.like-btn {
  font-size: 15px;
  animation: scaleDraw 1s ease-in-out;
}

.my-pagination {
  padding: 20px;
}

.blogger-tag {
  // background: #ffa51e;
  // font-size: 4px;
  // display: inline-block;
  // border-radius: 2px;
  // color: #fff;
  // padding: 0 5px;
  // margin: 0 6px;
  margin: 4px;
  font-size: 8px;
  color: #ffa51e;
}

.ip {
  margin-left: 8px;
  font-size: 8px;
  color: #b3b3b3;
}

.comment-action {
  margin: 16px 0;
}

.comment-title {
  display: flex;
  align-items: center;
  font-size: 1.25rem;
  font-weight: bold;
  line-height: 40px;
  margin-bottom: 10px;
  color: var(--theme-color);
}

.comment-input-wrapper {
  border: 1px solid var(--theme-color);
  border-radius: 4px;
  padding: 10px;
  margin: 0 0 10px;
  color: var(--theme-color);
}

.comment-textarea {
  background: var(--theme-card-color);
  font-size: 0.875rem;
  color: var(--theme-color);
  outline: none;
  padding: 10px 5px;
  min-height: 122px;
  resize: none;
  width: 100%;
  border-radius: 4px;
  border-style: none;
}

.comment-input {
  position: relative;
  width: 100%;
  height: 200px;
}

.count {
  padding: 25px 0;
  line-height: 1.75;
  font-size: 1.25rem;
  font-weight: bold;
}

.comment-meta {
  margin-left: 0.8rem;
  width: 100%;
  border-bottom: 1px dashed #f5f5f5;
}

.reply-meta {
  margin-left: 0.8rem;
  width: 100%;
  margin-bottom: 0.8rem;
}

.comment-user {
  font-size: 16px;
  line-height: 1.75;
}

.comment-user a {
  color: #1abc9c !important;
  font-weight: 500;
  transition: 0.3s all;
}

.comment-nickname {
  text-decoration: none;
  color: #1abc9c !important;
  font-weight: 500;
}

.comment-info {
  line-height: 1.75;
  font-size: 0.75rem;
  color: #b3b3b3;
}

.reply-btn {
  cursor: pointer;
  // float: right;
  color: #ef2f11;
}

.comment-content {
  font-size: 0.875rem;
  line-height: 1.75;
  padding-top: 0.625rem;
  white-space: pre-line;
  word-wrap: break-word;
  word-break: break-all;
  color: var(--theme-color);
}

.comment-avatar {
  position: relative;
  display: flex;
  flex-direction: column;

  .tag {
    text-align: center;
    margin-top: 8px;
    background-color: #ef2f11;
    color: #ededed;
    //padding: 2px;
    font-size: 2px;
    border-radius: 4px;
  }

  .avatar {
    transition: all 0.5s;
  }

  .avatar:hover {
    transform: rotate(360deg);
  }
}



.like {
  cursor: pointer;
  font-size: 1.1rem;
  color: var(--theme-color);
}

.like:hover {
  animation: scaleDraw 1s ease-in-out;
  color: #eb5055;
}

.like-active {
  cursor: pointer;
  font-size: 1.2rem;
  color: #eb5055;
}

.load-wrapper {
  margin-top: 10px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.load-wrapper button {
  background-color: #49b1f5;
  color: #fff;
}

/* 点赞动画效果 */
@keyframes scaleDraw {
  0% {
    transform: scale(1);
    /*开始为原始大小*/
  }

  25% {
    transform: scale(1.2);
    /*放大1.1倍*/
  }

  50% {
    transform: scale(1);
  }

  75% {
    transform: scale(1.2);
  }
}
</style>
