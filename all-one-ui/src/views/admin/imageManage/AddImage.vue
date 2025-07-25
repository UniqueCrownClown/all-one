<template>
    <el-form ref="ruleFormRef" :model="ruleForm" label-width="auto" :rules="rules">
        <el-form-item label="图片路径" prop="imgUrl">
            <el-input v-model="ruleForm.imgUrl" />
        </el-form-item>
        <el-form-item label="类型" prop="type">
            <el-input v-model="ruleForm.type" />
        </el-form-item>
        <el-form-item label="所属日期" prop="dateStr">
            <el-date-picker v-model="ruleForm.dateStr" type="date" placeholder="Pick a day" />
        </el-form-item>
        <el-form-item label="内容" prop="content">
            <el-input v-model="ruleForm.content" type="textarea" />
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="onSubmit(ruleFormRef)">Confirm</el-button>
            <el-button @click="onCancel">Cancel</el-button>
        </el-form-item>
    </el-form>
</template>
<script lang="ts" setup>
import { onMounted, reactive, watch, ref } from 'vue';
import { addImage, updateImage } from '@/apis';
import dayjs from 'dayjs';
import { ElMessage } from 'element-plus';
import type { FormInstance, FormRules } from 'element-plus';
interface RuleForm {
    imgUrl: string,
    type: string,
    dateStr: string,
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
    imgUrl: '',
    type: "摄影",
    dateStr: '',
    content: '',
})

const rules = reactive<FormRules<RuleForm>>({
    imgUrl: [
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
    dateStr: [
        {
            type: 'date',
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
})
const submitRequest = async () => {
    if (Object.keys(props.formData).length === 0) {
        try {
            const { data } = await addImage({ ...ruleForm, dateStr: dayjs(ruleForm.dateStr).format('DD MMM YYYY') });
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
            const { data } = await updateImage({ ...props.formData, ...form, dateStr: dayjs(ruleForm.dateStr).format('DD MMM YYYY') });
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
    ruleForm.imgUrl = props.formData.imgUrl;
    ruleForm.type = props.formData.type;
    ruleForm.dateStr = props.formData.dateStr;
    ruleForm.content = props.formData.content;
})

watch(() => props.formData, (newVal) => {
    if (Object.keys(newVal).length === 0) {
        resetForm(ruleFormRef.value);
        return;
    }
    ruleForm.imgUrl = newVal.imgUrl;
    ruleForm.type = newVal.type;
    ruleForm.dateStr = newVal.dateStr;
    ruleForm.content = newVal.content;
}, { deep: true });

</script>
