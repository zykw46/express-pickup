import request from '@/utils/request'

export const getUserList = (params) => {
  return request.get('/user/list', { params })
}

export const getUserDetail = (id) => {
  return request.get(`/user/detail/${id}`)
}

export const addUser = (data) => {
  return request.post('/user/add', data)
}

export const updateUser = (data) => {
  return request.put('/user/update', data)
}

export const deleteUser = (id) => {
  return request.delete(`/user/delete/${id}`)
}
