import axios from 'axios'
import router from '@/router'

// 设置 axios 默认请求头为 JSON
axios.defaults.headers.common['Content-Type'] = 'application/json'

// 请求拦截器，可按需添加
axios.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['token'] = `${token}`
    }
    return config
  },
  error => {
    // 处理请求错误
    return Promise.reject(error)
  },
)

// 响应拦截器
axios.interceptors.response.use(
  response => {
    // 对响应数据做点什么
    return response
  },
  error => {
    if (error.response && error.response.status === 401) {
      // 401 表示未授权，跳转到登录页面
      router.push('/login')
    }
    // 处理响应错误
    return Promise.reject(error)
  },
)

export const getImageList = () => {
  return axios.get('/api/image/list')
}

export const getImageListByPage = (
  page: number,
  pageSize: number,
  entity?: Record<string, string>,
) => {
  return axios.post(
    `/api/image/page?page=${page}&pageSize=${pageSize}`,
    entity || {},
  )
}

export const getImageDetail = (id: string) => {
  return axios.get(`/api/image/detail/${id}`)
}

export const addImage = (data: Record<string, string>) => {
  return axios.post('/api/image/save', data)
}

export const deleteImage = (id: string) => {
  return axios.delete(`/api/image/${id}`)
}

export const updateImage = (data: Record<string, string>) => {
  return axios.put(`/api/image`, data)
}

export const uploadImage = (data: Record<string, string>) => {
  return axios.post('/api/image/upload', data)
}

export const getArticleList = () => {
  return axios.get('/api/article/list')
}

export const getArticleListByPage = (
  page: number,
  pageSize: number,
  entity?: Record<string, string>,
) => {
  return axios.post(
    `/api/article/page?page=${page}&pageSize=${pageSize}`,
    entity || { layout: '1' },
  )
}

export const getQuestionListByPage = (
  page: number,
  pageSize: number,
  entity?: Record<string, string>,
) => {
  return axios.post(
    `/api/question/page?page=${page}&pageSize=${pageSize}`,
    entity || { layout: '2' },
  )
}

export const getArticleDetail = (id: string) => {
  return axios.get(`/api/article/detail/${id}`)
}

export const addArticle = (data: Record<string, string>) => {
  return axios.post(`/api/article/save`, data)
}

export const deleteArticle = (id: string) => {
  return axios.delete(`/api/article/${id}`)
}

export const updateArticle = (data: Record<string, string>, id: string) => {
  return axios.put(`/api/article/update/${id}`, data)
}

export const login = (data: Record<string, string>) => {
  return axios.post('/api/admin/employee/login', data)
}
