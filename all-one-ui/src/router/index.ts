import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import ArticleDetail from '@/views/ArticleDetail.vue'
import ImageDetail from '@/views/ImageDetail.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/about',
      name: 'about',
      component: () => import('../views/AboutView.vue'),
    },
    {
      path: '/article/:id', // 动态路由路径，:id 是一个参数占位符
      component: ArticleDetail,
      name: 'articleDetail',
      props: true,
    },
    {
      path: '/image/:id', // 动态路由路径，:id 是一个参数占位符
      component: ImageDetail,
      name: 'imageDetail',
      props: true,
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/admin/Login.vue'),
    },
    {
      path: '/admin',
      name: 'admin',
      component: () => import('../views/admin/AdminView.vue'),
      children: [
        {
          path: '/image',
          name: 'image',
          component: () => import('../views/admin/imageManage/index.vue'),
        },
        {
          path: '/article',
          name: 'article',
          component: () => import('../views/admin/articleManage/index.vue'),
        },
      ],
    },
  ],
})

// 全局前置守卫
router.beforeEach((to, from, next) => {
  const isLoggedIn = localStorage.getItem('isLoggedIn') === 'true'
  if (
    to.name === 'admin' ||
    to.matched.some(record => record.name === 'admin')
  ) {
    if (isLoggedIn) {
      next()
    } else {
      next({ name: 'login' })
    }
  } else {
    next()
  }
})

export default router
