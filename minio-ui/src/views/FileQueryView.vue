<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getFilePage, getSatellites } from '../api/index'
import { clearAuth, getUser } from '../utils/auth'

const router = useRouter()
const user = getUser()

/* ---- 固定的 bucket 选项 ---- */
const buckets = [
  { value: '', label: '全部 Bucket' },
  { value: 'yaoce', label: 'yaoce（遥测数据）' },
  { value: 'guidao', label: 'guidao（轨道数据）' },
  { value: 'comfile', label: 'comfile（通用文件）' },
]

/* ---- 筛选 ---- */
const bucketFilter = ref('')
const satelliteFilter = ref('')

/* ---- 卫星列表（复用已有 /api/getSate） ---- */
const satellites = ref([])
async function loadSatellites() {
  try {
    const { data } = await getSatellites()
    if (data.code === 1 && Array.isArray(data.data)) {
      satellites.value = data.data
    }
  } catch (e) { /* ignore */ }
}

/* ---- 文件列表 ---- */
const fileList = ref([])
const total = ref(0)
const pages = ref(0)
const current = ref(1)
const loading = ref(false)
const errorMsg = ref('')

async function doSearch(pageNum) {
  loading.value = true
  errorMsg.value = ''
  try {
    const { data } = await getFilePage({
      bucketName: bucketFilter.value || '',
      id: satelliteFilter.value || undefined,
      pageNum,
      pageSize: 10,
    })
    if (data.code === 1 && data.data) {
      const page = data.data
      fileList.value = page.records || []
      total.value = page.total || 0
      pages.value = page.pages || 0
      current.value = page.current || pageNum
    } else {
      errorMsg.value = data.msg || '查询失败'
    }
  } catch (err) {
    errorMsg.value = err.response?.data?.msg || err.message || '查询失败'
  } finally {
    loading.value = false
  }
}

function handleReset() {
  bucketFilter.value = ''
  satelliteFilter.value = ''
  doSearch(1)
}

onMounted(() => {
  loadSatellites()
  doSearch(1)
})

/* ---- 分页页码 ---- */
function visiblePages() {
  const totalPages = pages.value
  const cur = current.value
  if (totalPages <= 1) return []
  const result = []
  let start = Math.max(1, cur - 3)
  let end = Math.min(totalPages, cur + 3)
  if (end - start < 6) {
    if (start === 1) end = Math.min(totalPages, start + 6)
    else start = Math.max(1, end - 6)
  }
  for (let i = start; i <= end; i++) result.push(i)
  return result
}

/* ---- 下载 ---- */
function handleDownload(file) {
  if (!file.url) return
  window.open(file.url, '_blank')
}

function handleLogout() {
  clearAuth()
  router.push({ name: 'login' })
}
</script>

<template>
  <div class="page">
    <div class="card query-card">
      <header class="header">
        <div>
          <h1>文件查询</h1>
          <p>当前用户：<strong>{{ user?.username || '-' }}</strong></p>
        </div>
        <div class="header-actions">
          <button class="ghost" type="button" @click="$router.push({ name: 'upload' })">文件上传</button>
          <button class="ghost" type="button" @click="handleLogout">退出登录</button>
        </div>
      </header>

      <!-- 筛选 -->
      <div class="upload-options">
        <div class="field">
          <label>Bucket</label>
          <select v-model="bucketFilter">
            <option v-for="b in buckets" :key="b.value" :value="b.value">{{ b.label }}</option>
          </select>
        </div>
        <div class="field">
          <label>卫星</label>
          <select v-model="satelliteFilter">
            <option value="">全部卫星</option>
            <option v-for="s in satellites" :key="s.id" :value="s.id">
              {{ s.name }}{{ s.epName ? ' (' + s.epName + ')' : '' }}
            </option>
          </select>
        </div>
      </div>

      <div class="filter-actions">
        <button class="primary" type="button" @click="doSearch(1)">查询</button>
        <button class="ghost" type="button" @click="handleReset">重置</button>
      </div>

      <p v-if="errorMsg" class="message error">{{ errorMsg }}</p>

      <!-- 表格 -->
      <div v-if="loading" class="loading-area">加载中...</div>
      <div v-else class="table-wrap">
        <table v-if="fileList.length > 0" class="file-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>文件名</th>
              <th>Bucket</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="f in fileList" :key="f.id">
              <td>{{ f.id }}</td>
              <td class="file-name-cell">{{ f.fileName || '-' }}</td>
              <td><span class="bucket-tag">{{ f.bucketName || '-' }}</span></td>
              <td>
                <button class="ghost btn-sm" type="button" :disabled="!f.url" @click="handleDownload(f)">下载</button>
              </td>
            </tr>
          </tbody>
        </table>
        <div v-else class="empty">暂无文件数据</div>
      </div>

      <!-- 统计 + 分页 -->
      <div v-if="total > 0" class="stats">
        共 <strong>{{ total }}</strong> 条，第 {{ current }} / {{ pages }} 页
      </div>

      <div v-if="pages > 1" class="pagination">
        <button class="ghost btn-sm" :disabled="current <= 1" @click="doSearch(current - 1)">上一页</button>
        <template v-for="p in visiblePages()" :key="p">
          <button v-if="p === current" class="primary btn-sm" @click="doSearch(p)">{{ p }}</button>
          <button v-else class="ghost btn-sm" @click="doSearch(p)">{{ p }}</button>
        </template>
        <button class="ghost btn-sm" :disabled="current >= pages" @click="doSearch(current + 1)">下一页</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.query-card {
  max-width: 900px;
}

.filter-actions {
  display: flex;
  gap: 8px;
  margin-bottom: 16px;
}

.loading-area {
  text-align: center;
  padding: 32px 0;
  color: #6b7280;
}

.table-wrap {
  overflow-x: auto;
}

.file-table {
  width: 100%;
  border-collapse: collapse;
}

.file-table th {
  text-align: left;
  padding: 10px 12px;
  font-size: 0.85rem;
  font-weight: 600;
  color: #6b7280;
  border-bottom: 2px solid #e5e7eb;
  white-space: nowrap;
}

.file-table td {
  padding: 10px 12px;
  font-size: 0.9rem;
  border-bottom: 1px solid #f3f4f6;
  color: #374151;
}

.file-table tbody tr:hover {
  background: #f8fafc;
}

.file-name-cell {
  color: #2563eb;
  font-weight: 500;
}

.bucket-tag {
  display: inline-block;
  padding: 2px 10px;
  border-radius: 10px;
  background: #eff6ff;
  color: #2563eb;
  font-size: 0.8rem;
}

.btn-sm {
  padding: 6px 14px;
  font-size: 0.85rem;
  border-radius: 8px;
}

.no-link {
  color: #d1d5db;
}

.empty {
  text-align: center;
  padding: 40px 0;
  color: #9ca3af;
  font-size: 0.95rem;
}

.stats {
  text-align: center;
  margin-top: 12px;
  font-size: 0.85rem;
  color: #6b7280;
}

.stats strong {
  color: #2563eb;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 6px;
  margin-top: 16px;
}
</style>
