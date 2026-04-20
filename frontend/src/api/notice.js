import request from '@/utils/request'

export const getNoticeList = (params) => {
  return request.get('/notice/list', { params })
}

export const getActiveNoticeList = (limit) => {
  return request.get('/notice/active-list', { params: { limit } })
}

export const getNoticeDetail = (id) => {
  return request.get(`/notice/detail/${id}`)
}

export const addNotice = (data) => {
  return request.post('/notice/add', data)
}

export const updateNotice = (data) => {
  return request.put('/notice/update', data)
}

export const deleteNotice = (id) => {
  return request.delete(`/notice/delete/${id}`)
}
