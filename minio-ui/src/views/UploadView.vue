<script setup>
import { computed, ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { uploadFile, getSatellites, insertSatellite } from '../api/index'
import { clearAuth, getUser } from '../utils/auth'

const router = useRouter()

const user = computed(() => getUser())

/* ---- 文件 ---- */
const selectedFile = ref(null)
const dragOver = ref(false)

function handleFileChange(event) {
  const file = event.target.files?.[0]
  if (file) {
    selectedFile.value = file
    message.value = ''
  }
}

function handleDrop(event) {
  dragOver.value = false
  const file = event.dataTransfer.files?.[0]
  if (file) {
    selectedFile.value = file
    message.value = ''
  }
}

function formatSize(bytes) {
  if (bytes < 1024) return `${bytes} B`
  if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(1)} KB`
  return `${(bytes / (1024 * 1024)).toFixed(2)} MB`
}

/* ---- 上传参数 ---- */
const fileType = ref(1)
const sateName = ref('')
const uploading = ref(false)
const message = ref('')
const messageType = ref('info')

/* ---- 卫星列表 ---- */
const satellites = ref([])
const sateLoading = ref(false)

async function fetchSatellites() {
  sateLoading.value = true
  try {
    const { data } = await getSatellites()
    if (data.code === 1 && Array.isArray(data.data)) {
      satellites.value = data.data
    }
  } catch (err) {
    // 静默失败，用户可手动插入
  } finally {
    sateLoading.value = false
  }
}

onMounted(fetchSatellites)

/* ---- 插入新卫星 ---- */
const newSateName = ref('')
const sateInserting = ref(false)
const sateMsg = ref('')
const sateMsgType = ref('info')

async function handleInsertSatellite() {
  const name = newSateName.value.trim()
  if (!name) return
  sateInserting.value = true
  sateMsg.value = ''
  try {
    const { data } = await insertSatellite(name)
    if (data.code === 1) {
      sateMsg.value = '插入卫星成功'
      sateMsgType.value = 'success'
      newSateName.value = ''
      await fetchSatellites()
      // 自动选中刚插入的卫星
      if (satellites.value.length > 0) {
        sateName.value = satellites.value[satellites.value.length - 1].name
      }
    } else {
      sateMsg.value = data.msg || '插入失败'
      sateMsgType.value = 'error'
    }
  } catch (err) {
    sateMsg.value = err.response?.data?.msg || err.message || '插入失败'
    sateMsgType.value = 'error'
  } finally {
    sateInserting.value = false
  }
}

/* ---- 上传 ---- */
async function handleUpload() {
  if (!selectedFile.value) {
    message.value = '请先选择文件'
    messageType.value = 'error'
    return
  }
  if (!sateName.value) {
    message.value = '请选择卫星'
    messageType.value = 'error'
    return
  }

  uploading.value = true
  message.value = '正在上传...'
  messageType.value = 'info'

  try {
    const { data } = await uploadFile(selectedFile.value, fileType.value, sateName.value)
    if (data.code === 1) {
      message.value = data.data || '上传成功'
      messageType.value = 'success'
      selectedFile.value = null
    } else {
      message.value = data.msg || '上传失败'
      messageType.value = 'error'
    }
  } catch (err) {
    message.value = err.response?.data?.msg || err.message || '上传失败'
    messageType.value = 'error'
  } finally {
    uploading.value = false
  }
}

function handleLogout() {
  clearAuth()
  router.push({ name: 'login' })
}
</script>

<template>
  <div class="page">
    <div class="card upload-card">
      <!-- 顶部 -->
      <header class="header">
        <div>
          <h1>文件上传</h1>
          <p>当前用户：<strong>{{ user?.username || '-' }}</strong></p>
        </div>
        <button class="ghost" type="button" @click="handleLogout">退出登录</button>
      </header>

      <!-- 上传参数：文件类型 + 卫星选择 -->
      <div class="upload-options">
        <div class="field">
          <label for="fileType">文件类型</label>
          <select id="fileType" v-model.number="fileType">
            <option :value="1">遥测数据 (yaoce)</option>
            <option :value="2">轨道数据 (guidao)</option>
            <option :value="3">通用文件 (comfile)</option>
          </select>
        </div>
        <div class="field">
          <label for="sateName">选择卫星</label>
          <select id="sateName" v-model="sateName">
            <option value="" disabled>-- 请选择卫星 --</option>
            <option v-for="s in satellites" :key="s.id" :value="s.name">{{ s.name }}</option>
          </select>
        </div>
      </div>

      <!-- 插入卫星 -->
      <div class="add-satellite-row">
        <input
          v-model="newSateName"
          type="text"
          placeholder="输入新卫星名称"
          @keyup.enter="handleInsertSatellite"
        />
        <button type="button" :disabled="sateInserting || !newSateName.trim()" @click="handleInsertSatellite">
          {{ sateInserting ? '插入中' : '+ 插入卫星' }}
        </button>
      </div>
      <p v-if="sateMsg" class="message" :class="sateMsgType" style="margin-top:0;margin-bottom:12px;">{{ sateMsg }}</p>

      <!-- 拖拽上传区 -->
      <div
        class="dropzone"
        :class="{ active: dragOver, filled: !!selectedFile }"
        @dragover.prevent="dragOver = true"
        @dragleave.prevent="dragOver = false"
        @drop.prevent="handleDrop"
      >
        <input id="fileInput" type="file" @change="handleFileChange" />
        <label for="fileInput">
          <span v-if="!selectedFile">点击或拖拽文件到此处</span>
          <span v-else>
            已选择：{{ selectedFile.name }}
            <small>({{ formatSize(selectedFile.size) }})</small>
          </span>
        </label>
      </div>

      <!-- 上传按钮 -->
      <button
        class="primary"
        type="button"
        :disabled="uploading || !selectedFile || !sateName"
        @click="handleUpload"
      >
        {{ uploading ? '上传中...' : '开始上传' }}
      </button>

      <p v-if="message" class="message" :class="messageType">{{ message }}</p>
    </div>
  </div>
</template>
