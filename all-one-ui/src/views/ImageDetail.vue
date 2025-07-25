<script lang="ts" setup>
import { onMounted, ref } from 'vue';
import { getImageDetail } from '@/apis';
import { useRouter } from 'vue-router';

const props = defineProps({
    id: { type: String, required: true },
})

const labels = ref('');
const type = ref('');

const imgUrl = ref('');
const content = ref('');
const dateStr = ref('');

const getDate = (dateStr: string) => {
    return dateStr.split(' ')[0];
};
const getExcludeDate = (dateStr: string) => {
    return dateStr.split(' ')[1] + dateStr.split(' ')[2];
}

const router = useRouter();
const handleReturn = () => {
    router.back();
}

onMounted(() => {
    getImageDetail(props.id).then((res) => {
        const { data: { data, code } } = res;
        if (code === 1) {
            labels.value = data.labels;
            content.value = data.content;
            imgUrl.value = data.imgUrl;
            type.value = data.type;
            dateStr.value = data.dateStr;
        }
    }).catch((err) => {
        console.log(err);
    });
})
</script>
<template>
    <div class="image-detail-layout">
        <el-container>
            <el-header>
                <div class="home-header-bar" @click="handleReturn">
                    ONE一个
                </div>
            </el-header>
            <el-main>
                <div class="image-detail-title">
                    <img :src="imgUrl" alt="" style="width: 100%;">
                </div>
                <div style="margin-bottom: 30px;">
                    <!-- <div style="float: left;font-size: 20px;height: 40px;line-height: 40px;">{{ labels }}</div> -->
                    <div style="float: right;font-size: 18px;height: 40px;text-align: right;">{{ type }}</div>
                    <div style="clear: both;"></div>
                </div>
                <div class="image-detail-main">
                    <p>{{ content }}</p>
                    <div class="image-detail-main-block">
                        <div style="font-size: 56px;font-weight: 600;line-height: 56px;">{{ getDate(dateStr) }}</div>
                        <div style="font-size: 12px;text-align: center;">{{ getExcludeDate(dateStr) }}</div>
                    </div>
                </div>
            </el-main>
        </el-container>
    </div>

</template>
<style scoped>
.image-detail-layout {
    max-width: 960px;
    margin: 0 auto;
    padding: 0 2rem;
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

.image-detail-keyword {
    padding: 20px 40px;
    color: #999;
    font-size: 14px;
    line-height: 1.67;
}

.image-detail-title {
    font-size: 30px;
}

.image-detail-main {
    background-color: #f2f2f2;
    display: flex;
    margin-bottom: 30px;
    width: 100%;
    padding: 1em;
}

:deep(.image-detail-main img) {
    max-width: 100%;
    max-height: 400px;
}

:deep(.image-detail-main p) {
    font-size: 14px;
    line-height: 1.67;
    margin: 0 10px 10px 0;
    flex: 1;
}

.image-detail-main-block {
    color: #01aef0;
}
</style>