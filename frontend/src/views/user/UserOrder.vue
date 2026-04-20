<template>
  <div class="user-order">
    <h2 class="page-title">我的订单</h2>
    
    <el-card>
      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane label="全部" name="">
          <order-list :orders="orderList" @refresh="loadData" />
        </el-tab-pane>
        <el-tab-pane label="待接单" name="0">
          <order-list :orders="orderList" @refresh="loadData" />
        </el-tab-pane>
        <el-tab-pane label="配送中" name="1">
          <order-list :orders="orderList" @refresh="loadData" />
        </el-tab-pane>
        <el-tab-pane label="已完成" name="2">
          <order-list :orders="orderList" @refresh="loadData" />
        </el-tab-pane>
        <el-tab-pane label="已取消" name="3">
          <order-list :orders="orderList" @refresh="loadData" />
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMyOrders } from '@/api/order'
import OrderList from './components/OrderList.vue'

const activeTab = ref('')
const orderList = ref([])

const loadData = async () => {
  try {
    const status = activeTab.value === '' ? null : parseInt(activeTab.value)
    const res = await getMyOrders({ status })
    orderList.value = res.data
  } catch (error) {
    console.error(error)
  }
}

const handleTabChange = () => {
  loadData()
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.user-order {
  padding: 20px 0;
}

.page-title {
  margin-bottom: 20px;
  color: #303133;
}
</style>
