<template>
  <div class="delivery-record">
    <h2 class="page-title">接单记录</h2>
    
    <el-card>
      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane label="配送中" name="1">
          <el-table :data="orderList" v-loading="loading" border>
            <el-table-column prop="orderNo" label="订单号" />
            <el-table-column prop="stationName" label="快递站点" />
            <el-table-column prop="pickupCode" label="取件码" />
            <el-table-column prop="buildingName" label="宿舍楼" />
            <el-table-column prop="roomNumber" label="房间号" />
            <el-table-column prop="totalPrice" label="总价">
              <template #default="{ row }">
                ¥{{ row.totalPrice }}
              </template>
            </el-table-column>
            <el-table-column prop="deliveryIncome" label="我的收入">
              <template #default="{ row }">
                ¥{{ row.deliveryIncome }}
              </template>
            </el-table-column>
            <el-table-column prop="receiveTime" label="接单时间" />
            <el-table-column label="操作" width="120">
              <template #default="{ row }">
                <el-button type="success" @click="handleComplete(row)">完成</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        
        <el-tab-pane label="已完成" name="2">
          <el-table :data="orderList" v-loading="loading" border>
            <el-table-column prop="orderNo" label="订单号" />
            <el-table-column prop="stationName" label="快递站点" />
            <el-table-column prop="pickupCode" label="取件码" />
            <el-table-column prop="buildingName" label="宿舍楼" />
            <el-table-column prop="roomNumber" label="房间号" />
            <el-table-column prop="deliveryIncome" label="我的收入">
              <template #default="{ row }">
                ¥{{ row.deliveryIncome }}
              </template>
            </el-table-column>
            <el-table-column prop="completeTime" label="完成时间" />
          </el-table>
        </el-tab-pane>
        
        <el-tab-pane label="已取消" name="3">
          <el-table :data="orderList" v-loading="loading" border>
            <el-table-column prop="orderNo" label="订单号" />
            <el-table-column prop="stationName" label="快递站点" />
            <el-table-column prop="pickupCode" label="取件码" />
            <el-table-column prop="cancelReason" label="取消原因" />
            <el-table-column prop="cancelTime" label="取消时间" />
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getDeliveryOrders, completeOrder } from '@/api/order'

const activeTab = ref('1')
const orderList = ref([])
const loading = ref(false)

const loadData = async () => {
  loading.value = true
  try {
    const res = await getDeliveryOrders({ status: parseInt(activeTab.value) })
    orderList.value = res.data
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const handleTabChange = () => {
  loadData()
}

const handleComplete = async (order) => {
  try {
    await ElMessageBox.confirm('确定要完成该订单吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await completeOrder(order.id)
    ElMessage.success('订单已完成')
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
.delivery-record {
  padding: 20px;
}

.page-title {
  margin-bottom: 20px;
  color: #303133;
}
</style>
