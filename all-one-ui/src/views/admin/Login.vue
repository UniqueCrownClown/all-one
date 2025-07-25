<template>
    <div class="login-container">
        <el-card class="login-card">
            <h2 class="login-title">ONE登录</h2>
            <el-form :model="loginForm" :rules="rules" ref="loginFormRef" label-width="80px">
                <el-form-item label="用户名" prop="username">
                    <el-input v-model="loginForm.username" placeholder="请输入用户名"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password">
                    <el-input v-model="loginForm.password" type="password" placeholder="请输入密码"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-checkbox v-model="loginForm.rememberPassword">记住密码</el-checkbox>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="handleLogin" class="login-btn">登录</el-button>
                </el-form-item>
            </el-form>
        </el-card>
    </div>
</template>

<script lang="ts" setup>
import { reactive, ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router';
import { login } from '@/apis';

const router = useRouter();

// 定义登录表单数据
const loginForm = reactive({
    username: 'admin',
    password: '123456',
    rememberPassword: true
});

// 定义表单验证规则
const rules = {
    username: [
        { required: true, message: '请输入用户名', trigger: 'blur' }
    ],
    password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, message: '密码长度不能少于 6 个字符', trigger: 'blur' }
    ]
};

// 定义表单引用
const loginFormRef = ref(null);

// 页面加载时检查是否记住密码
onMounted(() => {
    const savedUsername = localStorage.getItem('username');
    const savedPassword = localStorage.getItem('password');
    if (savedUsername && savedPassword) {
        loginForm.username = savedUsername;
        loginForm.password = savedPassword;
        loginForm.rememberPassword = true;
    }
});

// 登录处理函数
const handleLogin = async () => {
    if (!loginFormRef.value) return;
    await loginFormRef.value.validate(async (valid: boolean) => {
        if (valid) {
            if (loginForm.rememberPassword) {
                localStorage.setItem('username', loginForm.username);
                localStorage.setItem('password', loginForm.password);
            } else {
                localStorage.removeItem('username');
                localStorage.removeItem('password');
            }
            try {
                const { data } = await login({
                    username: loginForm.username,
                    password: loginForm.password
                });
                if (data?.code === 1) {
                    // 设置登录状态
                    localStorage.setItem('isLoggedIn', 'true');
                    // 设置全局的token
                    localStorage.setItem('token', data.data.token);
                    // 跳转到 admin 页面
                    router.push({ name: 'admin' });
                    ElMessage.success('登录成功');
                } else {
                    ElMessage.error(`登陆失败-${data.msg}`);
                }
            } catch (error: any) {
                ElMessage.error(error.message);
                return false;
            }
        } else {
            ElMessage.error('登录失败，请检查输入');
            return false;
        }
    });
};
</script>

<style lang="scss" scoped>
.login-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;

    .login-card {
        width: 380px;
        padding: 30px;
        border-radius: 12px;
        box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);

        .login-title {
            text-align: center;
            margin-bottom: 30px;
            color: #333;
            font-size: 24px;
            font-weight: 600;
        }

        .login-btn {
            width: 100%;
            height: 44px;
            font-size: 16px;
            margin-top: 10px;
            border-radius: 6px;
        }

        .el-form-item {
            margin-bottom: 20px;
        }

        .el-checkbox {
            font-size: 14px;
            color: #666;
        }
    }
}
</style>