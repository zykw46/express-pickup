import request from '@/utils/request'

export const getRoleList = () => {
  return request.get('/role/list')
}

export const getRoleDetail = (id) => {
  return request.get(`/role/detail/${id}`)
}

export const addRole = (data) => {
  return request.post('/role/add', data)
}

export const updateRole = (data) => {
  return request.put('/role/update', data)
}

export const deleteRole = (id) => {
  return request.delete(`/role/delete/${id}`)
}
