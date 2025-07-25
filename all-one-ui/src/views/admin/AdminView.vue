<template>
    <div class="common-layout" style="height: 100vh;">
        <el-container>
            <el-header>
                <div style="height: 100%;display: flex;align-items: center;justify-content: space-between;">
                    <h2>ONE管理端</h2>
                    <el-dropdown @command="handleCommand">
                        <span class="el-dropdown-link">
                            我的
                            <el-icon class="el-icon--right">
                                <arrow-down />
                            </el-icon>
                        </span>
                        <template #dropdown>
                            <el-dropdown-menu>
                                <el-dropdown-item command="logout">退出登录</el-dropdown-item>
                            </el-dropdown-menu>
                        </template>
                    </el-dropdown>
                </div>
            </el-header>
            <el-container>
                <el-aside width="200px">
                    <el-row class="tac">
                        <el-col :span="24">
                            <el-menu active-text-color="#ffd04b" background-color="#545c64"
                                class="el-menu-vertical-demo" default-active="image" text-color="#fff"
                                @select="handleSelect">
                                <el-menu-item index="image">
                                    <el-icon>
                                        <document />
                                    </el-icon>
                                    <span>图片管理</span>
                                </el-menu-item>
                                <el-menu-item index="article">
                                    <el-icon>
                                        <setting />
                                    </el-icon>
                                    <span>文章问题管理</span>
                                </el-menu-item>
                            </el-menu>
                        </el-col>
                    </el-row>
                </el-aside>
                <el-main>
                    <RouterView />
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<script lang="ts" setup>
import {
    Document,
    Setting,
    ArrowDown
} from '@element-plus/icons-vue'
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const handleSelect = (key: string) => {
    // console.log(key, keyPath)
    router.push({
        path: key,
    })

}
// 处理下拉菜单命令
const handleCommand = (command: string) => {
    if (command === 'logout') {
        // 清除登录状态
        localStorage.removeItem('isLoggedIn');
        // 跳转到登录页面
        router.push({ name: 'login' });
    }
}
onMounted(() => {
    handleSelect("image")
})
</script>
<style lang="scss" scoped>
.el-dropdown-link {
    cursor: pointer;
    color: var(--el-color-primary);
    display: flex;
    align-items: center;
}
</style>