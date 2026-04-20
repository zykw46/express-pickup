import request from '@/utils/request'

export const getOrderList = (params) => {
  return request.get('/order/list', { params })
}

export const getPendingOrderList = (params) => {
  return request.get('/order/pending-list', { params })
}

export const getOrderDetail = (id) => {
  return request.get(`/order/detail/${id}`)
}

export const getMyOrders = (params) => {
  return request.get('/order/my-orders', { params })
}

export const getDeliveryOrders = (params) => {
  return request.get('/order/delivery-orders', { params })
}

export const createOrder = (data) => {
  return request.post('/order/create', data)
}

export const payOrder = (orderId) => {
  return request.post(`/order/pay/${orderId}`)
}

export const receiveOrder = (orderId) => {
  return request.post(`/order/receive/${orderId}`)
}

export const completeOrder = (orderId) => {
  return request.post(`/order/complete/${orderId}`)
}

export const confirmOrder = (orderId) => {
  return request.post(`/order/confirm/${orderId}`)
}

export const cancelOrder = (orderId, reason) => {
  return request.post(`/order/cancel/${orderId}`, null, {
    params: { reason }
  })
}

export const deleteOrder = (orderId) => {
  return request.delete(`/order/delete/${orderId}`)
}
