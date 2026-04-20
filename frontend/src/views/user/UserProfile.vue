<template>
  <div class="user-profile">
    <h2 class="page-title">个人中心</h2>
    
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>基本信息</span>
          </template>
          
          <div class="avatar-section">
            <el-avatar :size="100" :src="getAvatarUrl(form.avatar)" />
            <el-upload
              ref="uploadRef"
              :show-file-list="false"
              :before-upload="beforeUpload"
              :http-request="customUpload"
              accept="image/jpeg,image/jpg,image/png,image/gif,image/webp"
              class="avatar-uploader"
            >
              <el-button type="primary" size="small" style="margin-left: 20px;">
                更换头像
              </el-button>
            </el-upload>
            <div class="avatar-tip">支持 jpg、png、gif 格式，大小不超过 5MB</div>
          </div>

          <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
            <el-form-item label="用户名">
              <el-input v-model="form.username" disabled />
            </el-form-item>
            
            <el-form-item label="昵称" prop="nickname">
              <el-input v-model="form.nickname" />
            </el-form-item>
            
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="form.phone" />
            </el-form-item>
            
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" />
            </el-form-item>
            
            <el-form-item>
              <el-button type="primary" @click="handleUpdate" :loading="updateLoading">
                保存修改
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>修改密码</span>
          </template>
          
          <el-form ref="passwordFormRef" :model="passwordForm" :rules="passwordRules" label-width="100px">
            <el-form-item label="原密码" prop="oldPassword">
              <el-input v-model="passwordForm.oldPassword" type="password" />
            </el-form-item>
            
            <el-form-item label="新密码" prop="newPassword">
              <el-input v-model="passwordForm.newPassword" type="password" />
            </el-form-item>
            
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input v-model="passwordForm.confirmPassword" type="password" />
            </el-form-item>
            
            <el-form-item>
              <el-button type="primary" @click="handleChangePassword" :loading="passwordLoading">
                修改密码
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/store/user'
import { getUserInfo, updateProfile, resetPassword, uploadAvatar } from '@/api/auth'

const userStore = useUserStore()
const formRef = ref(null)
const passwordFormRef = ref(null)
const uploadRef = ref(null)
const updateLoading = ref(false)
const passwordLoading = ref(false)
const uploadLoading = ref(false)

const form = reactive({
  username: '',
  nickname: '',
  phone: '',
  email: '',
  avatar: ''
})

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
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

const rules = {
  nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
  ]
}

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== passwordForm.newPassword) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const passwordRules = {
  oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

const beforeUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt5M = file.size / 1024 / 1024 < 5

  if (!isImage) {
    ElMessage.error('只能上传图片文件！')
    return false
  }
  if (!isLt5M) {
    ElMessage.error('图片大小不能超过 5MB！')
    return false
  }
  return true
}

const customUpload = async (options) => {
  const { file } = options
  uploadLoading.value = true

  try {
    const res = await uploadAvatar(file)
    if (res.code === 200) {
      ElMessage.success('头像上传成功')
      form.avatar = res.data.url
      await loadUserInfo()
      userStore.setUserInfo({
        ...userStore.userInfo,
        avatar: res.data.url
      })
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('上传失败，请重试')
  } finally {
    uploadLoading.value = false
  }
}

const loadUserInfo = async () => {
  try {
    const res = await getUserInfo()
    const data = res.data
    Object.assign(form, {
      username: data.username,
      nickname: data.nickname,
      phone: data.phone,
      email: data.email,
      avatar: data.avatar
    })
  } catch (error) {
    console.error(error)
  }
}

const handleUpdate = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  updateLoading.value = true
  try {
    await updateProfile(form)
    ElMessage.success('修改成功')
    const res = await getUserInfo()
    userStore.setUserInfo(res.data)
  } catch (error) {
    console.error(error)
  } finally {
    updateLoading.value = false
  }
}

const handleChangePassword = async () => {
  const valid = await passwordFormRef.value.validate().catch(() => false)
  if (!valid) return

  passwordLoading.value = true
  try {
    await resetPassword(passwordForm.oldPassword, passwordForm.newPassword)
    ElMessage.success('密码修改成功')
    passwordForm.oldPassword = ''
    passwordForm.newPassword = ''
    passwordForm.confirmPassword = ''
  } catch (error) {
    console.error(error)
  } finally {
    passwordLoading.value = false
  }
}

onMounted(() => {
  loadUserInfo()
})
</script>

<style scoped>
.user-profile {
  padding: 20px 0;
}

.page-title {
  margin-bottom: 20px;
  color: #303133;
}

.avatar-section {
  display: flex;
  align-items: center;
  margin-bottom: 30px;
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 8px;
}

.avatar-uploader {
  display: inline-block;
}

.avatar-tip {
  margin-left: 20px;
  font-size: 12px;
  color: #909399;
}
</style>
