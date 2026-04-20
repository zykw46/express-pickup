<template>
  <div class="user-recharge">
    <h2 class="page-title">账户充值</h2>
    
    <el-card class="balance-card">
      <div class="balance-info">
        <span class="label">当前余额</span>
        <span class="amount">¥{{ balance }}</span>
      </div>
    </el-card>

    <el-card class="recharge-card">
      <template #header>
        <span>选择充值金额</span>
      </template>
      
      <div class="amount-options">
        <div
          v-for="amount in amountOptions"
          :key="amount"
          class="amount-option"
          :class="{ active: selectedAmount === amount }"
          @click="selectedAmount = amount"
        >
          ¥{{ amount }}
        </div>
      </div>
      
      <div class="custom-amount">
        <span>自定义金额:</span>
        <el-input-number v-model="customAmount" :min="1" :precision="2" style="margin-left: 10px;" />
      </div>
      
      <el-button
        type="primary"
        size="large"
        class="recharge-btn"
        :loading="loading"
        @click="handleRecharge"
      >
        立即充值 ¥{{ finalAmount }}
      </el-button>
    </el-card>

    <el-card class="flow-card">
      <template #header>
        <span>充值记录</span>
      </template>
      <el-table :data="flowList" v-loading="flowLoading" border>
        <el-table-column prop="createTime" label="时间" />
        <el-table-column prop="amount" label="金额">
          <template #default="{ row }">
            <span class="positive">+¥{{ row.amount }}</span>
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
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getBalance, getFlowList, recharge } from '@/api/account'

const balance = ref(0)
const loading = ref(false)
const selectedAmount = ref(50)
const customAmount = ref(0)
const amountOptions = [10, 20, 50, 100, 200, 500]

const flowList = ref([])
const flowLoading = ref(false)
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

const finalAmount = computed(() => {
  return customAmount.value > 0 ? customAmount.value : selectedAmount.value
})

const loadBalance = async () => {
  try {
    const res = await getBalance()
    balance.value = res.data.balance
  } catch (error) {
    console.error(error)
  }
}

const loadFlowData = async () => {
  flowLoading.value = true
  try {
    const res = await getFlowList({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      type: 1
    })
    flowList.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    console.error(error)
  } finally {
    flowLoading.value = false
  }
}

const handleRecharge = async () => {
  if (finalAmount.value <= 0) {
    ElMessage.warning('请输入充值金额')
    return
  }
  
  loading.value = true
  try {
    await recharge(finalAmount.value)
    ElMessage.success('充值成功')
    loadBalance()
    loadFlowData()
    customAmount.value = 0
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadBalance()
  loadFlowData()
})
</script>

<style scoped>
.user-recharge {
  padding: 20px 0;
}

.page-title {
  margin-bottom: 20px;
  color: #303133;
}

.balance-card {
  margin-bottom: 20px;
}

.balance-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
}

.balance-info .label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 10px;
}

.balance-info .amount {
  font-size: 48px;
  font-weight: bold;
  color: #67C23A;
}

.recharge-card {
  margin-bottom: 20px;
}

.amount-options {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  margin-bottom: 20px;
}

.amount-option {
  width: 100px;
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 2px solid #dcdfe6;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
  font-size: 16px;
  font-weight: bold;
}

.amount-option:hover {
  border-color: #409EFF;
  color: #409EFF;
}

.amount-option.active {
  border-color: #409EFF;
  background-color: #ecf5ff;
  color: #409EFF;
}

.custom-amount {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
}

.recharge-btn {
  width: 100%;
}

.positive {
  color: #67C23A;
}

.pagination {
  margin-top: 20px;
  justify-content: center;
}
</style>
