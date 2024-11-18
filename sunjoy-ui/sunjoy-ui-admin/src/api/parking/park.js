import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

// 查询车场
export function listPark(query) {
  return request({
    url: '/parkmodel/park/list',
    method: 'get',
    params: query
  });
}

// 查询用户详细
export function getPark(parkId) {
  return request({
    url: '/parkmodel/park/' + parseStrEmpty(parkId),
    method: 'get'
  })
}

// 新增车场
export function addPark(data) {
  return request({
    url: '/parkmodel/park',
    method: 'post',
    data: data
  })
}

// 修改车场
export function updatePark(data) {
  return request({
    url: '/parkmodel/park',
    method: 'put',
    data: data
  })
}

// 删除用户
export function delPark(parkId) {
  return request({
    url: '/parkmodel/park/' + parkId,
    method: 'delete'
  })
}



// 用户状态修改
export function changeParkStatus(parkId, status) {
  const data = {
    parkId,
    status
  }
  return request({
    url: '/parkmodel/park/changeStatus',
    method: 'put',
    data: data
  })
}


// 查询部门下拉树结构
export function opuTreeSelect() {
  return request({
    url: '/parkmodel/park/opuTree',
    method: 'get'
  });
}

// 查询车场下拉树结构
export function getParkTree(opuId){
  return request({
    url: '/parkmodel/park/tree/' + parseStrEmpty(opuId),
    method: 'get'
  });
}


// 查询车场收费标准列表
export function getParkPrices(parkId) {
  return request({
    url: '/parkmodel/park/price/' + parseStrEmpty(parkId),
    method: 'get'
  })
}
