<template>
  <el-container class="user-layout">
    <el-header class="header">
      <div class="logo" @click="router.push('/user/home')">
        <el-icon size="28"><Box /></el-icon>
        <span>快递代取平台</span>
      </div>
      <el-menu
        :default-active="$route.path"
        router
        mode="horizontal"
        class="user-menu"
      >
        <el-menu-item v-for="item in menuItems" :key="item.path" :index="item.path">
          {{ item.title }}
        </el-menu-item>
      </el-menu>
      <div class="header-right">
        <el-dropdown @command="handleCommand">
          <span class="user-info">
            <el-avatar :size="40" :src="getAvatarUrl(userStore.userInfo?.avatar)">
              <el-icon><UserFilled /></el-icon>
            </el-avatar>
            <span class="username">{{ userStore.userInfo?.nickname || userStore.userInfo?.username }}</span>
            <el-icon><ArrowDown /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile">个人中心</el-dropdown-item>
              <el-dropdown-item command="recharge">账户充值</el-dropdown-item>
              <el-dropdown-item v-if="userStore.isDelivery" command="delivery-center">配送员中心</el-dropdown-item>
              <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </el-header>
    <el-main class="main-content">
      <router-view />
    </el-main>
    <el-footer class="footer">
      <p>© 2024 快递代取平台 - 让取件更便捷</p>
    </el-footer>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '@/store/user'
import { logout } from '@/api/auth'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const menuItems = computed(() => {
  const routes = router.getRoutes().find(r => r.path === '/user')?.children || []
  return routes.filter(r => r.meta && !['create-order', 'profile'].includes(r.path)).map(r => ({
    path: '/user/' + r.path,
    title: r.meta.title
  }))
})

const getAvatarUrl = (avatar) => {
  if (!avatar) {
    return ''
  }
  if (avatar.startsWith('http://') || avatar.startsWith('https://')) {
    return avatar
  }
  return '/api' + avatar
}

const handleCommand = async (command) => {
  if (command === 'logout') {
    try {
      await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      await logout()
      userStore.logout()
      router.push('/login')
      ElMessage.success('退出成功')
    } catch (error) {
      if (error !== 'cancel') {
        console.error(error)
      }
    }
  } else if (command === 'profile') {
    router.push('/user/profile')
  } else if (command === 'recharge') {
    router.push('/user/recharge')
  } else if (command === 'delivery-center') {
    router.push('/delivery/hall')
  }
}
</script>

<style scoped>
.user-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  background-color: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  padding: 0 40px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 20px;
  font-weight: bold;
  color: #409EFF;
  cursor: pointer;
  margin-right: 40px;
}

.user-menu {
  flex: 1;
  border-bottom: none;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 10px;
}

.username {
  color: #606266;
  font-size: 14px;
}

.main-content {
  flex: 1;
  background-color: #f5f7fa;
  padding: 20px 40px;
}

.footer {
  background-color: #304156;
  color: #bfcbd9;
  text-align: center;
  padding: 20px;
}
</style>
