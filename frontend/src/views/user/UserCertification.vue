<template>
  <div class="user-certification">
    <h2 class="page-title">配送员认证</h2>
    
    <el-card v-if="certification">
      <div class="cert-status">
        <el-result
          :icon="getStatusIcon(certification.status)"
          :title="getStatusTitle(certification.status)"
          :sub-title="getStatusDesc(certification.status)"
        />
        <div v-if="certification.status === 3" class="reject-reason">
          <el-alert
            :title="`拒绝原因: ${certification.rejectReason}`"
            type="error"
            :closable="false"
          />
        </div>
      </div>
    </el-card>

    <el-card v-if="!certification || certification.status === 0 || certification.status === 3" class="form-card">
      <template #header>
        <span>填写认证信息</span>
      </template>
      
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="form.realName" placeholder="请输入真实姓名" />
        </el-form-item>
        
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="form.idCard" placeholder="请输入身份证号" />
        </el-form-item>
        
        <el-form-item label="学号" prop="studentId">
          <el-input v-model="form.studentId" placeholder="请输入学号（选填）" />
        </el-form-item>
        
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
            提交认证
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getMyCertification, submitCertification } from '@/api/certification'

const certification = ref(null)
const formRef = ref(null)
const submitLoading = ref(false)

const form = reactive({
  realName: '',
  idCard: '',
  studentId: '',
  phone: ''
})

const rules = {
  realName: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }],
  idCard: [
    { required: true, message: '请输入身份证号', trigger: 'blur' },
    { pattern: /^\d{17}[\dXx]$/, message: '身份证号格式不正确', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ]
}

const getStatusIcon = (status) => {
  const icons = { 0: 'info', 1: 'warning', 2: 'success', 3: 'error' }
  return icons[status] || 'info'
}

const getStatusTitle = (status) => {
  const titles = { 0: '未认证', 1: '审核中', 2: '已通过', 3: '已拒绝' }
  return titles[status] || '未知'
}

const getStatusDesc = (status) => {
  const descs = {
    0: '请填写认证信息提交审核',
    1: '您的认证申请正在审核中，请耐心等待',
    2: '恭喜！您已通过配送员认证，可以开始接单了',
    3: '您的认证申请被拒绝，请修改后重新提交'
  }
  return descs[status] || ''
}

const loadData = async () => {
  try {
    const res = await getMyCertification()
    certification.value = res.data
  } catch (error) {
    console.error(error)
  }
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  submitLoading.value = true
  try {
    await submitCertification(form)
    ElMessage.success('提交成功，请等待审核')
    loadData()
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
.user-certification {
  padding: 20px 0;
}

.page-title {
  margin-bottom: 20px;
  color: #303133;
}

.cert-status {
  padding: 20px;
}

.reject-reason {
  max-width: 500px;
  margin: 0 auto;
}

.form-card {
  max-width: 600px;
  margin: 20px auto;
}
</style>
