<template>
  <div class="create-order">
    <h2 class="page-title">快递代取下单</h2>
    
    <el-card class="order-card">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="快递站点" prop="stationId">
          <el-select v-model="form.stationId" placeholder="请选择快递站点" style="width: 100%">
            <el-option
              v-for="station in stationList"
              :key="station.id"
              :label="station.stationName"
              :value="station.id"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="包裹规格" prop="specId">
          <el-select v-model="form.specId" placeholder="请选择包裹规格" style="width: 100%">
            <el-option
              v-for="spec in specList"
              :key="spec.id"
              :label="`${spec.specName} (+¥${spec.extraPrice})`"
              :value="spec.id"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="取件码" prop="pickupCode">
          <el-input v-model="form.pickupCode" placeholder="请输入取件码" />
        </el-form-item>
        
        <el-form-item label="配送地址" required>
          <el-row :gutter="10">
            <el-col :span="12">
              <el-form-item prop="buildingId" style="margin-bottom: 0;">
                <el-select v-model="form.buildingId" placeholder="选择宿舍楼" style="width: 100%">
                  <el-option
                    v-for="building in buildingList"
                    :key="building.id"
                    :label="building.buildingName"
                    :value="building.id"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item prop="roomNumber" style="margin-bottom: 0;">
                <el-input v-model="form.roomNumber" placeholder="房间号" />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form-item>
        
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" rows="3" placeholder="如有特殊要求请填写" />
        </el-form-item>
        
        <el-form-item label="订单预览">
          <div class="order-preview">
            <p>基础费用: ¥2.00</p>
            <p>规格加价: +¥{{ extraPrice }}</p>
            <p class="total">订单总价: ¥{{ totalPrice }}</p>
          </div>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" size="large" @click="handleSubmit" :loading="submitLoading">
            确认下单
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getActiveStationList } from '@/api/station'
import { getActiveSpecList } from '@/api/spec'
import { getActiveBuildingList } from '@/api/building'
import { createOrder, payOrder } from '@/api/order'

const router = useRouter()
const formRef = ref(null)
const submitLoading = ref(false)

const stationList = ref([])
const specList = ref([])
const buildingList = ref([])

const form = reactive({
  stationId: null,
  specId: null,
  pickupCode: '',
  buildingId: null,
  roomNumber: '',
  remark: ''
})

const rules = {
  stationId: [{ required: true, message: '请选择快递站点', trigger: 'change' }],
  specId: [{ required: true, message: '请选择包裹规格', trigger: 'change' }],
  pickupCode: [{ required: true, message: '请输入取件码', trigger: 'blur' }],
  buildingId: [{ required: true, message: '请选择宿舍楼', trigger: 'change' }],
  roomNumber: [{ required: true, message: '请输入房间号', trigger: 'blur' }]
}

const extraPrice = computed(() => {
  const spec = specList.value.find(s => s.id === form.specId)
  return spec ? spec.extraPrice : 0
})

const totalPrice = computed(() => {
  return (2 + parseFloat(extraPrice.value)).toFixed(2)
})

const loadData = async () => {
  try {
    const [stationRes, specRes, buildingRes] = await Promise.all([
      getActiveStationList(),
      getActiveSpecList(),
      getActiveBuildingList()
    ])
    stationList.value = stationRes.data
    specList.value = specRes.data
    buildingList.value = buildingRes.data
  } catch (error) {
    console.error(error)
  }
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  submitLoading.value = true
  try {
    const res = await createOrder(form)
    const orderId = res.data.id
    
    try {
      await payOrder(orderId)
      ElMessage.success('下单并支付成功')
      router.push('/user/order')
    } catch (error) {
      ElMessage.warning('订单创建成功，但支付失败，请前往订单页面支付')
      router.push('/user/order')
    }
  } catch (error) {
    console.error(error)
  } finally {
    submitLoading.value = false
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.create-order {
  padding: 20px 0;
}

.page-title {
  margin-bottom: 20px;
  color: #303133;
}

.order-card {
  max-width: 600px;
  margin: 0 auto;
}

.order-preview {
  padding: 15px;
  background: #f5f7fa;
  border-radius: 4px;
}

.order-preview p {
  margin: 5px 0;
  color: #606266;
}

.order-preview .total {
  font-size: 18px;
  font-weight: bold;
  color: #F56C6C;
  margin-top: 10px;
  padding-top: 10px;
  border-top: 1px solid #dcdfe6;
}
</style>
