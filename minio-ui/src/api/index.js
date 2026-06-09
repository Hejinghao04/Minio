import axios from 'axios'
import { getToken, clearAuth } from '../utils/auth'

const request = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 60000,
})

// 请求拦截器：自动附带 JWT token
request.interceptors.request.use((config) => {
  const token = getToken()
  if (token) {
    config.headers.token = token
  }
  return config
})

// 响应拦截器：token 过期或无效时跳转登录
request.interceptors.response.use(
  (res) => res,
  (err) => {
    if (err.response?.status === 401) {
      clearAuth()
      window.location.href = '/login'
    }
    return Promise.reject(err)
  },
)

/** 用户登录 */
export function login(username, userPass) {
  return request.post('/login', { username, userPass })
}

/** 用户注册 */
export function register(username, userPass, enterPrise, enterKey) {
  return request.post('/register', { username, userPass, enterPrise, enterKey })
}

/** 公司注册 */
export function companyRegister(epName, numSate, enterKey) {
  return request.post('/enterPriseRegister', { epName, numSate, enterKey })
}

/** 获取所有卫星 */
export function getSatellites() {
  return request.get('/getSate')
}

/** 插入卫星 */
export function insertSatellite(sateName) {
  return request.post('/insertSatellite', { sateName })
}

/** 上传文件 */
export function uploadFile(file, fileType, sateName) {
  const formData = new FormData()
  formData.append('file', file)
  formData.append('fileType', fileType)
  formData.append('sateName', sateName)
  return request.post('/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  })
}

/** 分页查询文件 */
export function getFilePage(params = {}) {
  return request.get('/fileEnterprisePage', { params })
}

export default request
