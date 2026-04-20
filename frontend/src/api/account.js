import request from '@/utils/request'

export const getBalance = () => {
  return request.get('/account/balance')
}

export const getFlowList = (params) => {
  return request.get('/account/flow', { params })
}

export const recharge = (amount) => {
  return request.post('/account/recharge', null, {
    params: { amount }
  })
}
