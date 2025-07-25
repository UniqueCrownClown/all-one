<script lang="ts" setup>
import { onMounted, ref } from 'vue';
import { getArticleDetail } from '@/apis';
import { useRouter } from 'vue-router';

const props = defineProps({
    id: { type: String, required: true },
})

const keyword = ref('');
const title = ref('');
const content = ref('');

const router = useRouter();
const handleReturn = (event: MouseEvent) => {
    event.preventDefault();
    router.back();
}

onMounted(() => {
    getArticleDetail(props.id).then((res) => {
        const { data: { data, code } } = res;
        if (code === 1) {
            title.value = data.title;
            content.value = data.content;
            keyword.value = data.keyword;

        }
    }).catch((err) => {
        console.log(err);
    });
})

</script>
<template>
    <div class="article-detail-layout">
        <el-container>
            <el-header>
                <div class="home-header-bar" @click="handleReturn">
                    ONE一个
                </div>
            </el-header>
            <el-container>
                <el-header>
                    <div class="article-detail-keyword">{{ keyword }}</div>
                </el-header>
                <el-main>
                    <div class="article-detail-title">{{ title }}</div>
                    <div class="article-detail-main" v-html="content"></div>
                </el-main>
            </el-container>
        </el-container>
    </div>

</template>
<style scoped>
.article-detail-layout {
    max-width: 960px;
    margin: 0 auto;
    padding: 2rem;
}

.home-header-bar {
    margin: 10px auto;
    background-color: #01aef0;
    height: 50px;
    line-height: 50px;
    color: #fff;
    text-align: center;
    font-size: 30px;
    cursor: pointer;
    caret-color: transparent;
}

.home-main-block-header {
    background-color: #01aef0;
    height: 50px;
    line-height: 50px;
    color: #fff;
    text-align: center;
    font-size: 24px;
}

.article-detail-keyword {
    padding: 20px 40px;
    color: #999;
    font-size: 14px;
    line-height: 1.67;
}

.article-detail-title {
    font-size: 30px;
}

.article-detail-main {}

:deep(.article-detail-main img) {
    max-width: 100%;
    max-height: 400px;
}

:deep(.article-detail-main p) {
    font-size: 14px;
    line-height: 1.67;
    margin: 0 0 10px;
}
</style>