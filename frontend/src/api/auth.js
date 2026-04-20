import request from '@/utils/request'

export const login = (data) => {
  return request.post('/auth/login', data)
}

export const register = (data) => {
  return request.post('/auth/register', data)
}

export const logout = () => {
  return request.post('/auth/logout')
}

export const getUserInfo = () => {
  return request.get('/auth/info')
}

export const updateProfile = (data) => {
  return request.put('/auth/profile', data)
}

export const resetPassword = (oldPassword, newPassword) => {
  return request.post('/auth/reset-password', null, {
    params: { oldPassword, newPassword }
  })
}

export const uploadAvatar = (file) => {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/upload/avatar', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

export const getCaptcha = () => {
  return request.get('/captcha/generate')
}
