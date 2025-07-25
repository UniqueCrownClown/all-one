<template>
    <el-container style="height: calc(100vh - 80px);">
        <el-header>
            <div>{{ keyword }}</div>
        </el-header>
        <el-main>
            <div style="height:100%">
                <WEditor v-model="hello" :read-only="true" />
            </div>
        </el-main>
    </el-container>
</template>
<script lang="ts" setup>
import WEditor from "@/components/WEditor.vue";
import { defineProps, ref, watch } from 'vue';

// 定义接收的属性
const props = defineProps<{
    keyword: string;

    content: string;
}>();

const hello = ref(props.content);

// 监听 props.content 的变化，更新 hello 的值
watch(() => props.content, (newValue) => {
    hello.value = newValue;
});
</script>