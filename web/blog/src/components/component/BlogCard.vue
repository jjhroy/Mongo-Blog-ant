<template>
    <div class="card">
        <div class="card-header">
            <slot name="icon"></slot>
            <span class="card-title">{{ title }}</span>
        </div>
        <div class="card-body">
            <slot name="slot-body">出现一点小问题....</slot>
        </div>
    </div>
</template>

<script setup lang="ts">
const props = defineProps({
    title: String
})
</script>

<style scoped>
.card-title {
    color: var(--theme-color);
}

.card {
    background: var(--theme-card-color);
    border-radius: 8px;
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.05);
    padding: 20px 24px;
    width: 100%;
    margin-top: 25px;
    box-sizing: border-box;
}

.card-header {
    text-align: left !important;
    margin-bottom: 20px;
}

.header-icon {
    font-size: 18px;
    margin-right: 10px;
}

.card-header span {
    font-size: 17px;
}
</style>