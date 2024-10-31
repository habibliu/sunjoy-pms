import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

// 新增通道
export function addLane(data) {
    return request({
      url: '/parkmodel/lane',
      method: 'post',
      data: data
    })
  }
  
  // 修改通道
  export function updateLane(data) {
    return request({
      url: '/parkmodel/lane',
      method: 'put',
      data: data
    })
  }
  
  // 删除通道
  export function delLane(parkId,laneIds) {
    return request({
      url: '/parkmodel/lane/'+parkId+'/' + laneIds,
      method: 'delete'
    })
  }
  
  //获取车场通道列表
export function getParkLaneList(parkId){
  
    return request({
      url: '/parkmodel/lane/list/' + parseStrEmpty(parkId),
      method: 'get'
     
    });
  }

//获取通道设备列表
export function getLaneDeviceList(parkId){
  
    return request({
      url: '/parkmodel/lane/device/list/' + parseStrEmpty(parkId),
      method: 'get'
     
    });
  }

// 保存通道设备关系
export function bindDevice(data) {
    return request({
      url: '/parkmodel/lane/device/bind',
      method: 'post',
      data: JSON.stringify(data)
    });
  }

// 解决通道设备关系
export function unbindDevice(data) {
    return request({
      url: '/parkmodel/lane/device/unbind/'+parseStrEmpty(data),
      method: 'delete'
    });
  }