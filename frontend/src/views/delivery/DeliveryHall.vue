<template>
  <div class="delivery-hall">
    <h2 class="page-title">接单大厅</h2>
    
    <el-row :gutter="20">
      <el-col :span="8" v-for="order in orderList" :key="order.id">
        <el-card class="order-card" shadow="hover">
          <template #header>
            <div class="order-header">
              <span class="order-no">{{ order.orderNo }}</span>
              <el-tag type="info">待接单</el-tag>
            </div>
          </template>
          <div class="order-info">
            <p><el-icon><OfficeBuilding /></el-icon> {{ order.stationName }}</p>
            <p><el-icon><Box /></el-icon> {{ order.specName }}</p>
            <p><el-icon><Key /></el-icon> 取件码: {{ order.pickupCode }}</p>
            <p><el-icon><School /></el-icon> {{ order.buildingName }} {{ order.roomNumber }}</p>
            <p class="price">¥{{ order.totalPrice }}</p>
          </div>
          <div class="order-footer">
            <el-button type="primary" @click="handleReceive(order)">立即接单</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-empty v-if="orderList.length === 0" description="暂无待接订单" />

    <el-pagination
      v-model:current-page="pageNum"
      v-model:page-size="pageSize"
      :total="total"
      layout="prev, pager, next"
      class="pagination"
      @current-change="loadData"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getPendingOrderList, receiveOrder } from '@/api/order'

const orderList = ref([])
const pageNum = ref(1)
const pageSize = ref(12)
const total = ref(0)

const loadData = async () => {
  try {
    const res = await getPendingOrderList({
      pageNum: pageNum.value,
      pageSize: pageSize.value
    })
    orderList.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    console.error(error)
  }
}

const handleReceive = async (order) => {
  try {
    await ElMessageBox.confirm('确定要接该订单吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await receiveOrder(order.id)
    ElMessage.success('接单成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error(error)
    }
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.delivery-hall {
  padding: 20px;
}

.page-title {
  margin-bottom: 20px;
  color: #303133;
}

.order-card {
  margin-bottom: 20px;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.order-no {
  font-weight: bold;
  color: #409EFF;
}

.order-info p {
  margin: 8px 0;
  color: #606266;
  display: flex;
  align-items: center;
  gap: 8px;
}

.order-info .price {
  font-size: 20px;
  font-weight: bold;
  color: #F56C6C;
}

.order-footer {
  margin-top: 15px;
  text-align: center;
}

.pagination {
  margin-top: 20px;
  justify-content: center;
}
</style>
