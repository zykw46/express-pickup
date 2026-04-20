import request from '@/utils/request'

export const getSpecList = () => {
  return request.get('/spec/list')
}

export const getActiveSpecList = () => {
  return request.get('/spec/active-list')
}

export const getSpecDetail = (id) => {
  return request.get(`/spec/detail/${id}`)
}

export const addSpec = (data) => {
  return request.post('/spec/add', data)
}

export const updateSpec = (data) => {
  return request.put('/spec/update', data)
}

export const deleteSpec = (id) => {
  return request.delete(`/spec/delete/${id}`)
}
