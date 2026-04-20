import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import Cookies from 'js-cookie'

export const useUserStore = defineStore('user', () => {
  const token = ref(Cookies.get('token') || '')
  const userInfo = ref(null)
  
  const isLogin = computed(() => !!token.value)
  const roleCode = computed(() => userInfo.value?.roleCode || '')
  const isAdmin = computed(() => roleCode.value === 'admin')
  const isDelivery = computed(() => roleCode.value === 'delivery')
  const isUser = computed(() => roleCode.value === 'user')
  
  const setToken = (newToken) => {
    token.value = newToken
    Cookies.set('token', newToken, { expires: 30 })
  }
  
  const setUserInfo = (info) => {
    userInfo.value = info
    localStorage.setItem('userInfo', JSON.stringify(info))
  }
  
  const loadUserInfo = () => {
    const saved = localStorage.getItem('userInfo')
    if (saved) {
      userInfo.value = JSON.parse(saved)
    }
  }
  
  const logout = () => {
    token.value = ''
    userInfo.value = null
    Cookies.remove('token')
    localStorage.removeItem('userInfo')
  }
  
  return {
    token,
    userInfo,
    isLogin,
    roleCode,
    isAdmin,
    isDelivery,
    isUser,
    setToken,
    setUserInfo,
    loadUserInfo,
    logout
  }
})
