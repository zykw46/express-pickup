import request from '@/utils/request'

export const getBuildingList = () => {
  return request.get('/building/list')
}

export const getActiveBuildingList = () => {
  return request.get('/building/active-list')
}

export const getBuildingDetail = (id) => {
  return request.get(`/building/detail/${id}`)
}

export const addBuilding = (data) => {
  return request.post('/building/add', data)
}

export const updateBuilding = (data) => {
  return request.put('/building/update', data)
}

export const deleteBuilding = (id) => {
  return request.delete(`/building/delete/${id}`)
}
