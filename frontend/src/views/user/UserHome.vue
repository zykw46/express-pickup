<template>
  <div class="user-home">
    <el-carousel height="300px" class="banner">
      <el-carousel-item>
        <div class="banner-item banner-1">
          <h2>快递代取服务</h2>
          <p>让取件更便捷，让生活更轻松</p>
          <el-button type="primary" size="large" @click="router.push('/user/create-order')">立即下单</el-button>
        </div>
      </el-carousel-item>
      <el-carousel-item>
        <div class="banner-item banner-2">
          <h2>成为配送员</h2>
          <p>利用空闲时间，赚取额外收入</p>
          <el-button type="success" size="large" @click="router.push('/user/certification')">立即申请</el-button>
        </div>
      </el-carousel-item>
    </el-carousel>

    <el-row :gutter="20" class="feature-row">
      <el-col :span="8">
        <el-card class="feature-card" @click="router.push('/user/create-order')">
          <el-icon size="48" color="#409EFF"><ShoppingBag /></el-icon>
          <h3>快递代取</h3>
          <p>一键下单，快速取件</p>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="feature-card" @click="router.push('/user/order')">
          <el-icon size="48" color="#67C23A"><Document /></el-icon>
          <h3>订单管理</h3>
          <p>查看订单，实时追踪</p>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="feature-card" @click="router.push('/user/recharge')">
          <el-icon size="48" color="#E6A23C"><Money /></el-icon>
          <h3>账户充值</h3>
          <p>余额充值，便捷支付</p>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="notice-card">
      <template #header>
        <div class="card-header">
          <span><el-icon><Bell /></el-icon> 通知公告</span>
          <el-link type="primary" @click="router.push('/user/notice')">查看更多</el-link>
        </div>
      </template>
      <el-timeline>
        <el-timeline-item
          v-for="notice in noticeList"
          :key="notice.id"
          :timestamp="notice.publishDate"
        >
          {{ notice.title }}
        </el-timeline-item>
      </el-timeline>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getActiveNoticeList } from '@/api/notice'

const router = useRouter()
const noticeList = ref([])

const loadNotice = async () => {
  try {
    const res = await getActiveNoticeList(5)
    noticeList.value = res.data
  } catch (error) {
    console.error(error)
  }
}

onMounted(() => {
  loadNotice()
})
</script>

<style scoped>
.user-home {
  padding: 20px 0;
}

.banner {
  margin-bottom: 30px;
  border-radius: 8px;
  overflow: hidden;
}

.banner-item {
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #fff;
  text-align: center;
}

.banner-1 {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.banner-2 {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.banner-item h2 {
  font-size: 36px;
  margin-bottom: 15px;
}

.banner-item p {
  font-size: 18px;
  margin-bottom: 20px;
}

.feature-row {
  margin-bottom: 30px;
}

.feature-card {
  text-align: center;
  padding: 30px;
  cursor: pointer;
  transition: transform 0.3s;
}

.feature-card:hover {
  transform: translateY(-5px);
}

.feature-card h3 {
  margin: 15px 0 10px;
  color: #303133;
}

.feature-card p {
  color: #909399;
}

.notice-card {
  margin-top: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
