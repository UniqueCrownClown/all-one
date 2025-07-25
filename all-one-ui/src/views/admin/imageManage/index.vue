<template>
    <div class="common-layout">
        <el-container>
            <el-header>
                <el-button @click="handleAdd">新增图片</el-button>
            </el-header>
        </el-container>
        <el-main>
            <el-table :data="tableData" stripe style="width: 100%">
                <el-table-column prop="imgUrl" label="图片路径" width="180" />
                <el-table-column prop="type" label="类型" width="100" />
                <el-table-column prop="content" label="内容" />
                <el-table-column prop="dateStr" label="所属日期" width="100" />
                <el-table-column prop="createdTime" label="创建时间" width="150px">
                    <template #default="scope">
                        {{ dayjs(scope.row.createdTime).format('YYYY-MM-DD HH:mm:ss') }}
                    </template>
                </el-table-column>
                <el-table-column label="操作">
                    <template #default="scope">
                        <el-button size="small" @click="handleEdit(scope.$index, scope.row)">
                            Edit
                        </el-button>
                        <el-button size="small" type="danger" @click="handleDelete(scope.$index, scope.row)">
                            Delete
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div style="margin:10px;display: flex;justify-content: flex-end;">
                <el-pagination layout="total, sizes,prev, pager, next" v-model:current-page="currentPage" :total="total"
                    @change="paginationChange" />
            </div>
        </el-main>
        <el-dialog :title="title" v-model="openAdd" width="80%" top="5vh" append-to-body>
            <add-image v-model="openAdd" @refresh="initData(currentPage, 10)" :formData="currentRow" />
        </el-dialog>
    </div>
</template>
<script lang="ts" setup>
import { ref, onMounted } from "vue";
import dayjs from 'dayjs';
import { ElMessage, ElMessageBox } from 'element-plus';
import { deleteImage, getImageListByPage } from "@/apis";
import AddImage from "./AddImage.vue";
const tableData = ref([]);
const total = ref(0);
const openAdd = ref<boolean>(false);
const title = ref<string>("");
const currentRow = ref<Record<string, string>>({});
const handleAdd = () => {
    title.value = "新增";
    currentRow.value = {};
    openAdd.value = true;
}
const handleEdit = (index: number, row: Record<string, string>) => {
    title.value = "修改";
    currentRow.value = row;
    openAdd.value = true;
}
const handleDelete = (index: number, row: Record<string, string>) => {
    ElMessageBox.confirm(
        '确定删除吗？',
        'Warning',
        {
            confirmButtonText: 'OK',
            cancelButtonText: 'Cancel',
            type: 'warning',
        }
    )
        .then(async () => {
            try {
                const { data } = await deleteImage(row.id);
                if (data?.code === 1) {
                    ElMessage({
                        message: "删除成功",
                        type: 'success',
                    });
                    currentPage.value = 1;
                    initData(currentPage.value, 10);
                }
            } catch (e) {
                console.log(e)
            }
        })
        .catch(() => {
            ElMessage({
                type: 'info',
                message: 'Delete canceled',
            })
        })
}
const currentPage = ref(1);
const paginationChange = (currentPage: number, pageSize: number) => {
    initData(currentPage, pageSize)
}
onMounted(() => {
    initData(1, 10);
})

const initData = async (currentPage: number, pageSize: number) => {
    const { data: articlesData } = await getImageListByPage(currentPage, pageSize);
    if (articlesData?.code === 1) {
        tableData.value = articlesData.data.records;
        total.value = articlesData.data.total;
    }
}
</script>