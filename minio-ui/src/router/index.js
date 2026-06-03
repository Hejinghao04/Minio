import { createRouter, createWebHistory } from 'vue-router'
import { isLoggedIn } from '../utils/auth'
import LoginView from '../views/LoginView.vue'
import UploadView from '../views/UploadView.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', redirect: '/login' },
    { path: '/login', name: 'login', component: LoginView },
    {
      path: '/upload',
      name: 'upload',
      component: UploadView,
      meta: { requiresAuth: true },
    },
  ],
})

router.beforeEach((to) => {
  if (to.meta.requiresAuth && !isLoggedIn()) {
    return { name: 'login' }
  }
  if (to.name === 'login' && isLoggedIn()) {
    return { name: 'upload' }
  }
})

export default router
