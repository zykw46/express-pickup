<template>
  <div class="delivery-income">
    <h2 class="page-title">我的收益</h2>
    
    <el-row :gutter="20" class="income-cards">
      <el-col :span="8">
        <el-card class="income-card">
          <div class="income-label">账户余额</div>
          <div class="income-value">¥{{ balance }}</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="income-card">
          <div class="income-label">今日收入</div>
          <div class="income-value">¥{{ todayIncome }}</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="income-card">
          <div class="income-label">累计收入</div>
          <div class="income-value">¥{{ totalIncome }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="flow-card">
      <template #header>
        <span>收支明细</span>
      </template>
      <el-table :data="flowList" v-loading="loading" border>
        <el-table-column prop="createTime" label="时间" width="180" />
        <el-table-column prop="type" label="类型" width="100">
          <template #default="{ row }">
            <el-tag :type="row.type === 3 ? 'success' : 'primary'">
              {{ row.type === 3 ? '收入' : '其他' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="amount" label="金额">
          <template #default="{ row }">
            <span :class="row.amount > 0 ? 'positive' : 'negative'">
              {{ row.amount > 0 ? '+' : '' }}¥{{ row.amount }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="balance" label="余额">
          <template #default="{ row }">
            ¥{{ row.balance }}
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" />
      </el-table>
      
      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :total="total"
        layout="prev, pager, next"
        class="pagination"
        @current-change="loadFlowData"
      />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getBalance, getFlowList } from '@/api/account'
import { getDeliveryOrders } from '@/api/order'

const balance = ref(0)
const todayIncome = ref(0)
const totalIncome = ref(0)
const flowList = ref([])
const loading = ref(false)
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

const loadBalance = async () => {
  try {
    const res = await getBalance()
    balance.value = res.data.balance
  } catch (error) {
    console.error(error)
  }
}

const loadIncome = async () => {
  try {
    const res = await getDeliveryOrders({ status: 2 })
    const orders = res.data
    totalIncome.value = orders.reduce((sum, order) => sum + parseFloat(order.deliveryIncome), 0).toFixed(2)
    
    const today = new Date().toDateString()
    const todayOrders = orders.filter(order => {
      return new Date(order.completeTime).toDateString() === today
    })
    todayIncome.value = todayOrders.reduce((sum, order) => sum + parseFloat(order.deliveryIncome), 0).toFixed(2)
  } catch (error) {
    console.error(error)
  }
}

const loadFlowData = async () => {
  loading.value = true
  try {
    const res = await getFlowList({
      pageNum: pageNum.value,
      pageSize: pageSize.value
    })
    flowList.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadBalance()
  loadIncome()
  loadFlowData()
})
</script>

<style scoped>
.delivery-income {
  padding: 20px;
}

.page-title {
  margin-bottom: 20px;
  color: #303133;
}

.income-cards {
  margin-bottom: 20px;
}

.income-card {
  text-align: center;
  padding: 20px;
}

.income-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 10px;
}

.income-value {
  font-size: 32px;
  font-weight: bold;
  color: #67C23A;
}

.flow-card {
  margin-top: 20px;
}

.positive {
  color: #67C23A;
}

.negative {
  color: #F56C6C;
}

.pagination {
  margin-top: 20px;
  justify-content: center;
}
</style>
