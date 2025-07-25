<template>
    <el-form ref="ruleFormRef" :model="ruleForm" label-width="auto" :rules="rules">
        <el-form-item label="标题" prop="title">
            <el-input v-model="ruleForm.title" />
        </el-form-item>
        <el-form-item label="作者" prop="author">
            <el-input v-model="ruleForm.author" />
        </el-form-item>
        <el-form-item label="类型" prop="type">
            <el-select v-model="ruleForm.type" placeholder="请选择">
                <el-option label="文章" :value="1" />
                <el-option label="问题" :value="2" />
            </el-select>
        </el-form-item>
        <el-form-item label="关键词" prop="keyword">
            <el-input v-model="ruleForm.keyword" type="textarea" />
        </el-form-item>
        <el-form-item label="内容" prop="content">
            <div style="height:480px">
                <WEditor v-model="ruleForm.content" />
            </div>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="onSubmit(ruleFormRef)">Confirm</el-button>
            <el-button @click="onCancel">Cancel</el-button>
        </el-form-item>
    </el-form>
</template>
<script lang="ts" setup>
import WEditor from "@/components/WEditor.vue";
import { onMounted, reactive, watch, ref } from 'vue';
import { addArticle, updateArticle } from '@/apis';
import { ElMessage } from 'element-plus';
import type { FormInstance, FormRules } from 'element-plus';
interface RuleForm {
    title: string,
    author: string,
    type: string,
    keyword: string,
    content: string,
}
// 定义触发的事件
const emit = defineEmits(['update:modelValue', 'refresh']);
const props = defineProps({
    formData: {
        type: Object,
        default: () => ({})
    }
})
const ruleFormRef = ref<FormInstance>()
const ruleForm = reactive<RuleForm>({
    title: '',
    author: '',
    type: '',
    keyword: '',
    content: '',
})
const rules = reactive<FormRules<RuleForm>>({
    title: [
        {
            required: true,
            trigger: 'blur',
        },
    ],
    type: [
        {
            required: true,
            trigger: 'change',
        },
    ],
    content: [
        {
            required: true,
            trigger: 'blur',
        },
    ],
});

const submitRequest = async () => {
    if (Object.keys(props.formData).length === 0) {
        try {
            const { data } = await addArticle({ ...ruleForm });
            if (data?.code === 1) {
                ElMessage.success('添加成功')
            }

            emit('update:modelValue', false)
            emit('refresh');
            // eslint-disable-next-line @typescript-eslint/no-explicit-any
        } catch (error: any) {
            ElMessage.error(error?.message)
        }
    } else {
        try {
            const { data } = await updateArticle({ ...props.formData, ...ruleForm }, props.formData.id);
            if (data?.code === 1) {
                ElMessage.success('修改成功')
            }

            emit('update:modelValue', false)
            emit('refresh');
            // eslint-disable-next-line @typescript-eslint/no-explicit-any
        } catch (error: any) {
            ElMessage.error(error?.message)
        }
    }
}

const onSubmit = async (formEl: FormInstance | undefined) => {
    if (!formEl) return
    await formEl.validate(async (valid, fields) => {
        if (valid) {
            submitRequest();
        } else {
            console.log('error submit!', fields)
        }
    })
}

const resetForm = (formEl: FormInstance | undefined) => {
    if (!formEl) return
    formEl.resetFields()
}

const onCancel = () => {
    emit('update:modelValue', false)
}

onMounted(() => {
    console.log(props.formData);
    if (Object.keys(props.formData).length === 0) {
        resetForm(ruleFormRef.value);
        return;
    }
    ruleForm.title = props.formData.title;
    ruleForm.author = props.formData.authorName;
    ruleForm.type = props.formData.type;
    ruleForm.keyword = props.formData.keyword;
    ruleForm.content = props.formData.content;

})

watch(() => props.formData, (newVal) => {
    if (Object.keys(newVal).length === 0) {
        resetForm(ruleFormRef.value);
        return;
    }
    ruleForm.title = newVal.title;
    ruleForm.author = newVal.authorName;
    ruleForm.type = newVal.type;
    ruleForm.keyword = newVal.keyword;
    ruleForm.content = newVal?.content || '';
}, { deep: true });

</script>
