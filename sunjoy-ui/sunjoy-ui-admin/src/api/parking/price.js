import request from '@/utils/request';
import { parseStrEmpty } from "@/utils/ruoyi";

// 查询收费标准
export function listPrice(query) {
  return request({
    url: '/parkmodel/price/list',
    method: 'get',
    params: query
  });
}

// 查询收费标准详细
export function getPrice(priceId) {
  return request({
    url: '/parkmodel/price/' + parseStrEmpty(priceId),
    method: 'get'
  })
}

// 新增收费标准
export function addPrice(data) {
  return request({
    url: '/parkmodel/price',
    method: 'post',
    data: data
  })
}

// 修改收费标准
export function updatePrice(data) {
  return request({
    url: '/parkmodel/price',
    method: 'put',
    data: data
  })
}

// 删除收费标准
export function delPrice(priceId) {
  return request({
    url: '/parkmodel/price/' + priceId,
    method: 'delete'
  })
}



// 收费标准状态修改
export function changePriceStatus(priceId, status) {
  const data = {
    priceId,
    status
  }
  return request({
    url: '/parkmodel/price/changeStatus',
    method: 'put',
    data: data
  })
}
//---------------------------------明细处理
// 新增收费标准明细
export function addDetail(data) {
    return request({
      url: '/parkmodel/price/detail',
      method: 'post',
      data: data
    })
  }

// 修改收费标准明细
export function updateDetail(data) {
    return request({
      url: '/parkmodel/price/detail',
      method: 'put',
      data: data
    })
  }

// 删除收费标准明细
export function deleteDetail(id) {
    return request({
      url: '/parkmodel/price/detail/' + id,
      method: 'delete'
    })
  }

// 查询收费标准明细列表
export function getDetailList(priceId) {
    return request({
      url: '/parkmodel/price/detail/' + parseStrEmpty(priceId),
      method: 'get'
    })
  }