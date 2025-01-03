import request from '@/utils/request'
import { parseStrEmpty } from '@/utils/ruoyi'

// 查询车辆
export function listVehicle(query) {
  return request({
    url: '/parkmodel/vehicle/list',
    method: 'get',
    params: query
  });
}

// 新增车辆档案
export function addVehicle(data) {
  return request({
    url: '/parkmodel/vehicle',
    method: 'post',
    data: data
  })
}
// 修改车辆档案
export function updateVehicle(data) {
  return request({
    url: '/parkmodel/vehicle',
    method: 'put',
    data: data
  })
}
// 获取车辆档案
export function getVehicle(vehicleId) {
  return request({
    url: '/parkmodel/vehicle/' + parseStrEmpty(vehicleId),
    method: 'get'

  })
}

// －－－－－－－－－－－－服务处理
// 新增收费标准
export function addService(data) {
  return request({
    url: '/parkmodel/vehicle/service',
    method: 'post',
    data: data
  })
}

// 修改收费标准
export function updateService(data) {
  return request({
    url: '/parkmodel/vehicle/service',
    method: 'put',
    data: data
  })
}

// 删除收费标准
export function deleteVehicleService(vehicleId, serviceIds) {
  return request({
    url: '/parkmodel/vehicle/service/' + vehicleId + '/' + serviceIds,
    method: 'delete'
  })
}
/**
 * 获取车辆的收费标准
 * @param {*} vehicleId
 */
export function getVehicleService(vehicleId) {
  return request({
    url: '/parkmodel/vehicle/service/' + parseStrEmpty(vehicleId),
    method: 'get'
  })
}

/**
 * 变更车辆状态
 * @param {} id
 * @returns
 */
export function changeVehicleStatus(vehicleId, status) {
  return request({
    url: '/parkmodel/vehicle/' + parseStrEmpty(vehicleId) + '/' + parseStrEmpty(status),
    method: 'put'
  })
}

/**
 * 变更车辆服务状态
 * @param {} id
 * @returns
 */
export function changeVehicleServiceStatus(data) {
  return request({
    url: '/parkmodel/vehicle/service',
    method: 'put',
    data: data
  })
}
/**
 * 获取收费标准信息
 * @param {*} parkId
 * @param {*} serviceId
 * @returns
 */
export function getPriceInfo(parkId, serviceId) {
  return request({
    url: '/parkmodel/vehicle/price/' + parseStrEmpty(parkId) + '/' + parseStrEmpty(serviceId),
    method: 'get'
  })
}

/**
 * 生成登记车辆预付费服务订单
 * @param {*} order
 * @returns
 */
export function createPaymentOrder(order){
  return request({
    url: '/parkmodel/vehicle/service/order',
    method: 'post',
    data: order
  })
}
