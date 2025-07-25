<template>
    <div class="common-layout">
        <el-container>
            <el-header>
                <div style="display: flex;">
                    <el-select v-model="type" placeholder="请选择" style="width: 200px;">
                        <el-option label="文章" value="1" />
                        <el-option label="问题" value="2" />
                    </el-select>

                    <el-button @click="handleAdd" style="margin-left: 20px;">新增</el-button>
                </div>

            </el-header>
        </el-container>
        <el-main>
            <el-table :data="tableData" stripe style="width: 100%">
                <el-table-column prop="title" label="标题" width="180" />
                <el-table-column prop="type" label="类型" width="180">
                    <template #default="scope">
                        {{ scope.row.type === 1 ? "文章" : "问题" }}
                    </template>
                </el-table-column>
                <el-table-column prop="author" label="作者" width="180" />
                <el-table-column prop="createdTime" label="创建时间" width="150px">
                    <template #default="scope">
                        {{ dayjs(scope.row.createdTime).format('YYYY-MM-DD HH:mm:ss') }}
                    </template>
                </el-table-column>
                <el-table-column label="操作">
                    <template #default="scope">
                        <el-button size="small" @click="handleDetail(scope.$index, scope.row)">
                            详情
                        </el-button>
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
                <el-pagination layout="total, sizes,prev, pager, next" :total="total" v-model:current-page="currentPage"
                    @change="paginationChange" />
            </div>
        </el-main>
        <el-dialog :title="title" v-model="openAdd" :fullscreen="true" append-to-body>
            <add-article v-model="openAdd" @refresh="initData(currentPage, 10)" :formData="currentRow" />
        </el-dialog>
        <el-dialog title="文章详情" v-model="openDetail" :fullscreen="true" append-to-body>
            <detail-article :content="currentContent" :keyword="currentKeyword" />
        </el-dialog>
    </div>
</template>
<script lang="ts" setup>
import { ref, onMounted, watch } from "vue";
import dayjs from "dayjs";
import { ElMessage, ElMessageBox } from 'element-plus';
import { getArticleListByPage, getArticleDetail, deleteArticle } from "@/apis";
import AddArticle from "./AddArticle.vue";
import DetailArticle from "./DetailArticle.vue";
const tableData = ref([]);
const total = ref(0);
const openAdd = ref<boolean>(false);
const openDetail = ref<boolean>(false);
const title = ref<string>("新增");
const type = ref<string>("1");
const currentRow = ref<Record<string, string>>({});
const handleAdd = () => {
    title.value = "新增";
    currentRow.value = {};
    openAdd.value = true;
}
const handleEdit = async (index: number, row: Record<string, string>) => {
    const res = await getArticleDetail(row.id);
    title.value = "修改";
    currentRow.value = { ...res.data.data };
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
                const { data } = await deleteArticle(row.id);
                if (data?.code === 1) {
                    ElMessage({
                        message: "删除成功",
                        type: 'success',
                    });
                    currentPage.value = 1;
                    initData(currentPage.value, 10, { type: type.value });
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
const currentContent = ref("");
const currentKeyword = ref("");
const handleDetail = async (index: number, row: Record<string, string>) => {
    const res = await getArticleDetail(row.id);
    currentContent.value = res.data.data.content;
    currentKeyword.value = res.data.data.keyword;
    openDetail.value = true;
}
const currentPage = ref(1);
const paginationChange = (currentPage: number, pageSize: number) => {
    initData(currentPage, pageSize, { type: type.value })
}
onMounted(() => {
    const entity = {
        type: type.value
    }
    initData(1, 10, entity);
})

const initData = async (currentPage: number, pageSize: number, entity: Record<string, string>) => {

    const { data: articlesData } = await getArticleListByPage(currentPage, pageSize, entity);
    if (articlesData?.code === 1) {
        tableData.value = articlesData.data.records;
        total.value = articlesData.data.total;
    }
}

watch(() => type.value, (newVal) => {
    const entity = {
        type: newVal
    }
    initData(1, 10, entity);
})
</script>