import request from '@/utils/request'

export const getCertificationList = (params) => {
  return request.get('/certification/list', { params })
}

export const getCertificationDetail = (id) => {
  return request.get(`/certification/detail/${id}`)
}

export const getMyCertification = () => {
  return request.get('/certification/my')
}

export const submitCertification = (data) => {
  return request.post('/certification/submit', data)
}

export const auditCertification = (id, status, rejectReason) => {
  return request.post(`/certification/audit/${id}`, null, {
    params: { status, rejectReason }
  })
}

export const deleteCertification = (id) => {
  return request.delete(`/certification/delete/${id}`)
}
