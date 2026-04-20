<template>
  <div class="order-list">
    <el-empty v-if="orders.length === 0" description="暂无订单" />
    
    <el-card v-for="order in orders" :key="order.id" class="order-item" shadow="hover">
      <div class="order-header">
        <span class="order-no">订单号: {{ order.orderNo }}</span>
        <el-tag :type="getStatusType(order.status)">{{ getStatusText(order.status) }}</el-tag>
      </div>
      
      <el-divider />
      
      <div class="order-content">
        <div class="info-row">
          <span class="label">快递站点:</span>
          <span class="value">{{ order.stationName }}</span>
        </div>
        <div class="info-row">
          <span class="label">包裹规格:</span>
          <span class="value">{{ order.specName }}</span>
        </div>
        <div class="info-row">
          <span class="label">取件码:</span>
          <span class="value">{{ order.pickupCode }}</span>
        </div>
        <div class="info-row">
          <span class="label">配送地址:</span>
          <span class="value">{{ order.buildingName }} {{ order.roomNumber }}</span>
        </div>
        <div class="info-row" v-if="order.deliveryName">
          <span class="label">配送员:</span>
          <span class="value">{{ order.deliveryName }}</span>
        </div>
        <div class="info-row" v-if="order.remark">
          <span class="label">备注:</span>
          <span class="value">{{ order.remark }}</span>
        </div>
      </div>
      
      <el-divider />
      
      <div class="order-footer">
        <div class="price">
          订单金额: <span class="amount">¥{{ order.totalPrice }}</span>
        </div>
        <div class="actions">
          <el-button v-if="order.status === 0 && order.payStatus === 0" type="primary" @click="handlePay(order)">支付</el-button>
          <el-button v-if="order.status === 0" type="danger" @click="handleCancel(order)">取消</el-button>
          <el-button v-if="order.status === 1" type="success" @click="handleConfirm(order)">确认收货</el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ElMessage, ElMessageBox } from 'element-plus'
import { payOrder, cancelOrder, confirmOrder } from '@/api/order'

const props = defineProps({
  orders: {
    type: Array,
    default: () => []
  }
})

const emit = defineEmits(['refresh'])

const getStatusType = (status) => {
  const types = { 0: 'info', 1: 'warning', 2: 'success', 3: 'danger' }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = { 0: '待接单', 1: '配送中', 2: '已完成', 3: '已取消' }
  return texts[status] || '未知'
}

const handlePay = async (order) => {
  try {
    await ElMessageBox.confirm('确定要支付该订单吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await payOrder(order.id)
    ElMessage.success('支付成功')
    emit('refresh')
  } catch (error) {
    if (error !== 'cancel') {
      console.error(error)
    }
  }
}

const handleCancel = async (order) => {
  try {
    const { value } = await ElMessageBox.prompt('请输入取消原因', '取消订单', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputPlaceholder: '请输入取消原因'
    })
    await cancelOrder(order.id, value)
    ElMessage.success('取消成功')
    emit('refresh')
  } catch (error) {
    if (error !== 'cancel') {
      console.error(error)
    }
  }
}

const handleConfirm = async (order) => {
  try {
    await ElMessageBox.confirm('确定要确认收货吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await confirmOrder(order.id)
    ElMessage.success('确认收货成功')
    emit('refresh')
  } catch (error) {
    if (error !== 'cancel') {
      console.error(error)
    }
  }
}
</script>

<style scoped>
.order-list {
  padding: 10px 0;
}

.order-item {
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

.order-content {
  padding: 10px 0;
}

.info-row {
  display: flex;
  margin: 8px 0;
}

.label {
  width: 80px;
  color: #909399;
}

.value {
  flex: 1;
  color: #606266;
}

.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.price {
  color: #606266;
}

.amount {
  font-size: 18px;
  font-weight: bold;
  color: #F56C6C;
}

.actions {
  display: flex;
  gap: 10px;
}
</style>
