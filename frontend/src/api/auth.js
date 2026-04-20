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
  return request.put('/auth/password', null, {
    params: { oldPassword, newPassword }
  })
}

export const getCaptcha = () => {
  return request.get('/captcha/generate')
}
