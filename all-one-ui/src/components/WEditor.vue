<script lang="ts" setup>
import '@wangeditor/editor/dist/css/style.css' // 引入 css

import { onBeforeUnmount, ref, shallowRef, onMounted, watch, nextTick } from 'vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue';

import { defineProps, defineEmits } from 'vue';

// 定义接收的属性
const props = defineProps<{
    modelValue: string;
    readOnly?: {
        type: boolean,
        default: false
    }
}>();

// 定义触发的事件
const emit = defineEmits(['update:modelValue']);

// 编辑器实例，必须用 shallowRef
const editorRef = shallowRef()

// 内容 HTML
const valueHtml = ref(props?.modelValue || "")

// 监听 modelValue 的变化，更新 valueHtml
watch(() => props.modelValue, (newValue) => {
    nextTick(() => {
        valueHtml.value = newValue;
    })

}, { immediate: true });

// 监听 valueHtml 的变化，触发 update:modelValue 事件
watch(valueHtml, (newValue) => {
    emit('update:modelValue', newValue);
});

// 模拟 ajax 异步获取内容
onMounted(() => {
    // setTimeout(() => {
    //     valueHtml.value = '<p>模拟 Ajax 异步设置内容</p>'
    // }, 1500)
})

const toolbarConfig = {}
const editorConfig = { placeholder: '请输入内容...', readOnly: props.readOnly || false }

// 组件销毁时，也及时销毁编辑器
onBeforeUnmount(() => {
    const editor = editorRef.value
    if (editor == null) return
    editor.destroy()
})

const handleCreated = (editor) => {
    editorRef.value = editor // 记录 editor 实例，重要！
}

const mode = ref('default');// 或 'simple'
</script>
<template>
    <div style="border: 1px solid #ccc;height:100%;">
        <Toolbar style="border-bottom: 1px solid #ccc" :editor="editorRef" :defaultConfig="toolbarConfig"
            :mode="mode" />
        <Editor style="height: calc(100% - 40px); overflow-y: hidden;" v-model="valueHtml" :defaultConfig="editorConfig"
            :mode="mode" @onCreated="handleCreated" />
    </div>
</template>