import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

// 查询车场
export function listDevice(query) {
  return request({
    url: '/parkmodel/device/list',
    method: 'get',
    params: query
  });
}

// 查询用户详细
export function getDevice(deviceId) {
  return request({
    url: '/parkmodel/device/' + parseStrEmpty(deviceId),
    method: 'get'
  })
}

// 新增用户
export function addDevice(data) {
  return request({
    url: '/parkmodel/device',
    method: 'post',
    data: data
  })
}

// 修改用户
export function updateDevice(data) {
  return request({
    url: '/parkmodel/device',
    method: 'put',
    data: data
  })
}

// 删除用户
export function delDevice(deviceId) {
  return request({
    url: '/parkmodel/device/' + deviceId,
    method: 'delete'
  })
}



// 用户状态修改
export function changeDeviceStatus(deviceId, status) {
  const data = {
    deviceId,
    status
  }
  return request({
    url: '/parkmodel/device/changeStatus',
    method: 'put',
    data: data
  })
}


//获取车场通道列表
export function getDeviceLaneList(deviceId){
  
  return request({
    url: '/parkmodel/park/parklane/list/' + parseStrEmpty(deviceId),
    method: 'get'
   
  });
}