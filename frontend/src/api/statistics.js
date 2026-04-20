import request from '@/utils/request'

export const getHomeStatistics = () => {
  return request.get('/statistics/home')
}

export const getOrderStatusStatistics = () => {
  return request.get('/statistics/order-status')
}

export const getOrderDailyStatistics = (days) => {
  return request.get('/statistics/order-daily', { params: { days } })
}
