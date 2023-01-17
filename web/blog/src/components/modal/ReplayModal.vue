<template>
  <div>
    <a-modal v-model:visible="props.showModal" :footer="null" :closable="false" :onCancel="closeModal"
      :bodyStyle="{ 'padding': '0', 'border-radius': '8px' }" centered>
      <div class="reply-input-wrapper" ref="reply">
        <textarea class="comment-textarea" :placeholder="'回复 @' + props.replayState.nickname + '：'" auto-grow dense
          v-model="state.commentContent" />
        <div class="emoji-container">
          <div style="margin-left: auto">
            <button @click="cancelReply" class="cancel-btn v-comment-btn">
              取消
            </button>
            <button @click="insertReply" class="upload-btn v-comment-btn">
              提交
            </button>
          </div>
        </div>
        <!-- 表情框 -->
        <V3Emoji @click-emoji="addEmoji" />
      </div>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { comment,replayStates } from "@/types/api/comment"
import { message } from "ant-design-vue";
import { addComment } from "@/api/comment"
import { reactive } from "vue";
//@ts-ignore
import V3Emoji from "vue3-emoji";
import "@/assets/css/emoji.css";
import { useStore } from "@/store/index";

const pinia = useStore();

const state = reactive({
  index: 0,
  nickname: "",
  replyUserId: null,
  parentId: null,
  commentContent: "",
});
const props = defineProps<{
  replayState: replayStates
  showModal: boolean
}>()

const emit = defineEmits(["closeReplay"]);

const closeModal = () => {
  emit("closeReplay", props.replayState.index);
};

const cancelReply = () => {
  emit("closeReplay", props.replayState.index);
};
const insertReply = async () => {
  //判断是否登录
  if (pinia.loginUserInfo.id === undefined) {
    message.error("没登录还想评论？");
    return false;
  }
  //判断评论是否为空
  if (state.commentContent === "") {
    message.error("说点啥吧!");
    return false;
  }
  //解析表情
  let reg = /\[.+?\]/g;
  state.commentContent = state.commentContent.replace(
    reg,
    function (str) {
      return (
        "<img src= '" +
        //@ts-ignore
        EmojiList[str] +
        "' width='24'height='24' style='margin: 0 1px;vertical-align: text-bottom'/>"
      );
    }
  );
  var comment = {
    type: props.replayState.type,
    commentContent: state.commentContent,
    topicId: props.replayState.topicId,
  };
  await addComment(comment)
  state.commentContent = "";
  message.success("回复成功");
  closeModal();
}
const addEmoji = (text: string) => {
  state.commentContent += text;
};
</script>

<style scoped>
.reply-input-wrapper {
  border: 1px solid #90939950;
  border-radius: 4px;
  padding: 10px;
  margin: 0 0 10px;
}

.blogger-tag {
  background: #ffa51e;
  font-size: 12px;
  display: inline-block;
  border-radius: 2px;
  color: #fff;
  padding: 0 5px;
  margin-left: 6px;
}

.comment-title {
  display: flex;
  align-items: center;
  font-size: 1.25rem;
  font-weight: bold;
  line-height: 40px;
  margin-bottom: 10px;
}

.comment-input-wrapper {
  border: 1px solid #90939950;
  border-radius: 4px;
  padding: 10px;
  margin: 0 0 10px;
}

.comment-textarea {
  font-size: 0.875rem;
  color: #555 !important;
  outline: none;
  padding: 10px 5px;
  min-height: 122px;
  resize: none;
  width: 100%;
  border-radius: 4px;
  border-style: none;
  background: url(https://static.talkxj.com/config/commentBack.webp) 100% 100% no-repeat;
}

.upload-btn {
  color: #fff;
  background-color: #49b1f5;
}

.upload-btn:hover {
  background-color: #ff7242;
}

.v-comment-btn {
  border: 1px solid #ededed;
  border-color: #fff;
  border-radius: 4px;
  text-align: center;
  padding: 6px 16px;
  font-size: 14px;
  transition: all 0.3s;
  outline: none;
}

.comment-input {
  position: relative;
}

.count {
  padding: 5px;
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
}

.comment-user {
  font-size: 14px;
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
  float: right;
  color: #ef2f11;
}

.comment-content {
  font-size: 0.875rem;
  line-height: 1.75;
  padding-top: 0.625rem;
  white-space: pre-line;
  word-wrap: break-word;
  word-break: break-all;
}

.comment-avatar {
  transition: all 0.5s;
}

.comment-avatar:hover {
  transform: rotate(360deg);
}

.like {
  cursor: pointer;
  font-size: 0.875rem;
}

.like:hover {
  color: #eb5055;
}

.like-active {
  cursor: pointer;
  font-size: 0.875rem;
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
</style>
