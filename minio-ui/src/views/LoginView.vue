<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { login, register, companyRegister } from '../api/index'
import { setToken, setUser } from '../utils/auth'

const router = useRouter()

/* ---- tab switch ---- */
const activeTab = ref('login')

/* ---- login form ---- */
const loginForm = ref({ username: '', userPass: '' })
const loginError = ref('')
const loginLoading = ref(false)

async function handleLogin() {
  loginError.value = ''
  if (!loginForm.value.username.trim() || !loginForm.value.userPass) {
    loginError.value = '请填写用户名和密码'
    return
  }
  loginLoading.value = true
  try {
    const { data } = await login(loginForm.value.username.trim(), loginForm.value.userPass)
    if (data.code === 1) {
      setToken(data.data.token)
      setUser({ id: data.data.id, username: data.data.username })
      router.push({ name: 'upload' })
    } else {
      loginError.value = data.msg || '登录失败'
    }
  } catch (err) {
    loginError.value = err.response?.data?.msg || err.message || '登录失败'
  } finally {
    loginLoading.value = false
  }
}

/* ---- register form ---- */
const registerForm = ref({ username: '', userPass: '', enterPrise: '', enterKey: '' })
const registerError = ref('')
const registerSuccess = ref('')
const registerLoading = ref(false)

async function handleRegister() {
  registerError.value = ''
  registerSuccess.value = ''
  const f = registerForm.value
  if (!f.username.trim() || !f.userPass || !f.enterPrise.trim() || !f.enterKey.trim()) {
    registerError.value = '请填写所有字段'
    return
  }
  registerLoading.value = true
  try {
    const { data } = await register(f.username.trim(), f.userPass, f.enterPrise.trim(), f.enterKey.trim())
    if (data.code === 1) {
      registerSuccess.value = data.data || '注册成功，请切换到登录'
      registerForm.value = { username: '', userPass: '', enterPrise: '', enterKey: '' }
    } else {
      registerError.value = data.msg || '注册失败'
    }
  } catch (err) {
    registerError.value = err.response?.data?.msg || err.message || '注册失败'
  } finally {
    registerLoading.value = false
  }
}

/* ---- company register modal ---- */
const showCompanyModal = ref(false)
const companyForm = ref({ epName: '', numSate: 0, enterKey: '' })
const companyError = ref('')
const companySuccess = ref('')
const companyLoading = ref(false)

function openCompanyModal() {
  companyForm.value = { epName: '', numSate: 0, enterKey: '' }
  companyError.value = ''
  companySuccess.value = ''
  showCompanyModal.value = true
}

async function handleCompanyRegister() {
  companyError.value = ''
  companySuccess.value = ''
  const f = companyForm.value
  if (!f.epName.trim()) {
    companyError.value = '请填写公司名称'
    return
  }
  companyLoading.value = true
  try {
    const { data } = await companyRegister(f.epName.trim(), f.numSate, f.enterKey.trim() || undefined)
    if (data.code === 1) {
      companySuccess.value = data.data || '公司注册成功'
    } else {
      companyError.value = data.msg || '公司注册失败'
    }
  } catch (err) {
    companyError.value = err.response?.data?.msg || err.message || '公司注册失败'
  } finally {
    companyLoading.value = false
  }
}
</script>

<template>
  <div class="page" style="position: relative;">
    <!-- 角落：公司注册入口 -->
    <button class="corner-link" type="button" @click="openCompanyModal">公司注册</button>

    <div class="card">
      <div class="brand">
        <div class="logo">M</div>
        <div>
          <h1>MinIO 卫星数据管理</h1>
          <p>登录或注册以继续</p>
        </div>
      </div>

      <!-- 登录 / 注册 tab -->
      <div class="tabs">
        <button class="tab" :class="{ active: activeTab === 'login' }" @click="activeTab = 'login'">登录</button>
        <button class="tab" :class="{ active: activeTab === 'register' }" @click="activeTab = 'register'">注册</button>
      </div>

      <!-- ====== 登录表单 ====== -->
      <form v-if="activeTab === 'login'" class="form" @submit.prevent="handleLogin">
        <label for="loginUser">用户名</label>
        <input id="loginUser" v-model="loginForm.username" type="text" placeholder="请输入用户名" autocomplete="username" />

        <label for="loginPass">密码</label>
        <input id="loginPass" v-model="loginForm.userPass" type="password" placeholder="请输入密码" autocomplete="current-password" />

        <p v-if="loginError" class="error">{{ loginError }}</p>
        <button type="submit" :disabled="loginLoading">{{ loginLoading ? '登录中...' : '登 录' }}</button>
      </form>

      <!-- ====== 注册表单 ====== -->
      <form v-if="activeTab === 'register'" class="form" @submit.prevent="handleRegister">
        <label for="regUser">用户名</label>
        <input id="regUser" v-model="registerForm.username" type="text" placeholder="请输入用户名" autocomplete="username" />

        <label for="regPass">密码</label>
        <input id="regPass" v-model="registerForm.userPass" type="password" placeholder="请输入密码" autocomplete="new-password" />

        <label for="regCompany">公司名称</label>
        <input id="regCompany" v-model="registerForm.enterPrise" type="text" placeholder="请输入所属公司名称" />

        <label for="regKey">公司专属 Key</label>
        <input id="regKey" v-model="registerForm.enterKey" type="text" placeholder="请输入公司专属注册 Key" />

        <p v-if="registerError" class="error">{{ registerError }}</p>
        <p v-if="registerSuccess" class="success-text">{{ registerSuccess }}</p>
        <button type="submit" :disabled="registerLoading">{{ registerLoading ? '注册中...' : '注 册' }}</button>
      </form>
    </div>

    <!-- ====== 公司注册弹窗 ====== -->
    <div v-if="showCompanyModal" class="modal-overlay" @click.self="showCompanyModal = false">
      <div class="modal">
        <div class="modal-header">
          <h2>注册新公司</h2>
          <button class="modal-close" type="button" @click="showCompanyModal = false">&times;</button>
        </div>

        <form class="form" @submit.prevent="handleCompanyRegister">
          <label for="epName">公司名称</label>
          <input id="epName" v-model="companyForm.epName" type="text" placeholder="请输入公司名称" />

          <label for="numSate">卫星数量</label>
          <input id="numSate" v-model.number="companyForm.numSate" type="number" placeholder="请输入卫星数量" min="0" />

          <label for="enterKey">公司专属 Key（选填，不填则自动生成）</label>
          <input id="enterKey" v-model="companyForm.enterKey" type="text" placeholder="留空则自动生成" />

          <p v-if="companyError" class="error">{{ companyError }}</p>
          <p v-if="companySuccess" class="success-text">{{ companySuccess }}</p>
          <button type="submit" :disabled="companyLoading">{{ companyLoading ? '提交中...' : '注册公司' }}</button>
        </form>
      </div>
    </div>
  </div>
</template>
