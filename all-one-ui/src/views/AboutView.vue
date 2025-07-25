<script lang="ts" setup>
import { useRouter } from 'vue-router';
const router = useRouter();
const handleReturn = (event: MouseEvent) => {
  event.preventDefault();
  router.back();
}
</script>
<template>
  <div class="about-layout">
    <el-container>
      <el-header>
        <div class="home-header-bar" @click="handleReturn">
          ONE一个
        </div>
      </el-header>
      <el-main>
        <div style="text-align: center;">
          <div style="width: 100%;text-align: center;font-size: 30px;">关于</div>
          <div>模仿[ONE一个]的风格自实现的一个简单的博客展示网站</div>
          <div>支持通过富文本编辑器自定义自己的文章问答摄影图片内容等</div>
          <div>测试数据来自[ONE一个]</div>
          <div> 复杂世界里, 一个就够了</div>
        </div>
      </el-main>
    </el-container>


  </div>
</template>

<style scoped>
.about-layout {
  max-width: 960px;
  min-height: 90vh;
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
</style>
