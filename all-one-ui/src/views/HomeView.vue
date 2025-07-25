<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { getImageListByPage, getArticleListByPage, getQuestionListByPage } from '@/apis';
import { useRouter } from 'vue-router'

const router = useRouter()

interface ImgCarousel {
  id: string;
  type: string;
  url: string;
  imgUrl: string;
  content: string;
  labels: string;
  dateStr: string;
}
interface ContentItem {
  id: string;
  order: string;
  title: string;
  author: string;
}
const imageList = ref<Array<ImgCarousel>>([]);
const articles = ref<Array<ContentItem>>([
]);
const questions = ref<Array<ContentItem>>([
]);
const handleImg = async (item: ImgCarousel) => {
  router.push("/image/" + item.id)
}
const handleDetail = async (item: ContentItem) => {
  router.push("/article/" + item.id)
}

const getDate = (dateStr: string) => {
  return dateStr.split(' ')[0];
};
const getExcludeDate = (dateStr: string) => {
  return dateStr.split(' ')[1] + dateStr.split(' ')[2];
}
const handleToAbout = () => {
  router.push("/about");
}
onMounted(async () => {
  const { data } = await getImageListByPage(1, 7);
  if (data?.code === 1) {
    imageList.value = data.data.records;
  }

  const { data: articlesData } = await getArticleListByPage(1, 7);
  if (articlesData?.code === 1) {
    articles.value = articlesData.data.records;
  }

  const { data: questionsData } = await getQuestionListByPage(1, 7);
  if (questionsData?.code === 1) {
    questions.value = questionsData.data.records;
  }
});
</script>

<template>
  <div class="home-layout">
    <el-container>
      <el-header>
        <div class="home-header-bar">
          ONE一个
        </div>
      </el-header>
      <el-container style="padding:20px;">
        <el-aside width="566px">
          <el-carousel trigger="click" height="543px" :autoplay="false" motion-blur
            style="--el-carousel-indicator-width: 20px;">
            <el-carousel-item v-for="item in imageList" :key="item.id" @click="handleImg(item)">
              <div style="height: 378px;">
                <img :src="item.imgUrl" alt="" style="width: 100%;height:100%;">
              </div>
              <div class="home-main-pic-type">{{ item.type }}</div>
              <div class="home-main-block-word">
                <div class="home-main-block-word-main">{{ item.content }}</div>
                <div class="home-main-block-word-aside">
                  <!-- <div style="font-size: 12px;">{{ item.labels }}</div> -->
                  <div style="font-size: 56px;font-weight: 600;line-height: 56px;">{{ getDate(item.dateStr) }}</div>
                  <div style="font-size: 12px;">{{ getExcludeDate(item.dateStr) }}</div>
                </div>
              </div>
            </el-carousel-item>
          </el-carousel>
        </el-aside>
        <el-main>
          <div class="home-main-block">
            <div class="home-main-block-header">ONE文章</div>
            <ul>
              <li v-for="item in articles" :key="item.order" @click="handleDetail(item)">
                <span>{{ item.order }}</span><span>{{ item.title }}</span><span>{{ item.author }}</span>
              </li>
            </ul>
          </div>
          <div class="home-main-block">
            <div class="home-main-block-header">ONE问题</div>
            <ul>
              <li v-for="item in questions" :key="item.order" @click="handleDetail(item)">
                <span>{{ item.order }}</span><span>{{ item.title }}</span>
              </li>
            </ul>
          </div>
        </el-main>
      </el-container>
      <el-footer style="margin-top: 50px;">
        <div class="home-main-footer-content">
          每天只为你准备一张图片、一篇文字和一个问答
        </div>
        <p class="home-main-footer-content">
          复杂世界里, 一个就够了. One is all.
        </p>
        <div @click="handleToAbout" class="home-main-footer-content"
          style="font-size:14px;color: #01aef0;cursor: pointer;">[关于]</div>
      </el-footer>
    </el-container>
  </div>
</template>

<style lang="scss" scoped>
.home-layout {
  max-width: 1280px;
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

.home-main-pic-type {
  background-color: #333;
  color: #fff;
  padding: 3px 0;
  text-align: center;
}

.home-main-block-word {
  background-color: #d6d6d6;
  height: 120px;
  display: flex;
}

.home-main-block-word-aside {
  background-color: #f2f2f2;
  color: #333;
  height: 120px;
  padding: 12.5px;
  text-align: center;
  width: 100px;
}

.home-main-block-word-main {
  font-size: 14px;
  height: 80px;
  line-height: 1.67;
  padding: 10px 5%;
  vertical-align: middle;
  flex: 1;
  display: -webkit-box;
  overflow: hidden;
  line-clamp: 3;
  -webkit-line-clamp: 3;
  text-overflow: ellipsis;
  -webkit-box-orient: vertical;
}

.home-main-block {
  background: #f6f6f6;
  min-width: 280px;

  &>ul {
    list-style-type: none;
    padding: 10px;

    li:first-child {
      color: #01aef0;
      font-size: 20px;
      margin: 10px 0;
    }

    li {
      cursor: pointer;
      font-size: 12px;
      height: 26px;
      line-height: 26px;
      overflow: hidden;
      padding: 0 10px;
      text-overflow: ellipsis;
      white-space: nowrap;

      &:hover {
        text-decoration: underline;
      }
    }
  }
}

:deep(.el-carousel .el-carousel__arrow) {
  top: 40%;
}

.home-main-footer-content {
  font-size: 12px;
  color: #333;
  text-align: center;
}
</style>
