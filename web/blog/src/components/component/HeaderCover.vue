<template>
    <div class="article-cover">
        <slot></slot>
    </div>
</template>

<style scoped>
.article-cover {
    background-size: cover;
    background-repeat: no-repeat;
    background-position: center;
    /* background-image: url(https://api.yimian.xyz/img?type=moe&size=1920x*); */
    /* background-image: url(https://api.vvhan.com/api/acgimg); */
    background-image: url(http://www.dmoe.cc/random.php);
    /* background-image: url(https://tuapi.eees.cc/api.php?category=dongman&type=302); */
    height: 400px;
    width: 100%;
    animation: fadeUpInCover 1s;
}

.article-cover::before {
    width: 100%;
    height: 400px;
    background-color: rgba(0, 0, 0, 0.3);
    content: "";
    position: absolute;
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
</style>