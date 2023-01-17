<template>
    <div class="catalog-card">
        <div class="catalog-card-header">
            <div @click="getTitles()">
                <menu-outlined /> &nbsp;<span>目录</span>
            </div>
            <span class="progress">{{ state.progress }}</span>
        </div>

        <div class="catalog-content">
            <div v-for="title in state.titles" :key="title.id" @click="scrollToView(title.scrollTop)" :class="[
                'catalog-item',
                state.currentTitle.id == title.id ? 'active' : 'not-active',
            ]" :style="{ marginLeft: title.level * 20 + 'px' }" v-show="title.isVisible" :title="title.rawName">
                {{ title.name }}
            </div>
        </div>
    </div>
</template>

<script setup>
import { reactive } from "vue";
import { MenuOutlined } from '@ant-design/icons-vue'
const state = reactive({
    titles: [],
    currentTitle: [],
    progress: 0
})
// 获取目录的标题
const getTitles = () => {
    let titles = [];
    let levels = ["h1", "h2", "h3"];
    let articleElement = document.querySelector(".article-content");
    if (!articleElement) {
        return titles;
    }
    let elements = Array.from(articleElement.querySelectorAll("*"));
    // 调整标签等级
    let tagNames = new Set(
        elements.map((el) => el.tagName.toLowerCase())
    );
    for (let i = levels.length - 1; i >= 0; i--) {
        if (!tagNames.has(levels[i])) {
            levels.splice(i, 1);
        }
    }
    let serialNumbers = levels.map(() => 0);
    for (let i = 0; i < elements.length; i++) {
        const element = elements[i];
        let tagName = element.tagName.toLowerCase();
        let level = levels.indexOf(tagName);
        if (level == -1) continue;

        let id = tagName + "-" + element.innerText + "-" + i;
        let node = {
            id,
            level,
            parent: null,
            children: [],
            rawName: element.innerText,
            scrollTop: element.offsetTop,
        };

        if (titles.length > 0) {
            let lastNode = titles.at(-1);

            // 遇到子标题
            if (lastNode.level < node.level) {
                node.parent = lastNode;
                lastNode.children.push(node);
            }
            // 遇到上一级标题
            else if (lastNode.level > node.level) {
                serialNumbers.fill(0, level + 1);
                let parent = lastNode.parent;
                while (parent) {
                    if (parent.level < node.level) {
                        parent.children.push(node);
                        node.parent = parent;
                        break;
                    }
                    parent = parent.parent;
                }
            }
            // 遇到平级
            else if (lastNode.parent) {
                node.parent = lastNode.parent;
                lastNode.parent.children.push(node);
            }
        }

        serialNumbers[level] += 1;
        let serialNumber = serialNumbers.slice(0, level + 1).join(".");

        node.isVisible = node.parent == null;
        node.name = serialNumber + ". " + element.innerText;
        titles.push(node);
    }
    return titles;
}

// 监听滚动事件并更新样式
window.addEventListener("scroll", function () {
    state.progress =
        parseInt(
            (window.scrollY / document.documentElement.scrollHeight) *
            100
        ) + "%";

    let visibleTitles = [];
    if (state.titles.length === 0) {
        state.titles.push(...getTitles());
    }
    for (let i = state.titles.length - 1; i >= 0; i--) {
        const title = state.titles[i];
        if (title.scrollTop <= window.scrollY) {
            if (state.currentTitle.id === title.id) return;

            Object.assign(state.currentTitle, title);

            // 展开节点
            setChildrenVisible(title, true);
            visibleTitles.push(title);

            // 展开父节点
            let parent = title.parent;
            while (parent) {
                setChildrenVisible(parent, true);
                visibleTitles.push(parent);
                parent = parent.parent;
            }

            // 折叠其余节点
            for (const t of state.titles) {
                if (!visibleTitles.includes(t)) {
                    setChildrenVisible(t, false);
                }
            }
            return;
        }
    }
});

// 设置子节点的可见性
const setChildrenVisible = (title, isVisible) => {
    for (const child of title.children) {
        child.isVisible = isVisible;
    }
}

// 滚动到指定的位置
const scrollToView = (scrollTop) => {
    window.scrollTo({ top: scrollTop, behavior: "smooth" });
}
</script>

<style lang="less" scoped>
.catalog-card {
    background: var(--theme-card-color);
    border-radius: 8px;
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.05);
    padding: 20px 24px;
    width: 100%;
    margin-top: 25px;
    box-sizing: border-box;
}

.catalog-card-header {
    text-align: left !important;
    margin-bottom: 15px;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.catalog-icon {
    font-size: 18px;
    margin-right: 10px;
    color: dodgerblue;
}

.catalog-card-header div>span {
    font-size: 17px;
    color: var(--theme-color);
}

.progress {
    color: #a9a9a9;
    font-style: italic;
    font-size: 140%;
}

.catalog-content {
    max-height: calc(100vh - 120px);
    overflow: auto;
    margin-right: -24px;
    padding-right: 20px;
}

.catalog-item {
    color: var(--theme-color);
    margin: 5px 0;
    line-height: 28px;
    cursor: pointer;
    transition: all 0.2s ease-in-out;
    font-size: 14px;
    padding: 2px 6px;
    display: -webkit-box;
    overflow: hidden;
    text-overflow: ellipsis;
    -webkit-line-clamp: 1;
    -webkit-box-orient: vertical;

    &:hover {
        color: var(--theme-color);
    }
}

.active {
    background-color: #0c82e9;
    color: white;

    &:hover {
        background-color: #0c82e9;
        color: white;
    }
}
</style>