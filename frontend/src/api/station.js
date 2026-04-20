import request from '@/utils/request'

export const getStationList = () => {
  return request.get('/station/list')
}

export const getActiveStationList = () => {
  return request.get('/station/active-list')
}

export const getStationDetail = (id) => {
  return request.get(`/station/detail/${id}`)
}

export const addStation = (data) => {
  return request.post('/station/add', data)
}

export const updateStation = (data) => {
  return request.put('/station/update', data)
}

export const deleteStation = (id) => {
  return request.delete(`/station/delete/${id}`)
}
