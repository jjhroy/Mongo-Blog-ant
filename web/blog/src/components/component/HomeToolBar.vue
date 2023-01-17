<!-- 公告栏组件 -->
<template>
    <div class="tool-bar">
        <div :class="`sort ${state.currentIndex == 1 ? 'active' : ''}`" @click="articleSort(1, 1)">
            <icon-font type="icon-daohang_008" class="icon" /><span>时间优先</span>
        </div>
        <div :class="`sort ${state.currentIndex == 2 ? 'active' : ''}`" @click="articleSort(1, 2)">
            <icon-font type="icon-daohang_remen-xuanzhong" class="icon" /><span>热度优先</span>
        </div>
        <div :class="`sort ${state.currentIndex == 3 ? 'active' : ''}`" @click="articleSort(1, 3)">
            <icon-font type="icon-daohang_015" class="icon" /><span>点赞优先</span>
        </div>
        <div class="notice">
            <bell-filled style="color: rgb(240, 17, 17);" />&nbsp;<span style="margin-right: 4px;">公告</span>
            <div ref="wrap" class="notice-bar__wrap">
                <div ref="content" class="notice-bar__content" :style="state.contentStyle">{{ text }}</div>
            </div>
        </div>
    </div>
</template>
  
<script setup lang="ts">
import { onBeforeUnmount, onMounted, reactive, ref, watch } from 'vue';
import { createFromIconfontCN, BellFilled } from "@ant-design/icons-vue";
import config from "@/config/config";
import { useStore } from "@/store/index";
const pinia = useStore();
const IconFont = createFromIconfontCN({
    scriptUrl: config.ICON_FONT_URL,
});
const wrap = ref()
const content = ref()
const state = reactive({
    contentStyle: {
        transitionDuration: '0s',
        transform: 'translateX(0px)'
    },
    wrapWidth: 0,
    contentWidth: 0,
    time: 0,
    timer: null as any,
    convertSpeed: 1,
    currentIndex: 1

})
const props = defineProps({
    sub_title: {
        type: String,
        default: '通知：'
    },
    text: {
        type: String,
        default: ''
    },
    speed: {
        type: Number,
        default: 50
    },
    defaultWidth: {
        type: Number,
        default: 375
    }
})
const emit = defineEmits(["sortArticle"]);
const articleSort = (page: number, type: number) => {
    state.currentIndex = type
    emit("sortArticle", page, type)
}

const init = () => {
    const _width = window.innerWidth
    state.convertSpeed = _width / props.defaultWidth * props.speed  // 根据分辨率转化成不同的速度
    state.wrapWidth = wrap.value.offsetWidth
    state.contentWidth = content.value.offsetWidth
    startAnimate()
    state.timer = setInterval(() => {
        startAnimate()
    }, state.time * 1000)
}
const startAnimate = () => {
    state.contentStyle.transitionDuration = '0s'
    state.contentStyle.transform = 'translateX(' + state.wrapWidth + 'px)'
    state.time = (state.wrapWidth + state.contentWidth) / state.convertSpeed
    setTimeout(() => {
        state.contentStyle.transitionDuration = state.time + 's'
        state.contentStyle.transform = 'translateX(-' + state.contentWidth + 'px)'
    }, 200)
}
onMounted(() => {
    init()
})
onBeforeUnmount(() => {
    clearInterval(state.timer)
    state.timer = null
})


</script>
<style scoped lang='less'>
.tool-bar {
    color: var(--theme-color);
    background-color: var(--theme-card-color);
    width: 100%;
    padding: 8px 16px;
    margin-bottom: 8px;
    border-radius: 8px;
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.05);
    display: flex;

    .icon {
        font-size: 24px;
        margin-right: 4px;
    }

    .sort {
        display: flex;
        align-items: center;
        margin-right: 8px;
    }

    .active {
        span {
            color: var(--theme-footer-left);
        }
    }

    .notice {
        flex: 2;
        display: flex;
        align-items: center;
    }

    .notice-bar__wrap {
        position: relative;
        display: flex;
        flex: 1;
        height: 100%;
        align-items: center;
        overflow: hidden;

        .notice-bar__content {
            color: #222;
            white-space: nowrap;
            transition-timing-function: linear;
        }
    }
}
</style>
  
  
  